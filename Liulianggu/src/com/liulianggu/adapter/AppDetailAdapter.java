package com.liulianggu.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

/***********
 * ͼƬ������������
 * 
 * @author xyc
 *
 */
public class AppDetailAdapter extends PagerAdapter {
	/**
	 * װImageView����
	 */
	private ImageView[] mImageViews;

	public AppDetailAdapter(ImageView[] mImageViews) {
		this.mImageViews = mImageViews;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		// ((ViewPager) container).removeView(mImageViews[position
		// % mImageViews.length]);

	}

	/**
	 * ����ͼƬ��ȥ���õ�ǰ��position ���� ͼƬ���鳤��ȡ�����ǹؼ�
	 */
	@Override
	public Object instantiateItem(View container, int position) {
		try {
			((ViewPager) container).addView(mImageViews[position
					% mImageViews.length], 0);
		} catch (Exception e) {
			// handler something
		}
		return mImageViews[position % mImageViews.length];
	}

}
