package com.bloodsoul.projectframework.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.bloodsoul.projectframework.R;
import com.bloodsoul.projectframework.base.BaseActivity;
import com.bloodsoul.projectframework.base.BaseRecyAdapter;
import com.bloodsoul.projectframework.base.BaseRecyViewHolder;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class XRecyclerViewActivity extends BaseActivity {

    private XRecyclerView mXRecyclerView;
    private XAdapter mXAdapter;

    private List<String> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xrecycler_view);
    }

    @Override
    protected void initView() {
        mXRecyclerView = findViewById(R.id.xrecyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mXRecyclerView.setLayoutManager(layoutManager);
        mXAdapter = new XAdapter(this);

        initData();

        mXRecyclerView.setAdapter(mXAdapter);
        mXAdapter.bindRecyclerView(mXRecyclerView);
        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mXRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mXAdapter.insert(mDatas);
                                mXRecyclerView.loadMoreComplete();
                            }
                        });
                    }
                }).start();
            }
        });

        mXRecyclerView.setRefreshProgressStyle(ProgressStyle.BallZigZag); //设定下拉刷新样式
        mXRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallZigZag);//设定上拉加载样式

        // 底部提示语
        mXRecyclerView.getDefaultFootView().setLoadingHint("自定义加载中提示");
        mXRecyclerView.getDefaultFootView().setNoMoreHint("自定义加载完毕提示");

        // When the item number of the screen number is list.size-2,we call the onLoadMore
        // 即当底部还剩2条时, 就调用onLoadMore方法, 默认是1
        mXRecyclerView.setLimitNumberToCallLoadMore(2);

//        mXRecyclerView.setLoadingMoreEnabled(false);

        mXAdapter.setOnItemClickListener(new BaseRecyAdapter.OnRecyclerViewItemClickerListener<String>() {
            @Override
            public void onItemClick(BaseRecyViewHolder view, Object data, int position) {
                log("onItemClick - " + data + " / " + position + " / " + mXAdapter.getItemCount());
            }
        });

    }

    private void initData() {
        for (int i = 0; i < 50; i++) {
            mDatas.add("item - " + i);
        }
        mXAdapter.setData(mDatas);
    }

    private static class XAdapter extends BaseRecyAdapter<String> {
        public XAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getItemLayoutId() {
            return R.layout.layout_item_xrecy;
        }

        @Override
        protected void onBindItemViewHolder(BaseRecyViewHolder holder, String s, int position) {
            holder.setText(R.id.textview, s);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mXRecyclerView != null) {
            mXRecyclerView.destroy();
            mXRecyclerView = null;
        }
    }
}
