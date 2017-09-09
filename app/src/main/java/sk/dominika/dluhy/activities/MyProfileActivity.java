package sk.dominika.dluhy.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuInflater;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import sk.dominika.dluhy.databases.MyFirebaseDatabaseHandler;
import sk.dominika.dluhy.database_models.CurrentUser;
import sk.dominika.dluhy.dialogs.DialogFriends;
import sk.dominika.dluhy.R;
import sk.dominika.dluhy.listeners.DialogListener;
import sk.dominika.dluhy.notifications.MyNotificationManager;

/**
 * The MyProfileActivity has the same function as the FriendProfileActivity. After the activity
 * has started, the static class CurrentUser.UserCurrent is updated with user's data from database.
 * If the app was started, this activity decides whether the app stays in this activity or it
 * goes to LogInActivity based on if getCurrentUser == null (currently authenticated user by firebase).
 * The activity has menu in toolbar containing of a button for adding new friend, a button for
 * showing the list of his friends and a button for signing out.
 */
public class MyProfileActivity extends AppCompatActivity implements DialogListener {

    public final String ID = "id";
    public final String FRIENDS = "friends";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set XML layout the activity will be using
        setContentView(R.layout.profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //set action for floating button which adds new debt
        FloatingActionButton floatingButton_add = (FloatingActionButton) findViewById(R.id.floatingButton_newDebt);
        floatingButton_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toNewDebtActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // get currently logged user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //decide whether to stay in this activity or go to LogInActivity
        if (currentUser != null) {
            //set id of user in CurrentUser.UserCurrent static class
            String curUserID = currentUser.getUid();
            CurrentUser.setId(curUserID);
            //set his views
            TextView name = (TextView) findViewById(R.id.profile_name);
            TextView sum = (TextView) findViewById(R.id.profile_sum);
            MyFirebaseDatabaseHandler.setCurrentUserViews(curUserID, name, sum, MyProfileActivity.this);
            getDebtsAndSetRecyclerView();
        } else {
            toLogInActivity();
        }
    }

    /**
     * Start NewDebtActivity.
     */
    private void toNewDebtActivity() {
        Intent intent_debt = new Intent(this, NewDebtActivity.class);
        startActivity(intent_debt);
    }

    /**
     * Start AddFriendActivity.
     */
    private void toAddFriendActivity() {
        Intent intent_person = new Intent(this, AddFriendActivity.class);
        startActivity(intent_person);
    }

    /**
     * Start LogInActivity.
     */
    private void toLogInActivity() {
        CurrentUser.cleanUser();
        Intent intent_person = new Intent(this, LogInActivity.class);
        startActivity(intent_person);
    }

    /**
     * Show dialog with a list of user's friends.
     */
    private void showDialogFriends() {
        DialogFragment newDialog = new DialogFriends();
        newDialog.show(getFragmentManager(), FRIENDS);
    }

    /**
     * Menu handler. Each item in the activity menu starts an activity.
     * @param item Item that was pressed in the menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addPerson:
                toAddFriendActivity();
                break;
            case R.id.listNames:
                showDialogFriends();
                break;
            case R.id.signOut:
                /*When user signs out, cancel all notification so that they are not
                triggered when user is not logged in.*/
                MyNotificationManager.cancelAllNotifications(getBaseContext());
                FirebaseAuth.getInstance().signOut();
                toLogInActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Create menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_my_profile, menu);
        return true;
    }

    /**
     * When clicked on a friend in friends dialog, send his id through intent
     * to FriendProfileActivity and start it.
     * @param id Id of friend.
     */
    @Override
    public void onClick(String id) {
        Intent intent = new Intent(this, FriendProfileActivity.class);
        //add data to intent
        intent.putExtra(ID, id);
        startActivity(intent);
    }

    /**
     * Get debts from firebase database, store them in an ArrayList and then show
     * them in RecyclerView.
     * While data from database are loading, show loading spinner.
     */
    private void getDebtsAndSetRecyclerView() {
        //get reference to loading spinner and set it visible while data is loading
        final ProgressBar spinner = (ProgressBar) findViewById(R.id.progress_bar);
        spinner.setVisibility(View.VISIBLE);
        RecyclerView recyclerViewDebts = (RecyclerView) findViewById(R.id.recycler_viewDebts);
        //layout for swiping to refresh RecyclerView
        //load
        MyFirebaseDatabaseHandler.loadMyDebtsRecyclerView(spinner, recyclerViewDebts,
                MyProfileActivity.this);
    }

    /**
     * When back button pressed, do nothing.
     */
    @Override
    public void onBackPressed() {
    }
}