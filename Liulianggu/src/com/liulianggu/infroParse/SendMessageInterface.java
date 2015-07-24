package com.liulianggu.infroParse;

/*****************
 * 发送短信接口
 * 
 * @author xyc
 *
 */
public interface SendMessageInterface {
	/***********
	 * 运营商号码
	 * 
	 * @return
	 */
	public String getOperatorNum();

	/***********
	 * 应该发送的信息
	 * 
	 * @return
	 */
	public String getOperatorMessage();
}
