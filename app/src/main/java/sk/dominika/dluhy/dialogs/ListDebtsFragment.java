package sk.dominika.dluhy.dialogs;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.adapters.DebtAdapter;
import sk.dominika.dluhy.databases_objects.Debt;


public class ListDebtsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.dialog_debts, container, false);

        //create recycle view for debts
        RecyclerView recycler_viewDebts = (RecyclerView) view.findViewById(R.id.recycler_viewDebts);
        DebtAdapter adapter = new DebtAdapter(view.getContext(), Debt.myDebts);
        recycler_viewDebts.setAdapter(adapter);
        recycler_viewDebts.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }
}
