package com.liulianggu.infroParse;

import java.util.ArrayList;
import java.util.List;

/*************
 * 电信用户的短信
 * 
 * @author xyc
 *
 */
public class TelecomPhone implements SMSParsingInterface, SendMessageInterface {
	// 电信短信的分割符
	String[] telecomeOver = { "。", ";", "！", "？", "." };

	@Override
	public List<RestPackage> getAllRestPackages(String msg) {
		List<RestPackage> restPackages = new ArrayList<RestPackage>();
		// 把短信中信息分解成句
		TextPrasing text = new TextPrasing();
		List<String> sentences = text.splitIntoSentence(msg, telecomeOver);
		// 逐句分析，查找其中包含流量的套餐信息
		for (int i = 0; i < sentences.size(); i++) {
			String sentence = sentences.get(i);
			if (sentence.contains("数据")) {
				RestPackage restPackage = new RestPackage();
				restPackage.setChargeName(getPackageName(sentence));
				restPackage.setLastDate(getLastDate(sentence));
				restPackage.setStartDate(getStartDate(sentence));
				restPackage.setRestGprs(getRestFlow(sentence));
				if (restPackage.getRestGprs() != -1)
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

		return "电信套餐";
	}

	@Override
	public float getRestFlow(String sentence) {
		float gprs = -1;
		String str = "";
		if (sentence.contains("省内数据") || sentence.contains("数据流量")) {
			int startIndex = sentence.lastIndexOf("剩余量:") + 4;
			int endIndex = sentence.lastIndexOf("M");
			str = sentence.substring(startIndex, endIndex);
		} else {
			return -1;
		}
		if (!str.equals(""))
			gprs = Float.parseFloat(str);
		return gprs;
	}

	@Override
	public int getLastDate(String sentence) {
		return 0;
	}

	@Override
	public int getStartDate(String sentence) {
		return 1;
	}

	@Override
	public String getOperatorNum() {
		return "10001";
	}

	@Override
	public String getOperatorMessage() {
		return "108";
	}

}
