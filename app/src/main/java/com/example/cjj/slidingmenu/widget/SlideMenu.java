package com.example.cjj.slidingmenu.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.cjj.slidingmenu.R;
import com.example.cjj.slidingmenu.fragment.FloatingMenuAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJJ on 2016/8/25.
 *
 */
public class SlideMenu extends LinearLayout implements View.OnClickListener, FloatingMenuAdapter.ItemClickListener, View.OnTouchListener {

    private static final String TAG = "SlideMenu";
    private RecyclerView recyclerView;
    private LayoutInflater inflater;
    private View handler;
    private boolean expand = false;
    private FloatingMenuAdapter adapter;
    private PercentRelativeLayout parent;
    private View ribbon;
    private PercentRelativeLayout.LayoutParams lp;
    private PercentRelativeLayout.LayoutParams ribbonLP;
    private boolean animating = false;

    public SlideMenu(Context context) {
        super(context);
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void init() {
        setOrientation(HORIZONTAL);
        LayoutTransition layoutTransition = new LayoutTransition();
        setLayoutTransition(layoutTransition);
        inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.floating_menu, this, true);
        recyclerView = (RecyclerView) findViewById(R.id.menu);
        adapter = new FloatingMenuAdapter();
        adapter.setItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SimpleItemDecoration(getContext(),SimpleItemDecoration.VERTICAL_LIST));
        postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Object> dt = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    dt.add(new Object());
                }
                adapter.setDatas(dt);
            }
        },1000);
        handler = findViewById(R.id.handler);
        handler.setOnClickListener(this);
    }

    public void setDatas(List<Object> datas)
    {
        adapter.setDatas(datas);
    }

    @Override
    public void onClick(View v) {
        if (expand)
        {
            recyclerView.setVisibility(GONE);
        }else {
            recyclerView.setVisibility(VISIBLE);
        }
        expand=!expand;
        Toast.makeText(getContext().getApplicationContext(), "click handler!", Toast.LENGTH_SHORT).show();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onItemClick(View itemView, int position) {
        if (recyclerView.isLayoutFrozen())return;
        if (parent == null)
            parent = (PercentRelativeLayout) getParent();
        if (ribbon != null&&ribbon.getParent() != null)
        {
            parent.removeView(ribbon);
        }
        ribbon = inflater.inflate(R.layout.specific_item_ribbon,this,false);
        ribbon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {//点击之后失去焦点
                Toast.makeText(getContext().getApplicationContext(), "----click---", Toast.LENGTH_SHORT).show();
            }
        });
        if (lp == null)
        lp = new PercentRelativeLayout.LayoutParams(parent.getWidth(),itemView.getHeight());
        parent.setOnTouchListener(this);
        ribbon.setZ(100);
        lp.topMargin = itemView.getTop();
        ribbon.setLayoutParams(lp);
        parent.addView(ribbon);
        if (ribbon.getOnFocusChangeListener()== null)
        ribbon.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!animating)
                    startTranslateOutAnimation();
//                    parent.removeView(ribbon);
                }
//                recyclerView.setLayoutFrozen(hasFocus);
                Log.i(TAG, "onFocusChange: "+hasFocus);
            }
        });
        ribbon.setFocusableInTouchMode(true);
        ribbon.setFocusable(true);
        ribbon.requestFocus();
        boolean b = ribbon.requestFocusFromTouch();
        Log.i(TAG, "onItemClick: "+b);
    }

    private void startTranslateOutAnimation() {

        ValueAnimator animator = ValueAnimator.ofInt(ribbon.getWidth(),0).setDuration(900);
        animator.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return input*input*input*input;
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
              /*  if (ribbonLP == null)
                {
                    ribbonLP = new PercentRelativeLayout.LayoutParams((int)animation.getAnimatedValue(),ribbon.getHeight());
                    ribbonLP.topMargin = ribbon.getTop();
                }
                else

                ribbonLP.width = (int) animation.getAnimatedValue();
                ribbon.setLayoutParams(ribbonLP);*/
                ((PercentRelativeLayout.LayoutParams)ribbon.getLayoutParams()).width = (int) animation.getAnimatedValue();
                ribbon.requestLayout();
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                parent.removeView(ribbon);
                ribbon = null;
                animating = false;
//                recyclerView.setLayoutFrozen(false);
            }
        });
        animator.start();
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {//在ribbon 获得焦点的情况下，事件都会被consume

            if (ribbon != null) {
                Rect outRect = new Rect();
                ribbon.getHitRect(outRect);
                if (!outRect.contains((int) event.getX(),(int) event.getY()))//响应ribbon 内部子view的click事件
                {
                    //recyclerview也不能再滚动
                    ribbon.setFocusable(false);
                }
            }

    if(ribbon != null&&ribbon.isInTouchMode())
        return true;
        else return false;
    }
}
