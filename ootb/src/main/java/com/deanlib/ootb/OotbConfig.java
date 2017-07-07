package com.deanlib.ootb;

import android.app.Application;
import android.content.Context;

import com.deanlib.ootb.data.io.ILoadingDialog;
import com.deanlib.ootb.data.io.IRequestParam;
import com.deanlib.ootb.data.io.IResultCode;
import com.deanlib.ootb.data.io.Request;
import com.deanlib.ootb.utils.DLogUtils;

import org.xutils.x;

/**
 * 该工程配置类
 *
 * 配置DEBUG,服务器地址等
 *
 * Created by dean on 2017/4/24.
 */

public class OotbConfig {


    private static boolean DEBUG = false;

    public static Context mContext;

    public static void init(Application context, boolean debug){

        DEBUG = debug;

        mContext = context.getApplicationContext();

        DLogUtils.getInstance();

        x.Ext.init(context);

        x.Ext.setDebug(debug);

    }

    public static boolean isDEBUG(){

        return DEBUG;
    }

    public static String getRequestServer() {
        return Request.SERVER;
    }

//    public static void setRequestServer(String requestServer) {
//        RequestServer = requestServer;
//    }

//    public static RequestParams getExtenisonParams() {
//        return extenisonParams;
//    }
//
//    public static void setExtenisonParams(RequestParams extenisonParams) {
//        UtilsConfig.extenisonParams = extenisonParams;
//    }

    /**
     * 使用Requst类时，必须先对其设置
     * @param requestServer
     * @param param
     * @param code
     */
    public static void setRequestServer(String requestServer, IRequestParam param, IResultCode code, ILoadingDialog dialog){

        Request.SERVER = requestServer;

        Request.iRequestParam = param;

        Request.iResultCode = code;

        Request.iLoadingDialog = dialog;

    }
}
