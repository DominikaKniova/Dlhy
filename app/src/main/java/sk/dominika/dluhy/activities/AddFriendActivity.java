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
            TextInputEditText edTxtName = (TextInputEditText) findViewById(R.id.textInput_add_person_name);
            TextInputEditText edTxtEmail = (TextInputEditText) findViewById(R.id.textInput_add_person_mail);

            //My backend
//            Friend f = new Friend(edTxtName, edTxtEmail);
//            Friend.myFriends.add(f);

            //Database
            DatabaseHandler dbFriends = new DatabaseHandler(this);
            //Add friend to database
            Friend f = new Friend(edTxtName, edTxtEmail);
            dbFriends.addFriendToDatabase(f);

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
