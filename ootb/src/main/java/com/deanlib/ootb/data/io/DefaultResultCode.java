package com.deanlib.ootb.data.io;

/**
 * 默认的ResultCode类
 *
 * successCode = "200"
 *
 * Created by dean on 2017/6/15.
 */

public class DefaultResultCode extends IResultCode {

    public DefaultResultCode() {
        super(successCode);
        successCode = "200";
    }
}
