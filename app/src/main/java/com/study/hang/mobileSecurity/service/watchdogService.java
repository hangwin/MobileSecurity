package com.study.hang.mobileSecurity.service;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
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
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private String getRunningApp() {
        UsageStatsManager usageStatsManager= (UsageStatsManager) this.getSystemService(USAGE_STATS_SERVICE);
        long ts = System.currentTimeMillis();
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts - 2000, ts);
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
                            System.out.println("+++++++++" + name + "+++++++++");
                            Intent intent=new Intent(getApplicationContext(),LockAppActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            tag=false;
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
    }
}
