package sk.dominika.dluhy.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.database_models.CurrentUser;
import sk.dominika.dluhy.database_models.Debt;
import sk.dominika.dluhy.databases.MyFirebaseDatabaseHandler;

/**
 * Adapter for showing list of debts in RecyclerView.
 */
public class DebtAdapter extends RecyclerView.Adapter<DebtAdapter.ViewHolder> {
    /**
     * Provide a direct reference to each of the views within a data item.
     * Item is one row in the list of debts. (= one debt).
     * It is used to cache the views within the item layout for fast access.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView sum_noteTextView;
        private ImageView menuImageView;
        private TextView namesTextView;
        private TextView notificationTextView;


        private ViewHolder(View itemView) {
            super(itemView);
            sum_noteTextView = (TextView) itemView.findViewById(R.id.dialog_debt_sum_note);
            menuImageView = (ImageView) itemView.findViewById(R.id.dialog_debt_menu);
            namesTextView = (TextView) itemView.findViewById(R.id.dialog_debt_name);
            notificationTextView = (TextView) itemView.findViewById(R.id.reminder_debt);
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
        View debtView = inflater.inflate(R.layout.item_debt, parent, false);
        DebtAdapter.ViewHolder viewHolder = new DebtAdapter.ViewHolder(debtView);
        return viewHolder;
    }

    /**
     * Populate data into the item through holder.
     * @param position Position of the clicked item in the ArrayList.
     */
    @Override
    public void onBindViewHolder(final DebtAdapter.ViewHolder holder, final int position) {
        // Get the data model based on position
        final Debt debt = memberDebts.get(position);

        //set views of the item
        holder.namesTextView.setText(debt.getName_who() + "->" + debt.getName_toWhom());

        if (debt.getId_who().equals(CurrentUser.UserCurrent.id)) {
            //I owe
            holder.sum_noteTextView.setText("-" + String.valueOf(debt.getSum()) + ", " + debt.getNote());
            holder.namesTextView.setTextColor(ContextCompat.getColor(memberContext, R.color.red_sum));
        } else {
            //they owe me
            holder.sum_noteTextView.setText("+" + String.valueOf(debt.getSum()) + ", " + debt.getNote());
            holder.namesTextView.setTextColor(ContextCompat.getColor(memberContext, R.color.green_sum));
        }

        //check if the debt has a notification, if yes then involve it in the item
        if (!debt.getDateOfAlert().equals("")) {
            holder.notificationTextView.setVisibility(View.VISIBLE);
            holder.notificationTextView.setText(debt.getDateOfAlert() + ", " + debt.getTimeOfAlert());
        }

        //check if debt is paid. YES = strike through text
        if (debt.getIsPaid().equals("true")) {
            holder.namesTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.sum_noteTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.notificationTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        /*create menu for every item in RecyclerView, containing of an option for
        deleting the debt (item) and an option for paying the debt (item)*/
        holder.menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a popup menu
                PopupMenu popup = new PopupMenu(memberContext, holder.menuImageView);
                //inflate menu from xml resource
                popup.inflate(R.menu.menu_dialog_debts);

                //if the debt is paid, set menu option's title to "Unpay debt"
                if (debt.getIsPaid().equals("true")) {
                    popup.getMenu().findItem(R.id.pay_debt).setTitle("Unpay debt");
                }
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.pay_debt:
                                if (debt.getIsPaid().equals("true")) {
                                    MyFirebaseDatabaseHandler.updateIsPaid(debt.getId_debt(), "false");
                                    memberDebts.get(position).setIsPaid("false");
                                    notifyItemChanged(position);
                                } else {
                                    MyFirebaseDatabaseHandler.updateIsPaid(debt.getId_debt(), "true");
                                    memberDebts.get(position).setIsPaid("true");
                                    notifyItemChanged(position);
                                }
                                break;
                            case R.id.delete_debt:
                                MyFirebaseDatabaseHandler.deleteDebt(debt.getId_debt());
                                memberDebts.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, memberDebts.size());
                                break;
                        }
                        return false;
                    }
                });
                //display the popup menu
                popup.show();
            }
        });
    }

    /**
     * Returns the total count of items in the list.
     */
    @Override
    public int getItemCount() {
        return memberDebts.size();
    }
}