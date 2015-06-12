package com.liulianggu.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.liulianggu.application.PersonalData;
import com.liulianggu.tabmenu.R;
import com.liulianggu.userOpration.UserOpration;

/******************
 * 登录页面
 * 
 * @author xyc
 *
 */
public class LoginActivity extends Activity implements OnClickListener {
	private EditText userName, userPasswd;
	private Button login;
	private ImageButton btnRegist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginpage);
		init();
		userName.setText("1");
		userPasswd.setText("1");
	}

	private void init() {
		userName = (EditText) this.findViewById(R.id.login_edit_account);
		userPasswd = (EditText) this.findViewById(R.id.login_edit_pwd);
		login = (Button) this.findViewById(R.id.login_btn_login);
		btnRegist = (ImageButton) this.findViewById(R.id.regist);
		login.setOnClickListener(LoginActivity.this);
		btnRegist.setOnClickListener(LoginActivity.this);

	}

	// 登录按钮
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_btn_login:
			String name = userName.getText().toString().trim();
			String passwd = userPasswd.getText().toString().trim();
			// 验证用户登录是否正确
			UserOpration userOpration = new UserOpration(
					(PersonalData) getApplication());
			if (userOpration.userLoginChk(name, passwd)) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), LiuLianggu.class);
				startActivity(intent);
				LoginActivity.this.finish();
			} else {
				Toast.makeText(LoginActivity.this, "用户名或密码错误，请重新输入",
						Toast.LENGTH_LONG).show();
			}
			break;

		case R.id.regist:
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), RegistActivity.class);
			startActivity(intent);
			break;
		}
	}
}
