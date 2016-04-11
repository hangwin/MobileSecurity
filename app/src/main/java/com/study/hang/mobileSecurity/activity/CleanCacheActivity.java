package com.study.hang.mobileSecurity.activity;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.study.hang.mobileSecurity.R;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by hang on 16/4/10.
 */
public class CleanCacheActivity extends Activity {
    private static final int SCAN = 0;
    private static final int END = 1;
    private TextView status;
    private ProgressBar pb;
    private LinearLayout content;
    private PackageManager manager;
    private LayoutInflater inflater;
    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN:
                    appCacheinfo info= (appCacheinfo) msg.obj;
                    View view=inflater.inflate(R.layout.cache_item,null);
                    ImageView icon= (ImageView) view.findViewById(R.id.app_icon);
                    TextView appname= (TextView) view.findViewById(R.id.appname);
                    TextView cache= (TextView) view.findViewById(R.id.cache);
                    icon.setImageDrawable(info.icon);
                    appname.setText(info.appName);
                    cache.setText("缓存大小：" + Formatter.formatFileSize(CleanCacheActivity.this, info.cache));
                    content.addView(view, 0);
                    break;
                case END:
                    status.setText("扫描完成");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clean_cache);
        status = (TextView) findViewById(R.id.tv_status);
        pb = (ProgressBar) findViewById(R.id.pb);
        content = (LinearLayout) findViewById(R.id.content);
        inflater=getLayoutInflater();
        manager = getPackageManager();
        getCache();
    }

    private void getCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ApplicationInfo> list = manager.getInstalledApplications(0);
                Method[] methods = PackageManager.class.getMethods();
                Method getPackageSizeInfo = null;
                pb.setMax(list.size());
                int progress=0;
                try {
                    getPackageSizeInfo = PackageManager.class.getDeclaredMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
                    for (final ApplicationInfo info : list) {
                        Thread.sleep(100);
                        pb.setProgress(progress++);
                        System.out.println("===========>" + info.packageName);
                        getPackageSizeInfo.invoke(manager, info.packageName, new myPackageStateObserver());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                status.setText("正在扫描：" + info.loadLabel(manager));
                            }
                        });
                    }
                    Message m=Message.obtain();
                    m.what=END;
                    handler.sendMessage(m);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    System.out.println("====>" + e.getMessage());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    class myPackageStateObserver extends IPackageStatsObserver.Stub {

        @Override
        public void onGetStatsCompleted(final PackageStats pStats, boolean succeeded) throws RemoteException {
            long cache = pStats.cacheSize;

                final appCacheinfo app = new appCacheinfo();
                ApplicationInfo info = null;
                try {
                    info = manager.getApplicationInfo(pStats.packageName, 0);

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                app.icon = info.loadIcon(manager);
                app.packageName = info.packageName;
                app.appName = info.loadLabel(manager).toString();
            if(cache>0) {
                app.cache=cache;
                Message message = Message.obtain();
                message.what = SCAN;
                message.obj = app;
                handler.sendMessage(message);
                System.out.println("缓存========>" + Formatter.formatFileSize(CleanCacheActivity.this, cache));
            }

        }
    }
    class myPackageDateObserver extends IPackageDataObserver.Stub {

        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
               System.out.println("清理=========>"+packageName);
               System.out.println("**********succeed"+succeeded);
        }
    }
    class appCacheinfo{
        Drawable icon;
        String packageName,appName;
        long cache;
    }
    private long getDataDirectorySize() {
        File tmpFile = Environment.getDataDirectory();
        if (tmpFile == null) {
            return 0l;
        }
        String strDataDirectoryPath = tmpFile.getPath();
        StatFs localStatFs = new StatFs(strDataDirectoryPath);
        long size = localStatFs.getBlockSize() * localStatFs.getBlockCount();
        return size;
    }
    public void cleanAll(View view) {
        try {
            //Method freeStorageAndNotify=PackageManager.class.getDeclaredMethod("freeStorageAndNotify", Long.class, IPackageDataObserver.class);
            Method freeStorageAndNotify=null;
            Method[] methods=PackageManager.class.getMethods();
            for(Method m:methods) {
                if("freeStorageAndNotify".equals(m.getName())) {
                    freeStorageAndNotify=m;
                    System.out.println("================>"+"找到了"+freeStorageAndNotify.getName());
                }
            }
            Long freeStorageSize = Long.valueOf(getDataDirectorySize());
            freeStorageAndNotify.invoke(manager,freeStorageSize,new myPackageDateObserver());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
