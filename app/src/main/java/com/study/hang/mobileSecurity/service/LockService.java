package com.study.hang.mobileSecurity.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by hang on 2016/4/7.
 */
public class LockService extends Service {
    private LockScreenReceiver receiver;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("================>服务开启");
        receiver=new LockScreenReceiver();
        registerReceiver(receiver,new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("============服务销毁=========");
        unregisterReceiver(receiver);
        receiver=null;
    }

    class LockScreenReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("=================锁屏了==================");
            ActivityManager manager= (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> list= manager.getRunningAppProcesses();
            for(ActivityManager.RunningAppProcessInfo info:list) {
                System.out.println("kill============>"+info.processName);
                manager.killBackgroundProcesses(info.processName);
            }
        }
    }
}
