package com.test.firebasetast;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class weatherActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weatheractivity_layout);
        String TAG = "oncreate";

        //mainActivity送來的資料
        Bundle bundle = getIntent().getExtras();

        //設定分頁
        ArrayList<View> mPages = new ArrayList<>();
        for (int i=0;i<3;i++) {//新增5個分頁
            mPages.add(new Pagers(getApplicationContext(), (i + 1),bundle));
        }

        //主要原件
        ViewPager viewPager = findViewById(R.id.mViewPager);
        TabLayout tab = findViewById(R.id.tab);

        //調節器
        viewPagerAdapter myPagerAdapter = new viewPagerAdapter(mPages);

        tab.setupWithViewPager(viewPager);//將TabLayout綁定給ViewPager
        viewPager.setAdapter(myPagerAdapter);//綁定適配器
        viewPager.setCurrentItem(0);//指定跳到某頁，一定得設置在setAdapter後面

    }


}
