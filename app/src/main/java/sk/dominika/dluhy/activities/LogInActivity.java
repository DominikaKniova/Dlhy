package sk.dominika.dluhy.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sk.dominika.dluhy.R;


public class LogInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

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
        //method tracking whenever the user signs in or out
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    // Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    // Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        Button signIn = (Button) findViewById(R.id.button_signUp);
        signIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                newAcitivity_signIn(view);
            }
        });

        Button toMain = (Button) findViewById(R.id.button_logIn);
        toMain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                TextInputEditText email = (TextInputEditText) findViewById(R.id.text_input_logIn_email);
                TextInputEditText password = (TextInputEditText) findViewById(R.id.text_input_logIn_password);
                logIn(email.getText().toString(), password.getText().toString());
                newAcitivity_main(view);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Validates email and password and logs in an existing user
     * @param email
     * @param password
     */
    private void logIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LogInActivity.this, "auth failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                        //TODO
                    }
                });
    }

    private void newAcitivity_signIn(View view){
        Intent SignInActivity = new Intent(this, SignUpActivity.class);
        startActivity(SignInActivity);
    }

    private void newAcitivity_main(View view){
        Intent mainActivity = new Intent(this,MainActivity.class);
        startActivity(mainActivity);
    }
}
