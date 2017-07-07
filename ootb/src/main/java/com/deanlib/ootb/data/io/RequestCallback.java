package com.deanlib.ootb.data.io;

import org.xutils.common.Callback;

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
