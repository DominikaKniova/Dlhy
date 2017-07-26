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

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.databases.DatabaseHandler;
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
        TextView profile_name = (TextView) findViewById(R.id.profile_name);
        profile_name.setText(bundle.getString("firstname") + " " + bundle.getString("lastname"));

        //TODO: email, sum, pic ....

        //On click listener: Showing all out debts
        Button our_debts = (Button) findViewById(R.id.our_debts);
        our_debts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog_ourDebts(v);
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

    //Start dialog list of out debts
    private void showDialog_ourDebts(View view){
        DialogFragment newDialog = new DialogFriendDebts();
        newDialog.show(getFragmentManager(), "debts");

    }
}


