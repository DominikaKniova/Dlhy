package sk.dominika.dluhy.activities;


import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import sk.dominika.dluhy.databases_objects.Account;
import sk.dominika.dluhy.R;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button newAccountCreated = (Button) findViewById(R.id.button_signIn);
        newAccountCreated.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                TextInputEditText edTxtName = (TextInputEditText) findViewById(R.id.text_input_signIn_name);
                TextInputEditText edTxtEmail = (TextInputEditText) findViewById(R.id.text_input_signIn_email);
                TextInputEditText edTxtPassword = (TextInputEditText) findViewById(R.id.text_input_signIn_password);

                Account p = new Account(edTxtName, edTxtEmail, edTxtPassword);
                Account.listOfAccounts.add(p);

                newAcitivity_main(view);
            }
        });
    }

    private void newAcitivity_main(View view){
        Intent mainActivity = new Intent(this,MainActivity.class);
        startActivity(mainActivity);
    }
}
