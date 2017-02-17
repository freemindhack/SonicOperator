package com.hillsidewatchers.sdu.sonicoperator.Utils;

import android.util.Log;

/**
 * Created by lenovo on 2016/3/11.
 * 十六进制转十进制
 */
public class HexToDecimal {
    private int demical =0;
    String oldHex ="";
    private static HexToDecimal hexToDecimal = new HexToDecimal();
    public static HexToDecimal getInstance(){
        return hexToDecimal;
    }
    //传入一个字符串，将其转化为十进制的数
    public String getDemicalFromHex(String hex){
       oldHex = hex;
        if(hex.indexOf('.')!=-1){
            hex = oldHex.trim();
        }
        String realHex = getNumString(hex);
        if(hex.equals("")){
            return String.valueOf(0);
        }
        else {
            Log.e("getDemicalFromHex",realHex.trim());
            int temp = Integer.parseInt(realHex.trim(), 16);

            return String.valueOf(temp);
        }
    }
    public String getNumString(String hex){
        int j=-1;
        for(int i=0;i<hex.length();i++){
            if(hex.charAt(i)!='0'){
                j = i;//开始有效数字的字符串的位置
                break;
            }
        }
        if(j==-1){
            return "";
        }
        return hex.substring(j,hex.length());
    }
}
