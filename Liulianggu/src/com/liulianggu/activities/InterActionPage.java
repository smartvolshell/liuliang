package com.liulianggu.activities;

import java.util.Collection;
import java.util.Iterator;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

import com.liulianggu.adapter.ExpandAdapter;
import com.liulianggu.adapter.MyListView;
import com.liulianggu.application.PersonalData;
import com.liulianggu.beans.FriendItem;
import com.liulianggu.beans.Friends;
import com.liulianggu.tabmenu.R;
import com.liulianggu.userOpration.FriendsOpration;
import com.liulianggu.utils.XmppTool;

/***************************
 * 互动界面
 * 
 * @author xyc
 *
 */
public class InterActionPage extends Activity implements OnChildClickListener,
		OnItemLongClickListener, android.view.View.OnClickListener {
	private MyListView mListView = null;
	private ExpandAdapter mAdapter = null;
	private PersonalData appData;
	// 好友数据
	private Friends mData = null;
	private FriendsOpration friendsOpration = null;
	public static InterActionPage interActionPage = null;
	private Button mesgButton;

	private Roster roster;
	// 发送消息者
	private String fromUserJid;
	// 消息接收者
	private String toUserJid;
	// 增加的用户
	public String addUserString;
	// 好友申请相应次数
	public int addResponseTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.interaction_page);
		interActionPage = this;
		// mListView 的构造
		friendsOpration = new FriendsOpration((PersonalData) getApplication());
		mData = friendsOpration.getData(this);
		init();
	}

	/**
	 * 控件、监听绑定
	 */
	private void init() {
		appData = (PersonalData) getApplication();
		appData.getServiceManager().setAlias(
				((PersonalData) getApplication()).getPhoneNum());
		// ListView的构造
		mListView = (MyListView) findViewById(R.id.expendableListView1);
		mListView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mListView.setGroupIndicator(getResources().getDrawable(
				R.drawable.expander_floder));
		mesgButton = (Button) findViewById(R.id.message);
		mesgButton.setOnClickListener(this);

		// 绑定适配器
		mAdapter = new ExpandAdapter(this, mData);
		mListView.setAdapter(mAdapter);
		mListView
				.setDescendantFocusability(ExpandableListView.FOCUS_AFTER_DESCENDANTS);
		// 绑定子菜单的监听
		mListView.setOnChildClickListener(this);

		// 列表长按点击事件
		mListView.setOnItemLongClickListener(this);
		roster = XmppTool.getConnection().getRoster();
		roster.addRosterListener(new RosterListener() {
			@Override
			// 监听好友申请消息
			public void entriesAdded(Collection<String> invites) {
				// TODO Auto-generated method stub

				System.out.println("监听到好友申请的消息是：" + invites);
				for (Iterator iter = invites.iterator(); iter.hasNext();) {
					String fromUserJids = (String) iter.next();
					System.out.println("fromUserJids是：" + fromUserJids);
					fromUserJid = fromUserJids;
				}
				Message mes = new Message();
				if (fromUserJid != null && !fromUserJid.equals(addUserString)) {
					Intent intent = new Intent();
					//
					// intent.setClass(InterActionPage.this,
					// InterActionPage.class);
					// startActivity(intent);
					mes.what = 3;
					handler.sendMessage(mes);

				} else if (fromUserJid != null
						&& fromUserJid.equals(addUserString)) {

					mes.what = 5;
					handler.sendMessage(mes);
				}
			}

			@Override
			// 监听好友同意添加消息
			public void entriesUpdated(Collection<String> invites) {
				// TODO Auto-generated method stub
				System.out.println("监听到好友同意的消息是：" + invites);
				for (Iterator iter = invites.iterator(); iter.hasNext();) {
					String fromUserJids = (String) iter.next();
					System.out.println("同意添加的好友是：" + fromUserJids);
					toUserJid = fromUserJids;
				}
				System.out.println("222222222222" + toUserJid + "22222222222"
						+ addUserString);

				// 获取到被添加方的同意信息后，转主进程处理
				if (toUserJid != null && toUserJid.equals(addUserString)) {
					addResponseTime++;
					if (addResponseTime >= 4) {
						Message message = new Message();
						message.what = 4;
						handler.sendMessage(message);
					}
				}
			}

			@Override
			// 监听好友删除消息
			public void entriesDeleted(Collection<String> delFriends) {
				// TODO Auto-generated method stub
				System.out.println("监听到删除好友的消息是：" + delFriends);
				if (delFriends.size() > 0) {
					// loadFriend();
				}
			}

			@Override
			// 监听好友状态改变消息
			public void presenceChanged(Presence presence) {

				// TODO Auto-generated method stub
				// friendMood = presence.getStatus();
				// System.out.println("presence.getStatus()是："
				// + presence.getStatus());
			}

		});
	}

	/*
	 * ChildView 设置 布局很可能onChildClick进不来，要在 ChildView layout 里加上
	 * android:descendantFocusability="blocksDescendants",
	 * 还有isChildSelectable里返回true
	 */
	/**
	 * 列表选项的点击事件
	 */
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		FriendItem item = mAdapter.getChild(groupPosition, childPosition);
		new AlertDialog.Builder(this)
				.setTitle(item.getNickName())
				.setMessage(item.getMessage() + item.getPhoneNum())
				.setIcon(android.R.drawable.ic_menu_more)
				.setNegativeButton(android.R.string.cancel,
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						}).create().show();
		return true;
	}

	/**
	 * 列表选项的长按事件
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {

		final int groupPosition = (Integer) view.getTag(R.id.groupPosition);
		final int childPosition = (Integer) view.getTag(R.id.childPosion);
		// 弹出菜单的设定及响应
		PopupMenu popupMenu = new PopupMenu(InterActionPage.this, view);
		popupMenu.inflate(R.menu.popmenu1);
		popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				final FriendsOpration friendsOpration = new FriendsOpration(
						(PersonalData) getApplication());
				switch (item.getItemId()) {
				// 单击赠送流量，页面转跳
				case R.id.send:
					Intent intent1 = new Intent(InterActionPage.this,
							SendFlows.class);
					intent1.putExtra("receiverNum",
							mAdapter.getChild(groupPosition, childPosition)
									.getPhoneNum());
					startActivity(intent1);
					break;
				case R.id.delete:
					// 点击删除选项，弹出对话框及其点击相应
					new AlertDialog.Builder(InterActionPage.this)
							.setTitle("删除").setMessage("确定删除？")
							.setPositiveButton("是", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String resultString = "";
									if (friendsOpration.deleteFriend(mAdapter
											.getChild(groupPosition,
													childPosition)
											.getPhoneNum())) {
										resultString = "删除成功";
									} else {
										resultString = "删除失败";
									}
									Toast.makeText(InterActionPage.this,
											resultString, Toast.LENGTH_LONG)
											.show();
								}
							}).setNegativeButton("否", null).show();
					break;
				case R.id.change:
					// 点击修改备注，弹出备注修改框，及其相应事件
					final EditText edt = new EditText(InterActionPage.this);
					edt.setText(mAdapter.getChild(groupPosition, childPosition)
							.getNickName());
					new AlertDialog.Builder(InterActionPage.this)
							.setTitle("请输入新备注")
							.setIcon(android.R.drawable.ic_dialog_info)
							.setView(edt)
							.setPositiveButton("确定", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String resultString = "";
									if (friendsOpration.changeNickName(
											mAdapter.getChild(groupPosition,
													childPosition)
													.getPhoneNum(), edt
													.getText().toString()
													.trim())) {
										resultString = "修改成功";
									} else {
										resultString = "修改失败";
									}
									Toast.makeText(InterActionPage.this,
											resultString, Toast.LENGTH_LONG)
											.show();
								}
							}).setNegativeButton("取消", null).show();
				default:
					break;
				}
				return false;
			}
		});
		if (childPosition == -1)
			Toast.makeText(InterActionPage.this, "组", Toast.LENGTH_LONG).show();
		else
			popupMenu.show();
		return true;
	}

	/**********
	 * 返回按钮
	 */
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setMessage("确定退出？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						System.exit(0);
					}
				}).setNegativeButton("取消", null).show();
	}

	/************
	 * 左键菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.inter_action_page_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**********
	 * 左键菜单点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.friend_search:
			freshList();
			break;
		case R.id.friend_add:
			Intent intent2 = new Intent(InterActionPage.this, AddFriend.class);
			startActivity(intent2);
			break;
		case R.id.flow_send:
			Intent intent1 = new Intent(InterActionPage.this, SendFlows.class);
			intent1.putExtra("receiverNum", "");
			startActivity(intent1);
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case 1:
				break;
			case 2:
				break;
			case 3:
				// 获取到申请者的添加信息，弹框提醒
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						InterActionPage.this);
				dialog.setTitle("好友申请")
						.setIcon(R.drawable.icon)
						.setMessage(
								"【"
										+ FriendsOpration
												.getNickName(fromUserJid
														.split("@")[0])
										+ "】向你发来好友申请，是否添加对方为好友?")
						.setPositiveButton("添加",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.cancel();// 取消弹出框
										// 允许添加好友则回复消息，被邀请人应当也发送一个邀请请求。
										friendsOpration
												.addFrind(
														fromUserJid,
														FriendsOpration
																.getNickName(fromUserJid
																		.split("@")[0]),
														null);
										freshList();
									}
								})
						.setNegativeButton("拒绝",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										// XmppService.removeUser(roster,
										// fromUserJid);
										dialog.cancel();// 取消弹出框
									}
								}).create().show();
				break;
			case 4:
				Toast.makeText(
						getApplicationContext(),
						FriendsOpration.getNickName(toUserJid.split("@")[0])
								+ "已同意添加您为好友", Toast.LENGTH_SHORT).show();
				mesgButton.setVisibility(View.VISIBLE);
				mesgButton.setText("1");
				break;
			// 再次发送好友申请
			case 5:
				Presence subscription = new Presence(Presence.Type.subscribe);
				subscription.setTo(fromUserJid);
				XmppTool.getConnection().sendPacket(subscription);
				// addUser(roster, fromUserJid, fromUserJid);
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.message:
			Toast.makeText(getApplicationContext(),
					toUserJid.split("@")[0] + "已同意添加" + addResponseTime,
					Toast.LENGTH_SHORT).show();
			mesgButton.setVisibility(View.GONE);
			freshList();
			break;
		default:
			break;
		}

	}

	private void freshList() {
		mData = friendsOpration.getData(InterActionPage.this);
		mAdapter.setData(mData);
		mAdapter.notifyDataSetChanged();
	}
}
