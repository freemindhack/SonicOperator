package com.hillsidewatchers.sdu.sonicoperator.DatabaseManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2016/3/16.
 */
public class DBSQLiteDao {
    private DBOpenHelper helper = null;
    public DBSQLiteDao(Context context){
        helper = new DBOpenHelper(context);
    }
    public boolean add(String table_name,ContentValues values){
        boolean flags = false;
        SQLiteDatabase database = null;
        long id = -1;
        try {
            database = helper.getWritableDatabase();
            id =database.insert(table_name,null,values);//表名
            flags = (id!=-1?true:false);
        }catch (Exception e){

        }finally {
            if(database!=null){
                database.close();
            }
        }
        return flags;
    }
    public boolean delete(String table_name,String whereClouse,String[]whereArgs){
        boolean flag = false;
        SQLiteDatabase database = null;
        int count =0;
        try {
            database = helper.getWritableDatabase();
            count = database.delete(table_name, whereClouse, whereArgs);
            flag = (count>0?true:false);
        }catch (Exception e){

        }finally {
            if(database!=null){
                database.close();
            }
        }
        return flag;
    }
    public boolean update(String table_name,ContentValues values,String whereClouse,String[]whereArgs){
        boolean flag = false;
        SQLiteDatabase database = null;
        int count =0;
        try{
            database = helper.getWritableDatabase();
            count = database.update(table_name, values, whereClouse, whereArgs);
            flag = (count>0?true:false);
        }catch (Exception e){

        }finally {
            if(database!=null){
                database.close();
            }
        }
        return flag;
    }
    //查询单条记录
    public Map<String,String> query(String table_name,String selection,String[]selectionArgs){
        SQLiteDatabase database = null;
        Cursor cursor = null;
        Map<String,String> map = new HashMap<String ,String>();
        try {
            database = helper.getReadableDatabase();
            //查询单条记录
            cursor = database.query(true, table_name, null, selection, selectionArgs, null, null, null, null);
            int cols_len = cursor.getColumnCount();//获取游标个数，即查询所获结果数目
            while (cursor.moveToNext()){
                for(int i=0;i<cols_len;i++){
                    String cols_name = cursor.getColumnName(i);
                    String cols_values = cursor.getString(cursor.getColumnIndex(cols_name));
                    if(cols_values==null){
                        cols_values = "";
                    }
                    map.put(cols_name,cols_values);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(database!=null){
                database.close();
            }
        }
        return map;
    }
    public List<Map<String,String>> getMapList(String table_name,String selelction,String[]selectionArgs){
        SQLiteDatabase database = null;
        Cursor cursor = null;
        List<Map<String,String>>list = new ArrayList<>();
        try {
            database = helper.getReadableDatabase();
            cursor = database.query(false,table_name,null,selelction,selectionArgs,null,null,null,null);
            int cols_len = cursor.getColumnCount();
            while (cursor.moveToNext()){
                Map<String,String>map = new HashMap<String,String>();
                for(int i=0;i<cols_len;i++){
                    String cols_name = cursor.getColumnName(i);
                    String cols_values = cursor.getString(cursor.getColumnIndex(cols_name));
                    if(cols_values==null){
                        cols_values = "";
                    }
                    map.put(cols_name,cols_values);
                }
                list.add(map);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(database!=null){
                database.close();
            }
        }
        return list;

    }
}
