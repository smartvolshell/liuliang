package com.liulianggu.userOpration;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.liulianggu.application.PersonalData;
import com.liulianggu.beans.FriendItem;
import com.liulianggu.sever.SeverOpration;
import com.liulianggu.tabmenu.R;
import com.liulianggu.utils.PhoneNumType;

public class FriendsOpration extends Activity {
	private PersonalData appDate;
	private SeverOpration sopOpration;
	private Context mContext;

	/******************
	 * 构造函数，需要app
	 * 
	 * @param appDate
	 */
	public FriendsOpration(PersonalData appDate) {
		this.appDate = appDate;
		sopOpration = new SeverOpration();
	}

	/***************
	 * 删除好友
	 * 
	 * @param phoneNum
	 * @return
	 */
	public boolean deleteFriend(String phoneNum) {
		return true;
	}

	/*****************
	 * 修改好友备注
	 * 
	 * @param phoneNum
	 * @param newNickName
	 * @return
	 */
	public boolean changeNickName(String phoneNum, String newNickName) {
		return true;
	}

	public List<List<FriendItem>> getData(Context ctx) {
		mContext = ctx;
		List<List<FriendItem>> mData = new ArrayList<List<FriendItem>>();
		String[] phoneNumType = mContext.getResources().getStringArray(
				R.array.groups);
		for (int i = 0; i < phoneNumType.length; i++)
			mData.add(getPhoneContracts(phoneNumType[i]));
		return mData;
	}

	/**********************
	 * 从通讯录内获取好友
	 * 
	 * @param token
	 * @return
	 */
	private List<FriendItem> getPhoneContracts(String token) {
		List<FriendItem> contractsInfosList = new ArrayList<FriendItem>();
		ContentResolver resolver = mContext.getContentResolver();
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, null, null,
				null, null);
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				int nameIndex = phoneCursor.getColumnIndex(Phone.DISPLAY_NAME);
				String name = phoneCursor.getString(nameIndex);
				String phoneNumber = phoneCursor.getString(phoneCursor
						.getColumnIndex(Phone.NUMBER));
				String type = PhoneNumType.getNumType(phoneNumber);
				if (type.equals(token.trim())) {
					FriendItem friendItem = new FriendItem();
					friendItem.setFriendType(FriendItem.TEL_FRIEND);
					friendItem.setNote(name);
					friendItem.setPhoneNum(phoneNumber);
					friendItem.setMessage(type);
					contractsInfosList.add(friendItem);
				}
			}
		}
		if (phoneCursor != null)
			phoneCursor.close();
		return contractsInfosList;
	}

	// private Context mContext;
	// private int[] mGroupArrays = new int[] { R.array.tianlongbabu,
	// R.array.shediaoyingxiongzhuan, R.array.shendiaoxialv };
	//
	// private int[] mDetailIds = new int[] { R.array.tianlongbabu_detail,
	// R.array.shediaoyingxiongzhuan_detail, R.array.shendiaoxialv_detail };
	//
	// private int[][] mImageIds = new int[][] {
	// { R.drawable.liulianggu, R.drawable.liulianggu,
	// R.drawable.liulianggu },
	// { R.drawable.liulianggu, R.drawable.liulianggu,
	// R.drawable.liulianggu, R.drawable.liulianggu,
	// R.drawable.liulianggu, R.drawable.liulianggu,
	// R.drawable.liulianggu },
	// { R.drawable.liulianggu, R.drawable.liulianggu } };
	//
	// public List<List<FriendItem>> getData(Context ctx) {
	// mContext = ctx;
	// List<List<FriendItem>> mData = new ArrayList<List<FriendItem>>();
	// for (int i = 0; i < mGroupArrays.length; i++) {
	// List<FriendItem> friendItems = getFriedsFromGroup(i);
	// mData.add(friendItems);
	// }
	// return mData;
	// }
	//
	// private List<FriendItem> getFriedsFromGroup(int groupNum) {
	// List<FriendItem> friendItems = new ArrayList<FriendItem>();
	// String[] childs = getStringArray(mGroupArrays[groupNum]);
	// String[] details = getStringArray(mDetailIds[groupNum]);
	// for (int j = 0; j < childs.length; j++) {
	// String phoneNum = "" + groupNum + "000" + j;
	//
	// FriendItem item = new FriendItem(mImageIds[groupNum][j], phoneNum,
	// childs[j], details[j]);
	// friendItems.add(item);
	// }
	//
	// return friendItems;
	// }
	//
	// private String[] getStringArray(int resId) {
	// return mContext.getResources().getStringArray(resId);
	// }

}
