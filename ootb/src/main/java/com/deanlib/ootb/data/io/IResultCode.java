package com.deanlib.ootb.data.io;

import java.util.HashMap;

/**
 * 返回结果代号的自定义类接口
 *
 *
 * Created by dean on 2017/6/15.
 */

public abstract class IResultCode {

    static String successCode;

    /**
     * key:code,value:msg
     *
     * msg的显示级别高于服务器信息，当msg不为空时，优先展示msg信息并屏蔽服务器的msg信息
     */
    static HashMap<String,String> resultCodeMap = new HashMap<>();

    public IResultCode(String successCode){

        this.successCode = successCode;

        resultCodeMap.put(successCode,"");
    }

    public IResultCode(String successCode, String successMsg){

        this.successCode = successCode;

        resultCodeMap.put(successCode,successMsg);
    }

    public IResultCode(String successCode, HashMap<String,String> resultCodeMap){

        this.successCode = successCode;

        resultCodeMap.put(successCode,resultCodeMap.get(successCode));

        this.resultCodeMap.putAll(resultCodeMap);
    }

}
