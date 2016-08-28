package com.example.cjj.slidingmenu.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.cjj.slidingmenu.widget.DataBindIntef;

import java.util.List;

/**
 * Created by CJJ on 2016/8/25.
 */
public class BaseMenuAdapter<T> extends RecyclerView.Adapter implements DataBindIntef {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void setDatas(List datas) {

    }
}
