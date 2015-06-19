package com.liulianggu.message;

import java.util.List;
import android.telephony.SmsManager;
import android.util.Log;

/**************************
 * ������Ϣ
 * 
 * @author xyc
 *
 */
public class SendMessage {
	/*********************
	 * ͨ�����Ͷ��Ų��ж��Ƿ�ɹ�
	 * 
	 * @return
	 */
	public boolean sendMessage() {
		String smsRet = sendSMS("10086", "3");
		if (smsRet.equals("���ͳɹ���")) {
			return true;
		} else {
			return false;
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
}
