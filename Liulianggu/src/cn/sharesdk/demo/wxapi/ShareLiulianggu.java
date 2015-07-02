package cn.sharesdk.demo.wxapi;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.liulianggu.tabmenu.R;

import android.content.Context;

/*******************
 * ����ר����
 * 
 * @author xyc
 *
 */

public class ShareLiulianggu {
	/********************
	 * ����context����ҳ���з�������
	 * 
	 * @param ctx
	 */
	public static void share(Context ctx) {
		showShare(ctx);
	}

	private static void showShare(Context ctx) {
		ShareSDK.initSDK(ctx);
		OnekeyShare oks = new OnekeyShare();
		// �ر�sso��Ȩ
		oks.disableSSOWhenAuthorize();

		// title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		oks.setTitle(ctx.getString(R.string.share));
		// titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		oks.setTitleUrl("http://weibo.com/Volshell");
		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		oks.setText("�����ȣ�����������ȫ��ʱ��");
		// imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		// oks.setImagePath("/sdcard/test.jpg");// ȷ��SDcard������ڴ���ͼƬ
		// url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		oks.setUrl("http://weibo.com/Volshell");
		// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		oks.setComment("�����ȣ�һ����~");
		// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		oks.setSite(ctx.getString(R.string.app_name));
		// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		oks.setSiteUrl("http://weibo.com/Volshell");
		oks.setDialogMode();
		// ��������GUI
		oks.show(ctx);
	}

}
