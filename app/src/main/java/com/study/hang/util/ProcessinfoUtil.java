package com.study.hang.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Debug;

import com.study.hang.mobileSecurity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hang on 16/4/5.
 */
public class ProcessinfoUtil {
    private static  List<ProcessEntity> list;
    public static List<ProcessEntity> getProcessInfo(Context context) {
        PackageManager packagemanager=context.getPackageManager();
        ActivityManager manager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos=manager.getRunningAppProcesses();
        list=new ArrayList<ProcessEntity>();
        for (ActivityManager.RunningAppProcessInfo info:infos) {
            ProcessEntity process=new ProcessEntity();
            String processName=info.processName;
            int[] pid=new int[]{info.pid};
            Debug.MemoryInfo[] processmem=manager.getProcessMemoryInfo(pid);
            process.setMemsize(processmem[0].getTotalPrivateDirty()*1024);
            process.setPackageName(processName);
            process.setIsChecked(false);
            try {
               ApplicationInfo appinfo=packagemanager.getApplicationInfo(processName,0);
               process.setIcon(appinfo.loadIcon(packagemanager));
               process.setName(appinfo.loadLabel(packagemanager).toString());
               if((appinfo.flags&ApplicationInfo.FLAG_SYSTEM)==0) {
                   process.setIsuserprocess(true);
               }else {
                   process.setIsuserprocess(false);
               }

             } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                process.setIcon(context.getResources().getDrawable(R.mipmap.ic_launcher));
                process.setName(processName);
            }
            list.add(process);
        }
        return list;
    }
}
