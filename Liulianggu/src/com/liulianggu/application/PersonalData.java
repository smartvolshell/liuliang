package com.liulianggu.application;

import java.math.BigDecimal;

import org.androidpn.client.ServiceManager;

import android.app.Application;

import com.liulianggu.beans.UserInfo;

public class PersonalData extends Application {
	/** 是否登录 */
	private boolean log;
	/** gprs流量 */
	private float gprs;
	/** 手机号 */
	private String PhoneNum;
	/** 密码 */
	private String Password;
	/** 昵称 */
	private String NickName;
	/** 头像 */
	private String HeadPortrait;
	/** 用户等级 */
	private int UserDegree;
	/** 消息推送链接 */
	private ServiceManager serviceManager;

	public void setUserInfo(UserInfo userInfo) {
		this.PhoneNum = userInfo.getPhoneNum();
		this.Password = userInfo.getPassword();
		this.NickName = userInfo.getNickName();
		this.HeadPortrait = userInfo.getHeadPortrait();
		this.UserDegree = userInfo.getUserDegree();
	}
	
	public ServiceManager getServiceManager() {
		return serviceManager;
	}

	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}

	public String getHeadPortrait() {
		return HeadPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		HeadPortrait = headPortrait;
	}

	public int getUserDegree() {
		return UserDegree;
	}

	public void setUserDegree(int userDegree) {
		UserDegree = userDegree;
	}

	public String getPhoneNum() {
		return PhoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		PhoneNum = phoneNum;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public boolean isLog() {
		return log;
	}

	public void setLog(boolean log) {
		this.log = log;
	}

	public float getGprs() {
		return gprs;
	}

	public void setGprs(float gprs) {

		BigDecimal b = new BigDecimal(gprs);
		float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		this.gprs = f1;
	}

	@Override
	public void onCreate() {
		log = false;
		gprs = 0;
		super.onCreate();
	}
}
