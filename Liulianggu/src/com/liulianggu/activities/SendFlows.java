package com.liulianggu.activities;

import org.androidpn.client.ServiceManager;

import com.liulianggu.application.PersonalData;
import com.liulianggu.tabmenu.R;
import com.liulianggu.tabmenu.R.id;
import com.liulianggu.tabmenu.R.layout;
import com.liulianggu.userOpration.FlowOpration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/****************
 * 赠送流量页面
 * 
 * @author xyc
 *
 */
public class SendFlows extends Activity {

	EditText receiverNum;
	EditText flowNum;
	Button sure;
	Button cancel;
	Button backButton;

	private PersonalData appData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_send_flows);
		init();

		// 获取流量接收者的号码信息
		Intent intent1 = getIntent();
		String numString = intent1.getStringExtra("receiverNum");
		if (!numString.equals("")) {
			receiverNum.setText(numString);
			receiverNum.setFocusable(false);
		}
		// 确定按钮点击事件
		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				float flow = Float.parseFloat(flowNum.getText().toString()
						.trim());
				String phoneNum = receiverNum.getText().toString().trim();
				FlowOpration flowOpration = new FlowOpration(
						(PersonalData) getApplication());
				if (flowOpration.sendFlow(phoneNum, flow)) {
					// Intent intent2 = new Intent(SendFlows.this,
					// LiuLianggu.class);
					// startActivity(intent2);
					if (SaveDataPage.saveDataPage != null)
						SaveDataPage.saveDataPage.freash();
					appData.getServiceManager().sendFlow(phoneNum,
							String.valueOf(flow));
					SendFlows.this.finish();
				} else {
					Toast.makeText(SendFlows.this, "赠送失败", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	/***
	 * 绑定控件
	 */
	private void init() {
		receiverNum = (EditText) findViewById(R.id.receiverNum);
		flowNum = (EditText) findViewById(R.id.flowNum);
		sure = (Button) findViewById(R.id.sure);
		cancel = (Button) findViewById(R.id.cancel);
		appData = (PersonalData) getApplication();
		backButton = (Button) findViewById(R.id.send_back_btn);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SendFlows.this.finish();
			}
		});
	}

}
