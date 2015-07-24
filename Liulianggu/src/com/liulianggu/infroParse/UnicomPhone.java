package com.liulianggu.infroParse;

import java.util.List;

/******************
 * 联通用户的短信
 * 
 * @author xyc
 *
 */
public class UnicomPhone implements SMSParsingInterface, SendMessageInterface {

	@Override
	public List<RestPackage> getAllRestPackages(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getAllPackageGprs(String msg) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getPackageName(String sentence) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getRestFlow(String sentence) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLastDate(String sentence) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStartDate(String sentence) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getOperatorNum() {
		return "10010";
	}

	@Override
	public String getOperatorMessage() {
		return null;
	}

}
