package com.hillsidewatchers.sdu.sonicoperator.Analyzer;

import android.util.Log;

import com.hillsidewatchers.sdu.sonicoperator.Utils.HexToDecimal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/3/28.
 */
public class InputInfoListAnalyzer {
    /**
     * infoList第二个段的三中情况
     */
    private static final String TAG = "InputInfoListAnalyzer";
    private static final String ABS_MT_TRACKING_ID = "ABS_MT_TRACKING_ID";
    private static final String ABS_MT_POSITION_X = "ABS_MT_POSITION_X";
    private static final String ABS_MT_POSITION_Y = "ABS_MT_POSITION_Y";
    private static final String KEY_HOME = "KEY_HOME";
    private static final String KEY_BACK = "KEY_BACK";
    private static final String KEY_MENU = "KEY_MENU";
    /**
     *每次动作的起始点
     *
     */
    int count =0;
    private String start_x;
    private String start_y;
    private String end_x;
    private String end_y;
    List<String> infoList = new ArrayList<>();
    String actionListString = "";
    String packageName;
    HexToDecimal hexToDecimal = HexToDecimal.getInstance();
    public void setInputInfoList(List<String> infoList) {
        for(int i=0;i<infoList.size();i++){

            this.infoList.add(infoList.get(i));
        }
        actionListString= DealInfoList(this.infoList);
    }
    public String DealInfoList(List<String> list){
        for(int i=0;i<list.size();i++){
            Log.e(TAG,list.get(i));
            if(list.get(i).indexOf(".")!=-1){
                packageName = list.get(i).trim();
            }
            else {
                String item = list.get(i);
                if(item!=null&&!item.equals("")){
                    String[] temp_parts = item.trim().split(" ");
                    if(temp_parts.length==3){
                        if(temp_parts[1].trim().indexOf("KEY")!=-1){
                            Log.e(TAG,temp_parts[1]);
                            String info;
                            switch (temp_parts[1].trim()){
                                case KEY_BACK:
                                    info = "keyevent 4";
                                    i=i+3;
                                    Log.e(TAG,info);
                                    actionListString+=add(info);
                                    break;
                                case KEY_HOME:
                                    info = "keyevent 3";
                                    i=i+3;
                                    Log.e(TAG,info);
                                    actionListString+=add(info);
                                    break;
                                case KEY_MENU:
                                    info = "keyevent 1";
                                    i=i+3;
                                    Log.e(TAG,info);
                                    actionListString+=add(info);
                                    break;
                                default:
                                    break;
                            }
                        }
                        else if(temp_parts[1].trim().equals(ABS_MT_TRACKING_ID)){
                            /**
                             * 直到读到下一个ID为ffffffff的时候
                             */
                            start_x = "";
                            start_y = "";
                            end_x = "";
                            end_y = "";
                            int j=0;
                            for( j=i+1;j<list.size();j++){
                                String itemID = list.get(j);
                                if(item!=null&&!item.equals("")) {
                                    String[] temp_partsID = itemID.trim().split(" ");
                                    Log.e(TAG,TAG+"==="+itemID);
                                    if(temp_partsID.length==3){
                                        switch (temp_partsID[1].trim()){
                                            case ABS_MT_POSITION_X:
                                                if(start_x==null||start_x.equals("")){
                                                    start_x = temp_partsID[2];
                                                }
                                                else {
                                                    end_x = temp_partsID[2];
                                                }
                                                break;
                                            case ABS_MT_POSITION_Y:
                                                if(start_y==null||start_y.equals("")){
                                                    start_y = temp_partsID[2];
                                                }
                                                else {
                                                    end_y = temp_partsID[2];
                                                }
                                                break;
                                            case ABS_MT_TRACKING_ID:
                                                count++;
                                                Log.e(TAG,"检测到ID"+count+"次");
                                                i=j+2;
                                                j = list.size();
                                                break;
                                            default:
                                                continue;
                                        }
                                    }

                                }

                            }
                            if((end_x==null||end_x.equals(""))&&(end_y==null||end_y.equals(""))){
                                //tap
                                start_x = hexToDecimal.getDemicalFromHex(start_x);
                                start_y = hexToDecimal.getDemicalFromHex(start_y);
                                String info = "tap "+start_x+ " "+start_y;
                                Log.e(TAG,info);
                                actionListString+=add(info);
                                start_x = "";
                                start_y = "";
                                end_x = "";
                                end_y = "";
                            }else {
                                //swipe
                                if(end_x!=null&&!end_x.equals("")&&end_y!=null&&!end_y.equals("")){

                                    start_x = hexToDecimal.getDemicalFromHex(start_x);
                                    start_y = hexToDecimal.getDemicalFromHex(start_y);
                                    end_x = hexToDecimal.getDemicalFromHex(end_x);
                                    end_y = hexToDecimal.getDemicalFromHex(end_y);
                                    String info = "swipe "+start_x+" "+start_y+" "+end_x+" "+end_y;
                                    Log.e(TAG,info);
                                    actionListString+=add(info);
                                }
                                else if((end_x==null||end_x.equals(""))){
                                    end_x = start_x;
                                    start_x = hexToDecimal.getDemicalFromHex(start_x);
                                    start_y = hexToDecimal.getDemicalFromHex(start_y);
                                    end_x = hexToDecimal.getDemicalFromHex(end_x);
                                    end_y = hexToDecimal.getDemicalFromHex(end_y);
                                    String info = "swipe "+start_x+" "+start_y+" "+end_x+" "+end_y;
                                    Log.e(TAG,info);
                                    actionListString+=add(info);
                                }
                                else if(end_y==null||end_y.equals("")){
                                    end_y = start_y;
                                    start_x = hexToDecimal.getDemicalFromHex(start_x);
                                    start_y = hexToDecimal.getDemicalFromHex(start_y);
                                    end_x = hexToDecimal.getDemicalFromHex(end_x);
                                    end_y = hexToDecimal.getDemicalFromHex(end_y);
                                    String info = "swipe "+start_x+" "+start_y+" "+end_x+" "+end_y;
                                    Log.e(TAG,info);
                                    actionListString+=add(info);
                                }
                                start_x = "";
                                start_y = "";
                                end_x = "";
                                end_y = "";
                            }
                        }
                    }

                }
            }
        }
        return actionListString;
    }
    public String getactionListString(){
        if(actionListString==null||actionListString.equals("")){
            Log.e(TAG,"actionListString为空");
        }
        Log.e(TAG,"============"+actionListString);
//        String []temp = actionListString.split("\\|");
//        Log.e(TAG,String.valueOf(temp.length));
//        if(temp.length>=1){
//            String []temp2 = new String[temp.length-1];
//            actionListString = "";
//            for(int i=0;i<temp.length-1;i++){
//                if(!temp[i].trim().equals("")){
//                    actionListString+=(temp[i]+"|");
//                }
//            }
//        }


        Log.e(TAG,actionListString);
        return actionListString;
    }
    public String add(String action){
        String temp = "("+action+")|";
        return temp;
    }
    public String getPackageName(){
        if(packageName==null){
            Log.e(TAG,"packageName为空");
        }
        return packageName;
    }
}
