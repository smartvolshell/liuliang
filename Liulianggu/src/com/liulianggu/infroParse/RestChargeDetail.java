package com.liulianggu.infroParse;

import java.util.ArrayList;
import java.util.List;

/***********************
 * ��ȡ�ĵ������а����������ײ͵�ʣ����Ϣ
 * 
 * @author xyc
 *
 */
public class RestChargeDetail {
	// �ƶ����ŷָ�ķָ���
	private String[] over = { "��", "��", "��", "��" };

	public List<RestPackage> getAllRestCharge(String path) {
		List<RestPackage> restPackages = new ArrayList<RestPackage>();
		// �Ѷ�������Ϣ�ֽ�ɾ�
		TextPrasing text = new TextPrasing();
		List<String> sentences = text.splitIntoSentence(path, over);
		// ���������������а����������ײ���Ϣ
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
	 * ��ȡ�ײ�����
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
		if (chargeName.contains("��"))
			chargeName = "";
		return chargeName;
	}

	/********************
	 * ��ȡ�ײ�ʣ������
	 * 
	 * @param sen
	 * @return
	 */
	private float getRestGprs(String sen) {
		float gprs = -1;
		String str = "";
		int mul = 1;
		for (int i = 0; i < sen.length(); i++) {
			if (sen.charAt(i) == 'ʣ'
					&& sen.substring(i, sen.length() - 1).startsWith("ʣ��GPRSΪ")) {
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
	 * �����ײ���ֹ����
	 * 
	 * @param sen
	 * @return
	 */
	private int getLastDate(String sen) {
		int date = -1;
		String str = "";
		for (int i = 0; i < sen.length(); i++) {
			if (sen.charAt(i) == '��'
					&& sen.substring(i, sen.length() - 1).startsWith("��Ч��")) {
				str = str + sen.charAt(i + 18) + sen.charAt(i + 19);
				break;
			}
		}
		if (!str.equals(""))
			date = Integer.parseInt(str);
		return date;
	}
}
