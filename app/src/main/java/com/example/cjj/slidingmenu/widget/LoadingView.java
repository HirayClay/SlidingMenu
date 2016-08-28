package com.example.cjj.slidingmenu.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.example.cjj.slidingmenu.R;

/**
 * Created by CJJ on 2016/8/25.
 */
public class LoadingView extends TextView {
    private static final String TAG = "LoadingView";
    private final int DEFAULT_BALLON_NUM = 3;
    private float textSize;
    private Bitmap arrow;
    private float defaultPadding;
    private float ballonPadding;
    private int ballonNum;
    private int color;
    private int MAX_PROGRESS = 100;
    int progress = 0;
    private int minRadius;
    private int maxRadius;
    private int stepSize;
    private float pivotOneX;
    private float pivotSecX;
    private float pivotThirdX;
    private float pivotY;
    private Paint paint;
    private Paint linePaint;


    public static final int MODE_LOADING = 0xfff01;
    public static final int MODE_TEXT = 0xfff02;

    public int mode;
    private Paint textPaint;
    private String text;
    private Paint fillPaint;
    private int width;
    private int height;

    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LoadingStyle);
        ballonNum = array.getInt(R.styleable.LoadingStyle_ballon, DEFAULT_BALLON_NUM);
        ballonPadding = array.getDimension(R.styleable.LoadingStyle_ballonPadding, getDefaultPadding());
        color = array.getColor(R.styleable.LoadingStyle_color, Color.GRAY);
        minRadius = (int) array.getDimension(R.styleable.LoadingStyle_minRadius, 100);
        maxRadius = (int) array.getDimension(R.styleable.LoadingStyle_maxRadius, 300);
        textSize = array.getDimension(R.styleable.LoadingStyle_textSize, 0);
        text = array.getString(R.styleable.LoadingStyle_text);
        stepSize = (maxRadius - minRadius) / 100;
        init();
        Log.i(TAG, "Category: maxRadius:" + maxRadius + "\n" +
                "ballonPadding:" + ballonPadding + "\n " +
                "minRadius:" + minRadius + "\n " +
                "ballonNum:" + ballonNum + "\n" +
                "textSize:" + textSize);
    }

    public void init() {
        arrow = BitmapFactory.decodeResource(getResources(), R.mipmap.place);
        mode = MODE_LOADING;
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        Log.i(TAG, "onMeasure: sizeWidth:sizeHeight  "+sizeWidth+":"+sizeHeight);
        int computedExtWidth = (int) (getDefaultPadding() * 2 + (maxRadius + ballonPadding) * 2 * ballonNum);
        int computeHeight = (int) (getDefaultPadding()*2+(maxRadius + ballonPadding) * 2);
        if (mode == MODE_TEXT) {
            width = (int) (getTextPaint().measureText(text) + getPaddingRight() + getPaddingLeft());
        }
        Log.i(TAG, "onMeasure: " + width + "---" + height);
        if (mode == MODE_LOADING)
        {
            setMeasuredDimension(Math.max(computedExtWidth,width),computeHeight);
        }else {

            setMeasuredDimension(Math.max(computedExtWidth,width),computeHeight);
        }
        computePosForBallon();
        //在measure阶段确定最新的宽度
    }

    private void computePosForBallon() {
        pivotY = (float) ((getMeasuredHeight() - arrow.getHeight()) / 2 + 0.0);
        pivotOneX = (getPaddingLeft() + ballonPadding + maxRadius) + ballonPadding * 2;
        pivotSecX = (pivotOneX + (maxRadius + ballonPadding) * 2);
        pivotThirdX = (pivotSecX + (maxRadius + ballonPadding) * 2);
    }

    /**
     * 初始气泡为小 中 大
     **/
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float radiusOne = (float) ((0.85 * maxRadius * progress / MAX_PROGRESS + minRadius) % maxRadius);
        float radiusSec = (float) ((1.08 * maxRadius * progress / MAX_PROGRESS + minRadius) % maxRadius);
        float radiusThird = (float) (1.2 * maxRadius * progress / MAX_PROGRESS + minRadius) % maxRadius;
        canvas.drawRoundRect(1, 1, getMeasuredWidth()-2, getMeasuredHeight() - arrow.getHeight()-2, 5, 5, getFillPaint());
        canvas.drawRoundRect(0, 0, getMeasuredWidth()-2, getMeasuredHeight() - arrow.getHeight()-2, 5, 5, getLinePaint());
        canvas.drawBitmap(arrow, (getMeasuredWidth() - arrow.getWidth()) / 2, getMeasuredHeight() - arrow.getHeight()-4, null);
        if (mode == MODE_LOADING) {
//            Log.i(TAG, "onDraw: circle:[" + pivotOneX + "," + pivotY + "], [" + pivotSecX + "," + pivotY + "] ,[" + pivotThirdX + "," + pivotY + "]");
            canvas.drawCircle(pivotOneX, pivotY, radiusOne, getSelfPaint());
            canvas.drawCircle(pivotSecX, pivotY, radiusSec, getSelfPaint());
            canvas.drawCircle(pivotThirdX, pivotY, radiusThird, getSelfPaint());
            progress += MAX_PROGRESS * 0.1;
            postInvalidateDelayed(190);
        } else {
            //文字
            Paint textPaint = getTextPaint();
            Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
            Rect bounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), bounds);
            int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            canvas.drawText(text, getMeasuredWidth() / 2 - bounds.width() / 2, baseline, textPaint);
        }
    }

    public float getDefaultPadding() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        defaultPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, displayMetrics);
        return defaultPadding;
    }

    public Paint getSelfPaint() {
        if (paint == null) {
            paint = new Paint();
            paint.setColor(color);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeWidth(3);
        }
        return paint;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        arrow.recycle();
    }

    public Paint getLinePaint() {
        if (linePaint == null) {
            linePaint = new Paint();
            linePaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
            linePaint.setAntiAlias(true);
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setColor(color);
        }
        return linePaint;
    }

    public Paint getTextPaint() {
        if (textPaint == null) {
            textPaint = new Paint();
            textPaint.setTextSize(textSize);
            textPaint.setColor(Color.BLACK);
            textPaint.setAntiAlias(true);
            textPaint.setStyle(Paint.Style.STROKE);
            textPaint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.NORMAL));
            textPaint.setTextAlign(Paint.Align.LEFT);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            int baseline = fontMetrics.top + (fontMetrics.bottom - fontMetrics.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        }
        return textPaint;
    }

    public void setText(String text) {
        this.mode = MODE_TEXT;
        this.text = text;
        Log.i(TAG, "setText: ");
        computeAndTriggerLayoutAnimation();
    }

    /**
     * 触发一个ScaleAnimation ，在动画结束时invalidate更新布局
     */
    private void computeAndTriggerLayoutAnimation() {
//        int oldW = getWidth();
        Paint paint = getTextPaint();
        float textLen = paint.measureText(text);
        int newW = (int) (textLen + getPaddingLeft() + getPaddingRight()+ballonPadding*2);
        this.width = newW;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                requestLayout();
                invalidate();
            }
        },1200);

      /*  ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.4f, 0.0f, 1.4f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(900);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(scaleAnimation);*/
    }

    public void setMinRadius(int minRadius) {
        this.minRadius = dp2px(minRadius);
    }

    public void setMaxRadius(int maxRadius) {
        this.maxRadius = dp2px(maxRadius);
    }

    public void setBallonPadding(float ballonPadding) {
        this.ballonPadding = dp2px((int) ballonPadding);
    }

    public void setBallonNum(int ballonNum) {
        this.ballonNum = ballonNum;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public Paint getFillPaint() {
        if (fillPaint == null) {
            fillPaint = new Paint();
            fillPaint.setColor(Color.WHITE);
            fillPaint.setStyle(Paint.Style.FILL);
            fillPaint.setAntiAlias(true);
        }
        return fillPaint;
    }
}
