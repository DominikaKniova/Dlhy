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
import com.google.firebase.database.FirebaseDatabase;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.database_models.CurrentUser;
import sk.dominika.dluhy.database_models.User;
import sk.dominika.dluhy.dialogs.ShowAlertDialogNeutral;
import sk.dominika.dluhy.utilities.Utility;

/**
 * In the SignUpActivity a new user can be created, authenticated and added to database of users.
 * Authentication with firebase works here in the same way as in the LogInActivity.
 * The Activity has input TextViews for first name, last name, email and password.
 * For successful registration all fields must be completed.
 */
public class SignUpActivity extends AppCompatActivity {

    public final String TAG = "firebase_registration";

    private User user;

    private TextInputEditText firstname, lastname, emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set XML layout the activity will be using
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign up");

        //reference to views and listeners for detecting errors
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

        //set up touch listener for non-text views to hide keyboard when touched outside a TextView
        Utility.handleSoftKeyboard(findViewById(R.id.lin_layout_signup), SignUpActivity.this);

        final Button newAccountCreated = (Button) findViewById(R.id.button_signUp);
        newAccountCreated.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                newAccountCreated.setEnabled(false);

                if (firstname.getText().toString().equals("") ||
                        lastname.getText().toString().equals("") ||
                        emailInput.getText().toString().equals("") ||
                        passwordInput.getText().toString().equals("")) {
                    //if any text field was not completed, display error messages
                    ShowAlertDialogNeutral.showAlertDialog("You must complete text fields", SignUpActivity.this);
                    firstname.setError("First name is required");
                    lastname.setError("Last name is required");
                    emailInput.setError("Email is required");
                    passwordInput.setError("Password is required");
                    newAccountCreated.setEnabled(true);
                }
                else {
                    //all input fields were completed, create an user
                    user = new User("", firstname.getText().toString(),
                            lastname.getText().toString(), emailInput.getText().toString());
                    //set current user
                    CurrentUser.setData(user.getFirstname(), user.getLastname(), user.getEmail());
                    createUser(user.getEmail(), passwordInput.getText().toString());

                    newAccountCreated.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        findViewById(R.id.button_signUp).setEnabled(true);
    }

    /**
     * Validate email and password, and if no exception occurs create a new user.
     * If registration is successful, go to MyProfileActivity.
     * @param email Entered email.
     * @param password Entered password.
     */
    private void createUser(final String email, final String password){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //get his new id
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String id = currentUser.getUid();
                            CurrentUser.setId(id);
                            //set id to the user object and to CurrentUser.UserCurrent static class
                            user.setId(id);
                            //add user to database
                            addNewUser(user);
                            Log.d(TAG, "user successfully created and added to database ");
                            //start his profile activity
                            toMyProfileActivity();
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
     * Add a new user to database.
     * @param user User to be added.
     */
    private void addNewUser(User user) {
        //get instance to database and to 'users' node
        //get a reference to location 'id' and add the user to this location
        FirebaseDatabase.getInstance().getReference("users").child(user.getId()).setValue(user);
    }

    /**
     * Start MyProfileActivity.
     */
    private void toMyProfileActivity() {
        Intent mainActivity = new Intent(this, MyProfileActivity.class);
        startActivity(mainActivity);
    }
}
