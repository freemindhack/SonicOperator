package com.hillsidewatchers.sdu.sonicoperator.ShellCommand;

import android.util.Log;

import com.hillsidewatchers.sdu.sonicoperator.Analyzer.EventKeyValue;

/**
 * Created by lenovo on 2016/3/12.
 */
public class EventSender {
    RootShell rootShell = null;

    private static EventSender eventSender = new EventSender();
    public static EventSender getInstance(){

        return eventSender;
    }
    public static final int KEYCODE_HOME =3;//HOME键
    public static final int KEYCODE_BACK = 4;//返回键
    public static final int KEYCODE_CALL = 5;//拨号键
    public static final int KEYCODE_ENDCALL = 6;//挂机键
    //0-9
    public static final int KEYCODE_0=7;
    public static final int KEYCODE_1=8;
    public static final int KEYCODE_2=9;
    public static final int KEYCODE_3=10;
    public static final int KEYCODE_4=11;
    public static final int KEYCODE_5=12;
    public static final int KEYCODE_6=13;
    public static final int KEYCODE_7=14;
    public static final int KEYCODE_8=15;
    public static final int KEYCODE_9=16;
    //*和#号键
    public static final int KEYCODE_STAR=17;
    public static final int KEYCODE_POUND=18;
    public static final int KEYCODE_DPAD_UP=19;
    //导航键
//    public static final int KEYCODE_DPAD_DOWN = 20;
//    public static final int KEYCODE_DPAD_LEFT = 21;
//    public static final int KEYCODE_DPAD_RIGHT = 22;
//    public static final int KEYCODE_DPAD_CENTER = 23;
    //音量键
    public static final int KEYCODE_VOLUME_UP = 24;//音量+
    public static final int KEYCODE_VOLUME_DOWN = 25;//音量—

    public static final int KEYCODE_POWER = 26;//电源键

//    public static final int KEYCODE_CAMERA = 27;
//    public static final int KEYCODE_CLEAR = 28;
    public static final int KEYCODE_MENU = 82;//菜单键
    public static final int KEYCODE_NOTIFICATION = 83;
    public static final int KEYCODE_SEARCH = 84;
//A-Z
    public static final int KEYCODE_A=29;
    public static final int KEYCODE_B=30;
    public static final int KEYCODE_C=31;
    public static final int KEYCODE_D=32;
    public static final int KEYCODE_E=33;
    public static final int KEYCODE_F=34;
    public static final int KEYCODE_G=35;
    public static final int KEYCODE_H=36;
    public static final int KEYCODE_I=37;
    public static final int KEYCODE_J=38;
    public static final int KEYCODE_K=39;
    public static final int KEYCODE_L=40;
    public static final int KEYCODE_M=41;
    public static final int KEYCODE_N=42;
    public static final int KEYCODE_O=43;
    public static final int KEYCODE_P=44;
    public static final int KEYCODE_Q=45;
    public static final int KEYCODE_R=46;
    public static final int KEYCODE_S=47;
    public static final int KEYCODE_T=48;
    public static final int KEYCODE_U=49;
    public static final int KEYCODE_V=50;
    public static final int KEYCODE_W=51;
    public static final int KEYCODE_X=52;
    public static final int KEYCODE_Y=53;
    public static final int KEYCODE_Z=54;
//标点符号
    public static final int KEYCODE_COMMA=55;//，
    public static final int KEYCODE_PERIOD=56;//。

    public static final int KEYCODE_ALT_LEFT=57;
    public static final int KEYCODE_ALT_RIGHT=58;
    public static final int KEYCODE_SHIFT_LEFT=59;
    public static final int KEYCODE_SHIFT_RIGHT=60;

    public static final int KEYCODE_TAB=61;
    public static final int KEYCODE_SPACE=62;//空格
    public static final int KEYCODE_SYM=63;//选择输入方式
    public static final int KEYCODE_EXPLORER=64;//搜索
    public static final int KEYCODE_ENVELOPE=65;//
    public static final int KEYCODE_ENTER = 66;
    public static final int KEYCODE_DEL = 67;//退格键
    public static final int KEYCODE_ESCAPE =111;//ESC键
    public static final int KEYCODE_MOVE_HOME = 122;//光标移动到开始键
    public static final int KEYCODE_MOVE_END = 123;//光标移动到末尾键
    public static final int KEYCODE_PAGE_UP = 92;//向上翻页
    public static final int KEYCODE_PAGE_DOWN = 93;//向下翻页

    public static final String COMMAND_INPUT_KEY = "input keyevent ";
    public static final String COMMAND_INPUT_SWIPE = "input swipe ";
    public static final String COMMAND_INPUT_TAP = "input tap ";
    private static final String TAG = "EventSender";
    public void inputTap(int tap_x_start,int tap_y_start){
        if(rootShell==null){
            Log.e(TAG,"得到rootShell的实例1");
            rootShell = RootShell.getInstance();
        }
        String COMMAND_TAP = COMMAND_INPUT_TAP+tap_x_start+" "+tap_y_start;
        Log.e("滑动的时候的命令", COMMAND_TAP);
        rootShell.OStream(COMMAND_TAP + "\n");

    }
    public void inputSwipe(int x_start,int y_start,int x_end,int y_end){
        if(rootShell==null){
            Log.e(TAG,"得到rootShell的实例2");
            rootShell = RootShell.getInstance();
        }
        String COMMAND_SWIPE = COMMAND_INPUT_SWIPE+x_start+" "+y_start+" "+x_end+" "+y_end;
        Log.e("滑动的时候的命令", COMMAND_SWIPE);
        rootShell.OStream(COMMAND_SWIPE + "\n");

    }
    public void inputKey(String command){
        if(rootShell==null){
            Log.e(TAG,"得到rootShell的实例3");
            rootShell = RootShell.getInstance();
        }
        String COMMAND_KEY =  "";
        if(command.equals(EventKeyValue.KEY_HOME)){
            COMMAND_KEY = COMMAND_INPUT_KEY+KEYCODE_HOME;
        }
        else if(command.equals(EventKeyValue.KEY_BACK)){
            COMMAND_KEY = COMMAND_INPUT_KEY+KEYCODE_BACK;
        }
        else if(command.equals(EventKeyValue.KEY_MENU)){
            COMMAND_KEY = COMMAND_INPUT_KEY + KEYCODE_MENU;
        }
        Log.e("滑动的时候的命令",COMMAND_KEY);
        rootShell.OStream(COMMAND_KEY+"\n");

    }
}
