package com.liulianggu.infroParse;

/*****************
 * Ì×²Í
 * 
 * @author xyc
 *
 */
public class Charge {
	private String chargeName = "";
	private float price = 0;
	private float sumgprs = 0;
	private float value = 0;

	Charge(String chargeName) {
		this.chargeName = chargeName;
	}

	public String getPackageName() {
		return chargeName;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getSumGprs() {
		return sumgprs;
	}

	public void setSumGprs(float gprs) {
		this.sumgprs = gprs;
	}

	public float getValue() {
		return value;
	}

	public void setValue() {
		if (sumgprs != 0)
			value = price / sumgprs;
		else
			value = 0;
	}
}
