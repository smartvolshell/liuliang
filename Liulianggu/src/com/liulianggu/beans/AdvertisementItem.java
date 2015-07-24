package com.liulianggu.beans;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class AdvertisementItem implements Serializable {

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
	/** 下载链接 */
	private String apkUrl;
	private int reward;
	/** 应用图片 */
	private Bitmap imag;

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

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

	//
	// public static final Parcelable.Creator<AdvertisementItem> CREATOR = new
	// Parcelable.Creator<AdvertisementItem>() {
	// public AdvertisementItem createFromParcel(Parcel in) {
	// return new AdvertisementItem(in);
	// }
	//
	// public AdvertisementItem[] newArray(int size) {
	// return new AdvertisementItem[size];
	// }
	// };
	//
	// public AdvertisementItem() {
	//
	// }
	//
	// public AdvertisementItem(Parcel in) {
	// appName = in.readString();
	// appIcon = in.readString();
	// appMsg = in.readString();
	// appDownLoadVal = in.readInt();
	// appType = in.readString();
	// appFreeTag = in.readInt();
	// evaluation = in.readFloat();
	// apkUrl = in.readString();
	// reward = in.readInt();
	// }
}
