package com.liulianggu.sever;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.liulianggu.beans.AdvertisementItem;
import com.liulianggu.beans.UserInfo;
import com.liulianggu.tabmenu.R;
import com.liulianggu.utils.BitmapUtil;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

public class SeverOpration {

	private final String KEY = "http://192.168.252.5:8088/liulianggu/";

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
		// Log.e("log_tag", "1111" + result + "2222");
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

	/***************
	 * 赠送流量
	 * 
	 * @param phone
	 * @param getPhoneNum
	 * @param flow
	 * @return
	 */
	public boolean transaction(String phone, String getPhoneNum, float flow) {
		boolean flag = true;
		pretreatment();
		String keyString = KEY + "TransactionInfo.action?type=insert&phoneNum="
				+ phone + "&toPhoneNum=" + getPhoneNum + "&transVolume=" + flow;
		// http://localhost:8088/liulianggu/TransactionInfo.action?type=insert&phoneNum=15763941111&toPhoneNum=1122&transVolume=10

		String result = getResult(keyString);
		if (result.replace("\n", "").equals("error"))
			return false;
		return flag;
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
				+ "MobileDataInfo.action?type=update&dataVolume="
				+ (0 - takeGprs) + "&phoneNum=" + phoneNum;
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

	public List<AdvertisementItem> getAppInfo(Context mContext, String opType,
			String appType, int sorType, int clo) {
		List<AdvertisementItem> advertisementItems = new ArrayList<AdvertisementItem>();

		pretreatment();
		String keyString = KEY + "AppInfo.action?type=" + opType
				+ String.valueOf(sorType) + "&appType=" + appType + "&clo="
				+ clo;
		// http://172.20.41.58:8088/liulianggu/AppInfo.action?type=selectSome&appType='手机游戏'&clo=1
		String result = getResult(keyString);
		if (result.replace("\n", "").equals("error")) {
			Log.e("log_tag", "2222222222");
			return null;
		}
		try {
			JSONArray jsonArray = new JSONArray(result);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				AdvertisementItem advertisementItem = new AdvertisementItem();

				String imageUrl = KEY + jsonObject.getString("appIcon");
				advertisementItem.setImag(BitmapUtil.small(mContext,
						getBitmap(imageUrl)));
				advertisementItem.setAppIcon(jsonObject.getString("appIcon"));
				advertisementItem.setAppName(jsonObject.getString("appName"));
				advertisementItem.setAppMsg(jsonObject.getString("appMsg"));
				advertisementItem.setAppType(jsonObject.getString("appType"));
				advertisementItem.setApkUrl(jsonObject.getString("apkUrl"));
				advertisementItem.setReward(jsonObject.getInt("reward"));
				advertisementItem.setEvaluation(Float.parseFloat(jsonObject
						.getString("evaluation")));
				advertisementItem.setAppDownLoadVal(jsonObject
						.getInt("appDownLoadVal"));
				advertisementItems.add(advertisementItem);
			}

		} catch (JSONException e) {
			Log.e("log_tag", "111111111");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("log_tag", "图片获取失败");
			return null;
		}

		return advertisementItems;
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

	private Bitmap getBitmap(String path) throws IOException {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == 200) {
			InputStream inputStream = conn.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			return bitmap;
		}
		return null;
	}

	public boolean apkDownLoad(String path) throws IOException {
		boolean flag = true;
		URL url = new URL(KEY + path);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(10 * 1000); // 超时时间
		connection.connect(); // 连接
		Log.e("log_tag", "链接");
		int a = connection.getResponseCode();
		if (a == 200) { // 返回的响应码200,是成功.
			// Log.e("log_tag", "链接");
			// // File file = new File("/mnt/" + path); // 这里我是手写了。建议大家用自带的类
			// Log.e("log_tag", "链接");
			// // file.createNewFile();
			// Log.e("log_tag", "链接");

			// 获得存储卡路径，构成 保存文件的目标路径
			String dirName = Environment.getExternalStorageDirectory()
					+ "/MyDownload/";
			File f = new File(dirName);
			Log.e("log_tag", dirName);
			if (!f.exists()) {
				f.mkdir();
				Log.e("log_tag", "链接1111111");
			}
			Log.e("log_tag", "链接");
			Log.e("log_tag", path);
			String newFilename = path.substring(path.lastIndexOf("/") + 1);
			newFilename = dirName + newFilename;
			Log.e("log_tag", newFilename);
			File file = new File(newFilename);
			Log.e("log_tag", "链接");
			if (file.exists()) {
				file.delete();
				Log.e("log_tag", "链接wwwwwww");
			}
			Log.e("log_tag", "链接");
			InputStream inputStream = connection.getInputStream();
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(); // 缓存
			byte[] buffer = new byte[1024 * 10];
			while (true) {
				int len = inputStream.read(buffer);
				Log.e("log_tag", "Error converting result " + len);
				// publishProgress(len);
				if (len == -1) {
					break; // 读取完
				}
				arrayOutputStream.write(buffer, 0, len); // 写入
			}
			arrayOutputStream.close();
			inputStream.close();

			byte[] data = arrayOutputStream.toByteArray();
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(data); // 记得关闭输入流
			fileOutputStream.close();
		} else {
			Log.e("log_tag", "" + a);
			return false;
		}
		return flag;
	}
}
