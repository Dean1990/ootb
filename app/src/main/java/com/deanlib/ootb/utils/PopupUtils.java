package com.deanlib.ootb.utils;

import android.widget.Toast;

import com.deanlib.ootb.OotbConfig;

/**
 * 弹出框 dialog,toast,popwindow 等
 *
 * @author dean
 * @time 2018/6/28 下午2:57
 */
public class PopupUtils {

    public static void sendToast(int rid){
        sendToast(OotbConfig.mContext.getString(rid));
    }
    public static void sendToast(String msg){
        sendToast(msg,Toast.LENGTH_SHORT);
    }
    public static void sendToast(int rid,int duration){
        sendToast(OotbConfig.mContext.getString(rid),duration);
    }
    public static void sendToast(String msg,int duration){
        Toast.makeText(OotbConfig.mContext, msg, duration).show();
    }
}
