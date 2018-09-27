package com.raffinato.contactlensreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.raffinato.contactlensreminder.database.DatabaseHelper;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String DB_PATH = "/data/data/com.raffinato.contactlenstimer/databases/" + DatabaseHelper.DB_NAME;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper nh = new NotificationHelper(context);

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Cursor c = null;
                SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
                try {
                    String query = "SELECT * FROM " + LensesInUse.TABLE_NAME +
                            " ORDER BY " + LensesInUse._ID + " DESC LIMIT 1;";

                    c = db.rawQuery(query, null);
                    while (c.moveToNext()) {
                        Lens rxLens = new Lens(Lens.Duration.fromInt(c.getInt(c.getColumnIndex(LensesInUse.COLUMN_DURATION_RX))),
                                DateTime.parse(c.getString(c.getColumnIndex(LensesInUse.COLUMN_INIT_DATE_RX)), DateTimeFormat.forPattern("dd-MM-yyyy")));
                        Lens lxLens = new Lens(Lens.Duration.fromInt(c.getInt(c.getColumnIndex(LensesInUse.COLUMN_DURATION_LX))),
                                DateTime.parse(c.getString(c.getColumnIndex(LensesInUse.COLUMN_INIT_DATE_LX)), DateTimeFormat.forPattern("dd-MM-yyyy")));
                        if (rxLens.getExpDate().isEqual(lxLens.getExpDate())) {
                            NotificationScheduler.setReminder(context, AlarmReceiver.class, lxLens.getExpDate().getDayOfYear(), new DateTime().withMinuteOfHour(0).getMinuteOfHour(), new DateTime().withHourOfDay(20).getHourOfDay(), 0);
                        } else {
                            NotificationScheduler.setReminder(context, AlarmReceiver.class, lxLens.getExpDate().getDayOfYear(), new DateTime().withMinuteOfHour(0).getMinuteOfHour(), new DateTime().withHourOfDay(20).getHourOfDay(), NotificationHelper.LX_LENS_NOT_ID);
                            NotificationScheduler.setReminder(context, AlarmReceiver.class, rxLens.getExpDate().getDayOfYear(), new DateTime().withMinuteOfHour(0).getMinuteOfHour(), new DateTime().withHourOfDay(20).getHourOfDay(), NotificationHelper.RX_LENS_NOT_ID);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (c != null) {
                        c.close();
                    }
                    db.close();
                }
                return;
            }
            if (intent.getAction().equalsIgnoreCase(NotificationHelper.ACTION_SILENCE)) {
                int notId = intent.getExtras().getInt(NotificationHelper.SILENCE_EXTRA, 0);
                Bundle b = intent.getExtras();
                NotificationScheduler.cancelReminder(context, AlarmReceiver.class, notId);
                nh.cancelNotifications();

                return;
            }
        }

        int id = intent.getIntExtra("notification-id", 0);
        //Trigger the notification
        nh.createNotification(context.getResources().getString(R.string.notification_title), context.getResources().getString(R.string.notification_body), id);
    }
}
