package com.raffinato.contactlensreminder.utility.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.raffinato.contactlensreminder.MainActivity;
import com.raffinato.contactlensreminder.R;

import java.util.Objects;

public class NotificationHelper {
    private static final String NOTIFICATION_CHANNEL_ID = "10001";
    public static final String ACTION_SILENCE = "com.android.example.SILENCE_NOTIFICATION";
    public static final int RX_LENS_NOT_ID = 321547922;
    public static final int LX_LENS_NOT_ID = 321123242;
    public static final String SILENCE_EXTRA = "silence-extra";
    private final Context mContext;
    private NotificationManager mNotificationManager;

    public NotificationHelper(Context context) {
        mContext = context;
    }

    /**
     * Create and push the notification
     */
    public void createNotification(String title, String message, int notificationId) {
        /*Creates an explicit intent for an Activity in your app*/
        Intent resultIntent = new Intent(mContext, MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                notificationId, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent snoozeIntent = new Intent(mContext, AlarmReceiver.class);
        snoozeIntent.putExtra(SILENCE_EXTRA, notificationId);
        snoozeIntent.setAction(ACTION_SILENCE);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(mContext, 0, snoozeIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        String GROUP_KEY_LENS_TIMER = "com.android.example.LENS_TIMER";
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_contact_lens)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent)
                .setColor(ContextCompat.getColor(mContext, R.color.colorAccent))
                .addAction(0, mContext.getResources().getString(R.string.not_action_label), snoozePendingIntent)
                .setGroup(GROUP_KEY_LENS_TIMER);

        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
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
        Objects.requireNonNull(mNotificationManager).cancelAll();
    }
}
