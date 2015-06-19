package com.liulianggu.games;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.liulianggu.activities.LiuLianggu;
import com.liulianggu.activities.MakeDataPage;
import com.liulianggu.tabmenu.R;

public class GameOf2048 extends Activity {
	private TextView tvScore, tvMaxScore;
	private Button btnShowFlow;
	private static GameOf2048 mainActivity = null;

	private String SDpath;
	private String fileName = "2048.bd";
	private int curFlow = 0;
	/**
	 * �Ʒ���
	 */
	public static int score = 0;
	public int Maxscore = 0;
	private int newScore = 0;
	/**
	 * width_pm��ʾ��Ļ�Ŀ�� height_pm��ʾ��Ļ�ĸ߶�
	 */
	public static int width_pm = 0, height_pm = 0;

	public GameOf2048() {
		mainActivity = this;
	}

	public static GameOf2048 getMainActivity() {
		return mainActivity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ����ȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.gameof2048);

		tvScore = (TextView) findViewById(R.id.tvScore);
		tvMaxScore = (TextView) findViewById(R.id.tvMaxScore);
		btnShowFlow = (Button) this.findViewById(R.id.btn_show_flow);

		SDpath = Environment.getExternalStorageDirectory().getPath();
		WindowManager wm = this.getWindowManager();
		width_pm = wm.getDefaultDisplay().getWidth();
		height_pm = wm.getDefaultDisplay().getHeight();

		LinearLayout layout = (LinearLayout) this
				.findViewById(R.id.gameContainer);
		LayoutParams params = (LayoutParams) layout.getLayoutParams();
	
		params.height = width_pm;
		layout.setLayoutParams(params);

	}

	/**
	 * �����ļ�
	 */
	public void SaveSDFile() {
		File wj = new File(SDpath, fileName);// �ļ�
		// ����ļ������ھʹ����ļ�
		if (!wj.exists()) {
			try {
				wj.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (Maxscore <= score) {
			Maxscore = score;
			try {
				FileOutputStream fos = new FileOutputStream(wj);
				String BestScode = "��߷���:" + Maxscore;
				fos.write(BestScode.getBytes());
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * ��ȡ�ļ�
	 * 
	 * @throws FileNotFoundException
	 */
	public void ReadSDFile() throws FileNotFoundException {
		File file = new File(SDpath, fileName);
		FileInputStream fis = new FileInputStream(file);
		/* ׼��һ���ֽ������û�װ������ȡ������ */
		try {
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			String strResult = new String(buffer);
			strResult = strResult.replace("��߷���:", "");
			newScore = Integer.parseInt(strResult);

			tvMaxScore.setText(strResult);
			fis.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * ����Ͷ�ȡ����
	 */
	public void SaveSDorReadSD() {
		if (newScore > score) {
			try {
				ReadSDFile();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			SaveSDFile();
			try {
				ReadSDFile();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * ����÷�
	 */
	public void clearScore() {
		score = 0;
		showScore();
	}

	/**
	 * ���ַ���
	 */
	public void showScore() {

		try {
			ReadSDFile();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tvScore.setText(score + "");
	}

	/**
	 * ��ӷ���
	 */
	public void addScore(int s) {
		addFlow(s);
		score += s;
		if (newScore <= score) {
			Maxscore = score;
		}
		showScore();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// �����˳��Ի���
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			// ���öԻ������
			isExit.setTitle("��ȷ��Ҫ�˳���Ϸ��?");
			// ���ѡ��ť��ע�����
			isExit.setButton("ȷ��", listener);
			isExit.setButton2("����", listener);
			isExit.setButton3("ȡ��", listener);
			Window window = isExit.getWindow();
			WindowManager.LayoutParams lp = window.getAttributes();
			lp.alpha = 0.8f;// ��������͸����
			window.setAttributes(lp);
			// ��ʾ�Ի���
			isExit.show();
		}
		return false;
	}

	/**
	 * �����Ի��������button����¼�
	 */
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "ȷ��"��ť�˳�����
				Intent intent = new Intent();
				intent.putExtra("Maxscore", Maxscore);
				intent.setClass(getApplicationContext(), LiuLianggu.class);
				startActivity(intent);
				finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "����"�ڶ�����ťȡ���Ի���
				Intent it = new Intent();
				it.setClass(GameOf2048.this, GameOf2048Help.class);
				startActivity(it);
				break;
			case AlertDialog.BUTTON_NEUTRAL:// "ȡ��"�� ������ťȡ���Ի���
				break;
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void addFlow(int addingScore) {
		curFlow += addingScore / 32;// ������Ƭ����һ������Ŀ
		// �ֳܷ���һ�����
		if (score / 100 != (score + addingScore) / 100) {
			// curFlow += addingScore / 100 +(score + addingScore) / 100;
			int start = score / 100 + 1;
			int end = (score + addingScore) / 100;
			curFlow += (start + end) * (end - start + 1) / 2;
		}
		btnShowFlow.setText("��ϲ�㣬���Ѿ�׬ȡ�� " + curFlow + "KB ��_��");
	}
}
