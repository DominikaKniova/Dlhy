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
import sk.dominika.dluhy.database_models.Debt;
import sk.dominika.dluhy.databases.MyFirebaseDatabaseHandler;
import sk.dominika.dluhy.decorations.DividerDecoration;

/**
 * Dialog for showing recycleview of all my debts with concrete friend.
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

        View view = inflater.inflate(R.layout.fragment_list_debts, container, false);

        /**
         * Get only debts created with the friend of id_friend.
         */
        Debt.myDebts.clear();
        MyFirebaseDatabaseHandler.getOurDebts(id_friend);

        RecyclerView recycler_viewDebts = (RecyclerView) view.findViewById(R.id.recycler_viewDebts);
        DebtAdapter adapter = new DebtAdapter(view.getContext(), Debt.myDebts);
        recycler_viewDebts.addItemDecoration(new DividerDecoration(view.getContext()));
        recycler_viewDebts.setAdapter(adapter);
        recycler_viewDebts.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }
}
