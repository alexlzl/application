package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.task.DownloadTask;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
//https://cloud.tencent.com/developer/article/1378402
public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private String TAG = "TAG";
    private Button button;
    private String DOWNLOAD_URL = "http://3g.163.com/links/4636";
    private String DOWNLOAD_PATH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Aria.download(this).register();
        button = findViewById(R.id.button_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goCapture();
            }
        });
        DOWNLOAD_PATH=getFilesDir().getAbsolutePath()+"/test.apk";
//        DOWNLOAD_PATH =getAppFolder().getPath()+ File.separator+"test.apk";
        Log.e(TAG, "存储路径===" + DOWNLOAD_PATH);
//        DOWNLOAD_PATH = Environment.getExternalStorageDirectory() + "/test.apk";
    }

    private void goCapture() {
        List<String> strings = new ArrayList<>();
        strings.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        strings.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        strings.add(Manifest.permission.REQUEST_INSTALL_PACKAGES);
        if (CheckPermission.requestPermissions(this, strings, 1)) {
            //已经拥有权限后执行的操作
            download();
        }
    }

    private void download() {
        Toast.makeText(this, "开始下载", Toast.LENGTH_SHORT).show();
        long taskId = Aria.download(this)
                .load(DOWNLOAD_URL)     //读取下载地址
                .setFilePath(DOWNLOAD_PATH) //设置文件保存的完整路径
                .create();   //创建并启动下载
    }

    //在这里处理任务执行中的状态，如进度进度条的刷新
    @Download.onTaskRunning
    protected void running(DownloadTask task) {
        Toast.makeText(this, "下载回调", Toast.LENGTH_SHORT).show();
        if (task.getKey().equals(DOWNLOAD_URL)) {
            int p = task.getPercent();    //任务进度百分比
            String speed = task.getConvertSpeed();    //转换单位后的下载速度，单位转换需要在配置文件中打开
            long speed1 = task.getSpeed(); //原始byte长度速度
            Log.e(TAG, "任务比例===" + p);
        }

    }

    @Download.onTaskComplete
    void taskComplete(DownloadTask task) {
        //在这里处理任务完成的状态
        if (task.getKey().equals(DOWNLOAD_URL)) {
            Log.e(TAG, "任务下载完成");
            installApk();
        }

    }

    public  final String UPDATE_APK_URL = getAppFolder() + File.separator + "apk";
    private  final String APP_FOLDER_NAME = "Allinmd";
    public  String UPDATE_APK_NAME = "Allinmd"  + ".apk";
    private  String apkfile = UPDATE_APK_URL + File.separator +
            UPDATE_APK_NAME;
    public  File getAppFolder() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File appFolder = new File(Environment.getExternalStorageDirectory(), APP_FOLDER_NAME);
            return createOnNotFound(appFolder);
        } else {
            return null;
        }
    }
    private static File createOnNotFound(final File folder) {
        if (folder == null) {
            return null;
        }

        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
//        if (folder.exists()) {
//            return folder;
//        } else {
//            return null;
//        }
    }
    /**
     * 下载成功后安装apk
     *
     * @param
     */
    public  void installApk( ) {
        File file = new File(DOWNLOAD_PATH);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String authority = getString( R.string.authorityOfAppPackageProvider);
                Uri apkUri = FileProvider.getUriForFile(this, authority, file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            startActivity(intent);
            //Note:注释掉是因为在安装Apk的时候,提示包解析数据错误
//            android.os.Process.killProcess(android.os.Process.myPid());
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //用户同意了权限请求后的操作
                    Toast.makeText(MainActivity.this, "同意了权限", Toast.LENGTH_SHORT).show();
                    download();
                } else {
//                    CheckPermission.showMissingPermissionDialog(CreateVideoActivity.this, "相机");
                }
            }
            break;
        }

    }
}