package com.bloodsoul.projectframework.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bloodsoul.projectframework.R;
import com.bloodsoul.projectframework.base.BaseActivity;
import com.bloodsoul.projectframework.base.BaseRecyAdapter;
import com.bloodsoul.projectframework.base.BaseRecyViewHolder;
import com.bloodsoul.projectframework.base.PageRecyAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

//        MyAdapter adapter = new MyAdapter(this);
//        adapter.bindRecyclerView(mRecyclerView);
//        mRecyclerView.setAdapter(adapter);
//
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            list.add("item - " + i);
//        }
//        adapter.setData(list);

        final MyPageAdapter adapter = new MyPageAdapter(this);
        adapter.bindRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(adapter);

        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list.add("item - " + i);
        }

        adapter.setOnLoadMoreListener(new PageRecyAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                adapter.addLoadMoreData(list);
            }
        });
        adapter.openAutoLoadMore();
        adapter.startLoadMore();

        adapter.setData(list);
    }

    private static class MyAdapter extends BaseRecyAdapter<String> {

        public MyAdapter(Context context) {
            super(context);
        }

        public MyAdapter(Context context, List<String> datas) {
            super(context, datas);
        }

        @Override
        protected int getViewType(String data, int position) {
            return 0;
        }

        @Override
        protected int getItemLayoutId() {
            return R.layout.layout_recyclerview_item;
        }

        @Override
        protected void onBindItemViewHolder(BaseRecyViewHolder holder, String s, int position) {
            holder.setText(R.id.tv, s);
        }
    }

    private static class MyPageAdapter extends PageRecyAdapter<String> {

        public MyPageAdapter(Context context) {
            super(context);
        }

        @Override
        public View inflateLoadingView() {
            TextView tv = new TextView(mContext);
            tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
            tv.setPadding(10,10,10,10);
            tv.setText("loading");
            return tv;
        }

        @Override
        public View inflateLoadFailedView() {
            TextView tv = new TextView(mContext);
            tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
            tv.setPadding(10,10,10,10);
            tv.setText("loading failed");
            return tv;
        }

        @Override
        public View inflateLoadEndView() {
            TextView tv = new TextView(mContext);
            tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
            tv.setPadding(10,10,10,10);
            tv.setText("loading end");
            return null;
        }

        @Override
        protected int getItemLayoutId() {
            return R.layout.layout_recyclerview_item;
        }

        @Override
        protected void onBindItemViewHolder(BaseRecyViewHolder holder, String s, int position) {
            holder.setText(R.id.tv, s);
        }
    }

}
