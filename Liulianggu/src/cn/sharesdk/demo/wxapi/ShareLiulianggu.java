package cn.sharesdk.demo.wxapi;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.liulianggu.tabmenu.R;

import android.content.Context;

/*******************
 * 分享专用类
 * 
 * @author xyc
 *
 */

public class ShareLiulianggu {
	/********************
	 * 传入context，在页面中分享内容
	 * 
	 * @param ctx
	 */
	public static void share(Context ctx) {
		showShare(ctx);
	}

	private static void showShare(Context ctx) {
		ShareSDK.initSDK(ctx);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(ctx.getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://weibo.com/Volshell");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("流量谷，开启流量的全新时代");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://weibo.com/Volshell");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("流量谷，一起来~");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(ctx.getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://weibo.com/Volshell");
		oks.setDialogMode();
		// 启动分享GUI
		oks.show(ctx);
	}

}
