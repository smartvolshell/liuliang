package com.liulianggu.activities;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
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

import com.liulianggu.adapter.NewAppListAdapter;
import com.liulianggu.application.PersonalData;
import com.liulianggu.beans.AdvertisementItem;
import com.liulianggu.infroParse.RestCharge;
import com.liulianggu.infroParse.RestChargeDetail;
import com.liulianggu.server.SendMessageService;
import com.liulianggu.tabmenu.R;
import com.liulianggu.userOpration.AdvertisementOpration;
import com.liulianggu.userOpration.FlowOpration;
import com.liulianggu.utils.MyToast;

/**********************
 * ����������
 * 
 */
@SuppressLint("HandlerLeak")
public class SaveDataPage extends Activity implements OnClickListener {
	private final Intent intent = new Intent();
	private static int TIME_SEND = 1;
	private PersonalData app;
	private Button btnTakeData;
	private TextView txtShowData;
	private TextView txtWelcome;
	private ListView mListView;
	// Ӧ���б�
	NewAppListAdapter adapter;

	// private Intent intent = new Intent();
	// ��Ϣ�շ����
	private SmsObserver smsObserver;
	private Uri SMS_INBOX = Uri.parse("content://sms/inbox");
	// �Ƿ����������Ϣ���
	private boolean allowAcept = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.save_data_page);
		init();

		// ����������ȡ
		smsObserver = new SmsObserver(this, smsHandler);
		getContentResolver().registerContentObserver(SMS_INBOX, true,
				smsObserver);
		// app�б��ʵ��
		adapter = new NewAppListAdapter(this,
				new AdvertisementOpration().getData(this, "ȫ��", "����", 2, 0, 9));
		mListView.setAdapter(adapter);
		// �б�ĵ���¼�
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AdvertisementItem item = adapter.getItem(position);
				Intent intent1 = new Intent(SaveDataPage.this,
						AdvertisementDetial.class);
				intent1.putExtra("oldRating", item.getEvaluation());
				intent1.putExtra("detail", item.getAppName());
				intent1.putExtra("title", item.getAppMsg());
				startActivity(intent1);

			}
		});
	}

	// ��ʼ������ʾ���ؼ���
	private void init() {
		/** ע�ᶨʱ���Ͷ��ŷ��� */
		intent.setClass(SaveDataPage.this, SendMessageService.class);

		btnTakeData = (Button) this.findViewById(R.id.btn_take_data);
		txtShowData = (TextView) this.findViewById(R.id.txt_show_gprs);
		txtWelcome = (TextView) findViewById(R.id.welcome);
		mListView = (ListView) findViewById(R.id.advertisements);
		btnTakeData.setOnClickListener(SaveDataPage.this);
		app = (PersonalData) getApplication();

		if (app.isLog()) {
			String gp = String.valueOf(app.getGprs());
			txtShowData.setText(gp);
			String welc = "��ӭ��" + app.getNickName();
			txtWelcome.setText(welc);
		}

	}

	// ��ȡ������ť����¼�
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_take_data:
			btnTakeData.setClickable(false);
			if (app.isLog()) {
				/***
				 * // �����Ƿ��ͳɹ��������Ƿ�����¶��� if (new SendMessage().sendMessage()) {
				 * // btnTakeData.setClickable(true);
				 * Toast.makeText(getApplicationContext(), "��Ϣ���ͳɹ�",
				 * Toast.LENGTH_SHORT).show(); allowAcept = true;// ���������������
				 * 
				 * } else { Toast.makeText(getApplicationContext(), "��Ϣ����ʧ�ܣ�",
				 * Toast.LENGTH_SHORT).show(); btnTakeData.setClickable(true); }
				 ***/
				/** �O�Æ��ӷ��յĕr�g�g����һ���R */
				intent.putExtra("timesend", TIME_SEND);
				startService(intent);

				MyToast.showMesg(SaveDataPage.this, "һ����֮���ȡ����");
				btnTakeData.setClickable(true); // ��ȡ��ť����
				allowAcept = true;// ���������������
			} else {

				MyToast.showMesg(SaveDataPage.this, "���ȵ�¼");
				findViewById(R.id.btn_take_data).setClickable(true);
			}
			break;

		default:
			break;
		}
	}

	/******
	 * ������ȡ�ļ���
	 */
	public Handler smsHandler = new Handler() {

	};

	/**********
	 * �������ŵı��
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
				// ��ȡ����
				if (!res.isEmpty()) {
					RestChargeDetail resCh = new RestChargeDetail();
					List<RestCharge> restCharges = resCh.getAllRestCharge(res);
					for (int i = 0; i < restCharges.size(); i++) {
						allGprs += restCharges.get(i).getRestGprs();
					}
					FlowOpration flowOpration = new FlowOpration(
							(PersonalData) getApplication());
					// �洢�ѽ�����������
					if (flowOpration.saveFlow(allGprs)) {
						Toast toast3 = Toast.makeText(SaveDataPage.this,
								"�ɹ�����������" + allGprs + "MB", Toast.LENGTH_SHORT);
						toast3.setGravity(Gravity.TOP, 0, 0);
						toast3.show();
						Intent intent1 = new Intent(SaveDataPage.this,
								LiuLianggu.class);
						intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent1);
					} else {
						Toast.makeText(SaveDataPage.this, "��������ʧ��",
								Toast.LENGTH_SHORT).show();
					}

				} else {
					Toast.makeText(SaveDataPage.this, "������ȡʧ��",
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
