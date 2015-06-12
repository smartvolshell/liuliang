package com.liulianggu.activities;

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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/****************
 * ��������ҳ��
 * 
 * @author xyc
 *
 */
public class SendFlows extends Activity {

	EditText receiverNum;
	EditText flowNum;
	Button sure;
	Button cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_flows);
		init();

		// ��ȡ���������ߵĺ�����Ϣ
		Intent intent1 = getIntent();
		String numString = intent1.getStringExtra("receiverNum");
		if (!numString.equals("")) {
			receiverNum.setText(numString);
			receiverNum.setFocusable(false);
		}
		// ȷ����ť����¼�
		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				float flow = Float.parseFloat(flowNum.getText().toString()
						.trim());
				String phoneNum = receiverNum.getText().toString().trim();
				FlowOpration flowOpration = new FlowOpration(
						(PersonalData) getApplication());
				if (flowOpration.sendFlow(phoneNum, flow)) {
					Intent intent2 = new Intent(SendFlows.this,
							LiuLianggu.class);
					startActivity(intent2);
				} else {
					Toast.makeText(SendFlows.this, "����ʧ��", Toast.LENGTH_LONG)
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
	 * �󶨿ؼ�
	 */
	private void init() {
		receiverNum = (EditText) findViewById(R.id.receiverNum);
		flowNum = (EditText) findViewById(R.id.flowNum);
		sure = (Button) findViewById(R.id.sure);
		cancel = (Button) findViewById(R.id.cancel);
	}

}
