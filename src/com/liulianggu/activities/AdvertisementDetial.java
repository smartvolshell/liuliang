package com.liulianggu.activities;

import com.liulianggu.beans.AdvertisementItem;
import com.liulianggu.tabmenu.R;
import com.liulianggu.tabmenu.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class AdvertisementDetial extends Activity {
	ImageView image;
	RatingBar newRating;
	RatingBar oldRating;
	TextView advertiseDetail;
	AdvertisementItem item;
	Intent intent1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advertisement_detial);
		intent1 = getIntent();

		init();
	}

	private void init() {
		image = (ImageView) findViewById(R.id.img);
		newRating = (RatingBar) findViewById(R.id.rating_new);
		oldRating = (RatingBar) findViewById(R.id.rating_old);
		advertiseDetail = (TextView) findViewById(R.id.ad_detail);

		image.setBackgroundDrawable(this.getResources().getDrawable(
				R.drawable.liulianggu));
		oldRating.setRating(intent1.getFloatExtra("oldRating", -1));
		advertiseDetail.setText(intent1.getStringExtra("detail"));
	}
}
