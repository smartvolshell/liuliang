package com.liulianggu.infroParse;

/*************************
 * ʣ���ײ�
 * 
 * @author xyc
 *
 */
public class RestPackage {
	// �ײ���
	private String chargeName = "";
	// �ײ���ʣ������
	private float restGprs = 0;
	// �ײ���������ֵ
	private float value = 0;
	// �ײͽ�����ĩ����
	private int lastDate = 0;
	// �ײͿ�ʼ����
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
