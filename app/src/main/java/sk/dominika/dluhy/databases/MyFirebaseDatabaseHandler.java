package sk.dominika.dluhy.databases;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.activities.MainActivity;
import sk.dominika.dluhy.database_models.CurrentUser;
import sk.dominika.dluhy.database_models.Debt;
import sk.dominika.dluhy.database_models.Friend;
import sk.dominika.dluhy.database_models.Relationship;
import sk.dominika.dluhy.database_models.User;
import sk.dominika.dluhy.notifications.MyAlarmManager;

public class MyFirebaseDatabaseHandler {
    public static String TAG = "MyFirebaseHandler";

    /**
     * Listener for getting all my debts from firebase database and store them in arraylist Debt.myDebts.
     */
    public static ValueEventListener listenerAllMyDebts = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //loop through all debts in the database
            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                Debt value = snapshot.getValue(Debt.class);
                //add only my debts from database
                if ( value.getId_who().equals(CurrentUser.UserCurrent.id)
                        || value.getId_toWhom().equals(CurrentUser.UserCurrent.id)) {
                    Debt.myDebts.add(value);
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e("The read failed: " ,databaseError.getMessage());
        }
    };

    /**
     * Listener for looping through all friends in firebase database and adding them to arraylist Friend.myFriends.
     */
    public static ValueEventListener listenerLoopThroughFriends = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                Relationship value = snapshot.getValue(Relationship.class);
                Friend friend = new Friend(value.getToUserName(), value.getToUserId());
                Friend.myFriends.add(friend);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e("The read failed: " ,databaseError.getMessage());
        }
    };

    /**
     * Create notifications from my debts.
     * @param context
     */
    public static void getAndCreateNotifications(Context context) {
        Debt.myDebts.clear();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("debts");
        ref.addValueEventListener(MyFirebaseDatabaseHandler.listenerAllMyDebts);

        int id_notification = 0;

        //look through all my debts
        for (Debt debt : Debt.myDebts) {
            //check if debt has notification, if yes then add it
            if ( !(debt.getDateOfAlert().equals("") || debt.getTimeOfAlert().equals(""))){
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                try {
                    Date date = dateFormat.parse(debt.getDateOfAlert());
                    Date time = timeFormat.parse(debt.getTimeOfAlert());

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

                    //add notification
                    MyAlarmManager.scheduleNotification(
                            context,
                            calendar,
                            id_notification,
                            debt.getName_who() + "->" + debt.getName_toWhom(),
                            debt.getSum() + ", " + debt.getNote());

                    id_notification += 1;
                }
                catch (ParseException e) {
                    Log.d(TAG, e.getMessage());
                }
            }
        }
    }

    /**
     * Get only debts created with the friend of id_friend.
     * @param id_friend
     */
    public static void getOurDebts(final String id_friend){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("debts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Debt debt = snapshot.getValue(Debt.class);
                    if ((debt.getId_who().equals(CurrentUser.UserCurrent.id) && debt.getId_toWhom().equals(id_friend))
                            || (debt.getId_who().equals(id_friend) && debt.getId_toWhom().equals(CurrentUser.UserCurrent.id))) {
                        Debt.myDebts.add(debt);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Calculate overall sum of my debts or my debts with concrete friend.
     * Set sum view in profile.
     *
     * @param id ID of me or ID of the friend.
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
                    textView.setText(String.valueOf(sum));
                    if (sum <0) {
                        //red
                        textView.setTextColor(Color.parseColor("#df2d00"));
                    }
                    else {
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
                    textView.setText(String.valueOf(sum));
                    if (sum <0) {
                        //red
                        textView.setTextColor(Color.parseColor("#df2d00"));
                    }
                    else {
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
     * Delete friend with all debts created with them.
     * @param id ID of friend to be deleted.
     */
    public static void deleteFriendAndDebts(final String id) {

        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        //delete debts
        final DatabaseReference refDebts = mDatabase.getReference("debts");
        refDebts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Debt value = snapshot.getValue(Debt.class);
                    if((value.getId_who().equals(CurrentUser.UserCurrent.id) && value.getId_toWhom().equals(id))
                            || (value.getId_who().equals(id) && value.getId_toWhom().equals(CurrentUser.UserCurrent.id))) {
                        refDebts.child(value.getId_debt()).removeValue();
                    }
                }
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
                    if( (value.getFromUserId().equals(CurrentUser.UserCurrent.id) && value.getToUserId().equals(id))
                            || (value.getFromUserId().equals(id) && value.getToUserId().equals(CurrentUser.UserCurrent.id)) ) {
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
     * Check if new adding relationship doesn't already exist.
     * If not then add new relationship.
     * Show result toast in activity
     * @param fromUser
     * @param toUser the user I am adding relationship with
     * @param activity
     */
    public static void checkIfRelationshipExists(String fromUser, final User toUser, final Activity activity) {

        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = mDatabase.getReference("friends");
        ref.orderByChild("fromUserId").equalTo(fromUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean result = false;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Relationship relationship = snapshot.getValue(Relationship.class);
                    if (relationship.getToUserId().equals(toUser.getId())){
                        //relationship exists
                        result = true;
                    }
                }
                if(!result) {
                    //relationship doesn't exist
                    Relationship r = new Relationship(CurrentUser.UserCurrent.id, toUser.getId(), toUser.getFirstname());
                    DatabaseReference friends = mDatabase.getReference("friends");
                    String id = friends.push().getKey();
                    friends.child(id).setValue(r);

                    Relationship r2 = new Relationship(toUser.getId(), CurrentUser.UserCurrent.id, CurrentUser.UserCurrent.firstName);
                    id = friends.push().getKey();
                    friends.child(id).setValue(r2);
                    Toast.makeText(activity, R.string.friend_added, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(activity, R.string.friend_not_added, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
