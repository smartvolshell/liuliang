package com.liulianggu.beans;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

public class AdvertisementItem implements Serializable {
	private Drawable image;
	private String title;
	private String detail;
	private float evaluation;

	public AdvertisementItem(Drawable image, String title, String detail,
			float evaluation) {
		this.image = image;
		this.title = title;
		this.detail = detail;
		this.evaluation = evaluation;
	}

	public float getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(float evaluation) {
		this.evaluation = evaluation;
	}

	public Drawable getImage() {
		return image;
	}

	public void setImage(Drawable image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
