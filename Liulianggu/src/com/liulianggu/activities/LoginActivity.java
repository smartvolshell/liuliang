package com.liulianggu.activities;

import org.androidpn.client.ServiceManager;
import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.liulianggu.application.PersonalData;
import com.liulianggu.tabmenu.R;
import com.liulianggu.userOpration.UserOpration;
import com.liulianggu.utils.NetworkUtil;
import com.liulianggu.utils.XmppTool;

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
	private PersonalData appData;

	private LinearLayout loginLayout, loadingLayout;

	private ServiceManager serviceManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginpage);
		appData = (PersonalData) getApplication();
		serviceManager = new ServiceManager(getApplicationContext());
		serviceManager.setNotificationIcon(R.drawable.liulianggu);//
		// 设置推送图标
		serviceManager.startService();
		appData.setServiceManager(serviceManager);
		init();
		userName.setText("1");
		userPasswd.setText("1");
	}

	private void init() {
		userName = (EditText) this.findViewById(R.id.login_edit_account);
		userPasswd = (EditText) this.findViewById(R.id.login_edit_pwd);
		login = (Button) this.findViewById(R.id.login_btn_login);
		btnRegist = (ImageButton) this.findViewById(R.id.regist);
		loginLayout = (LinearLayout) findViewById(R.id.loginRoot);
		loadingLayout = (LinearLayout) findViewById(R.id.formlogin_layout1);
		login.setOnClickListener(LoginActivity.this);
		btnRegist.setOnClickListener(LoginActivity.this);

	}

	// 登录按钮
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_btn_login:
			final String name = userName.getText().toString().trim();
			final String passwd = userPasswd.getText().toString().trim();
			// 验证用户登录是否正确
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					// 发送消息1，等待
					Looper.prepare();
					if (!NetworkUtil.isConnected(LoginActivity.this)) {
						handler.sendEmptyMessage(3);
						return;
					}
					handler.sendEmptyMessage(1);
					UserOpration userOpration = new UserOpration(appData);
					Log.e("log_tag", "111111111111");
					if (userOpration.userLoginChk(name, passwd)) {
						Log.e("log_tag", "2");
						try {
							Log.e("log_tag", "3");
							XmppTool.getConnection().login(name, passwd);
							Log.e("log_tag", "4");
							Intent intent = new Intent();
							intent.setClass(getApplicationContext(),
									LiuLianggu.class);
							startActivity(intent);
							Log.e("log_tag", "111111111111");
							LoginActivity.this.finish();
						} catch (XMPPException e) {
							e.printStackTrace();
							handler.sendEmptyMessage(2);
						}

					} else {
						handler.sendEmptyMessage(2);
					}
				}
			});
			thread.start();

			break;

		case R.id.regist:
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), RegistActivity.class);
			startActivity(intent);
			break;
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 登陆等待
			if (msg.what == 1) {
				loadingLayout.setVisibility(View.VISIBLE);
				loginLayout.setVisibility(View.GONE);
			}
			// 登陆失败
			else if (msg.what == 2) {
				loadingLayout.setVisibility(View.GONE);
				loginLayout.setVisibility(View.VISIBLE);
				Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT)
						.show();
			}
			// 无网络连接
			else if (msg.what == 3) {
				loadingLayout.setVisibility(View.GONE);
				loginLayout.setVisibility(View.VISIBLE);
				Toast.makeText(LoginActivity.this, "网络不可用，请检查网络！",
						Toast.LENGTH_SHORT).show();
			}
		};
	};
}
