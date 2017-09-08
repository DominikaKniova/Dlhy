package sk.dominika.dluhy.databases;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.adapters.DebtAdapter;
import sk.dominika.dluhy.adapters.FriendAdapter;
import sk.dominika.dluhy.database_models.CurrentUser;
import sk.dominika.dluhy.database_models.Debt;
import sk.dominika.dluhy.database_models.Friend;
import sk.dominika.dluhy.database_models.Relationship;
import sk.dominika.dluhy.database_models.User;
import sk.dominika.dluhy.decorations.DividerDecoration;
import sk.dominika.dluhy.dialogs.DialogFriends;
import sk.dominika.dluhy.listeners.DialogListener;
import sk.dominika.dluhy.notifications.MyNotificationManager;

public class MyFirebaseDatabaseHandler {

    private static String TAG = "MyFirebaseHandler";

    /**
     * Create notifications from my debts.
     */
    public static void getAndCreateNotifications(final Context context) {
        //Listener for looping through all my debts and creating future notifications.
        FirebaseDatabase.getInstance().getReference("debts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int id_notification = 0;

                        //look through all my debts
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Debt value = snapshot.getValue(Debt.class);
                            //add only unpaid debts
                            if (value.getIsPaid().equals("false")) {
                                //add only my debts from database
                                if (value.getId_who().equals(CurrentUser.UserCurrent.id)
                                        || value.getId_toWhom().equals(CurrentUser.UserCurrent.id)) {
                                    //check if debt has notification, if yes then add it
                                    if (!(value.getDateOfAlert().equals("") || value.getTimeOfAlert().equals(""))) {
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                        try {
                                            //create date and time
                                            Date date = dateFormat.parse(value.getDateOfAlert());
                                            Date time = timeFormat.parse(value.getTimeOfAlert());

                                            Calendar calendarDate = Calendar.getInstance();
                                            calendarDate.setTime(date);
                                            Calendar calendarTime = Calendar.getInstance();
                                            calendarTime.setTime(time);

                                            Calendar calendar = Calendar.getInstance();
                                            calendar.set(Calendar.YEAR, calendarDate.get(Calendar.YEAR));
                                            calendar.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
                                            calendar.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
                                            calendar.set(Calendar.DAY_OF_MONTH, calendarDate.get(Calendar.DAY_OF_MONTH));
                                            calendar.set(Calendar.MONTH, calendarDate.get(Calendar.MONTH));

                                            //check if I am not adding notification from the past
                                            if ((calendar.getTime().getTime() - System.currentTimeMillis() > 0)) {
                                                //add notification
                                                MyNotificationManager.scheduleNotification(
                                                        context,
                                                        calendar,
                                                        id_notification,
                                                        value.getName_who() + "->" + value.getName_toWhom(),
                                                        value.getSum() + ", " + value.getNote());

                                                id_notification += 1;
                                            }
                                        } catch (ParseException e) {
                                            Log.d(TAG, e.getMessage());
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    /**
     * Find current user of id_user in database and initialize views with his data.
     * @param id_user  ID of user.
     * @param name     Reference to name TextView.
     * @param sum      Reference to sum TextView.
     * @param activity Activity from which the method is called.
     */
    public static void setCurrentUserViews(String id_user, final TextView name,
                                           final TextView sum, final Activity activity) {
        //find the user in database to get his data
        FirebaseDatabase.getInstance().getReference("users").child(id_user)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        //set CurrentUser.UserCurrent static class with current user's data
                        CurrentUser.setData(user.getFirstname(), user.getLastname(), user.getEmail());
                        //set views
                        name.setText(CurrentUser.UserCurrent.firstName + " " + CurrentUser.UserCurrent.lastName);
                        MyFirebaseDatabaseHandler.getOverallSum(CurrentUser.UserCurrent.id, sum);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //if user with the id does not exist
                        Toast.makeText(activity, "User doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Get the name of the friend from the database based on his id and set name TextView.
     * @param id_friend Id of friend.
     * @param name Reference to TextView.
     * @param activity Activity from which the method is called.
     */
    public static void getFriendNameFromDatabase(String id_friend, final TextView name, final Activity activity) {
        FirebaseDatabase.getInstance().getReference("users").child(id_friend)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User value = dataSnapshot.getValue(User.class);
                        if (value != null) {
                            name.setText(value.getFirstname());
                        } else {
                            /*If an user was deleted in database but the current user still has him in list of
                            friends a clicks on him.*/
                            Toast.makeText(activity, R.string.not_existing_user, Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(activity, R.string.not_existing_user, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    /**
     * Find the friend in database based on his id and initialize views with his data.
     * @param id_friend ID of friend.
     * @param name Reference to his name TextView.
     * @param sum Reference to his sum TextView.
     */
    public static void setFriendsProfileViews(String id_friend, final TextView name, final TextView sum,
                                              final CollapsingToolbarLayout cToolbar, final AppBarLayout appbar,
                                              final Activity activity) {
        // get reference to 'users' node and child with the id
        FirebaseDatabase.getInstance().getReference("users").child(id_friend)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get the object
                final User value = dataSnapshot.getValue(User.class);
                if (value != null) {
                    //set name view
                    name.setText(value.getFirstname() + " " + value.getLastname());
                    //get overall sum and set sum view
                    MyFirebaseDatabaseHandler.getOverallSum(value.getId(), sum);
                    //animation for toolbar title when collapsing
                    appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                        boolean isShow = false;
                        int scrollRange = -1;

                        @Override
                        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                            if (scrollRange == -1) {
                                scrollRange = appBarLayout.getTotalScrollRange();
                            }
                            if (scrollRange + verticalOffset == 0) {
                                cToolbar.setTitle(value.getFirstname() + " " + value.getLastname());
                                isShow = true;
                            } else if (isShow) {
                                cToolbar.setTitle("");
                                isShow = false;
                            }
                        }
                    });
                } else {
                    /*If an user was deleted in database but the current user still has him in list of
                    friends a clicks on him.*/
                    Toast.makeText(activity, R.string.not_existing_user, Toast.LENGTH_SHORT).show();
                    activity.finish();
                }

            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    /**
     * Create debt (object) containing of: id of the debt, who to whom owes (ids and names),
     * sum, note, date and time of notification (if added), and information whether the debt
     * is paid or not. When a new debt is created, it is always initialized with isPaid = false.
     * At first, method decides who owes whom, by looking at the position of arrow ImageView.
     * Then id of the new debt is created and the debt object is created which is added
     * to database. If user picked date and time for notification, all notifications are synced.
     * @param idFriend If of the friend I am creating a debt with.
     * @param name     First name of the friend.
     * @param note     Reference to note TextView.
     * @param sum      Reference to sum TextView.
     * @param date     Reference to date TextView.
     * @param time     Reference to time TextView.
     * @param arrow    Reference to arrow ImageView.
     * @param activity Activity from which this method is called.
     */
    public static void createDebt(String idFriend, TextView name, TextView note, TextView sum,
                                  TextView date, TextView time, ImageView arrow, Activity activity) {
        //find out if I owe friend money or the other way round
        boolean heOwesMe;
        String imageTag = (String) arrow.getTag();
        if (imageTag.equals("arrForward")) {
            heOwesMe = false;
        } else {
            heOwesMe = true;
        }
        // get instance to database and reference to 'debts' node
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("debts");
        //get id of a new node
        String id = ref.push().getKey();
        Debt debt;
        if (!heOwesMe) {
            //I owe
            //create a debt object
            debt = new Debt(
                    id,
                    CurrentUser.UserCurrent.id,
                    idFriend,
                    CurrentUser.UserCurrent.firstName,
                    name.getText().toString(),
                    Float.parseFloat(sum.getText().toString()),
                    note.getText().toString(),
                    date.getText().toString(),
                    time.getText().toString(),
                    "false");
        } else {
            //he owes
            //get id of new node
            debt = new Debt(
                    id,
                    idFriend,
                    CurrentUser.UserCurrent.id,
                    name.getText().toString(),
                    CurrentUser.UserCurrent.firstName,
                    Float.parseFloat(sum.getText().toString()),
                    note.getText().toString(),
                    date.getText().toString(),
                    time.getText().toString(),
                    "false");
        }
        //get a reference to location 'id' and add this new debt to this location
        ref.child(id).setValue(debt);

        //if alert is added then sync new notification with old ones
        if (!(date.getText().toString().equals("") && time.getText().toString().equals(""))) {
            MyNotificationManager.syncNotifications(activity.getBaseContext());
        }
    }

    /**
     * Calculate overall sum of only my debts or my debts with concrete friend based on
     * whose id I am sending to the method.
     * Set sum view in profile.
     * @param id       ID of me or ID of the friend.
     * @param textView Sum TextView in profile.
     */
    public static void getOverallSum(final String id, final TextView textView) {

        if (id.equals(CurrentUser.UserCurrent.id)) {
            //calling method from my profile
            FirebaseDatabase.getInstance().getReference("debts")
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    float sum = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Debt value = snapshot.getValue(Debt.class);

                        //do not include debts that are paid
                        if (value.getIsPaid().equals("false")) {
                            //calculate only my debts from database
                            if (value.getId_who().equals(id)
                                    || value.getId_toWhom().equals(id)) {

                                //validate if + or -
                                if (value.getId_who().equals(id)) {
                                    //I owe => -
                                    sum -= value.getSum();
                                } else {
                                    //they owe me => +
                                    sum += value.getSum();
                                }
                            }
                        }
                    }
                    textView.setText(String.valueOf(sum));
                    if (sum < 0) {
                        //red
                        textView.setTextColor(Color.parseColor("#df2d00"));
                    } else {
                        //green
                        textView.setTextColor(Color.parseColor("#52df00"));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            //calling method from friend's profile
            //id is friend's
            FirebaseDatabase.getInstance().getReference("debts")
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    float sum = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Debt value = snapshot.getValue(Debt.class);

                        //do not include debts that are paid
                        if (value.getIsPaid().equals("false")) {
                            //calculate only my debts with the friend from database
                            if ((value.getId_who().equals(CurrentUser.UserCurrent.id) && value.getId_toWhom().equals(id))
                                    || (value.getId_who().equals(id) && value.getId_toWhom().equals(CurrentUser.UserCurrent.id))) {

                                //validate if + or -
                                if (value.getId_who().equals(id)) {
                                    //they owe me => +
                                    sum += value.getSum();
                                } else {
                                    //I owe => -
                                    sum -= value.getSum();
                                }
                            }
                        }
                    }
                    textView.setText(String.valueOf(sum));
                    if (sum < 0) {
                        //red
                        textView.setTextColor(Color.parseColor("#df2d00"));
                    } else {
                        //green
                        textView.setTextColor(Color.parseColor("#52df00"));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    /**
     * Connect to firebase and load only my debts created with the friend of id to recyclerView.
     * @param id_friend    ID of friend with whom the method is looking for debts in database.
     * @param spinner      (View) loading spinner.
     * @param recyclerView View for showing list of debts.
     * @param activity     An activity where the recyclerView is shown.
     */
    public static void loadDebtsWithFriendRecyclerView(final String id_friend,
                                                       final ProgressBar spinner,
                                                       final RecyclerView recyclerView,
                                                       final Activity activity,
                                                       final SwipeRefreshLayout swipeRefreshLayout) {
        //get only the debts created with the friend from database
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
                //set up recycle view from the arraylist of the debts, and its adapter
                final DebtAdapter adapter = new DebtAdapter(activity.getBaseContext(), listDebts);
                //data is loaded, so the loading spinner can be hidden
                spinner.setVisibility(View.GONE);
                recyclerView.addItemDecoration(new DividerDecoration(activity.getBaseContext()));
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity.getBaseContext()));
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Connect to firebase and load all my debts created to recyclerView.
     * @param spinner      (View) loading spinner.
     * @param recyclerView View for showing list of debts.
     * @param activity     An activity where the recyclerView is shown.
     * @param swipeRefreshLayout Set refresh listener on swipe.
     */
    public static void loadMyDebtsRecyclerView(final ProgressBar spinner,
                                               final RecyclerView recyclerView,
                                               final Activity activity,
                                               final SwipeRefreshLayout swipeRefreshLayout) {
        //get debts from database
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
                //set up recycle view from the ArrayList of the debts, and its adapter
                final DebtAdapter adapter = new DebtAdapter(activity.getBaseContext(), listDebts);
                recyclerView.addItemDecoration(new DividerDecoration(activity.getBaseContext()));
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity.getBaseContext()));
                spinner.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Get my friends from database and load them to recyclerView.
     */
    public static void loadFriendsRecyclerView(final RecyclerView recyclerView,
                                               final DialogListener dialogListener,
                                               final DialogFriends dialogFriends,
                                               final View view) {
        FirebaseDatabase.getInstance().getReference("friends")
                .orderByChild("fromUserId")
                .equalTo(CurrentUser.UserCurrent.id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Friend> listFriends = new ArrayList<Friend>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Relationship value = snapshot.getValue(Relationship.class);
                            Friend friend = new Friend(value.getToUserName(), value.getToUserId());
                            listFriends.add(friend);
                        }
                        FriendAdapter adapter = new FriendAdapter(view.getContext(), listFriends,
                                dialogListener, dialogFriends);
                        recyclerView.addItemDecoration(new DividerDecoration(view.getContext()));
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("The read failed: ", databaseError.getMessage());
                    }
                });
    }

    /**
     * Delete friendship and all debts created with the friend from database and synchronize notifications.
     * @param id       ID of friend to be deleted.
     */
    public static void deleteFriendAndDebts(final String id, final Context context1) {
        //delete debts from database
        FirebaseDatabase.getInstance().getReference("debts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Debt value = snapshot.getValue(Debt.class);
                    if ((value.getId_who().equals(CurrentUser.UserCurrent.id) && value.getId_toWhom().equals(id))
                            || (value.getId_who().equals(id) && value.getId_toWhom().equals(CurrentUser.UserCurrent.id))) {
                        FirebaseDatabase.getInstance().getReference("debts").child(value.getId_debt()).removeValue();
                    }
                }
                MyNotificationManager.syncNotifications(context1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //delete friendship
        FirebaseDatabase.getInstance().getReference("friends")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Relationship value = snapshot.getValue(Relationship.class);
                    if ((value.getFromUserId().equals(CurrentUser.UserCurrent.id) && value.getToUserId().equals(id))
                            || (value.getFromUserId().equals(id) && value.getToUserId().equals(CurrentUser.UserCurrent.id))) {
                        FirebaseDatabase.getInstance().getReference("friends").child(snapshot.getKey()).removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Check if the new friend is user.
     * Yes - create AB, BA friendship in database "friends".
     * No - make toast that user doesn't exist*.
     * @param item     Button from menu.
     * @param email    Email of friend which is about to be added.
     * @param activity Activity from which the method is called.
     */
    public static void checkIfUserAndAdd(final MenuItem item, final TextInputEditText email, final Activity activity) {
        FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("email")
                .equalTo(email.getText().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    //user exists
                    User user = new User();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(User.class);
                    }
                    /*Firstly check whether the relationship doesn't already exist.
                    if not then add new relationship*/
                    checkIfRelationshipExistsAndAdd(CurrentUser.UserCurrent.id, user, activity);
                    activity.finish();
                    item.setEnabled(true);
                } else {
                    //user does not exist
                    Toast.makeText(activity, R.string.not_existing_user, Toast.LENGTH_SHORT).show();
                    item.setEnabled(true);
                    email.setText("");
                    email.setError("Invalid email");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Check if the new adding relationship doesn't already exist.
     * If not then add new AB BA relationship to database.
     * Show result toast in activity.
     * @param fromUser Me.
     * @param toUser   the user I am adding relationship with.
     * @param activity Activity from which this method is called.
     */
    public static void checkIfRelationshipExistsAndAdd(String fromUser, final User toUser, final Activity activity) {
        FirebaseDatabase.getInstance().getReference("friends")
                .orderByChild("fromUserId").equalTo(fromUser)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean result = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Relationship relationship = snapshot.getValue(Relationship.class);
                    if (relationship.getToUserId().equals(toUser.getId())) {
                        //relationship exists
                        result = true;
                    }
                }
                if (!result) {
                    //relationship doesn't exist
                    //create AB relationship
                    Relationship r = new Relationship(
                            CurrentUser.UserCurrent.id,
                            CurrentUser.UserCurrent.firstName + " " + CurrentUser.UserCurrent.lastName,
                            toUser.getId(),
                            toUser.getFirstname() + " " + toUser.getLastname());
                    //add AB relationship to database "friends"
                    DatabaseReference friends = FirebaseDatabase.getInstance().getReference("friends");
                    //id of new item in database
                    String id = friends.push().getKey();
                    friends.child(id).setValue(r);

                    //create BA relationship
                    Relationship r2 = new Relationship(toUser.getId(),
                            toUser.getFirstname() + " " + toUser.getLastname(),
                            CurrentUser.UserCurrent.id,
                            CurrentUser.UserCurrent.firstName + " " + CurrentUser.UserCurrent.lastName);
                    //add BA relationship to database "friends"
                    //id of new item in database
                    id = friends.push().getKey();
                    friends.child(id).setValue(r2);
                    Toast.makeText(activity, R.string.friend_added, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(activity, R.string.friend_not_added, Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Delete debt with the id id_debt from database.
     */
    public static void deleteDebt(String id_debt) {
        FirebaseDatabase.getInstance().getReference("debts").child(id_debt).removeValue();
    }

    /**
     * Update debt state in database whether debt is paid or not.
     * @param id_debt id of debt
     * @param isPaid  new state
     */
    public static void updateIsPaid(String id_debt, String isPaid) {
        FirebaseDatabase.getInstance().getReference("debts").child(id_debt).child("isPaid").setValue(isPaid);
    }
}