package sk.dominika.dluhy.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.adapters.DebtAdapter;
import sk.dominika.dluhy.databases.DatabaseHandler;
import sk.dominika.dluhy.databases_objects.Debt;

public class DialogDebts extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_debts, container, false);

        //get debts from database
        DatabaseHandler dbDeb = new DatabaseHandler(this.getActivity());
        Debt.myDebts= dbDeb.getMyDebtsFromDatabase();
//        Debt.myDebts = dbDeb.getOurDebtsWithFriend(2);

        RecyclerView recycler_viewFriends = (RecyclerView) view.findViewById(R.id.recycler_viewDebts);
        DebtAdapter adapter = new DebtAdapter(view.getContext(), Debt.myDebts);
        recycler_viewFriends.setAdapter(adapter);
        recycler_viewFriends.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }

}
