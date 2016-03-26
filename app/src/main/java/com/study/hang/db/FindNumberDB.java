package com.study.hang.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by hang on 16/3/26.
 */
public class FindNumberDB {


    public static String findNumber(Context context, String number) {
        File path = context.getFilesDir();
        String str = path.getAbsolutePath() + "/address.db";
        //System.out.println("======"+str+"======");
        String address = number;
        SQLiteDatabase database = SQLiteDatabase.openDatabase(str, null, SQLiteDatabase.OPEN_READONLY);
        String match = "^1[34568][0-9]{9}$";  //手机号码的正则表达式
        if (number.matches(match)) {

            Cursor cursor = database.rawQuery("SELECT location from data2 where id=(select outkey from data1 where id=?);", new String[]{number.substring(0, 7)});
            while (cursor.moveToNext()) {
                String location = cursor.getString(0);
                address = location;
            }
            cursor.close();
        } else {
            switch (number.length()) {
                case 3:
                    address = "报警求助电话";
                    break;
                case 4:
                    address = "模拟器号码";
                    break;
                case 5:
                    address = "客服电话";
                    break;
                case 6:
                    break;
                case 7:
                case 8:
                    address = "本地电话";
                    break;
                default:
                    if (number.length() == 11 && number.startsWith("0")) {
                        Cursor cursor = database.rawQuery("SELECT location from data2 where area=?;", new String[]{number.substring(1, 3)});
                        while (cursor.moveToNext()) {
                            address = cursor.getString(0);
                            address = address.substring(0, address.length() - 2);
                        }
                        cursor.close();

                     }
                    else {
                        Cursor cursor = database.rawQuery("SELECT location from data2 where area=?;", new String[]{number.substring(1, 4)});
                        while (cursor.moveToNext()) {
                            address = cursor.getString(0);
                            address = address.substring(0, address.length() - 2);
                        }
                        cursor.close();
                    }
                    break;
            }
        }
        return address;
    }
}
