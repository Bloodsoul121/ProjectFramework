package com.bloodsoul.projectframework.test;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.bloodsoul.projectframework.R;
import com.bloodsoul.projectframework.base.BaseActivity;
import com.bloodsoul.projectframework.base.BaseViewPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
    }

    @Override
    protected void initView() {
        TabPageIndicator indicator = findViewById(R.id.indicator);
        ViewPager viewPager = findViewById(R.id.viewpager);

        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("page - " + i);
        }

        viewPager.setAdapter(new BaseViewPagerAdapter<String>(list) {
            @Override
            public CharSequence getPageTitle(int position) {
                return mDatas.get(position);
            }

            @Override
            protected View createItemView(int position) {
                TextView tv = new TextView(mContext);
                return tv;
            }

            @Override
            protected void bindItemView(View view, int position) {
                ((TextView)view).setText(mDatas.get(position));
            }
        });

        indicator.setViewPager(viewPager);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
