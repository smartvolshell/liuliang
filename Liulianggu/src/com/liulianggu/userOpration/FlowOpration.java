package com.liulianggu.userOpration;

import com.liulianggu.application.PersonalData;
import com.liulianggu.sever.SeverOpration;

import android.app.Activity;

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
		boolean flag = true;

		if (sopOpration.saveFlow(appDate.getPhoneNum(), gprs)) {
			appDate.setGprs(sopOpration.getGprs(appDate.getPhoneNum()));
			flag = true;
		}
		return flag;
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
		} else {
			sopOpration.takeFlow(appDate.getPhoneNum(), 0 - gprs);
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

		if (sopOpration.transaction(appDate.getPhoneNum(), phoneNum, gprs)) {
			appDate.setGprs(sopOpration.getGprs(appDate.getPhoneNum()));
			return true;
		} else {
			return false;
		}

	}
}
