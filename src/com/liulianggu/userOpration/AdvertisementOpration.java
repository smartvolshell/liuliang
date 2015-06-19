package com.liulianggu.userOpration;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.liulianggu.beans.AdvertisementItem;
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
			String sorType, int sortDirection, int start, int end) {
		List<AdvertisementItem> advertisementItems = new ArrayList<AdvertisementItem>();
		for (int i = start; i <= end; i++) {
			Bitmap imagBitmap = BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.liulianggu);
			String title = "软件" + i;
			String detail = type + i + "排序" + sorType + "方向" + sortDirection;
			AdvertisementItem advertisementItem = new AdvertisementItem();
			advertisementItem.setImag(imagBitmap);
			advertisementItem.setAppName(title);
			advertisementItem.setAppMsg(detail);
			advertisementItem.setEvaluation((float) 3.5);
			advertisementItems.add(advertisementItem);
		}
		return advertisementItems;
	}
}
