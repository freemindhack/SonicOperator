package com.hillsidewatchers.sdu.sonicoperator.ShellCommand;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hillsidewatchers.sdu.sonicoperator.Analyzer.InputInfoListAnalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/3/11.
 */
public class RootShell {

    private static final String FIRST_COMMAND = "input keyevent 3\n";
    private static final String TAG = "RootShell";
    public static final String COMMAND = "getevent -q -t -l\n";
    public static final String PACKAGE_COMMAND = "dumpsys activity | grep \"mFocusedActivity\"\n";
    public static final String COMMAND_SU = "su";
    public static final String COMMAND_SH = "sh";
    public static final String COMMAND_EXIT = "exit\n";
    public static final String COMMAND_LINE_END = "\n";
    private static Runtime r = null;
    public  OutputStream os;
    private Process process = null;
    BufferedReader inputbr = null;
    BufferedReader errorbf = null;
    String sinput = null;
    String serror = null;
    int iinput = 0;
    int ierror = 0;
    Thread inputThread = null;
    Thread errorThread = null;
    List<String> infoList = new ArrayList<>();
    InputInfoListAnalyzer inputInfoListAnalyzer;
    String packageName;
    private static RootShell rootShell = new RootShell();
//    static Context context = null;
    public static RootShell getInstance(){
//        context = c;
        if(r==null){
            r = Runtime.getRuntime();
        }
        return rootShell;
    }
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x111://录制
                    init();//初始化RunTime,Process,读入流和输出流
                            //获取当前Activity
//                    rebackToFirstHome();
//                    startThreads();
                    getTopAppPackageName();
                    OStream(COMMAND);

