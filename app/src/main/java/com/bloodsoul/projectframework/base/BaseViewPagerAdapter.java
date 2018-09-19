package com.bloodsoul.projectframework.base;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseViewPagerAdapter<T> extends PagerAdapter {

    protected List<T> mDatas = new ArrayList<>();

    public BaseViewPagerAdapter() {}

    public BaseViewPagerAdapter(List<T> datas) {
        this.mDatas = datas;
    }

    public void setData(List<T> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = createItemView(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return createPageTitle(position);
    }

    protected abstract CharSequence createPageTitle(int position);

    protected abstract View createItemView(int position);

}
