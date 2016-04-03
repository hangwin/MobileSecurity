package com.study.hang.util;

import android.graphics.drawable.Drawable;

/**
 * Created by hang on 16/4/3.
 */
public class AppEntity {
    private Drawable icon;
    private String packageName;
    private String appName;
    private boolean isInRom;
    private boolean isUser;

    public String getAppName() {
        return appName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public boolean isInRom() {
        return isInRom;
    }

    public boolean isUser() {
        return isUser;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setIsInRom(boolean isInRom) {
        this.isInRom = isInRom;
    }

    public void setIsUser(boolean isUser) {
        this.isUser = isUser;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
