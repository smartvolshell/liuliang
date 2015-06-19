package com.liulianggu.games;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;

public class GameOf2048View extends GridLayout {
	private Card[][] cardsMap = new Card[4][4];
	private List<Point> emptyPoints = new ArrayList<Point>();

	public GameOf2048View(Context context) {
		super(context);
		initGameView();
	}

	public GameOf2048View(Context context, AttributeSet attrs) {
		super(context, attrs);
		initGameView();
	}

	public GameOf2048View(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initGameView();
	}

	private void initGameView() {
		setColumnCount(4);// 4列
		setRowCount(4);// 4行
		setBackgroundColor(0xffbbada0);// 表格背景色
		setOnTouchListener(new View.OnTouchListener() {
			/**
			 * 手指按下的坐标
			 */
			private float startX, startY;
			/**
			 * 手指松开后偏移的坐标
			 */
			private float offsetX, offsetY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// 手指按下
					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_UP:// 手指松开
					offsetX = event.getX() - startX;
					offsetY = event.getY() - startY;

					if (Math.abs(offsetX) > Math.abs(offsetY)) {
						// x轴方向的差距比y轴方向的差距大,手指水平方向移动
						if (offsetX < -5) {
							swipeLeft();
							GameOf2048.getMainActivity().SaveSDorReadSD();

						} else if (offsetX > 5) {
							swipeRight();
							GameOf2048.getMainActivity().SaveSDorReadSD();
						}
					} else {
						// 手指上下方向移动
						if (offsetY < -5) {
							swipeUp();
							GameOf2048.getMainActivity().SaveSDorReadSD();
						} else if (offsetY > 5) {
							swipeDown();
							GameOf2048.getMainActivity().SaveSDorReadSD();
						}
					}

					break;
				}
				return true;
			}
		});
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL);// 实心矩形框
		int width_pm = GameOf2048.width_pm;
		int height_pm = GameOf2048.height_pm;
		canvas.drawRect(new RectF(0, 730, width_pm, height_pm), paint);
		super.onDraw(canvas);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int cardWidth = (Math.min(w, h) - 10) / 4;// 正方形卡片
		int cardHeight = (Math.min(w, h) - 10) / 4;
		addCards(cardWidth, cardHeight);
		startGame();
	}

	/**
	 * 添加卡片
	 */
	private void addCards(int cardWidth, int cardHeight) {
		Card c;
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				c = new Card(getContext());
				c.setNum(0);
				addView(c, cardWidth, cardHeight);
				cardsMap[x][y] = c;
			}
		}
	}

	/**
	 * 开始游戏
	 */
	private void startGame() {
		GameOf2048.getMainActivity().showScore();
		GameOf2048.getMainActivity().clearScore();
		// 清除数据
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				cardsMap[x][y].setNum(0);
			}
		}
		addRandonNum();// 添加随机数
		addRandonNum();
	}

	/**
	 * 添加数字
	 */
	private void addRandonNum() {
		emptyPoints.clear();

		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (cardsMap[x][y].getNum() <= 0) {
					emptyPoints.add(new Point(x, y));
				}
			}
		}
		// 随机位置
		Point p = emptyPoints
				.remove((int) (Math.random() * emptyPoints.size()));
		// 产生的随机数大于0.1的话输出2，否则输出4 // 出现2的机会与出现4的机会之比为:9:1
		cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);
		cardsMap[p.x][p.y].addScaleAnimation();
	}

	private void checkComplete() {
		boolean complete = true;
		ALL: for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (cardsMap[x][y].getNum() == 0
						|| (x > 0 && cardsMap[x][y].equals(cardsMap[x - 1][y]))
						|| (x < 3 && cardsMap[x][y].equals(cardsMap[x + 1][y]))
						|| (y > 0 && cardsMap[x][y].equals(cardsMap[x][y - 1]))
						|| (y < 3 && cardsMap[x][y].equals(cardsMap[x][y + 1]))) {
					complete = false;
					break ALL;
				}
			}
		}
		if (complete) {
			GameOf2048.getMainActivity().SaveSDFile();
			AlertDialog alertDialog = new AlertDialog.Builder(getContext())
					.setTitle("游戏结束")
					.setMessage("本轮得分:" + GameOf2048.score)
					.setPositiveButton("再来一次",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									startGame();

								}
							}).create();
			Window window = alertDialog.getWindow();
			WindowManager.LayoutParams lp = window.getAttributes();
			lp.alpha = 0.8f;// 这里设置透明度
			window.setAttributes(lp);
			alertDialog.show();
		}
	}

	/**
	 * 向 左划
	 */
	private void swipeLeft() {
		boolean merge = false;
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				for (int x1 = x + 1; x1 < 4; x1++) {
					if (cardsMap[x1][y].getNum() > 0) {// 不为空
						if (cardsMap[x][y].getNum() <= 0) {// 当前位置的值为空
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);
							x--;
							merge = true;
						} else if (cardsMap[x][y].equals(cardsMap[x1][y])) {// 相同
							cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
							cardsMap[x1][y].setNum(0);
							GameOf2048.getMainActivity().addScore(
									cardsMap[x][y].getNum());
							merge = true;
						}
						break;
					}
				}
			}
		}
		if (merge) {
			addRandonNum();
			checkComplete();
		}
	}

	/**
	 * 向右划
	 */
	private void swipeRight() {
		boolean merge = false;
		for (int y = 0; y < 4; y++) {
			for (int x = 3; x >= 0; x--) {
				for (int x1 = x - 1; x1 >= 0; x1--) {
					if (cardsMap[x1][y].getNum() > 0) {// 不为空
						if (cardsMap[x][y].getNum() <= 0) {// 当前位置的值为空
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);
							x++;
							merge = true;
						} else if (cardsMap[x][y].equals(cardsMap[x1][y])) {// 相同
							cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
							cardsMap[x1][y].setNum(0);
							GameOf2048.getMainActivity().addScore(
									cardsMap[x][y].getNum());
							merge = true;
						}
						break;
					}
				}
			}
		}
		if (merge) {
			addRandonNum();
			checkComplete();
		}
	}

	/**
	 * 向上划
	 */
	private void swipeUp() {
		boolean merge = false;
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				for (int y1 = y + 1; y1 < 4; y1++) {
					if (cardsMap[x][y1].getNum() > 0) {// 不为空
						if (cardsMap[x][y].getNum() <= 0) {// 当前位置的值为空
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);
							y--;
							merge = true;
						} else if (cardsMap[x][y].equals(cardsMap[x][y1])) {// 相同
							cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
							cardsMap[x][y1].setNum(0);
							GameOf2048.getMainActivity().addScore(
									cardsMap[x][y].getNum());
							merge = true;
						}
						break;
					}
				}
			}
		}
		if (merge) {
			addRandonNum();
			checkComplete();
		}
	}

	/**
	 * 向下划
	 */
	private void swipeDown() {
		boolean merge = false;
		for (int x = 0; x < 4; x++) {
			for (int y = 3; y >= 0; y--) {
				for (int y1 = y - 1; y1 >= 0; y1--) {
					if (cardsMap[x][y1].getNum() > 0) {// 不为空
						if (cardsMap[x][y].getNum() <= 0) {// 当前位置的值为空
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);
							y++;
							merge = true;
						} else if (cardsMap[x][y].equals(cardsMap[x][y1])) {// 相同
							cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
							cardsMap[x][y1].setNum(0);
							GameOf2048.getMainActivity().addScore(
									cardsMap[x][y].getNum());
							merge = true;
						}
						break;
					}
				}
			}
		}
		if (merge) {
			addRandonNum();
			checkComplete();
		}
	}

}
