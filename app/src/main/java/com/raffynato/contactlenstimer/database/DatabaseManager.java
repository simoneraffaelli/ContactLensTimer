package com.raffynato.contactlenstimer.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.raffynato.contactlenstimer.Lens;
import com.raffynato.contactlenstimer.LensesInUse;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

import static com.raffynato.contactlenstimer.LensesInUse.COLUMN_DURATION_LX;
import static com.raffynato.contactlenstimer.LensesInUse.COLUMN_DURATION_RX;
import static com.raffynato.contactlenstimer.LensesInUse.COLUMN_INIT_DATE_LX;
import static com.raffynato.contactlenstimer.LensesInUse.COLUMN_INIT_DATE_RX;

public class DatabaseManager {

    private DatabaseHelper dbHelper;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<Lens> getLenses() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Lens> l = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query =  "SELECT * FROM " + LensesInUse.TABLE_NAME +
                            " ORDER BY " + LensesInUse._ID + " DESC LIMIT 1;";

            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Lens rxLens = new Lens( Lens.Duration.fromInt(cursor.getInt(cursor.getColumnIndex(COLUMN_DURATION_RX))),
                                        DateTime.parse(cursor.getString(cursor.getColumnIndex(COLUMN_INIT_DATE_RX)), DateTimeFormat.forPattern("dd/MM/yyyy")));
                Lens lxLens = new Lens( Lens.Duration.fromInt(cursor.getInt(cursor.getColumnIndex(COLUMN_DURATION_LX))),
                                        DateTime.parse(cursor.getString(cursor.getColumnIndex(COLUMN_INIT_DATE_LX)), DateTimeFormat.forPattern("dd/MM/yyyy")));

                l.add(lxLens);
                l.add(rxLens);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return l;
    }

    public ArrayList<Lens> getHistory() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Lens> l = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query =  "SELECT * FROM " + LensesInUse.TABLE_NAME +
                    " ORDER BY " + LensesInUse._ID + " DESC;";

            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Lens rxLens = new Lens( Lens.Duration.fromInt(cursor.getInt(cursor.getColumnIndex(COLUMN_DURATION_RX))),
                        DateTime.parse(cursor.getString(cursor.getColumnIndex(COLUMN_INIT_DATE_RX)), DateTimeFormat.forPattern("dd/MM/yyyy")));
                Lens lxLens = new Lens( Lens.Duration.fromInt(cursor.getInt(cursor.getColumnIndex(COLUMN_DURATION_LX))),
                        DateTime.parse(cursor.getString(cursor.getColumnIndex(COLUMN_INIT_DATE_LX)), DateTimeFormat.forPattern("dd/MM/yyyy")));

                l.add(lxLens);
                l.add(rxLens);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return l;
    }

    public boolean addLenses(LensesInUse lensCouple) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long row = db.insert(LensesInUse.TABLE_NAME, null, lensCouple.getContentValues());
        return row > 0;
    }
}
