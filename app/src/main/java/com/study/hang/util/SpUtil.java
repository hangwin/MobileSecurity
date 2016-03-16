package com.study.hang.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences工具类
 * Created by hang on 2016/3/14.
 */
public class SpUtil {
    public static final String pathName="MyData";

    /**
     * 验证是否第一次启动app
     * @return
     */
    public static  Boolean getBoolean(Context context,String key) {
        SharedPreferences sp=context.getSharedPreferences(pathName,Context.MODE_PRIVATE);
        Boolean result=sp.getBoolean(key,false);
        return result;
    }
    public static void setBoolean(Context context,String key,Boolean val) {
        SharedPreferences sp=context.getSharedPreferences(pathName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean(key,val);
        editor.commit();
    }
}
