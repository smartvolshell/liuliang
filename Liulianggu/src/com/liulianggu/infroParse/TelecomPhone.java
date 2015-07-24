package com.liulianggu.infroParse;

import java.util.ArrayList;
import java.util.List;

/*************
 * �����û��Ķ���
 * 
 * @author xyc
 *
 */
public class TelecomPhone implements SMSParsingInterface, SendMessageInterface {
	// ���Ŷ��ŵķָ��
	String[] telecomeOver = { "��", ";", "��", "��", "." };

	@Override
	public List<RestPackage> getAllRestPackages(String msg) {
		List<RestPackage> restPackages = new ArrayList<RestPackage>();
		// �Ѷ�������Ϣ�ֽ�ɾ�
		TextPrasing text = new TextPrasing();
		List<String> sentences = text.splitIntoSentence(msg, telecomeOver);
		// ���������������а����������ײ���Ϣ
		for (int i = 0; i < sentences.size(); i++) {
			String sentence = sentences.get(i);
			if (sentence.contains("����")) {
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

		return "�����ײ�";
	}

	@Override
	public float getRestFlow(String sentence) {
		float gprs = -1;
		String str = "";
		if (sentence.contains("ʡ������") || sentence.contains("��������")) {
			int startIndex = sentence.lastIndexOf("ʣ����:") + 4;
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
