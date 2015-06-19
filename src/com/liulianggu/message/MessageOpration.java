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
 * 发送并接受解析信息，获取流量值
 * 
 * @author xyc
 *
 */
public class MessageOpration extends Activity {
	// 信息收发相关
	private SmsObserver smsObserver;
	private Uri SMS_INBOX = Uri.parse("content://sms/inbox");
	// 是否允许监听信息变更
	private boolean allowAcept = true;
	// 是否获取到流量
	private boolean getFlow = false;
	// 获取到的流量
	private float flow;

	/*********************
	 * 通过发送短信获取流量值
	 * 
	 * @return
	 */
	public float getFlowFromMessage(Activity activity) {

		smsObserver = new SmsObserver(this, smsHandler);
		activity.getContentResolver().registerContentObserver(SMS_INBOX, true,
				smsObserver);

		String smsRet = sendSMS("10086", "3");
		if (smsRet.equals("发送成功！")) {
			allowAcept = true;
		} else {
			Toast.makeText(getApplicationContext(), "流量获取失败",
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
	 * 发送短信函数 需用到<uses-permission android:name="android.permission.SEND_SMS"
	 * />权限
	 * 
	 * @param destinationAddress
	 *            目标手机号
	 * @param message
	 *            短信内容
	 * @return
	 */
	private String sendSMS(String destinationAddress, String message) {
		String strRet = "";
		// 获取信息内容
		// 移动运营商允许每次发送的字节数据有限，
		// 我们可以使用Android给我们提供 的短信工具。
		if (message != null) {
			try {
				SmsManager sms = SmsManager.getDefault();
				// 如果短信没有超过限制长度，则返回一个长度的List。
				List<String> texts = sms.divideMessage(message);
				for (String text : texts) {
					sms.sendTextMessage(destinationAddress, null, text, null,
							null);
					/*********************************************************
					 * 说明sms.sendTextMessage(destinationAddress, scAddress,
					 * text, sentIntent, deliveryIntent)：
					 * destinationAddress:接收方的手机号码 scAddress:短信服务中心号码(null即可)
					 * text:信息内容 sentIntent:发送是否成功的回执， DeliveryIntent:接收是否成功的回执。
					 *********************************************************/
				}
			} catch (Exception ex) {
				strRet = "发送失败：" + ex.getMessage();
				Log.d("Error in SendingSms", ex.getMessage());
			}
			strRet = "发送成功！";
		}
		return strRet;
	}

	/******
	 * 短信收取的监听
	 */
	public static Handler smsHandler = new Handler() {

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
