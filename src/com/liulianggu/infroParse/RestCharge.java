package com.liulianggu.infroParse;
/*************************
 * Ê£ÓàÌ×²Í
 * @author xyc
 *
 */
public class RestCharge {
	private String chargeName = "";
	private float restGprs = 0;
	private float value = 0;
	private int lastdate = 0;

	public int getLastdate() {
		return lastdate;
	}

	public void setLastdate(int lastdate) {
		this.lastdate = lastdate;
	}

	public String getChargeName() {
		return chargeName;
	}

	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	public float getRestGprs() {
		return restGprs;
	}

	public void setRestGprs(float restGprs) {
		this.restGprs = restGprs;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

}
