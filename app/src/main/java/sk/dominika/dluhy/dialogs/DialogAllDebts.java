package sk.dominika.dluhy.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.adapters.AllDebtAdapter;
import sk.dominika.dluhy.database_models.Debt;
import sk.dominika.dluhy.databases.MyFirebaseDatabaseHandler;
import sk.dominika.dluhy.decorations.DividerDecoration;

/**
 * Dialog for showing recycleview.
 */
public class DialogAllDebts extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_all_debts, container, false);

        /**
         * Get debts from firebase database and store them in arraylist Debt.myDebts.
         */
        Debt.myDebts.clear();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("debts");
        ref.addValueEventListener(MyFirebaseDatabaseHandler.listenerAllMyDebts);

        RecyclerView recycler_viewDebts = (RecyclerView) view.findViewById(R.id.recycler_viewDebts);
        AllDebtAdapter adapter = new AllDebtAdapter(view.getContext(), Debt.myDebts);
        recycler_viewDebts.addItemDecoration(new DividerDecoration(view.getContext()));
        recycler_viewDebts.setAdapter(adapter);
        recycler_viewDebts.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }

}