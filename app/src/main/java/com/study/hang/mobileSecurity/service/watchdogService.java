package com.study.hang.mobileSecurity.service;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.study.hang.db.LockAppDao;

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
                    List<ActivityManager.RunningTaskInfo> list=manager.getRunningTasks(100);
                    ActivityManager.RunningTaskInfo task=list.get(0);
                    if(dao.find(task.topActivity.getPackageName())) {
                            System.out.println("+++++++++"+task.topActivity.getPackageName());
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
