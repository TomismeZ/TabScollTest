package com.example.test.mytest.adapter;
/** * @author  作者 E-mail: 194093798@qq.com 
 * @date 创建时间：2016年2月22日 上午11:44:22 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.example.test.mytest.R;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceListAdapter extends BaseAdapter {

	List<PoiInfo> mList;
	LayoutInflater mInflater;
	private Map<Integer, Boolean> isSelectmap=new HashMap<Integer,Boolean>();

	private class MyViewHolder {
		TextView placeName;
		TextView placeAddree;
		ImageView placeSelected;

	}

	public PlaceListAdapter(LayoutInflater mInflater, List mList) {
		super();
		this.mList = mList;
		this.mInflater = mInflater;

	}

	/**
	 * @param notifyTip
	 */
	public void setNotifyTip(int notifyTip) {
		for (int i = 0; i < this.mList.size(); i++) {
			if (i == notifyTip) {
				isSelectmap.put(i, true);
			} else {
				isSelectmap.put(i, false);
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub

		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MyViewHolder holder;
		if (convertView == null) {
			System.out.println("----aa-");
			convertView = mInflater.inflate(R.layout.listitem_place, parent,
					false);
			holder = new MyViewHolder();
			holder.placeName = (TextView) convertView
					.findViewById(R.id.place_name);
			holder.placeAddree = (TextView) convertView
					.findViewById(R.id.place_adress);

			holder.placeName.setText(mList.get(position).toString());
			holder.placeAddree.setText(mList.get(position).toString());
			holder.placeSelected = (ImageView) convertView
					.findViewById(R.id.place_select);

			convertView.setTag(holder);
		} else {
			holder = (MyViewHolder) convertView.getTag();
		}
		holder.placeName.setText(mList.get(position).name);
		holder.placeAddree.setText(mList.get(position).address);

		
		if (isSelectmap.get(position)) {
//			holder.placeSelected.setImageResource(R.mipmap.gouxuan);
		} else {
//			holder.placeSelected.setImageResource(R.mipmap.weigouxuan);
		}

		return convertView;
	}

	public List<PoiInfo> getmList() {
		return mList;
	}

	public void setmList(List<PoiInfo> mList, int position) {
		this.mList = mList;
		setNotifyTip(position);
	}

	// class MyItemClickListener implements OnClickListener {
	//
	// ImageView mImg;
	// public MyItemClickListener(ImageView mImg) {
	// this.mImg = mImg;
	// }
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// mImg.setBackgroundResource(R.drawable.ic_select);
	// }
	//
	// }

}

