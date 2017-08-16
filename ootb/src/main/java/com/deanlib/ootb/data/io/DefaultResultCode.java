package com.deanlib.ootb.data.io;

/**
 * 提供默认的ResultCode类以方便使用
 *
 * successCode = "200"
 *
 * Created by dean on 2017/6/15.
 */

public class DefaultResultCode extends ResultCode {

    public DefaultResultCode() {
        super(successCode);
        successCode = "200";
    }
}
