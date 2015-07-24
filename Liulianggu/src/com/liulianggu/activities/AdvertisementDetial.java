package com.liulianggu.activities;

import java.io.File;
import java.io.IOException;

import com.liulianggu.adapter.AppDetailAdapter;
import com.liulianggu.tabmenu.R;
import com.liulianggu.userOpration.AdvertisementOpration;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class AdvertisementDetial extends Activity {
	private ImageView image;
	private ImageView app_back;
	private TextView app_title1;
	private TextView app_message1;
	private RatingBar rating;
	private TextView app_score;
	private TextView app_title2;
	private TextView app_message2;
	private Button down_button;
	private TextView app_type;
	private TextView app_name;
	private Intent intent1;
	// ͼƬ������
	private ViewPager mViewPager;
	// װImageView����
	private ImageView[] mImageViews;
	// ͼƬ��Դid
	private int[] imgIdArray;
	// ͼƬ������������
	private AppDetailAdapter mAdapter;

	// ������APP��Ϣ
	private String appName;
	private String appType;
	private String appMsg;
	private String appUrl;
	private float appEvaluation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		setContentView(R.layout.activity_advertisement_detial);
		intent1 = getIntent();
		appName = intent1.getStringExtra("appName");
		appType = intent1.getStringExtra("appType");
		appMsg = intent1.getStringExtra("appMsg");
		appUrl = intent1.getStringExtra("appUrl");
		appEvaluation = intent1.getFloatExtra("appRating", 0);
		init();
	}

	private void init() {
		app_title1 = (TextView) findViewById(R.id.app_title1);
		app_title2 = (TextView) findViewById(R.id.app_title2);
		app_message1 = (TextView) findViewById(R.id.app_message1);
		app_message2 = (TextView) findViewById(R.id.app_message2);
		rating = (RatingBar) findViewById(R.id.app_rating);
		app_score = (TextView) findViewById(R.id.app_score);
		down_button = (Button) findViewById(R.id.app_down_load2);
		app_type = (TextView) findViewById(R.id.app_type);
		app_name = (TextView) findViewById(R.id.app_type_name);
		app_back = (ImageView) findViewById(R.id.app_back);
		mViewPager = (ViewPager) findViewById(R.id.viewPager);

		app_type.setText(appType);
		app_title1.setText(appName);
		app_title2.setText(appName);
		app_message1.setText(appMsg);
		app_message2.setText(appMsg);
		app_name.setText(appName);
		Log.e("appEvaluation", "" + appEvaluation);
		rating.setRating(appEvaluation);
		app_score.setText(appEvaluation + "��");
		// ���ذ�ť
		app_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
				AdvertisementDetial.this.finish();
			}
		});
		// ���ذ�ť
		down_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final String urlString = appUrl;

				if (down_button.getText().toString().trim().equals("����")) {
					down_button.setCompoundDrawables(null, null, null, null);
					down_button.setText("������..");
					down_button.setClickable(false);
					Thread thread = new Thread() {
						public void run() {
							Looper.prepare();
							Message message = new Message();
							message.obj = down_button;
							try {
								// ���سɹ�
								if (new AdvertisementOpration()
										.apkDownLoad(urlString)) {
									message.what = 3;
								} else {
									// ����ʧ��
									message.what = 4;
								}
								handler.sendMessage(message);
							} catch (IOException e) {
								// ����ʧ��
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
					Log.e("log_tag", "����");
					if (!f.exists()) {
						f.mkdir();
						Log.e("log_tag", "����1111111");
					}
					Log.e("log_tag", "����");
					String newFilename = urlString.substring(urlString
							.lastIndexOf("/") + 1);
					newFilename = dirName + newFilename;
					File file = new File(newFilename);

					Intent intent = new Intent();
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setAction(android.content.Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(file),
							"application/vnd.android.package-archive");
					startActivity(intent);
				}

			}
		});
		// ����ʵ��ͼƬ����Ч��
		// ����ͼƬ��ԴID
		imgIdArray = new int[] { R.drawable.app1, R.drawable.app2,
				R.drawable.app3 };
		// ��ͼƬװ�ص�������
		mImageViews = new ImageView[imgIdArray.length];
		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(this);
			mImageViews[i] = imageView;
			imageView.setBackgroundResource(imgIdArray[i]);
		}
		mAdapter = new AppDetailAdapter(mImageViews);
		// ����Adapter
		mViewPager.setAdapter(mAdapter);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// �������
			case 3:
				Button button = (Button) msg.obj;
				button.setCompoundDrawablesWithIntrinsicBounds(getResources()
						.getDrawable(R.drawable.open_apk), null, null, null);
				button.setText("��");
				button.setClickable(true);
				break;
			// ����ʧ�ܣ�����������
			case 4:
				Button button1 = (Button) msg.obj;
				button1.setCompoundDrawablesWithIntrinsicBounds(getResources()
						.getDrawable(R.drawable.down_load), null, null, null);
				button1.setText("����");
				button1.setClickable(true);
				break;
			default:
				break;
			}
		}
	};

}
