package sk.dominika.dluhy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class LogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Button signIn = (Button) findViewById(R.id.button_signIn);
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
                newAcitivity_main(view);
            }
        });
    }

    private void newAcitivity_signIn(View view){
        Intent SignInActivity = new Intent(this,SignIn.class);
        startActivity(SignInActivity);
    }

    private void newAcitivity_main(View view){
        Intent mainActivity = new Intent(this,MainActivity.class);
        startActivity(mainActivity);
    }
}
