package com.hillsidewatchers.sdu.sonicoperator;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hillsidewatchers.sdu.sonicoperator.DatabaseManager.DBSQLiteDao;
import com.hillsidewatchers.sdu.sonicoperator.ViewSurface.DragLayout;
import com.hillsidewatchers.sdu.sonicoperator.ViewSurface.Fragment1;
import com.hillsidewatchers.sdu.sonicoperator.ViewSurface.Fragment2;
import com.hillsidewatchers.sdu.sonicoperator.ViewSurface.Fragment3;
import com.hillsidewatchers.sdu.sonicoperator.ViewSurface.Fragment4;
import com.hillsidewatchers.sdu.sonicoperator.ViewSurface.Fragment5;
import com.hillsidewatchers.sdu.sonicoperator.ViewSurface.SettingActivity;

import java.util.List;
import java.util.Map;

import sdu.embedded.SonicCollector.CollectorService;

/**
 * 这个类修改过
 */
public class MainActivity extends Activity {
    private static final String mobile_operation = "mobile_operation";
    private static final String sonic_operation = "sonic_operation";
    private static final String connection = "connection";
    private static final String TAG = "MainActivity";
    private static final String MIUI_HOME = "com.miui.home";
    private static final String WEIXIN = "com.tencent.mm";
    Intent intent = null;
    Fragment1 f1;
    Fragment2 f2;
    Fragment3 f3;
    Fragment4 f4;
    Fragment5 f5;
    boolean state = false;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private ImageButton toggleBt = null;
    private ImageButton sw_open_sonic = null;
    TextView textView = null;

    /**
     * 设置按钮
     */
    private ImageButton ll_setting;
/**
 * 录制脚本
 */
    private LinearLayout ll_scriptRecord;
    /**
     * 从云端获得手势
     */
    private LinearLayout ll_getGesture;

    /**
     * 操作中心
     */
    private LinearLayout ll_operatorcenter;

    /**
     * 扩展
     */

