package com.liulianggu.infroParse;

import java.util.ArrayList;
import java.util.List;

/**************
 * 移动用户的短信处理
 * 
 * @author xyc
 *
 */
public class MobilePhone implements SMSParsing {
	// 移动短信分割的分隔符
	private String[] moblieOver = { "。", "；", "！", "？" };

	@Override
	public List<RestPackage> getAllRestPackages(String msg) {
		List<RestPackage> restPackages = new ArrayList<RestPackage>();
		// 把短信中信息分解成句
		TextPrasing text = new TextPrasing();
		List<String> sentences = text.splitIntoSentence(msg, moblieOver);
		// 逐句分析，查找其中包含流量的套餐信息
		for (int i = 0; i < sentences.size(); i++) {
			String sentence = sentences.get(i);
			String chargeName = getPackageName(sentence);
			if (!chargeName.isEmpty()) {
				RestPackage restPackage = new RestPackage();
				restPackage.setChargeName(chargeName);
				restPackage.setRestGprs(getRestFlow(sentence));
				restPackage.setLastDate(getLastDate(sentence));
				restPackage.setStartDate(getStartDate(sentence));
				if (restPackage.getLastDate() != -1
						&& restPackage.getRestGprs() != -1)
					restPackages.add(restPackage);
			}
		}
		return restPackages;
	}

	@Override
	public float getAllPackageGprs(String msg) {
		float allGprs = 0;
		List<RestPackage> restPackages = getAllRestPackages(msg);
		for (int i = 0; i < restPackages.size(); i++) {
			allGprs += restPackages.get(i).getRestGprs();
		}
		return allGprs;
	}

	@Override
	public String getPackageName(String sentence) {
		String chargeName = "";
		for (int i = 0; i < sentence.length(); i++) {
			if (sentence.charAt(i) == '"') {
				i++;
				while (sentence.charAt(i) != '"' && i < sentence.length()) {
					chargeName += sentence.charAt(i);
					i++;
				}
			}
		}
		if (chargeName.contains("送"))
			chargeName = "";
		return chargeName;
	}

	@Override
	public float getRestFlow(String sentence) {
		float gprs = -1;
		String str = "";
		int mul = 1;
		for (int i = 0; i < sentence.length(); i++) {
			if (sentence.charAt(i) == '剩'
					&& sentence.substring(i, sentence.length() - 1).startsWith(
							"剩余GPRS为")) {
				i = i + 7;
				while (sentence.charAt(i) != 'M' && sentence.charAt(i) != 'G'
						&& sentence.charAt(i) != 'K') {
					str = str + sentence.charAt(i);
					i++;
				}
				if (sentence.charAt(i) == 'G')
					mul = 1024;
				else if (sentence.charAt(i) == 'K')
					mul = 0;
				else
					mul = 1;
			}
		}
		if (!str.equals(""))
			gprs = Float.parseFloat(str) * mul;
		return gprs;
	}

	@Override
	public int getLastDate(String sentence) {
		int date = -1;
		String str = "";
		for (int i = 0; i < sentence.length(); i++) {
			if (sentence.charAt(i) == '有'
					&& sentence.substring(i, sentence.length() - 1).startsWith(
							"有效期")) {
				str = str + sentence.charAt(i + 18) + sentence.charAt(i + 19);
				break;
			}
		}
		if (!str.equals(""))
			date = Integer.parseInt(str);
		return date;
	}

	@Override
	public int getStartDate(String sentence) {
		int date = -1;
		String str = "";
		for (int i = 0; i < sentence.length(); i++) {
			if (sentence.charAt(i) == '有'
					&& sentence.substring(i, sentence.length() - 1).startsWith(
							"有效期")) {
				str = str + sentence.charAt(i + 9) + sentence.charAt(i + 10);
				break;
			}
		}
		if (!str.equals(""))
			date = Integer.parseInt(str);
		return date;
	}

}
