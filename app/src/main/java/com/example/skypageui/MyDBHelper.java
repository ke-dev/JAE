package com.example.skypageui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    private Context mContext;
    private static final String DATABASE_NAME = "MyDB.db";
    private static final int version = 1;

    public static final String CREATE_USERDATA = "create table userData("// +
           // "id integer primary key autoincrement,"
            + "nametext,"
            + "password)";


    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        super(context, name, cursorFactory, version);
        mContext = context;
    }
//创建表格
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERDATA);
    }
//更新表格
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table if exists"+getDatabaseName());
        onCreate(db);
    }

}