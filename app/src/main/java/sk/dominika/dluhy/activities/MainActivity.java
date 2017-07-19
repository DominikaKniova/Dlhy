package sk.dominika.dluhy.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuInflater;
import android.widget.TextView;

import sk.dominika.dluhy.dialogs.DialogFriends;
import sk.dominika.dluhy.kindOfBackend.Person;
import sk.dominika.dluhy.R;


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
    public void newActivity_ListFriends(MenuItem item) {
        Intent intent_friends = new Intent(this,ListFriendsActivity.class);
        startActivity(intent_friends);
    }

    private void newDialog_friends(MenuItem item) {
        DialogFragment newDialog = new DialogFriends();
        newDialog.show(getFragmentManager(), "friends");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addPerson:
                newActivity_addPerson(item);
                break;
            case R.id.listNames:
                //printAccounts();
                //newActivity_ListFriends(item);
                newDialog_friends(item);
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
        for (int i = 0; i < Person.listOfAccounts.size(); i++) {
            s.append(Person.listOfAccounts.get(i).getName());
            s.append("\n");
        }
        printer.setText(s);
    }
}
