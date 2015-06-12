package com.liulianggu.activities;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.liulianggu.adapter.AdvertiseListAdapter;
import com.liulianggu.application.PersonalData;
import com.liulianggu.beans.AdvertisementItem;
import com.liulianggu.infroParse.RestCharge;
import com.liulianggu.infroParse.RestChargeDetail;
import com.liulianggu.message.SendMessage;
import com.liulianggu.tabmenu.R;
import com.liulianggu.userOpration.AdvertisementOpration;
import com.liulianggu.userOpration.FlowOpration;

/**********************
 * 存流量界面
 * 
 * @author xyc
 *
 */
public class SaveDataPage extends Activity implements OnClickListener {

	private PersonalData app;
	private Button btnTakeData;
	private TextView txtShowData;
	private TextView txtWelcome;
	private ListView mListView;
	// 广告列表
	AdvertiseListAdapter adapter;

	// private Intent intent = new Intent();
	// 信息收发相关
	private SmsObserver smsObserver;
	private Uri SMS_INBOX = Uri.parse("content://sms/inbox");
	// 是否允许监听信息变更
	private boolean allowAcept = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.save_data_page);
		init();

		// 监听短信收取
		smsObserver = new SmsObserver(this, smsHandler);
		getContentResolver().registerContentObserver(SMS_INBOX, true,
				smsObserver);
		// 广告列表的实现
		adapter = new AdvertiseListAdapter(this,
				new AdvertisementOpration().getData());
		mListView.setAdapter(adapter);
		// 列表的点击事件
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AdvertisementItem item = adapter.getItem(position);
				Intent intent1 = new Intent(SaveDataPage.this,
						AdvertisementDetial.class);
				intent1.putExtra("oldRating", item.getEvaluation());
				intent1.putExtra("detail", item.getDetail());
				intent1.putExtra("title", item.getTitle());
				startActivity(intent1);

			}
		});
	}

	// 初始界面显示及控件绑定
	private void init() {
		btnTakeData = (Button) this.findViewById(R.id.btn_take_data);
		txtShowData = (TextView) this.findViewById(R.id.txt_show_gprs);
		txtWelcome = (TextView) findViewById(R.id.welcome);
		mListView = (ListView) findViewById(R.id.advertisements);
		btnTakeData.setOnClickListener(SaveDataPage.this);
		app = (PersonalData) getApplication();

		if (app.isLog()) {
			String gp = String.valueOf(app.getGprs());
			txtShowData.setText(gp);
			String welc = "欢迎，" + app.getNickName();
			txtWelcome.setText(welc);
		}

	}

	// 获取流量按钮点击事件
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_take_data:
			btnTakeData.setClickable(false);
			if (app.isLog()) {
				// 短信是否发送成功，决定是否接收新短信
				if (new SendMessage().sendMessage()) {
					// btnTakeData.setClickable(true);
					Toast.makeText(getApplicationContext(), "信息发送成功",
							Toast.LENGTH_SHORT).show();
					allowAcept = true;// 允许解析短信内容

				} else {
					Toast.makeText(getApplicationContext(), "信息发送失败！",
							Toast.LENGTH_SHORT).show();
					btnTakeData.setClickable(true);
				}
			} else {
				Toast.makeText(getApplicationContext(), "请先登录",
						Toast.LENGTH_SHORT).show();
				findViewById(R.id.button1).setClickable(true);
			}
			break;

		default:
			break;
		}
	}

	/******
	 * 短信收取的监听
	 */
	public Handler smsHandler = new Handler() {

	};

	/**********
	 * 监听短信的变更
	 * 
	 * @author xyc
	 *
	 */
	class SmsObserver extends ContentObserver {

		public SmsObserver(Context context, Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			String res = "";
			float allGprs = 0;
			if (allowAcept) {
				res = getSmsFromPhone();
				// 读取短信
				if (!res.isEmpty()) {
					RestChargeDetail resCh = new RestChargeDetail();
					List<RestCharge> restCharges = resCh.getAllRestCharge(res);
					for (int i = 0; i < restCharges.size(); i++) {
						allGprs += restCharges.get(i).getRestGprs();
					}
					FlowOpration flowOpration = new FlowOpration(
							(PersonalData) getApplication());
					// 存储已解析到的流量
					if (flowOpration.saveFlow(allGprs)) {
						Toast toast3 = Toast.makeText(SaveDataPage.this,
								"成功存入流量：" + allGprs + "MB", Toast.LENGTH_SHORT);
						toast3.setGravity(Gravity.TOP, 0, 0);
						toast3.show();
						Intent intent1 = new Intent(SaveDataPage.this,
								LiuLianggu.class);
						intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent1);
					} else {
						Toast.makeText(SaveDataPage.this, "流量存入失败",
								Toast.LENGTH_SHORT).show();
					}

				} else {
					Toast.makeText(SaveDataPage.this, "流量获取失败",
							Toast.LENGTH_SHORT).show();
				}
				btnTakeData.setClickable(true);
			}
		}
	}

	private String getSmsFromPhone() {
		String result = "";
		ContentResolver cr = getContentResolver();
		String[] projection = new String[] { "body" };
		String where = " address = '10086'";
		Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");

		if (null == cur) {
			return result;
		}

		if (cur.moveToNext()) {
			String body = cur.getString(cur.getColumnIndex("body"));
			result = body;
			allowAcept = false;
		}
		return result;
	}

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
