package com.liulianggu.beans;

/******************
 * �û���Ϣ��
 * 
 * @author xyc
 * 
 */

public class UserInfo {
	/** �绰���� */
	private String PhoneNum;
	/** ���� */
	private String Password;
	/** �ǳ� */
	private String NickName;
	/** ͷ�� */
	private String HeadPortrait;
	/** �û��ȼ� */
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
