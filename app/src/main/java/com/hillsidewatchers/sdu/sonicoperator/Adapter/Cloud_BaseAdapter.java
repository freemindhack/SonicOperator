package com.hillsidewatchers.sdu.sonicoperator.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hillsidewatchers.sdu.sonicoperator.R;

import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2016/3/25.
 */
public class Cloud_BaseAdapter extends BaseAdapter {
    static class ViewHolder{
        ImageView cloud_image;
        TextView cloud_title;
        TextView cloud_content;
        TextView cloud_time;
        Button cloud_download;
    }
    List<Map<String,Object>> data;
    Context context ;
    ItemCloadListener itemCloadListener;
    private LayoutInflater layoutInflater = null;
    public Cloud_BaseAdapter(Context context, List<Map<String,Object>> data){
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
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
        ViewHolder viewHolder = null;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.cloud_list_item,null);
            viewHolder.cloud_image = (ImageView) convertView.findViewById(R.id.cloud_image);
            viewHolder.cloud_title = (TextView) convertView.findViewById(R.id.cloud_title);
            viewHolder.cloud_content = (TextView) convertView.findViewById(R.id.cloud_content);
//            viewHolder.cloud_time = (TextView) convertView.findViewById(R.id.cloud_time);
            viewHolder.cloud_download = (Button) convertView.findViewById(R.id.cloud_downloud);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.cloud_image.setBackgroundResource((Integer) data.get(position).get("cloud_image"));
        viewHolder.cloud_title.setText((String) data.get(position).get("cloud_title"));
        viewHolder.cloud_content.setText((String) data.get(position).get("cloud_content"));
//        viewHolder.cloud_time.setText((String) data.get(position).get("cloud_time"));
        viewHolder.cloud_download.setBackgroundResource((Integer) data.get(position).get("cloud_downloud"));
        itemCloadListener = new ItemCloadListener(position);
        viewHolder.cloud_download.setOnClickListener(itemCloadListener);
        return convertView;
    }
    private class ItemCloadListener implements View.OnClickListener{
        int itemPosition;
        public ItemCloadListener(int position){
            itemPosition = position;
        }
        @Override
        public void onClick(View v) {
            Log.e("===", String.valueOf(itemPosition));
            Toast.makeText(context, "手势下载完成，已经存入地方数据库", Toast.LENGTH_SHORT).show();

            //用于点击上传按钮
            //做上传处理
        }


    }
}
