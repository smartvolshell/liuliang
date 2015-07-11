package com.liulianggu.activities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ProgressBar;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.liulianggu.adapter.NewAppListAdapter;
import com.liulianggu.adapter.NewAppListAdapter.Callback;
import com.liulianggu.beans.AdvertisementItem;
import com.liulianggu.games.GameOf2048;
import com.liulianggu.tabmenu.R;
import com.liulianggu.userOpration.AdvertisementOpration;

/****************
 * 赚取流量相关界面
 * 
 * @author xyc
 *
 */
public class MakeDataPage extends Activity implements OnScrollListener,
		OnItemSelectedListener, OnClickListener, Callback {
	// private Button playGame;
	private ListView appList;
	private Spinner appType;
	private Spinner appSortType;
	private List<AdvertisementItem> mData = new ArrayList<AdvertisementItem>();
	private NewAppListAdapter adapter;
	// 缓冲布局
	LinearLayout loadingLayout;
	// 数据刷新线程
	private Thread mThread;
	private int nowPosition = 0;
	private boolean isOver = false;
	// 当前的排序情况
	private String nowSortType;
	private int nowSrotDirection;
	/**
	 * 设置布局显示属性
	 */
	private LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);
	/**
	 * 设置布局显示目标最大化属性
	 */
	private LayoutParams FFlayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.FILL_PARENT,
			LinearLayout.LayoutParams.FILL_PARENT);

	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.make_data_page);
		init();
	}

	/**
	 * 绑定控件
	 */
	private void init() {
		// playGame = (Button) findViewById(R.id.play_game);
		appList = (ListView) findViewById(R.id.apps);
		appType = (Spinner) findViewById(R.id.appType);
		appSortType = (Spinner) findViewById(R.id.appSortType);

		// 线性布局
		LinearLayout layout = new LinearLayout(this);
		// 设置布局 水平方向
		layout.setOrientation(LinearLayout.HORIZONTAL);
		// 进度条
		progressBar = new ProgressBar(this);
		// 进度条显示位置
		progressBar.setPadding(0, 0, 15, 0);
		// 把进度条加入到layout中
		layout.addView(progressBar, mLayoutParams);
		// 文本内容
		TextView textView = new TextView(this);
		textView.setText("加载中...");
		textView.setGravity(Gravity.CENTER_VERTICAL);
		// 把文本加入到layout中
		layout.addView(textView, FFlayoutParams);
		// 设置layout的重力方向，即对齐方式是
		layout.setGravity(Gravity.CENTER);

		// 设置ListView的页脚layout
		loadingLayout = new LinearLayout(this);
		loadingLayout.addView(layout, mLayoutParams);
		loadingLayout.setGravity(Gravity.CENTER);
		// 添加到脚页显示
		appList.addFooterView(loadingLayout);
		// 首次的页面显示
		nowSortType = appSortType.getSelectedItem().toString().trim();
		nowSrotDirection = 0;
		mData = getData();
		adapter = new NewAppListAdapter(this, mData, this);
		nowPosition = adapter.getCount();
		// 给ListView添加适配器
		appList.setAdapter(adapter);
		// 给ListView注册滚动监听
		appList.setOnScrollListener(this);
		appType.setSelection(0, false);
		appType.setOnItemSelectedListener(this);
		appSortType.setSelection(0, false);
		appSortType.setOnItemSelectedListener(this);
		// 玩游戏
		// playGame.setOnClickListener(this);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	/**
	 * 滑动监听事件，到底刷新
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (firstVisibleItem + visibleItemCount == totalItemCount && !isOver) {
			// 开线程去下载网络数据
			if (mThread == null || !mThread.isAlive()) {
				mThread = new Thread() {
					@Override
					public void run() {
						Looper.prepare();
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						mData = getData();
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
				};
				mThread.start();
			}

		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	/*****
	 * 选择列表监听，刷新数据
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.appType:
			nowSrotDirection = 0;
			break;
		case R.id.appSortType:
			Log.e("Tag", "111111");
			if (nowSortType.equals(appSortType.getItemAtPosition(position)
					.toString().trim()))
				nowSrotDirection = (nowSrotDirection + 1) % 2;
			else {
				nowSortType = appSortType.getItemAtPosition(position)
						.toString().trim();
				nowSrotDirection = 0;
			}
			break;
		default:
			break;
		}
		refresh();

	}

	/*****
	 * 按钮点击
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;

		}
	}

	/***
	 * 每次选择刷新数据
	 */
	private void refresh() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				nowPosition = 0;
				mData = getData();

				Message message = new Message();
				message.what = 2;
				handler.sendMessage(message);
			}
		};
		thread.start();
	}

	/***
	 * 获取数据
	 * 
	 * @return
	 */
	private List<AdvertisementItem> getData() {
		return new AdvertisementOpration().getData(this, appType
				.getSelectedItem().toString().trim(), nowSortType,
				nowSrotDirection, nowPosition / 10 + 1);
	}

	/**
	 * 主线程接收数据
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 数据下拉更新
			case 1:
				if (mData == null || mData.size() < 10) {
					appList.removeFooterView(loadingLayout);
					isOver = true;
				}
				adapter.addData(mData);
				nowPosition = adapter.getCount();
				Log.e("log_tag", "" + nowPosition);
				adapter.notifyDataSetChanged();
				break;
			// 数据选择刷新
			case 2:
				adapter.getNewData(mData);
				nowPosition = adapter.getCount();
				if (nowPosition < 10) {
					appList.removeFooterView(loadingLayout);
					isOver = true;
				} else {
					appList.addFooterView(loadingLayout);
					isOver = false;
				}
				adapter.notifyDataSetChanged();
				appList.setSelection(0);
				break;
			// 下载完成
			case 3:

				Button button = (Button) msg.obj;

				button.setCompoundDrawablesWithIntrinsicBounds(getResources()
						.getDrawable(R.drawable.open_apk), null, null, null);
				button.setText("打开");
				button.setClickable(true);
				break;
			// 下载失败，可重新下载
			case 4:
				Button button1 = (Button) msg.obj;
				button1.setCompoundDrawablesWithIntrinsicBounds(getResources()
						.getDrawable(R.drawable.down_load), null, null, null);
				button1.setText("下载");
				button1.setClickable(true);
				break;
			default:
				break;
			}
		}

	};

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

	/*********************
	 * 列表中的下载按钮点击事件
	 */
	@Override
	public void click(View v) {

		final String urlString = adapter.getItem((Integer) v.getTag())
				.getApkUrl();
		final Button button = (Button) v;
		Log.e("log_tag", urlString);
		// 初始，未下载点击，开始下载
		if (button.getText().toString().trim().equals("下载")) {
			button.setCompoundDrawables(null, null, null, null);
			button.setText("下载中..");
			button.setClickable(false);
			Thread thread = new Thread() {
				public void run() {
					Looper.prepare();
					Message message = new Message();
					message.obj = button;
					try {
						// 下载成功
						if (new AdvertisementOpration().apkDownLoad(urlString)) {
							message.what = 3;
						} else {
							// 下载失败
							message.what = 4;
						}
						handler.sendMessage(message);
					} catch (IOException e) {
						// 下载失败
						message.what = 4;
						handler.sendMessage(message);
					}
				};
			};
			thread.start();
		} else {
			String dirName = Environment.getExternalStorageDirectory()
					+ "/MyDownload/";
			File f = new File(dirName);
			Log.e("log_tag", "链接");
			if (!f.exists()) {
				f.mkdir();
				Log.e("log_tag", "链接1111111");
			}
			Log.e("log_tag", "链接");
			String newFilename = urlString
					.substring(urlString.lastIndexOf("/") + 1);
			newFilename = dirName + newFilename;
			File file = new File(newFilename);

			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file),
					"application/vnd.android.package-archive");
			startActivity(intent);

		}
		// try {
		// if (new AdvertisementOpration().apkDownLoad(adapter.getItem(
		// (Integer) v.getTag()).getApkUrl()))
		// button.setText("下载完毕");
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	/************
	 * 左键菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.make_data_page_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**********
	 * 左键菜单点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.play_game:
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), GameOf2048.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
