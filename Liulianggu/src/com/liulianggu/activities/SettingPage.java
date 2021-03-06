package com.liulianggu.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.sharesdk.demo.wxapi.ShareLiulianggu;

import com.liulianggu.application.PersonalData;
import com.liulianggu.tabmenu.R;

/***********************
 * 设置界面
 * 
 * @author xyc
 *
 */
public class SettingPage extends Activity implements OnClickListener {
	private TextView textButton;
	private PersonalData app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_page);
		init();
	}

	private void init() {
		app = (PersonalData) getApplication();
		textButton = (TextView) findViewById(R.id.attention_user);
		textButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.attention_user:
			ShareLiulianggu.share(this);
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setMessage("确定退出？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						System.exit(0);
					}
				}).setNegativeButton("取消", null).show();
	}

}
