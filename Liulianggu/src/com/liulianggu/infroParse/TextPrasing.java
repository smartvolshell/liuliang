package com.liulianggu.infroParse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TextPrasing {
	/***********
	 * 分解信息分解成句
	 * 
	 * @param articlePath
	 */
	public List<String> splitIntoSentence(String articlePath) {
		List<String> infor = new ArrayList<String>();
		try {
			String encoding = "UTF-8";

			InputStream in_withcode = new ByteArrayInputStream(
					articlePath.getBytes("UTF-8"));
			InputStreamReader read = new InputStreamReader(in_withcode,
					encoding);
			BufferedReader bufferedReader = new BufferedReader(read);
			int word = 0;
			String senten = "";
			String[] over = { "。", "；", "！", "？" };
			while ((word = bufferedReader.read()) != -1) {
				if (((char) word) != '\r' && ((char) word) != '\n') {
					boolean has = false;
					for (int i = 0; i < over.length; i++) {
						if (over[i].indexOf((char) word) >= 0)
							has = true;
					}
					if (!has) {
						senten = senten + (char) word;
					} else {
						if (!senten.equals(""))
							infor.add(senten);
						senten = "";
					}
				} else if (((char) word) != '\r') {
					if (!senten.equals(""))
						infor.add(senten);
					senten = "";
				}
			}
			read.close();

		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}

		return infor;
	}
}
