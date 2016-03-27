package com.study.hang.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hang on 16/3/27.
 */
public class BlackNumberDao {
    private BlackDbHelper mDbHelper;
    private List<BlackNumberEntity> list;
    public BlackNumberDao(Context context) {
        mDbHelper=new BlackDbHelper(context);
    }

    public void insert(String number,String mode) {
        SQLiteDatabase db=mDbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("number",number);
        values.put("mode", mode);
        db.insert("blackNumber",null,values);
        db.close();
    }
    public List<BlackNumberEntity> queryAll() {
        list=new ArrayList<BlackNumberEntity>();
        SQLiteDatabase db=mDbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select number,mode from blackNumber", null);
        BlackNumberEntity entity;
        while (cursor.moveToNext()) {
            entity=new BlackNumberEntity();
            entity.setNumber(cursor.getString(0));
            entity.setMode(cursor.getString(1));
            list.add(entity);
        }
        db.close();
        return list;
    }
    public void update(String number,String mode) {
        SQLiteDatabase db=mDbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("mode",mode);
        db.update("blackNumber",values,"number=?",new String[]{number});
        db.close();
    }
    public boolean query(String number) {
        SQLiteDatabase db=mDbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from blackNumber where number=?", new String[]{number});
        if(cursor.moveToNext()) {
            return true;
        }
        return false;
    }

    public String queryMode(String number) {
        SQLiteDatabase db=mDbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select mode from blackNumber where number=?", new String[]{number});
        String mode=null;
        if(cursor.moveToNext()) {
            mode=cursor.getString(0);
        }
        return mode;
    }

    public void delete(String number) {
        SQLiteDatabase db=mDbHelper.getWritableDatabase();
        db.delete("blackNumber","number=?",new String[]{number});
        db.close();
    }
}
