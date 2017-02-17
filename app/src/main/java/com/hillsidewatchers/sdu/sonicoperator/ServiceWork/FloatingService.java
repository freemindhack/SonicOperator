package com.hillsidewatchers.sdu.sonicoperator.ServiceWork;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hillsidewatchers.sdu.sonicoperator.R;
import com.hillsidewatchers.sdu.sonicoperator.ViewSurface.SliderSwitch;

/**
 * Created by lenovo on 2016/3/13.
 */
public class FloatingService extends Service{
    /*
    定义浮动窗口布局
     */
    LinearLayout layout;
//    SliderSwitch floatingIv;
    WindowManager.LayoutParams wmParams;
    LayoutInflater inflater;
    //创建浮动窗口设置布局参数的对象
    WindowManager windowManager;
    //触摸监听器
    GestureDetector gestureDetector;
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        initWindow();//设置窗口的参数
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initFloating();//设置悬浮窗图标
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(layout!=null){
            windowManager.removeView(layout);
        }
    }

    private void initFloating() {
//        floatingIv.getBackground().setAlpha(150);
//        gestureDetector = new GestureDetector(this,new FloatOnGestureListener());
//        //设置监听器
//        floatingIv.setOnTouchListener(new FloatingListener());
    }
    private int touchStartX,touchStartY,touchCurrentX,touchCurrentY;
    private int startX,startY,stopX,stopY;
    private boolean isMove;
    private void initWindow() {
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        wmParams = getParams(wmParams);
        wmParams.gravity = Gravity.LEFT|Gravity.TOP;
        wmParams.x = 50;
        wmParams.y = 50;
        inflater = LayoutInflater.from(getApplication());
        layout = (LinearLayout) inflater.inflate(R.layout.floating_layout,null);
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
//    private class FloatingListener implements View.OnTouchListener{
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            int action = event.getAction();
//            switch (action){
//                case MotionEvent.ACTION_DOWN:
//                    isMove = false;
//                    touchStartX = (int) event.getRawX();
//                    touchStartY = (int) event.getRawY();
//                    startX = (int) event.getX();
//                    startY = (int) event.getY();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    touchCurrentX = (int) event.getRawX();
//                    touchCurrentY = (int) event.getRawY();
//                    wmParams.x += touchCurrentX-touchStartX;
//                    wmParams.y += touchCurrentY-touchStartY;
//                    windowManager.updateViewLayout(layout,wmParams);
//                    touchStartX = touchCurrentX;
//                    touchStartY = touchCurrentY;
//                    break;
//                case MotionEvent.ACTION_UP:
//                    stopX = (int) event.getX();
//                    stopY = (int) event.getY();
//                    if(Math.abs(startX-stopX)>=1||Math.abs(startY-stopY)>=1){
//                        isMove = true;
//                    }
//                    break;
//            }
//            return gestureDetector.onTouchEvent(event);
//        }
//    }
//    class FloatOnGestureListener extends GestureDetector.SimpleOnGestureListener{
//        @Override
//        public boolean onSingleTapConfirmed(MotionEvent e) {
//            if(!isMove){
//                Toast.makeText(getApplicationContext(),"你点击了悬浮窗",Toast.LENGTH_SHORT).show();
//                System.out.println("onclick");
//            }
//            return super.onSingleTapConfirmed(e);
//        }
//    }
}
