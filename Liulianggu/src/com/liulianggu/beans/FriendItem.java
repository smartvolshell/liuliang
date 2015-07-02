package com.liulianggu.beans;

public class FriendItem {
	public static String TEL_FRIEND = "ͨѶ¼����";
	/** ͼƬ */
	private int imageId;
	/** �绰���� */
	private String phoneNum;
	/** ��ע */
	private String note;
	/** �ǳ� */
	private String nickName;
	/** ��ϸ��Ϣ */
	private String message;
	/** ��ʾλ��1Ϊ���ݿ���ѣ�0Ϊ�ֻ����� */
	private String friendType;

	public FriendItem() {
		imageId = -1;
		phoneNum = null;
		nickName = null;
		message = null;
		note = "";
	}

	public FriendItem(int imageId, String phoneNum, String nickName,
			String message) {
		this.imageId = imageId;
		this.phoneNum = phoneNum;
		this.nickName = nickName;
		this.message = message;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFriendType() {
		return friendType;
	}

	public void setFriendType(String friendType) {
		this.friendType = friendType;
	}

}
