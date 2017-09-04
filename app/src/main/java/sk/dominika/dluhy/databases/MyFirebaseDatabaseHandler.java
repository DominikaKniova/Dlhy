package sk.dominika.dluhy.databases;


import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import sk.dominika.dluhy.activities.AddFriendActivity;
import sk.dominika.dluhy.adapters.DebtAdapter;
import sk.dominika.dluhy.database_models.CurrentUser;
import sk.dominika.dluhy.database_models.Debt;
import sk.dominika.dluhy.database_models.Friend;
import sk.dominika.dluhy.database_models.Relationship;
import sk.dominika.dluhy.database_models.User;
import sk.dominika.dluhy.decorations.DividerDecoration;
import sk.dominika.dluhy.notifications.MyAlarmManager;

public class MyFirebaseDatabaseHandler {
    private static Context context;

    private static String TAG = "MyFirebaseHandler";

    /**
     * Listener for getting all my debts from firebase database and store them in arraylist Debt.myDebts.
     */
//    public static ValueEventListener listenerAllMyDebts = new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            //loop through all debts in the database
//            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                Debt value = snapshot.getValue(Debt.class);
//                //add only my debts from database
//                if (value.getId_who().equals(CurrentUser.UserCurrent.id)
//                        || value.getId_toWhom().equals(CurrentUser.UserCurrent.id)) {
//                    Debt.myDebts.add(value);
//                }
//            }
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//            Log.e("The read failed: ", databaseError.getMessage());
//        }
//    };

