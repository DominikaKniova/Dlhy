package sk.dominika.dluhy.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.databases.MyFirebaseDatabaseHandler;
import sk.dominika.dluhy.database_models.CurrentUser;
import sk.dominika.dluhy.utilities.Utility;

/**
 * The AddFriendActivity for adding new friends. Activity consist of only one
 * text field for entering friend's email. Only friends that are
 * also users of the app can be added.
 * The activity controls user's input whether the email is valid or entered by a text listener and any errors
 * notifies by a toast.
 * It also connects to firebase database and checks if the added person is a user. If not, the activity
 * warns the current user that addition can not be done by a toast.
 * The activity has menu in toolbar containing of a button to add a friend.
 */
public class AddFriendActivity extends AppCompatActivity {

    private TextInputEditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set XML layout the activity will be using
        setContentView(R.layout.activity_add_friend);
        //add toolbar and title
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Add Friend");

        //get reference to email TextView
        email = (TextInputEditText) findViewById(R.id.textInput_add_person_mail);
        //add text listener to check if input is correct
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (email.getText().toString().equals("")){
                    email.setError("Email is required");
                }
                else {
                    email.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //set up touch listener for non-text views to hide keyboard when touched outside a textview
        Utility.handleSoftKeyboard(findViewById(R.id.lin_layout_add_friend), AddFriendActivity.this);
    }
    /**
     * Create a menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_check, menu);
        return true;
    }
    /**
     * Menu handler. Menu contains of a button for adding a new friend.
     * @param item Button clicked.
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.check) {
            item.setEnabled(false);

            if (email.getText().toString().equals("")){
                //if user hasn't completed email text field
                Toast.makeText(AddFriendActivity.this, R.string.uncompleted_fields, Toast.LENGTH_SHORT).show();
                item.setEnabled(true);
                email.setError("Email is required");
            }
            else {
                //if email was completed
                //check if user is not adding himself
                if (!email.getText().toString().equals(CurrentUser.UserCurrent.email)) {
                    //check if email belongs to any user in database and add
                    MyFirebaseDatabaseHandler.checkIfUserAndAdd(item, email, AddFriendActivity.this);
                }
                else {
                    //Current user entered his email
                    Toast.makeText(AddFriendActivity.this, R.string.adding_curUsers_email, Toast.LENGTH_SHORT).show();
                    item.setEnabled(true);
                    email.setText("");
                    email.setError("Invalid email");
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * When back button pressed then return to MyProfileActivity
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}