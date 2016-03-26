package com.study.hang.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by hang on 16/3/26.
 */
public class ServiceUtil {
    private static  ActivityManager activityManager;
    public static boolean isServiceAlive(Context context,String serviceName) {
        activityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> list=activityManager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info:list) {
           if(info.service.getClassName().equals(serviceName)) {
               return true;
           }
        }
        return false;
    }
}
