package com.hillsidewatchers.sdu.sonicoperator.Analyzer;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	private static String path_test = "",
			path_model  = "",
			path_out="" ;
//

	public static void main(String[] args) throws IOException{

	      String[] parg = {

	    		  	"/home/elvis/final_svm", // 这个是存放测试数据
					"/home/elvis/sample.data.model", // 调用的是训练以后的模型
					"/home/elvis/out" }; // 生成的结果的文件的路径
			System.out.println("........SVM运行开始..........");
			svm_predict p = new svm_predict();
			calculator(null);
	}
	public static int calculator(double [] info) throws IOException{
		path_test = Environment.getExternalStorageDirectory().getAbsolutePath()+"/sonic/test" ;
				path_model = Environment.getExternalStorageDirectory().getAbsolutePath()+"/sonic/temp.file.model" ;
				path_out = Environment.getExternalStorageDirectory().getAbsolutePath()+"/sonic/out";
		  File file = new File(path_test);
		  if(file.exists()){
			  file.delete();
		  }else{
			  if(!file.createNewFile()){
				  Log.e("FILE NOT CREATE ." , "Error for here.");
			  }
			  return  -1;
		  }
		FileWriter fileWritter = new FileWriter(file.getName(),true);
		BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		for (int i = 0 ; i < info.length ; i++){
			bufferWritter.write((i+1)+":"+info[i]+" ");
		}
		bufferWritter.close();
		String[] parg = {     path_test	    // 这个是存放测试数据
				  			, path_model	// 调用的是训练以后的模型
				  			, path_out   }; // 生成的结果的文件的路径
	      svm_predict p = new svm_predict();
		  p.main(parg);
	      Scanner scanner = new Scanner(new File(path_out));
	      double result =  -1;
	      while(scanner.hasNextDouble()){
	    	  result = scanner.nextDouble();
	    	  System.out.println(result);
	      }
	      if(result < 0) throw new IOException();
	      return (int)(result+0.5);
	}
}
