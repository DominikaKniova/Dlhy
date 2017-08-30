package sk.dominika.dluhy.activities;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sk.dominika.dluhy.database_models.CurrentUser;
import sk.dominika.dluhy.database_models.Debt;
import sk.dominika.dluhy.database_models.User;
import sk.dominika.dluhy.dialogs.DatePickerFragment;
import sk.dominika.dluhy.R;
import sk.dominika.dluhy.dialogs.DialogFriends;
import sk.dominika.dluhy.dialogs.ShowAlertDialogNeutral;
import sk.dominika.dluhy.dialogs.TimePickerFragment;
import sk.dominika.dluhy.listeners.DialogListener;
import sk.dominika.dluhy.notifications.MyAlarmManager;
import sk.dominika.dluhy.utilities.Utility;

public class NewDebtActivity extends AppCompatActivity implements DialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_debt);
        setTitle("New Debt");

        //Set up touch listener for non-text box views to hide keyboard.
        Utility.handleSoftKeyboard(findViewById(R.id.lin_layout_new_debt), NewDebtActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * Check if there exists data (id of friend) from previous activity
         * YES - NewDebtActivity is called from FriendProfileActivity
         * NO - NewDebtActivity is called from MainActivity
         */
        Intent intent = getIntent();
        if (intent.getStringExtra("id") != null) {
            onClick(intent.getStringExtra("id"));
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
                expandableButtonAlerts(view);
            }
        });

        //date picker
        TextInputEditText dateEditText = (TextInputEditText) findViewById(R.id.inputDate);
        //disable input/keyboard
        dateEditText.setFocusable(false);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        //time picker
        TextInputEditText timeEditText = (TextInputEditText) findViewById(R.id.inputTime);
        //disable input/keyboard
        timeEditText.setFocusable(false);
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        //name of current user
        TextView myName = (TextView) findViewById(R.id.myPic);
        myName.setText(CurrentUser.UserCurrent.firstName);

        //choose friend
        TextView firendPic = (TextView) findViewById(R.id.friendsPic);
        firendPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog_friends(v);
            }
        });

        //change arrow
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.check) {
            item.setEnabled(false);

            //get data from inputs
            TextView tName = (TextView) findViewById(R.id.friendsPic);
            TextInputEditText tNote = (TextInputEditText) findViewById(R.id.textInput_note);
            TextInputEditText tSum = (TextInputEditText) findViewById(R.id.textInput_money);
            TextInputEditText tDateAlert = (TextInputEditText) findViewById(R.id.inputDate);
            TextInputEditText tTimeAlert = (TextInputEditText) findViewById(R.id.inputTime);

            //if user hasn't chosen friend
            if (tName.getText().toString().equals("")) {
                ShowAlertDialogNeutral.showAlertDialog("You must choose friend", NewDebtActivity.this);
                item.setEnabled(true);
            } else {

                if (tNote.getText().toString().equals("") || tSum.getText().toString().equals("")) {
                    ShowAlertDialogNeutral.showAlertDialog("You must complete note and sum", NewDebtActivity.this);
                    item.setEnabled(true);
                } else {
                    //id of new debt
                    String id;
                    //find out if I owe friend money or other way round
                    boolean heOwesMe;
                    ImageView arrow = (ImageView) findViewById(R.id.arrow);
                    String imageTag = (String) arrow.getTag();
                    if (imageTag.equals("arrForward")) {
                        heOwesMe = false;
                    } else {
                        heOwesMe = true;
                    }

                    /**
                     * Add debt d to Firebase database.
                     */
                    //get instance to database
                    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                    // get reference to 'debts' node
                    DatabaseReference ref = mDatabase.getReference("debts");

                    //I owe
                    if (!heOwesMe) {
                        //get id of new node
                        id = ref.push().getKey();
                        Debt debt = new Debt(
                                id,
                                CurrentUser.UserCurrent.id,
                                id_of_friend,
                                CurrentUser.UserCurrent.firstName,
                                tName.getText().toString(),
                                Float.parseFloat(tSum.getText().toString()),
                                tNote.getText().toString(),
                                tDateAlert.getText().toString(),
                                tTimeAlert.getText().toString(),
                                "false");
                        //get a reference to location id and set the data at this location to the given value
                        ref.child(id).setValue(debt);
                    }
                    //they owe
                    else {
                        //get id of new node
                        id = ref.push().getKey();
                        Debt debt = new Debt(
                                id,
                                id_of_friend,
                                CurrentUser.UserCurrent.id,
                                tName.getText().toString(),
                                CurrentUser.UserCurrent.firstName,
                                Float.parseFloat(tSum.getText().toString()),
                                tNote.getText().toString(),
                                tDateAlert.getText().toString(),
                                tTimeAlert.getText().toString(),
                                "false");
                        //get a reference to location id and set the data at this location to the given value
                        ref.child(id).setValue(debt);
                    }

                    //if alert is added then sync new notification with old ones
                    if (!(tDateAlert.getText().toString().equals("") && tTimeAlert.getText().toString().equals(""))) {
                        MyAlarmManager.syncNotifications(getBaseContext());
                    }
                    finish();
                    item.setEnabled(true);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    ExpandableRelativeLayout expandableLayout;

    private void expandableButtonAlerts(View v) {
        expandableLayout = (ExpandableRelativeLayout) findViewById(R.id.expandableLayoutAlert);
        expandableLayout.toggle(); // toggle expand and collapse
    }

    private void showTimePickerDialog(View v) {
        DialogFragment newFragmentTime = new TimePickerFragment();
        newFragmentTime.show(getFragmentManager(), "timePicker");

    }

    private void showDatePickerDialog(View v) {
        DialogFragment newFragmentDate = new DatePickerFragment();
        newFragmentDate.show(getFragmentManager(), "datePicker");
    }

    private void showDialog_friends(View v) {
        DialogFragment newDialog = new DialogFriends();
        newDialog.show(getFragmentManager(), "friends");
    }

    /**
     * Makes access to friend's id through DialogFriends.
     * Sets the text (name of friend) of TextView friendsPic.
     */
    private String id_of_friend;

    @Override
    public void onClick(String friend_id) {
        id_of_friend = friend_id;
        //DatabaseHandler database = new DatabaseHandler(this);
        //tvName.setText(database.getNameFromDatabase(friend_id));

        /**
         * find friend in database based on the friend's id
         */
        //get instance to database
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        // get reference to 'users' node and child with the id
        DatabaseReference ref = mDatabase.getReference("users").child(friend_id);

        // get data (name of friend) from firebase database and set view
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User value = dataSnapshot.getValue(User.class);

                TextView tvName = (TextView) findViewById(R.id.friendsPic);
                tvName.setText(value.getFirstname());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    /**
     * get returned values from date and time pickers
     */
//    private int year, month, day, hour, minute;
//
//    @Override
//    public void onReturnValueDate(Calendar calendar) {
//        this.year = calendar.get(Calendar.YEAR);
//        this.month = calendar.get(Calendar.MONTH);
//        this.day = calendar.get(Calendar.DAY_OF_MONTH);
//
//    }
//
//    @Override
//    public void onReturnValueTime(Calendar calendar) {
//        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
//        this.minute = calendar.get(Calendar.MINUTE);
//    }
}