package com.liulianggu.adapter;

import java.util.List;

import com.liulianggu.beans.AdvertisementItem;
import com.liulianggu.tabmenu.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class AdvertiseListAdapter extends BaseAdapter {

	private Context mContext;
	private List<AdvertisementItem> mData = null;
	private LayoutInflater mInflater = null;

	public AdvertiseListAdapter(Context ctx, List<AdvertisementItem> list) {
		mContext = ctx;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mData = list;
	}

	@Override
	public int getCount() {

		return mData.size();
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
		holder.mIcon = (ImageView) convertView.findViewById(R.id.img);
		Drawable drawable = mContext.getResources().getDrawable(
				R.drawable.liulianggu);
		mData.get(position).setImage(drawable);
		holder.mIcon.setBackgroundDrawable(drawable);
		// holder.mIcon.setBackgroundDrawable(mData.get(position).getImage());
		holder.mChildName = (TextView) convertView.findViewById(R.id.item_name);
		holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating);
		holder.ratingBar.setRating(mData.get(position).getEvaluation());
		holder.mChildName.setText(mData.get(position).getTitle());
		holder.mDetail = (TextView) convertView.findViewById(R.id.item_detail);
		holder.mDetail.setText(mData.get(position).getDetail());
		convertView.setFocusable(false);
		return convertView;

	}

	private class ItemView {
		ImageView mIcon;
		TextView mChildName;
		TextView mDetail;
		RatingBar ratingBar;
	}

}
