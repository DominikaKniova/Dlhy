package sk.dominika.dluhy.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import sk.dominika.dluhy.R;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    /**
     * Create a new instance of TimePickerDialog and return it.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    /**
     * Set time TextView in NewDebtActivity after the time was chosen.
     */
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String formattedTime = sdf.format(c.getTime());

        TextInputEditText inputTime = (TextInputEditText)getActivity().findViewById(R.id.inputTime);
        inputTime.setText(formattedTime);
    }
}

