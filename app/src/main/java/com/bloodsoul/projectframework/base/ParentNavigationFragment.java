package com.bloodsoul.projectframework.base;

import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bloodsoul.projectframework.R;

/**
 *  导航栏 ( 视需求进行修改 )
 *  rootView 需要子类赋值
 */
public abstract class ParentNavigationFragment extends BaseFragment {

    private View rootView = null; // 需要在子类中赋值, 即 onCreateView 的返回值
    private TextView tv_title;
    private TextView tv_right;
    private ImageView tv_left;
    private LinearLayout ll_navi;
    private ToolBarListener listener;

    /**
     * 初始化
     */
    public void initNaviView(){
        ll_navi = getView(R.id.ll_navi);
        tv_title = getView(R.id.tv_title);
        tv_right = getView(R.id.tv_right);
        tv_left = getView(R.id.tv_left);
        setListener(setToolBarListener());
        tv_left.setOnClickListener(clickListener);
        tv_right.setOnClickListener(clickListener);
        tv_title.setText(title());
        refreshTop();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_left:
                    if (listener != null){
                        listener.clickLeft();
                    }
                    break;
                case R.id.tv_right:
                    if (listener != null)
                        listener.clickRight();
                    break;

                default:
                    break;
            }
        }
    };

    private void refreshTop() {
        setLeftView(left());
        setValue(R.id.tv_right, right());
        this.tv_title.setText(title());
    }

    private void setLeftView(Object obj){
        if(obj !=null && !obj.equals("")){
            tv_left.setVisibility(View.VISIBLE);
            if(obj instanceof Integer){
                tv_left.setImageResource(Integer.parseInt(obj.toString()));
            }else{
//                tv_left.setImageResource(R.drawable.base_action_bar_back_bg_selector);
            }
        }else{
            tv_left.setVisibility(View.INVISIBLE);
        }
    }

    private void setValue(int id, Object obj){
        if (obj != null && !obj.equals("")) {
            ((TextView) getView(id)).setText("");
            getView(id).setBackgroundDrawable(new BitmapDrawable());
            if (obj instanceof String) {
                ((TextView) getView(id)).setText(obj.toString());
            } else if (obj instanceof Integer) {
                getView(id).setBackgroundResource(Integer.parseInt(obj.toString()));
            }
        } else {
            ((TextView) getView(id)).setText("");
            getView(id).setBackgroundDrawable(new BitmapDrawable());
        }
    }

    private void setListener(ToolBarListener listener) {
        this.listener = listener;
    }

    /**
     * 导航栏标题
     */
    protected abstract String title();

    /**
     * 导航栏右边：可以为string或图片资源id，不是必填项
     */
    public Object right(){
        return null;
    }

    /**
     * 导航栏左边
     */
    public Object left(){
        return null;
    }

    /**
     * 设置导航条背景色
     */
    public void setNavBackground(int color){
        ll_navi.setBackgroundColor(color);
    }

    /**
     * 设置右边按钮的文字大小
     */
    public void setRightTextSize(float dimenId){
        tv_right.setTextSize(dimenId);
    }

    /**
     * 设置导航栏监听
     */
    public ToolBarListener setToolBarListener(){
        return null;
    }

    protected <T extends View> T getView(int id) {
        if (rootView == null) {
            log("error : rootView is null , please assign a value to rootView");
        }
        return (T) rootView.findViewById(id);
    }

    public interface ToolBarListener {
        void clickLeft();
        void clickRight();
    }

}
