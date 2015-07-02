package com.liulianggu.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;

/******************
 * 头像选择辅助类；从相册选取图片，照相选取
 * 
 * @author xyc
 *
 */
public class BitmapUtil {

	// 对外公开的
	public Uri photoUri;

	private File picFile;
	private Uri tempUri;

	public final static int activity_result_camara_with_data = 1006;
	public final static int activity_result_cropimage_with_data = 1007;

	private Context context;

	public BitmapUtil(Context context) {
		super();
		this.context = context;
	}

	/**
	 * 拍照获取图片
	 * 
	 */
	public void doTakePhoto() {
		try {
			File uploadFileDir = new File(
					Environment.getExternalStorageDirectory(), "/upload");
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			if (!uploadFileDir.exists()) {
				uploadFileDir.mkdirs();
			}
			picFile = new File(uploadFileDir,
					SystemClock.currentThreadTimeMillis() + ".png");
			if (!picFile.exists()) {
				picFile.createNewFile();
			}
			tempUri = Uri.fromFile(picFile);

			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
			((Activity) context).startActivityForResult(cameraIntent,
					activity_result_camara_with_data);
			Log.i("TAG",
					"photo1:" + tempUri.toString() + ",photo2:"
							+ tempUri.getPath());
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从相册选取图片
	 * 
	 */
	public void doCropPhoto() {
		try {

			File pictureFileDir = new File(
					Environment.getExternalStorageDirectory(), "/upload");
			if (!pictureFileDir.exists()) {
				pictureFileDir.mkdirs();
			}
			picFile = new File(pictureFileDir,
					SystemClock.currentThreadTimeMillis() + ".png");
			if (!picFile.exists()) {
				picFile.createNewFile();
			}
			tempUri = Uri.fromFile(picFile);

			final Intent intent = getCropImageIntent();

			((Activity) context).startActivityForResult(intent,
					activity_result_cropimage_with_data);
			Log.i("TAG",
					"photo3:" + tempUri.toString() + ",photo4:"
							+ tempUri.getPath());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructs an intent for image cropping. 调用图片剪辑程序
	 */
	private Intent getCropImageIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		return intent;
	}

	public void cropImageUriByTakePhoto() {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(tempUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		((Activity) context).startActivityForResult(intent,
				activity_result_cropimage_with_data);
	}

	public Bitmap decodeUriAsBitmap() {
		Bitmap bitmap = null;
		try {
			if (tempUri != null) {
				photoUri = tempUri;
				bitmap = BitmapFactory.decodeStream(context
						.getContentResolver().openInputStream(photoUri));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/***************
	 * 转换bitmap大小
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap small(Context mContext, Bitmap bitmap) {
		Matrix matrix = new Matrix();
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Log.e("log_tag", width + "-----" + height);
		int newWidth = dip2px(mContext, 60);
		int newHeight = dip2px(mContext, 60);

		matrix.postScale((float) newWidth / width, (float) newHeight / height); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	/**************
	 * dipTopx
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/****************
	 * PxTodip
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}