package com.deanlib.ootb.data.io;

/**
 * 提供默认的ResultCode类以方便使用
 *
 * successCode = "200"
 *
 * Created by dean on 2017/6/15.
 */

public class DefaultResult extends Result {

    String code;
    String msg;

    public DefaultResult() {
        super(successCode);
        successCode = "200";
    }

    @Override
    String getResultCode() {
        return code;
    }

    @Override
    String getResultMsg() {
        return msg;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
