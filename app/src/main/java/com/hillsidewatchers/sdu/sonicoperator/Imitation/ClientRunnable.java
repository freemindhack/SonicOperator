//package com.hillsidewatchers.sdu.sonicoperator.Imitation;
//
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.Socket;
//
///**
// * Created by lenovo on 2016/3/22.
// */
//public class ClientRunnable implements Runnable{
//    private static final String PUSH = "push";
//    private static final String PULL = "pull";
//    private static final String LEFT = "left";
//    private static final String RIGHT = "right";
//    private static final String UP = "up";
//    private static final String DOWN = "down";
//    private static final String DOT_UP = "dot_up";
//    private static final String DOT_DOWN = "dot_down";
//    private static final String DOT_LEFT = "dot_left";
//    private static final String DOT_RIGHT ="dot_right";
//    private static final String DOT_PUSH = "dot_push";
//    private static final String TAG = "ClientThread";
////    private static final String HOST = "192.168.1.213";
////    private static final int POST = 15006;
//private static final String HOST = "121.250.223.242";
//    private static final int POST = 6689;
//    private  Socket client;
//    OutputStream outputStream;
//    BufferedReader bufferedReader ;
//    Handler handler;
//
//    public ClientRunnable(Handler handler){
//        this.handler = handler;
//    }
//    @Override
//    public void run() {
//        String s ;
//        try {
//            Log.e(TAG,"aa");
//            client = new Socket(HOST,POST);
//            outputStream = client.getOutputStream();
//            Log.e(TAG,"接收端已经连上服务器");
//            outputStream.write("get\n".getBytes());
//            Message message = new Message();
//            bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
//            while ((s = bufferedReader.readLine())!=null){
//                Log.e(TAG,"已经读入数据");
//                Log.e(TAG,s);
//                if (s.equals("push")){
//                    //存入数据库
//                    message.what=0x112;
//                    handler.sendEmptyMessage(message.what);
//                    Log.e(TAG, "get" + "push");
//                }else if (s.equals("pull")){
//                    message.what=0x113;
//                    handler.sendEmptyMessage(message.what);
//                    Log.e(TAG, "get" + "pull");
//                }else if (s.equals("left")){
//                    message.what=0x114;
//                    handler.sendEmptyMessage(message.what);
//                    Log.e(TAG, "get" + "left");
//                }else if (s.equals("right")){
//                    message.what=0x115;
//                    handler.sendEmptyMessage(message.what);
//                    Log.e(TAG, "get" + "right");
//                }else if (s.equals("up")){
//                    message.what=0x116;
//                    handler.sendEmptyMessage(message.what);
//                    Log.e(TAG, "get" + "up");
//                }else if (s.equals("down")){
//                    message.what=0x117;
//                    handler.sendEmptyMessage(message.what);
//                    Log.e(TAG, "get" + "down");
//                }else if(s.equals(DOT_UP)){
//                    message.what = 0x118;
//                    handler.sendEmptyMessage(message.what);
//                    Log.e(TAG, DOT_UP);
//                }
//                else if(s.equals(DOT_DOWN)){
//                    message.what = 0x119;
//                    handler.sendEmptyMessage(message.what);
//                    Log.e(TAG,DOT_DOWN);
//                }
//                else if(s.equals(DOT_LEFT)){
//                    message.what = 0x120;
//                    handler.sendEmptyMessage(message.what);
//                    Log.e(TAG, DOT_LEFT);
//                }
//                else if(s.equals(DOT_RIGHT)){
//                    message.what = 0x121;
//                    handler.sendEmptyMessage(message.what);
//                    Log.e(TAG, DOT_RIGHT);
//                }else if(s.equals(DOT_PUSH)){
//                    message.what = 0x130;
//                    handler.sendEmptyMessage(message.what);
//                    Log.e(TAG,DOT_PUSH);
//                }
//                s = "";
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
