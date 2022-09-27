package com.example.myapplication;

/**
 * @ describe
 * @ author lzl
 * @ time 2022/8/1 10:38 下午
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjianli on 2016/4/5 0005.
 */
public class CheckPermission {

    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案
    /**
     * 6.0以上：activity单个权限申请
     * @param context
     * @param permissionName
     * @param requstCode
     * @return
     */
    public static boolean requestPermission(Activity context, String permissionName, int requstCode) {

        if (ContextCompat.checkSelfPermission(context, permissionName) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(context, new String[]{permissionName}, requstCode);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 6.0以上：activity多个权限申请
     * @param context
     * @param permissions
     * @param requstCode
     * @return
     */
    public static boolean requestPermissions(Activity context, List<String> permissions, int requstCode){

        List<String> needRequetPermissions = new ArrayList<>();
        for(int i = 0;i < permissions.size();i++){
            String permission =  permissions.get(i);
            if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                needRequetPermissions.add(permission);
            }
        }

        int requestPermissionCount = needRequetPermissions.size();
        if(requestPermissionCount > 0){
            String[]requestPermissions = new String[requestPermissionCount];
            for(int i = 0;i < requestPermissionCount;i++){
                requestPermissions[i] = needRequetPermissions.get(i);
            }
            ActivityCompat.requestPermissions(context, requestPermissions, requstCode);
            return false;
        }else{
            return true;
        }
    }

    /**
     *:6.0以上：fragment的单个权限申请
     * @param activity
     * @param fragment
     * @param permissionName
     * @param requstCode
     * @return
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean requestPermissionFragment(Activity activity, Fragment fragment, String permissionName, int requstCode) {

        if (ContextCompat.checkSelfPermission(activity, permissionName) != PackageManager.PERMISSION_GRANTED) {

            fragment.requestPermissions(new String[]{permissionName}, requstCode);
            return false;
        } else {
            return true;
        }
    }

    /**
     *:6.0以上：fragment的多个权限申请
     * @param activity
     * @param fragment
     * @param permissions
     * @param requstCode
     * @return
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean requestPermissionFragments(Activity activity, Fragment fragment, List<String> permissions, int requstCode) {

        List<String> needRequetPermissions = new ArrayList<>();
        for(int i = 0;i < permissions.size();i++){
            String permission =  permissions.get(i);
            if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                needRequetPermissions.add(permission);
            }
        }

        int requestPermissionCount = needRequetPermissions.size();
        if(requestPermissionCount > 0){
            String[]requestPermissions = new String[requestPermissionCount];
            for(int i = 0;i < requestPermissionCount;i++){
                requestPermissions[i] = needRequetPermissions.get(i);
            }
            fragment.requestPermissions(requestPermissions, requstCode);
            return false;
        }else{
            return true;
        }

    }

    // 显示缺失权限提示
//    public static void showMissingPermissionDialog(final Activity context, String permission) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(context.getResources().getString(R.string.dialog_permission_title));
//        builder.setMessage("由于"+"不能获取设备信息的权限，不能正常运行，请设置" + permission + "权限后使用。设置路径：系统设置->"+ MyApp.mApp.getResources().getString(R.string.app_name)+"->权限");
//        builder.setCancelable(false);
//        // 拒绝, 退出应用
//        builder.setNegativeButton(context.getResources().getString(R.string.dialog_permission_negative), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(context, MainActivity.class);
//                intent.putExtra("EXIT_TAG", "SINGLETASK");
//                context.startActivity(intent);
//            }
//        });
//
//        builder.setPositiveButton(context.getResources().getString(R.string.dialog_permission_positive), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                goSetting(context);
//            }
//        });
//
//        builder.show();
//    }

    //去应用手动设置权限
    public static void goSetting(Activity context){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + context.getPackageName()));
        context.startActivity(intent);
    }
}
