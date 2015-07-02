package com.liulianggu.userOpration;

import org.jivesoftware.smack.XMPPConnection;

import android.app.Activity;

import com.liulianggu.application.PersonalData;
import com.liulianggu.beans.UserInfo;
import com.liulianggu.sever.SeverOpration;
import com.liulianggu.utils.XmppService;
import com.liulianggu.utils.XmppTool;

/**
 * @author Volshell
 * 
 *         �û�������Ϣ�Ĳ���
 */
public class UserOpration extends Activity {
	private PersonalData app;
	private SeverOpration severOpration;

	/*********
	 * ���캯������Ҫapp
	 * 
	 * @param app
	 */
	public UserOpration(PersonalData app) {
		this.app = app;
		severOpration = new SeverOpration();
	}

	/*************************
	 * ��֤�û��������Ƿ���ȷ
	 * 
	 * @param userName
	 * @param userPasswd
	 * @return
	 */
	public boolean userLoginChk(String userName, String userPasswd) {
		UserInfo userInfo = new UserInfo();
		userInfo = severOpration.getUserInfo(userName, userPasswd);
		if (userInfo == null) {
			return false;
		} else {
			app.setLog(true);
			if (userName.equals("1"))
				app.setGprs(200);
			else
				app.setGprs(severOpration.getGprs(userName));
			app.setUserInfo(userInfo);
		}
		return true;
	}

	/************************
	 * �ж��û�ע���Ƿ��ܹ��ɹ�
	 * 
	 * @param user
	 * @return
	 */
	public boolean userRegist(UserInfo user) {
		if (severOpration.registSucc(user)) {
			// �û�ע��
			String result = XmppService.regist(user.getPhoneNum(),
					user.getPassword(), user.getNickName());
			XmppTool.closeConnection();
			if (result.equals("1"))
				return true;
			else
				return false;
		} else {
			return false;
		}

	}
}
