package com.wwlh.projectframework.base;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 *  1.封装了头部view和底部view
 *  2.封装了初始化数据前后的UI展示
 *  3.封装了多种布局,由子类实现
 */
public abstract class BaseRecyAdapter<T> extends RecyclerView.Adapter<BaseRecyViewHolder> {

    protected static final int TYPE_COMMON_VIEW = 100001;//普通类型 Item
    private static final int TYPE_FOOTER_VIEW = 100002;//footer类型 Item
    private static final int TYPE_INIT_LOADING_VIEW = 100003;//初始化加载时的提示View
    private static final int TYPE_EMPTY_VIEW = 100004;//初次加载无数据的默认空白view
    private static final int TYPE_RELOAD_VIEW = 100005;//初次加载无数据的可重新加载或提示用户的view
    private static final int TYPE_BASE_HEADER_VIEW = 200000;

    protected List<T> mDatas;
    protected Context mContext;
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected SparseArrayCompat<View> mHeaderViews;
    protected View mInitLoadingView; //首次预加载view
    protected View mReloadView; //首次预加载失败、或无数据的view
    protected RelativeLayout mFooterLayout;//FooterView
    private OnRecyclerViewItemClickerListener mItemClickListener;
    private ArrayList<Integer> mItemChildIds;
    private ArrayList<OnItemChildClickListener<T>> mItemChildListeners;

    protected boolean mShowHeaderView;//是否显示HeaderView
    protected boolean mIsInitDataEnd;//是否已经初始化过数据了

    protected abstract int getViewType(T data, int position);
    protected abstract int getItemLayoutId();

    public BaseRecyAdapter(Context context) {
        this(context, null);
    }

    public BaseRecyAdapter(Context context, List<T> datas) {
        this.mContext = context;
        this.mDatas = datas == null ? new ArrayList<T>() : datas;
        init();
    }

