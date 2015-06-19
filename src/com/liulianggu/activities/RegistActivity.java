package com.liulianggu.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.liulianggu.application.PersonalData;
import com.liulianggu.beans.UserInfo;
import com.liulianggu.tabmenu.R;
import com.liulianggu.userOpration.UserOpration;
import com.liulianggu.utils.Code;

/*******************
 * 注册页面
 * 
 * @author xyc
 *
 */
public class RegistActivity extends Activity {

	ImageView vc_image;
	Button vc_shuaixi, vc_ok;
	String getCode = null;
	EditText vc_code;

	EditText psw1;
	EditText psw2;
	EditText NickName;
	EditText PhoneNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		// 注册信息
		psw1 = (EditText) findViewById(R.id.mm1_ok);
		psw2 = (EditText) findViewById(R.id.mm2_ok);
		NickName = (EditText) findViewById(R.id.nc_ok);
		PhoneNum = (EditText) findViewById(R.id.sjh_ok);

		vc_image = (ImageView) findViewById(R.id.image);
		vc_image.setImageBitmap(Code.getInstance().getBitmap());
		vc_code = (EditText) findViewById(R.id.yzm_ok);
		getCode = Code.getInstance().getCode(); // 获取显示的验证码
		Log.e("info", getCode + "----");
		vc_shuaixi = (Button) findViewById(R.id.sx);
		// 验证码更换按钮
		vc_shuaixi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				vc_image.setImageBitmap(Code.getInstance().getBitmap());
				getCode = Code.getInstance().getCode();
			}
		});
		// 取消按钮
		Button cancel = (Button) findViewById(R.id.cancel_ok);
		cancel.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
		// 注册按钮点击事件
		Button regist = (Button) findViewById(R.id.regist_ok);
		regist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String v_code = vc_code.getText().toString().trim();
				String psw11 = psw1.getText().toString().trim();
				String psw22 = psw2.getText().toString().trim();
				String PhoneNum1 = PhoneNum.getText().toString().trim();
				String NickName1 = NickName.getText().toString().trim();
				// SeverOpration sop = new SeverOpration();
				if (NickName1.isEmpty()) {
					Toast.makeText(getApplicationContext(), "请输入昵称",
							Toast.LENGTH_SHORT).show();
				} else if (PhoneNum1.isEmpty()) {
					Toast.makeText(getApplicationContext(), "请输入手机号",
							Toast.LENGTH_SHORT).show();
				} else if (psw11.isEmpty()) {
					Toast.makeText(getApplicationContext(), "请输入密码",
							Toast.LENGTH_SHORT).show();
				} else if (psw22.isEmpty()) {
					Toast.makeText(getApplicationContext(), "请再次输入密码",
							Toast.LENGTH_SHORT).show();
				} else if (!psw11.equals(psw22)) {
					Toast.makeText(getApplicationContext(), "两次密码输入不一致",
							Toast.LENGTH_SHORT).show();
				} else if (psw11.length() < 6) {
					Toast.makeText(getApplicationContext(), "请输入六位以上密码",
							Toast.LENGTH_SHORT).show();
				} else if (v_code == null || v_code.equals("")) {
					Toast.makeText(getApplicationContext(), "没有填写验证码",
							Toast.LENGTH_SHORT).show();
				} else if (!v_code.equals(getCode)) {
					Toast.makeText(getApplicationContext(), "验证码填写不正确",
							Toast.LENGTH_SHORT).show();
				} else {
					UserInfo user = new UserInfo();
					user.setNickName(NickName1);
					user.setPhoneNum(PhoneNum1);
					user.setPassword(psw11);
					UserOpration userOpration = new UserOpration(
							(PersonalData) getApplication());
					if (userOpration.userRegist(user)) {
						Toast.makeText(getApplicationContext(), "注册成功",
								Toast.LENGTH_SHORT).show();
						Intent intent1 = new Intent(getApplicationContext(),
								LiuLianggu.class);
						intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent1);
					} else {
						Toast.makeText(getApplicationContext(), "注册失败",
								Toast.LENGTH_SHORT).show();
					}
				}
			}

		});
	}

}
