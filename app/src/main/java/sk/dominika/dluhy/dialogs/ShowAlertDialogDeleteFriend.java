package sk.dominika.dluhy.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.databases.MyFirebaseDatabaseHandler;
import sk.dominika.dluhy.notifications.MyAlarmManager;

public class ShowAlertDialogDeleteFriend {

    /**
     * Show alert dialog with a message in an activity asking whether user wants to delete friend.
     * Dialog has two buttons- POSITIVE = friend and debts will be deleted, notifications will be synchronized
     *                         NEGATIVE = friend and debts won't be deleted
     * @param message message shown in AlertDialog
     * @param activity where AlertDialog is shown
     * @param id of friend or debt to be deleted
     */
    public static void showAlertDialog(final String message, final Activity activity, final String id) {
        android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(activity).create();
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        MyFirebaseDatabaseHandler.deleteFriendAndDebts(id, activity.getBaseContext());

//
                    }
                });
        alertDialog.show();
    }
}
