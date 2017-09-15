package sk.dominika.dluhy.activities;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import sk.dominika.dluhy.network.NetworkChangeReceiver;
import sk.dominika.dluhy.utilities.Utility;

/**
 * In the SignUpActivity a new user can be created, authenticated and added to database of users.
 * Authentication with firebase works here in the same way as in the LogInActivity.
 * The Activity has input TextViews for first name, last name, email and password.
 * For successful registration all fields must be completed.
 */
public class SignUpActivity extends AppCompatActivity {

    private User user;

    private TextInputEditText firstName, lastName, emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set XML layout the activity will be using
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign up");

        //reference to views and listeners for detecting errors
        firstName = (TextInputEditText) findViewById(R.id.text_input_signUp_firstname);
        lastName = (TextInputEditText) findViewById(R.id.text_input_signUp_lastname);
        emailInput = (TextInputEditText) findViewById(R.id.text_input_signUp_email);
        passwordInput = (TextInputEditText) findViewById(R.id.text_input_signUp_password);

        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (firstName.getText().toString().equals("")) {
                    firstName.setError(getBaseContext().getResources().getString(R.string.firstname_required));
                }
                else {
                    firstName.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (lastName.getText().toString().equals("")) {
                    lastName.setError(getBaseContext().getResources().getString(R.string.lastname_required));
                }
                else {
                    lastName.setError(null);
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
                    emailInput.setError(getBaseContext().getResources().getString(R.string.email_required));
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
                    passwordInput.setError(getBaseContext().getResources().getString(R.string.password_required));
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

                if (firstName.getText().toString().equals("") ||
                        lastName.getText().toString().equals("") ||
                        emailInput.getText().toString().equals("") ||
                        passwordInput.getText().toString().equals("")) {
                    //if any text field was not completed, display error messages
                    ShowAlertDialogNeutral.showAlertDialog("You must complete text fields", SignUpActivity.this);
                    firstName.setError(getBaseContext().getResources().getString(R.string.firstname_required));
                    lastName.setError(getBaseContext().getResources().getString(R.string.lastname_required));
                    emailInput.setError(getBaseContext().getResources().getString(R.string.email_required));
                    passwordInput.setError(getBaseContext().getResources().getString(R.string.password_required));
                    newAccountCreated.setEnabled(true);
                }
                else {
                    if (NetworkChangeReceiver.isConnected(SignUpActivity.this)) {
                        //all input fields were completed, create an user
                        user = new User("", firstName.getText().toString(),
                                lastName.getText().toString(), emailInput.getText().toString());
                        //set current user
                        CurrentUser.setData(user.getFirstname(), user.getLastname(), user.getEmail());
                        createUser(user.getEmail(), passwordInput.getText().toString());

                        newAccountCreated.setEnabled(true);
                    } else {
                        ShowAlertDialogNeutral.showAlertDialog(
                                getBaseContext().getResources().getString(R.string.log_no_connection),
                                SignUpActivity.this);
                        newAccountCreated.setEnabled(true);
                    }

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
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (currentUser != null) {
                                //get his new id
                                String id = currentUser.getUid();
                                CurrentUser.setId(id);
                                //set id to the user object and to CurrentUser.UserCurrent static class
                                user.setId(id);
                                //add user to database
                                addNewUser(user);
                                //start his profile activity
                                toMyProfileActivity();
                            } else {
                                Toast.makeText(SignUpActivity.this, R.string.sign_again, Toast.LENGTH_SHORT).show();
                            }
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