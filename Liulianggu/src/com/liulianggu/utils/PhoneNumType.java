package com.liulianggu.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.raw;
import android.util.Log;

public class PhoneNumType {

	public static String CHINAMOBILE = "移动好友";
	public static String CHINAUNICOM = "联通好友";
	public static String CHINATELECOM = "电信好友";
	public static String OTHERTYPE = "其他好友";

	static String[] mobile = { "134", "135", "136", "137", "138", "139", "147",
			"150", "151", "152", "157", "158", "159", "182", "183", "187",
			"188" };
	static String[] unicom = { "130", "131", "132", "155", "156", "185", "186" };
	static String[] telecom = { "133", "153", "180", "189" };

	private static List<String> chinaMobile = new ArrayList<String>(
			Arrays.asList(mobile));
	private static List<String> chinaUnicom = new ArrayList<String>(
			Arrays.asList(unicom));
	private static List<String> chinaTelecom = new ArrayList<String>(
			Arrays.asList(telecom));

	private static Map<String, List<String>> phoneTypes = new HashMap<String, List<String>>();

	public static String getNumType(String phoneNumber) {
		phoneNumber = phoneNumber.replace("-", "");
		Log.e("phoenNum", phoneNumber);
		String type = "";
		initList();
		int start = phoneNumber.indexOf('1');
		start = start > 0 ? start : 0;
		if (start >= 3)
			return OTHERTYPE;
		String token = phoneNumber.substring(start, start + 3);
		if (start != 0 && start != 3)
			return OTHERTYPE;
		if (start == 3 && phoneNumber.charAt(0) != '+')
			return OTHERTYPE;
		if (chinaMobile.contains(token)) {
			type = CHINAMOBILE;
		} else if (chinaUnicom.contains(token)) {
			type = CHINAUNICOM;
		} else if (chinaTelecom.contains(token)) {
			type = CHINATELECOM;
		} else {
			type = OTHERTYPE;
		}
		return type;
	}

	private static void initList() {
		phoneTypes.put("chinaMobile", chinaMobile);
		phoneTypes.put("chinaUnicom", chinaUnicom);
		phoneTypes.put("chinaTelecom", chinaTelecom);

	}
}
