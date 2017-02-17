package com.hillsidewatchers.sdu.sonicoperator.ViewSurface;

import android.content.Context;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.hillsidewatchers.sdu.sonicoperator.R;
import com.hillsidewatchers.sdu.sonicoperator.ShellCommand.RootShell;

/**
 * Created by lenovo on 2016/3/28.
 */
public class FragmentSwitch extends View implements View.OnTouchListener {
    private static final String TAG = "SliderSwitch";
    RootShell rootShell = RootShell.getInstance();
    /*
    *上下文
     */
    private Context context;
    /*
    按下时的x按钮
     */
    private int downX;
    //当前位置
    private int lastX;
    //手指移动的距离
    private int dis;
    private boolean oldSwitchState = false;
    //底层背景
    private Bitmap bgBmp;
    //上层按钮背景
    private Bitmap btnBmp;
    //画笔
    private Paint paint;
    //开关状态
    private boolean switchState = false;
    //上层按钮距左边界的距离
    private int btnLeft =0;
    //最大滑动距离
    private int juli;
    private int aWidth;
    private int aHeight;
    //按钮上按钮的宽
    private int btnWidth;

    IntentFilter intentFilter;

    public FragmentSwitch(Context context) {
        super(context);
    }
    public FragmentSwitch(Context context, AttributeSet attrs){
        super(context,attrs);

        this.context = context;
        init();
        initAttrs(attrs);

    }
    /*
        测量控件大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(aWidth, aHeight);
    }
    /*
        绘组件
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //记录一下state，如果state是什么就画画什么

        canvas.drawBitmap(bgBmp, 0, 0, paint);
        canvas.drawBitmap(btnBmp, btnLeft, 0, paint);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    /*
        初始化
         */
    private void init() {
        //画笔
        paint = new Paint();
        //开启抗锯齿
        paint.setAntiAlias(true);
        setOnTouchListener(this);
    }
    /*
    获取xml配置文件中的属性值
    */
    private void initAttrs(AttributeSet attrs) {
        TypedArray te = getContext().obtainStyledAttributes(attrs, R.styleable.SliderSwitch);
        //获取xml中图片的ID
        int bgId = te.getResourceId(R.styleable.SliderSwitch_abackground,0);
        int btnbgID = te.getResourceId(R.styleable.SliderSwitch_btn_background,0);
        //根据ID获取图片
        bgBmp = BitmapFactory.decodeResource(getResources(), bgId);
        btnBmp = BitmapFactory.decodeResource(getResources(),btnbgID);
        //获取控件的尺寸信息
        aWidth = (int) te.getDimension(R.styleable.SliderSwitch_awidth,0f);
        aHeight = (int) te.getDimension(R.styleable.SliderSwitch_aheight,0f);
        btnWidth = (int) te.getDimension(R.styleable.SliderSwitch_btn_width,0f);
        //如果awight属性未配置
        if(aWidth==0f){
            aWidth = bgBmp.getWidth();
        }
        //如果aheight属性未配置
        if(aHeight==0f){
            aHeight = bgBmp.getHeight();
        }
        //如果btn_width属性未配置
        if(btnWidth==0f){
            btnWidth = btnBmp.getWidth();
        }
        //按xml中配置的尺寸改变bitmap
        bgBmp = Bitmap.createScaledBitmap(bgBmp,aWidth,aHeight,true);
        btnBmp = Bitmap.createScaledBitmap(btnBmp,btnWidth,aHeight,true);
        //计算最大滑动距离
        juli = aWidth-btnWidth;
        te.recycle();
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                downX = lastX = (int) event.getX();
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                //计算手指在屏幕上的滑动距离
                dis = (int) (event.getX()-lastX);
                lastX = (int) event.getX();
                //根据移动距离计算左边距值
                btnLeft = btnLeft+dis;
                change();
                break;
            }
            case MotionEvent.ACTION_UP:{
                lastX = (int) event.getX();
                changeState();

                break;
            }
        }
        return true;
    }

    private void changeState() {
        //抬起时坐标与按下时坐标相对距离
        int x = lastX-downX;
        //如果是点击，距离小于1即可视为点击
        if((x>-1&&x<=0)||x<1&&x>=0){
            Log.e(TAG, "点击");
            if(switchState){
                btnLeft =0;

            }
            else {
                btnLeft = juli;
            }
            switchState = !switchState;
            oldSwitchState = switchState;
        }
        //如果滑动距离大于最大距离的一半，则自动完成状态转换,否则状态不变
        if(btnLeft>=juli/2){
            btnLeft = juli;
            oldSwitchState = switchState;
            switchState = true;
        }else if(btnLeft<juli/2){
            btnLeft =0;
            oldSwitchState = switchState;
            switchState = false;
        }
        if(listener!=null){
            listener.onChange(this,switchState);
        }
        invalidate();
        dis=0;

    }
    private OnChangeListener listener = null;
    /*
    改变按钮的位置
     */
    private void change() {
        if(btnLeft>juli){
            btnLeft = juli;
        }
        if(btnLeft<0){
            btnLeft =0;
        }
        invalidate();
    }
    public void setOnChangeListener(OnChangeListener listener){
        this.listener = listener;
    }
    public interface OnChangeListener{
        public void onChange(FragmentSwitch sw,boolean state);
    }

}
