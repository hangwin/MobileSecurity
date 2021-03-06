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

    public static  Boolean getBoolean(Context context,String key,boolean defVal) {
        SharedPreferences sp=context.getSharedPreferences(pathName,Context.MODE_MULTI_PROCESS);
        Boolean result=sp.getBoolean(key,defVal);
        return result;
    }
    public static void setBoolean(Context context,String key,Boolean val) {
        SharedPreferences sp=context.getSharedPreferences(pathName,Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean(key,val);
        editor.commit();
    }
    public static boolean setString(Context context,String key,String val) {
        SharedPreferences sp=context.getSharedPreferences(pathName,Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(key, val);
        boolean res=editor.commit();
        return res;
    }
    public static String getString(Context context,String key) {
        SharedPreferences sp=context.getSharedPreferences(pathName, Context.MODE_MULTI_PROCESS);
        String str=sp.getString(key, null);
        return str;
    }
    public static int getInt(Context context,String key) {
        SharedPreferences sp=context.getSharedPreferences(pathName, Context.MODE_MULTI_PROCESS);
        int str=sp.getInt(key, 0);
        return str;
    }
    public static void setInt(Context context,String key,int value) {
        SharedPreferences sp=context.getSharedPreferences(pathName,Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    public static float getFloat(Context context,String key) {
        SharedPreferences sp=context.getSharedPreferences(pathName, Context.MODE_MULTI_PROCESS);
        float str=sp.getFloat(key, 0);
        return str;
    }
    public static void setFloat(Context context,String key,float value) {
        SharedPreferences sp=context.getSharedPreferences(pathName,Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor=sp.edit();
        editor.putFloat(key, value);
        editor.commit();
    }
}
