package com.example.cjj.slidingmenu;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.cjj.slidingmenu.fragment.MainContentFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.add(R.id.container,MainContentFragment.newInstance());
//        ft.addToBackStack(null).commitAllowingStateLoss();
    }

    public void click(View view) {
        switch (view.getId())
        {
            case R.id.menu:
//                startActivity(new Intent(this,));
                break;
            case R.id.map:
                startActivity(new Intent(this,MapActivity.class));
                break;
        }
    }
}
