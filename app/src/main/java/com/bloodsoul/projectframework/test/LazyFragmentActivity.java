package com.bloodsoul.projectframework.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bloodsoul.projectframework.R;
import com.bloodsoul.projectframework.base.BaseActivity;
import com.bloodsoul.projectframework.base.LazyFragment;
import com.bloodsoul.projectframework.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class LazyFragmentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lazy_fragment);
    }

    @Override
    protected void initView() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        List<LazyFragment> list = new ArrayList<>();
        list.add(MyFragment.newInstance("111"));
        list.add(MyFragment.newInstance("222"));
        list.add(MyFragment.newInstance("333"));
        list.add(MyFragment.newInstance("444"));
        list.add(MyFragment.newInstance("555"));
        list.add(MyFragment.newInstance("666"));

        adapter.setData(list);

    }

    private static class MyAdapter extends FragmentPagerAdapter {

        private List<LazyFragment> mDatas = new ArrayList<>();

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setData(List<LazyFragment> list) {
            this.mDatas = list;
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }
    }

    public static class MyFragment extends LazyFragment {

        private String name;

        public static MyFragment newInstance(String name) {
            MyFragment fragment = new MyFragment();
            Bundle bundle = new Bundle();
            bundle.putString("key", name);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        protected void onFragmentVisibleChange(boolean isVisible) {
            Logger.i(name + " isVisible : " + isVisible);
        }

        @Override
        protected void onFragmentFirstVisible() {
            Logger.i(name + " init");
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            this.name = getArguments().getString("key");
            TextView tv = new TextView(getActivity());
            tv.setText(name);
            Logger.i(name + " onCreateView");
            return tv;
        }
    }

}
