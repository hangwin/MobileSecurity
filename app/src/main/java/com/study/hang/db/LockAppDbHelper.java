package com.study.hang.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hang on 16/3/27.
 */
public class LockAppDbHelper extends SQLiteOpenHelper {
    public LockAppDbHelper(Context context) {
        super(context, "lockedApp.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table lockedApp(_id Integer primary key autoincrement,packageName varchar(20));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
