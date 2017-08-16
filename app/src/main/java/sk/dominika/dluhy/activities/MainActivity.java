package sk.dominika.dluhy.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import sk.dominika.dluhy.databases.FirebaseDatabaseHandler;
import sk.dominika.dluhy.databases_objects.CurrentUser;
import sk.dominika.dluhy.databases_objects.User;
import sk.dominika.dluhy.dialogs.DialogAllDebts;
import sk.dominika.dluhy.dialogs.DialogFriends;
import sk.dominika.dluhy.R;
import sk.dominika.dluhy.listeners.DialogListener;


public class MainActivity extends AppCompatActivity implements DialogListener {

    private final String TAG = "signed/logged";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("My profile");

        TextView name = (TextView) findViewById(R.id.profile_name);

        //On click listener: Adding new debt_all
        FloatingActionButton floatingButton_add = (FloatingActionButton) findViewById(R.id.floatingButton_add);
        floatingButton_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newActivity_addDebt(view);
            }
        });

        //On click listener: My debts
        Button myDebts = (Button) findViewById(R.id.my_debts);
        myDebts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog_debts(v);
            }
        });

        // FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        //method tracking whenever the user signs in or out
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //set views of logged user
                if(user != null) {
                    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                    String id = user.getUid();

                    DatabaseReference ref = mDatabase.getReference("users").child(id);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user!= null) { //TODO toto uprav ked to budes mat spravne
                                CurrentUser.setData(user.getFirstname(), user.getLastname(), user.getEmail());
                                CurrentUser.setId(user.getId());

                                //set views
                                TextView name = (TextView) findViewById(R.id.profile_name);
                                name.setText(CurrentUser.UserCurrent.firstName + " " + CurrentUser.UserCurrent.lastName);

                                TextView sum = (TextView) findViewById(R.id.my_profile_sum);
                                FirebaseDatabaseHandler.getOverallSum(CurrentUser.UserCurrent.id, sum);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(MainActivity.this, "User doesn't exist", Toast.LENGTH_SHORT);

                        }
                    });

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };



    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

        //TODO bude asi v log in activity
        /**
         * set current user
         */
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser loggedUser = mAuth.getCurrentUser();
//        if (loggedUser != null) {
//            String id = loggedUser.getUid();
//
//
//            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
//
//            DatabaseReference ref = mDatabase.getReference("users").child(id);
//            ref.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    User user = dataSnapshot.getValue(User.class);
//                    CurrentUser.setData(user.getFirstname(), user.getLastname(), user.getEmail());
//                    CurrentUser.setId(user.getId());
//
//                    //set views
//                    TextView name = (TextView) findViewById(R.id.profile_name);
//                    name.setText(CurrentUser.UserCurrent.firstName + " " + CurrentUser.UserCurrent.lastName);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }
    }



    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        TextView name = (TextView)findViewById(R.id.profile_name);
        name.setText("");
    }

    //Start activity New Debt
    private void newActivity_addDebt(View v){
        Intent intent_debt = new Intent(this,NewDebtActivity.class);
        startActivity(intent_debt);
    }

    //Start activity Add Friend
    private void newActivity_addFriend(MenuItem item){
        Intent intent_person = new Intent(this,AddFriendActivity.class);
        startActivity(intent_person);
    }

    //Start activity Log In
    private void newActivity_signOut(MenuItem item){
        CurrentUser.cleanUser();
        Intent intent_person = new Intent(this,LogInActivity.class);
        startActivity(intent_person);
    }

    private void newDialog_friends(MenuItem item) {
        DialogFragment newDialog = new DialogFriends();
        newDialog.show(getFragmentManager(), "friends");
    }

    //Start dialog list of my debts
    private void showDialog_debts(View view){
        DialogFragment newDialog = new DialogAllDebts();
        newDialog.show(getFragmentManager(), "debts");

    }

    //Start activities from Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addPerson:
                newActivity_addFriend(item);
                break;
            case R.id.listNames:
                //newActivity_ListFriends(item);
                //printAccounts();
                //newDialog_friends(item);
                //showDialog_debts(item);
                newDialog_friends(item);
                break;
            case R.id.calendar:
                break;
            case R.id.signOut:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                newActivity_signOut(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Menu handler
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(String id) {
        Intent intent = new Intent(this, FriendProfileActivity.class);
        //add data to intent
        intent.putExtra("id", id);
        startActivity(intent);
    }

    //print my friends from database ...testing
//    private void printAccounts(){
//        DatabaseHandler dbFr = new DatabaseHandler(this);
//        List<Friend> myFr = dbFr.getFriendsFromDatabase();
//
//        TextView printer = (TextView) findViewById(R.id.printer);
//        StringBuilder s = new StringBuilder();
//        for (int i = 0; i < myFr.size(); i++) {
//            s.append(myFr.get(i).getFirstName());
//            s.append("\n");
//        }
//        printer.setText(s);
//    }
}
