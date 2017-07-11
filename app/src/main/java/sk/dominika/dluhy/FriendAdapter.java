package sk.dominika.dluhy;

import android.content.Context;
import android.icu.util.Freezable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.friend_name);
        }
    }

    private List<Friend> memberFriends;

    private Context memberContext;

    public FriendAdapter(Context context, List<Friend> friends) {
        memberFriends = friends;
        memberContext = context;
    }

    private Context getContext() {
        return memberContext;
    }

    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View friendView = inflater.inflate(R.layout.friend, parent, false);

        ViewHolder viewHolder = new ViewHolder(friendView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FriendAdapter.ViewHolder holder, int position) {
        Friend friend = memberFriends.get(position);

        TextView textView = holder.nameTextView;
        textView.setText(friend.getName());

    }

    @Override
    public int getItemCount() {
        return memberFriends.size();
    }
}