package com.example.cjj.slidingmenu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.cjj.slidingmenu.R;

/**
 * Created by CJJ on 2016/8/25.
 */
public class MarkerLayout extends LinearLayout {

    private LayoutInflater inflater;

    public MarkerLayout(Context context) {
        super(context);
    }

    public MarkerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarkerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(inflater == null)
            inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.marker_template,this,true);
    }
}
