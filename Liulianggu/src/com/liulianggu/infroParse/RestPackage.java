package com.liulianggu.infroParse;

/*************************
 * 剩余套餐
 * 
 * @author xyc
 *
 */
public class RestPackage {
	// 套餐名
	private String chargeName = "";
	// 套餐内剩余流量
	private float restGprs = 0;
	// 套餐内流量价值
	private float value = 0;
	// 套餐结束月末日期
	private int lastDate = 0;
	// 套餐开始日期
	private int startDate = 0;

	public int getLastDate() {
		return lastDate;
	}

	public void setLastDate(int lastDate) {
		this.lastDate = lastDate;
	}

	public int getStartDate() {
		return startDate;
	}

	public void setStartDate(int startDate) {
		this.startDate = startDate;
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
