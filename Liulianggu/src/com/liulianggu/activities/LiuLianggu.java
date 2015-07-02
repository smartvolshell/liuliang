package com.liulianggu.activities;



//import org.androidpn.client.ServiceManager;

import com.liulianggu.application.PersonalData;
import com.liulianggu.tabmenu.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/*********************
 * 主界面，实现TabHost
 * 
 * @author xyc
 *
 */
public class LiuLianggu extends TabActivity {
	// 创建TabHost
	public TabHost mth;
	public static final String TAB_HOME = "首页";
	public static final String TAB_NEWS = "信息";
	public static final String TAB_ABOUT = "关于";
	public static final String TAB_SEARCH = "搜索";
	public static final String TAB_PAGE = "更多";
	public RadioGroup radioGroup;
	//private ServiceManager serviceManager;
	private PersonalData appData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		// 初始化底部菜单
		init();
		appData = (PersonalData) getApplication();
		// Start the service
	
		
		// serviceManager.setAlias(new PersonalData().getPhoneNum());
		// 底部菜单点击事件
		clickevent();

	}

	/**
	 * 每一个底部按钮点击事件，切换相应的界面
	 */
	private void clickevent() {
		this.radioGroup = (RadioGroup) findViewById(R.id.main_radio);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// 根据点击的按钮跳转到相应的界面
				switch (checkedId) {
				case R.id.radio_button0:
					mth.setCurrentTabByTag(TAB_HOME);
					break;
				case R.id.radio_button1:
					mth.setCurrentTabByTag(TAB_NEWS);
					break;
				case R.id.radio_button2:
					mth.setCurrentTabByTag(TAB_ABOUT);
					break;
				case R.id.radio_button3:
					mth.setCurrentTabByTag(TAB_SEARCH);
					break;
				case R.id.radio_button4:
					mth.setCurrentTabByTag(TAB_PAGE);
					break;
				}
			}
		});
	}

	/**
	 * 实例化TabHost,往TabHost添加5个界面
	 */
	private void init() {
		// 实例化TabHost
		mth = this.getTabHost();
		TabSpec ts1 = mth.newTabSpec(TAB_HOME).setIndicator(TAB_HOME);
		ts1.setContent(new Intent(LiuLianggu.this, SaveDataPage.class));
		mth.addTab(ts1);// 往TabHost中第一个底部菜单添加界面

		TabSpec ts2 = mth.newTabSpec(TAB_NEWS).setIndicator(TAB_NEWS);
		ts2.setContent(new Intent(LiuLianggu.this, MakeDataPage.class));
		mth.addTab(ts2);

		TabSpec ts3 = mth.newTabSpec(TAB_ABOUT).setIndicator(TAB_ABOUT);
		ts3.setContent(new Intent(LiuLianggu.this, TakeDataPage.class));
		mth.addTab(ts3);

		TabSpec ts4 = mth.newTabSpec(TAB_SEARCH).setIndicator(TAB_SEARCH);
		ts4.setContent(new Intent(LiuLianggu.this, InterActionPage.class));
		mth.addTab(ts4);

		TabSpec ts5 = mth.newTabSpec(TAB_PAGE).setIndicator(TAB_PAGE);
		ts5.setContent(new Intent(LiuLianggu.this, SettingPage.class));
		mth.addTab(ts5);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (new PersonalData().isLog()) {
			System.exit(0);
		}
	}
}
