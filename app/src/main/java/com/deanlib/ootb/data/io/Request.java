package com.deanlib.ootb.data.io;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.deanlib.ootb.OotbConfig;
import com.deanlib.ootb.utils.DLogUtils;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.common.util.MD5;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * 网络请求的基类
 * <p>
 * Created by dean on 2017/4/19.
 */
public abstract class Request {

    public static int requestCount;

    /**
     * 服务器地址
     */
    public static String SERVER = "";
    //假数据
    public static boolean FALSEDATA = false;
    //假数据延时
    public static long DELAYED = 0;

    public static final long EXPIRE_SECOND = 1000;
    public static final long EXPIRE_SECOND_10 = 1000 * 10;
    public static final long EXPIRE_MINUTE = 1000 * 60;
    public static final long EXPIRE_MINUTE_10 = 1000 * 60 * 10;
    public static final long EXPIRE_HOUR = 1000 * 60 * 60;
    public static final long EXPIRE_DAY = 1000 * 60 * 60 * 24;
    public static final long EXPIRE_WEEK = 1000 * 60 * 60 * 24 * 7;
    public static final long EXPIRE_MONTH = 1000 * 60 * 60 * 24 * 30;
    public static final long EXPIRE_YEAR = 1000 * 60 * 60 * 24 * 365;

    /**
     * 用于标识服务器内部规定的错误类型（区分）
     */
    public static final String THROWABLE_LABEL = "Request Inner Throwable";

    public Context context;

//	/** Https 证书验证对象 */
//	private static SSLContext s_sSLContext = null;

    static Dialog mDialog;

    public Request(Context context) {

        this.context = context;

    }

    public abstract String getName();

    public abstract RequestParams params();

    public abstract <T> T parse(String json);

    // 是否使用缓存
    private boolean isCache;

    /**
     * 设置是否使用缓存
     *
     * @param isCache
     * @return
     */
    public Request setCache(boolean isCache) {

        this.isCache = isCache;

        return this;

    }

    /**
     * 是否使用缓存
     *
     * @return
     */
    public boolean isCache() {

        return isCache;
    }

    // 是否展示服务器msg
    private boolean isShowServerMsg = true;

    /**
     * 设置是否显示服务器返回的信息
     *
     * @param isShowServerMsg
     * @return
     */
    public Request setShowServerMsg(boolean isShowServerMsg) {

        this.isShowServerMsg = isShowServerMsg;

        return this;
    }

    /**
     * 是否显示服务器返回的信息
     *
     * @return
     */
    public boolean isShowServerMsg() {

        return isShowServerMsg;
    }

    RequestCallback mCallback;

    /**
     * 强制刷新
     */
    public void forceRefresh() {

        if (mCallback == null) {
            // throw new NullPointerException("Request mCallback is null");
            DLogUtils.e("Request mCallback is null");

            return;
        }

        isCache = false;

        execute(mCallback);

    }

    /**
     * 设置加载框
     *
     * @param iLoadingDialog
     * @return
     */
    public Request setLoadingDialog(ILoadingDialog iLoadingDialog) {

        this.iLoadingDialog = iLoadingDialog;

        return this;

    }

    //默认加签名
//    static boolean needSign = true;

    /**
     * 是否加了签名
     * @return
     */
//    public boolean isNeedSign(){
//
//        return needSign;
//    }

    /**
     * 设置是否加签名
     *
     * @param needSign
     * @return
     */
//    public Request setNeedSign(boolean needSign){
//
//        this.needSign = needSign;
//
//        return this;
//
//    }

    public static IRequestParam iRequestParam;
    public static Result resultMode;
    public static ILoadingDialog iLoadingDialog;


    /**
     * 执行网络请求方法
     * 加载框默认打开
     *
     * @param callback 回调函数
     * @return 返回该请求的句柄，可以用来控制其取消执行操作
     */
    public <T> Callback.Cancelable execute(RequestCallback callback) {

        return execute(true, callback);
    }

