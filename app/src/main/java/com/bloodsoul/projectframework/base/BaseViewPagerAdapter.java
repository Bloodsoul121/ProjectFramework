package com.bloodsoul.projectframework.base;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

public abstract class BaseViewPagerAdapter<T> extends PagerAdapter {

    private LinkedList<View> mViewCache;

    protected List<T> mDatas;

    public BaseViewPagerAdapter() {
        this(null);
    }

    public BaseViewPagerAdapter(List<T> datas) {
        this.mDatas = datas;
        this.mViewCache = new LinkedList<>();
    }

    public void setData(List<T> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View convertView;
        if(mViewCache.size() == 0){
            convertView = createItemView(position);
        } else {
            convertView = mViewCache.removeFirst();
        }
        bindItemView(convertView, position);
        container.addView(convertView);
        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View contentView = (View) object;
        container.removeView(contentView);
        mViewCache.add(contentView);
    }

    protected abstract View createItemView(int position);

    protected abstract void bindItemView(View view, int position);

}
