package com.deanlib.ootb.data.io;

import android.app.Activity;
import android.app.Dialog;

/**
 * Created by dean on 2017/7/8.
 */

public interface ILoadingDialog {

    Dialog showLoadingDialog(Activity activity);
    void dismissLoadingDialog();
}
