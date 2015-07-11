package com.liulianggu.userOpration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.liulianggu.beans.AdvertisementItem;
import com.liulianggu.sever.SeverOpration;
import com.liulianggu.tabmenu.R;

public class AdvertisementOpration extends Activity {
	/******
	 * 根据条件获取应用列表
	 * 
	 * @param mContext
	 * @param type
	 *            ，app类型
	 * @param sorType
	 *            ，排序类型
	 * @param sortDirection
	 *            ，排序方向；1：升序，2：降序
	 * @param start
	 *            ，开始位置
	 * @param end
	 *            ，结束位置
	 * @return
	 */
	public List<AdvertisementItem> getData(Context mContext, String type,
			String sorType, int sortDirection, int clo) {
		String optypeString = "";
		int sortType = 0;
		if (type.equals("全部"))
			optypeString = "selectSome";
		else {
			optypeString = "selectSomeType";
		}
		if (sorType.equals("下载量")) {
			sortType = 1;
		} else if (sorType.equals("评价")) {
			sortType = 2;
		}

		Log.e("log_tag", type + "1" + sorType + "1" + clo);
		// List<AdvertisementItem> advertisementItems = new
		// ArrayList<AdvertisementItem>();
		List<AdvertisementItem> advertisementItems = new SeverOpration()
				.getAppInfo(mContext, optypeString, type, sortType, clo);

		return advertisementItems;
	}

	public boolean apkDownLoad(String url) throws IOException {
		SeverOpration severOpration = new SeverOpration();
		return severOpration.apkDownLoad(url);
	}
}
