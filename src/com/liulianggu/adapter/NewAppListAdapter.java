package com.liulianggu.adapter;

import java.util.List;

import com.liulianggu.beans.AdvertisementItem;
import com.liulianggu.tabmenu.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class NewAppListAdapter extends BaseAdapter {

	private Context mContext;
	private List<AdvertisementItem> mData = null;

	public NewAppListAdapter(Context ctx) {
		mContext = ctx;
		mData = null;
	}

	public NewAppListAdapter(Context ctx, List<AdvertisementItem> list) {
		mContext = ctx;
		mData = list;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	/**
	 * 添加数据
	 * 
	 * @param newList
	 */
	public void addData(List<AdvertisementItem> newList) {
		mData.addAll(newList);
	}

	/***
	 * 获取新数据
	 * 
	 * @param newList
	 */
	public void getNewData(List<AdvertisementItem> newList) {
		mData = newList;
	}

	@Override
	public AdvertisementItem getItem(int position) {

		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.advertise_item_layout, null);
		}

		ItemView holder = new ItemView();
		// app图标
		holder.mIcon = (ImageView) convertView.findViewById(R.id.img);
		holder.mIcon.setImageBitmap(mData.get(position).getImag());
		// app评分
		holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating);
		holder.ratingBar.setRating(mData.get(position).getEvaluation());
		// app名称
		holder.mChildName = (TextView) convertView.findViewById(R.id.item_name);
		holder.mChildName.setText(mData.get(position).getAppName());
		// app信息
		holder.mDetail = (TextView) convertView.findViewById(R.id.item_detail);
		holder.mDetail.setText(mData.get(position).getAppMsg());
		convertView.setFocusable(false);
		return convertView;

	}

	/******
	 * 推广应用的布局
	 * 
	 * @author xyc
	 *
	 */
	private class ItemView {
		ImageView mIcon;
		TextView mChildName;
		TextView mDetail;
		RatingBar ratingBar;
	}

}
