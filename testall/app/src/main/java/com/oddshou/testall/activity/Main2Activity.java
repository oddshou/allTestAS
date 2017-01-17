package com.oddshou.testall.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.oddshou.testall.Constants.Constants;
import com.oddshou.testall.R;
import com.oddshou.testall.Utils.RootUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Main2Activity extends Activity {

    private Button btnMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//        findViewById(R.id.btnGoHome).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Main2Activity.this, HomePageActivity.class);
//                startActivity(intent);
//            }
//        });

        findViewById(R.id.btnGetRoot).setOnClickListener(myListener);
        findViewById(R.id.btnInstall).setOnClickListener(myListener);
        findViewById(R.id.btnInstallNormal).setOnClickListener(myListener);
        btnMove = (Button)findViewById(R.id.btnMove);
        btnMove.setOnClickListener(myListener);
    }

    private View.OnClickListener myListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btnGetRoot:
                    new GetRootPermissionThread().start();
                    break;
                case R.id.btnInstall:
                    break;
                case R.id.btnInstallNormal:
                    new InstallApkThread().start();
                    break;
                case R.id.btnMove:
                    String pkgName = "com.zl.hw";
                    new moveTask().execute(pkgName);
                    break;
                default:
                    break;
            }
        }
    };

    public static void CopyAPKTOSD(Context context) {
        try {
            File file = new File(
                    android.os.Environment.getExternalStorageDirectory(),
                    Constants.APK_NAME);
            System.out.println("filePath: "+file.getPath());
            if(!file.exists()){
                file.createNewFile();
            }
//			InputStream in = context.getAssets().open(Constants.APK_NAME);
            InputStream in = context.getResources().openRawResource(R.raw.hw);
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println("copy "+ Constants.APK_NAME + " error !");
            e.printStackTrace();
        }
    }

    public void installApk(File file) {
        // TODO Auto-generated method stub
        System.out.println("installApk com.zl.hw");
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    class InstallApkThread extends Thread{
        public void run(){
            CopyAPKTOSD(Main2Activity.this);
            File apkFile = new File(
                    android.os.Environment.getExternalStorageDirectory(),
                    Constants.APK_NAME);
            if (apkFile.exists()) {
                installApk(apkFile);
            }else{
                System.out.println(android.os.Environment.getExternalStorageDirectory()+File.separator+
                        Constants.APK_NAME + " no exist! ");
            }
        }
    }
    class GetRootPermissionThread extends Thread {
        public void run() {
            RootUtil.preparezlsu(Main2Activity.this);
        }
    }

    /**
     * 开启线程去执行move操作
     *
     * @author liky
     *
     */
    class moveTask extends AsyncTask<String, Object, Integer> {
        private String pkgName = "";

        @Override
        public Integer doInBackground(String... params) {
            pkgName = params[0];
            return movepkg(params[0]);
        }

        @Override
        public void onPostExecute(Integer result) {
            String installLocation = appInstalledLocation(pkgName);
            PackageInfo info;
            String appName = "com.zl.hw";
            try {
                info = getPackageManager().getPackageInfo(pkgName, 0);
                appName = (String) getPackageManager().getApplicationLabel(
                        info.applicationInfo);
                //PackageManager.MOVE_SUCCEEDED == -100
                if (result == -100) {
                    String oldLocation = installLocation.equals(IN_SDCARD) ? IN_MOBILE_MEMORY
                            : IN_SDCARD;
                    Toast.makeText(
                            Main2Activity.this,
                            "move " + appName + " from " + oldLocation + " to "
                                    + installLocation + " sucessed",
                            Toast.LENGTH_LONG).show();
                    btnMove.setText("move " + appName + " to " + oldLocation);
                } else {
                    String hopeLocation = installLocation.equals(IN_SDCARD) ? IN_MOBILE_MEMORY
                            : IN_SDCARD;
                    Toast.makeText(
                            Main2Activity.this,
                            "move " + appName + " from " + installLocation
                                    + " to " + hopeLocation + " failed",
                            Toast.LENGTH_LONG).show();
                    btnMove.setText("move " + appName + " to " + hopeLocation);
                }
            } catch (PackageManager.NameNotFoundException e) {
                btnMove.setText(appName + " not existed ");
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }

    }

    /**
     * 移动包名为pkgName的应用程序，如果， 1、在sdcard上则移动到手机内存； 2、在手机内存则移动到sdcard上 PS:系统应用不能移动
     *
     * @param pkgName
     *            包名
     * @return 同 {@link PackageManager #movePackage} 返回值一样
     *         {@link PackageManager #MOVE_SUCCEEDED}
     *
     */
    private int movepkg(String pkgName) {
        int result = -100;
        PackageInfo info;
        String minePkgName = "";// 本程序包名
        try {
            minePkgName = getPackageName();
            info = getPackageManager().getPackageInfo(pkgName, 0);

            //PackageManager.MOVE_INTERNAL == 0x00000001
            //PackageManager.MOVE_EXTERNAL_MEDIA == 0x00000002
            int moveFlag = (info.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0 ? 0x00000002
                    : 0x00000001;
            // 当前应用程序 Movepkgdemo 的 apk文件 的存放路径
            String classpath = getPackageManager().getPackageInfo(minePkgName,
                    0).applicationInfo.publicSourceDir;
            System.out.println("classPath: " + classpath);
            String cmd = "movedemo -c 'export CLASSPATH="
                    + classpath
                    + " && export LD_LIBRARY_PATH=/vendor/lib:/system/lib && exec app_process /data/app "
                    + minePkgName + "/MoveUtil " // /system/bin
                    + pkgName + " " + moveFlag + "'";
            String[] progArray = new String[] {
                    Constants.ROOT_SU,
                    "-c",
                    "export CLASSPATH="
                            + classpath
                            + " && export LD_LIBRARY_PATH=/vendor/lib:/system/lib && exec app_process /data/app "
                            + minePkgName + "/util/MoveUtil " // /system/bin
                            + pkgName + " " + moveFlag };
            String returnCode = "";
            returnCode = RootUtil.exeCmd(progArray,
                    Constants.RETURNCODE_MOVE_PACKAGE);
            try {
                System.out.println("result: " + returnCode);
                result = Integer.parseInt(returnCode);
            } catch (NumberFormatException e) {
                return result;
            }

        } catch (PackageManager.NameNotFoundException e) {
            System.out.println("Main2Activity: movepkg : " + pkgName
                    + " not found");
            e.printStackTrace();
        }
        return result;

    }

    public static final String IN_SDCARD = "sdcard"; // 安装在sdcard中
    public static final String IN_MOBILE_MEMORY = "mobile_memory";// 安装在手机内存中
    public static final String NOT_FOUND_PACKAGE = "not_found_package";// 未安装

    /**
     * 应用程序安装在哪里
     *
     * @param pkgName
     * @return <ui> <li>{@link #IN_SDCARD} <li>{@link #IN_MOBILE_MEMORY} <li>
     *         {@link #NOT_FOUND_PACKAGE} </ui>
     */
    private String appInstalledLocation(String pkgName) {
        PackageInfo info;
        String moveFlag = NOT_FOUND_PACKAGE;
        try {
            info = getPackageManager().getPackageInfo(pkgName, 0);
            moveFlag = (info.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0 ? IN_MOBILE_MEMORY
                    : IN_SDCARD;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return moveFlag;
    }

}
