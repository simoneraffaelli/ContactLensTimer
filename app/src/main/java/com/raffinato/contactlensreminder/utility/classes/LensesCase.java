package com.raffinato.contactlensreminder.utility.classes;

import android.content.ContentValues;
import android.provider.BaseColumns;

import java.io.Serializable;

public class LensesCase implements Serializable, BaseColumns {

    public static final String TABLE_NAME = "lensescase";
    public static final String COLUMN_LENSES_REMAINING = "lenses_remaining";
    public static final String COLUMN_LENSES_TYPE = "lenses_type";
    public static final String COLUMN_STATE = "isactive";

    private int lensesLeft;
    final private Lens.Duration lensesType;

    private boolean state;


    LensesCase(int lensesLeft, Lens.Duration lensesType) {
        this.lensesLeft = lensesLeft;
        this.lensesType = lensesType;
        this.state = true;
    }

    public int getLensesLeft() {
        return lensesLeft;
    }

    public void setLensesLeft(int lensesLeft) {
        this.lensesLeft = lensesLeft;
    }

    private boolean isActive() {
        return state;
    }

    public void setActive(boolean state) {
        this.state = state;
    }


    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LENSES_REMAINING, this.lensesLeft);
        cv.put(COLUMN_LENSES_TYPE, this.lensesType.getTime());
        cv.put(COLUMN_STATE, isActive());

        return cv;
    }

}
