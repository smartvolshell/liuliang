package com.liulianggu.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.liulianggu.tabmenu.R;

/****************
 * ׬ȡ������ؽ���
 * 
 * @author xyc
 *
 */
public class MakeDataPage extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.make_data_page);
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
