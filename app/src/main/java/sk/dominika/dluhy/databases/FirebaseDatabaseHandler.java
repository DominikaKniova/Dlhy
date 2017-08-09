package sk.dominika.dluhy.databases;


import android.graphics.Color;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sk.dominika.dluhy.databases_objects.CurrentUser;
import sk.dominika.dluhy.databases_objects.Debt;
import sk.dominika.dluhy.databases_objects.Relationship;

public class FirebaseDatabaseHandler {

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
                        textView.setTextColor(Color.RED);
                    }
                    else {
                        textView.setTextColor(Color.GREEN);
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
                        textView.setTextColor(Color.RED);
                    }
                    else {
                        textView.setTextColor(Color.GREEN);
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
        refDebts.addValueEventListener(new ValueEventListener() {
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
        //TODO vyriesit co ked nie je user
        final DatabaseReference refFriends = mDatabase.getReference("friends");
        refFriends.addValueEventListener(new ValueEventListener() {
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
}
