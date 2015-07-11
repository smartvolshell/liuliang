package com.liulianggu.infroParse;

import java.util.ArrayList;
import java.util.List;

public class ChargeDetail {
	// �ƶ����ŷָ�ķָ���
	private String[] over = { "��", "��", "��", "��" };

	/**********************
	 * ��ȡ�ĵ������а����������ײ���Ϣ
	 * 
	 * @param path
	 * @return
	 */
	public List<Charge> getAllCharges(String path) {
		List<Charge> charges = new ArrayList<Charge>();
		TextPrasing text = new TextPrasing();
		List<String> sentences = text.splitIntoSentence(path, over);
		for (int i = 0; i < sentences.size(); i++) {
			String sentence = sentences.get(i);
			String chargeName = getChargeName(sentence);
			if (!chargeName.isEmpty()) {
				Charge charge = new Charge(chargeName);
				charge.setSumGprs(getGPRS(sentence));
				charge.setPrice(getPrice(sentence));
				if (charge.getSumGprs() != -1 && charge.getPrice() != -1) {
					charge.setValue();
					charges.add(charge);
				}
			}
		}
		return charges;
	}

	/*********************
	 * ��ȡ�ײ�����
	 * 
	 * @param sentence
	 * @return
	 */

	private String getChargeName(String sentence) {
		String chargeName = "";
		if (sentence.contains(".") && sentence.contains(":")) {
			if (sentence.contains("->"))
				chargeName = sentence.substring(sentence.indexOf(".") + 1,
						sentence.indexOf("->"));
			else
				chargeName = sentence.substring(sentence.indexOf(".") + 1,
						sentence.indexOf(":"));
		}
		return chargeName;
	}

	/************
	 * ��ȡ�ײ�����
	 * 
	 * @param sen
	 * @return
	 */
	private float getGPRS(String sen) {
		float gprs = -1;
		String type = "";
		for (int i = 0; i < sen.length(); i++) {
			if ((sen.charAt(i) == 'M' || sen.charAt(i) == 'G')
					&& sen.substring(i + 2, sen.length() - 1).startsWith(
							"����������")) {
				type = type + sen.charAt(i + 1);
				int mul = 1;
				if (sen.charAt(i) == 'G')
					mul = 1024;
				String str = "";
				String str1 = "";
				i--;
				while (isNum(sen.charAt(i) + str)
						|| isNum(sen.charAt(i - 1) + sen.charAt(i) + str)) {
					str1 = str;
					str = sen.charAt(i) + str;
					i--;
				}
				gprs = Float.parseFloat(str1) * mul;
				break;
			}
		}
		return gprs;
	}

	/**************************
	 * ��ȡ�ײͼ�ֵ
	 * 
	 * @param sen
	 * @return
	 */

	private float getPrice(String sen) {
		float price = -1;
		for (int i = 0; i < sen.length(); i++) {
			if (sen.charAt(i) == 'Ԫ'
					&& sen.substring(i + 1, sen.length() - 1).startsWith("/��")) {
				String str = "";
				String str1 = "";
				i--;
				while (isNum(sen.charAt(i) + str)
						|| isNum(sen.charAt(i - 1) + sen.charAt(i) + str)) {
					str1 = str;
					str = sen.charAt(i) + str;
					i--;
				}
				price = Float.parseFloat(str1);
				break;
			}
		}
		return price;
	}

	/*********************
	 * �ж��ַ����Ƿ�Ϊ����
	 * 
	 * @param str
	 * @return
	 */
	private boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
}
