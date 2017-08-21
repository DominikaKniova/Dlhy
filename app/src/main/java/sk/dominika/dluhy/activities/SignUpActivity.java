package sk.dominika.dluhy.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
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
import sk.dominika.dluhy.database_models.CurrentUser;
import sk.dominika.dluhy.database_models.User;
import sk.dominika.dluhy.dialogs.ShowAlertDialog;

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

        firstname = (TextInputEditText) findViewById(R.id.text_input_signUp_firstname);
        lastname = (TextInputEditText) findViewById(R.id.text_input_signUp_lastname);
        emailInput = (TextInputEditText) findViewById(R.id.text_input_signUp_email);
        passwordInput = (TextInputEditText) findViewById(R.id.text_input_signUp_password);

        firstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (firstname.getText().toString().equals("")){
                    firstname.setError("First name is required");
                }
                else {
                    firstname.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (lastname.getText().toString().equals("")){
                    lastname.setError("Last name is required");
                }
                else {
                    lastname.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (emailInput.getText().toString().equals("")){
                    emailInput.setError("Email is required");
                }
                else {
                    emailInput.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (passwordInput.getText().toString().equals("")){
                    passwordInput.setError("Password is required");
                }
                else {
                    passwordInput.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final Button newAccountCreated = (Button) findViewById(R.id.button_signUp);
        newAccountCreated.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                newAccountCreated.setEnabled(false);

                if (firstname.getText().toString().equals("") ||
                        lastname.getText().toString().equals("") ||
                        emailInput.getText().toString().equals("") ||
                        passwordInput.getText().toString().equals("")) {
                    ShowAlertDialog.showAlertDialog("You must complete text fields", SignUpActivity.this);
                    firstname.setError("First name is required");
                    lastname.setError("Last name is required");
                    emailInput.setError("Email is required");
                    passwordInput.setError("Password is required");
                    newAccountCreated.setEnabled(true);
                }
                else {
                    user = new User("",firstname.getText().toString(), lastname.getText().toString(), emailInput.getText().toString());

                    //set current user
                    CurrentUser.setData(user.getFirstname(), user.getLastname(), user.getEmail());

                    createUser(user.getEmail(), passwordInput.getText().toString());

                    newAccountCreated.setEnabled(true);
                }
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
