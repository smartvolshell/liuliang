package com.liulianggu.infroParse;

import java.util.List;

/************
 * 短信解析接口
 * 
 * @author xyc
 *
 */
public interface SMSParsingInterface {
	/**
	 * 获取信息内所有的套餐信息
	 * 
	 * @param msg
	 * @return
	 */
	public List<RestPackage> getAllRestPackages(String msg);

	/**
	 * 获取信息内所有流量
	 * 
	 * @param msg
	 * @return
	 */
	public float getAllPackageGprs(String msg);

	/**
	 * 获取句子中保护的套餐名称
	 * 
	 * @param sentence
	 * @return
	 */
	public String getPackageName(String sentence);

	/**
	 * 获取句子中包含的剩余流量
	 * 
	 * @param sentence
	 * @return
	 */
	public float getRestFlow(String sentence);

	/**
	 * 获取套餐结束日期 0：自然月末，-1：错误
	 * 
	 * @param sentence
	 * @return
	 */
	public int getLastDate(String sentence);

	/**
	 * 获取套餐开始日期 0：自然月末，-1：错误
	 * 
	 * @param sentence
	 * @return
	 */
	public int getStartDate(String sentence);
}
