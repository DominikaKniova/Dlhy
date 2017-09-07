package sk.dominika.dluhy.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.widget.DatePicker;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import sk.dominika.dluhy.R;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    /**
     * Create a new instance of DatePickerDialog and return it.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    /**
     * Set date TextView in NewDebtActivity after the date was chosen.
     */
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = sdf.format(c.getTime());

        TextInputEditText inputDate = (TextInputEditText) getActivity().findViewById(R.id.inputDate);
        inputDate.setText(formattedDate);
    }
}
