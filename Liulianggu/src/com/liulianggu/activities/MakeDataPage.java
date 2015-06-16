package com.liulianggu.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
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

import com.liulianggu.adapter.NewAppListAdapter;
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
		OnItemSelectedListener, OnClickListener {
	private Button playGame;
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
	private int isFirst = 0;
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
		playGame = (Button) findViewById(R.id.play_game);
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
		adapter = new NewAppListAdapter(this, mData);
		// 给ListView添加适配器
		appList.setAdapter(adapter);
		// 给ListView注册滚动监听
		appList.setOnScrollListener(this);
		appType.setOnItemSelectedListener(this);
		appSortType.setOnItemSelectedListener(this);
		//
		playGame.setOnClickListener(this);
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
		if (firstVisibleItem + visibleItemCount == totalItemCount) {
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
		if (isFirst < 2) {
			isFirst++;
			return;
		}

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

		case R.id.play_game:
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), GameOf2048.class);
			startActivity(intent);
			finish();
			break;

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
				nowSrotDirection, nowPosition, nowPosition + 9);
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
				adapter.addData(mData);
				nowPosition = adapter.getCount();
				// 重新刷新Listview的adapter里面数据
				adapter.notifyDataSetChanged();
				break;
			// 数据选择刷新
			case 2:
				adapter.getNewData(mData);
				nowPosition = adapter.getCount();
				adapter.notifyDataSetChanged();
				appList.setSelection(0);
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

}
