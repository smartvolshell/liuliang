package com.liulianggu.message;

import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.liulianggu.infroParse.RestCharge;
import com.liulianggu.infroParse.RestChargeDetail;
import com.liulianggu.tabmenu.R;

/**************************
 * ���Ͳ����ܽ�����Ϣ����ȡ����ֵ
 * 
 * @author xyc
 *
 */
public class MessageOpration extends Activity {
	// ��Ϣ�շ����
	private SmsObserver smsObserver;
	private Uri SMS_INBOX = Uri.parse("content://sms/inbox");
	// �Ƿ����������Ϣ���
	private boolean allowAcept = true;
	// �Ƿ��ȡ������
	private boolean getFlow = false;
	// ��ȡ��������
	private float flow;

	/*********************
	 * ͨ�����Ͷ��Ż�ȡ����ֵ
	 * 
	 * @return
	 */
	public float getFlowFromMessage(Activity activity) {

		smsObserver = new SmsObserver(this, smsHandler);
		activity.getContentResolver().registerContentObserver(SMS_INBOX, true,
				smsObserver);

		String smsRet = sendSMS("10086", "3");
		if (smsRet.equals("���ͳɹ���")) {
			allowAcept = true;
		} else {
			Toast.makeText(getApplicationContext(), "������ȡʧ��",
					Toast.LENGTH_SHORT).show();
			findViewById(R.id.btn_take_data).setClickable(true);
			return -1;
		}
		while (true) {
			Log.e("log_tag", "" + getFlow + "111" + allowAcept + "3333");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (getFlow)
				return flow;
		}

	}

	/**
	 * ���Ͷ��ź��� ���õ�<uses-permission android:name="android.permission.SEND_SMS"
	 * />Ȩ��
	 * 
	 * @param destinationAddress
	 *            Ŀ���ֻ���
	 * @param message
	 *            ��������
	 * @return
	 */
	private String sendSMS(String destinationAddress, String message) {
		String strRet = "";
		// ��ȡ��Ϣ����
		// �ƶ���Ӫ������ÿ�η��͵��ֽ��������ޣ�
		// ���ǿ���ʹ��Android�������ṩ �Ķ��Ź��ߡ�
		if (message != null) {
			try {
				SmsManager sms = SmsManager.getDefault();
				// �������û�г������Ƴ��ȣ��򷵻�һ�����ȵ�List��
				List<String> texts = sms.divideMessage(message);
				for (String text : texts) {
					sms.sendTextMessage(destinationAddress, null, text, null,
							null);
					/*********************************************************
					 * ˵��sms.sendTextMessage(destinationAddress, scAddress,
					 * text, sentIntent, deliveryIntent)��
					 * destinationAddress:���շ����ֻ����� scAddress:���ŷ������ĺ���(null����)
					 * text:��Ϣ���� sentIntent:�����Ƿ�ɹ��Ļ�ִ�� DeliveryIntent:�����Ƿ�ɹ��Ļ�ִ��
					 *********************************************************/
				}
			} catch (Exception ex) {
				strRet = "����ʧ�ܣ�" + ex.getMessage();
				Log.d("Error in SendingSms", ex.getMessage());
			}
			strRet = "���ͳɹ���";
		}
		return strRet;
	}

	/******
	 * ������ȡ�ļ���
	 */
	public static Handler smsHandler = new Handler() {

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
				if (!res.isEmpty()) {
					RestChargeDetail resCh = new RestChargeDetail();
					List<RestCharge> restCharges = resCh.getAllRestCharge(res);
					for (int i = 0; i < restCharges.size(); i++) {
						allGprs += restCharges.get(i).getRestGprs();
					}
					flow = allGprs;
					getFlow = true;
					allowAcept = false;
				}
			}
		}

		private String getSmsFromPhone() {
			String result = "";
			ContentResolver cr = getContentResolver();
			String[] projection = new String[] { "body" };
			String where = " address = '10086'";
			Cursor cur = cr.query(SMS_INBOX, projection, where, null,
					"date desc");

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
	}
}
