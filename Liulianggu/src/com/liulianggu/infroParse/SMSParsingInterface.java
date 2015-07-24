package com.liulianggu.infroParse;

import java.util.List;

/************
 * ���Ž����ӿ�
 * 
 * @author xyc
 *
 */
public interface SMSParsingInterface {
	/**
	 * ��ȡ��Ϣ�����е��ײ���Ϣ
	 * 
	 * @param msg
	 * @return
	 */
	public List<RestPackage> getAllRestPackages(String msg);

	/**
	 * ��ȡ��Ϣ����������
	 * 
	 * @param msg
	 * @return
	 */
	public float getAllPackageGprs(String msg);

	/**
	 * ��ȡ�����б������ײ�����
	 * 
	 * @param sentence
	 * @return
	 */
	public String getPackageName(String sentence);

	/**
	 * ��ȡ�����а�����ʣ������
	 * 
	 * @param sentence
	 * @return
	 */
	public float getRestFlow(String sentence);

	/**
	 * ��ȡ�ײͽ������� 0����Ȼ��ĩ��-1������
	 * 
	 * @param sentence
	 * @return
	 */
	public int getLastDate(String sentence);

	/**
	 * ��ȡ�ײͿ�ʼ���� 0����Ȼ��ĩ��-1������
	 * 
	 * @param sentence
	 * @return
	 */
	public int getStartDate(String sentence);
}
