package com.hillsidewatchers.sdu.sonicoperator.ViewSurface;

/**
 * 这个类修改过
 */

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hillsidewatchers.sdu.sonicoperator.Adapter.Gesture_BaseAdapter;
import com.hillsidewatchers.sdu.sonicoperator.Analyzer.OutputDataAnalyzer;
import com.hillsidewatchers.sdu.sonicoperator.DatabaseManager.DBSQLiteDao;
//import com.hillsidewatchers.sdu.sonicoperator.Imitation.ClientRunnable;
import com.hillsidewatchers.sdu.sonicoperator.R;
import com.hillsidewatchers.sdu.sonicoperator.ServiceWork.DotService;
//import com.hillsidewatchers.sdu.sonicoperator.ShellCommand.RootShell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment3 extends Fragment {
	private static final String DOT_UP = "dot_up";
	private static final String DOT_DOWN = "dot_down";
	private static final String DOT_LEFT = "dot_left";
	private static final String DOT_RIGHT ="dot_right";
	private static final String DOT_PUSH = "dot_push";
	private static final String mobile_operation = "mobile_operation";
	private static final String sonic_operation = "sonic_operation";
	private static final String connection = "connection";
	private static final String TAG = "Fragment3";
	private List<Map<String,Object>> gestureList = new ArrayList<>();
	private View fragmentview;
	private LinearLayout dialog_view;
	private ListView listView;
	private EditText editText;
	private Gesture_BaseAdapter gesture_baseAdapter;
	boolean flag= true;
	private DBSQLiteDao dbsqLiteDao = null;
	FragmentSwitch sw_open_dot;
	private List<Map<String,String>> connectionMapList = null;
	private List<Map<String,String>> gestureMapList = null;
	AlertDialog.Builder builder ;
	private boolean btnStateOpen = true;
	ActivityManager activityManager = null ;
	Intent intent1 = null;
	boolean firstOnCreate = true;
	boolean first = true;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 0X112:
					Log.e(TAG, "push");
//pull
//					DoAction("push");
					if(btnStateOpen){
						storeGestureToDatabase("push");
					}else {
						DoAction("push");

					}

					break;
				case 0x113:
//push
					if(btnStateOpen){
						storeGestureToDatabase("pull");
					}else {
						DoAction("pull");
					}
//					DoAction("push");
					Log.e(TAG, "pull");
					break;
				case 0x114:
//left
					if(btnStateOpen){
						storeGestureToDatabase("left");
					}else {
						DoAction("left");
					}
//					DoAction("left");
					Log.e(TAG, "left");
					break;
				case 0x115:
//right
					if(btnStateOpen){
						storeGestureToDatabase("right");
					}else {
						DoAction("right");
					}
//					DoAction("right");
					Log.e(TAG, "right");
					break;
				case 0x116:
//up
					if(btnStateOpen){
						storeGestureToDatabase("up");
					}
					else {
						DoAction("up");
					}
//					DoAction("up");
					Log.e(TAG, "up");
					break;
				case 0x117:
//down
					if(btnStateOpen){
						storeGestureToDatabase("down");
					}else {
						DoAction("down");
					}
//					DoAction("down");
					Log.e(TAG, "down");
					break;
				case 0x118:
					Log.e(TAG, DOT_UP);
					intent1.putExtra("direction",DOT_UP);
					getActivity().sendBroadcast(intent1);
					break;
				case 0x119:
					Log.e(TAG, DOT_DOWN);
					intent1.putExtra("direction",DOT_DOWN);
					getActivity().sendBroadcast(intent1);
					break;
				case 0x120:
					Log.e(TAG, DOT_LEFT);
					intent1.putExtra("direction",DOT_LEFT);
					getActivity().sendBroadcast(intent1);
					break;
				case 0x121:
					Log.e(TAG, DOT_RIGHT);
					intent1.putExtra("direction",DOT_RIGHT);
					getActivity().sendBroadcast(intent1);
					break;
				case 0x130:
					Log.e(TAG, DOT_PUSH);
					intent1.putExtra("direction",DOT_PUSH);
					getActivity().sendBroadcast(intent1);

				case 0x222:
					Log.e(TAG,"检测到按下222");

					if(msg.obj!=null){
						String []strings = new String[3];
						strings = (String[]) msg.obj;
						updateData(strings[2],strings[1]);
						gesture_baseAdapter.notifyDataSetChanged();
					}
