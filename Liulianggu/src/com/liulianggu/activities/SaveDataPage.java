package com.liulianggu.activities;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.liulianggu.adapter.NewAppListAdapter;
import com.liulianggu.adapter.NewAppListAdapter.Callback;
import com.liulianggu.application.PersonalData;
import com.liulianggu.beans.AdvertisementItem;
import com.liulianggu.infroParse.RestCharge;
import com.liulianggu.infroParse.RestChargeDetail;
import com.liulianggu.tabmenu.R;
import com.liulianggu.userOpration.AdvertisementOpration;
import com.liulianggu.userOpration.FlowOpration;
import com.liulianggu.utils.SendMessageService;

/**********************
 * ����������
 * 
 * @author xyc
 *
 */
public class SaveDataPage extends Activity implements OnClickListener, Callback {
	private final Intent intent = new Intent();
	private static int TIME_SEND = 1;
	private PersonalData app;
	// private Button btnTakeData;
	private TextView txtShowData;
	private TextView txtWelcome;
	private ListView mListView;
	// Ӧ���б�
	NewAppListAdapter adapter;
	private List<AdvertisementItem> mData;

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
		// getData();

	}

	// ��ʼ������ʾ���ؼ���
	private void init() {
		intent.setClass(SaveDataPage.this, SendMessageService.class);
		txtShowData = (TextView) this.findViewById(R.id.txt_show_gprs);
		txtWelcome = (TextView) findViewById(R.id.welcome);
		mListView = (ListView) findViewById(R.id.advertisements);
		// btnTakeData.setOnClickListener(SaveDataPage.this);
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
			Log.e("log_tag", "111");
			super.onChange(selfChange);
			Log.e("log_tag", "222");
			String res = "";
			float allGprs = 0;
			if (allowAcept) {
				Log.e("log_tag", "333");
				res = getSmsFromPhone();
				Log.e("log_tag", "444");
				// ��ȡ����
				if (!res.isEmpty()) {
					RestChargeDetail resCh = new RestChargeDetail();
					List<RestCharge> restCharges = resCh.getAllRestCharge(res);
					for (int i = 0; i < restCharges.size(); i++) {
						allGprs += restCharges.get(i).getRestGprs();
					}
					Log.e("log_tag", "555");
					FlowOpration flowOpration = new FlowOpration(
							(PersonalData) getApplication());
					// �洢�ѽ�����������
					if (flowOpration.saveFlow(allGprs)) {
						Log.e("log_tag", "666");
						Toast toast3 = Toast.makeText(SaveDataPage.this,
								"�ɹ�����������" + allGprs + "MB", Toast.LENGTH_SHORT);
						Log.e("log_tag", "777");
						toast3.setGravity(Gravity.TOP, 0, 0);
						toast3.show();
						app.setGprs(app.getGprs() + allGprs);
						txtShowData.setText(String.valueOf(app.getGprs()));
						// Intent intent1 = new Intent(SaveDataPage.this,
						// LiuLianggu.class);
						// intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						// startActivity(intent1);
					} else {
						Toast.makeText(SaveDataPage.this, "��������ʧ��",
								Toast.LENGTH_SHORT).show();
					}

				} else {
					Toast.makeText(SaveDataPage.this, "������ȡʧ��",
							Toast.LENGTH_SHORT).show();
				}
				// btnTakeData.setClickable(true);
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

	private void getData() {
		Thread thread = new Thread() {
			public void run() {
				Looper.prepare();
				mData = new AdvertisementOpration().getData(SaveDataPage.this,
						"ȫ��", "����", 2, 1);
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			};
		};
		thread.start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				adapter = new NewAppListAdapter(SaveDataPage.this, mData,
						SaveDataPage.this);
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
				break;
			// �������
			case 3:
				Button button = (Button) msg.obj;
				button.setText("��");
				button.setClickable(true);
				break;
			// ����ʧ�ܣ�����������
			case 4:
				Button button1 = (Button) msg.obj;
				button1.setText("����");
				button1.setClickable(true);
				break;
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

	/************
	 * ����˵�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.save_data_page_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**********
	 * ����˵�����¼�
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.get_flow_test:
			// btnTakeData.setClickable(false);
			if (app.isLog()) {
				// �����Ƿ��ͳɹ��������Ƿ�����¶���
				// if (new SendMessage().sendMessage()) {
				// // btnTakeData.setClickable(true);
				// Toast.makeText(getApplicationContext(), "��Ϣ���ͳɹ�",
				// Toast.LENGTH_SHORT).show();
				// allowAcept = true;// ���������������
				//
				// } else {
				// Toast.makeText(getApplicationContext(), "��Ϣ����ʧ�ܣ�",
				// Toast.LENGTH_SHORT).show();
				// // btnTakeData.setClickable(true);
				// }
				intent.putExtra("timesend", TIME_SEND);
				startService(intent);

				Toast.makeText(getApplicationContext(), "һ����֮���ȡ����",
						Toast.LENGTH_SHORT).show();
				allowAcept = true;// ���������������
			} else {
				Toast.makeText(getApplicationContext(), "���ȵ�¼",
						Toast.LENGTH_SHORT).show();
				findViewById(R.id.btn_take_data).setClickable(true);
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/*********************
	 * �б��е����ذ�ť����¼�
	 */
	@Override
	public void click(View v) {

		final String urlString = adapter.getItem((Integer) v.getTag())
				.getApkUrl();
		final Button button = (Button) v;
		// ��ʼ��δ���ص������ʼ����
		if (button.getText().toString().trim().equals("����")) {
			button.setText("������..");
			button.setClickable(false);
			Thread thread = new Thread() {
				public void run() {
					Looper.prepare();
					Message message = new Message();
					message.obj = button;
					try {
						// ���سɹ�
						if (new AdvertisementOpration().apkDownLoad(urlString)) {
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
		// button.setText("�������");
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

}
