package com.liulianggu.adapter;

import java.util.List;

import com.liulianggu.beans.FriendItem;
import com.liulianggu.beans.Friends;
import com.liulianggu.tabmenu.R;

import android.R.drawable;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private LayoutInflater mInflater = null;
	private String[] mGroupStrings = null;
	private List<List<FriendItem>> mData = null;

	public ExpandAdapter(Context ctx, Friends friends) {
		mContext = ctx;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		List<String> groupNames = friends.getGroupNames();
		mGroupStrings = groupNames.toArray(new String[groupNames.size()]);
		mData = friends.getAllFriends();
	}

	public void setData(Friends friends) {
		List<String> groupNames = friends.getGroupNames();
		mGroupStrings = groupNames.toArray(new String[groupNames.size()]);
		mData = friends.getAllFriends();
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return mData.get(groupPosition).size();
	}

	@Override
	public List<FriendItem> getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return mData.get(groupPosition);
	}

	@Override
	public FriendItem getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return mData.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.group_item_layout, null);
		}
		convertView.setTag(R.id.groupPosition, groupPosition);
		convertView.setTag(R.id.childPosion, -1);
		GroupViewHolder holder = new GroupViewHolder();
		holder.mGroupName = (TextView) convertView
				.findViewById(R.id.group_name);
		holder.mGroupName.setText(mGroupStrings[groupPosition]);
		holder.mGroupCount = (TextView) convertView
				.findViewById(R.id.group_count);
		holder.mGroupCount.setText("[" + mData.get(groupPosition).size() + "]");
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.child_item_layout, null);
		}
		convertView.setTag(R.id.groupPosition, groupPosition);
		convertView.setTag(R.id.childPosion, childPosition);
		ChildViewHolder holder = new ChildViewHolder();
		holder.mIcon = (ImageView) convertView.findViewById(R.id.img);
		holder.mIcon.setBackgroundResource(R.drawable.head_bg);
		holder.mChildName = (TextView) convertView.findViewById(R.id.item_name);
		holder.mChildName.setText((getChild(groupPosition, childPosition)
				.getNote().isEmpty() ? getChild(groupPosition, childPosition)
				.getNickName() : getChild(groupPosition, childPosition)
				.getNote())
				+ "("
				+ getChild(groupPosition, childPosition).getPhoneNum()
				+ ")");
		holder.mDetail = (TextView) convertView.findViewById(R.id.item_detail);
		holder.mDetail.setText(getChild(groupPosition, childPosition)
				.getMessage());
		convertView.setFocusable(false);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		/* 很重要：实现ChildView点击事件，必须返回true */
		return true;
	}

	private class GroupViewHolder {
		TextView mGroupName;
		TextView mGroupCount;
	}

	private class ChildViewHolder {
		ImageView mIcon;
		TextView mChildName;
		TextView mDetail;
	}

}
