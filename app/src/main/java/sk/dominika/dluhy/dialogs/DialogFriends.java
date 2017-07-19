package sk.dominika.dluhy.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.activities.MainActivity;
import sk.dominika.dluhy.adapters.FriendAdapter;
import sk.dominika.dluhy.kindOfBackend.AddFriend;
import sk.dominika.dluhy.listeners.DialogListener;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View.OnClickListener lis = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(((TextView) v).getText().toString());
                dismiss();
            }
        };

        View view = inflater.inflate(R.layout.fragment_list_friends, container, false);

        RecyclerView recycler_viewFriends = (RecyclerView) view.findViewById(R.id.recycler_viewFriends);
        FriendAdapter adapter = new FriendAdapter(view.getContext(), AddFriend.myFriends, lis);
        recycler_viewFriends.setAdapter(adapter);
        recycler_viewFriends.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }

}
