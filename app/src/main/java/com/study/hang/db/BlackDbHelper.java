package com.study.hang.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hang on 16/3/27.
 */
public class BlackDbHelper extends SQLiteOpenHelper {
    public BlackDbHelper(Context context) {
        super(context, "blackNumber", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table blackNumber(_id Integer primary key autoincrement,number varchar(20),mode varchar(2));
        db.execSQL("create table blackNumber(_id Integer primary key autoincrement,number varchar(20),mode varchar(2));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
