package com.liulianggu.beans;

/******************
 * 用户信息表
 * 
 * @author xyc
 * 
 */

public class UserInfo {
	/** 电话号码 */
	private String PhoneNum;
	/** 密码 */
	private String Password;
	/** 昵称 */
	private String NickName;
	/** 头像 */
	private String HeadPortrait;
	/** 用户等级 */
	private int UserDegree;

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

}
