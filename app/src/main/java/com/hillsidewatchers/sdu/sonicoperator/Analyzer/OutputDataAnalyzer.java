package com.hillsidewatchers.sdu.sonicoperator.Analyzer;

import android.util.Log;

import com.hillsidewatchers.sdu.sonicoperator.ShellCommand.RootShell;


/**
 * Created by lenovo on 2016/3/28.
 */
public class OutputDataAnalyzer  {
    private static final String TAG = "OutputDataAnalyzer";
    String outputData = "";
    RootShell rootShell = null;
    public OutputDataAnalyzer(){
        rootShell = RootShell.getInstance();
    }
    public void outputData(String outputData) {
        this.outputData = outputData;
        DealOutputData();
    }

    private void DealOutputData() {

        String[]data = outputData.split("\\|");
        Log.e(TAG,"数据的长度"+data.length);
        for(int i=0;i<data.length;i++){
            if(!data[i].trim().equals("")){

                String temp = data[i].trim();
                temp= temp.replace('(',' ');
                temp= temp.replace(',',' ');
                temp=  temp.replace(')',' ');
                Log.e(TAG, temp);
                OsCommand(temp);
                Log.e(TAG, String.valueOf(i));
            }

        }


    }
    public void OsCommand(String command){
        String COMMAND ="input "+ command+"\n";
        Log.e(TAG,COMMAND);
        rootShell.OStream(COMMAND);
    }

}
