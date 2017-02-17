package com.hillsidewatchers.sdu.sonicoperator.ViewSurface;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hillsidewatchers.sdu.sonicoperator.Adapter.Cloud_BaseAdapter;
import com.hillsidewatchers.sdu.sonicoperator.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 录制脚本所用的fragment
 */
public class Fragment2 extends Fragment {

	Cloud_BaseAdapter cloud_baseAdapter ;
	List<Map<String, Object>> cloudlist = new ArrayList<>();
	ListView listView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View fragmentview = inflater.inflate(R.layout.fragment2, container,
				false);
		listView = (ListView) fragmentview.findViewById(R.id.cloudList);
		getRecordListView();
		return fragmentview;
	}

	private void getRecordListView() {

		cloudlist = getData();
		cloud_baseAdapter = new Cloud_BaseAdapter(getActivity(), cloudlist);
		listView.setAdapter(cloud_baseAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getActivity(), "点击的位置是" + position, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private List<Map<String, Object>> getData() {
		int[]imageViews = new int []{R.drawable.appqq,R.drawable.ppt,R.drawable.news,R.drawable.appqiyi,R.drawable.appkugou,R.drawable.appshenmiao,R.drawable.appweibo,R.drawable.apphand1,};
		String [] cloud_title = new String[]{"QQ自动阅读消息","PPT控制","看新闻资讯","爱奇艺视屏播放","个性化的一套动作"
				,"无触屏切换歌曲","神庙逃亡专属手势","支持点赞功能哦"};
		String[]cloud_content = new String[]{"只需要一个简单的手势就可以进入QQ，并阅读QQ消息",
				"随意在PPT间切换，可以一边做笔记，一边看PPT",
				"浏览新闻资讯，左右切换新闻类型，PUSH阅读新闻",
				"控制视屏的暂停、播放、音量的加减、快进快退",
				"简单的动作就可以实现音乐的暂停和切换，以及音量的加减",
				"不用触碰手机和平板就能玩神庙逃亡，隔空玩游戏而且不需要外设",
				"通过简单的手势上下滑动阅读浏览微博消息，还有点赞功能",
				"拨打常用联系人的电话，锁屏时候的解锁功能",};
//		String []cloud_content = new String[]{"单手向左向右－－切换新闻频道 单手上抬、下按－－滑动屏幕 向前轻推－－点击阅读新闻",
//				"单手向左向右－－切换新闻频道 单手上抬、下按－－滑动屏幕 向前轻推－－点击阅读新闻",
//				"单手向左向右－－切换新闻频道 单手上抬、下按－－滑动屏幕 向前轻推－－点击阅读新闻",
//				"单手向左向右－－切换新闻频道 单手上抬、下按－－滑动屏幕 向前轻推－－点击阅读新闻",
//				"单手向左向右－－切换新闻频道 单手上抬、下按－－滑动屏幕 向前轻推－－点击阅读新闻",
//				"单手向左向右－－切换新闻频道 单手上抬、下按－－滑动屏幕 向前轻推－－点击阅读新闻",
//				"单手向左向右－－切换新闻频道 单手上抬、下按－－滑动屏幕 向前轻推－－点击阅读新闻",
//				"单手向左向右－－切换新闻频道 单手上抬、下按－－滑动屏幕 向前轻推－－点击阅读新闻",
//		};
		String[]cloud_time = new String[]{"2016-03-19 17:50:54",
				"2016-03-19 21:04:23",
				"2016-03-20 13:05:45",
				"2016-03-20 23:23:34",
				"2016-03-21 07:21:43",
				"2016-03-21 12:31:12",
				"2016-03-22 14:45:21",
				"2016-03-23 16:09:34"
		};
		if(cloudlist!=null)
		{
			for(int i=cloudlist.size()-1;i>=0;i--){
				cloudlist.remove(i);
			}
		}
		for(int i=0;i<8;i++){
			addData(imageViews[i],cloud_title[i],cloud_content[i]);
		}

		return cloudlist;
	}
	private void addData(int cloud_image,String cloud_title,String cloud_content) {
		Map<String, Object> map = new HashMap<>();
		map.put("cloud_image", cloud_image);
		map.put("cloud_title", cloud_title);
		map.put("cloud_content", cloud_content);
//		map.put("cloud_time",cloud_time);
		map.put("cloud_downloud",R.drawable.download);
		cloudlist.add(map);
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onDestroy() {
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
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
}
