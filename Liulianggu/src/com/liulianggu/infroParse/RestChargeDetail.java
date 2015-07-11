package com.liulianggu.infroParse;

import java.util.ArrayList;
import java.util.List;

/***********************
 * 获取文档中所有包含流量的套餐的剩余信息
 * 
 * @author xyc
 *
 */
public class RestChargeDetail {
	// 移动短信分割的分隔符
	private String[] over = { "。", "；", "！", "？" };

	public List<RestPackage> getAllRestCharge(String path) {
		List<RestPackage> restPackages = new ArrayList<RestPackage>();
		// 把短信中信息分解成句
		TextPrasing text = new TextPrasing();
		List<String> sentences = text.splitIntoSentence(path, over);
		// 逐句分析，查找其中包含流量的套餐信息
		for (int i = 0; i < sentences.size(); i++) {
			String sentence = sentences.get(i);
			String chargeName = getChargeName(sentence);
			if (!chargeName.isEmpty()) {
				RestPackage restPackage = new RestPackage();
				restPackage.setChargeName(chargeName);
				restPackage.setRestGprs(getRestGprs(sentence));
				restPackage.setLastDate(getLastDate(sentence));
				if (restPackage.getLastDate() != -1
						&& restPackage.getRestGprs() != -1)
					restPackages.add(restPackage);
			}
		}
		return restPackages;
	}

	/*****************
	 * 获取套餐名称
	 * 
	 * @param sen
	 * @return
	 */
	private String getChargeName(String sen) {
		String chargeName = "";
		for (int i = 0; i < sen.length(); i++) {
			if (sen.charAt(i) == '"') {
				i++;
				while (sen.charAt(i) != '"' && i < sen.length()) {
					chargeName += sen.charAt(i);
					i++;
				}
			}
		}
		if (chargeName.contains("送"))
			chargeName = "";
		return chargeName;
	}

	/********************
	 * 获取套餐剩余流量
	 * 
	 * @param sen
	 * @return
	 */
	private float getRestGprs(String sen) {
		float gprs = -1;
		String str = "";
		int mul = 1;
		for (int i = 0; i < sen.length(); i++) {
			if (sen.charAt(i) == '剩'
					&& sen.substring(i, sen.length() - 1).startsWith("剩余GPRS为")) {
				i = i + 7;
				while (sen.charAt(i) != 'M' && sen.charAt(i) != 'G'
						&& sen.charAt(i) != 'K') {
					str = str + sen.charAt(i);
					i++;
				}
				if (sen.charAt(i) == 'G')
					mul = 1024;
				else if (sen.charAt(i) == 'K')
					mul = 0;
				else
					mul = 1;
			}
		}
		if (!str.equals(""))
			gprs = Float.parseFloat(str) * mul;
		return gprs;
	}

	/***************
	 * 解析套餐终止日期
	 * 
	 * @param sen
	 * @return
	 */
	private int getLastDate(String sen) {
		int date = -1;
		String str = "";
		for (int i = 0; i < sen.length(); i++) {
			if (sen.charAt(i) == '有'
					&& sen.substring(i, sen.length() - 1).startsWith("有效期")) {
				str = str + sen.charAt(i + 18) + sen.charAt(i + 19);
				break;
			}
		}
		if (!str.equals(""))
			date = Integer.parseInt(str);
		return date;
	}
}
