package com.raffynato.contactlenstimer;

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

    private Lens rxLens;

    public Lens getRxLens() {
        return rxLens;
    }

    public Lens getLxLens() {
        return lxLens;
    }

    private Lens lxLens;

    public LensesInUse(Lens lx, Lens rx) {
        this.rxLens = rx;
        this.lxLens = lx;
    }

    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DURATION_LX, lxLens.getDuration().getTime());
        cv.put(COLUMN_INIT_DATE_LX, lxLens.getInitialDate().toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
        cv.put(COLUMN_DURATION_RX, rxLens.getDuration().getTime());
        cv.put(COLUMN_INIT_DATE_RX, rxLens.getInitialDate().toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
        return cv;
    }
}
