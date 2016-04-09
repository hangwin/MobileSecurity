package com.study.hang.mobileSecurity.service;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.study.hang.db.LockAppDao;
import com.study.hang.mobileSecurity.activity.LockActivity;
import com.study.hang.mobileSecurity.activity.LockAppActivity;

import java.util.List;

/**
 * Created by hang on 16/4/7.
 */
public class watchdogService extends Service {
    private boolean tag;
    private myReceiver receiver;
    private ScreenOff off;
    private String resultPackage;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private String getRunningApp() {
        UsageStatsManager usageStatsManager= (UsageStatsManager) this.getSystemService(USAGE_STATS_SERVICE);
        long ts = System.currentTimeMillis();
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts - 1000*3600, ts);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return null;
        }

        UsageStats recentStats = null;
        for (UsageStats usageStats : queryUsageStats) {
            if (recentStats == null || recentStats.getLastTimeUsed() < usageStats.getLastTimeUsed()) {
                recentStats = usageStats;
            }
        }
        return recentStats.getPackageName();

    }

    @Override
    public void onCreate() {
        super.onCreate();
        off=new ScreenOff();
        registerReceiver(off, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        receiver=new myReceiver();
        registerReceiver(receiver,new IntentFilter("com.hang.stopWatchdog"));
        final LockAppDao dao=new LockAppDao(this);
        System.out.println("============开启看门狗服务");
        final ActivityManager manager= (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        tag=true;
        new Thread(new Runnable() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                while (tag) {
                 /*   List<ActivityManager.RunningTaskInfo> list=manager.getRunningTasks(100);
                    ActivityManager.RunningTaskInfo task=list.get(0); */
                    String name=getRunningApp();
                    System.out.println("当前应用名------->"+name);
                    if(!TextUtils.isEmpty(name)) {
                        if (dao.find(name)) {
                            if(name.equals(resultPackage)) {

                            }else {
                                System.out.println("+++++++++" + name + "+++++++++");
                                Intent intent = new Intent(getApplicationContext(), LockAppActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("curPackageName", name);
                                startActivity(intent);

                            }
                        }
                    }
                    try {
                        Thread.sleep(60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tag=false;
        unregisterReceiver(receiver);
        receiver=null;
        unregisterReceiver(off);
        off=null;

    }

    private class myReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(!TextUtils.isEmpty(intent.getStringExtra("resultApp"))) {
                System.out.println("收到广播");
                resultPackage=intent.getStringExtra("resultApp");
            }
        }
    }

    private class ScreenOff extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            resultPackage=null;
        }
    }
}
