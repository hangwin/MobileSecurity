package com.study.hang.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by hang on 16/3/26.
 */
public class FindNumberDB {
    private  static final String path="data/data/com.study.hang/files/adress.db";
    public static String findNumber() {
        SQLiteDatabase database=SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
         
        return null;
    }
}
