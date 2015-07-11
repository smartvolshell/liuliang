package com.liulianggu.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.liulianggu.application.PersonalData;
import com.liulianggu.beans.UserInfo;
import com.liulianggu.tabmenu.R;
import com.liulianggu.userOpration.UserOpration;
import com.liulianggu.utils.BitmapUtil;
import com.liulianggu.utils.Code;

/*******************
 * 注册页面
 * 
 * @author xyc
 *
 */
public class RegistActivity extends Activity implements OnClickListener {
	// 验证码刷新按钮
	private Button vc_shuaixi;
	// 验证码内容
	private String getCode = null;
	// 验证码图片及其输入框
	private EditText vc_code;
	private ImageView vc_image;
	// 头像
	private ImageView headImage;
	private BitmapUtil bitmapUtil;
	// 注册信息
	private EditText NickName;
	private EditText PhoneNum;
	private EditText psw1;
	private EditText psw2;

	private Button regist;
	private Button returnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_regist);
		init();
	}

	// 控件绑定
	private void init() {
		// 验证码图片
		vc_image = (ImageView) findViewById(R.id.image);
		vc_image.setImageBitmap(Code.getInstance().getBitmap());
		// 获取显示的验证码
		getCode = Code.getInstance().getCode();
		// 验证码输入框
		vc_code = (EditText) findViewById(R.id.yzm_ok);
		// 验证码更换按钮
		vc_shuaixi = (Button) findViewById(R.id.sx);
		vc_shuaixi.setOnClickListener(this);
		// 选取头像辅助类
		bitmapUtil = new BitmapUtil(RegistActivity.this);
		// 头像选取
		headImage = (ImageView) findViewById(R.id.reg_headImage);
		headImage.setOnClickListener(this);
		// 返回按钮
		returnBack = (Button) findViewById(R.id.reg_back_btn);
		returnBack.setOnClickListener(this);
		// 注册信息
		psw1 = (EditText) findViewById(R.id.reg_password);
		psw2 = (EditText) findViewById(R.id.reg_password2);
		NickName = (EditText) findViewById(R.id.reg_nickName);
		PhoneNum = (EditText) findViewById(R.id.reg_phoneNum);
		// 注册按钮
		regist = (Button) findViewById(R.id.register_btn);
		regist.setOnClickListener(this);

	}

	/**
	 * 头像选择回调 resultCode: 正常返回-1 用户后退返回0
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("TAG", requestCode + " ： " + resultCode);
		switch (requestCode) {
		case BitmapUtil.activity_result_camara_with_data: // 拍照
			try {
				if (resultCode == -1) {
					bitmapUtil.cropImageUriByTakePhoto();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case BitmapUtil.activity_result_cropimage_with_data:
			try {
				if (resultCode == -1) {
					Bitmap bitmap = bitmapUtil.decodeUriAsBitmap();
					if (bitmap != null) {
						headImage.setImageBitmap(bitmap);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 头像点击选取头像
		case R.id.reg_headImage:
			new android.app.AlertDialog.Builder(RegistActivity.this)
					.setTitle("头像选择")
					.setNegativeButton("相册选取",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
									bitmapUtil.doCropPhoto();
								}
							})
					.setPositiveButton("相机拍照",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
									String status = Environment
											.getExternalStorageState();
									if (status
											.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
										bitmapUtil.doTakePhoto();// 用户点击了从照相机获取
									}
								}
							}).show();
			break;
		// 验证码刷新
		case R.id.sx:
			vc_image.setImageBitmap(Code.getInstance().getBitmap());
			getCode = Code.getInstance().getCode();
			break;
		case R.id.reg_back_btn:
			this.finish();
			break;
		case R.id.register_btn:
			regist();
			break;
		default:
			break;
		}

	}

	private void regist() {
		// // TODO Auto-generated method stub
		String v_code = vc_code.getText().toString().trim();
		final String psw11 = psw1.getText().toString().trim();
		String psw22 = psw2.getText().toString().trim();
		final String PhoneNum1 = PhoneNum.getText().toString().trim();
		final String NickName1 = NickName.getText().toString().trim();
		// SeverOpration sop = new SeverOpration();
		if (NickName1.isEmpty()) {
			Toast.makeText(getApplicationContext(), "请输入昵称", Toast.LENGTH_SHORT)
					.show();
		} else if (PhoneNum1.isEmpty()) {
			Toast.makeText(getApplicationContext(), "请输入手机号",
					Toast.LENGTH_SHORT).show();
		} else if (psw11.isEmpty()) {
			Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT)
					.show();
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
		} else if (!v_code.equals(getCode) && false) {
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
				RegistActivity.this.finish();
			} else {
				Toast.makeText(getApplicationContext(), "注册失败",
						Toast.LENGTH_SHORT).show();
			}

			// Thread thread = new Thread() {
			//
			// public void run() {
			// Looper.prepare();
			//
			// };
			// };
			// thread.start();
		}
	}
}
