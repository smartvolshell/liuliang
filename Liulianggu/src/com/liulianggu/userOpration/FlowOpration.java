package com.liulianggu.userOpration;

import com.liulianggu.application.PersonalData;
import com.liulianggu.sever.SeverOpration;

import android.app.Activity;

/*********************
 * 用户流量的操作
 * 
 * @author xyc
 *
 */
public class FlowOpration extends Activity {
	private PersonalData appDate;
	private SeverOpration sopOpration;

	/******************
	 * 构造函数，需要app
	 * 
	 * @param appDate
	 */
	public FlowOpration(PersonalData appDate) {
		this.appDate = appDate;
		sopOpration = new SeverOpration();
	}

	/********************
	 * 存储Grps流量
	 * 
	 * @param gprs
	 * @return
	 */
	public boolean saveFlow(float gprs) {
		boolean flag = true;
		return true;
		// if (sopOpration.saveFlow(appDate.getPhoneNum(), gprs)) {
		// appDate.setGprs(sopOpration.getGprs(appDate.getPhoneNum()));
		// flag = true;
		// }
		// return flag;
	}

	/*******************
	 * 提取Gprs流量
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
	 * 赠送流量
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
