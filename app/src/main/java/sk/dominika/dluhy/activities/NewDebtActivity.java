package sk.dominika.dluhy.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.database_models.CurrentUser;
import sk.dominika.dluhy.databases.MyFirebaseDatabaseHandler;
import sk.dominika.dluhy.dialogs.DatePickerFragment;
import sk.dominika.dluhy.dialogs.DialogFriends;
import sk.dominika.dluhy.dialogs.ShowAlertDialogNeutral;
import sk.dominika.dluhy.dialogs.TimePickerFragment;
import sk.dominika.dluhy.listeners.DialogListener;
import sk.dominika.dluhy.utilities.Utility;

/**
 * The NewDebtActivity gives an user interface between the app and the database for creating
 * new debts. An user must complete all TextViews but TextViews for choosing date and time of
 * notification, those two are not necessary. And also a friend with whom the debt is created
 * must be chosen. Any input errors are notified by Alert Dialog. In the activity's toolbar
 * there is a button for adding the debt.
 */
public class NewDebtActivity extends AppCompatActivity implements DialogListener {
    private String idFriend;
    public final String ID = "id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set XML layout the activity will be using
        setContentView(R.layout.activity_new_debt);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("New Debt");

        //set up touch listener for non-text views to hide keyboard when touched outside a textview
        Utility.handleSoftKeyboard(findViewById(R.id.lin_layout_new_debt), NewDebtActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
         /*Check if there exists data (id of friend) from previous activity
         YES - NewDebtActivity is called from FriendProfileActivity
         NO - NewDebtActivity is called from MyProfileActivity*/
        Intent intent = getIntent();
        if (intent.getStringExtra(ID) != null) {
            onClick(intent.getStringExtra(ID));
        }

        //expand date/time pickers
        final Button expButton = (Button) findViewById(R.id.expandableButton_alert);
        expButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                //hide soft keyboard
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                //expand
                expandableButtonNotification();
            }
        });

        //date picker
        TextInputEditText dateEditText = (TextInputEditText) findViewById(R.id.inputDate);
        //disable input/keyboard for date TextView
        dateEditText.setFocusable(false);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        //time picker
        TextInputEditText timeEditText = (TextInputEditText) findViewById(R.id.inputTime);
        //disable input/keyboard for time TextView
        timeEditText.setFocusable(false);
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        //name of current user
        TextView myName = (TextView) findViewById(R.id.myPic);
        myName.setText(CurrentUser.UserCurrent.firstName);

        //choose friend
        TextView friendPic = (TextView) findViewById(R.id.friendsPic);
        friendPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogFriends();
            }
        });

        //change arrow when clicking
        final ImageView arrow = (ImageView) findViewById(R.id.arrow);
        arrow.setImageResource(R.drawable.ic_arrow_forward);
        arrow.setTag("arrForward");
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageTag = (String) arrow.getTag();
                if (imageTag.equals("arrForward")) {
                    arrow.setTag("arrBack");
                    arrow.setImageResource(R.drawable.ic_arrow_back);
                } else {
                    arrow.setTag("arrForward");
                    arrow.setImageResource(R.drawable.ic_arrow_forward);
                }
            }
        });
    }

    /**
     * Create menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_check, menu);
        return true;
    }

    /**
     * Menu for creating debt. It checks whether the user completed all needed text fields.
     * Any errors are notified. If the check is succeeded, the debt is added to the database
     * and then if the debt has notification then all notifications are synced, that means
     * that all are deleted and created again with the new notification from the already
     * created debt.
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.check) {
            item.setEnabled(false);
            //get reference to TextView
            TextView tName = (TextView) findViewById(R.id.friendsPic);
            TextInputEditText tNote = (TextInputEditText) findViewById(R.id.textInput_note);
            TextInputEditText tSum = (TextInputEditText) findViewById(R.id.textInput_money);
            TextInputEditText tDateNotif = (TextInputEditText) findViewById(R.id.inputDate);
            TextInputEditText tTimeNotif = (TextInputEditText) findViewById(R.id.inputTime);

            if (tName.getText().toString().equals("")) {
                //if user hasn't chosen friend
                ShowAlertDialogNeutral.showAlertDialog("You must choose friend", NewDebtActivity.this);
                item.setEnabled(true);
            } else {

                if (tNote.getText().toString().equals("") || tSum.getText().toString().equals("")) {
                    //if user hasn't completed note and sum TextViews
                    ShowAlertDialogNeutral.showAlertDialog("You must complete note and sum", NewDebtActivity.this);
                    item.setEnabled(true);
                } else {
                    ImageView arrow = (ImageView) findViewById(R.id.arrow);
                    MyFirebaseDatabaseHandler.createDebt(idFriend, tName, tNote, tSum,
                            tDateNotif, tTimeNotif, arrow, NewDebtActivity.this);
                    finish();
                    item.setEnabled(true);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Expand expandable layout with date and time TextViews.
     */
    private void expandableButtonNotification() {
        ExpandableRelativeLayout expandableLayout =
                (ExpandableRelativeLayout) findViewById(R.id.expandableLayoutAlert);
        expandableLayout.toggle();
    }

    /**
     * Dialog for selecting time of notification.
     */
    private void showTimePickerDialog() {
        DialogFragment newFragmentTime = new TimePickerFragment();
        newFragmentTime.show(getFragmentManager(), "timePicker");
    }

    /**
     * Dialog for selecting date of notification.
     */
    private void showDatePickerDialog() {
        DialogFragment newFragmentDate = new DatePickerFragment();
        newFragmentDate.show(getFragmentManager(), "datePicker");
    }

    /**
     * Show dialog with list of user's friends.
     */
    private void showDialogFriends() {
        MyFirebaseDatabaseHandler.loadFriendsFromDatabase(NewDebtActivity.this);
    }

    /**
     * Make access to friend's id through DialogFriends and find friend in the database.
     * Sets the text (name of friend) of TextView friendsPic.
     * @param id Friend's id that is from FriendAdapter.
     */
    @Override
    public void onClick(String id) {
        idFriend = id;
        TextView tvName = (TextView) findViewById(R.id.friendsPic);
        MyFirebaseDatabaseHandler.getFriendNameFromDatabase(id, tvName, NewDebtActivity.this);
    }
}