    /**
     * Listener for looping through all friends in firebase database and adding them to arraylist Friend.myFriends.
     */
    public static ValueEventListener listenerLoopThroughFriends = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Relationship value = snapshot.getValue(Relationship.class);
                Friend friend = new Friend(value.getToUserName(), value.getToUserId());
                Friend.myFriends.add(friend);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e("The read failed: ", databaseError.getMessage());
        }
    };

    /**
     * Listener for looping through all my debts and creating future notifications.
     */
    private static ValueEventListener listenerNotifications = new ValueEventListener() {
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

                                //do not add notifications from past
                                if ((calendar.getTime().getTime() - System.currentTimeMillis() > 0)) {
                                    //add notification
                                    MyAlarmManager.scheduleNotification(
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
    };

    /**
     * Create notifications from my debts.
     *
     * @param context
     */
    public static void getAndCreateNotifications(Context context) {
        MyFirebaseDatabaseHandler.context = context;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("debts");
        ref.addListenerForSingleValueEvent(MyFirebaseDatabaseHandler.listenerNotifications);
    }

    /**
     * Get only debts created with the friend of id_friend.
     *
     * @param id_friend
     */
//    public static void getOurDebts(final String id_friend) {
//        FirebaseDatabase.getInstance().getReference("debts").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Debt debt = snapshot.getValue(Debt.class);
//                    if ((debt.getId_who().equals(CurrentUser.UserCurrent.id) && debt.getId_toWhom().equals(id_friend))
//                            || (debt.getId_who().equals(id_friend) && debt.getId_toWhom().equals(CurrentUser.UserCurrent.id))) {
//                        Debt.myDebts.add(debt);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//    }

    /**
     * Find the friend in database based on his id and initialize views with his data.
     * @param id_friend ID of friend.
     * @param name Reference to his name textview.
     * @param sum Reference to his sum textview.
     */
    public static void setFriendsProfileViews(String id_friend, final TextView name, final TextView sum,
                                              final CollapsingToolbarLayout cToolbar, final AppBarLayout appbar,
                                              final Activity activity) {
        // get reference to 'users' node and child with the id
        FirebaseDatabase.getInstance().getReference("users").child(id_friend).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get the object
                final User value = dataSnapshot.getValue(User.class);
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
            }

            @Override
            public void onCancelled(DatabaseError error) {
                /*If an user was deleted in database but the current user still has him in list of
                friends a clicks on him.*/
                Toast.makeText(activity, R.string.not_existing_user, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Calculate overall sum of only my debts or my debts with concrete friend.
     * Set sum view in profile.
     *
     * @param id       ID of me or ID of the friend.
     * @param textView Sum textview in profile.
     */
    public static void getOverallSum(final String id, final TextView textView) {

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = mDatabase.getReference("debts");

        if (id.equals(CurrentUser.UserCurrent.id)) {

            //calling method from my profile
            ref.addValueEventListener(new ValueEventListener() {
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
            ref.addValueEventListener(new ValueEventListener() {
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
     *
     * @param id_friend    ID of friend with whom the method is looking for debts in database.
     * @param spinner      (View) loading spinner.
     * @param recyclerView View for showing list of debts.
     * @param activity     An activity where the recyclerView is shown.
     */
    public static void loadDebtsWithFriendRecycleView(final String id_friend,
                                                      final ProgressBar spinner,
                                                      final RecyclerView recyclerView,
                                                      final Activity activity) {
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
                DebtAdapter adapter = new DebtAdapter(activity.getBaseContext(), listDebts);
                //data is loaded, so the loading spinner can be hidden
                spinner.setVisibility(View.GONE);
                //recyclerview is set up
                recyclerView.addItemDecoration(new DividerDecoration(activity.getBaseContext()));
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity.getBaseContext()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Delete friendship and all debts created with the friend from database and synchronize notifications.
     * @param id       ID of friend to be deleted.
     * @param context1
     */
    public static void deleteFriendAndDebts(final String id, final Context context1) {

        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        //delete debts from database
        final DatabaseReference refDebts = mDatabase.getReference("debts");
        refDebts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Debt value = snapshot.getValue(Debt.class);
                    if ((value.getId_who().equals(CurrentUser.UserCurrent.id) && value.getId_toWhom().equals(id))
                            || (value.getId_who().equals(id) && value.getId_toWhom().equals(CurrentUser.UserCurrent.id))) {
                        refDebts.child(value.getId_debt()).removeValue();
                    }
                }
                MyAlarmManager.syncNotifications(context1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //delete friendship
        final DatabaseReference refFriends = mDatabase.getReference("friends");
        refFriends.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Relationship value = snapshot.getValue(Relationship.class);
                    if ((value.getFromUserId().equals(CurrentUser.UserCurrent.id) && value.getToUserId().equals(id))
                            || (value.getFromUserId().equals(id) && value.getToUserId().equals(CurrentUser.UserCurrent.id))) {
                        refFriends.child(snapshot.getKey()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Check if new friend is user.
     * Yes - create AB, BA friendship in database "friends".
     * No - make toast that user doesn't exist*.
     *
     * @param item     Button from menu
     * @param email    Email of friend which is about to be added.
     * @param activity Activity from which the method is called.
     */
    public static void checkIfUserAndAdd(final MenuItem item, final TextInputEditText email, final Activity activity) {
        // get reference to database and to 'users' node
        final DatabaseReference refUsers = FirebaseDatabase.getInstance().getReference("users");
        refUsers.orderByChild("email").equalTo(email.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
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
     * Check if new adding relationship doesn't already exist.
     * If not then add new AB BA relationship to database.
     * Show result toast in activity
     *
     * @param fromUser
     * @param toUser   the user I am adding relationship with
     * @param activity
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
     * Delete debt with id id_debt from database.
     *
     * @param id_debt
     */
    public static void deleteDebt(String id_debt) {
        FirebaseDatabase.getInstance().getReference("debts").child(id_debt).removeValue();
    }

    /**
     * Overwrite debt state in database whether debt is paid or not.
     *
     * @param id_debt id of debt
     * @param isPaid  new state
     */
    public static void updateIsPaid(String id_debt, String isPaid) {
        FirebaseDatabase.getInstance().getReference("debts").child(id_debt).child("isPaid").setValue(isPaid);
    }
}