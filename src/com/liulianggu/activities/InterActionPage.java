package com.liulianggu.activities;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

import com.liulianggu.adapter.ExpandAdapter;
import com.liulianggu.adapter.MyListView;
import com.liulianggu.application.PersonalData;
import com.liulianggu.beans.FriendItem;
import com.liulianggu.tabmenu.R;
import com.liulianggu.userOpration.FriendsOpration;

/***************************
 * 互动界面
 * 
 * @author xyc
 *
 */
public class InterActionPage extends Activity implements OnChildClickListener {
	private MyListView mListView = null;
	private ExpandAdapter mAdapter = null;
	private List<List<FriendItem>> mData = new ArrayList<List<FriendItem>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.interaction_page);
		// mListView 的构造
		mData = new FriendsOpration((PersonalData) getApplication())
				.getData(this);
		mListView = (MyListView) findViewById(R.id.expendableListView1);
		// mListView = new MyListView(this);
		mListView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		// setContentView(R.layout.interaction_page);
		mListView.setGroupIndicator(getResources().getDrawable(
				R.drawable.expander_floder));
		mAdapter = new ExpandAdapter(this, mData);
		// mAdapter.getGroupView(1,true,null,null);
		mListView.setAdapter(mAdapter);
		mListView
				.setDescendantFocusability(ExpandableListView.FOCUS_AFTER_DESCENDANTS);
		mListView.setOnChildClickListener(this);
		// 列表长按点击事件
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				final int groupPosition = (Integer) view
						.getTag(R.id.groupPosition);
				final int childPosition = (Integer) view
						.getTag(R.id.childPosion);
				// 弹出菜单的设定及响应
				PopupMenu popupMenu = new PopupMenu(InterActionPage.this, view);
				popupMenu.inflate(R.menu.popmenu1);
				popupMenu
						.setOnMenuItemClickListener(new OnMenuItemClickListener() {

							@Override
							public boolean onMenuItemClick(MenuItem item) {
								// TODO Auto-generated method stub
								final FriendsOpration friendsOpration = new FriendsOpration(
										(PersonalData) getApplication());
								switch (item.getItemId()) {
								// 单击赠送流量，页面转跳
								case R.id.send:
									Intent intent1 = new Intent(
											InterActionPage.this,
											SendFlows.class);
									intent1.putExtra(
											"receiverNum",
											mAdapter.getChild(groupPosition,
													childPosition)
													.getPhoneNum());
									startActivity(intent1);
									break;
								case R.id.delete:
									// 点击删除选项，弹出对话框及其点击相应
									new AlertDialog.Builder(
											InterActionPage.this)
											.setTitle("删除")
											.setMessage("确定删除？")
											.setPositiveButton("是",
													new OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															String resultString = "";
															if (friendsOpration
																	.deleteFriend(mAdapter
																			.getChild(
																					groupPosition,
																					childPosition)
																			.getPhoneNum())) {
																resultString = "删除成功";
															} else {
																resultString = "删除失败";
															}
															Toast.makeText(
																	InterActionPage.this,
																	resultString,
																	Toast.LENGTH_LONG)
																	.show();
														}
													})
											.setNegativeButton("否", null)
											.show();
									break;
								case R.id.change:
									// 点击修改备注，弹出备注修改框，及其相应事件
									final EditText edt = new EditText(
											InterActionPage.this);
									edt.setText(mAdapter.getChild(
											groupPosition, childPosition)
											.getNickName());
									new AlertDialog.Builder(
											InterActionPage.this)
											.setTitle("请输入新备注")
											.setIcon(
													android.R.drawable.ic_dialog_info)
											.setView(edt)
											.setPositiveButton("确定",
													new OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															String resultString = "";
															if (friendsOpration
																	.changeNickName(
																			mAdapter.getChild(
																					groupPosition,
																					childPosition)
																					.getPhoneNum(),
																			edt.getText()
																					.toString()
																					.trim())) {
																resultString = "修改成功";
															} else {
																resultString = "修改失败";
															}
															Toast.makeText(
																	InterActionPage.this,
																	resultString,
																	Toast.LENGTH_LONG)
																	.show();
														}
													})
											.setNegativeButton("取消", null)
											.show();
								default:
									break;
								}
								return false;
							}
						});
				if (childPosition == -1)
					Toast.makeText(InterActionPage.this, "组", Toast.LENGTH_LONG)
							.show();
				else
					// Toast.makeText(
					// HomeActivity.this,
					// mAdapter.getChild(groupPosition, childPosition)
					// .getName(), Toast.LENGTH_LONG).show();

					popupMenu.show();
				return true;
			}
		});

	}

	/*
	 * ChildView 设置 布局很可能onChildClick进不来，要在 ChildView layout 里加上
	 * android:descendantFocusability="blocksDescendants",
	 * 还有isChildSelectable里返回true
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
			Toast.makeText(InterActionPage.this, "11", Toast.LENGTH_LONG)
					.show();
			break;
		case R.id.friend_add:
			Toast.makeText(InterActionPage.this, "增加好友", Toast.LENGTH_LONG)
					.show();
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

}