//					deleteData(strings[0]);
					break;

			}
		}
	};
//	ClientRunnable clientRunnable = new ClientRunnable(handler);
	/**
	 *传入的是声波即使声波的属性
	 * 通过声波找到对应之前声波的命名 sonic_operation
	 * 通过声波的名字找到建立的关系的屏幕操作的名字 connection_operation
	 * 通过屏幕操作的名字找到这项操作的具体信息 mobile_operation
	 */
	private void DoAction(String gesture){
		Map<String,String> sonic_map ;
		Map<String,String> connection_map;
		Map<String,String> record_map;
		if(dbsqLiteDao==null){
			dbsqLiteDao = new DBSQLiteDao(getActivity());
		}
		sonic_map = dbsqLiteDao.query(sonic_operation," info = ?",new String[]{gesture});
		if(sonic_map.size()!=0){
			String gesture_name = sonic_map.get("name");
			connection_map = dbsqLiteDao.query(connection," gesture_name = ?",new String[]{gesture_name});
			String record_name = connection_map.get("record_name");
			Toast.makeText(getActivity(),"开始播放从数据库中找到的屏幕操作的属性",Toast.LENGTH_SHORT).show();
			ComponentName cn = activityManager.getRunningTasks(1).get(0).topActivity;
			String packageName = cn.getPackageName();
			Log.e(TAG,"顶层应用的包名是"+packageName);
			//用于点击播放按钮，做播放处理
			record_map = dbsqLiteDao.query(mobile_operation,"packagename=? and name=?",new String[]{packageName,record_name
			});
			if((record_map!=null)&&(record_map.size()!=0)&&!record_map.get("info").equals("")){
				Log.e(TAG,record_map.get("info"));
				OutputDataAnalyzer outputDataAnalyzer = new OutputDataAnalyzer();
				outputDataAnalyzer.outputData(record_map.get("info"));
			}
			else {
				Toast.makeText(getActivity(),"数据库中没有找到该应用下的操作信息，请进行录制",Toast.LENGTH_SHORT).show();
				Log.e(TAG,"从数据库中没有找到这个名字，这个包名的动作");
			}
		}
		else {
			Toast.makeText(getActivity(),"没有查询到之前有过这个手势",Toast.LENGTH_SHORT).show();
		}
	}

	public void storeGestureToDatabase(String gesture) {
		startDialog(gesture);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Log.i("fragment3", "onCreat");

		if(activityManager==null){
			activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
		}
//		if (firstOnCreate){
//			storeGestureToDatabase("push");
//			storeGestureToDatabase("pull");
//			storeGestureToDatabase("left");
//			storeGestureToDatabase("right");
//			storeGestureToDatabase("up");
//			storeGestureToDatabase("down");
//			firstOnCreate = false;
//		}
		}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i("fragment3", "onCreateView");
		fragmentview = inflater.inflate(R.layout.fragment3, container,
				false);
		initView();
		getOnClickListener();
		getGestureListView();
		dbsqLiteDao = new DBSQLiteDao(getActivity());
		return fragmentview;
	}

	private void getOnClickListener() {
		sw_open_dot.setOnChangeListener(new FragmentSwitch.OnChangeListener() {
			@Override
			public void onChange(FragmentSwitch sw, boolean state) {
				Intent intent = new Intent(getActivity(), DotService.class);
				Log.d("switchButton", state ? "开" : "关");
				if (state) {
//					getActivity().startService(intent);
//					intent1 = new Intent();
//					intent1.setAction("com.hillsidewatchers.sdu.sonicoperator.DotServiceReciever");
					btnStateOpen = false;

				} else {
//					getActivity().stopService(intent);
					//
				}
				Toast.makeText(getActivity(), state ? "开" : "关", Toast.LENGTH_SHORT).show();
			}
		});

	}

	private void initView() {
		listView = (ListView) fragmentview.findViewById(R.id.gestureList);
		sw_open_dot = (FragmentSwitch) fragmentview.findViewById(R.id.sw_open_dot);

	}

	private void getGestureListView() {
		gestureList= getData();
		gesture_baseAdapter = new Gesture_BaseAdapter(getActivity(),gestureList,handler);
		listView.setAdapter(gesture_baseAdapter);
	}

	/**
	 *
	 * 先找一遍sonic_operation,找出所有的行,声波的名字
	 * 再找一遍connection_operation，通过sonic_operation中的行找connection中的行，能找到的是匹配了动作的，不能找到的给它一个“匹配一个操作吧”
	 * @return
	 */
	private List<Map<String, Object>> getData() {

		for(int i=gestureList.size()-1;i>=0;i--){
			gestureList.remove(i);
		}
		boolean has_same = false;
		Log.e(TAG, "调用getData");
		if(dbsqLiteDao==null){
			dbsqLiteDao = new DBSQLiteDao(getActivity());
		}
		if(true) {
			gestureMapList = dbsqLiteDao.getMapList(sonic_operation, null, null);
			connectionMapList = dbsqLiteDao.getMapList(connection, null, null);
			for (int i = 0; i < gestureMapList.size(); i++) {
				String gesture_name = gestureMapList.get(i).get("name");
				String gesturename_in_connection = "";
				String record_in_connection = "";
				for (int j = 0; j < connectionMapList.size(); j++) {
					gesturename_in_connection = connectionMapList.get(j).get("gesture_name");
					record_in_connection = connectionMapList.get(j).get("record_name");
					if (gesture_name.equals(gesturename_in_connection)) {
						has_same = true;
						break;
					} else {
						continue;
					}
				}
				if (has_same) {
					addData(gesture_name, record_in_connection);
					has_same = false;
				} else {
					addData(gesture_name, "找一个record吧");
				}

			}
		}
		flag = false;
		return gestureList;
	}

	private void addData(String getstureName,String recordName) {
		Map<String, Object> map = new HashMap<>();
		map.put("gesture_name", getstureName);
		map.put("add_icon", R.drawable.link);
		map.put("record_name", recordName);
		gestureList.add(map);
	}
	private void updateData(String positon,String record){
		gestureList.get(Integer.valueOf(positon)).put("record_name", record);

	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("fragment3", "onPause");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("fragment3", "onDestroy");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Log.i("fragment3", "onDetach");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("fragment3", "onResume");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		Log.i("fragment3", "onStart");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("fragment3", "onStop");
	}
	private void startDialog(final String gesture){
//		ContentValues contentValues = new ContentValues();
//		contentValues.put("name", "push");
//		contentValues.put("info", gesture);
//		flag = dbsqLiteDao.add(sonic_operation, contentValues);
//		getData();
//		gesture_baseAdapter.notifyDataSetChanged();



		builder = new AlertDialog.Builder(getActivity());
		dialog_view = (LinearLayout)View.inflate(getActivity(),R.layout.dialog_text,null);
		editText = (EditText) dialog_view.findViewById(R.id.name_text);
//		builder.setIcon(R.drawable.dialog_sonic);
		builder.setTitle("输入这段手势的名字");
		builder.setView(dialog_view);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getActivity(), "点击确定按钮，为动作制定名字后开始存入数据库", Toast.LENGTH_SHORT).show();
				String name_text = editText.getText().toString();
				if (!name_text.equals("")){
					addData(name_text, "");
					getData();
					gesture_baseAdapter.notifyDataSetChanged();
					Log.e(TAG, name_text);
					ContentValues contentValues = new ContentValues();
					contentValues.put("name", name_text);
					contentValues.put("info", gesture);
					flag = dbsqLiteDao.add(sonic_operation, contentValues);
					getData();
					gesture_baseAdapter.notifyDataSetChanged();
					if (flag) {
						Toast.makeText(getActivity(), "插入数据库成功", Toast.LENGTH_SHORT).show();

					} else {
						//界面报个错
						Toast.makeText(getActivity(), "您存入数据库没有成功，需要重新录制", Toast.LENGTH_SHORT).show();
					}
					name_text = "";
				}
				dialog.dismiss();
			}


		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

//	public void startThread() {
//		new Thread(clientRunnable).start();
//	}
}
