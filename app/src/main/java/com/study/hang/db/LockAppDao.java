package com.study.hang.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by hang on 16/4/7.
 */
public class LockAppDao {
    private LockAppDbHelper helper;
    public LockAppDao(Context context) {
        helper=new LockAppDbHelper(context);
    }

    public void insert(String packageName) {
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("packageName",packageName);
        db.insert("lockedApp",null,values);
        db.close();
    }

    public void delete(String packageName) {
        SQLiteDatabase db=helper.getWritableDatabase();
        db.delete("lockedApp", "packageName=?", new String[]{packageName});
        db.close();
    }

    public boolean find(String packageName) {
        SQLiteDatabase db=helper.getReadableDatabase();
        boolean result=false;
        Cursor cursor=db.query("lockedApp", new String[]{"packageName"}, "packageName=?", new String[]{packageName}, null, null, null, null);
        if(cursor.moveToFirst()) {
          result=true;
        }
        return result;
    }
}
