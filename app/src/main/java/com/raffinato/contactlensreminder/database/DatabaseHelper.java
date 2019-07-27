package com.raffinato.contactlensreminder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.raffinato.contactlensreminder.LensesCase;
import com.raffinato.contactlensreminder.LensesInUse;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "lenstimerv2.db";
    private static final String CREATE_TABLE_LENSES = "CREATE TABLE " +
            LensesInUse.TABLE_NAME + " (" +
            LensesInUse._ID + " INTEGER PRIMARY KEY, " +
            LensesInUse.COLUMN_INIT_DATE_LX + " TEXT, " +
            LensesInUse.COLUMN_DURATION_LX + " INTEGER," +
            LensesInUse.COLUMN_STATE_LX + " INTEGER, " +
            LensesInUse.COLUMN_INIT_DATE_RX +" TEXT, " +
            LensesInUse.COLUMN_DURATION_RX + " INTEGER,"+
            LensesInUse.COLUMN_STATE_RX + " INTEGER)";
    private static final String CREATE_TABLE_CASE = "CREATE TABLE " +
            LensesCase.TABLE_NAME + " (" +
            LensesCase._ID + " INTEGER PRIMARY KEY, " +
            LensesCase.COLUMN_LENSES_REMAINING + " INTEGER, " +
            LensesCase.COLUMN_LENSES_TYPE + " INTEGER," +
            LensesCase.COLUMN_STATE + " INTEGER)";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LENSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
