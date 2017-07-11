package sk.dominika.dluhy;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //On click listener: Adding new debt
        FloatingActionButton floatingButton_add = (FloatingActionButton) findViewById(R.id.floatingButton_add);
        floatingButton_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        newActivity_addDebt(view);
            }
        });
    }

    private void newActivity_addDebt(View v){
        Intent intent_debt = new Intent(this,NewDebt.class);
        startActivity(intent_debt);
    }

    private void newActivity_addPerson(MenuItem item){
        Intent intent_person = new Intent(this,AddPerson.class);
        startActivity(intent_person);
    }
    private void newActivity_signOut(MenuItem item){
        Intent intent_person = new Intent(this,LogIn.class);
        startActivity(intent_person);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addPerson:
                newActivity_addPerson(item);
                break;
            case R.id.listNames:
                printAccounts();
                break;
            case R.id.calendar:
                break;
            case R.id.signOut:
                newActivity_signOut(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    private void printAccounts(){
        TextView printer = (TextView) findViewById(R.id.printer);
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < Accounts.listOfAccounts.size(); i++) {
            s.append(Accounts.listOfAccounts.get(i).getName());
            s.append("\n");
        }
        printer.setText(s);
    }
}
