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
		String txt = "开始时棋盘内随机出现两个数字，出现的数字仅可能为2或4.\n";
		txt = txt + "玩家可以选择上下左右四个方向,若棋盘内的数字出现位移或合并,视为有效移动.\n";
		txt = txt + "玩家选择的方向上若有相同的数字则合并，每次有效移动可以同时合并，但不可以连续合并.\n";
		txt = txt + "合并所得的所有新生成数字想加即为该步的有效得分.\n";
		txt = txt + "玩家选择的方向行或列前方有空格则出现位移.\n";
		txt = txt + "每有效移动一步，棋盘的空位(无数字处)随机出现一个数字(依然可能为2或4).\n";
		txt = txt + "棋盘被数字填满，无法进行有效移动，判负，游戏结束.\n";
		txt1.setText(txt);
	}
}
