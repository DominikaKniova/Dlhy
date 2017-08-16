package sk.dominika.dluhy.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.databases.FirebaseDatabaseHandler;
import sk.dominika.dluhy.databases_objects.CurrentUser;
import sk.dominika.dluhy.databases_objects.Friend;
import sk.dominika.dluhy.databases_objects.Relationship;
import sk.dominika.dluhy.databases_objects.User;

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
            item.setEnabled(false);

            //get data from inputs
            final TextInputEditText firstName = (TextInputEditText) findViewById(R.id.textInput_add_person_firstname);
            TextInputEditText lastName = (TextInputEditText) findViewById(R.id.textInput_add_person_lastname);
            TextInputEditText email = (TextInputEditText) findViewById(R.id.textInput_add_person_mail);

            Friend f = new Friend(firstName, lastName, email);

            //get instance to database
            final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            // get reference to 'users' node and to 'friends' node
            final DatabaseReference refUsers = mDatabase.getReference("users");
            final DatabaseReference refFriends = mDatabase.getReference("friends");


            /**
             * check if new friend is user
             * Yes - create AB, BA friendship in database "friends"
             * No - create only AB, with generated ID for friend
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
                    }
                    //user does not exist
                    else {
                        DatabaseReference friends = mDatabase.getReference("friends");
                        String id = friends.push().getKey();
                        Relationship r = new Relationship(CurrentUser.UserCurrent.id, id, firstName.getText().toString());
                        friends.child(id).setValue(r);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

//            /**
//             * Add friend f to Firebase database.
//             */
//            //get instance to database
//            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
//            // get reference to 'friends' node
//            DatabaseReference ref = mDatabase.getReference("friends");
//            //get id of new node
//            String id = ref.push().getKey();
//            f.setId(id);
//            //get a reference to location id and set the data at this location to the given value
//            ref.child(id).setValue(f);
//
            finish();
            item.setEnabled(true);
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
