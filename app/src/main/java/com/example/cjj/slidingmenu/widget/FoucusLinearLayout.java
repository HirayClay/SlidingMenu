package com.example.cjj.slidingmenu.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowId;
import android.widget.LinearLayout;

/**
 * Created by CJJ on 2016/8/25.
 *
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class FoucusLinearLayout extends LinearLayout{
    public FoucusLinearLayout(Context context) {
        super(context);
    }

    public FoucusLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FoucusLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
