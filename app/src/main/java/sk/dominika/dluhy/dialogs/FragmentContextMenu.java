package sk.dominika.dluhy.dialogs;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import sk.dominika.dluhy.R;

public class FragmentContextMenu extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the list fragment and add it as our sole content.
        ContextMenuFragment content = new ContextMenuFragment();
        getFragmentManager().beginTransaction().add(android.R.id.content, content).commit();
    }

    public static class ContextMenuFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.debt, container, false);
            registerForContextMenu(root.findViewById(R.id.dialog_debt_menu));
            return root;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);
            menu.add(Menu.NONE, R.id.pay_debt, Menu.NONE, "Menu A");
            menu.add(Menu.NONE, R.id.edit_debt, Menu.NONE, "Menu B");
        }

        @Override
        public boolean onContextItemSelected(MenuItem item) {
            switch (item.getItemId()) {

            }
            return super.onContextItemSelected(item);
        }
    }
}
