package com.liulianggu.beans;

import java.util.Date;

/**********************
 * �û�������Ϣ��
 * 
 * @author xyc
 *
 */
public class MobileDataInfo {
	/** �û��ֻ��� */
	private String phoneNum;
	/** �û�ʣ������ */
	private float gprs;
	/** �û�������ȡ���� */
	private Date endDate;

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public float getGprs() {
		return gprs;
	}

	public void setGprs(float gprs) {
		this.gprs = gprs;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