    public void bindRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        this.mLayoutManager = mRecyclerView.getLayoutManager();
    }

    private void init() {
        mShowHeaderView = true;
        mHeaderViews = new SparseArrayCompat<>();
        mItemChildIds = new ArrayList<>();
        mItemChildListeners = new ArrayList<>();
        this.mFooterLayout = new RelativeLayout(mContext);
    }

    public void setData(List<T> data) {
        if (data != null) {
            this.mDatas.clear();
            this.mDatas.addAll(data);
            notifyDataSetChanged();
        }
        mIsInitDataEnd = true;
    }

    public void setIsShowHeaderView(boolean showHeaderView) {
        this.mShowHeaderView = showHeaderView;
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.isEmpty()) {
            if (mInitLoadingView != null) {
                return TYPE_INIT_LOADING_VIEW;
            }

            if (mReloadView != null) {
                return TYPE_RELOAD_VIEW;
            }

            if (mShowHeaderView && isHeaderView(position)) {
                return mHeaderViews.keyAt(position);
            }

            return TYPE_EMPTY_VIEW;
        }

        if (mShowHeaderView && isHeaderView(position)) {
            return mHeaderViews.keyAt(position);
        }

        if (isFooterView(position)) {
            return TYPE_FOOTER_VIEW;
        }

        return getViewType(mDatas.get(position - getHeaderCount()), position - getHeaderCount());
    }

    @Override
    public BaseRecyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mShowHeaderView && mHeaderViews.get(viewType) != null) {
            return BaseRecyViewHolder.create(mHeaderViews.get(viewType));
        }

        if (isCommonItemView(viewType)) {
            return BaseRecyViewHolder.create(mContext, getItemLayoutId(), parent);
        }

        BaseRecyViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_FOOTER_VIEW:
                viewHolder = BaseRecyViewHolder.create(getFooterLayout());
                break;
            case TYPE_INIT_LOADING_VIEW:
                viewHolder = BaseRecyViewHolder.create(mInitLoadingView);
                break;
            case TYPE_RELOAD_VIEW:
                viewHolder = BaseRecyViewHolder.create(mReloadView);
                break;
            case TYPE_EMPTY_VIEW:
                viewHolder = BaseRecyViewHolder.create(new View(mContext));
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseRecyViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if (isCommonItemView(viewType)) {
            bindCommonItem(holder, position - getHeaderCount());
        }
    }

    private void bindCommonItem(final BaseRecyViewHolder holder, final int position) {
        final T t = mDatas.get(position);
        onBindItemViewHolder(holder, t, position);

        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(holder, t, position);
                }
            }
        });

        for (int i = 0; i < mItemChildIds.size(); i++) {
            final int tempI = i;
            if (holder.getItemView().findViewById(mItemChildIds.get(i)) != null) {
                holder.getItemView().findViewById(mItemChildIds.get(i)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemChildListeners.get(tempI).onItemChildClick(holder, t, position);
                    }
                });
            }
        }
    }

    protected abstract void onBindItemViewHolder(BaseRecyViewHolder holder, T t, int position);

    @Override
    public int getItemCount() {
        if (mDatas.isEmpty() && (mInitLoadingView != null || mReloadView != null)) {
            return 1;
        }
        return mDatas.size() + getFooterViewCount() + getHeaderCount();
    }

    protected int getFooterViewCount() {
        return 0;
    }

    private int getHeaderCount() {
        if (mShowHeaderView) {
            return mHeaderViews.size();
        }
        return 0;
    }

    private RelativeLayout getFooterLayout() {
        if (mFooterLayout == null) {
            mFooterLayout = new RelativeLayout(mContext);
        }
        return mFooterLayout;
    }

    public void addHeaderView(View view) {
        if (view == null) {
            return;
        }
        mHeaderViews.put(TYPE_BASE_HEADER_VIEW + getHeaderCount(), view);
    }

    public void removeHeaderView(int position) {
        if (position < 0 || position >= getHeaderCount()) {
            return;
        }
        mHeaderViews.remove(TYPE_BASE_HEADER_VIEW + position);
        notifyItemRemoved(position);
    }

    protected boolean isHeaderView(int position) {
        return position < getHeaderCount();
    }

    protected boolean isFooterView(int position) {
        return false;
    }

    protected boolean isCommonItemView(int viewType) {
        return viewType != TYPE_EMPTY_VIEW && viewType != TYPE_FOOTER_VIEW
                && viewType != TYPE_INIT_LOADING_VIEW && viewType != TYPE_RELOAD_VIEW
                && !(viewType >= TYPE_BASE_HEADER_VIEW);
    }

    protected void addFooterView(View footerView) {
        if (footerView == null) {
            return;
        }

        getFooterLayout();
        removeFooterView();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mFooterLayout.addView(footerView, params);
    }

    private void removeFooterView() {
        mFooterLayout.removeAllViews();
    }

    public T getData(int position) {
        if (mDatas.isEmpty()) {
            return null;
        }
        return mDatas.get(position);
    }

    public List<T> getAllData() {
        return mDatas;
    }

    public interface OnRecyclerViewItemClickerListener<T> {
        void onItemClick(BaseRecyViewHolder view, Object data, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickerListener<T> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface OnItemChildClickListener<T> {
        void onItemChildClick(BaseRecyViewHolder viewHolder, T data, int position);
    }

    public void setOnItemChildClickListener(int viewId, OnItemChildClickListener<T> itemChildClickListener) {
        mItemChildIds.add(viewId);
        mItemChildListeners.add(itemChildClickListener);
    }

    public void remove(int position) {
        if (position >= mDatas.size() || position < 0) {
            return;
        }
        mDatas.remove(position);
        notifyItemRemoved(position + getHeaderCount());
        if (position != mDatas.size()) {
            notifyItemRangeChanged(position + getHeaderCount(), mDatas.size() - position);
        }
    }

    public void insert(final List<T> datas, final int position) {
        if (position > mDatas.size() || position < 0) {
            return;
        }
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mDatas.addAll(position, datas);
                notifyItemRangeInserted(position + getHeaderCount(), datas.size());
                notifyItemRangeChanged(position + getHeaderCount(), datas.size());
            }
        });
    }

    public void insert(List<T> datas) {
        insert(datas, mDatas.size());
    }

    public void insert(final T data, final int position) {
        if (position > mDatas.size() || position < 0) {
            return;
        }
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mDatas.add(position, data);
                notifyItemInserted(position + getHeaderCount());
                notifyItemRangeChanged(position + getHeaderCount(), mDatas.size() - position);
            }
        });
    }

    public void insert(T data) {
        insert(data, mDatas.size());
    }

    public void change(int position) {
        notifyItemChanged(position);
    }

    public void reset() {
        mDatas.clear();
    }

    protected View inflate(Context context, int layoutId) {
        if (layoutId <= 0) {
            return null;
        }
        return LayoutInflater.from(context).inflate(layoutId, null);
    }

    public void setInitLoadingView(View initLoadingView) {
        mInitLoadingView = initLoadingView;
    }

    public void removeInitLoadingView() {
        mInitLoadingView = null;
        notifyDataSetChanged();
    }

    public void setReloadView(View reloadView) {
        mReloadView = reloadView;
        mInitLoadingView = null;
        notifyDataSetChanged();
    }

}
