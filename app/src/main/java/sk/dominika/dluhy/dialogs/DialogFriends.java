package sk.dominika.dluhy.dialogs;

import android.app.Activity;
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
import sk.dominika.dluhy.adapters.FriendAdapter;
import sk.dominika.dluhy.database_models.CurrentUser;
import sk.dominika.dluhy.database_models.Friend;
import sk.dominika.dluhy.databases.MyFirebaseDatabaseHandler;
import sk.dominika.dluhy.decorations.DividerDecoration;
import sk.dominika.dluhy.listeners.DialogListener;

/**
 * Dialog for showing recycleview.
 */
public  class DialogFriends extends DialogFragment {

    private DialogListener mListener;

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

        View view = inflater.inflate(R.layout.fragment_list_friends, container, false);

        /**
         * Get my friends from firebase database and store them in arraylist Friend.myFriends.
         */
        Friend.myFriends.clear();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("friends");
        ref.orderByChild("fromUserId").equalTo(CurrentUser.UserCurrent.id).addValueEventListener(MyFirebaseDatabaseHandler.listenerLoopThroughFriends);

        RecyclerView recycler_viewFriends = (RecyclerView) view.findViewById(R.id.recycler_viewFriends);
        FriendAdapter adapter = new FriendAdapter(view.getContext(), Friend.myFriends, mListener, DialogFriends.this);
        recycler_viewFriends.addItemDecoration(new DividerDecoration(view.getContext()));
        recycler_viewFriends.setAdapter(adapter);
        recycler_viewFriends.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }


}
