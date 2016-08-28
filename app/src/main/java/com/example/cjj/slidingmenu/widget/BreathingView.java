package com.example.cjj.slidingmenu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;

import com.example.cjj.slidingmenu.R;

public class BreathingView extends View {
    private static final String TAG = "BreathingView";
    private int innerRadius;
    private int gapRadius;
    private int outerRadius;
    private int color;
    private Paint paint;
    private float progress = 0.0f;

    private float curInnerR;
    private float curOuterR;
    private AccelerateDecelerator intepretor;

    public BreathingView(Context context) {
        super(context);
    }

    public BreathingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.BreathingView);
        innerRadius = (int) array.getDimension(R.styleable.BreathingView_innerRadius, 10);
        outerRadius = (int) array.getDimension(R.styleable.BreathingView_outerRadius,20);
        gapRadius = (int) array.getDimension(R.styleable.BreathingView_gapRadius,15);
        color = array.getColor(R.styleable.BreathingView_innerColor, Color.CYAN);
        init();
    }

    public BreathingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化状态
     */
    private void init() {
        curOuterR = outerRadius;
        curInnerR = gapRadius;
        intepretor = new AccelerateDecelerator();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int height =0;
//        int width = 0;
//
//        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
//        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.i(TAG, "onMeasure: "+outerRadius);
        int size = (int) (outerRadius*2+(outerRadius -gapRadius)*2+gapRadius*0.1f);
        setMeasuredDimension(size,size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = getCanvasPaint();
        paint.setColor(Color.GRAY);
        caculateRadius();
        canvas.drawCircle(getWidth()/2,getHeight()/2,curOuterR,paint);
        paint.setColor(color);
        canvas.drawCircle(getWidth()/2,getHeight()/2,curInnerR,paint);
        postInvalidateDelayed(100);
    }

    private void caculateRadius() {
        progress+=0.05f;
        float p = intepretor.getInterpolation(progress);
        curInnerR = innerRadius+p*gapRadius/2;
        curOuterR = outerRadius-p*gapRadius/2;
    }


    public Paint getCanvasPaint()
    {
        if (paint == null) {
            paint = new Paint();
            paint.setColor(color);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
        }
        return paint;
    }

    private static class AccelerateDecelerator implements Interpolator{

        @Override
        public float getInterpolation(float input) {
            return (float)(Math.cos((input + 1) * Math.PI) / 2.0f) + 0.5f;
        }
    }
}
