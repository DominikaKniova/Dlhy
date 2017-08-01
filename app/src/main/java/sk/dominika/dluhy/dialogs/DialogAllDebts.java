package sk.dominika.dluhy.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.adapters.AllDebtAdapter;
import sk.dominika.dluhy.databases.DatabaseHandler;
import sk.dominika.dluhy.databases_objects.Debt;

/**
 * Dialog for showing recycleview.
 */
public class DialogAllDebts extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_all_debts, container, false);

//        //get debts from database
//        DatabaseHandler dbDeb = new DatabaseHandler(this.getActivity());
//        Debt.myDebts= dbDeb.getMyDebtsFromDatabase();

        Debt.myDebts.clear();

        /**
         * Get debts from firebase database and store them in arraylist Debt.myDebts.
         */
        // get instance of database and reference to 'debts' node
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("debts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //loop through all debts in the database
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Debt value = snapshot.getValue(Debt.class);
                    Debt.myDebts.add(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed: " ,databaseError.getMessage());
                //TODO
            }
        });

        RecyclerView recycler_viewDebts = (RecyclerView) view.findViewById(R.id.recycler_viewDebts);
        AllDebtAdapter adapter = new AllDebtAdapter(view.getContext(), Debt.myDebts);
        recycler_viewDebts.setAdapter(adapter);
        recycler_viewDebts.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }

}