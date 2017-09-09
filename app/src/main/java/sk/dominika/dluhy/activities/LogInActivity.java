package sk.dominika.dluhy.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.database_models.CurrentUser;
import sk.dominika.dluhy.dialogs.ShowAlertDialogNeutral;
import sk.dominika.dluhy.notifications.MyNotificationManager;
import sk.dominika.dluhy.utilities.Utility;

/**
 * In the LogInActivity user can log in to the application by authentication in firebase.
 * Authentication gives him the permission to read and write do firebase database.
 * Authentication is successful only when user entered the valid email and password, which he
 * has chosen when registering to the app. Any invalid inputs will be notified.
 * After the user is successfully logged in, the MyProfileActivity is started.
 */
public class LogInActivity extends AppCompatActivity {

    private TextInputEditText emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set XML layout the activity will be using
        setContentView(R.layout.activity_log_in);
        setTitle("Log in");

        //reference to views and listeners for detecting errors
        emailInput = (TextInputEditText) findViewById(R.id.textinput_logIn_email);
        passwordInput = (TextInputEditText) findViewById(R.id.textinput_logIn_password);
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (emailInput.getText().toString().equals("")){
                    emailInput.setError(Resources.getSystem().getString(R.string.email_required));
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
                    passwordInput.setError(Resources.getSystem().getString(R.string.password_required));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //button for signing up/registration
        Button signIn = (Button) findViewById(R.id.button_signUp);
        signIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                toSignUpActivity();
            }
        });

        //button for login in. Starts MyProfileActivity if authentication was successful
        final Button toMyProfile = (Button) findViewById(R.id.button_logIn);
        toMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMyProfile.setEnabled(false);
                if (emailInput.getText().toString().equals("") || passwordInput.getText().toString().equals("")){
                    //inputs were completed incorrectly
                    ShowAlertDialogNeutral.showAlertDialog("You must complete text fields", LogInActivity.this);
                    toMyProfile.setEnabled(true);
                    emailInput.setError(Resources.getSystem().getString(R.string.email_required));
                    passwordInput.setError(Resources.getSystem().getString(R.string.password_required));
                }
                else {
                    //correctly completed inputs
                    //Log in
                    toMyProfile.setEnabled(true);
                    logIn(emailInput.getText().toString(), passwordInput.getText().toString());
                }

            }
        });

        //set up touch listener for non-text views to hide keyboard when touched outside a textview
        Utility.handleSoftKeyboard(findViewById(R.id.lin_layout_login), LogInActivity.this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        (findViewById(R.id.button_logIn)).setEnabled(true);
    }

    /**
     * Validates email and password and if the authentication is successful, then log the user in
     * and start MyProfileActivity.
     * @param email Entered email in TextView.
     * @param password Entered password in TextView.
     */
    private void logIn(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (currentUser != null) {
                                //set id of current user in static class CurrentUser.UserCurrent
                                CurrentUser.setId(currentUser.getUid());
                                //create all user's notifications
                                MyNotificationManager.createNotifications(getBaseContext());
                                //user is logged in, go to his profile
                                toMyProfileActivity();
                            } else {
                                Toast.makeText(LogInActivity.this, R.string.log_again, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            //authentication not successful
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                emailInput.setText("");
                                passwordInput.setText("");
                                ShowAlertDialogNeutral.showAlertDialog("Email or password not valid.", LogInActivity.this);

                            } catch(Exception e) {
                                emailInput.setText("");
                                passwordInput.setText("");
                                ShowAlertDialogNeutral.showAlertDialog("Enter email and password again.", LogInActivity.this);
                            }
                        }
                    }
                });
    }

    /**
     * Start SignUpActivity.
     */
    private void toSignUpActivity() {
        Intent SignInActivity = new Intent(this, SignUpActivity.class);
        startActivity(SignInActivity);
    }

    /**
     * Start MyProfileActivity.
     */
    private void toMyProfileActivity() {
        Intent myProfileActivity = new Intent(this, MyProfileActivity.class);
        startActivity(myProfileActivity);
    }

    /**
     * When back button pressed, do nothing.
     */
    @Override
    public void onBackPressed() {
    }
}