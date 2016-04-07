package com.study.hang.mobileSecurity.receiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hang on 16/4/7.
 */
public class MyAppWidgetProvider extends AppWidgetProvider{
    private Timer timer;
    private TimerTask task;
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        System.out.println("========>onReceive");
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        System.out.println("========>onDisabled");
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

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        System.out.println("========>onEnabled");
        timer=new Timer();
        task=new TimerTask() {
            @Override
            public void run() {
                System.out.println("=====>更新widget");
            }
        };
        timer.schedule(task,0,3000);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        System.out.println("========>onUpdate");
    }
}
