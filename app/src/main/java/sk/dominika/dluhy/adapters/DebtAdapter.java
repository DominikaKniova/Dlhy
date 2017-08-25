package sk.dominika.dluhy.adapters;

import android.content.ClipData;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
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
import sk.dominika.dluhy.dialogs.ShowAlertDialogDeleteFriend;

/**
 * Adapter for showing list of debts.
 */
public class DebtAdapter extends RecyclerView.Adapter<DebtAdapter.ViewHolder> {
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sum_noteTextView;
        public ImageView menuImageView;
        public TextView namesTextView;
        public TextView reminderTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            sum_noteTextView = (TextView) itemView.findViewById(R.id.dialog_debt_sum_note);
            menuImageView = (ImageView) itemView.findViewById(R.id.dialog_debt_menu);
            namesTextView = (TextView) itemView.findViewById(R.id.dialog_debt_name);
            reminderTextView = (TextView) itemView.findViewById(R.id.reminder_debt);
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

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final DebtAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        final Debt debt = memberDebts.get(position);
        TextView tNames = holder.namesTextView;
        tNames.setText(debt.getName_who() + "->" +debt.getName_toWhom());
        TextView tSumNote = holder.sum_noteTextView;

        //I owe
        if (debt.getId_who().equals(CurrentUser.UserCurrent.id)) {
            tSumNote.setText("-" + String.valueOf(debt.getSum()) + ", " + debt.getNote());
            tNames.setTextColor(ContextCompat.getColor(memberContext, R.color.red_sum));
        }
        //they owe me
        else {
            tSumNote.setText("+" + String.valueOf(debt.getSum()) + ", " + debt.getNote());
            tNames.setTextColor(ContextCompat.getColor(memberContext, R.color.green_sum));
        }
        if (!debt.getDateOfAlert().equals("")) {
            TextView tReminder = holder.reminderTextView;
            tReminder.setVisibility(View.VISIBLE);
            tReminder.setText(debt.getDateOfAlert() + ", " + debt.getTimeOfAlert());
        }

        //check if debt is paid
        if (debt.getIsPaid().equals("true")) {
            tNames.setText("PAID " + debt.getName_who() + "->" + debt.getName_toWhom());
        } else {
            tNames.setText(debt.getName_who() + "->" + debt.getName_toWhom());
        }

        holder.menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(memberContext, holder.menuImageView);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_dialog_debts);

                //if debt is paid, set menu option's title to Unpay debt"
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
                                } else {
                                    MyFirebaseDatabaseHandler.updateIsPaid(debt.getId_debt(), "true");
                                }
                                break;
                            case R.id.edit_debt:
                                break;
                            case R.id.delete_debt:
                                MyFirebaseDatabaseHandler.deleteDebt(debt.getId_debt());
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });
    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return memberDebts.size();
    }
}
