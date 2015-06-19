package com.liulianggu.userOpration;

import android.app.Activity;

import com.liulianggu.application.PersonalData;
import com.liulianggu.server.SeverOpration;

/*********************
 * �û������Ĳ���
 * 
 * @author xyc
 *
 */
public class FlowOpration extends Activity {
	private PersonalData appDate;
	private SeverOpration sopOpration;

	/******************
	 * ���캯������Ҫapp
	 * 
	 * @param appDate
	 */
	public FlowOpration(PersonalData appDate) {
		this.appDate = appDate;
		sopOpration = new SeverOpration();
	}

	/********************
	 * �洢Grps����
	 * 
	 * @param gprs
	 * @return
	 */
	public boolean saveFlow(float gprs) {
		boolean flag = false;
		if (sopOpration.saveFlow(appDate.getPhoneNum(), gprs)) {
			appDate.setGprs(sopOpration.getGprs(appDate.getPhoneNum()));
			flag = true;
		}
		// return flag; //ʵ��Ӧ�����Ƿ���flag
		// Ϊ�˷������ֱ�ӷ���true
		return true;
	}

	/*******************
	 * ��ȡGprs����
	 * 
	 * @param gprs
	 * @return
	 */
	public boolean takeFlow(float gprs) {
		boolean flag = false;
		if (sopOpration.takeFlow(appDate.getPhoneNum(), gprs)) {
			appDate.setGprs(sopOpration.getGprs(appDate.getPhoneNum()));
			flag = true;
		}
		return flag;
	}

	/*******
	 * ��������
	 * 
	 * @param phoneNum
	 * @param gprs
	 * @return
	 */
	public boolean sendFlow(String phoneNum, float gprs) {

		// if (!takeFlow(gprs))
		// return false;
		// if (!sopOpration.saveFlow(phoneNum, gprs)) {
		// saveFlow(gprs);
		// return false;
		// }
		return false;
	}
}
