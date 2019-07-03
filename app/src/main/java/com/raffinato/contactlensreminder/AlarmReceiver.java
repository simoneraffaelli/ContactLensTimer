package com.raffinato.contactlensreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.raffinato.contactlensreminder.database.DatabaseHelper;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper nh = new NotificationHelper(context);
        final String DB_PATH = context.getDatabasePath(DatabaseHelper.DB_NAME).getPath();


        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Cursor c = null;
                SQLiteDatabase db = null;
                try {
                    db = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
                    String query = "SELECT * FROM " + LensesInUse.TABLE_NAME +
                            " ORDER BY " + LensesInUse._ID + " DESC LIMIT 1;";

                    c = db.rawQuery(query, null);
                    while (c.moveToNext()) {
                        Lens rxLens = new Lens(c.getInt(c.getColumnIndex(LensesInUse.COLUMN_STATE_RX)) == 1, Lens.Duration.fromInt(c.getInt(c.getColumnIndex(LensesInUse.COLUMN_DURATION_RX))),
                                DateTime.parse(c.getString(c.getColumnIndex(LensesInUse.COLUMN_INIT_DATE_RX)), DateTimeFormat.forPattern("dd/MM/yyyy")));
                        Lens lxLens = new Lens(c.getInt(c.getColumnIndex(LensesInUse.COLUMN_STATE_LX)) == 1, Lens.Duration.fromInt(c.getInt(c.getColumnIndex(LensesInUse.COLUMN_DURATION_LX))),
                                DateTime.parse(c.getString(c.getColumnIndex(LensesInUse.COLUMN_INIT_DATE_LX)), DateTimeFormat.forPattern("dd/MM/yyyy")));
                        if(rxLens.isActive() || lxLens.isActive()) {
                            if (rxLens.getExpDate().isEqual(lxLens.getExpDate())) {
                                if (lxLens.getRemainingTime().getDays() > 0) {
                                    NotificationScheduler.setReminder(context, AlarmReceiver.class, lxLens.getExpDate().getDayOfYear(), new DateTime().withMinuteOfHour(0).getMinuteOfHour(), new DateTime().withHourOfDay(20).getHourOfDay(), 0);
                                }
                            } else {
                                if(lxLens.isActive() && lxLens.getRemainingTime().getDays() > 0)
                                    NotificationScheduler.setReminder(context, AlarmReceiver.class, lxLens.getExpDate().getDayOfYear(), new DateTime().withMinuteOfHour(0).getMinuteOfHour(), new DateTime().withHourOfDay(20).getHourOfDay(), NotificationHelper.LX_LENS_NOT_ID);
                                if(rxLens.isActive() && rxLens.getRemainingTime().getDays() > 0)
                                    NotificationScheduler.setReminder(context, AlarmReceiver.class, rxLens.getExpDate().getDayOfYear(), new DateTime().withMinuteOfHour(0).getMinuteOfHour(), new DateTime().withHourOfDay(20).getHourOfDay(), NotificationHelper.RX_LENS_NOT_ID);
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (c != null) {
                        c.close();
                    }
                    if (db != null) {
                        db.close();
                    }
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
