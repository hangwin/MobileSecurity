package com.study.hang.mobileSecurity.receiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.study.hang.mobileSecurity.service.WidgetService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hang on 16/4/7.
 */
public class MyAppWidgetProvider extends AppWidgetProvider{
    private Intent intent;
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        System.out.println("========>onReceive");
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        System.out.println("========>onDisabled");
        if(intent!=null) {
            context.stopService(intent);
            intent=null;
        }


    }



    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Intent intent=new Intent(context, WidgetService.class);
        context.startService(intent);
        System.out.println("========>onEnabled");

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        System.out.println("========>onUpdate");
    }
}
