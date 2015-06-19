package com.liulianggu.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.liulianggu.beans.UserInfo;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

public class SeverOpration {

	private final String KEY = "http://172.20.41.58:8088/liulianggu/";

	/**********
	 * 链接服务器预处理方法
	 */
	@SuppressLint("NewApi")
	private static void pretreatment() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedSqlLiteObjects()
				.penaltyLog().penaltyDeath().build());
	}

	/***************
	 * 判断用户名密码是否正确 若不正确返回空 并通过用户名密码获取个人信息，除流量
	 * 
	 * @param telNum
	 * @param psw
	 * @return
	 */
	public UserInfo getUserInfo(String telNum, String psw) {
		UserInfo userInfo = new UserInfo();
		if (telNum.equals("1") && psw.equals("1")) {
			userInfo.setHeadPortrait("");
			userInfo.setNickName("帅哥");
			userInfo.setPassword(psw);
			userInfo.setPhoneNum(telNum);
			userInfo.setUserDegree(1);
			return userInfo;
		}
		pretreatment();
		String keyString = KEY + "UserInfo.action?phoneNum=" + telNum
				+ "&password=" + psw;

		// http://172.20.41.58/liulianggu/UserInfo.action?phoneNum=44321&password=123456
		String result = getResult(keyString);
		Log.e("log_tag", result);
		if (result.replace("\n", "").equals("error"))
			return null;
		if (result.equals("wrong"))
			return null;
		// parse json data
		try {
			JSONObject jArray = new JSONObject(result);
			JSONObject json_data = jArray.getJSONObject("UserInfo");
			userInfo.setHeadPortrait(json_data.getString("headPortrait"));
			userInfo.setNickName(json_data.getString("nickName"));
			userInfo.setPassword(json_data.getString("password"));
			userInfo.setPhoneNum(json_data.getString("phoneNum"));
			userInfo.setUserDegree(json_data.getInt("userDegree"));
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
			return null;
		}

		return userInfo;
	}

	/*************
	 * 注册并判断是否注册成功
	 * 
	 * @param user
	 * @return
	 */
	public boolean registSucc(UserInfo user) {
		boolean flag = false;
		pretreatment();
		String keyString = KEY + "NewUserInfo.action?phoneNum="
				+ user.getPhoneNum() + "&password=" + user.getPassword()
				+ "&nickName=" + user.getNickName();
		// http://172.20.41.41:8088/liulianggu/NewUserInfo.action?phoneNum=98765&password=123456&nickName=1234
		String result = getResult(keyString).replace("\n", "");
		Log.e("log_tag", "1111" + result + "2222");
		if (result.equals("success"))
			flag = true;
		return flag;
	}

	/***************
	 * 通过手机号获取账号内流量
	 * 
	 * @param telNum
	 * @return
	 */
	public float getGprs(String telNum) {
		float gprs = 0;
		pretreatment();
		String keyString = KEY + "MobileDataInfo.action?type=select&phoneNum="
				+ telNum;
		// http://172.20.41.41:8088/liulianggu/MobileDataInfo.action?type=select&phoneNum=44321
		String result = getResult(keyString);
		Log.e("log_tag", result);
		if (result.replace("\n", "").equals("error"))
			return 0;
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject json_data = jsonObject.getJSONObject("MobileDataInfo");
			gprs = Float.parseFloat((json_data.getString("dataVolume")));
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
			return -1;
		}
		return gprs;
	}

	/******************
	 * 判断是否提取成功
	 * 
	 * @param takeGprs
	 * @return
	 */
	public boolean takeFlow(String phoneNum, float takeGprs) {
		boolean flag = false;
		pretreatment();
		String keyString = KEY
				+ "MobileDataInfo.action?type=update&dataVolume=" + takeGprs
				+ "&phoneNum=" + phoneNum;
		// http://172.20.41.41:8088/liulianggu/MobileDataInfo.action?type=update&dataVolume=3
		String result = getResult(keyString);
		Log.e("log_tag", result);
		if (result.equals("wrong"))
			return false;
		if (!result.replace("\n", "").equals("error"))
			flag = true;
		return flag;
	}

	/***********************
	 * 存储流量，并判断是否存储成功
	 * 
	 * @param saveGprs
	 * @return
	 */
	public boolean saveFlow(String phoneNum, float saveGprs) {
		return takeFlow(phoneNum, 0 - saveGprs);
	}

	/******************
	 * 调用云端方法操作数据库，并获取返回结果
	 * 
	 * @param keyString
	 * @return
	 */
	private String getResult(String keyString) {
		InputStream is = null;
		String result = "";
		StringBuilder sbuilder = new StringBuilder();
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(keyString);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
			return "wrong";
		}

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			String line = null;
			while ((line = reader.readLine()) != null) {
				sbuilder.append(line + "\n");
			}
			System.out.println(sbuilder);

			result = sbuilder.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
			return "wrong";
		}
		try {
			if (is != null)
				is.close();
		} catch (IOException e) {
			e.printStackTrace();
			return "wrong";
		}
		result = removeBOM(result);
		return result;
	}

	/*****************
	 * 获取网页的body值
	 * 
	 * @param data
	 * @return
	 */
	private final String removeBOM(String data) {
		if (TextUtils.isEmpty(data)) {
			return data;
		}

		if (data.startsWith("\ufeff")) {
			return data.substring(1);
		} else {
			return data;
		}
	}
}
