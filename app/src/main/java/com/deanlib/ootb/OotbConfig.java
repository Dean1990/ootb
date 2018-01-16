package com.deanlib.ootb;

import android.app.Application;
import android.content.Context;

import com.deanlib.ootb.data.io.ILoadingDialog;
import com.deanlib.ootb.data.io.IRequestParam;
import com.deanlib.ootb.data.io.Result;
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
     * @param resultMode
     */
    public static void setRequestServer(String requestServer, IRequestParam param, Result resultMode, ILoadingDialog dialog){

        Request.SERVER = requestServer;

        Request.iRequestParam = param;

        Request.resultMode = resultMode;

        Request.iLoadingDialog = dialog;

    }


    /**
     * @see OotbConfig#setRequestFalseData(boolean, long)
     * @param falseData
     */
    public static void setRequestFalseData(boolean falseData){

        setRequestFalseData(falseData,0);

    }

    /**
     * 设置网络请求的假数据开关
     * 在实现request的parse方法中设置假数据，默认json = {}
     * 做为测试时使用，开启后，不会请求网络而直接调用parse方法，并回调RequestCallback的onSuccess和onFinish方法。
     *
     * @param falseData
     * @param delayed   延迟时间毫秒
     */
    public static void setRequestFalseData(boolean falseData,long delayed){

        Request.FALSEDATA = falseData;
        Request.DELAYED = delayed<0?0:delayed;

    }
}
