package sk.dominika.dluhy.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
        Debt.myDebts= dbDeb.getDebtsFromDatabase();

        RecyclerView recycler_viewFriends = (RecyclerView) view.findViewById(R.id.recycler_viewDebts);
        DebtAdapter adapter = new DebtAdapter(view.getContext(), Debt.myDebts);
        recycler_viewFriends.setAdapter(adapter);
        recycler_viewFriends.setLayoutManager(new LinearLayoutManager(view.getContext()));

//        setHasOptionsMenu(true);
//        setMenuVisibility(true);
//        registerForContextMenu(view.findViewById(R.id.dialog_debt_menu));

        return view;
    }

//    private void showMenu(View view) {
//        setMenuVisibility(true);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_dialog_debts, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.add("delete");
//    }
}
