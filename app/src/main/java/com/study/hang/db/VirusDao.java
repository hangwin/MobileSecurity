package com.study.hang.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by hang on 16/4/10.
 */
public class VirusDao {


    public static boolean find(Context context,String md5) {
        boolean result=false;
        File path=context.getFilesDir();
        String str = path.getAbsolutePath() + "/antivirus.db";
        SQLiteDatabase db=SQLiteDatabase.openDatabase(str,null,SQLiteDatabase.OPEN_READONLY);
        Cursor cursor=db.query("datable",new String[]{"md5"},"md5=?",new String[]{md5},null,null,null);
        while (cursor.moveToFirst()) {
            result=true;
        }
        cursor.close();
        db.close();
        return result;
    }
}
