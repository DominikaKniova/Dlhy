package sk.dominika.dluhy.activities;

import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sk.dominika.dluhy.databases.DatabaseHandler;
import sk.dominika.dluhy.databases_objects.Debt;
import sk.dominika.dluhy.databases_objects.Friend;
import sk.dominika.dluhy.dialogs.DatePickerFragment;
import sk.dominika.dluhy.R;
import sk.dominika.dluhy.dialogs.DialogFriends;
import sk.dominika.dluhy.dialogs.TimePickerFragment;
import sk.dominika.dluhy.listeners.DialogListener;

public class NewDebtActivity extends AppCompatActivity implements DialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_debt);

        setTitle("New Debt");

        //expand date/time pickers
        final Button expButton = (Button) findViewById(R.id.expandableButton_alert);
        expButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
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
                }else {
                    arrow.setTag("arrForward");
                    arrow.setImageResource(R.drawable.ic_arrow_forward);
                }
            }
        });
    }

    @Override
    public void onBackPressed()
    {
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
        if(item.getItemId() == R.id.check) {

            //get data from inputs
            TextView edTxtName = (TextView) findViewById(R.id.friendsPic);
            TextInputEditText edTxtNote = (TextInputEditText) findViewById(R.id.textInput_note);
            TextInputEditText edTxtSum = (TextInputEditText) findViewById(R.id.textInput_money);
            TextInputEditText edTxtDateCreated = (TextInputEditText) findViewById(R.id.inputDate);
            TextInputEditText edTxtTimeCreated = (TextInputEditText) findViewById(R.id.inputTime);

//            //add debt_all to database
//            DatabaseHandler dbDebts = new DatabaseHandler(this);
//            Debt d = new Debt(id_of_friend, edTxtName, edTxtSum, edTxtNote, edTxtDateCreated, edTxtTimeCreated);
//            long newID = dbDebts.addDebtToDatabase(d);
//            //set debt_all's ID
//            d.setId_debt(newID);
//
//            //TODO  id of friend<<<
            Debt d = new Debt(id_of_friend, edTxtName, edTxtSum, edTxtNote, edTxtDateCreated, edTxtTimeCreated);

            /**
             * Add debt d to Firebase database.
             */
            //get instance to database
            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            // get reference to 'friends' node
            DatabaseReference ref = mDatabase.getReference("debts");
            //get id of new node
            String id = ref.push().getKey();
            d.setId_debt(id);
            //get a reference to location id and set the data at this location to the given value
            ref.child(id).setValue(d);

            finish();
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
        // get reference to 'friends' node and child with the id
        DatabaseReference ref = mDatabase.getReference("friends").child(friend_id);

        // get data (name of friend) from firebase database and set view
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Friend value = dataSnapshot.getValue(Friend.class);

                TextView tvName = (TextView) findViewById(R.id.friendsPic);
                tvName.setText(value.getFirstName());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //TODO
            }
        });
    }
}