package com.hillsidewatchers.sdu.sonicoperator.Adapter;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hillsidewatchers.sdu.sonicoperator.DatabaseManager.DBSQLiteDao;
import com.hillsidewatchers.sdu.sonicoperator.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2016/3/23.
 */
public class Gesture_BaseAdapter extends BaseAdapter {

    String selectName;
    List<String> list;
    private static final String TAG = "Gesture_BaseAdapter";
    private static final String mobile_operation = "mobile_operation";
    private static final String sonic_operation = "sonic_operation";
    private static final String connection = "connection";
    static class ViewHolder{
        public TextView gesture_name;
        public ImageButton add_icon;
        public TextView record_name;
    }
    List<Map<String,Object>> data;
    Context context;
    DBSQLiteDao dbsqLiteDao =null;
    Handler handler;
    private LayoutInflater layoutInflater = null;
    public Gesture_BaseAdapter(Context context, List<Map<String,Object>> data,Handler handler){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.handler = handler;
        dbsqLiteDao = new DBSQLiteDao(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String gesture_name;
        ItemGestureListner itemGestureListner;
        ViewHolder viewHolder = null;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.gesture_list_item,null);
            viewHolder.gesture_name = (TextView) convertView.findViewById(R.id.gesture_name);
            viewHolder.add_icon = (ImageButton) convertView.findViewById(R.id.add_icon);
            viewHolder.record_name = (TextView) convertView.findViewById(R.id.record_name);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.gesture_name.setText((String) data.get(position).get("gesture_name"));
        viewHolder.add_icon.setBackgroundResource((Integer) data.get(position).get("add_icon"));
        viewHolder.record_name.setText((String) data.get(position).get("record_name"));
        gesture_name = (String) viewHolder.gesture_name.getText();
        itemGestureListner = new ItemGestureListner(position,gesture_name,viewHolder.record_name);
        viewHolder.add_icon.setOnClickListener(itemGestureListner);
        //点击按钮，将数据库中mobile中的东西呈现出来，提供给用户选择，匹配过后，呈现出来，并将这种匹配关系存入数据库中，把名字写到右边

        return convertView;
    }
    private class ItemGestureListner implements View.OnClickListener{
        int itemPosition;
        String name;
        TextView record_view;
        public ItemGestureListner(int position,String name,TextView record_view){
            this.itemPosition = position;
            this.name = name;
            this.record_view = record_view;
        }

        @Override
        public void onClick(View v) {
              showDialog(name,record_view,String.valueOf(itemPosition));
            //存入关联数据库
        }
    }
    public String showDialog(final String name, final TextView record_view,final String positon){
        //将mobile_operation中所有的信息查询出来，以供用户绑定使用
        if(dbsqLiteDao==null){
            dbsqLiteDao = new DBSQLiteDao(context);
        }
        List<Map<String,String>> mapList = dbsqLiteDao.getMapList(mobile_operation,null,null);
        list = new ArrayList<>();
        for (int i=0;i<mapList.size();i++){
            Log.e(TAG,mapList.get(i).get("name"));
            String record_name = mapList.get(i).get("name");
            list.add(record_name);
        }
        final int size= list.size();
        Log.e(TAG,"大小为"+size);
        final String[]names = new String[size];
        for(int i=0;i<list.size();i++){
            names[i] = list.get(i);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.dialog_sonic);
        builder.setTitle("选择一个对应的屏幕操作");
        //    指定下拉列表的显示数据
        //    设置一个下拉的列表选择项
        builder.setItems(names, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!names[which].equals("")) {
                    Log.e("==========", names[which]);
                    selectName = names[which];

                    record_view.setText(selectName);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("gesture_name", name);
                    contentValues.put("record_name", selectName);
                    dbsqLiteDao.add(connection, contentValues);

                }
                Toast.makeText(context, "屏幕操作：" + names[which], Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();

        return selectName;
    }

}
