package sk.dominika.dluhy.dialogs;

import android.app.Activity;
import android.content.DialogInterface;

/**
 * Class for handling alert dialogs.
 */
public class ShowAlertDialogNeutral {
    /**
     * Show alert dialog with a message in an activity.
     * Dialog has only one neutral button.
     * @param message Message shown in the AlertDialog.
     * @param activity Where the AlertDialog is shown.
     */
    public static void showAlertDialog(String message, Activity activity) {
        android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(activity).create();
        alertDialog.setMessage(message);
        alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
