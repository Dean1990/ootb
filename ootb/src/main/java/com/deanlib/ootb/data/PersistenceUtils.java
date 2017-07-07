package com.deanlib.ootb.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.deanlib.ootb.utils.UtilsConfig;

/**
 * SharedPreferences再包装
 * <p>
 * 存放在名称为 AppData 的文件中，访问模式 MODE_PRIVATE
 * <p>
 * Created by dean on 16/7/11.
 */
public class PersistenceUtils {

    static SharedPreferences preferences;

    static SharedPreferences.Editor editor;

    public PersistenceUtils() {

        if (preferences == null) {

            preferences = UtilsConfig.mContext.getSharedPreferences("AppData", Context.MODE_PRIVATE);

            editor = preferences.edit();
            ;
        }

    }

    /**
     * 缓存
     *
     * @param key
     * @param str
     */
    public void setCache(String key, String str) {

        editor.putString(key, str);

        editor.commit();

    }

    /**
     * 缓存
     *
     * @param key
     * @param obj 对象类型会被换成json格式保存
     */
    public void setCache(String key, Object obj) {

        setCache(key, JSON.toJSONString(obj, false));

    }

    /**
     * 取缓存
     *
     * @param key
     * @return
     */
    public String getCache(String key) {

        return preferences.getString(key, "");
    }

    /**
     * 清空
     */
    public void clear(){

        editor.clear();

        editor.commit();

    }

    /**
     * 移除
     *
     * @param key
     */
    public void remove(String key){

        editor.remove(key);

        editor.commit();

    }

}
