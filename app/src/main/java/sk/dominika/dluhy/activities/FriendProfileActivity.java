package sk.dominika.dluhy.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.databases.FirebaseDatabaseHandler;
import sk.dominika.dluhy.database_models.CurrentUser;
import sk.dominika.dluhy.database_models.User;
import sk.dominika.dluhy.dialogs.DialogFriendDebts;

public class FriendProfileActivity extends AppCompatActivity {

    private String id_friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Get data (id of friend) from previous activity and set correct profile
        Intent intent = getIntent();
        id_friend = intent.getStringExtra("id");

        //if id friend == null
        setTitle(CurrentUser.UserCurrent.firstName);

        /**
         * find friend (user) in database based on the friend's id
         */
        //get instance to database
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        // get reference to 'users' node and child with the id
        DatabaseReference ref = mDatabase.getReference("users").child(id_friend);

        // get data (name of friend) from firebase database and set views
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User value = dataSnapshot.getValue(User.class);

                TextView profile_name = (TextView) findViewById(R.id.profile_name);
                profile_name.setText(value.getFirstname() + " " + value.getLastname());
                //TODO: email, sum, pic ....

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //TODO
            }
        });

        TextView sum = (TextView) findViewById(R.id.friend_profile_sum);
        FirebaseDatabaseHandler.getOverallSum(id_friend, sum);

        //On click listener: Showing all our debts
        final Button our_debts = (Button) findViewById(R.id.our_debts);
        our_debts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog_ourDebts(id_friend);
            }
        });

        //On click listener: Adding new debt(_all) from friend profile
        FloatingActionButton floatingButton = (FloatingActionButton) findViewById(R.id.new_debt_from_friend);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO pridavanie dlhu z profilu kamarata
            }
        });
    }

    //Menu handler
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile_friend, menu);
        return true;
    }

    //Start activities from Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_friend:
                FirebaseDatabaseHandler.deleteFriendAndDebts(id_friend);
                break;
            case R.id.close_profile:
                newActivity_main();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    //Start activity Main
    private void newActivity_main(){
        Intent intent_debt = new Intent(this,MainActivity.class);
        startActivity(intent_debt);
    }

    /**
     * Starts dialog- list of debts shared with the friend.
     *
     * @param id ID of friend.
     */
    private void showDialog_ourDebts(String id) {
        DialogFragment newDialog = new DialogFriendDebts();
        Bundle args = new Bundle();
        args.putString("id", id);
        newDialog.setArguments(args);
        newDialog.show(getFragmentManager(), "debts");
        //TODO

    }
}


