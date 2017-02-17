package com.hillsidewatchers.sdu.sonicoperator.DatabaseManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lenovo on 2016/3/16.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static String name ="SonicDatabase.db";
    private static int version =1;
    public DBOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_mobile_operation = "create table mobile_operation(id integer primary key autoincrement,name varchar(255),packagename varchar(255),info varchar)";
        db.execSQL(sql_mobile_operation);//完成数据库表的创建
        String sql_sonic_operation = "create table sonic_operation(id integer primary key autoincrement,name varchar(255),info varchar)";
        db.execSQL(sql_sonic_operation);
        String sql_connection = "create table connection(id integer primary key autoincrement,gesture_name varchar,record_name varchar(255))";
        db.execSQL(sql_connection);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
