package com.study.hang.mobileSecurity.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.study.hang.mobileSecurity.R;
import com.study.hang.mobileSecurity.receiver.MyAppWidgetProvider;
import com.study.hang.util.SystemInfoUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hang on 16/4/7.
 */
public class WidgetService extends Service {
    private Timer timer;
    private TimerTask task;
    private AppWidgetManager widgetManager;
    private ScreenOff off;
    private ScreenOn on;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        System.out.println("===================小组件服务开启============");
        super.onCreate();
        startTask();
        off=new ScreenOff();
        on=new ScreenOn();
        registerReceiver(off, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        registerReceiver(on,new IntentFilter(Intent.ACTION_SCREEN_ON));
    }

    private void startTask() {
        if (timer==null&&task==null) {
            timer = new Timer();
            task = new TimerTask() {
                @Override
                public void run() {
                    System.out.println("更新----------->");
                    widgetManager = AppWidgetManager.getInstance(WidgetService.this);
                    ComponentName provider = new ComponentName(WidgetService.this, MyAppWidgetProvider.class);
                    RemoteViews views = new RemoteViews(getPackageName(), R.layout.my_appwidget);
                    views.setTextViewText(R.id.processcount, SystemInfoUtil.getRunningProcessnum(WidgetService.this) + "个");
                    long mem = SystemInfoUtil.getAvailableMemory(WidgetService.this);
                    views.setTextViewText(R.id.availmen, Formatter.formatFileSize(WidgetService.this, mem));
                    Intent intent = new Intent();
                    intent.setAction("com.hang.cleanall");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(WidgetService.this, 0, intent, 0);
                    views.setOnClickPendingIntent(R.id.bt_clean, pendingIntent);
                    widgetManager.updateAppWidget(provider, views);
                }
            };
            timer.schedule(task, 0, 5000);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(on);
        unregisterReceiver(off);
        on=null;
        off=null;
        stopTask();
    }

    private void stopTask() {
        if(task!=null&&timer!=null) {
            task.cancel();
            timer.cancel();
            task=null;
            timer=null;
        }
    }

    class ScreenOff extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("锁屏了");
           stopTask();
        }
    }

    class ScreenOn extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("on===============");
            startTask();
        }
    }
}
