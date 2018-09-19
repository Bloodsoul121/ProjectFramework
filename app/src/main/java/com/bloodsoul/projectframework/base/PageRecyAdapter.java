package com.bloodsoul.projectframework.base;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class PageRecyAdapter<T> extends BaseRecyAdapter<T> {

    private boolean mIsOpenLoadMore;//是否开启加载更多
    private boolean mIsAutoLoadMore;//是否自动加载，当数据不满一屏幕会自动加载
    private boolean mIsAutoLoadMoreEnd;//自动加载更多是否已经结束
    private boolean mIsReset;//开始重新加载数据
    private boolean mIsLoading;//是否正在加载更多

    private View mLoadingView; //分页加载中view
    private View mLoadFailedView; //分页加载失败view
    private View mLoadEndView; //分页加载结束view

    private OnLoadMoreListener mLoadMoreListener;

    public PageRecyAdapter(Context context) {
        this(context, null, true);
    }

    public PageRecyAdapter(Context context, List<T> datas) {
        this(context, datas, true);
    }

    public PageRecyAdapter(Context context, List<T> datas, boolean isOpenLoadMore) {
        super(context, datas);
        this.mIsOpenLoadMore = isOpenLoadMore;
        this.mLoadingView = inflateLoadingView();
        this.mLoadFailedView = inflateLoadFailedView();
        this.mLoadEndView = inflateLoadEndView();
        addFooterView(mLoadingView);
    }

    @Override
    protected int getViewType(T data, int position) {
        return TYPE_COMMON_VIEW;
    }

    @Override
    protected int getFooterViewCount() {
        return mIsOpenLoadMore && !mDatas.isEmpty() ? 1 : 0;
    }

    @Override
    protected boolean isFooterView(int position) {
        return mIsOpenLoadMore && position >= getItemCount() - 1;
    }

    @Override
    public void reset() {
        if (mLoadingView != null) {
            addFooterView(mLoadingView);
        }
        mIsLoading = false;
        mIsReset = true;
        mIsAutoLoadMoreEnd = false;
        super.reset();
    }

    @Override
    public void onViewAttachedToWindow(BaseRecyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isFooterView(position) || isHeaderView(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isFooterView(position) || isHeaderView(position)) {
                        return gridManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    public void startLoadMore() {
        if (!mIsOpenLoadMore || mLoadMoreListener == null) {
            return;
        }

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (mIsAutoLoadMoreEnd && findLastVisibleItemPosition(mLayoutManager) + 1 == getItemCount()) {
                        scrollLoadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (findLastVisibleItemPosition(mLayoutManager) + 1 == getItemCount()) {
                    if (mInitLoadingView != null || mReloadView != null || (mHeaderViews.size() > 0 && mShowHeaderView && mDatas.isEmpty())) {
                        return;
                    }
                    if (mIsAutoLoadMore && !mIsAutoLoadMoreEnd) {
                        scrollLoadMore();
                    } else if (!mIsAutoLoadMoreEnd) {
                        loadEnd();
                        mIsAutoLoadMoreEnd = true;
                    }
                } else {
                    mIsAutoLoadMoreEnd = true;
                }
            }
        });
    }

    private void scrollLoadMore() {
        if (mIsReset) {
            return;
        }

        if (mFooterLayout.getChildAt(0) == mLoadingView && !mIsLoading) {
            if (mLoadMoreListener != null) {
                mIsLoading = true;
                mLoadMoreListener.onLoadMore(false);
            }
        }
    }

    private int findLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            return findMax(lastVisibleItemPositions);
        }
        return -1;
    }

    private int findMax(int[] lastVisiblePositions) {
        int max = lastVisiblePositions[0];
        for (int value : lastVisiblePositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public void addLoadMoreData(List<T> datas) {
        mIsLoading = false;
        insert(datas, mDatas.size());
    }

    @Override
    public void setData(List<T> data) {
        if (mIsOpenLoadMore) {
            if (mIsReset) {
                mIsReset = false;
            }
            mIsLoading = false;
            mInitLoadingView = null;
            mReloadView = null;
        }
        super.setData(data);
    }

    public void setLoadingView(int loadingId) {
        setLoadingView(inflate(mContext, loadingId));
    }

    public void setLoadingView(View loadingView) {
        mLoadingView = loadingView;
        addFooterView(mLoadingView);
    }

    public void setLoadFailedView(int loadFailedId) {
        setLoadFailedView(inflate(mContext, loadFailedId));
    }

    public void setLoadFailedView(View loadFailedView) {
        mLoadFailedView = loadFailedView;
    }

    public void setLoadEndView(int loadEndId) {
        setLoadEndView(inflate(mContext, loadEndId));
    }

    public void setLoadEndView(View loadEndView) {
        mLoadEndView = loadEndView;
    }

    public interface OnLoadMoreListener {
        void onLoadMore(boolean isReload);//是否为加载失败的重新加载
    }

    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    private void loadEnd() {
        if (mLoadEndView != null) {
            addFooterView(mLoadEndView);
        } else {
            addFooterView(new View(mContext));
        }
    }

    public void loadFailed() {
        addFooterView(mLoadFailedView);
        mLoadFailedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFooterView(mLoadingView);
                if (mLoadMoreListener != null) {
                    mLoadMoreListener.onLoadMore(true);
                }
            }
        });
    }

    public void openAutoLoadMore() {
        this.mIsAutoLoadMore = true;
    }

    public void clearLoadView() {
        addFooterView(new View(mContext));
    }

    public abstract View inflateLoadingView();
    public abstract View inflateLoadFailedView();
    public abstract View inflateLoadEndView();

}
