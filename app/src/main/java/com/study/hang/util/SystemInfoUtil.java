package com.study.hang.util;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.text.format.Formatter;

import java.util.List;

/**
 * Created by hang on 16/4/5.
 */
public class SystemInfoUtil {
    private static ActivityManager manager;
    /*
     *得到运行的进程数量
     */
    public static int getRunningProcessnum(Context context) {
        ActivityManager manager=getManager(context);
        List<ActivityManager.RunningAppProcessInfo> list=manager.getRunningAppProcesses();
        return list.size();
    }

    public static String getAvailableMemory(Context context) {
        ActivityManager manager=getManager(context);
        ActivityManager.MemoryInfo memoryInfo=new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(memoryInfo);
        return Formatter.formatFileSize(context, memoryInfo.availMem);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static String getTotalMemory(Context context) {
        ActivityManager manager=getManager(context);
        ActivityManager.MemoryInfo memoryInfo=new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(memoryInfo);
        return Formatter.formatFileSize(context,memoryInfo.totalMem);
    }


    public static ActivityManager getManager(Context context) {
        if(manager==null)
        manager=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return manager;
    }
}
