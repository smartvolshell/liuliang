package com.liulianggu.utils;

import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * @author volshell
 * 
 *         发送段信息服务
 */
public class SendMessageService extends Service {

	WakeLock wakeLock;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/******
	 * 关闭屏幕的时候，可以唤醒屏幕
	 */
	public void onCreate() {

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
				SendMessageService.class.getName());
		wakeLock.acquire();
		super.onCreate();
	}

	@SuppressWarnings("deprecation")
	public void onStart(Intent intent, int startId) {
		int timesend = intent.getExtras().getInt("timesend");
		Timer timer = new Timer();
		/** 启动定时活动 */
		TimerTask ttTask = new TimerTask() {

			@SuppressLint("UnlocalizedSms")
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage("10086", null, "3", null, null);
			}
		};
		timer.schedule(ttTask, (long) (timesend * 6e4));
		super.onStart(intent, startId);
	}

	public void onDestroy() {
		if (wakeLock != null) {
			wakeLock.release();
			wakeLock = null;
		}
		Log.d("sm", "stop");
		super.onDestroy();
	}

}