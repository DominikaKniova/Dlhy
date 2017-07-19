package sk.dominika.dluhy.dialogs;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.adapters.FriendAdapter;
import sk.dominika.dluhy.kindOfBackend.AddFriend;

public  class DialogFriends extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tv = (TextView) getView().findViewById(R.id.friend_name);
        tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_friends, container, false);

        RecyclerView recycler_viewFriends = (RecyclerView) view.findViewById(R.id.recycler_viewFriends);
        FriendAdapter adapter = new FriendAdapter(view.getContext(), AddFriend.myFriends);
        recycler_viewFriends.setAdapter(adapter);
        recycler_viewFriends.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }

}
