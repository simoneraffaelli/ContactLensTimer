package com.raffynato.contactlenstimer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static com.raffynato.contactlenstimer.NotificationHelper.LX_LENS_NOT_ID;
import static com.raffynato.contactlenstimer.NotificationHelper.RX_LENS_NOT_ID;

public class NotificationScheduler
{
    public static void setReminder(Context context, Class<?> cls, int doy, int min, int hour, int notificationId)
    {
        Calendar calendar = Calendar.getInstance();

        Calendar setcalendar = Calendar.getInstance();

        setcalendar.set(Calendar.DAY_OF_YEAR, doy);
        setcalendar.set(Calendar.HOUR_OF_DAY, hour);
        setcalendar.set(Calendar.MINUTE, min);
        setcalendar.set(Calendar.SECOND, 0);
        // cancel already scheduled reminders
        cancelReminder(context,cls, notificationId);

        if(setcalendar.before(calendar))
            setcalendar.add(Calendar.DATE,1);

        // Enable a receiver
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);


        Intent intent1 = new Intent(context, cls);
        intent1.putExtra("notification-id", notificationId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    public static void cancelReminder(Context context,Class<?> cls, int notificationId)
    {
        // Disable a receiver
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public static void cancelReminders(Context context, Class<?> cls) {
        NotificationScheduler.cancelReminder(context, AlarmReceiver.class, 0);
        NotificationScheduler.cancelReminder(context, AlarmReceiver.class, LX_LENS_NOT_ID);
        NotificationScheduler.cancelReminder(context, AlarmReceiver.class, RX_LENS_NOT_ID);
    }

}
