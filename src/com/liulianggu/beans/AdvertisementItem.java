package com.liulianggu.beans;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class AdvertisementItem implements Serializable {
	/** 应用图片 */
	private Bitmap imag;
	/** 应用名称 */
	private String appName;
	/** 应用图片url */
	private String appIcon;
	/** 应用基本信息 */
	private String appMsg;
	/** 应用下载量 */
	private int appDownLoadVal;
	/** 应用类型 */
	private String appType;
	/** 应用费用 */
	private int appFreeTag;
	/** 应用评价 */
	private float evaluation;

	public String getAppName() {
		return appName;
	}

	public Bitmap getImag() {
		return imag;
	}

	public void setImag(Bitmap imag) {
		this.imag = imag;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(String appIcon) {
		this.appIcon = appIcon;
	}

	public String getAppMsg() {
		return appMsg;
	}

	public void setAppMsg(String appMsg) {
		this.appMsg = appMsg;
	}

	public int getAppDownLoadVal() {
		return appDownLoadVal;
	}

	public void setAppDownLoadVal(int appDownLoadVal) {
		this.appDownLoadVal = appDownLoadVal;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public int getAppFreeTag() {
		return appFreeTag;
	}

	public void setAppFreeTag(int appFreeTag) {
		this.appFreeTag = appFreeTag;
	}

	public float getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(float evaluation) {
		this.evaluation = evaluation;
	}

}
