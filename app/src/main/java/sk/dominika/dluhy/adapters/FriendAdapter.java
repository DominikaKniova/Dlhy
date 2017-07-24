package sk.dominika.dluhy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sk.dominika.dluhy.databases_objects.Friend;
import sk.dominika.dluhy.R;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView idTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.friend_name);
            idTextView = (TextView) itemView.findViewById(R.id.idid);
        }
    }

    private List<Friend> memberFriends;

    private Context memberContext;

    private View.OnClickListener memberListener;

    public FriendAdapter(Context context, List<Friend> friends, View.OnClickListener listener) {
        memberFriends = friends;
        memberContext = context;
        memberListener = listener;

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
    public void onBindViewHolder(FriendAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Friend friend = memberFriends.get(position);

        TextView textView = holder.nameTextView;
        textView.setText(friend.getFirstName());

        //test
        TextView textView1 = holder.idTextView;
        textView1.setText(Long.toString(friend.getId()));

        textView.setOnClickListener(memberListener);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return memberFriends.size();
    }
}