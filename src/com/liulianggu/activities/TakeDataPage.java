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
 * ȡ��������
 * 
 * @author xyc
 *
 */
public class TakeDataPage extends Activity implements OnClickListener {
	/** ȡ���� */
	private EditText get;
	private TextView all;
	private PersonalData app;
	private Button cancel;
	private Button ok;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_data_page);
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		app = (PersonalData) getApplication();
		String gprs = String.valueOf(app.getGprs());
		// ��Ҫ��ȡ���ݿ������ֵ���ڴ˴���Ϊ111

		get = (EditText) findViewById(R.id.qcll);
		ok = (Button) findViewById(R.id.get_ok);
		all = (TextView) findViewById(R.id.kyll);
		cancel = (Button) findViewById(R.id.cancel_ok);
		all.setText(gprs);
		ok.setOnClickListener(TakeDataPage.this);
		cancel.setOnClickListener(TakeDataPage.this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.get_ok:
			FlowOpration flowOpration = new FlowOpration(
					(PersonalData) getApplication());

			if (!app.isLog()) {
				Toast.makeText(getApplicationContext(), "�û�δ��½�����½������",
						Toast.LENGTH_SHORT).show();
				break;
			}
			if (flowOpration.takeFlow(Float.parseFloat(get.getText().toString()
					.trim()))) {
				Toast.makeText(getApplicationContext(), "������ȡʧ�ܣ�",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.cancel_ok:
			
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setMessage("ȷ���˳���")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						System.exit(0);
					}
				}).setNegativeButton("ȡ��", null).show();
	}

}
