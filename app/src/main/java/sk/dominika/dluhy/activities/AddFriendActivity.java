package sk.dominika.dluhy.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.databases.FirebaseDatabaseHandler;
import sk.dominika.dluhy.database_models.CurrentUser;
import sk.dominika.dluhy.database_models.User;
import sk.dominika.dluhy.dialogs.ShowAlertDialogNeutral;

public class AddFriendActivity extends AppCompatActivity {
    private TextInputEditText firstName, lastName, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        setTitle("Add Friend");

        //get data from inputs
        firstName = (TextInputEditText) findViewById(R.id.textInput_add_person_firstname);
        lastName = (TextInputEditText) findViewById(R.id.textInput_add_person_lastname);
        email = (TextInputEditText) findViewById(R.id.textInput_add_person_mail);

        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (firstName.getText().toString().equals("")){
                    firstName.setError("First name is required");
                }
                else {
                    firstName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (lastName.getText().toString().equals("")){
                    lastName.setError("Last name is required");
                }
                else {
                    lastName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        //add friend
        if (item.getItemId() == R.id.check) {
            item.setEnabled(false);

            //if user hasn't completed any field
            if (firstName.getText().toString().equals("")
                    || lastName.getText().toString().equals("")
                    || email.getText().toString().equals("")){
                ShowAlertDialogNeutral.showAlertDialog("You must complete input fields", AddFriendActivity.this);
                item.setEnabled(true);
                email.setError("Email is required");
                lastName.setError("Last name is required");
                firstName.setError("First name is required");
            }
            //if all fields are completed then add new friend
            else {
                //get instance to database
                final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                // get reference to 'users' node and to 'friends' node
                final DatabaseReference refUsers = mDatabase.getReference("users");


                /**
                 * check if new friend is user
                 * Yes - create AB, BA friendship in database "friends"
                 * No - make toast that user doesn't exist
                 */
                refUsers.orderByChild("email").equalTo(email.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //user exists
                        if (dataSnapshot.hasChildren()) {
                            User user = new User();
                            //user = dataSnapshot.getValue(User.class);
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                user = snapshot.getValue(User.class);
                            }

                            /**
                             * Firstly check whether the relationship doesn't already exist.
                             * if not then add new relationship
                             */
                            FirebaseDatabaseHandler.checkIfRelationshipExists(CurrentUser.UserCurrent.id, user, AddFriendActivity.this);
                            finish();
                            item.setEnabled(true);
                        }
                        //user does not exist
                        else {
//                            DatabaseReference friends = mDatabase.getReference("friends");
//                            String id = friends.push().getKey();
//                            Relationship r = new Relationship(CurrentUser.UserCurrent.id, id, firstName.getText().toString());
//                            friends.child(id).setValue(r);
                            Toast.makeText(AddFriendActivity.this, R.string.not_existing_user, Toast.LENGTH_SHORT).show();
                            item.setEnabled(true);
                            email.setText("");
                            email.setError("Invalid email");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
