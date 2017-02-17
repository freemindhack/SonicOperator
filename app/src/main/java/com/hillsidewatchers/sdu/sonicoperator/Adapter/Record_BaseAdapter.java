//package com.hillsidewatchers.sdu.sonicoperator.Adapter;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.hillsidewatchers.sdu.sonicoperator.Analyzer.OutputDataAnalyzer;
//import com.hillsidewatchers.sdu.sonicoperator.DatabaseManager.DBSQLiteDao;
//import com.hillsidewatchers.sdu.sonicoperator.R;
//import com.hillsidewatchers.sdu.sonicoperator.ShellCommand.RootShell;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by lenovo on 2016/3/20.
// */
//public class Record_BaseAdapter extends BaseAdapter {
//
//    private static final String TAG = "Record_BaseAdapter";
//    PackageManager manager = null;
//    private static final String mobile_operation = "mobile_operation";
//    private static final String sonic_operation = "sonic_operation";
//    private static final String connection = "connection";
//    public String deleteItem = "";
//    static class ViewHolder{
//        public TextView name;
//        public Button  upload;
//        public Button run;
//    }
//    List<Map<String,Object>> data;
//    Context context ;
//    String selectdelete = "";
//    private LayoutInflater layoutInflater = null;
//    RootShell rootShell = RootShell.getInstance();
//     DBSQLiteDao dbsqLiteDaoMobile = new DBSQLiteDao(context);
//    public Record_BaseAdapter(Context context, List<Map<String,Object>> data,PackageManager manager){
//        this.context = context;
//        this.layoutInflater = LayoutInflater.from(context);
//        this.data = data;
//        this.manager = manager;
//    }
//    @Override
//    public int getCount() {
//        return data.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return position;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        ItemUploadListener itemUploadListener =null;
//        ItemRunListner itemRunListner = null;
//        ViewHolder viewHolder = null;
//        String name ;
//        if(convertView==null){
//            viewHolder = new ViewHolder();
//            convertView = layoutInflater.inflate(R.layout.record_list_item,null);
////            viewHolder.run = (Button) convertView.findViewById(R.id.run);
////            viewHolder.upload = (Button) convertView.findViewById(R.id.upload);
//            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
//            convertView.setTag(viewHolder);
//
//        }else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//        viewHolder.upload.setTag(position);
//        viewHolder.run.setTag(position);
//        viewHolder.run.setBackgroundResource((Integer) data.get(position).get("run"));
//        viewHolder.upload.setBackgroundResource((Integer) data.get(position).get("upload"));
//        viewHolder.name.setText((String) data.get(position).get("name"));
//        name = (String) viewHolder.name.getText();
//        itemUploadListener = new ItemUploadListener(position,name);
//        itemRunListner = new ItemRunListner(position,name);
//        viewHolder.upload.setOnClickListener(itemUploadListener);
//        viewHolder.run.setOnClickListener(itemRunListner);
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context,"监听到"+position,Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        return convertView;
//    }
//    private class ItemUploadListener implements View.OnClickListener{
//        int itemPosition;
//        String name;
//        public ItemUploadListener(int position,String name){
//            itemPosition = position;
//            this.name = name;
//        }
//
//        @Override
//        public void onClick(View v) {
//            Log.e("===", String.valueOf(itemPosition));
//            Toast.makeText(context,"删除状态"+name,Toast.LENGTH_SHORT).show();
////            Toast.makeText(context,"点击到删除",Toast.LENGTH_SHORT).show();
////            for(int i=data.size()-1;i>=0;i--){
////                data.remove(i);
////            }
//            DBSQLiteDao dbsqLiteDao = new DBSQLiteDao(context);
//            List<Map<String,String>> mapList = dbsqLiteDao.getMapList(mobile_operation, " name = ?", new String[]{name});
//            Log.e(TAG,"查询到的maplist的大小为:"+mapList.size());
////            if(mapList.size()==0){
////
////            }
//            List<String> packageList = new ArrayList<>();
//            final List<String> info = new ArrayList<>();
//            for(int i=0;i<mapList.size();i++){
//                packageList.add(mapList.get(i).get("packagename"));
//            }
//            Log.e(TAG,"查询到的packagenamelist的大小为"+packageList.size());
//            //将查询到的packagename的list转化为数组，方便后续onclick使用
//            final String delete_packageArray[] = new String[packageList.size()];
//            for(int i=0;i<packageList.size();i++){
//                delete_packageArray[i] = packageList.get(i);
//                Log.e(TAG,delete_packageArray[i]);
//            }
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setIcon(R.drawable.dialog_sonic);
//            builder.setTitle("选择你想删除什么应用下的这个动作");
//            builder.setItems(delete_packageArray, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    deleteItem = delete_packageArray[which];
//                    Map<String, String> map = dbsqLiteDaoMobile.query(mobile_operation, " name=? and packagename=?", new String[]{name, delete_packageArray[which]});
//                    if (map == null) {
//                        Toast.makeText(context, "之前没有录入则个动作", Toast.LENGTH_SHORT).show();
//                        Log.e(TAG, "map为空");
//                    } else {
//                        Toast.makeText(context, "要删除这个动作了", Toast.LENGTH_SHORT).show();
//                    }
//
//                    AlertDialog.Builder delete = new AlertDialog.Builder(context);
//                    delete.setIcon(R.drawable.dialog_sonic);
//                    delete.setTitle("确定删除？");
//                    delete.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Toast.makeText(context,"删除的 名字为"+name+"===要删除的包名："+deleteItem,Toast.LENGTH_SHORT).show();
//                            boolean deleteMap = dbsqLiteDaoMobile.delete(mobile_operation, "name=? and packagename=?", new String[]{name, deleteItem});
//                            if (deleteMap) {
//                                Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
//                            }else {
//                                Toast.makeText(context, "删除失败！", Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//                    });
//                    delete.show();
//
//
//                }
//            });
//            builder.show();
////            dbsqLiteDaoMobile.delete(mobile_operation," name = ? ",new String[]{name});
//            //用于点击上传按钮
//            //做上传处理
//        }
//    }
//    private class ItemRunListner implements View.OnClickListener{
//        int itemPosition;
//        String name;
//        public ItemRunListner(int position,String name){
//            itemPosition = position;
//            this.name = name;
//        }
//
//        @Override
//        public void onClick(View v) {
//            Toast.makeText(context,"开始播放",Toast.LENGTH_SHORT).show();
//            Log.e("===",String.valueOf(itemPosition));
//            //用于点击播放按钮，做播放处理
//            List<Map<String,String>> mapList = dbsqLiteDaoMobile.getMapList(mobile_operation, " name = ?", new String[]{name});
//            List<String> packageList = new ArrayList<>();
//            final List<String> info = new ArrayList<>();
//            for(int i=0;i<mapList.size();i++){
//                packageList.add(mapList.get(i).get("packagename"));
//            }
//
//            //将查询到的packagename的list转化为数组，方便后续onclick使用
//            final String packageArray[] = new String[packageList.size()];
//            for(int i=0;i<packageList.size();i++){
//                packageArray[i] = packageList.get(i);
//            }
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setIcon(R.drawable.dialog_sonic);
//            builder.setTitle("选择您想在什么能够程序下运行这个动作");
//            //    指定下拉列表的显示数据
//            //    设置一个下拉的列表选择项
//            builder.setItems(packageArray, new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    OutputDataAnalyzer outputDataAnalyzer = new OutputDataAnalyzer();
//                    Toast.makeText(context, "屏幕操作：" + packageArray[which], Toast.LENGTH_SHORT).show();
//                    if(packageArray[which].equals("com.miui.home")){
//                        outputDataAnalyzer.OsCommand("keyevent 3");
//                        Toast.makeText(context,"您已经回到桌面",Toast.LENGTH_SHORT).show();
//                        Map<String, String> map = dbsqLiteDaoMobile.query(mobile_operation, " name=? and packagename=?", new String[]{name, packageArray[which]});
//                        Log.e(TAG,"查询完毕");
//                        if (map==null){
//                            Toast.makeText(context,"之前没有录入则个动作",Toast.LENGTH_SHORT).show();
//                            Log.e(TAG,"map为空");
//                        }
//                        if (map != null) {
//                            if(map.get("info").equals("")){
//                                Log.e(TAG,"info为空");
//
//                            }
//                            if(!map.get("info").equals("")){
//                                Log.e(TAG,map.get("info"));
//                                outputDataAnalyzer.outputData(map.get("info"));
//                            }
//                            else {
//                                Log.e(TAG,"从数据库中读到的信息为空");
//                            }
//                        }
//                    }
//                    else {
//                        Intent intent = manager.getLaunchIntentForPackage(packageArray[which]);
//                        if(intent==null){
//                            Toast.makeText(context,"您所选择的应用没有MainActivty,无法启动，换一个吧",Toast.LENGTH_SHORT).show();
//                        }
//                        context.startActivity(intent);
//                        Map<String, String> map = dbsqLiteDaoMobile.query(mobile_operation, " name=? and packagename=?", new String[]{name, packageArray[which]});
//                        Log.e(TAG,"查询完毕");
//                        if (map==null){
//                            Toast.makeText(context,"之前没有录入则个动作",Toast.LENGTH_SHORT).show();
//                            Log.e(TAG,"map为空");
//                        }
//                        if (map != null) {
//                            if(map.get("info").equals("")){
//                                Log.e(TAG,"info为空");
//
//                            }
//                            if(!map.get("info").equals("")){
//                                Log.e(TAG,map.get("info"));
//                                outputDataAnalyzer.outputData(map.get("info"));
//                            }
//                            else {
//                                Log.e(TAG,"从数据库中读到的信息为空");
//                            }
//                        }
//                    }
//                }
//            });
//            builder.show();
//        }
//    }
//
//}
