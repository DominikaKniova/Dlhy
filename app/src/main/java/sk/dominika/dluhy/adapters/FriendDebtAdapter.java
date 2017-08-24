package sk.dominika.dluhy.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.database_models.CurrentUser;
import sk.dominika.dluhy.database_models.Debt;

/**
 * Adapter for showing debts I share with concrete item_friend.
 */
public class FriendDebtAdapter extends RecyclerView.Adapter<FriendDebtAdapter.ViewHolder> {
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sum_noteTextView;
        public ImageView menuImageView;
        public TextView namesTextView;
        public LinearLayout linearLayoutFriendDebts;
        public CardView cardViewFriend;


        public ViewHolder(View itemView) {
            super(itemView);
            sum_noteTextView = (TextView) itemView.findViewById(R.id.dialog_debt_friend_sum_note);
            menuImageView = (ImageView) itemView.findViewById(R.id.dialog_debt_friend_menu);
            namesTextView = (TextView) itemView.findViewById(R.id.dialog_debt_friend_name);
            linearLayoutFriendDebts = (LinearLayout) itemView.findViewById(R.id.linear_layout_debt_friend);
            cardViewFriend = (CardView) itemView.findViewById(R.id.card_view_debts_friend);
        }
    }
    private List<Debt> memberDebts;

    private Context memberContext;

    public FriendDebtAdapter(Context context, List<Debt> debts) {
        this.memberContext = context;
        this.memberDebts = debts;
    }

    @Override
    public FriendDebtAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View debtView = inflater.inflate(R.layout.item_debt_friend, parent, false);
        FriendDebtAdapter.ViewHolder viewHolder = new FriendDebtAdapter.ViewHolder(debtView);

        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(FriendDebtAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Debt debt = memberDebts.get(position);

        //I owe
        if (debt.getId_who().equals(CurrentUser.UserCurrent.id)) {
            TextView tSumNote = holder.sum_noteTextView;
            tSumNote.setText("-" + String.valueOf(debt.getSum()) + ", " + debt.getNote());
//            tSum.setTextColor(ContextCompat.getColor(memberContext, R.color.red_sum));
            CardView cardView = holder.cardViewFriend;
            cardView.setBackgroundColor(ContextCompat.getColor(memberContext, R.color.light_red_background));

        }
        //they owe me
        else {
            TextView tSumNote = holder.sum_noteTextView;
            tSumNote.setText("+" + String.valueOf(debt.getSum()) + ", " + debt.getNote());
            CardView cardView = holder.cardViewFriend;
            cardView.setBackgroundColor(ContextCompat.getColor(memberContext, R.color.light_green_background));
//            tSum.setTextColor(ContextCompat.getColor(memberContext, R.color.green_sum));

//            TextView tNote = holder.noteTextView;
//            tNote.setText(debt.getNote());
        }

//        TextView tvSum = holder.sum_noteTextView;
//        tvSum.setText(String.valueOf(debt.getSum()));
//
//        TextView tvNote = holder.noteTextView;
//        tvNote.setText(debt.getNote());

    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return memberDebts.size();
    }
}