    private LinearLayout ll_expand;
    private DragLayout dl = null;
    private CollectorService msgService;
    public ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            msgService = ((CollectorService.MsgBinder)service).getService();
            msgService.registReceiver();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG,"onServiceDisconnected");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        intent = new Intent(MainActivity.this, CollectorService.class);
        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();

        initDragLayout();

        if(f1==null){
            Log.e("MainActivity","f1==null");
            f1 = new Fragment1();

            transaction.add(R.id.container, f1);
        }

        else {
            transaction.show(f1);
        }
        if(f2==null){
            f2 = new Fragment2();
            transaction.add(R.id.container,f2);
        }
        else
            transaction.show(f2);
        if(f3==null){
            f3 = new Fragment3();


//            f3.startThread();
            transaction.add(R.id.container,f3);
        }
        else {
            transaction.show(f3);
        }
        if(f4==null){
            f4 = new Fragment4();
            transaction.add(R.id.container,f4);
        }
        else {
            transaction.show(f4);
        }
        if(f5==null){
            f5 = new Fragment5();
            transaction.add(R.id.container,f5);
        }
        else {
            transaction.show(f5);
        }
        hideFragments(transaction);
        transaction.show(f1);
        transaction.commit();

        Log.e("==", "==");
        sw_open_sonic = (ImageButton)findViewById(R.id.sw_open_sonic);
        textView = (TextView) findViewById(R.id.toggleText);
        toggleBt = (ImageButton) findViewById(R.id.toggleBt);
        toggleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dl.getStatus() == DragLayout.Status.Open)
                    dl.close();
                else
                    dl.open();
            }
        });
        sw_open_sonic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!state){
                   bindService(intent,conn,BIND_AUTO_CREATE);
                   sw_open_sonic.setBackgroundResource(R.drawable.sonic_open);
                   state = true;
                   Toast.makeText(getBaseContext(),"正在打开超声波监听",Toast.LENGTH_SHORT).show();
                   if(msgService!=null){
                       msgService.registReceiver();
                   }
                   else {
                       Log.e(TAG,"msgService为空");
                   }

               }
                else {
                   state = false;
                   sw_open_sonic.setBackgroundResource(R.drawable.sonic_close);
                   unbindService(conn);
                   Toast.makeText(getApplicationContext(),"unBindServeice",Toast.LENGTH_SHORT).show();
               }


            }
        });

        insert_into_db_sonic_operator();

    }
    public void insert_into_db_sonic_operator(){
        DBSQLiteDao dbsqLiteDao = new DBSQLiteDao(getApplicationContext());
//        Map<String,String> map_pull = dbsqLiteDao.query(sonic_operation,"name=?",new String[]{"pull"});
//        if (map_pull.size()==0){
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("name", "pull");
//            contentValues.put("info", "pull");
//            dbsqLiteDao.add(sonic_operation, contentValues);
//        }
//        Map<String,String> map_push = dbsqLiteDao.query(sonic_operation,"name=?",new String[]{"push"});
//        if (map_push.size()==0){
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("name", "push");
//            contentValues.put("info", "push");
//            dbsqLiteDao.add(sonic_operation, contentValues);
//        }
        Map<String,String> map_left = dbsqLiteDao.query(sonic_operation,"name=?",new String[]{"left"});
        if (map_left.size()==0){
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", "left");
            contentValues.put("info", "left");
            dbsqLiteDao.add(sonic_operation, contentValues);
        }
        Map<String,String> map_right = dbsqLiteDao.query(sonic_operation,"name=?",new String[]{"right"});
        if (map_right.size()==0){
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", "right");
            contentValues.put("info", "right");
            dbsqLiteDao.add(sonic_operation, contentValues);
        }
        Map<String,String> map_up = dbsqLiteDao.query(sonic_operation,"name=?",new String[]{"up"});
        if (map_up.size()==0){
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", "up");
            contentValues.put("info", "up");
            dbsqLiteDao.add(sonic_operation, contentValues);
        }
        Map<String,String> map_down = dbsqLiteDao.query(sonic_operation,"name=?",new String[]{"down"});
        if (map_down.size()==0){
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", "down");
            contentValues.put("info", "down");
            dbsqLiteDao.add(sonic_operation, contentValues);
        }
        Map<String,String> miui = dbsqLiteDao.query(mobile_operation,"packagename = ?",new String[]{MIUI_HOME});
        if(miui.size()==0){
            ContentValues contentValues1 = new ContentValues();
            contentValues1.put("name", "向左滑动");
            contentValues1.put("info", "(swipe 1000 1000 300 1000)");
            contentValues1.put("packagename",MIUI_HOME);
            dbsqLiteDao.add(mobile_operation, contentValues1);
            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("name", "向右滑动");
            contentValues2.put("info", "(swipe 300 1000 1000 1000)");
            contentValues2.put("packagename",MIUI_HOME);
            dbsqLiteDao.add(mobile_operation, contentValues2);
            ContentValues contentValues3 = new ContentValues();
            contentValues3.put("name", "向上滑动");
            contentValues3.put("info", "(swipe 550 1400 600 500)");
            contentValues3.put("packagename",MIUI_HOME);
            dbsqLiteDao.add(mobile_operation, contentValues3);
            ContentValues contentValues4 = new ContentValues();
            contentValues4.put("name", "向下滑动");
            contentValues4.put("info", "(swipe 580 300 580 1200)");
            contentValues4.put("packagename",MIUI_HOME);
            dbsqLiteDao.add(mobile_operation, contentValues4);
        }
//        Map<String,String> weixin = dbsqLiteDao.query(mobile_operation,"packagename = ?",new String[]{WEIXIN});
//        if(weixin.size()==0){
//            ContentValues contentValues1 = new ContentValues();
//            contentValues1.put("name", "向左滑动");
//            contentValues1.put("info", "(swipe 1000 1000 300 1000)");
//            contentValues1.put("packagename",WEIXIN);
//            dbsqLiteDao.add(mobile_operation, contentValues1);
//            ContentValues contentValues2 = new ContentValues();
//            contentValues2.put("name", "向右滑动");
//            contentValues2.put("info", "(swipe 300 1000 1000 1000)");
//            contentValues2.put("packagename",WEIXIN);
//            dbsqLiteDao.add(mobile_operation, contentValues2);
//            ContentValues contentValues3 = new ContentValues();
//            contentValues3.put("name", "向上滑动");
//            contentValues3.put("info", "(swipe 550 1400 600 500)");
//            contentValues3.put("packagename",WEIXIN);
//            dbsqLiteDao.add(mobile_operation, contentValues3);
//            ContentValues contentValues4 = new ContentValues();
//            contentValues4.put("name", "向下滑动");
//            contentValues4.put("info", "(swipe 580 300 580 1200)");
//            contentValues4.put("packagename",WEIXIN);
//            dbsqLiteDao.add(mobile_operation, contentValues4);
//        }
        List<Map<String,String>> connection_q = dbsqLiteDao.getMapList(connection,null,null);
        if(connection_q.size()==0){
            ContentValues contentValues1 = new ContentValues();
            contentValues1.put("gesture_name", "up");
            contentValues1.put("record_name", "向上滑动");
            dbsqLiteDao.add(connection, contentValues1);
            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("gesture_name", "down");
            contentValues2.put("record_name", "向下滑动");
            dbsqLiteDao.add(connection, contentValues2);
            ContentValues contentValues3 = new ContentValues();
            contentValues3.put("gesture_name", "left");
            contentValues3.put("record_name", "向左滑动");
            dbsqLiteDao.add(connection, contentValues3);
            ContentValues contentValues4 = new ContentValues();
            contentValues4.put("gesture_name", "right");
            contentValues4.put("record_name", "向右滑动");
            dbsqLiteDao.add(connection, contentValues4);
        }

    }
    public void initDragLayout() {
        /*
		 * 侧拉栏部分
		 */
        dl = (DragLayout) findViewById(R.id.dl);
        dl.setDragListener(new DragLayout.DragListener() {

            @Override
            public void onOpen() {

            }

            @Override
            public void onDrag(float percent) {

            }

            @Override
            public void onClose() {

            }
        });

        /**
         * 设置按钮
         */
        ll_setting = (ImageButton) findViewById(R.id.ll_main_settting);

        /**
         * 设置按钮监听
         */
        ll_setting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"点击设置",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        /**
         * scriptbox按钮
         */
        ll_scriptRecord = (LinearLayout) findViewById(R.id.ll_scriptRecord);

        /**
         * scriptbox按钮监听
         */
        ll_scriptRecord.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                transaction = fragmentManager.beginTransaction();
                hideFragments(transaction);
                textView.setText("脚本");
                Toast.makeText(getApplicationContext(), "scriptbox",
                        Toast.LENGTH_SHORT).show();
                if(f1==null){
                    f1 = new Fragment1();
                    transaction.add(R.id.container,f1);
                }
                else {
                    transaction.show(f1);
                }
                transaction.commit();
//                transaction.show(fragmentlist.get(0));
//                fragmentManager.beginTransaction().replace(R.id.container, fragmentlist.get(0)).commit();
            }
        });

        /**
         * GetGesture按钮
         */
        ll_getGesture = (LinearLayout) findViewById(R.id.ll_getGesture);

        /**
         * GetGesture按钮监听
         */
        ll_getGesture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                textView.setText("云端");
                Toast.makeText(getApplicationContext(), "GetGesture",
                        Toast.LENGTH_SHORT).show();
                transaction = fragmentManager.beginTransaction();
