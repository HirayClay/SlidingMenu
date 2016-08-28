package com.example.cjj.slidingmenu.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.example.cjj.slidingmenu.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CJJ on 2016/8/25.
 */
public class FloatingMenuAdapter extends RecyclerView.Adapter<FloatingMenuAdapter.Holder> {

    LayoutInflater inflater;
    List<Object> datas = new ArrayList<>();
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FloatingMenuAdapter() {
        for (int i = 0; i < 16; i++) {
            datas.add(new Object());
        }
    }

    public void setDatas(List<Object> dt) {
        if (dt != null && !dt.isEmpty())
            datas.clear();
        datas.addAll(dt);
        notifyDataSetChanged();
    }

    @Override
    public FloatingMenuAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.getContext());
        return new Holder(inflater.inflate(R.layout.item_floating, parent, false));
    }

    @Override
    public void onBindViewHolder(final FloatingMenuAdapter.Holder holder, final int position) {
        //do nothing bu stare blankly.....
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.onItemClick(holder.itemView,holder.getLayoutPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        @Bind(R.id.content)
        LinearLayout content;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface ItemClickListener {

        void onItemClick(View itemView, int position);
    }
}
