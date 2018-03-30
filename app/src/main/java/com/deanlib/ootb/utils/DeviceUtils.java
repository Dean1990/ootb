package com.deanlib.ootb.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StatFs;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.deanlib.ootb.R;
import com.deanlib.ootb.OotbConfig;
import com.deanlib.ootb.manager.NetworkManager;

import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * 设备相关
 *
 * Created by dean on 2017/4/24.
 */

public class DeviceUtils {

    /**
     * 屏幕宽度
     * @return
     */
    public static int getSreenWidth(){

        WindowManager wm = (WindowManager) OotbConfig.mContext.getSystemService(Context.WINDOW_SERVICE);

        Display defaultDisplay = wm.getDefaultDisplay();

        return defaultDisplay.getWidth();

    }

    /**
     * 屏幕高度
     * @return
     */
    public static int getSreenHight(){

        WindowManager wm = (WindowManager) OotbConfig.mContext.getSystemService(Context.WINDOW_SERVICE);

        Display defaultDisplay = wm.getDefaultDisplay();

        return defaultDisplay.getHeight();

    }

    /**
     * 打开或关闭软键盘
     */
    public static void toggleKeyboard(){

        InputMethodManager imm = (InputMethodManager) OotbConfig.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    /**
     * 隐藏软键盘
     * @param act
     */
    public static void hideKeyboard(Activity act){

        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromInputMethod(act.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


    }

    /**
     * 弹出软键盘
     * @param view
     */
    public static void showKeyboard(View view){

        InputMethodManager imm = (InputMethodManager) OotbConfig.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);

    }


    /**
     * 模拟发送按键事件
     * @param KeyCode
     */
    public static void sendKeyEvent(final int KeyCode) {
        new Thread() { // 不可在主线程中调用
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }


    /**
     * 设置添加屏幕的背景透明度
     * @param act
     * @param bgAlpha 0.0-1.0
     */
    public static void backgroundAlpha(Activity act, float bgAlpha)
    {
        WindowManager.LayoutParams lp = act.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        act.getWindow().setAttributes(lp);
    }

    /**
     * 获取本地MAC地址
     *
     * android.permission.ACCESS_WIFI_STATE
     * @return
     */
    public static String getLocalMacAddress() {
        WifiManager wifi = (WifiManager) OotbConfig.mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * 网络是否可用
     *
     * android.permission.ACCESS_NETWORK_STATE
     * @return
     */
    public static boolean isNetworkConnected() {
        return NetworkManager.isNetworkConn(OotbConfig.mContext);
    }



    /**
     * 获取当前网络类型
     *
     * android.permission.ACCESS_NETWORK_STATE
     * @return 0：没有网络 1：WIFI网络   2：WAP网络    3：NET网络
     */
    public static int getNetworkType() {
        return NetworkManager.getAPNType(OotbConfig.mContext);
    }

    /**
     * 是否是WIFI网络
     * @return
     */
    public static boolean isWifi(){

        return NetworkManager.isWifiConn(OotbConfig.mContext);
    }

    /**
     * 获取基本的设备及应用信息
     * @return
     */
    public static String getHandSetInfo(){
        String handSetInfo= String.format(OotbConfig.mContext.getString(R.string.tag_handsetinfo)
                ,android.os.Build.MODEL, Build.VERSION.SDK_INT,android.os.Build.VERSION.RELEASE
                , VersionUtils.getAppVersionName(), VersionUtils.getAppVersionCode());
        return handSetInfo;
    }

    /**
     * 创建快捷方式
     * 需要声明权限 com.android.launcher.permission.INSTALL_SHORTCUT
     * @param act   上下文
     * @param iconResId     图标
     * @param appnameResId      名称
     */
    public static void createShortCut(Activity act, int iconResId,
                                      int appnameResId) {
        createShortCut(act,iconResId,act.getString(appnameResId));

    }

    /**
     * 创建快捷方式
     * 需要声明权限 com.android.launcher.permission.INSTALL_SHORTCUT
     * @param act   上下文
     * @param iconResId     图标
     * @param appname       名称
     */
    public static void createShortCut(Activity act, int iconResId,
                                      String appname) {

        // com.android.launcher.permission.INSTALL_SHORTCUT

        Intent shortcutintent = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重复创建
        shortcutintent.putExtra("duplicate", false);
        // 需要现实的名称
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                appname);
        // 快捷图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(
                act.getApplicationContext(), iconResId);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        // 点击快捷图片，运行的程序主入口
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                new Intent(act.getApplicationContext(), act.getClass()));
        // 发送广播
        act.sendBroadcast(shortcutintent);
    }


    /**
     * 获得SD卡总大小
     *
     * @return
     */
    private long getSDTotalSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return  blockSize * totalBlocks;
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    private long getSDAvailableSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }

    /**
     * 获得机身内存总大小
     *
     * @return
     */
    private long getRomTotalSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return blockSize * totalBlocks;
    }

    /**
     * 获得机身可用内存
     *
     * @return
     */
    private long getRomAvailableSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }

    /**
     * 判断当前的进行或者服务是否存在（是否运行）
     * @param className
     * @return
     */
    public static boolean isServiceRunning(String className) {
        ActivityManager activityManager = (ActivityManager) OotbConfig.mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
        //30 就是一个数字 可以用常量Integer.MAX_VALUE 代替

        if (!(serviceList.size() > 0)) {
            return false;
        }
        boolean isRunning = false;
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

}
