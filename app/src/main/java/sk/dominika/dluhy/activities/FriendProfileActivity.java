package sk.dominika.dluhy.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.adapters.DebtAdapter;
import sk.dominika.dluhy.database_models.CurrentUser;
import sk.dominika.dluhy.database_models.Debt;
import sk.dominika.dluhy.databases.MyFirebaseDatabaseHandler;
import sk.dominika.dluhy.database_models.User;
import sk.dominika.dluhy.decorations.DividerDecoration;
import sk.dominika.dluhy.dialogs.DialogFriendDebts;
import sk.dominika.dluhy.dialogs.ShowAlertDialogDeleteFriend;

public class FriendProfileActivity extends AppCompatActivity {

    private String id_friend;
    private CollapsingToolbarLayout collTlbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView profile = (TextView) findViewById(R.id.profile);
        profile.setText("");

        //On click listener: Adding new debt(_all) from friend profile
        final FloatingActionButton floatingButton = (FloatingActionButton) findViewById(R.id.floatingButton_add);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newDebtActivityWithName();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showRecycleView();

        //Get data (id of friend) from previous activity and set correct profile
        Intent intent = getIntent();
        id_friend = intent.getStringExtra("id");

        /**
         * Find friend (user) in database based on the friend's id
         */
        //get instance to database
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        // get reference to 'users' node and child with the id
        DatabaseReference ref = mDatabase.getReference("users").child(id_friend);

        // get data (name of friend) from database and set views
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                User value = dataSnapshot.getValue(User.class);
                TextView name = (TextView) findViewById(R.id.profile_name);
                name.setText(value.getFirstname() + " " + value.getLastname());
                TextView sum = (TextView) findViewById(R.id.profile_sum);
                MyFirebaseDatabaseHandler.getOverallSum(value.getId(), sum);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
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
                ShowAlertDialogDeleteFriend.showAlertDialog(getBaseContext().getString(R.string.alert_dialog_delete_friend), FriendProfileActivity.this, id_friend);
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
     * @param id ID of friend.
     */
    private void showDialog_ourDebts(String id) {
        DialogFragment newDialog = new DialogFriendDebts();
        Bundle args = new Bundle();
        args.putString("id", id);
        newDialog.setArguments(args);
        newDialog.show(getFragmentManager(), "debts");
    }

    /**
     * Add new debt from friend's profile.
     * Send id through intent.
     */
    private void newDebtActivityWithName() {
        Intent intent = new Intent(this, NewDebtActivity.class);
        intent.putExtra("id", id_friend);
        startActivity(intent);
    }

    ExpandableRelativeLayout expandableLayout;

    private void expandableButtonRecycleView(View v) {
//        expandableLayout = (ExpandableRelativeLayout) findViewById(R.id.expandableLayoutRecycleView);
//        expandableLayout.toggle(); // toggle expand and collapse
    }

    private void showRecycleView() {
        /**
         * Get debts from firebase database and store them in arraylist Debt.myDebts.
         */
        Debt.myDebts.clear();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("debts");
        ref.addValueEventListener(MyFirebaseDatabaseHandler.listenerAllMyDebts);

        RecyclerView recycler_viewDebts = (RecyclerView) findViewById(R.id.recycler_viewDebts);
        DebtAdapter adapter = new DebtAdapter(getBaseContext(), Debt.myDebts);
        recycler_viewDebts.addItemDecoration(new DividerDecoration(getBaseContext()));
        recycler_viewDebts.setAdapter(adapter);
        recycler_viewDebts.setLayoutManager(new LinearLayoutManager(getBaseContext()));

    }
}