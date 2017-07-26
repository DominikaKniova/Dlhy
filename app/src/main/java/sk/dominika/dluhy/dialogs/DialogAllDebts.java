package sk.dominika.dluhy.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.adapters.AllDebtAdapter;
import sk.dominika.dluhy.databases.DatabaseHandler;
import sk.dominika.dluhy.databases_objects.Debt;

public class DialogAllDebts extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_all_debts, container, false);

        //get debts from database
        DatabaseHandler dbDeb = new DatabaseHandler(this.getActivity());
        Debt.myDebts= dbDeb.getMyDebtsFromDatabase();
        //TODO: myDebts, myFriends, mozno netreba v getMyDebt... sa vytvara novy list

        RecyclerView recycler_viewDebts = (RecyclerView) view.findViewById(R.id.recycler_viewDebts);
        AllDebtAdapter adapter = new AllDebtAdapter(view.getContext(), Debt.myDebts);
        recycler_viewDebts.setAdapter(adapter);
        recycler_viewDebts.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }

}