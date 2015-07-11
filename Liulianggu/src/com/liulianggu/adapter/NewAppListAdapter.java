package com.liulianggu.adapter;

import java.util.List;

import com.liulianggu.beans.AdvertisementItem;
import com.liulianggu.tabmenu.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class NewAppListAdapter extends BaseAdapter implements OnClickListener {

	private Context mContext;
	private List<AdvertisementItem> mData = null;
	private Callback mCallback;

	/**
	 * �Զ���ӿڣ����ڻص���ť����¼���Activity
	 * 
	 * @author Ivan Xu 2014-11-26
	 */
	public interface Callback {
		public void click(View v);
	}

	public NewAppListAdapter(Context ctx) {
		mContext = ctx;
		mData = null;
	}

	public NewAppListAdapter(Context ctx, List<AdvertisementItem> list) {
		mContext = ctx;
		mData = list;
	}

	public NewAppListAdapter(Context ctx, List<AdvertisementItem> list,
			Callback callback) {
		mContext = ctx;
		mData = list;
		mCallback = callback;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	/**
	 * �������
	 * 
	 * @param newList
	 */
	public void addData(List<AdvertisementItem> newList) {
		mData.addAll(newList);
	}

	/***
	 * ��ȡ������
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
		// appͼ��
		holder.mIcon = (ImageView) convertView.findViewById(R.id.img);
		holder.mIcon.setImageBitmap(mData.get(position).getImag());
		// app����
		holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating);
		holder.ratingBar.setRating(mData.get(position).getEvaluation());
		// app����
		holder.mChildName = (TextView) convertView.findViewById(R.id.item_name);
		holder.mChildName.setText(mData.get(position).getAppName());
		// app��Ϣ
		holder.mDetail = (TextView) convertView.findViewById(R.id.item_detail);
		holder.mDetail.setText(mData.get(position).getAppType());
		holder.downTimes = (TextView) convertView.findViewById(R.id.down_times);
		holder.downTimes.setText("(" + mData.get(position).getAppDownLoadVal()
				+ ")");
		// app����
		holder.downLoadButton = (Button) convertView
				.findViewById(R.id.app_down_load1);
		holder.downLoadButton.setOnClickListener(this);
		holder.downLoadButton.setTag(position);
		// + mData.get(position).getAppDownLoadVal()
		// + mData.get(position).getAppType());
		convertView.setFocusable(false);
		return convertView;

	}

	/******
	 * �ƹ�Ӧ�õĲ���
	 * 
	 * @author xyc
	 *
	 */
	private class ItemView {
		ImageView mIcon;
		TextView mChildName;
		TextView mDetail;
		RatingBar ratingBar;
		TextView downTimes;
		Button downLoadButton;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		mCallback.click(v);
	}

}
