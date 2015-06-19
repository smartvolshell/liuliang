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
	 * ����������ȡӦ���б�
	 * 
	 * @param mContext
	 * @param type
	 *            ��app����
	 * @param sorType
	 *            ����������
	 * @param sortDirection
	 *            ��������1������2������
	 * @param start
	 *            ����ʼλ��
	 * @param end
	 *            ������λ��
	 * @return
	 */
	public List<AdvertisementItem> getData(Context mContext, String type,
			String sorType, int sortDirection, int start, int end) {
		List<AdvertisementItem> advertisementItems = new ArrayList<AdvertisementItem>();
		for (int i = start; i <= end; i++) {
			Bitmap imagBitmap = BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.liulianggu);
			String title = "���" + i;
			String detail = type + i + "����" + sorType + "����" + sortDirection;
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
