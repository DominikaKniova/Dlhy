package sk.dominika.dluhy.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.DatePicker;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.interfaces.ReturnValueFragment;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = sdf.format(c.getTime());

        TextInputEditText inputDate = (TextInputEditText) getActivity().findViewById(R.id.inputDate);
        inputDate.setText(formattedDate);

//        //return date to NewDebtActivity
//        ReturnValueFragment activity = (ReturnValueFragment) getActivity();
//        activity.onReturnValueDate(c);

    }
}
