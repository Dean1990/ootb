package com.deanlib.ootb.data.io;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;

/**
 * 提供默认的LoadingDialog以方便使用
 *
 * Created by dean on 2017/7/8.
 */

public class DefaultLoadingDialog implements Request.ILoadingDialog {
    @Override
    public Dialog showLoadingDialog(Activity activity,String msg) {
        return ProgressDialog.show(activity, "", "加载中...");
    }

    @Override
    public void dismissLoadingDialog() {

    }
}
