package com.liulianggu.message;

import java.util.List;
import android.telephony.SmsManager;
import android.util.Log;

/**************************
 * 发送信息
 * 
 * @author xyc
 *
 */
public class SendMessage {
	/*********************
	 * 通过发送短信并判断是否成功
	 * 
	 * @return
	 */
	public boolean sendMessage() {
		String smsRet = sendSMS("10086", "3");
		if (smsRet.equals("发送成功！")) {
			return true;
		} else {
			return false;
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
}
