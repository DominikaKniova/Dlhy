package sk.dominika.dluhy.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.adapters.FriendAdapter;
import sk.dominika.dluhy.database_models.CurrentUser;
import sk.dominika.dluhy.database_models.Friend;
import sk.dominika.dluhy.database_models.Relationship;
import sk.dominika.dluhy.databases.MyFirebaseDatabaseHandler;
import sk.dominika.dluhy.decorations.DividerDecoration;
import sk.dominika.dluhy.listeners.DialogListener;

/**
 * Dialog for showing friend RecyclerView.
 */
public  class DialogFriends extends DialogFragment {

    private DialogListener mListener;
    public final String FRIENDS = "friends";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list_friends, container, false);
        RecyclerView recyclerViewFriends = (RecyclerView) view.findViewById(R.id.recycler_viewFriends);
        //get my friends list from bundle
        Bundle bundle = this.getArguments();
        List<Friend> listFriends = (List<Friend>) bundle.getSerializable(FRIENDS);
        //create RecyclerView from the list
        FriendAdapter adapter = new FriendAdapter(view.getContext(), listFriends,
                mListener, DialogFriends.this);
        recyclerViewFriends.addItemDecoration(new DividerDecoration(view.getContext()));
        recyclerViewFriends.setAdapter(adapter);
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter.notifyDataSetChanged();
        return view;
    }
}