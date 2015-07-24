package com.liulianggu.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.liulianggu.application.PersonalData;
import com.liulianggu.tabmenu.R;
import com.liulianggu.userOpration.FlowOpration;

/*******************************
 * 取流量界面
 * 
 * @author xyc
 *
 */
public class TakeDataPage extends Activity implements OnClickListener {
	/** 取流量 */
	private EditText get;
	private TextView all;
	private PersonalData app;
	// private Button cancel;
	private Button ok;
	public static TakeDataPage takeDataPage = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_data_page);
		takeDataPage = this;
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		app = (PersonalData) getApplication();
		String gprs = String.valueOf(app.getGprs());
		// 需要获取数据库的流量值，在此处设为111

		get = (EditText) findViewById(R.id.qcll);
		ok = (Button) findViewById(R.id.get_ok);
		all = (TextView) findViewById(R.id.kyll);
		// cancel = (Button) findViewById(R.id.cancel_ok);
		all.setText(gprs);
		ok.setOnClickListener(TakeDataPage.this);
		// cancel.setOnClickListener(TakeDataPage.this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.get_ok:
			FlowOpration flowOpration = new FlowOpration(
					(PersonalData) getApplication());

			if (!app.isLog()) {
				Toast.makeText(getApplicationContext(), "用户未登陆，请登陆后再试",
						Toast.LENGTH_SHORT).show();
				break;
			} else if (get.getText().toString().trim().isEmpty()) {
				Toast.makeText(getApplicationContext(), "请输入正确的流量值",
						Toast.LENGTH_SHORT).show();
			} else if (flowOpration.takeFlow(Float.parseFloat(get.getText()
					.toString().trim()))) {
				Toast.makeText(getApplicationContext(), "流量提取成功！",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "流量提取失败！",
						Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
	}

	public void freash() {
		all.setText(String.valueOf(app.getGprs()));
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
