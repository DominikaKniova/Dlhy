package sk.dominika.dluhy.activities;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.databases_objects.CurrentUser;
import sk.dominika.dluhy.databases_objects.User;

public class SignUpActivity extends AppCompatActivity {

    public final String TAG = "signing";

    private FirebaseAuth mAuth2;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private User user;

    private CurrentUser userCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("Sign up");

        /**
         * FirebaseAut instance.
         */
        mAuth2 = FirebaseAuth.getInstance();

        /**
         * method tracking whenever the user signs in or out
         */
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if(currentUser != null) {
//                    String id = currentUser.getUid();
//                    CurrentUser.setId(id);
//                    user.setId(id);
//                    addNewUser(user);
                    Toast.makeText(SignUpActivity.this, "auth successed",
                            Toast.LENGTH_LONG).show();
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Toast.makeText(SignUpActivity.this, "nepodarilo sa zaregistrovat", Toast.LENGTH_SHORT);

                }
            }
        };

        Button newAccountCreated = (Button) findViewById(R.id.button_signUp);
        newAccountCreated.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                TextInputEditText firstname = (TextInputEditText) findViewById(R.id.text_input_signUp_firstname);
                TextInputEditText lastname = (TextInputEditText) findViewById(R.id.text_input_signUp_lastname);
                TextInputEditText email = (TextInputEditText) findViewById(R.id.text_input_signUp_email);
                TextInputEditText password = (TextInputEditText) findViewById(R.id.text_input_signUp_password);

                user = new User("",firstname.getText().toString(), lastname.getText().toString(), email.getText().toString());

                createUser(user.getEmail(), password.getText().toString());

                //set current user
                CurrentUser.setData(user.getFirstname(), user.getLastname(), user.getEmail());

//                FirebaseUser currentUser = mAuth2.getCurrentUser();
//                while (currentUser == null) {
//                    currentUser = mAuth2.getCurrentUser();
//                }
//                String id = currentUser.getUid();
//                CurrentUser.setId(id);
//                user.setId(id);
//                addNewUser(user);

                newAcitivity_main(view);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth2.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mAuthListener != null) {
            mAuth2.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Validate email and password and create a new user
     * @param email
     * @param password
     */
    private void createUser(String email, String password){
        mAuth2.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        Log.d(TAG,"zavolal sa onComplete listener");

                        Toast.makeText(SignUpActivity.this, "auth",
                                Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "auth failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                }).addOnSuccessListener(SignUpActivity.this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser currentUser = mAuth2.getCurrentUser();
                String id = currentUser.getUid();
                CurrentUser.setId(id);
                user.setId(id);
                addNewUser(user);
                Log.d(TAG,"zavolal sa onSuccess listener");
            }
        }).addOnFailureListener(SignUpActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "zavolal sa onFailure listener");
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

    private void newAcitivity_main(View view){
        Intent mainActivity = new Intent(this,MainActivity.class);
        startActivity(mainActivity);
    }
}
