package com.liulianggu.games;

import com.liulianggu.tabmenu.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class GameOf2048Help extends Activity {
	private TextView txt1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameof2048help);
		txt1 = (TextView) findViewById(R.id.textView1);
		String txt = "��ʼʱ��������������������֣����ֵ����ֽ�����Ϊ2��4.\n";
		txt = txt + "��ҿ���ѡ�����������ĸ�����,�������ڵ����ֳ���λ�ƻ�ϲ�,��Ϊ��Ч�ƶ�.\n";
		txt = txt + "���ѡ��ķ�����������ͬ��������ϲ���ÿ����Ч�ƶ�����ͬʱ�ϲ����������������ϲ�.\n";
		txt = txt + "�ϲ����õ�����������������Ӽ�Ϊ�ò�����Ч�÷�.\n";
		txt = txt + "���ѡ��ķ����л���ǰ���пո������λ��.\n";
		txt = txt + "ÿ��Ч�ƶ�һ�������̵Ŀ�λ(�����ִ�)�������һ������(��Ȼ����Ϊ2��4).\n";
		txt = txt + "���̱������������޷�������Ч�ƶ����и�����Ϸ����.\n";
		txt1.setText(txt);
	}
}
