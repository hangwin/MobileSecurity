package com.study.hang.util;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hang on 16/4/3.
 */
public class AppInfoUtil {
    private static List<AppEntity> list;
    public static List<AppEntity> getAppInfoList(Context context) {
        list=new ArrayList<AppEntity>();
        PackageManager manager=context.getPackageManager();
        List<PackageInfo> infos= manager.getInstalledPackages(0);
        for(PackageInfo info:infos) {
            AppEntity entity=new AppEntity();
            entity.setPackageName(info.packageName);
            entity.setAppName((String) info.applicationInfo.loadLabel(manager));
            entity.setIcon(info.applicationInfo.loadIcon(manager));
            int flag=info.applicationInfo.flags;
            if ((flag&ApplicationInfo.FLAG_SYSTEM)==1) {
                entity.setIsUser(false);
            }else {
                entity.setIsUser(true);
            }
            if((flag&ApplicationInfo.FLAG_EXTERNAL_STORAGE)==1) {
                entity.setIsInRom(false);
            }else {
                entity.setIsInRom(true);
            }
            list.add(entity);
        }
        return list;
    }
}
