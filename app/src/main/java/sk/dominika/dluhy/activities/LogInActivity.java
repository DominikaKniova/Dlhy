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
import com.google.firebase.auth.FirebaseUser;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.database_models.CurrentUser;
import sk.dominika.dluhy.dialogs.ShowAlertDialogNeutral;
import sk.dominika.dluhy.notifications.MyAlarmManager;
import sk.dominika.dluhy.utilities.Utility;


public class LogInActivity extends AppCompatActivity {

    public final String TAG = "firebase_log";

    private FirebaseAuth mAuth;

    private TextInputEditText emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        setTitle("Log in");

        /**
         * Firebase authentication
         */
        // FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        emailInput = (TextInputEditText) findViewById(R.id.text_input_logIn_email);
        passwordInput = (TextInputEditText) findViewById(R.id.text_input_logIn_password);

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (emailInput.getText().toString().equals("")){
                    emailInput.setError("Email is required");
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
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button signIn = (Button) findViewById(R.id.button_signUp);
        signIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                newAcitivity_signIn(view);
            }
        });

        final Button toMain = (Button) findViewById(R.id.button_logIn);
        toMain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                toMain.setEnabled(false);
                if (emailInput.getText().toString().equals("") || passwordInput.getText().toString().equals("")){
                    ShowAlertDialogNeutral.showAlertDialog("You must complete text fields", LogInActivity.this);
                    toMain.setEnabled(true);
                    emailInput.setError("Email is required");
                    passwordInput.setError("Password is required");
                }
                else {
                    logIn(emailInput.getText().toString(), passwordInput.getText().toString());
                    toMain.setEnabled(true);
                }

            }
        });

        //Set up touch listener for non-text box views to hide keyboard.
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
     * Validates emailInput and passwordInput and logs in an existing user
     * @param email
     * @param password
     */
    private void logIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            CurrentUser.setId(user.getUid());
                            //create all user's notifications
                            MyAlarmManager.createNotifications(getBaseContext());
                            newAcitivity_main();
                        }
                        else {
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                Log.e(TAG, e.getMessage());
                                emailInput.setText("");
                                passwordInput.setText("");

                                ShowAlertDialogNeutral.showAlertDialog("Email or password not valid.", LogInActivity.this);
                            } catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                                emailInput.setText("");
                                passwordInput.setText("");

                                ShowAlertDialogNeutral.showAlertDialog("Enter email and password again.", LogInActivity.this);
                            }
                        }
                    }
                });
    }

    private void newAcitivity_signIn(View view){
        Intent SignInActivity = new Intent(this, SignUpActivity.class);
        startActivity(SignInActivity);
    }

    private void newAcitivity_main(){
        Intent mainActivity = new Intent(this,MainActivity.class);
        startActivity(mainActivity);
    }

    @Override
    public void onBackPressed() {
    }
}
