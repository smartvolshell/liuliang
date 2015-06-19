package com.liulianggu.message;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class GetMessage extends BroadcastReceiver {

	// �Ƿ����������Ϣ���

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		SmsMessage msg = null;
		Bundle bundle = arg1.getExtras();
		if (bundle != null) {
			Object[] pdusObj = (Object[]) bundle.get("pdus");
			for (Object p : pdusObj) {
				msg = SmsMessage.createFromPdu((byte[]) p);

				String msgTxt = msg.getMessageBody();// �õ���Ϣ������

				Date date = new Date(msg.getTimestampMillis());// ʱ��
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String receiveTime = format.format(date);

				String senderNumber = msg.getOriginatingAddress();
				Log.i("log_tag", msgTxt);
				if (msgTxt.equals("Testing!")) {
					Toast.makeText(arg0, "success!", Toast.LENGTH_LONG).show();
					System.out.println("success!");
					return;
				} else {
					Toast.makeText(arg0, msgTxt, Toast.LENGTH_LONG).show();
					System.out.println("�����ˣ�" + senderNumber + "  �������ݣ�"
							+ msgTxt + "����ʱ�䣺" + receiveTime);
					return;
				}
			}
			return;
		}
	}

}
