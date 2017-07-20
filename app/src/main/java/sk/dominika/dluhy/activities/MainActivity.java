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

import java.util.ArrayList;
import java.util.List;

import sk.dominika.dluhy.databases.FriendsDBHandler;
import sk.dominika.dluhy.dialogs.DialogFriends;
import sk.dominika.dluhy.kindOfBackend.AddFriend;
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
        Intent intent_debt = new Intent(this,NewDebtActivity.class);
        startActivity(intent_debt);
    }

    private void newActivity_addPerson(MenuItem item){
        Intent intent_person = new Intent(this,AddFriendActivity.class);
        startActivity(intent_person);
    }
    private void newActivity_signOut(MenuItem item){
        Intent intent_person = new Intent(this,LogInActivity.class);
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
                //newActivity_ListFriends(item);
                //printAccounts();
                //newDialog_friends(item);
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

    //print my friends from database ...testing
//    private void printAccounts(){
//        FriendsDBHandler dbFr = new FriendsDBHandler(this);
//        List<AddFriend> myFr = dbFr.getFriendFromDatabase();
//
//        TextView printer = (TextView) findViewById(R.id.printer);
//        StringBuilder s = new StringBuilder();
//        for (int i = 0; i < myFr.size(); i++) {
//            s.append(myFr.get(i).getName());
//            s.append("\n");
//        }
//        printer.setText(s);
//    }
}
