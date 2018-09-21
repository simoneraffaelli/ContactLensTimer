package com.raffynato.contactlenstimer;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class BarChart extends View {

    private int maxValue = 100;
    private float currentValue = 0;
    private ValueAnimator barAnim = null, barColorAnim = null;
    private long animationDuration = 3000L;  //Default time to complete the entire bar

    private float   mBarHeight,
            mBarWidth,
            mTextMargin;

    private int mContainerBarColor,
            mProgressBarColor,
            mTextColor;

    private boolean mShowText = true;

    private Paint   mProgressBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG),
            mContainerBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG),
            mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public BarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressBar, 0, 0);
        try {
            mProgressBarColor = ta.getColor(R.styleable.HorizontalProgressBar_progressBarColor, getResources().getColor(R.color.materialBlue));
            mBarHeight = ta.getDimension(R.styleable.HorizontalProgressBar_BarHeight, 0.0F);
            mBarWidth = ta.getDimension(R.styleable.HorizontalProgressBar_BarWidth, 0.0F);
            mContainerBarColor = ta.getColor(R.styleable.HorizontalProgressBar_containerBarColor, getResources().getColor(R.color.boringGrey));
            mTextColor = ta.getColor(R.styleable.HorizontalProgressBar_textColor, Color.BLACK);
            mShowText = ta.getBoolean(R.styleable.HorizontalProgressBar_showText, mShowText);
            mTextMargin = ta.getDimension(R.styleable.HorizontalProgressBar_textToBarMargin, 10F);
        } finally {
            ta.recycle();
        }

        mProgressBarPaint.setColor(mProgressBarColor);
        mContainerBarPaint.setColor(mContainerBarColor);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);

        int width, height;
        switch(widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                width = 1000;
                break;
            case MeasureSpec.UNSPECIFIED:
                width = 1000;
                break;
            default:
                width = 1000;
                break;
        }

        switch(heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = 30;
                break;
            case MeasureSpec.UNSPECIFIED:
                height = 30;
                break;
            default:
                height = 30;
                break;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw (Canvas canvas) {
        float containerBarHeight = mBarHeight == 0 ? getHeight() : mBarHeight;
        float progressBarHeight = containerBarHeight - getPaddingBottom();

        Rect txtBounds = new Rect();
        String maxValueString = String.valueOf(Math.round(maxValue));
        mTextPaint.setTextSize(progressBarHeight);
        mTextPaint.getTextBounds(maxValueString, 0, maxValueString.length(), txtBounds);
        float txtWidth = mShowText ? txtBounds.width() + mTextMargin : 0;

        float containerBarWidth = mBarWidth == 0 ? getWidth() - txtWidth : mBarWidth;
        float progressBarWidth = containerBarWidth * (currentValue/maxValue);

        /* Draw container bar */
        canvas.drawRoundRect(0f, 0f, containerBarWidth, containerBarHeight, containerBarHeight/2, containerBarHeight/2, mContainerBarPaint);

        /* Draw progress bar */
        canvas.drawRoundRect(getPaddingStart(), getPaddingTop(), progressBarWidth,progressBarHeight, progressBarHeight/2, progressBarHeight/2, mProgressBarPaint);

        /* Draw percentage text */
        if (mShowText) {
            canvas.drawText(String.valueOf(Math.round(currentValue)), getWidth() - txtWidth, (containerBarHeight / 2) + (txtBounds.height() / 2), mTextPaint);
        }
    }

    public void setValue(int newValue) {
        float previousValue = currentValue;
        if (newValue < 0) {
            currentValue = 0;
        } else if (newValue > maxValue) {
            currentValue = maxValue;
        } else {
            currentValue = newValue;
        }

        if (barAnim != null) {
            barAnim.cancel();
        }

        if (barColorAnim != null) {
            barColorAnim.cancel();
        }
        ;

        barAnim = ValueAnimator.ofFloat(previousValue, currentValue);
        //animationDuration specifies how long it should take to animate the entire graph, so the
        //actual value to use depends on how much the value needs to change
        int changeInValue = (int) Math.abs(currentValue - previousValue);
        long durationToUse = (long) (animationDuration * ((float) changeInValue / (float) maxValue)* .9);
        barAnim.setDuration(durationToUse);
        barAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                currentValue  = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        barAnim.start();
        invalidate();
    }

    public void setMaxValue(int maxVal) {
        this.maxValue = maxVal;
    }

}
