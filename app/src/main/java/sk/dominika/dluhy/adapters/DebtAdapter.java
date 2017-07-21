package sk.dominika.dluhy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.databases_objects.Debt;


public class DebtAdapter extends RecyclerView.Adapter<DebtAdapter.ViewHolder> {

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

    public DebtAdapter(Context context, List<Debt> debts) {
        this.memberContext = context;
        this.memberDebts = debts;
    }

    @Override
    public DebtAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View debtView = inflater.inflate(R.layout.debt, parent, false);
        ViewHolder viewHolder = new ViewHolder(debtView);

        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(DebtAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Debt debt = memberDebts.get(position);

        TextView tvSum = holder.sumTextView;
        tvSum.setText(debt.getSum());

        TextView tvName = holder.nameTextView;
        tvName.setText(debt.getName());

        TextView tvNote = holder.noteTextView;
        tvNote.setText(debt.getNote());

        ImageView tvMenu = holder.menuImageView;
    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return memberDebts.size();
    }
}
