package com.hillsidewatchers.sdu.sonicoperator.ViewSurface;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hillsidewatchers.sdu.sonicoperator.DatabaseManager.DBSQLiteDao;
import com.hillsidewatchers.sdu.sonicoperator.R;

/**
 * Created by lenovo on 2016/3/25.
 */
public class InsertTempActivity extends Activity {
    //left - right  719  204   1684  805
    //right - left 1493 222  600  600
    private static final String WPS = "cn.wps.moffice_eng";
    private static final String READ = "com.duokan.hdreader";
    private static final String DALIAN = "com.racoongame.binta";
    boolean flag = false;
    private static final String mobile_operation = "mobile_operation";
    private static final String sonic_operation = "sonic_operation";
    private static final String connection = "connection";
    DBSQLiteDao dbsqLiteDao ;
    Button button ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_activty);
        dbsqLiteDao = new DBSQLiteDao(getApplicationContext());
        button = (Button) findViewById(R.id.insert);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Insert_IntoMobileDB();
                InsertIntoSonicOperator();
                InsertIntoConnection();
            }
        });
    }
    public void Insert_IntoMobileDB(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", "向右滑动");
        contentValues.put("packagename","com.miui.home");
        contentValues.put("info", "(swipe 787 668 1257 668)|");
        flag = dbsqLiteDao.add(mobile_operation, contentValues);
        contentValues.put("name", "向左滑动");
        contentValues.put("packagename","com.miui.home");
        contentValues.put("info", "(swipe 1257 668 787 668)|");
        flag = dbsqLiteDao.add(mobile_operation, contentValues);
        contentValues.put("name", "向上滑动");
        contentValues.put("packagename","com.miui.home");
        contentValues.put("info", "(swipe 1062 1000 1162 400)|");
        flag = dbsqLiteDao.add(mobile_operation, contentValues);
        contentValues.put("name", "向下滑动");
        contentValues.put("packagename","com.miui.home");
        contentValues.put("info", "(swipe 1062 400 1062 1000)|");
        flag = dbsqLiteDao.add(mobile_operation, contentValues);

        contentValues.put("name", "返回");
        contentValues.put("packagename","com.miui.home");
        contentValues.put("info", "(keyevent 4)|");
        flag = dbsqLiteDao.add(mobile_operation, contentValues);
        contentValues.put("name", "点击");
        contentValues.put("packagename","com.miui.home");
        contentValues.put("info", "(tap 1000 300)|");
        flag = dbsqLiteDao.add(mobile_operation, contentValues);

//        flag = dbsqLiteDao.add(mobile_operation, contentValues);
//        ContentValues contentValues = new ContentValues();
//        ContentValues contentValues = new ContentValues();
        contentValues.put("name", "向右滑动");
        contentValues.put("packagename",WPS);
        contentValues.put("info", "(swipe 787 668 1257 668)|");
        flag = dbsqLiteDao.add(mobile_operation, contentValues);
        contentValues.put("name", "向左滑动");
        contentValues.put("packagename",WPS);
        contentValues.put("info", "(swipe 1257 668 787 668)|");
        flag = dbsqLiteDao.add(mobile_operation, contentValues);
        contentValues.put("name", "向上滑动");
        contentValues.put("packagename",WPS);
        contentValues.put("info", "(swipe 1062 1000 1162 400)|");
        flag = dbsqLiteDao.add(mobile_operation, contentValues);
        contentValues.put("name", "向下滑动");
        contentValues.put("packagename",WPS);
        contentValues.put("info", "(swipe 1062 400 1062 1000)|");
        flag = dbsqLiteDao.add(mobile_operation, contentValues);

        contentValues.put("name", "返回");
        contentValues.put("packagename",WPS);
        contentValues.put("info", "(keyevent 4)|");
        flag = dbsqLiteDao.add(mobile_operation, contentValues);
        contentValues.put("name", "点击");
        contentValues.put("packagename",WPS);
        contentValues.put("info", "swipe(0,500,1000,500)|");
        flag = dbsqLiteDao.add(mobile_operation, contentValues);

//        contentValues.put("name", "点击");
//        contentValues.put("packagename",WPS);
//        contentValues.put("info", "(tap 1000 300)|");
        /**
         *
         */
        contentValues.put("name", "向右滑动");
        contentValues.put("packagename",READ);
        contentValues.put("info", "(swipe 787 668 1257 668)|");
        flag = dbsqLiteDao.add(mobile_operation, contentValues);
        contentValues.put("name", "向左滑动");
        contentValues.put("packagename",READ);
        contentValues.put("info", "(swipe 1257 668 787 668)|");
        flag = dbsqLiteDao.add(mobile_operation, contentValues);
        contentValues.put("name", "向上滑动");
        contentValues.put("packagename",READ);
        contentValues.put("info", "(swipe 1062 1000 1162 400)|");
        flag = dbsqLiteDao.add(mobile_operation, contentValues);
        contentValues.put("name", "向下滑动");
        contentValues.put("packagename",READ);
        contentValues.put("info", "(swipe 1062 300 1062 1300)|");
        flag = dbsqLiteDao.add(mobile_operation, contentValues);

        contentValues.put("name", "返回");
        contentValues.put("packagename",READ);
        contentValues.put("info", "(keyevent 4)|");
        flag = dbsqLiteDao.add(mobile_operation, contentValues);
        contentValues.put("name", "点击");
        contentValues.put("packagename",READ);
        contentValues.put("info", "(tap 1000 300)|");
/**
 * 游戏的两个
 */

        contentValues.put("name", "向右滑动");
        contentValues.put("packagename",DALIAN);
        contentValues.put("info", "(swipe 600 600 650 650)|");
        flag = dbsqLiteDao.add(mobile_operation, contentValues);
        contentValues.put("name", "向左滑动");
        contentValues.put("packagename",DALIAN);
        contentValues.put("info", "(swipe 1000 400 950 450)|");
        flag = dbsqLiteDao.add(mobile_operation, contentValues);

    }
    public void InsertIntoSonicOperator(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", "right");
        contentValues.put("info","right");
        flag = dbsqLiteDao.add(sonic_operation, contentValues);
        contentValues.put("name", "left");
        contentValues.put("info","left");
        flag = dbsqLiteDao.add(sonic_operation, contentValues);
        contentValues.put("name", "up");
        contentValues.put("info","up");
        flag = dbsqLiteDao.add(sonic_operation, contentValues);
        contentValues.put("name", "down");
        contentValues.put("info","down");
        flag = dbsqLiteDao.add(sonic_operation, contentValues);
        contentValues.put("name", "pull");
        contentValues.put("info","pull");
        flag = dbsqLiteDao.add(sonic_operation, contentValues);
        contentValues.put("name", "push");
        contentValues.put("info","push");
        flag = dbsqLiteDao.add(sonic_operation, contentValues);
    }
    public void InsertIntoConnection(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("gesture_name", "right");
        contentValues.put("record_name","向右滑动");
        flag = dbsqLiteDao.add(connection, contentValues);
        contentValues.put("gesture_name", "left");
        contentValues.put("record_name","向左滑动");
        flag = dbsqLiteDao.add(connection, contentValues);
        contentValues.put("gesture_name", "up");
        contentValues.put("record_name","向上滑动");
        flag = dbsqLiteDao.add(connection, contentValues);
        contentValues.put("gesture_name", "down");
        contentValues.put("record_name","向下滑动");
        flag = dbsqLiteDao.add(connection, contentValues);
        contentValues.put("gesture_name", "pull");
        contentValues.put("record_name","返回");
        flag = dbsqLiteDao.add(connection, contentValues);
        contentValues.put("gesture_name", "push");
        contentValues.put("record_name","点击");
        flag = dbsqLiteDao.add(connection, contentValues);
    }

}
