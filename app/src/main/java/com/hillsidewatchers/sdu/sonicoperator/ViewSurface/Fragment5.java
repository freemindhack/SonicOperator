package com.hillsidewatchers.sdu.sonicoperator.ViewSurface;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hillsidewatchers.sdu.sonicoperator.DatabaseManager.DBSQLiteDao;
import com.hillsidewatchers.sdu.sonicoperator.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.R.layout.simple_list_item_1;


public class Fragment5 extends Fragment{
    private static final String TAG = "Gesture_BaseAdapter";
    private static final String mobile_operation = "mobile_operation";
    private static final String sonic_operation = "sonic_operation";
    private static final String connection = "connection";
    DBSQLiteDao dbsqLiteDao = null;
    ListView listview = null;
    View fragmentview;
    EditText select_app;
    Button upload;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Log.i("fragment1", "onCreateView");
        fragmentview = inflater.inflate(R.layout.fragment5, container, false);
        initView();
        setListener();
        return fragmentview;
    }
    private void initView() {
        listview = (ListView) fragmentview.findViewById(R.id.actionlist);
        select_app = (EditText) fragmentview.findViewById(R.id.select_app);
        upload = (Button) fragmentview.findViewById(R.id.upload);
    }
    private void setListener() {
        /**
         * 选择应用框的点击事件,点击后跳出dialog，供选择
         */
        select_app.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dbsqLiteDao = new DBSQLiteDao(getActivity());
                List<Map<String, String>> list = dbsqLiteDao.getMapList(mobile_operation, null, null);
                List<String> stringList = new ArrayList<String>();
                for (int i = 0; i < list.size(); i++) {
                    if (!stringList.contains(list.get(i).get("packagename"))) {
                        stringList.add(list.get(i).get("packagename"));
                    }
                }
                final String[] packageArray = new String[stringList.size()];
                for (int i = 0; i < stringList.size(); i++) {
                    packageArray[i] = stringList.get(i);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("选择您的应用名：");
                builder.setItems(packageArray, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!packageArray[which].equals("")) {
                            select_app.setText(packageArray[which]);
                            List<Map<String, String>> list1 = dbsqLiteDao.getMapList(mobile_operation, " packagename = ?", new String[]{packageArray[which]});
                            List<String> list2 = new ArrayList<String>();
                            for (int i = 0; i < list1.size(); i++) {
                                list2.add(list1.get(i).get("name"));
                            }
                            String[]tempArray = new String[list2.size()];
                            for(int i=0;i<list2.size();i++){
                                tempArray[i] = list2.get(i);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), simple_list_item_1,tempArray);
                            listview.setAdapter(adapter);
                        }
                    }
                });
                builder.show();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "您的解决方案已经上传到云端", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.i("fragment1", "onPause");
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.i("fragment1", "onDestroy");
    }

    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();
        Log.i("fragment1", "onDetach");
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.i("fragment1", "onResume");
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Log.i("fragment1", "onStart");
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        Log.i("fragment1", "onStop");
    }


}
