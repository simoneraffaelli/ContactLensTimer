package com.raffynato.contactlenstimer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class NotificationHelper {
    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    String GROUP_KEY_LENS_TIMER = "com.android.example.LENS_TIMER";
    public static final String ACTION_SILENCE = "com.android.example.SILENCE_NOTIFICATION";
    public static final int RX_LENS_NOT_ID = 321547922;
    public static final int LX_LENS_NOT_ID = 321123242;

    public NotificationHelper(Context context) {
        mContext = context;
    }

    /**
     * Create and push the notification
     */
    public void createNotification(String title, String message, int notificationId)
    {
        /*Creates an explicit intent for an Activity in your app*/
        Intent resultIntent = new Intent(mContext , MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                notificationId, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent snoozeIntent = new Intent(mContext, AlarmReceiver.class);
        snoozeIntent.setAction(ACTION_SILENCE);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(mContext, 0, snoozeIntent, 0);

        mBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_contact_lens)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent)
                .setColor(ContextCompat.getColor(mContext, R.color.colorAccent))
                .addAction(0 ,mContext.getResources().getString(R.string.not_action_label), snoozePendingIntent)
                .setGroup(GROUP_KEY_LENS_TIMER);

        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notifica", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(notificationId, mBuilder.build());
    }

    public void cancelNotifications() {
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
    }
}
