package com.study.hang.mobileSecurity.activity;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.study.hang.mobileSecurity.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by hang on 16/4/10.
 */
public class CleanCacheActivity extends Activity {
    private TextView status;
    private ProgressBar pb;
    private LinearLayout content;
    private PackageManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clean_cache);
        status= (TextView) findViewById(R.id.tv_status);
        pb= (ProgressBar) findViewById(R.id.pb);
        content= (LinearLayout) findViewById(R.id.content);
        manager=getPackageManager();
        getCache();
    }

    private void getCache() {
        List<ApplicationInfo> list=manager.getInstalledApplications(0);
        Method[] methods= manager.getClass().getMethods();
        Method getPackageSizeInfo = null;
        try {
            getPackageSizeInfo = PackageManager.class
                    .getDeclaredMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
        for(ApplicationInfo info:list) {
           getPackageSizeInfo.invoke(manager,info.packageName , new myPackageStateObserver());
       }} catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    class myPackageStateObserver extends IPackageStatsObserver.Stub{

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            long cache=pStats.cacheSize;
            System.out.println("缓存========>"+Formatter.formatFileSize(CleanCacheActivity.this,cache));
        }
    }
}