                    break;
                case 0x222://停止录制
                    Log.e("已收到", "=");
                    destroy();
                    break;
                case 0x333://播放录制内容
                    Log.e(TAG, "0x333");
                    init();
//                    rebackToFirstHome();
//                    getActionToMobile();
                    if(process==null){
                        //回到主桌面
                        Log.e(TAG,"回到主桌面，process == null");
                    }else {
                        Log.e(TAG,"回到主桌面，process != null");
                    }
                    break;
                case 0x444:
                    break;
                case 0x666:
                    destroy();
                    break;
                case 0x777:
                    init();
                    Log.e(TAG,"rootshell已经收到");
                    String s = msg.obj.toString();
                    Log.e(TAG,s);
                    OStream(s + "\n");
                    Log.e(TAG,"已经输出");
                    break;
            }
        }
    };
    public void getTopAppPackageName(){
        OStream(PACKAGE_COMMAND);
    }
    public void rebackToFirstHome(){
        OStream(FIRST_COMMAND);
        OStream(FIRST_COMMAND);
    }
    public void destroy(){
        Log.e(TAG, "开始毁灭");
//线程
        OStream("exit\n");
        Log.e(TAG,"毁灭流");
        if(process!=null){
            process.destroy();
            process = null;
        }
        Log.e(TAG,"毁灭process");

        Log.e(TAG,"一毁灭线程");
        try {
            if(os!=null){
                os.close();
            }
            else if(inputbr!=null){
                inputbr.close();
            }
            else if(errorbf!=null){
                errorbf.close();
            }
            Log.e(TAG,"毁灭流");
            os = null;
            inputbr = null;
            errorbf = null;

            if(inputThread!=null){
                inputThread.interrupt();
                inputThread = null;
            }
            if(errorThread!=null){
                errorThread.interrupt();
                errorThread = null;
            }



        } catch (IOException e) {
            Log.e(TAG,"出现BUG");
            e.printStackTrace();
        }

        if(infoList.size()!=0){
            for(int i=0;i<infoList.size();i++){
                Log.e(TAG,infoList.get(i));
            }
            inputInfoListAnalyzer = new InputInfoListAnalyzer();
            inputInfoListAnalyzer.setInputInfoList(infoList);
            for(int i=infoList.size()-1;i>=0;i--){
                infoList.remove(i);
//                Log.e(TAG,infoList.get(i));
            }
        }
        Log.e(TAG, "毁灭完毕");
    }

    private void init(){
        if(r==null){
            r = Runtime.getRuntime();
        }
        if(process==null){
            Log.e(TAG,"获取precess");
            try {
                process = r.exec("su");
                if (process==null){
//                    Toast.makeText(context,"获取root权限不成功",Toast.LENGTH_SHORT).show();
                }
                else {
//                    Toast.makeText(context,"获取root权限成功",Toast.LENGTH_SHORT).show();
                    inputbr = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    errorbf = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    os = process.getOutputStream();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {

            inputbr = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorbf = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            os = process.getOutputStream();
        }
        if(inputThread==null){
            Log.e(TAG,"获取流");
            inputThread = new Thread(new InputRunnable());
        }
        if(errorThread ==null){
            errorThread = new Thread(new ErrorRunnable());
        }
        startThreads();
    }

    /**
     * 需要读入数据的时候才需要启动这两个线程
     */
    private void startThreads() {
        if(inputbr!=null&&errorbf!=null){
            if ((!inputThread.isAlive())&&(!errorThread.isAlive())){
                inputThread.start();
                errorThread.start();
            }

        }

    }

    //封装一下输出流吧，
    public void OStream(String Command){
        try {
            if(process==null&&os==null){
                init();
                Log.e(TAG,"os==null");
            }
            os.write(Command.getBytes());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */

    class ErrorRunnable implements Runnable{
        @Override
        public void run() {
            Log.e("Listening==error", "start");
            try {
                while (serror!=null&&(serror = errorbf.readLine()) != null) {
                    ierror++;
                    Log.e("编号error", String.valueOf(ierror)+serror);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class InputRunnable implements Runnable {
        @Override
        public void run() {
            Log.e("Listening==input", "start");
            try {
                if(inputbr!=null){
                    while ((sinput = inputbr.readLine()) != null) {
                        //处理过
                        String temp = DealInputStream(sinput);
                        infoList.add(temp);
                        iinput++;
                        Log.e("编号input", String.valueOf(iinput)+sinput);
                    }
                }
                else {
                    Log.e(TAG,"inputbr为空");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 对inputstream每行数据进行初步的处理，返回有实际意义的字符串
     * @param sinput
     * @return
     */

    private String DealInputStream(String sinput) {
        String sarray[];
        String info;
        String[] info_parts;
        String finalInfo = "";
        if (!sinput.equals("")) {
            if (sinput.trim().contains("mFocusedActivity")) {
                //分解字符串
                sarray = sinput.trim().split(" ");
                String s = sarray[3];
                packageName = s.substring(0, s.indexOf('/'));
                return packageName;
            } else if (sinput.contains("/dev/input/event")) {
                info = sinput.trim().substring(sinput.trim().indexOf(":") + 1, sinput.trim().length());
                info_parts = info.split(" ");

                for (int i = 0; i < info_parts.length; i++) {
                    if(!info_parts[i].trim().equals("")){
                        finalInfo += (info_parts[i].trim() + " ");
                    }
                }
//                for (int i = 0; i < stringArray.length; i++) {
//                    if (stringArray[i].trim().endsWith("]")) {
//                        //时间的字符串
//                        touch_time = Double.valueOf(stringArray[i].trim().substring(1, stringArray[i].length() - 1));
////                Log.e("touch_time",String.valueOf(touch_time));
//                    } else if (stringArray[i].trim().startsWith("/")) {
//                        //设备
//                        devices_String = stringArray[i].trim().substring(0, stringArray[i].length() - 1);
////                Log.e("devices_string",devices_String);
//                    } else if (stringArray[i].trim().startsWith("E")) {
//                        //EV_ABS
//                        type_String = stringArray[i].trim();
////                Log.e("type_String",type_String);
//                    } else if (stringArray[i].trim().indexOf("_") != -1) {
//                        //ABS_MT_
//                        code_String = stringArray[i].trim();
////                Log.e("code_String",code_String);
//                    } else {
//                        if (!stringArray[i].trim().equals("") && !stringArray[i].trim().equals("[")) {
//                            //位置的字符串
//                            touch_value = stringArray[i].trim();
////                    Log.e("touch_value",touch_value);
//                        }
//                    }
            }
        }
        Log.e(TAG,finalInfo);
        packageName = "";
        return finalInfo;
    }
    public String getPackageName(){
        return packageName;
    }
    public InputInfoListAnalyzer getInputInfoListAnalyzer(){
        return inputInfoListAnalyzer;
    }
    public void setInputInfoListAnalyzerNULL(){
        inputInfoListAnalyzer = null;
    }
}
