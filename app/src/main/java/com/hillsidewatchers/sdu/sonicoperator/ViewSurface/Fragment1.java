package com.hillsidewatchers.sdu.sonicoperator.ViewSurface;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.hillsidewatchers.sdu.sonicoperator.DatabaseManager.DBSQLiteDao;
import com.hillsidewatchers.sdu.sonicoperator.R;
import com.hillsidewatchers.sdu.sonicoperator.ServiceWork.FloatingService;
import com.hillsidewatchers.sdu.sonicoperator.ShellCommand.RootShell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment1 extends Fragment {
    PackageManager manager = null;
    private static final String mobile_operation = "mobile_operation";
    private static final String sonic_operation = "sonic_operation";
    private static final String connection = "connection";
    int i = 0;
    RootShell rootShell = RootShell.getInstance();
    private static final String TAG = "Fragment1";
    private static final String FIRST_COMMAND = "input keyevent 3\n";
    private static String warning_toast = "悬浮窗向右滑动开关，开始录制，向左滑动开关停止录制";
    private ImageButton imageButton;
    private Button button;
    private View fragmentview;
    private View dialog_view;
    private EditText editText;
    private ListView listView;
    IntentFilter filter;
    FragmentReceiver fragmentReceiver = null;
    List<Map<String, Object>> recordlist = new ArrayList<>();
    private boolean flag = true;
    DBSQLiteDao dbsqLiteDaoMobile = null;
    List<Map<String, String>> mapList = null;
//    Record_BaseAdapter record_baseAdapter = null;
    SimpleAdapter adapter = null;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        fragmentReceiver = new FragmentReceiver();
        filter = new IntentFilter();
        filter.addAction("com.hillsidewatchers.sdu.sonicoperator.FragmentReceiver");
        getActivity().registerReceiver(fragmentReceiver, filter);
        manager = getActivity().getPackageManager();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Log.i("fragment1", "onCreateView");
        fragmentview = inflater.inflate(R.layout.fragment1, container, false);
        initView();
        getOnClickListener();
        return fragmentview;
    }


    private void getOnClickListener() {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), warning_toast, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), FloatingService.class);
                getActivity().finish();
                getActivity().startService(intent);

            }
        });
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        Log.e(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        getActivity().unregisterReceiver(fragmentReceiver);
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();
    }

    @Override
    public void onResume() {
        Log.e(TAG, "onResume");
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        Log.e(TAG, "onStart");
        Intent intent = new Intent();
        intent.setAction("com.hillsidewatchers.sdu.sonicoperator.SliderSwitch");
        getActivity().sendBroadcast(intent);
        filter = new IntentFilter();
        filter.addAction("com.hillsidewatchers.sdu.sonicoperator.FragmentReceiver");
        getActivity().registerReceiver(fragmentReceiver, filter);
        super.onStart();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    public void getRecordListView() {
        recordlist = getData();
         adapter = new SimpleAdapter(getActivity(), getData(),
                R.layout.record_list_item, new String[] { "img","name", "time","img_right"},
                new int[] { R.id.img, R.id.name, R.id.time,R.id.img_right });
        listView.setAdapter(adapter);
//        record_baseAdapter = new Record_BaseAdapter(getActivity(), recordlist,  manager);
//        listView.setAdapter(record_baseAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "点击的位置是" + position, Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //删除data
                //删除数据库中数据
                Toast.makeText(getActivity(), "长按已经监听到，正在删除数据", Toast.LENGTH_SHORT).show();
                TextView textview = (TextView) view.findViewById(R.id.name);
                String name = textview.getText().toString();
                removeData(position);
                adapter.notifyDataSetChanged();
                dbsqLiteDaoMobile = new DBSQLiteDao(getActivity());
                boolean flag = false;
                flag = dbsqLiteDaoMobile.delete(mobile_operation, " name = ?", new String[]{name});
                if (flag) {
                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

    }

    private List<Map<String, Object>> getData() {
        /**
         * 查询数据库中总的记录
         * 如果mapList==recordlist.size,不进行添加，否则进行添加item
         */
        Log.e(TAG, "调用getData");
        if (flag) {
            dbsqLiteDaoMobile = new DBSQLiteDao(getActivity());
            mapList = dbsqLiteDaoMobile.getMapList(mobile_operation, null, null);
            for (int i = 0; i < mapList.size(); i++) {
                addData(mapList.get(i).get("name").toString());
            }
        }
        flag = false;
        return recordlist;
    }

    private void removeData(int position) {
        recordlist.remove(position);
    }

    private void addData(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("img",R.drawable.point);
        map.put("name", name);
        map.put("time", "2016-08-08 13:32:23");
        map.put("img_right", R.drawable.play);
        recordlist.add(map);
    }

    private void initView() {
        imageButton = (ImageButton) fragmentview.findViewById(R.id.record_imageButton);
        listView = (ListView) fragmentview.findViewById(R.id.record_list);
        getRecordListView();
    }

    public class FragmentReceiver extends BroadcastReceiver {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Message message = new Message();
        DBSQLiteDao dbsqLiteDaoMobile = new DBSQLiteDao(getActivity());

        @Override
        public void onReceive( Context context, Intent intent) {
            Log.e(TAG, "广播已收到");
//            builder.setIcon(R.drawable.dialog_sonic);
            builder.setTitle("输入这段录屏的名字");
//            builder.setView(null);
            dialog_view = null;
            editText = null;
            dialog_view = View.inflate(getActivity(), R.layout.dialog_text, null);
            editText = (EditText) dialog_view.findViewById(R.id.name_text);
            builder.setView(dialog_view);

            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Toast.makeText(getActivity(), "点击确定按钮，开始存入数据库", Toast.LENGTH_SHORT).show();
                    String name_text = editText.getText().toString();
                    addData(name_text);
                    adapter.notifyDataSetChanged();
                    Log.e(TAG, name_text);
                    String tempData = rootShell.getInputInfoListAnalyzer().getactionListString();
                    String packageName = rootShell.getInputInfoListAnalyzer().getPackageName();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("name", name_text);
                    contentValues.put("packagename", packageName);
                    contentValues.put("info", tempData);
                    boolean flag = dbsqLiteDaoMobile.add(mobile_operation, contentValues);

                    if (flag) {
                        rootShell.destroy();
                        rootShell.setInputInfoListAnalyzerNULL();
                        Toast.makeText(getActivity(), "存入数据库成功", Toast.LENGTH_SHORT).show();

                    } else {
                        //界面报个错
                        Toast.makeText(getActivity(), "您存入数据库没有成功，需要重新录制", Toast.LENGTH_SHORT).show();
                    }
                    fragmentview = null;
//                    dialog_view = null;
//                    editText = null;
                }


            });
            builder.setNegativeButton("取消", null);
            builder.show();

        }
    }
}
