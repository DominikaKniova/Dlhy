package sk.dominika.dluhy.activities;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import sk.dominika.dluhy.databases.DebtsDBHandler;
import sk.dominika.dluhy.databases_objects.Debt;
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

        //expand date/time pickers
        final Button expButton = (Button) findViewById(R.id.expandableButton_alert);
        expButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                expandableButtonAlerts(view);
            }
        });

        TextInputEditText dateEditText = (TextInputEditText) findViewById(R.id.inputDate);
        //disable input/keyboard
        dateEditText.setFocusable(false);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        TextInputEditText timeEditText = (TextInputEditText) findViewById(R.id.inputTime);
        //disable input/keyboard
        timeEditText.setFocusable(false);
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        TextView firendPic = (TextView) findViewById(R.id.friendsPic);
        firendPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog_friends(v);
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


            //add debt to database
            DebtsDBHandler dbDebts = new DebtsDBHandler(this);
            Debt d = new Debt(edTxtName, edTxtSum, edTxtNote, edTxtDateCreated, edTxtTimeCreated);
            dbDebts.addDebtToDatabase(d);

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

    @Override
    public void onClick(String name) {
        TextView tvName = (TextView) findViewById(R.id.friendsPic);
        tvName.setText(name);
    }
}