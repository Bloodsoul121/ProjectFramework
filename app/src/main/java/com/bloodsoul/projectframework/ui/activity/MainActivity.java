package com.bloodsoul.projectframework.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import com.bloodsoul.projectframework.R;
import com.bloodsoul.projectframework.base.PermissionActivity;

import java.util.List;

public class MainActivity extends PermissionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    public void clickBtn(View view) {
        String[] permissions = new String[] {Manifest.permission.CALL_PHONE};
        requestRunPermisssion(permissions, new PermissionListener() {
            @Override
            public void onGranted() {
                log("succeed");
            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                log("deny : " + deniedPermission.toString());
                showNeedPermissionDialog();
            }
        });
    }

    public void clickBtn2(View view) {
        startActivity(RecyclerViewActivity.class);
    }

    public void clickBtn3(View view) {
        startActivity(ViewPagerActivity.class);
    }

    public void clickBtn4(View view) {
        startActivity(LazyFragmentActivity.class);
    }
}
