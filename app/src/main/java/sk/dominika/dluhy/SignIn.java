package sk.dominika.dluhy;


import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignIn extends AppCompatActivity {

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

                Person p = new Person(edTxtName, edTxtEmail, edTxtPassword);
                Accounts.listOfAccounts.add(p);

                newAcitivity_main(view);
            }
        });
    }

    private void newAcitivity_main(View view){
        Intent mainActivity = new Intent(this,MainActivity.class);
        startActivity(mainActivity);
    }
}
