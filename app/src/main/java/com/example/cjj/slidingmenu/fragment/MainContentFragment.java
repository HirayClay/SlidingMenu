package com.example.cjj.slidingmenu.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cjj.slidingmenu.R;
import com.example.cjj.slidingmenu.widget.LoadingView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainContentFragment extends Fragment {


    private static final String TAG = "MainContentFragment";
    LayoutInflater inflater;
    @Bind(R.id.loadingview)
    LoadingView loadingview;

    public MainContentFragment() {
        // Required empty public constructor
    }

    public static MainContentFragment newInstance() {

        Bundle args = new Bundle();

        MainContentFragment fragment = new MainContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_content, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Handler(getActivity().getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run: ");
                loadingview.setText("测试：TextView Faking!!!!!!~~`");
            }
        },2000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
