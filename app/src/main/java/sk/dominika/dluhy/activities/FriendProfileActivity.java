package sk.dominika.dluhy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.adapters.DebtAdapter;
import sk.dominika.dluhy.database_models.CurrentUser;
import sk.dominika.dluhy.database_models.Debt;
import sk.dominika.dluhy.databases.MyFirebaseDatabaseHandler;
import sk.dominika.dluhy.database_models.User;
import sk.dominika.dluhy.decorations.DividerDecoration;
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

        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);

        /**
         * Find friend (user) in database based on the friend's id
         */
        //get instance to database
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        // get reference to 'users' node and child with the id and get data and set views
        FirebaseDatabase.getInstance().getReference("users").child(id_friend).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                final User value = dataSnapshot.getValue(User.class);
                TextView name = (TextView) findViewById(R.id.profile_name);
                name.setText(value.getFirstname() + " " + value.getLastname());
                TextView sum = (TextView) findViewById(R.id.profile_sum);
                MyFirebaseDatabaseHandler.getOverallSum(value.getId(), sum);

                appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    boolean isShow = false;
                    int scrollRange = -1;

                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        if (scrollRange == -1) {
                            scrollRange = appBarLayout.getTotalScrollRange();
                        }
                        if (scrollRange + verticalOffset == 0) {
                            collapsingToolbar.setTitle(value.getFirstname() + " " + value.getLastname());
                            isShow = true;
                        } else if (isShow) {
                            collapsingToolbar.setTitle("");
                            isShow = false;
                        }
                    }
                });
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
        Intent intent_debt = new Intent(this, MyProfileActivity.class);
        startActivity(intent_debt);
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

    private void showRecycleView() {
        /**
         * Get debts from firebase database and store them in arraylist Debt.myDebts.
         */

        final ProgressBar spinner = (ProgressBar) findViewById(R.id.progress_bar);
        spinner.setVisibility(View.VISIBLE);

        FirebaseDatabase.getInstance().getReference("debts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Debt> listDebts = new ArrayList<Debt>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Debt debt = snapshot.getValue(Debt.class);
                    if ((debt.getId_who().equals(CurrentUser.UserCurrent.id) && debt.getId_toWhom().equals(id_friend))
                            || (debt.getId_who().equals(id_friend) && debt.getId_toWhom().equals(CurrentUser.UserCurrent.id))) {
                        listDebts.add(debt);
                    }
                }
                RecyclerView recycler_viewDebts = (RecyclerView) findViewById(R.id.recycler_viewDebts);
                DebtAdapter adapter = new DebtAdapter(getBaseContext(), listDebts);
                spinner.setVisibility(View.GONE);
                recycler_viewDebts.addItemDecoration(new DividerDecoration(getBaseContext()));
                recycler_viewDebts.setAdapter(adapter);
                recycler_viewDebts.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }
}