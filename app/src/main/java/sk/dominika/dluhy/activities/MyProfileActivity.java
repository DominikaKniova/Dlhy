package sk.dominika.dluhy.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuInflater;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

import sk.dominika.dluhy.adapters.DebtAdapter;
import sk.dominika.dluhy.database_models.Debt;
import sk.dominika.dluhy.databases.MyFirebaseDatabaseHandler;
import sk.dominika.dluhy.database_models.CurrentUser;
import sk.dominika.dluhy.database_models.User;
import sk.dominika.dluhy.decorations.DividerDecoration;
import sk.dominika.dluhy.dialogs.DialogFriends;
import sk.dominika.dluhy.R;
import sk.dominika.dluhy.listeners.DialogListener;
import sk.dominika.dluhy.notifications.MyAlarmManager;


public class MyProfileActivity extends AppCompatActivity implements DialogListener {

    private final String TAG = "signed/logged";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //On click listener: Adding new debt from my profile
        FloatingActionButton floatingButton_add = (FloatingActionButton) findViewById(R.id.floatingButton_add);
        floatingButton_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newActivity_addDebt(view);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // FirebaseAuth instance
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        //set views of logged user
        if (currentUser != null) {
            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            CurrentUser.setId(currentUser.getUid());
            mDatabase.getReference("users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    CurrentUser.setData(user.getFirstname(), user.getLastname(), user.getEmail());

                    //set views
                    TextView name = (TextView) findViewById(R.id.profile_name);
                    name.setText(CurrentUser.UserCurrent.firstName + " " + CurrentUser.UserCurrent.lastName);
                    TextView sum = (TextView) findViewById(R.id.profile_sum);
                    MyFirebaseDatabaseHandler.getOverallSum(CurrentUser.UserCurrent.id, sum);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MyProfileActivity.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            newActivity_signOut();
        }

        showRecycleViewAllDebts();
    }

    @Override
    protected void onStop() {
        super.onStop();
        TextView name = (TextView) findViewById(R.id.profile_name);
        name.setText("");
    }

    //Start activity New Debt
    private void newActivity_addDebt(View v) {
        Intent intent_debt = new Intent(this, NewDebtActivity.class);
        startActivity(intent_debt);
    }

    //Start activity Add Friend
    private void newActivity_addFriend(MenuItem item) {
        Intent intent_person = new Intent(this, AddFriendActivity.class);
        startActivity(intent_person);
    }

    //Start activity Log In
    private void newActivity_signOut() {
        CurrentUser.cleanUser();
        Intent intent_person = new Intent(this, LogInActivity.class);
        startActivity(intent_person);
    }

    private void newDialog_friends(MenuItem item) {
        DialogFragment newDialog = new DialogFriends();
        newDialog.show(getFragmentManager(), "friends");
    }

    //Start activities from Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addPerson:
                newActivity_addFriend(item);
                break;
            case R.id.listNames:
                newDialog_friends(item);
                break;
            case R.id.signOut:
                MyAlarmManager.cancelAllNotifications(getBaseContext());
                FirebaseAuth.getInstance().signOut();
                newActivity_signOut();
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

    @Override
    public void onClick(String id) {
        Intent intent = new Intent(this, FriendProfileActivity.class);
        //add data to intent
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void showRecycleViewAllDebts() {
        /**
         * Get debts from firebase database and store them in arraylist Debt.myDebts.
         */
        final ProgressBar spinner = (ProgressBar) findViewById(R.id.progress_bar);
        spinner.setVisibility(View.VISIBLE);

        FirebaseDatabase.getInstance().getReference("debts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //loop through all debts in the database
                List<Debt> listDebts = new ArrayList<Debt>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Debt value = snapshot.getValue(Debt.class);
                    //add only my debts from database
                    if (value.getId_who().equals(CurrentUser.UserCurrent.id)
                            || value.getId_toWhom().equals(CurrentUser.UserCurrent.id)) {
                        listDebts.add(value);
                    }
                }
                RecyclerView recycler_viewDebts = (RecyclerView) findViewById(R.id.recycler_viewDebts);
                DebtAdapter adapter = new DebtAdapter(getBaseContext(), listDebts);
                recycler_viewDebts.addItemDecoration(new DividerDecoration(getBaseContext()));
                recycler_viewDebts.setAdapter(adapter);
                recycler_viewDebts.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                spinner.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}