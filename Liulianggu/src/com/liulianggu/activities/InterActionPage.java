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
 * ��������
 * 
 * @author xyc
 *
 */
public class InterActionPage extends Activity implements OnChildClickListener,
		OnItemLongClickListener, android.view.View.OnClickListener {
	private MyListView mListView = null;
	private ExpandAdapter mAdapter = null;
	private PersonalData appData;
	// ��������
	private Friends mData = null;
	private FriendsOpration friendsOpration = null;
	public static InterActionPage interActionPage = null;
	private Button mesgButton;

	private Roster roster;
	// ������Ϣ��
	private String fromUserJid;
	// ��Ϣ������
	private String toUserJid;
	// ���ӵ��û�
	public String addUserString;
	// ����������Ӧ����
	public int addResponseTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.interaction_page);
		interActionPage = this;
		// mListView �Ĺ���
		friendsOpration = new FriendsOpration((PersonalData) getApplication());
		mData = friendsOpration.getData(this);
		init();
	}

	/**
	 * �ؼ���������
	 */
	private void init() {
		appData = (PersonalData) getApplication();
		appData.getServiceManager().setAlias(
				((PersonalData) getApplication()).getPhoneNum());
		// ListView�Ĺ���
		mListView = (MyListView) findViewById(R.id.expendableListView1);
		mListView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mListView.setGroupIndicator(getResources().getDrawable(
				R.drawable.expander_floder));
		mesgButton = (Button) findViewById(R.id.message);
		mesgButton.setOnClickListener(this);

		// ��������
		mAdapter = new ExpandAdapter(this, mData);
		mListView.setAdapter(mAdapter);
		mListView
				.setDescendantFocusability(ExpandableListView.FOCUS_AFTER_DESCENDANTS);
		// ���Ӳ˵��ļ���
		mListView.setOnChildClickListener(this);

		// �б�������¼�
		mListView.setOnItemLongClickListener(this);
		roster = XmppTool.getConnection().getRoster();
		roster.addRosterListener(new RosterListener() {
			@Override
			// ��������������Ϣ
			public void entriesAdded(Collection<String> invites) {
				// TODO Auto-generated method stub

				System.out.println("�����������������Ϣ�ǣ�" + invites);
				for (Iterator iter = invites.iterator(); iter.hasNext();) {
					String fromUserJids = (String) iter.next();
					System.out.println("fromUserJids�ǣ�" + fromUserJids);
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
			// ��������ͬ�������Ϣ
			public void entriesUpdated(Collection<String> invites) {
				// TODO Auto-generated method stub
				System.out.println("����������ͬ�����Ϣ�ǣ�" + invites);
				for (Iterator iter = invites.iterator(); iter.hasNext();) {
					String fromUserJids = (String) iter.next();
					System.out.println("ͬ����ӵĺ����ǣ�" + fromUserJids);
					toUserJid = fromUserJids;
				}
				System.out.println("222222222222" + toUserJid + "22222222222"
						+ addUserString);

				// ��ȡ������ӷ���ͬ����Ϣ��ת�����̴���
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
			// ��������ɾ����Ϣ
			public void entriesDeleted(Collection<String> delFriends) {
				// TODO Auto-generated method stub
				System.out.println("������ɾ�����ѵ���Ϣ�ǣ�" + delFriends);
				if (delFriends.size() > 0) {
					// loadFriend();
				}
			}

			@Override
			// ��������״̬�ı���Ϣ
			public void presenceChanged(Presence presence) {

				// TODO Auto-generated method stub
				// friendMood = presence.getStatus();
				// System.out.println("presence.getStatus()�ǣ�"
				// + presence.getStatus());
			}

		});
	}

	/*
	 * ChildView ���� ���ֺܿ���onChildClick��������Ҫ�� ChildView layout �����
	 * android:descendantFocusability="blocksDescendants",
	 * ����isChildSelectable�ﷵ��true
	 */
	/**
	 * �б�ѡ��ĵ���¼�
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
	 * �б�ѡ��ĳ����¼�
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {

		final int groupPosition = (Integer) view.getTag(R.id.groupPosition);
		final int childPosition = (Integer) view.getTag(R.id.childPosion);
		// �����˵����趨����Ӧ
		PopupMenu popupMenu = new PopupMenu(InterActionPage.this, view);
		popupMenu.inflate(R.menu.popmenu1);
		popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				final FriendsOpration friendsOpration = new FriendsOpration(
						(PersonalData) getApplication());
				switch (item.getItemId()) {
				// ��������������ҳ��ת��
				case R.id.send:
					Intent intent1 = new Intent(InterActionPage.this,
							SendFlows.class);
					intent1.putExtra("receiverNum",
							mAdapter.getChild(groupPosition, childPosition)
									.getPhoneNum());
					startActivity(intent1);
					break;
				case R.id.delete:
					// ���ɾ��ѡ������Ի���������Ӧ
					new AlertDialog.Builder(InterActionPage.this)
							.setTitle("ɾ��").setMessage("ȷ��ɾ����")
							.setPositiveButton("��", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String resultString = "";
									if (friendsOpration.deleteFriend(mAdapter
											.getChild(groupPosition,
													childPosition)
											.getPhoneNum())) {
										resultString = "ɾ���ɹ�";
									} else {
										resultString = "ɾ��ʧ��";
									}
									Toast.makeText(InterActionPage.this,
											resultString, Toast.LENGTH_LONG)
											.show();
								}
							}).setNegativeButton("��", null).show();
					break;
				case R.id.change:
					// ����޸ı�ע��������ע�޸Ŀ򣬼�����Ӧ�¼�
					final EditText edt = new EditText(InterActionPage.this);
					edt.setText(mAdapter.getChild(groupPosition, childPosition)
							.getNickName());
					new AlertDialog.Builder(InterActionPage.this)
							.setTitle("�������±�ע")
							.setIcon(android.R.drawable.ic_dialog_info)
							.setView(edt)
							.setPositiveButton("ȷ��", new OnClickListener() {

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
										resultString = "�޸ĳɹ�";
									} else {
										resultString = "�޸�ʧ��";
									}
									Toast.makeText(InterActionPage.this,
											resultString, Toast.LENGTH_LONG)
											.show();
								}
							}).setNegativeButton("ȡ��", null).show();
				default:
					break;
				}
				return false;
			}
		});
		if (childPosition == -1)
			Toast.makeText(InterActionPage.this, "��", Toast.LENGTH_LONG).show();
		else
			popupMenu.show();
		return true;
	}

	/**********
	 * ���ذ�ť
	 */
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setMessage("ȷ���˳���")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						System.exit(0);
					}
				}).setNegativeButton("ȡ��", null).show();
	}

	/************
	 * ����˵�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.inter_action_page_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**********
	 * ����˵�����¼�
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
				// ��ȡ�������ߵ������Ϣ����������
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						InterActionPage.this);
				dialog.setTitle("��������")
						.setIcon(R.drawable.icon)
						.setMessage(
								"��"
										+ FriendsOpration
												.getNickName(fromUserJid
														.split("@")[0])
										+ "�����㷢���������룬�Ƿ���ӶԷ�Ϊ����?")
						.setPositiveButton("���",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.cancel();// ȡ��������
										// ������Ӻ�����ظ���Ϣ����������Ӧ��Ҳ����һ����������
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
						.setNegativeButton("�ܾ�",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										// XmppService.removeUser(roster,
										// fromUserJid);
										dialog.cancel();// ȡ��������
									}
								}).create().show();
				break;
			case 4:
				Toast.makeText(
						getApplicationContext(),
						FriendsOpration.getNickName(toUserJid.split("@")[0])
								+ "��ͬ�������Ϊ����", Toast.LENGTH_SHORT).show();
				mesgButton.setVisibility(View.VISIBLE);
				mesgButton.setText("1");
				break;
			// �ٴη��ͺ�������
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
					toUserJid.split("@")[0] + "��ͬ�����" + addResponseTime,
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
