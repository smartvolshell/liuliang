package com.liulianggu.games;

import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout {
private int num=0;
private TextView label;
	public Card(Context context) {
		super(context);
		
		label=new TextView(getContext());
		label.setTextSize(32);
		label.setGravity(Gravity.CENTER);//中间
		
		LayoutParams lp=new LayoutParams(-1,-1);
		lp.setMargins(20,20,0,0);//卡片间隙
		addView(label,lp);
		
		setNum(0);
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
		if(num<=0){
		label.setText("");
		}else{
		label.setText(num+"");
		}
		BackgroundColor(num);
    }
  public boolean equals(Card o){
	  return getNum()==o.getNum();
  }
  /**
   * 出现卡片后的动画效果
   */
	public void addScaleAnimation(){
		ScaleAnimation sa = new ScaleAnimation(0.1f, 1, 0.1f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		sa.setDuration(250);
		setAnimation(null);
		label.startAnimation(sa);
	}
  public void BackgroundColor(int num){
	  switch (num) {
	  case 0:
          label.setBackgroundColor(0x00000000);
          break;
      case 2:
          label.setBackgroundColor(0xffeee4da);
          break;
      case 4:
          label.setBackgroundColor(0xffede0c8);
          break;
      case 8:
          label.setBackgroundColor(0xfff2b179);
          break;
      case 16:
          label.setBackgroundColor(0xfff59563);
          break;
      case 32:
          label.setBackgroundColor(0xfff67c5f);
          break;
      case 64:
          label.setBackgroundColor(0xfff65e3b);
          break;
      case 128:
          label.setBackgroundColor(0xffedcf72);
          break;
      case 256:
          label.setBackgroundColor(0xffedcc61);
          break;
      case 512:
          label.setBackgroundColor(0xffedc850);
          break;
      case 1024:
          label.setBackgroundColor(0xffedc53f);
          break;
      case 2048:
          label.setBackgroundColor(0xffedc22e);
          break;
      case 4096:
          label.setBackgroundColor(0xffffcc00);
          break;
      case 8192:
          label.setBackgroundColor(0xffFF9900);
          break;
      default:
          label.setBackgroundColor(0xff3c3a32);
          break;
       }
   }
}
