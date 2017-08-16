package sk.dominika.dluhy.activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.databases_objects.CurrentUser;
import sk.dominika.dluhy.databases_objects.User;

public class SignUpActivity extends AppCompatActivity {

    public final String TAG = "firebase_registration";

    private FirebaseAuth mAuth2;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private User user;

    private CurrentUser userCurrent;

    private TextInputEditText firstname, lastname, emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("Sign up");

        /**
         * FirebaseAut instance.
         */
        mAuth2 = FirebaseAuth.getInstance();


        final Button newAccountCreated = (Button) findViewById(R.id.button_signUp);
        newAccountCreated.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                newAccountCreated.setEnabled(false);

                firstname = (TextInputEditText) findViewById(R.id.text_input_signUp_firstname);
                lastname = (TextInputEditText) findViewById(R.id.text_input_signUp_lastname);
                emailInput = (TextInputEditText) findViewById(R.id.text_input_signUp_email);
                passwordInput = (TextInputEditText) findViewById(R.id.text_input_signUp_password);

                user = new User("",firstname.getText().toString(), lastname.getText().toString(), emailInput.getText().toString());

                //set current user
                CurrentUser.setData(user.getFirstname(), user.getLastname(), user.getEmail());

                createUser(user.getEmail(), passwordInput.getText().toString());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        findViewById(R.id.button_signUp).setEnabled(true);
    }

    /**
     * Validate emailInput and passwordInput and create a new user
     * @param email
     * @param password
     */
    private void createUser(final String email, final String password){
        mAuth2.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser currentUser = mAuth2.getCurrentUser();
                            String id = currentUser.getUid();
                            CurrentUser.setId(id);
                            user.setId(id);
                            addNewUser(user);
                            Log.d(TAG, "user successfully created and added to database ");

                            newAcitivity_main();
                        }
                        else {
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                //sem to nepada ????????
                                passwordInput.setError(getString(R.string.error_weak_password));
                                passwordInput.requestFocus();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                emailInput.setError(getString(R.string.error_invalid_email));
                                emailInput.requestFocus();
                            } catch(FirebaseAuthUserCollisionException e) {
                                emailInput.setError(getString(R.string.error_user_exists));
                                emailInput.requestFocus();
                            } catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                                passwordInput.setError(getString(R.string.error_weak_password));
                                passwordInput.requestFocus();
                            }
                        }
                    }
                });
    }

    /**
     * Add new user to Firebase database.
     * @param user User to be added.
     */
    private void addNewUser(User user) {
        //get instance to database
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        // get reference to 'users' node
        DatabaseReference ref = mDatabase.getReference("users");
        //get a reference to location id and set the data at this location to the given value
        ref.child(user.getId()).setValue(user);
    }

    private void newAcitivity_main(){
        Intent mainActivity = new Intent(this,MainActivity.class);
        startActivity(mainActivity);
    }
}
