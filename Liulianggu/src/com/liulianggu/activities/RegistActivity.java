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
 * ע��ҳ��
 * 
 * @author xyc
 *
 */
public class RegistActivity extends Activity implements OnClickListener {
	// ��֤��ˢ�°�ť
	private Button vc_shuaixi;
	// ��֤������
	private String getCode = null;
	// ��֤��ͼƬ���������
	private EditText vc_code;
	private ImageView vc_image;
	// ͷ��
	private ImageView headImage;
	private BitmapUtil bitmapUtil;
	// ע����Ϣ
	private EditText NickName;
	private EditText PhoneNum;
	private EditText psw1;
	private EditText psw2;

	private Button regist;
	private Button returnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		setContentView(R.layout.activity_regist);
		init();
	}

	// �ؼ���
	private void init() {
		// ��֤��ͼƬ
		vc_image = (ImageView) findViewById(R.id.image);
		vc_image.setImageBitmap(Code.getInstance().getBitmap());
		// ��ȡ��ʾ����֤��
		getCode = Code.getInstance().getCode();
		// ��֤�������
		vc_code = (EditText) findViewById(R.id.yzm_ok);
		// ��֤�������ť
		vc_shuaixi = (Button) findViewById(R.id.sx);
		vc_shuaixi.setOnClickListener(this);
		// ѡȡͷ������
		bitmapUtil = new BitmapUtil(RegistActivity.this);
		// ͷ��ѡȡ
		headImage = (ImageView) findViewById(R.id.reg_headImage);
		headImage.setOnClickListener(this);
		// ���ذ�ť
		returnBack = (Button) findViewById(R.id.reg_back_btn);
		returnBack.setOnClickListener(this);
		// ע����Ϣ
		psw1 = (EditText) findViewById(R.id.reg_password);
		psw2 = (EditText) findViewById(R.id.reg_password2);
		NickName = (EditText) findViewById(R.id.reg_nickName);
		PhoneNum = (EditText) findViewById(R.id.reg_phoneNum);
		// ע�ᰴť
		regist = (Button) findViewById(R.id.register_btn);
		regist.setOnClickListener(this);

	}

	/**
	 * ͷ��ѡ��ص� resultCode: ��������-1 �û����˷���0
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("TAG", requestCode + " �� " + resultCode);
		switch (requestCode) {
		case BitmapUtil.activity_result_camara_with_data: // ����
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
		// ͷ����ѡȡͷ��
		case R.id.reg_headImage:
			new android.app.AlertDialog.Builder(RegistActivity.this)
					.setTitle("ͷ��ѡ��")
					.setNegativeButton("���ѡȡ",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
									bitmapUtil.doCropPhoto();
								}
							})
					.setPositiveButton("�������",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
									String status = Environment
											.getExternalStorageState();
									if (status
											.equals(Environment.MEDIA_MOUNTED)) {// �ж��Ƿ���SD��
										bitmapUtil.doTakePhoto();// �û�����˴��������ȡ
									}
								}
							}).show();
			break;
		// ��֤��ˢ��
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
			Toast.makeText(getApplicationContext(), "�������ǳ�", Toast.LENGTH_SHORT)
					.show();
		} else if (PhoneNum1.isEmpty()) {
			Toast.makeText(getApplicationContext(), "�������ֻ���",
					Toast.LENGTH_SHORT).show();
		} else if (psw11.isEmpty()) {
			Toast.makeText(getApplicationContext(), "����������", Toast.LENGTH_SHORT)
					.show();
		} else if (psw22.isEmpty()) {
			Toast.makeText(getApplicationContext(), "���ٴ���������",
					Toast.LENGTH_SHORT).show();
		} else if (!psw11.equals(psw22)) {
			Toast.makeText(getApplicationContext(), "�����������벻һ��",
					Toast.LENGTH_SHORT).show();
		} else if (psw11.length() < 6) {
			Toast.makeText(getApplicationContext(), "��������λ��������",
					Toast.LENGTH_SHORT).show();
		} else if (v_code == null || v_code.equals("")) {
			Toast.makeText(getApplicationContext(), "û����д��֤��",
					Toast.LENGTH_SHORT).show();
		} else if (!v_code.equals(getCode) && false) {
			Toast.makeText(getApplicationContext(), "��֤����д����ȷ",
					Toast.LENGTH_SHORT).show();
		} else {

			UserInfo user = new UserInfo();
			user.setNickName(NickName1);
			user.setPhoneNum(PhoneNum1);
			user.setPassword(psw11);
			UserOpration userOpration = new UserOpration(
					(PersonalData) getApplication());
			if (userOpration.userRegist(user)) {
				Toast.makeText(getApplicationContext(), "ע��ɹ�",
						Toast.LENGTH_SHORT).show();
				RegistActivity.this.finish();
			} else {
				Toast.makeText(getApplicationContext(), "ע��ʧ��",
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