    /**
     * 执行网络请求方法
     *
     * @param showDialog 是否显示加载框
     * @param callback   回调函数
     * @return 返回该请求的句柄，可以用来控制其取消执行操作
     */
    public <T> Callback.Cancelable execute(boolean showDialog, final RequestCallback callback) {

        if (showDialog)
            showLoadingDialog();

        requestCount++;

        if (true) {
            for (int i = 0; params().getHeaders() != null && i < params().getHeaders().size(); i++) {
                DLogUtils.d("Header >>> " + params().getHeaders().get(i).key + " : " + params().getHeaders().get(i).getValueStr());
            }
            for (KeyValue kv : params().getQueryStringParams()) {
                DLogUtils.d("QueryStringParam >>> " + kv.key + " : " + kv.getValueStr());
            }
            for (KeyValue kv : params().getBodyParams()) {
                DLogUtils.d("BodyParam >>> " + kv.key + " : " + kv.getValueStr());
            }

            DLogUtils.d("BodyContent >>> " + params().getBodyContent());
        }

        RequestParams params = iRequestParam != null ? iRequestParam.disposeParam(params()) : params();


        Callback.Cancelable cancelable = null;

        if (!FALSEDATA) {

            cancelable = x.http().post(params, new Callback.CacheCallback<String>() {

                @Override
                public void onSuccess(String result) {

                    if (result != null) {

                        DLogUtils.d(getName() + ": 网络请求任务成功");

                        DLogUtils.d(getName() + ": " + result);

                        new ParseTask<T>(result, callback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                    DLogUtils.d(getName() + ": 网络请求任务失败");

                    ex.printStackTrace();

                    callback.onError(ex, isOnCallback);

                }

                @Override
                public void onCancelled(CancelledException cex) {

                    DLogUtils.d(getName() + ": 网络请求任务被取消");

                    callback.onCancelled(cex);

                }

                @Override
                public void onFinished() {

                    DLogUtils.d(getName() + ": 网络请求任务完成");

                    callback.onFinished();

                    requestCount--;

                    dismissLoadingDialog();
                }

                @Override
                public boolean onCache(String result) {

                    if (isCache) {

                        DLogUtils.d(getName() + ": 使用缓存数据");

                        new ParseTask<T>(result, callback).execute();
                    }

                    return isCache;
                }
            });

        } else {
            DLogUtils.d(getName() + ": 假数据测试,延时：" + DELAYED + "毫秒");

            x.task().postDelayed(new Runnable() {
                @Override
                public void run() {

                    callback.onSuccess(parse("{}"));

                    callback.onFinished();
                    requestCount--;
                    dismissLoadingDialog();

                }
            }, DELAYED);


        }

        return cancelable;
    }


    /**
     * 签名方法
     *
     * @param params
     * @return 得到一个MD5
     */
    private static String getSign(List<KeyValue> params) {
        TreeMap<String, String> map = new TreeMap<String, String>();
        StringBuffer str = new StringBuffer();
        String key;

        for (KeyValue kv : params) {
            map.put(kv.key, (String) kv.value);
        }
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            key = (String) it.next();
            str.append(key + "=" + map.get(key));
            str.append("&");
        }
        //str.append("secretKey=" + "");
        return MD5.md5(str.toString());
    }

//    public static class Result {
//
//        public String code;
//
//        public String msg;
//
//    }
//
//    Handler mHandler = new Handler(){
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            Result result = (Result) msg.obj;
//
//            Toast.makeText(getContext(),result.msg,Toast.LENGTH_SHORT).show();
//
//        }
//    };

    /**
     * 解析JSON任务
     */
    class ParseTask<T> extends AsyncTask<Void, Void, Result> {

        String json;

        RequestCallback<T> callback;

        T t;

        public ParseTask(String json, RequestCallback<T> callback) {

            this.json = json;

            this.callback = callback;
        }

        @Override
        protected Result doInBackground(Void... params) {
            if (resultMode != null) {

                try {

                    Result mResult;

                    if (callback == null) {

                        throw new NullPointerException("RequestCallback is null");

                    } else {

                        mResult = JSON.parseObject(json, resultMode.getClass());

                        if (mResult != null)
                            if (!resultMode.onResultParse(mResult.getResultCode())) {

                                if (resultMode.successCode.equals(mResult.getResultCode())) {

                                    t = parse(json);

                                }
                            }
                    }

                    return mResult;

                } catch (Exception e) {

                    e.printStackTrace();

                    return null;
                }

            } else {

                t = parse(json);

                return null;
            }
        }

        @Override
        protected void onPostExecute(Result result) {

            super.onPostExecute(result);

            if (resultMode == null) {

                callback.onSuccess(t);

            } else if (result == null) {

                callback.onError(new Throwable(THROWABLE_LABEL + ":解析结果为空"), false);

                if (isShowServerMsg)
                    Toast.makeText(getContext(), "解析结果为空", Toast.LENGTH_SHORT).show();


            } else {

                DLogUtils.d(getName() + "  code:" + result.getResultCode() + "  msg:" + result.getResultMsg());

                if (resultMode.successCode.equals(result.getResultCode())) {

                    callback.onSuccess(t);

                    String msg = resultMode.resultCodeMap.get(result.getResultCode());

                    if (!TextUtils.isEmpty(msg)) {

                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

                    }

                } else {

                    callback.onError(new Throwable(THROWABLE_LABEL + ":" + result.getResultCode() + "-" + result.getResultMsg()), false);

                    String msg = resultMode.resultCodeMap.get(result.getResultCode());

                    if (!TextUtils.isEmpty(msg)) {

                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

                    } else if (isShowServerMsg)
                        Toast.makeText(getContext(), result.getResultMsg(), Toast.LENGTH_SHORT).show();

                }
            }

//            callback.onFinished();
//
//            dismissLoadingDialog();

        }

    }

    private Context getContext() {

        if (context == null)
            context = OotbConfig.mContext;

        return context;
    }

    private void showLoadingDialog() {

        if (iLoadingDialog != null && requestCount == 0 && getContext() instanceof Activity && !((Activity) getContext()).isFinishing()) {
            //mDialog = ProgressDialog.show(getContext(), "", "加载中...");
            mDialog = iLoadingDialog.showLoadingDialog((Activity) getContext());
        }

    }

    private void dismissLoadingDialog() {

        if (iLoadingDialog != null && requestCount == 0 && mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            iLoadingDialog.dismissLoadingDialog();
        }
    }

    /**
     * 网络请求的回调函数
     * <p>
     * Created by dean on 2017/4/19.
     */
    public interface RequestCallback<T> {

        /**
         * 请求成功
         *
         * @param t 指定对象类型
         */
        void onSuccess(T t);

        /**
         * 请求失败
         *
         * @param ex           失败的异常信息
         * @param isOnCallback 是否调用了事前设置的失败时的回调，请忽略这个参数
         */
        void onError(Throwable ex, boolean isOnCallback);

        /**
         * 请求取消
         *
         * @param cex 取消的异常信息
         */
        void onCancelled(Callback.CancelledException cex);

        /**
         * 请求完成
         * 不管请求结果如何，onFinished 总是会被调用
         */
        void onFinished();

    }

    /**
     * 定义全局请求参数
     * <p>
     * Created by dean on 2017/5/22.
     */
    public interface IRequestParam {

        RequestParams disposeParam(RequestParams params);

    }

    /**
     * 自定义加载框
     * <p>
     * Created by dean on 2017/7/8.
     */
    public interface ILoadingDialog {

        /**
         * 需要显示加载框时被调用
         * 需要生成一个加载框，并返回给这个方法
         *
         * @param activity
         * @return
         */
        Dialog showLoadingDialog(Activity activity);

        /**
         * 加载框关闭时被调用
         */
        void dismissLoadingDialog();
    }

    /**
     * 返回结果参数的自定义类接口
     * <p>
     * <p>
     * Created by dean on 2017/6/15.
     */

    public static abstract class Result {

        protected String successCode;

        /**
         * key:code,value:msg
         * <p>
         * msg的显示级别高于服务器信息，当msg不为空时，优先展示msg信息并屏蔽服务器的msg信息
         */
        protected HashMap<String, String> resultCodeMap = new HashMap<>();

        public Result(String successCode) {

            this.successCode = successCode;

            resultCodeMap.put(successCode, "");
        }

        public Result(String successCode, String successMsg) {

            this.successCode = successCode;

            resultCodeMap.put(successCode, successMsg);
        }

        public Result(String successCode, HashMap<String, String> resultCodeMap) {

            this.successCode = successCode;

            resultCodeMap.put(successCode, resultCodeMap.get(successCode));

            this.resultCodeMap.putAll(resultCodeMap);
        }

        /**
         * 请求结果 自行分析处理
         *
         * @param code
         * @return 返回true，表示分析由用户自己完成，返回false，表示分析由用户和request一起完成
         */
        public boolean onResultParse(String code) {
            return false;
        }

        /**
         * 指定ResultCode的值
         * 数据请求的状态码
         *
         * @return
         */
        public abstract String getResultCode();

        /**
         * 指定ResultMsg的值
         * 数据请求的服务器信息
         *
         * @return
         */
        public abstract String getResultMsg();
//    public abstract T getResultData();


    }
}
