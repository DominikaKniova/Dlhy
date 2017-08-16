package sk.dominika.dluhy.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.databases_objects.CurrentUser;
import sk.dominika.dluhy.databases_objects.Debt;

/**
 * Adapter for showing dialog of my debts with all friends.
 */
public class AllDebtAdapter extends RecyclerView.Adapter<AllDebtAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sumTextView;
        public TextView nameTextView;
        public TextView noteTextView;
        public ImageView menuImageView;


        public ViewHolder(View itemView) {
            super(itemView);
            sumTextView = (TextView) itemView.findViewById(R.id.dialog_debt_sum);
            nameTextView = (TextView) itemView.findViewById(R.id.dialog_debt_name);
            noteTextView = (TextView) itemView.findViewById(R.id.dialog_debt_note);
            menuImageView = (ImageView) itemView.findViewById(R.id.dialog_debt_menu);
        }
    }

    private List<Debt> memberDebts;

    private Context memberContext;

    public AllDebtAdapter(Context context, List<Debt> debts) {
        this.memberContext = context;
        this.memberDebts = debts;
    }

    @Override
    public AllDebtAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View debtView = inflater.inflate(R.layout.debt_all, parent, false);
        ViewHolder viewHolder = new ViewHolder(debtView);

        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(AllDebtAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Debt debt = memberDebts.get(position);

        //I owe somebody
        if (debt.getId_who().equals(CurrentUser.UserCurrent.id)) {
            TextView tSum = holder.sumTextView;
            tSum.setText("-" + String.valueOf(debt.getSum()));
            tSum.setTextColor(ContextCompat.getColor(memberContext, R.color.red_sum));

            TextView tName = holder.nameTextView;
            tName.setText(debt.getName_toWhom());
            tName.setTextColor(ContextCompat.getColor(memberContext, R.color.red_sum));

            TextView tNote = holder.noteTextView;
            tNote.setText(debt.getNote());
        }
        //they owe me
        else {
            TextView tSum = holder.sumTextView;
            tSum.setText("+" + String.valueOf(debt.getSum()));
            tSum.setTextColor(ContextCompat.getColor(memberContext, R.color.green_sum));

            TextView tName = holder.nameTextView;
            tName.setText(debt.getName_who());
            tName.setTextColor(ContextCompat.getColor(memberContext, R.color.green_sum));

            TextView tNote = holder.noteTextView;
            tNote.setText(debt.getNote());
        }
    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return memberDebts.size();
    }
}
