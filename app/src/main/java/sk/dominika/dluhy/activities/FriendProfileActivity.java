package sk.dominika.dluhy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.databases.MyFirebaseDatabaseHandler;
import sk.dominika.dluhy.dialogs.ShowAlertDialogDeleteFriend;

/**
 * The FriendProfileActivity is showing profile of a concrete friend based on his id that is sent through
 * Intent from previous activity, from which this activity is called.
 * After the activity has started, the friend with the id is found in database and
 * views are initialized with his name, overall sum of all debts created with him and the recyclerview,
 * which is a list of the debts, is set up.
 * There is a floating button which starts the NewDebtActivity and simultaneously sends the id of the
 * friend through Intent to initialize the chosen friend for creating a debt in that activity.
 * The activity has menu in toolbar containing of button for deleting the friend with all the debts
 * created with him, and a button for closing the activity and returning to MyProfileActivity.
 */
public class FriendProfileActivity extends AppCompatActivity {

    private String id_friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set XML layout the activity will be using
        setContentView(R.layout.profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //no title when collapsing toolbar is down
        TextView profile = (TextView) findViewById(R.id.profile);
        profile.setText("");

        //set action for floating button which adds new debt
        FloatingActionButton buttonAddDebt = (FloatingActionButton) findViewById(R.id.floatingButton_newDebt);
        buttonAddDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNewDebtActivityWithName();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //get data (id of friend) from previous activity
        Intent intent = getIntent();
        id_friend = intent.getStringExtra("id");

        getDebtsAndShowRecycleView();

        //get reference to views
        final TextView name = (TextView) findViewById(R.id.profile_name);
        final TextView sum = (TextView) findViewById(R.id.profile_sum);
        final CollapsingToolbarLayout collToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        //set correct profile views for the person
        MyFirebaseDatabaseHandler.setFriendsProfileViews(id_friend, name, sum, collToolbar, appBarLayout, FriendProfileActivity.this);
    }

    /**
     * Create menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile_friend, menu);
        return true;
    }

    /**
     * Menu handler. Menu contains of button for deleting the friend
     * and button for closing the profile (returning to MyProfileActivity).
     * @param item Button clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_friend:
                ShowAlertDialogDeleteFriend.showAlertDialog(
                        getBaseContext().getString(R.string.alert_dialog_delete_friend),
                        FriendProfileActivity.this, id_friend);
                break;
            case R.id.close_profile:
                toMyProfileActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Start MyProfileActivity.
     */
    private void toMyProfileActivity(){
        Intent intent_debt = new Intent(this, MyProfileActivity.class);
        startActivity(intent_debt);
    }

    /**
     * Add new debt from friend's profile.
     * Method starts NewDebtActivity and sends to it the id of the friend.
     */
    private void toNewDebtActivityWithName() {
        Intent intent = new Intent(this, NewDebtActivity.class);
        intent.putExtra("id", id_friend);
        startActivity(intent);
    }

    /**
     * Get debts from firebase database, store them in an arraylist and then show them in recycleview.
     * While data from database are loading, show loading spinner.
     */
    private void getDebtsAndShowRecycleView() {
        //get reference to loading spinner and set it visible while data is loading
        final ProgressBar spinner = (ProgressBar) findViewById(R.id.progress_bar);
        spinner.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_viewDebts);
        //load
        MyFirebaseDatabaseHandler.loadDebtsWithFriendRecyclerView(id_friend, spinner,
                recyclerView, FriendProfileActivity.this);
    }
}