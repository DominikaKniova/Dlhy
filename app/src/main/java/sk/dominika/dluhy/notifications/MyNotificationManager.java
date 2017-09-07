package sk.dominika.dluhy.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

import sk.dominika.dluhy.R;
import sk.dominika.dluhy.activities.MyProfileActivity;
import sk.dominika.dluhy.databases.MyFirebaseDatabaseHandler;

/**
 * Class for handling notifications.
 */
public class MyNotificationManager {
    /**
     * Create and schedule individual notification.
     * @param context Where the notification is created.
     * @param calendar Date and time of notification.
     * @param notificationId Id of notification.
     * @param title Debt information: name of who owes -> name of to who he owes.
     * @param content Debt information: sum and note of debt.
     */
    public static void scheduleNotification(Context context, Calendar calendar, int notificationId, String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_notification)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent intent = new Intent(context, MyProfileActivity.class);
        PendingIntent activity = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(activity);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(context, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Date date = calendar.getTime();
        long alertCalculatedMillis = date.getTime();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alertCalculatedMillis, pendingIntent);
    }

    /**
     * Provide information for all new notifications by reading all my debts with notifications from database
     * and then creates individual notifications by method scheduleNotification().
     * Method is called when user logs in or when synchronizing notifications.
     * @param context Where the notification is createdz
     */
    public static void createNotifications(Context context) {
        MyFirebaseDatabaseHandler.getAndCreateNotifications(context);
    }

    /**
     * Cancels all notifications created.
     * Method is called when user signs out or when synchronizing notifications.
     * @param context Where the notification is created.
     */
    public static void cancelAllNotifications(Context context) {
        NotificationManager notificationMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationMgr.cancelAll();
    }

    /**
     * Synchronize notifications.
     * Method will delete all notifications and then create them again from database.
     * Method is called when user adds or deletes a debt with a notification, or when user deletes
     * his friend.
     * @param context Where the notification is created.
     */
    public static void syncNotifications(Context context) {
        cancelAllNotifications(context);
        MyFirebaseDatabaseHandler.getAndCreateNotifications(context);

    }
}
