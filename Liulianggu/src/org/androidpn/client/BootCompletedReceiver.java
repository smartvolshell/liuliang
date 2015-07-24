package org.androidpn.client;

import org.jivesoftware.smackx.carbons.Carbon.Private;

import com.liulianggu.tabmenu.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class BootCompletedReceiver extends BroadcastReceiver {

	ServiceManager serviceManager ;
	@Override
	public void onReceive(Context context, Intent arg1) {

		//Toast.makeText(context, "Boot completed", Toast.LENGTH_LONG).show();
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		if(sharedPreferences.getBoolean(Constants.SETTINGS_AUTO_START, true)){
			serviceManager = new ServiceManager(context);
	        serviceManager.setNotificationIcon(R.drawable.notification);//…Ë÷√Õ∆ÀÕÕº±Í
	        serviceManager.startService();
		}
	}

}
