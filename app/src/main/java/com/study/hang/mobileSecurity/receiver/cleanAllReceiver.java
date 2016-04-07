package com.study.hang.mobileSecurity.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * Created by hang on 16/4/7.
 */
public class cleanAllReceiver extends BroadcastReceiver {
    private ActivityManager manager;
    @Override
    public void onReceive(Context context, Intent intent) {
        manager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        System.out.println("************收到清理广播***********");
        List<ActivityManager.RunningAppProcessInfo> list=manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info:list) {
            manager.killBackgroundProcesses(info.processName);
        }
    }
}
