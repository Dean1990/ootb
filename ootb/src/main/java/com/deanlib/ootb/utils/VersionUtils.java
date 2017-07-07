package com.deanlib.ootb.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.deanlib.ootb.OotbConfig;

/**
 * 版本相关
 *
 * Created by dean on 2017/4/24.
 */

public class VersionUtils {

    /**
     * APP版本名（显示用）
     * @return
     */
    public static String getAppVersionName(){

        String versionName = "";

        try {
            PackageManager packageManager = OotbConfig.mContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(OotbConfig.mContext.getPackageName(), 0);
            versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * APP版本号（开发用）
     * @return
     */
    public static int getAppVersionCode(){
        int versionCode = 0;

        try {
            PackageManager packageManager = OotbConfig.mContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(OotbConfig.mContext.getPackageName(), 0);
            versionCode = packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;

    }

}
