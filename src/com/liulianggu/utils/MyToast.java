package com.liulianggu.utils;

import android.content.Context;
import android.widget.Toast;

public class MyToast {
	public static void showMesg(Context context, String mesg) {
		Toast.makeText(context, mesg, Toast.LENGTH_LONG).show();
	}
}
