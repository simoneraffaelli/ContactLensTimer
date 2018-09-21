package com.raffynato.contactlenstimer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.raffynato.contactlenstimer.LensesInUse;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "lenstimer.db";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }


    public static final String CREATE_TABLE_LENSES = "CREATE TABLE " +
            LensesInUse.TABLE_NAME + " (" +
            LensesInUse._ID + " INTEGER PRIMARY KEY, " +
            LensesInUse.COLUMN_INIT_DATE_LX + " TEXT, " +
            LensesInUse.COLUMN_DURATION_LX + " INTEGER," +
            LensesInUse.COLUMN_INIT_DATE_RX + " TEXT, " +
            LensesInUse.COLUMN_DURATION_RX + " INTEGER)";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LENSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
