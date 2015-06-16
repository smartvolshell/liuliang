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
 * ׬ȡ������ؽ���
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
	// ���岼��
	LinearLayout loadingLayout;
	// ����ˢ���߳�
	private Thread mThread;
	private int nowPosition = 0;
	private int isFirst = 0;
	// ��ǰ���������
	private String nowSortType;
	private int nowSrotDirection;
	/**
	 * ���ò�����ʾ����
	 */
	private LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);
	/**
	 * ���ò�����ʾĿ���������
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
	 * �󶨿ؼ�
	 */
	private void init() {
		playGame = (Button) findViewById(R.id.play_game);
		appList = (ListView) findViewById(R.id.apps);
		appType = (Spinner) findViewById(R.id.appType);
		appSortType = (Spinner) findViewById(R.id.appSortType);

		// ���Բ���
		LinearLayout layout = new LinearLayout(this);
		// ���ò��� ˮƽ����
		layout.setOrientation(LinearLayout.HORIZONTAL);
		// ������
		progressBar = new ProgressBar(this);
		// ��������ʾλ��
		progressBar.setPadding(0, 0, 15, 0);
		// �ѽ��������뵽layout��
		layout.addView(progressBar, mLayoutParams);
		// �ı�����
		TextView textView = new TextView(this);
		textView.setText("������...");
		textView.setGravity(Gravity.CENTER_VERTICAL);
		// ���ı����뵽layout��
		layout.addView(textView, FFlayoutParams);
		// ����layout���������򣬼����뷽ʽ��
		layout.setGravity(Gravity.CENTER);

		// ����ListView��ҳ��layout
		loadingLayout = new LinearLayout(this);
		loadingLayout.addView(layout, mLayoutParams);
		loadingLayout.setGravity(Gravity.CENTER);
		// ��ӵ���ҳ��ʾ
		appList.addFooterView(loadingLayout);
		// �״ε�ҳ����ʾ
		nowSortType = appSortType.getSelectedItem().toString().trim();
		nowSrotDirection = 0;
		mData = getData();
		adapter = new NewAppListAdapter(this, mData);
		// ��ListView���������
		appList.setAdapter(adapter);
		// ��ListViewע���������
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
	 * ���������¼�������ˢ��
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (firstVisibleItem + visibleItemCount == totalItemCount) {
			// ���߳�ȥ������������
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
	 * ѡ���б������ˢ������
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
	 * ��ť���
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
	 * ÿ��ѡ��ˢ������
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
	 * ��ȡ����
	 * 
	 * @return
	 */
	private List<AdvertisementItem> getData() {
		return new AdvertisementOpration().getData(this, appType
				.getSelectedItem().toString().trim(), nowSortType,
				nowSrotDirection, nowPosition, nowPosition + 9);
	}

	/**
	 * ���߳̽�������
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// ������������
			case 1:
				adapter.addData(mData);
				nowPosition = adapter.getCount();
				// ����ˢ��Listview��adapter��������
				adapter.notifyDataSetChanged();
				break;
			// ����ѡ��ˢ��
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
		new AlertDialog.Builder(this).setMessage("ȷ���˳���")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						System.exit(0);
					}
				}).setNegativeButton("ȡ��", null).show();
	}

}
