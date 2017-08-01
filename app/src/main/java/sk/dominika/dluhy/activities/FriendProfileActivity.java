package sk.dominika.dluhy.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.databases.DatabaseHandler;
import sk.dominika.dluhy.databases_objects.Friend;
import sk.dominika.dluhy.dialogs.DialogAllDebts;
import sk.dominika.dluhy.dialogs.DialogFriendDebts;

public class FriendProfileActivity extends AppCompatActivity {

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
        final long id_friend = intent.getLongExtra("id", 0);

//        if (id_friend != 0)

        //find friend in database based on the id_friend
        final DatabaseHandler db = new DatabaseHandler(this);
        Bundle bundle = db.getFriend(id_friend);

        //set views
        //TextView profile_name = (TextView) findViewById(R.id.profile_name);
        //profile_name.setText(bundle.getString("firstname") + " " + bundle.getString("lastname"));


        /**
         * find friend in database based on the friend's key
         */
        //get instance to database
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        // get reference to 'friends' node and child with the key
        DatabaseReference ref = mDatabase.getReference("friends").child("-KqSKbwrB5QO1KM3P2Jk");

        // get data (name of friend) from firebase database and set views
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Friend value = dataSnapshot.getValue(Friend.class);

                TextView profile_name = (TextView) findViewById(R.id.profile_name);
                profile_name.setText(value.getFirstName() + " " + value.getLastName());
                //TODO: email, sum, pic ....

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //TODO
            }
        });

        //On click listener: Showing all out debts
        Button our_debts = (Button) findViewById(R.id.our_debts);
        our_debts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog_ourDebts(id_friend);
            }
        });

        //On click listener: Adding new debt_all from friend profile
        FloatingActionButton floatingButton = (FloatingActionButton) findViewById(R.id.new_debt_from_friend);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //Menu handler
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Starts dialog- list of debts shared with the friend.
     * @param id ID of friend.
     */
    private void showDialog_ourDebts(long id){
        DialogFragment newDialog = new DialogFriendDebts();
        Bundle args = new Bundle();
        args.putLong("id", id);
        newDialog.setArguments(args);
        newDialog.show(getFragmentManager(), "debts");

    }
}


