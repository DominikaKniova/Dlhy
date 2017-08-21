package sk.dominika.dluhy.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.adapters.FriendDebtAdapter;
import sk.dominika.dluhy.database_models.CurrentUser;
import sk.dominika.dluhy.database_models.Debt;
import sk.dominika.dluhy.decorations.DividerDecoration;

/**
 * Dialog for showing recycleview.
 */
public class DialogFriendDebts extends DialogFragment{
    //sent id from FriendProfileActivity
    private String id_friend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id_friend = getArguments().getString("id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_friend_debts, container, false);

//        //get debts from database
//        DatabaseHandler dbDeb = new DatabaseHandler(this.getActivity());
//        Debt.myDebts = dbDeb.getOurDebtsWithFriend(id_friend);

        Debt.myDebts.clear();

        /**
         * Get only debts created with the friend of id_friend.
         */
        //get instance to database
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        // get reference to 'debts' node
        DatabaseReference ref = mDatabase.getReference("debts");
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
//        ref.orderByChild("id_friend").equalTo(id_friend).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Debt value = snapshot.getValue(Debt.class);
//                    Debt.myDebts.add(value);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                //TODO
//            }
//        });

        RecyclerView recycler_viewDebts = (RecyclerView) view.findViewById(R.id.recycler_viewFriendDebts);
        FriendDebtAdapter adapter = new FriendDebtAdapter(view.getContext(), Debt.myDebts);
        recycler_viewDebts.addItemDecoration(new DividerDecoration(view.getContext()));
        recycler_viewDebts.setAdapter(adapter);
        recycler_viewDebts.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }
}
