package com.study.hang.util;

import android.graphics.drawable.Drawable;

/**
 * Created by hang on 16/4/5.
 */
public class ProcessEntity {
    private Drawable icon;
    private String name;
    private String packageName;
    private long memsize;
    private boolean isuserprocess;

    public Drawable getIcon() {
        return icon;
    }

    public boolean isuserprocess() {
        return isuserprocess;
    }

    public long getMemsize() {
        return memsize;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setIsuserprocess(boolean isuserprocess) {
        this.isuserprocess = isuserprocess;
    }

    public void setMemsize(long memsize) {
        this.memsize = memsize;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
