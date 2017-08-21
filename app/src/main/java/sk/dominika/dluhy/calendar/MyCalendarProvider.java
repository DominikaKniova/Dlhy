package sk.dominika.dluhy.calendar;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.TimeZone;

import sk.dominika.dluhy.activities.MainActivity;

public class MyCalendarProvider extends AppCompatActivity {

    public static final int CALENDARHELPER_PERMISSION_REQUEST_CODE = 99;
    private static final String DEBUG_TAG = "CalendarActivity";

    public void createEvent(String note, String sum, String name) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, name)
                .putExtra(CalendarContract.Events.DESCRIPTION, sum + ", " + note);

        startActivity(intent);
    }
    public static long addReminder(Activity activity, String name, String note, String sum, int year, int month, int day, int hour, int minute) {
        int mMonth = month +1;
        long eventID = -1;

        try {
            Calendar time = Calendar.getInstance();
//            time.set(year, mMonth, day, hour, minute);
            time.set(2017, 8, 20, 7, 30);
            Calendar endTime = Calendar.getInstance();
            endTime.set(2017, 8, 20, 8, 45);

            ContentResolver cr = activity.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, time.getTimeInMillis());
            values.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
            values.put(CalendarContract.Events.TITLE, name);
            values.put(CalendarContract.Events.DESCRIPTION, sum + ", "+note);
            //
            values.put(CalendarContract.Events.CALENDAR_ID, 0);

            //Get current timezone
            values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
            Log.i(DEBUG_TAG, "Timezone retrieved=>"+ TimeZone.getDefault().getID());
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            Log.i(DEBUG_TAG, "Uri returned=>"+uri.toString());

            // get the event ID that is the last element in the Uri
            eventID = Long.parseLong(uri.getLastPathSegment());

            ContentValues reminder = new ContentValues();
            reminder.put(CalendarContract.Reminders.MINUTES, 0);
            reminder.put(CalendarContract.Reminders.EVENT_ID, eventID);
            reminder.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            Uri uri2 = cr.insert(CalendarContract.Reminders.CONTENT_URI, reminder);


        } catch (SecurityException e) {
            requestCalendarReadWritePermission(activity);

            Toast.makeText(activity, "You are not allowed to add reminder", Toast.LENGTH_SHORT).show();
        }

        return eventID;
    }

    public static void requestCalendarReadWritePermission(Activity caller)
    {
        List<String> permissionList = new ArrayList<String>();

        if  (ContextCompat.checkSelfPermission(caller, Manifest.permission.WRITE_CALENDAR)!=PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.WRITE_CALENDAR);

        }

        if  (ContextCompat.checkSelfPermission(caller,Manifest.permission.READ_CALENDAR)!=PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.READ_CALENDAR);

        }

        if (permissionList.size()>0)
        {
            String [] permissionArray = new String[permissionList.size()];

            for (int i=0;i<permissionList.size();i++)
            {
                permissionArray[i] = permissionList.get(i);
            }

            ActivityCompat.requestPermissions(caller,
                    permissionArray,
                    CALENDARHELPER_PERMISSION_REQUEST_CODE);
        }

    }

    public static Hashtable listCalendarId(Context c) {

        if (haveCalendarReadWritePermissions((Activity)c)) {

            String projection[] = {"_id", "calendar_displayName"};
            Uri calendars;
            calendars = Uri.parse("content://com.android.calendar/calendars");

            ContentResolver contentResolver = c.getContentResolver();
            Cursor managedCursor = contentResolver.query(calendars, projection, null, null, null);

            if (managedCursor.moveToFirst())
            {
                String calName;
                String calID;
                int cont = 0;
                int nameCol = managedCursor.getColumnIndex(projection[1]);
                int idCol = managedCursor.getColumnIndex(projection[0]);
                Hashtable<String,String> calendarIdTable = new Hashtable<>();

                do
                {
                    calName = managedCursor.getString(nameCol);
                    calID = managedCursor.getString(idCol);
                    Log.v(DEBUG_TAG, "CalendarName:" + calName + " ,id:" + calID);
                    calendarIdTable.put(calName,calID);
                    cont++;
                } while (managedCursor.moveToNext());
                managedCursor.close();

                return calendarIdTable;
            }

        }

        return null;

    }

    public static boolean haveCalendarReadWritePermissions(Activity caller)
    {
        int permissionCheck = ContextCompat.checkSelfPermission(caller,
                Manifest.permission.READ_CALENDAR);

        if (permissionCheck== PackageManager.PERMISSION_GRANTED)
        {
            permissionCheck = ContextCompat.checkSelfPermission(caller,
                    Manifest.permission.WRITE_CALENDAR);

            if (permissionCheck== PackageManager.PERMISSION_GRANTED)
            {
                return true;
            }
        }

        return false;
    }

}
