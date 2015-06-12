package com.liulianggu.userOpration;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.liulianggu.beans.AdvertisementItem;
import com.liulianggu.tabmenu.R;

public class AdvertisementOpration extends Activity {

	public List<AdvertisementItem> getData() {
		List<AdvertisementItem> advertisementItems = new ArrayList<AdvertisementItem>();
		for (int i = 0; i < 10; i++) {
			// Drawable image = this.getResources().getDrawable(
			// R.drawable.liulianggu);
			String title = "广告" + i;
			String detail = "广告详细信息" + i;
			AdvertisementItem advertisementItem = new AdvertisementItem(null,
					title, detail, (float) 3.5);
			advertisementItems.add(advertisementItem);
		}
		return advertisementItems;
	}
}
