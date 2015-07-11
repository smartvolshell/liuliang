package com.liulianggu.userOpration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.search.UserSearchManager;

import android.R.integer;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.liulianggu.application.PersonalData;
import com.liulianggu.beans.FriendItem;
import com.liulianggu.beans.Friends;
import com.liulianggu.sever.SeverOpration;
import com.liulianggu.tabmenu.R;
import com.liulianggu.utils.PhoneNumType;
import com.liulianggu.utils.XmppTool;

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

	/***************
	 * 获取好友信息
	 * 
	 * @param ctx
	 * @return
	 */
	public Friends getData(Context ctx) {
		mContext = ctx;
		Friends mData = getAllEntries(XmppTool.getConnection().getRoster());
		String[] phoneNumType = mContext.getResources().getStringArray(
				R.array.groups);
		for (int i = 0; i < phoneNumType.length; i++) {
			mData.getGroupNames().add(phoneNumType[i]);
			mData.getAllFriends().add(getPhoneContracts(phoneNumType[i]));
		}

		return mData;
	}

	/***
	 * 增加好友，如果无分组则默认为“我的好友”
	 * 
	 * @param userName
	 * @param name
	 * @param groupName
	 * @return
	 */
	public boolean addFrind(String userName, String name, String groupName) {
		if (groupName == null)
			groupName = "我的好友";
		boolean flag = XmppTool.addUser(XmppTool.getConnection().getRoster(),
				userName, name, groupName);
		Presence subscription = new Presence(Presence.Type.subscribe);
		subscription.setTo(userName);
		XmppTool.getConnection().sendPacket(subscription);
		return flag;
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

	/**
	 * 获取所有好友信息
	 * 
	 * @param roster
	 * @return
	 */
	private Friends getAllEntries(Roster roster) {
		Friends friends = new Friends();
		List<String> groupNameStrings = new ArrayList<String>();
		List<List<FriendItem>> allFriends = new ArrayList<List<FriendItem>>();
		Collection<RosterGroup> rosterGroups = roster.getGroups();

		for (RosterGroup group : rosterGroups) {
			String groupNameString = group.getName();
			List<FriendItem> allFriendItems = new ArrayList<FriendItem>();
			Collection<RosterEntry> rosterEntries = group.getEntries();
			for (RosterEntry entry : rosterEntries) {
				FriendItem friendItem = new FriendItem();
				friendItem.setPhoneNum(entry.getUser().split("@")[0]);
				friendItem.setNickName(entry.getName());
				friendItem.setFriendType(entry.getType().name());
				friendItem.setMessage(groupNameString + ";"
						+ entry.getUser().split("@")[0] + ";" + entry.getName()
						+ ";");
				allFriendItems.add(friendItem);
			}
			groupNameStrings.add(groupNameString);
			allFriends.add(allFriendItems);
		}
		friends.setFriends(allFriends);
		friends.setGroupNames(groupNameStrings);
		return friends;
	}

	/*****************
	 * 8 获取好友昵称
	 * 
	 * @param userName
	 * @return
	 */
	public static String getNickName(String userName) {
		String nickNameString = "";
		String queryResult = "";

		XMPPConnection connection = XmppTool.getConnection();
		UserSearchManager search = new UserSearchManager(connection);
		// 此处一定要加上 search.
		Form searchForm;
		try {
			searchForm = search.getSearchForm("search."
					+ connection.getServiceName());
			Form answerForm = searchForm.createAnswerForm();
			answerForm.setAnswer("Name", true);
			answerForm.setAnswer("Username", true);
			answerForm.setAnswer("search", userName);
			ReportedData data = search.getSearchResults(answerForm, "search."
					+ connection.getServiceName());
			Iterator<Row> it = data.getRows();
			Row row = null;
			while (it.hasNext()) {
				row = it.next();
				queryResult = row.getValues("Username").next().toString();
				nickNameString = row.getValues("Name").next().toString();
			}
		} catch (XMPPException e) {
			e.printStackTrace();
		}

		return nickNameString;
	}
}
