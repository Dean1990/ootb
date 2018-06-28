package com.deanlib.ootb.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * 应用操作相关
 * 打开，分享等
 *
 * @author dean
 * @time 2018/6/28 下午3:29
 */
public class AppUtils {

    /**
     * 打开第三方浏览器
     * @param activity
     * @param url
     */
    public static void openThirdBrowser(Activity activity, String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }

}
