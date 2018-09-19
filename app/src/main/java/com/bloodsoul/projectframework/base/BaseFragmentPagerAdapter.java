package com.bloodsoul.projectframework.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class BaseFragmentPagerAdapter<T extends Fragment> extends FragmentPagerAdapter {

    private List<T> mDatas;

    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(List<T> list) {
        this.mDatas = list;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        if (mDatas == null) {
            return null;
        }
        return mDatas.get(position);
    }

    @Override
    public int getCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }
}
