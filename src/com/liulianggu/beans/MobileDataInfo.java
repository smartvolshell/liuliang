package com.liulianggu.beans;

import java.util.Date;

/**********************
 * 用户流量信息表
 * 
 * @author xyc
 *
 */
public class MobileDataInfo {
	/** 用户手机号 */
	private String phoneNum;
	/** 用户剩余流量 */
	private float gprs;
	/** 用户流量获取日期 */
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
