package com.wwlh.projectframework.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import java.util.ArrayList;
import java.util.List;

public abstract class PermissionActivity extends BaseActivity {

    private static final int PERMISSION_REQUESTCODE = 100;

    private PermissionListener mListener;

    public void requestRunPermisssion(String[] permissions, PermissionListener listener){
        mListener = listener;
        List<String> permissionLists = new ArrayList<>();
        for(String permission : permissions){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                permissionLists.add(permission);
            }
        }

        if(!permissionLists.isEmpty()){
            ActivityCompat.requestPermissions(this, permissionLists.toArray(new String[permissionLists.size()]), PERMISSION_REQUESTCODE);
        }else{
            //表示全都授权了
            mListener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUESTCODE:
                if(grantResults.length > 0){
                    //存放没授权的权限
                    List<String> deniedPermissions = new ArrayList<>();
                    for(int i = 0; i < grantResults.length; i++){
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if(grantResult != PackageManager.PERMISSION_GRANTED){
                            deniedPermissions.add(permission);
                        }
                    }
                    if(deniedPermissions.isEmpty()){
                        //说明都授权了
                        mListener.onGranted();
                    }else{
                        mListener.onDenied(deniedPermissions);
                    }
                }
                break;
            default:
                break;
        }
    }

    public interface PermissionListener {

        void onGranted();//已授权

        void onDenied(List<String> deniedPermission);//未授权

    }

    public boolean shouldShowRequestPermissionRationale(List<String> deniedPermission) {
        for (String permission : deniedPermission) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                return true;
            }
        }
        return false;
    }

    public void showNeedPermissionDialog() {
        new AlertDialog.Builder(this)
                .setMessage("需要开启权限才能使用此功能")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts("package", getPackageName(), null));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

}
