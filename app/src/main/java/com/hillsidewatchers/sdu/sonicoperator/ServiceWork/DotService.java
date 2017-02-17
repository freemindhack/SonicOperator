package com.hillsidewatchers.sdu.sonicoperator.ServiceWork;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hillsidewatchers.sdu.sonicoperator.R;
import com.hillsidewatchers.sdu.sonicoperator.ShellCommand.RootShell;

/**
 * Created by lenovo on 2016/3/21.
 */

/**
 * 小白窗悬浮窗口
 */
public class DotService extends Service {
    private static final String DOT_UP = "dot_up";
    private static final String DOT_DOWN = "dot_down";
    private static final String DOT_LEFT = "dot_left";
    private static final String DOT_RIGHT ="dot_right";

    private static final String DOT_PUSH = "dot_push";
    DotServiceReciever dotServiceReciever =null;
     Handler handler  = null;
    private static final String TAG= "DotService";
    LinearLayout layout;
    Button floatingIv;
    WindowManager.LayoutParams wmParams;
    LayoutInflater inflater;
    //创建浮动窗口设置布局参数的对象
    WindowManager windowManager;
    //触摸监听器
    GestureDetector gestureDetector;
    RootShell rootShell = RootShell.getInstance();
    IntentFilter intentFilter;
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initWindow();//设置窗口的参数
        initFloating();//设置悬浮窗图标
        dotServiceReciever = new DotServiceReciever();
         intentFilter = new IntentFilter();

        intentFilter.addAction("com.hillsidewatchers.sdu.sonicoperator.DotServiceReciever");
        registerReceiver(dotServiceReciever,intentFilter);

//        new Thread(new ClientRunnable(handler)).start();
    }

    private void initWindow() {
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        wmParams = getParams(wmParams);
        wmParams.gravity = Gravity.LEFT|Gravity.TOP;
        wmParams.x = 240;
        wmParams.y = 240;
//        wmParams.x = 160;
//        wmParams.y = 160;
        inflater = LayoutInflater.from(getApplication());
        layout = (LinearLayout) inflater.inflate(R.layout.dot_layout,null);
        windowManager.addView(layout,wmParams);
    }

    private WindowManager.LayoutParams getParams(WindowManager.LayoutParams wmParams) {
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN|
                WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR|
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        //设置悬浮窗口的长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        return wmParams;

    }

    private int touchStartX,touchStartY,touchCurrentX,touchCurrentY;
    private int startX,startY,stopX,stopY;
    private boolean isMove;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);

    }

    private void initFloating() {
        floatingIv = (Button) layout.findViewById(R.id.floating_imageView);
        floatingIv.setOnTouchListener(new FloatingListener());
        gestureDetector = new GestureDetector(this,new FloatOnGestureListener());
        //设置监听器
        floatingIv.setOnTouchListener(new FloatingListener());
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0X112:
                        Log.e(TAG,"push");
//pull
//                    move(250,);
                        break;
                    case 0x113:
//push
                        Log.e(TAG,"pull");
                        break;
                    case 0x114:
//left
                        Log.e(TAG,"left");
                        move(-250,0);
                        break;
                    case 0x115:
//right
                        Log.e(TAG,"right");
                        move(250,0);
                        break;
                    case 0x116:
//up
                        Log.e(TAG,"up");
                        move(0,-250);
                        break;
                    case 0x117:
//down
                        Log.e(TAG,"down");
                        move(0,250);
                        break;
                }
            }
        };
    }
    private void move(int move_x,int move_y){
        for(int i=0;i<10;i++){
            wmParams.x+=move_x/10;
            wmParams.y+=move_y/10;
            windowManager.updateViewLayout(layout,wmParams);
        }

    }
    private class FloatingListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    isMove = false;
                    touchStartX = (int) event.getRawX();
                    touchStartY = (int) event.getRawY();
                    startX = (int) event.getX();
                    startY = (int) event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchCurrentX = (int) event.getRawX();
                    touchCurrentY = (int) event.getRawY();
                    wmParams.x += touchCurrentX-touchStartX;
                    wmParams.y += touchCurrentY-touchStartY;
                    windowManager.updateViewLayout(layout,wmParams);
                    touchStartX = touchCurrentX;
                    touchStartY = touchCurrentY;
                    break;
                case MotionEvent.ACTION_UP:
                    stopX = (int) event.getX();
                    stopY = (int) event.getY();
                    if(Math.abs(startX-stopX)>=1||Math.abs(startY-stopY)>=1){
                        isMove = true;
                    }
                    break;
            }
            return gestureDetector.onTouchEvent(event);
        }
    }
    class FloatOnGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if(!isMove){
                Toast.makeText(getApplicationContext(), "你点击了悬浮窗", Toast.LENGTH_SHORT).show();
                System.out.println("onclick");
            }
            return super.onSingleTapConfirmed(e);
        }
    }
    public class DotServiceReciever extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG,"DotServiceReciever已经收到消息");
            String direction = (String) intent.getExtras().get("direction");
            if(direction.equals(DOT_UP)){
//                move(0, -300);
                move(0,-310);
            }else if(direction.equals(DOT_DOWN)){
//                move(0, 300);
                move(0,310);
            }
            else if(direction.equals(DOT_LEFT)){
//                move(-260, 0);
                move(-340,0);
            }
            else if(direction.equals(DOT_RIGHT)){
//                move(260, 0);
                move(340,0);
            }else if(direction.equals(DOT_PUSH)){
                Log.e(TAG,"reciever已经收到PUSH");
                int x = wmParams.x+30;
                int y = wmParams.y+30;

                String command = "input tap "+x+" "+y;
                Message message = new Message();
                message.what=0x777;
                message.obj = command;
                rootShell.handler.sendMessage(message);
            }
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(dotServiceReciever);
        floatingIv.setVisibility(View.GONE);
        super.onDestroy();
    }
}
