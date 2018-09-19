package com.wwlh.projectframework.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseRecyViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;

    public BaseRecyViewHolder(View itemView) {
        super(itemView);
        this.mViews = new SparseArray<>();
    }

    public static BaseRecyViewHolder create(Context context, int layoutId, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new BaseRecyViewHolder(itemView);
    }

    public static BaseRecyViewHolder create(View itemView) {
        return new BaseRecyViewHolder(itemView);
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getItemView() {
        return itemView;
    }

    public void setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
    }

    public String getText(int viewId) {
        TextView textView = getView(viewId);
        return textView.getText().toString();
    }

    public void setTextColor(Context context, int viewId, int colorId) {
        TextView textView = getView(viewId);
        textView.setTextColor(context.getResources().getColor(colorId));
    }

    public void setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        View view = getView(viewId);
        view.setOnClickListener(onClickListener);
    }

    public void setBgRes(int viewId, int resId) {
        View view = getView(viewId);
        view.setBackgroundResource(resId);
    }

    public void setBgColor(Context context, int viewId, int colorId) {
        View view = getView(viewId);
        view.setBackgroundColor(context.getResources().getColor(colorId));
    }

    public void setVisibility(int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
    }

    public void setMaxLine(int viewId, int maxLine) {
        TextView view = (TextView) getView(viewId);
        view.setMaxLines(maxLine);
    }

    public void setImageLevel(int viewId, int level) {
        ImageView view = (ImageView) getView(viewId);
        view.setImageLevel(level);
    }

    public void setImageAlpha(int viewId, float alpha) {
        ImageView view = (ImageView) getView(viewId);
        view.setAlpha(alpha);
    }

    public void setTextRightDrawable(Context context, int viewId, int resId) {
        TextView view = (TextView) getView(viewId);
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, drawable, null);
    }

}