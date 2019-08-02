package com.raffinato.contactlensreminder;

import android.content.ContentValues;
import android.provider.BaseColumns;

import org.joda.time.format.DateTimeFormat;

import java.io.Serializable;

public class LensesInUse implements Serializable, BaseColumns {

    public static final String TABLE_NAME = "lenses";
    public static final String COLUMN_DURATION_LX = "durationlx";
    public static final String COLUMN_INIT_DATE_LX = "initdatelx";
    public static final String COLUMN_DURATION_RX = "durationrx";
    public static final String COLUMN_INIT_DATE_RX = "initdaterx";
    public static final String COLUMN_STATE_LX = "isactivelx";
    public static final String COLUMN_STATE_RX = "isactiverx";

    private final Lens rxLens;
    private final Lens lxLens;

    public LensesInUse(Lens lx, Lens rx) {
        this.rxLens = rx;
        this.lxLens = lx;
    }

    public Lens getRxLens() {
        return rxLens;
    }

    public Lens getLxLens() {
        return lxLens;
    }

    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DURATION_LX, lxLens.getDuration().getTime());
        cv.put(COLUMN_INIT_DATE_LX, lxLens.getInitialDate().toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
        cv.put(COLUMN_STATE_LX, lxLens.isActive());
        cv.put(COLUMN_DURATION_RX, rxLens.getDuration().getTime());
        cv.put(COLUMN_INIT_DATE_RX, rxLens.getInitialDate().toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
        cv.put(COLUMN_STATE_RX, rxLens.isActive());
        return cv;
    }
}
