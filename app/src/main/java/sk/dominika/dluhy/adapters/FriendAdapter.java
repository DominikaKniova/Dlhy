package sk.dominika.dluhy.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sk.dominika.dluhy.activities.FriendProfileActivity;
import sk.dominika.dluhy.databases_objects.Friend;
import sk.dominika.dluhy.R;
import sk.dominika.dluhy.dialogs.DialogFriends;
import sk.dominika.dluhy.listeners.DialogListener;

/**
 * Adapter for dialog showing list of my friends.
 */
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView idTextView;
        public ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.friend_name);
            idTextView = (TextView) itemView.findViewById(R.id.id_friend);
            idTextView.setVisibility(View.INVISIBLE);
            icon = (ImageView) itemView.findViewById(R.id.friend_profile_icon);
        }
    }

    private List<Friend> memberFriends;

    private Context memberContext;

    private DialogListener memberListener;

    private DialogFriends dialogFriends;

    /**
     *
     * @param context
     * @param friends
     * @param listener
     * @param dFriends Reference to dialog DialogFriend, so that can be dismissed.
     */
    public FriendAdapter(Context context, List<Friend> friends, DialogListener listener, DialogFriends dFriends) {
        memberFriends = friends;
        memberContext = context;
        memberListener = listener;
        dialogFriends = dFriends;

    }

    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View friendView = inflater.inflate(R.layout.friend, parent, false);

        ViewHolder viewHolder = new ViewHolder(friendView);

        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final FriendAdapter.ViewHolder holder, final int position) {
        // Get the data model based on position
        final Friend friend = memberFriends.get(position);

        //setting names of items (friends)
        TextView textView = holder.nameTextView;
        textView.setText(friend.getFirstName());

//        //test
//        TextView textView1 = holder.idTextView;
//        textView1.setText(friend.getId());

        //icon imageview leads to friend's profile
        ImageView profileIcon = holder.icon;
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivity_profileFriend(friend.getId());
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberListener.onClick(memberFriends.get(position).getId());
                dialogFriends.dismiss();
            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return memberFriends.size();
    }

    /**
     * Starts FriendProfileActivity
     * @param id ID of friend to be shown.
     */
    private void newActivity_profileFriend(String id) {
        Intent intent = new Intent(memberContext, FriendProfileActivity.class);
        //add data to intent
        intent.putExtra("id", id);
        memberContext.startActivity(intent);
    }
}
