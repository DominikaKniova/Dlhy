package sk.dominika.dluhy.activities;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import sk.dominika.dluhy.databases.DatabaseHandler;
import sk.dominika.dluhy.databases_objects.Friend;
import sk.dominika.dluhy.R;

public class AddFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        setTitle("Add Friend");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //add friend
        if(item.getItemId() == R.id.check) {

            //get data from inputs
            TextInputEditText firstName = (TextInputEditText) findViewById(R.id.textInput_add_person_firstname);
            TextInputEditText lastName = (TextInputEditText) findViewById(R.id.textInput_add_person_lastname);
            TextInputEditText email = (TextInputEditText) findViewById(R.id.textInput_add_person_mail);

            //Database
            DatabaseHandler dbFriends = new DatabaseHandler(this);
            //Add friend to database
            Friend f = new Friend(firstName, lastName, email);
            long newID = dbFriends.addFriendToDatabase(f);
            //set friend's ID
            f.setId(newID);

            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}
