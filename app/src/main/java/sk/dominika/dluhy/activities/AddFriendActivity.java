package sk.dominika.dluhy.activities;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import java.util.List;

import sk.dominika.dluhy.databases.DatabaseHandler;
import sk.dominika.dluhy.databases_objects.CurrentUser;
import sk.dominika.dluhy.databases_objects.Debt;
import sk.dominika.dluhy.databases_objects.Friend;
import sk.dominika.dluhy.R;
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

            //get data from inputs
            final TextInputEditText firstName = (TextInputEditText) findViewById(R.id.textInput_add_person_firstname);
            TextInputEditText lastName = (TextInputEditText) findViewById(R.id.textInput_add_person_lastname);
            TextInputEditText email = (TextInputEditText) findViewById(R.id.textInput_add_person_mail);

            Friend f = new Friend(firstName, lastName, email);

//            //Database
//            DatabaseHandler dbFriends = new DatabaseHandler(this);
//            //Add friend to database
//            Friend f = new Friend(firstName, lastName, email);
//            long newID = dbFriends.addFriendToDatabase(f);
//            //set friend's ID
//            f.setId(newID);


            final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            //get instance to database
            final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            // get reference to 'users' node
            final DatabaseReference ref = mDatabase.getReference("users");

            /**
             * check if new friend is user
             * Yes - create AB, BA friendship in database "friends"
             * No - create only AB, with generated ID for friend
             */

            ref.orderByChild("email").equalTo(email.getText().toString()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //user exists
                    if (dataSnapshot.hasChildren()) {
                        User user = new User();
                        //user = dataSnapshot.getValue(User.class);
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            user = snapshot.getValue(User.class);
                        }

                        Relationship r = new Relationship(currentUser.getUid(), user.getId(), user.getFirstname());
                        DatabaseReference friends = mDatabase.getReference("friends");
                        String id = friends.push().getKey();
                        friends.child(id).setValue(r);

                        Relationship r2 = new Relationship(user.getId(), CurrentUser.UserCurrent.id, CurrentUser.UserCurrent.firstName);
                        id = friends.push().getKey();
                        friends.child(id).setValue(r2);
                    }
                    //user does not exist
                    else {
                        DatabaseReference friends = mDatabase.getReference("friends");
                        String id = friends.push().getKey();
                        Relationship r = new Relationship(currentUser.getUid(), id, firstName.getText().toString());
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