//                Toast.makeText(getApplicationContext(),"该功能尚未开启",Toast.LENGTH_SHORT).show();
//                transaction.show(fragmentlist.get(1));
                hideFragments(transaction);
                if(f2==null){
                    f2 = new Fragment2();
                    transaction.add(R.id.container,f2);
                }
                else
                    transaction.show(f2);
                    transaction.commit();
//                fragmentManager.beginTransaction().replace(R.id.container, fragmentlist.get(1)).commit();
            }
        });

        /**
         * OperatorCenter按钮
         */
        ll_operatorcenter = (LinearLayout) findViewById(R.id.ll_operatorcenter);

        /**
         * 手OperatorCenter按钮监听
         */
        ll_operatorcenter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                textView.setText("操作");
                Toast.makeText(getApplicationContext(), "OperatorCenter",
                        Toast.LENGTH_SHORT).show();
                transaction = fragmentManager.beginTransaction();
                hideFragments(transaction);
                if(f3==null){
                    f3 = new Fragment3();
                    transaction.add(R.id.container,f3);
                }
                else {
                    transaction.show(f3);
                }
                transaction.commit();
//                fragmentManager.beginTransaction().replace(R.id.container, fragmentlist.get(2)).commit();
//                transaction.show(fragmentlist.get(2));
            }
        });

        /**
         * 拓展按钮
         */
        ll_expand = (LinearLayout) findViewById(R.id.ll_expand);

        /**
         * 拓展按钮监听
         */
        ll_expand.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"该功能尚未开启",Toast.LENGTH_SHORT).show();
//                transaction = fragmentManager.beginTransaction();
//                hideFragments(transaction);
//                if(f4==null){
//                    f4 = new Fragment4();
//                    transaction.add(R.id.container,f4);
//                }
//                else {
//                    transaction.show(f4);
//                }
//                transaction.commit();
//
//                textView.setText("Expand");
////                fragmentManager.beginTransaction().replace(R.id.container, fragmentlist.get(3)).commit();
////                transaction.show(fragmentlist.get(5));
//
//                Toast.makeText(getApplicationContext(), "Expand",
//                        Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void hideFragments(FragmentTransaction transaction) {
        if (f1 != null) {
            transaction.hide(f1);
        }
        if (f2 != null) {
            transaction.hide(f2);
        }
        if (f3 != null) {
            transaction.hide(f3);
        }
        if (f4 != null) {
            transaction.hide(f4);
        }
        if (f5 != null) {
            transaction.hide(f5);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unbindService(conn);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
