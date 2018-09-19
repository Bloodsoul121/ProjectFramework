package com.bloodsoul.projectframework.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bloodsoul.projectframework.common.Config;
import com.bloodsoul.projectframework.util.Logger;

public abstract class BaseFragment extends Fragment {

    private Toast toast;

    public BaseFragment() {}

//    public static BaseFragment newInstance() {
//        BaseFragment fragment = new BaseFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("key", "value");
//        fragment.setArguments(bundle);
//        return fragment;
//    }

    public void startActivity(Class<? extends Activity> target) {
        startActivity(target, null);
    }

    public void startActivity(Class<? extends Activity> target, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), target);
        if (bundle != null) {
            intent.putExtra(getActivity().getPackageName(), bundle);
        }
        startActivity(intent);
    }

    public void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void toast(final String msg) {
        try {
            getActivity().runOnUiThread(new Runnable() {

                @SuppressLint("ShowToast")
                @Override
                public void run() {
                    if (toast == null) {
                        toast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
                    }
                    toast.setText(msg);
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void log(String msg){
        if(Config.DEBUG){
            Logger.i(msg);
        }
    }

}
