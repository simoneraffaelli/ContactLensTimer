package com.raffynato.contactlenstimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.raffynato.contactlenstimer.database.DatabaseHelper;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import static com.raffynato.contactlenstimer.LensesInUse.COLUMN_DURATION_LX;
import static com.raffynato.contactlenstimer.LensesInUse.COLUMN_DURATION_RX;
import static com.raffynato.contactlenstimer.LensesInUse.COLUMN_INIT_DATE_LX;
import static com.raffynato.contactlenstimer.LensesInUse.COLUMN_INIT_DATE_RX;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String DB_PATH = "/data/data/com.raffynato.contactlenstimer/databases/" + DatabaseHelper.DB_NAME;
    String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper nh = new NotificationHelper(context);

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                Cursor c = null;
                SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
                try {
                    String query =  "SELECT * FROM " + LensesInUse.TABLE_NAME +
                            " ORDER BY " + LensesInUse._ID + " DESC LIMIT 1;";

                    c = db.rawQuery(query, null);
                    while (c.moveToNext()) {
                        Lens rxLens = new Lens( Lens.Duration.fromInt(c.getInt(c.getColumnIndex(COLUMN_DURATION_RX))),
                                DateTime.parse(c.getString(c.getColumnIndex(COLUMN_INIT_DATE_RX)), DateTimeFormat.forPattern("dd-MM-yyyy")));
                        Lens lxLens = new Lens( Lens.Duration.fromInt(c.getInt(c.getColumnIndex(COLUMN_DURATION_LX))),
                                DateTime.parse(c.getString(c.getColumnIndex(COLUMN_INIT_DATE_LX)), DateTimeFormat.forPattern("dd-MM-yyyy")));
                        if(rxLens.getExpDate().isEqual(lxLens.getExpDate())) {
                            NotificationScheduler.setReminder(context, AlarmReceiver.class, lxLens.getExpDate().getDayOfYear(), new DateTime().withMinuteOfHour(0).getMinuteOfHour(), new DateTime().withHourOfDay(20).getHourOfDay(), 0);
                        } else {
                            NotificationScheduler.setReminder(context, AlarmReceiver.class, lxLens.getExpDate().getDayOfYear(), new DateTime().withMinuteOfHour(0).getMinuteOfHour(), new DateTime().withHourOfDay(20).getHourOfDay(), 0);
                            NotificationScheduler.setReminder(context, AlarmReceiver.class, rxLens.getExpDate().getDayOfYear(), new DateTime().withMinuteOfHour(0).getMinuteOfHour(), new DateTime().withHourOfDay(20).getHourOfDay(), 0);
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
                Log.d(TAG, "onReceive: VIEW");

                NotificationScheduler.cancelReminders(context, AlarmReceiver.class);
                nh.cancelNotifications();

                return;
            }
        }

        Log.d(TAG, "onReceive: ");
        int id = intent.getIntExtra("notification-id", 0);

        //Trigger the notification

        nh.createNotification("Lenses have expired!", "Open app to check it out.", id);

    }
}
