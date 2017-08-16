package com.deanlib.ootb.data.io;

import java.util.HashMap;

/**
 * 返回结果代号的自定义类接口
 *
 *
 * Created by dean on 2017/6/15.
 */

public abstract class ResultCode {

    static String successCode;

    /**
     * key:code,value:msg
     *
     * msg的显示级别高于服务器信息，当msg不为空时，优先展示msg信息并屏蔽服务器的msg信息
     */
    static HashMap<String,String> resultCodeMap = new HashMap<>();

    public ResultCode(String successCode){

        this.successCode = successCode;

        resultCodeMap.put(successCode,"");
    }

    public ResultCode(String successCode, String successMsg){

        this.successCode = successCode;

        resultCodeMap.put(successCode,successMsg);
    }

    public ResultCode(String successCode, HashMap<String,String> resultCodeMap){

        this.successCode = successCode;

        resultCodeMap.put(successCode,resultCodeMap.get(successCode));

        this.resultCodeMap.putAll(resultCodeMap);
    }

    /**
     * 请求结果分析
     *
     * @param code
     * @return 返回true，表示分析由用户自己完成，返回false，表示分析由用户和request一起完成
     */
    public boolean onResultParse(String code){
        return false;
    }

}
