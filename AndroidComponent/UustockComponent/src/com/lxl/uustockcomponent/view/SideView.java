package com.lxl.uustockcomponent.view;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 此view组件式模仿网易新闻客户端主界面的显现效果，并加入view抽屉显示效果�?
 * 
 * @author liuxiaolong
 * 
 */
public class SideView extends RelativeLayout {
	private LinearLayout leftView, rightView;
	private ContreView centerView;
	private int mTouchSlop; // 触发移动事件的最短距�?
	private int showState = 0; // 主界面显示状态，-1为左滑出�?为未滑动�?为右滑动.
	private boolean isIssuedEvent = false; // 是否下发事件，false下发，true不下�?
	private float mLastMotionX, mLastMotionY, mLastMotionX_, mLastMotionY_;
	private int leftViewWidth = 0, conterViewWidth = 0, rightViewWidth = 0; // 左边view的宽度，右边view的宽�?
	private int leftViewhight = 0, conterViewhight = 0, rightViewhight = 0;
	private int leftViewMargin = 0, rightViewMargin = 0; // 左右view和左右边框的距离.
	private int unResponseDistance_w, unResponseDistance_h,
			unResponseDistance_w_, unResponseDistance_h_;
	private boolean leftViewOff, rightViewOff;
	// private boolean leftViewOff_, rightViewOff_;
	private SideViewStateChangeListener sideViewStateChangeListener;

	public SideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		conterViewWidth = displayMetrics.widthPixels;
		conterViewhight = displayMetrics.heightPixels;
		leftViewhight = displayMetrics.heightPixels;
		rightViewhight = displayMetrics.heightPixels;
		leftViewWidth = dip2px((float) (displayMetrics.widthPixels * 0.7));
		rightViewWidth = dip2px((float) (displayMetrics.widthPixels * 0.7));
		leftViewMargin = -dip2px(70);
		rightViewMargin = -dip2px(70);
		initView(context);
	}

	public SideView(Context context) {
		super(context);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		conterViewWidth = displayMetrics.widthPixels;
		conterViewhight = displayMetrics.heightPixels;
		leftViewhight = displayMetrics.heightPixels;
		rightViewhight = displayMetrics.heightPixels;
		leftViewWidth = (int) (displayMetrics.widthPixels * 0.7);
		rightViewWidth = (int) (displayMetrics.widthPixels * 0.7);
		leftViewMargin = -dip2px(70);
		rightViewMargin = -dip2px(70);
		initView(context);
	}

	/**
	 * 初始化view.
	 * 
	 * @param context
	 */
	private void initView(Context context) {
		leftView = new LinearLayout(context);
		rightView = new LinearLayout(context);
		centerView = new ContreView(context);
		leftView.setId(100);
		rightView.setId(101);
		centerView.setId(102);
		LayoutParams behindParams = new LayoutParams(leftViewWidth,
				leftViewhight);
		behindParams.leftMargin = leftViewMargin;
		LayoutParams behindParams1 = new LayoutParams(rightViewWidth,
				rightViewhight);
		// behindParams1.rightMargin = rightViewMargin;
		behindParams1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		LayoutParams aboveParams2 = new LayoutParams(conterViewWidth,
				conterViewhight);
		addView(leftView, behindParams);
		addView(rightView, behindParams1);
		addView(centerView, aboveParams2);
	}

	/**
	 * 添加左左，中，右view�?
	 * 
	 * @param left
	 * @param center
	 * @param right
	 */
	public void addShowView(View left, View center, View right) {
		if (left != null) {
			addLeftView(left);
		}
		if (center != null) {
			setCenterView(center);
		}
		if (right != null) {
			addRightView(right);
		}
	}

	/**
	 * 设置滑动监听。
	 * 
	 * @param changeListener
	 */
	public void setSideViewStateChangeListener(
			SideViewStateChangeListener changeListener) {
		sideViewStateChangeListener = changeListener;
	}

	/**
	 * 获取view显示状�?.
	 * 
	 * @return
	 */
	public int getViewShowState() {
		return showState;
	}

	/**
	 * 获取左viewId�?
	 * 
	 * @return
	 */
	public int getLeftParentViewId() {
		return leftView.getId();
	}

	/**
	 * 获取右viewId
	 * 
	 * @return
	 */
	public int getRightParentViewId() {
		return rightView.getId();
	}

	/**
	 * 获取中间viewId
	 * 
	 * @return
	 */
	public int getCenterParentViewId() {
		return centerView.getId();
	}

	/**
	 * 显示左边view.
	 */
	public void showLeftView() {
		leftView.setVisibility(View.VISIBLE);
		rightView.setVisibility(View.GONE);
		moveAnimation(centerView, 0, 0, leftViewWidth, 0, 0, 200);
		moveAnimation(leftView, getViewXY(leftView)[0], 0,
				Math.abs(getViewXY(leftView)[0]), 0, 0, 200);
		shieldContreViewItemFocus();
		showState = -1;
		if(sideViewStateChangeListener != null){
			sideViewStateChangeListener.stateChangeListener(showState) ;
		}
	}

	/**
	 * 显示右边view.
	 */
	public void showRightView() {
		leftView.setVisibility(View.GONE);
		rightView.setVisibility(View.VISIBLE);
		moveAnimation(centerView, 0, 0, -rightViewWidth, 0, 0, 200);
		shieldContreViewItemFocus();
		showState = 1;
		if(sideViewStateChangeListener != null){
			sideViewStateChangeListener.stateChangeListener(showState) ;
		}
	}

	/**
	 * 显示中心view.
	 */
	public void showCenterView() {
		if (showState == -1) {
			rightView.setVisibility(View.GONE);
			moveAnimation(centerView, leftViewWidth, 0, -leftViewWidth, 0, 0,
					200);
			moveAnimation(leftView, 0, 0, leftViewMargin, 0, 0, 200);
		} else if (showState == 1) {
			leftView.setVisibility(View.GONE);
			moveAnimation(centerView, -rightViewWidth, 0, rightViewWidth, 0, 0,
					200);
		}
		openContreViewItemFocus();
		showState = 0;
		if(sideViewStateChangeListener != null){
			sideViewStateChangeListener.stateChangeListener(showState) ;
		}
	}

	/**
	 * 设置无滑屏响应区域。
	 * 
	 * @param 区域左上角的坐标和右下角的坐标
	 *            .
	 */
	public void setUnResponseDistance_h(int x, int y, int x_, int y_) {
		unResponseDistance_w = x;
		unResponseDistance_h = y;
		unResponseDistance_w_ = x_;
		unResponseDistance_h_ = y_;
	}

	/**
	 * 关闭右view。
	 */
	public void closeRightView() {
		rightViewOff = true;
	}

	/**
	 * 开启右view。
	 */
	public void openRightView() {
		rightViewOff = false;
	}

	/**
	 * 关闭左view.
	 */
	public void closeLeftView() {
		leftViewOff = true;
	}

	/**
	 * 开启左view。
	 */
	public void openLeftView() {
		leftViewOff = false;
	}

	// /**
	// * 设置在中心view显示后左view显示是否屏蔽。
	// *
	// * @param off
	// */
	// public void centerShowAfterLeftOff(boolean off) {
	// leftViewOff_ = off;
	// }
	//
	// /**
	// * 设置在中心view显示后右view显示是否屏蔽。
	// *
	// * @param off
	// */
	// public void centerShowAfterRightOff(boolean off) {
	// rightViewOff_ = off;
	// }

	/**
	 * 添加左边view�?
	 * 
	 * @param view
	 */
	private void addLeftView(View view) {
		LayoutParams behindParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		leftView.addView(view, behindParams);
	}

	/**
	 * 添加右边view�?
	 * 
	 * @param view
	 */
	private void addRightView(View view) {
		LayoutParams behindParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		rightView.addView(view, behindParams);
	}

	/**
	 * 添加中心view�?
	 * 
	 * @param view
	 */
	private void setCenterView(View view) {
		LayoutParams aboveParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		centerView.addView(view, aboveParams);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		float x = ev.getX();
		float y = ev.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			mLastMotionX_ = x;
			mLastMotionY_ = y;
			isIssuedEvent = false;
			break;

		case MotionEvent.ACTION_MOVE:
			final float dx = x - mLastMotionX;
			final float xDiff = Math.abs(dx);
			final float yDiff = Math.abs(y - mLastMotionY);
			if (dx > 0 && leftViewOff && showState == 0) {
				return false;
			} else if (dx > 0 && leftViewOff && showState == -1) {
				return true;
			}
			if (dx <= 0 && rightViewOff && showState == 0) {
				return false;
			} else if (dx <= 0 && rightViewOff && showState == 1) {
				return true;
			}
			if (mLastMotionY_ > unResponseDistance_h
					&& mLastMotionY_ <= unResponseDistance_h_
					&& mLastMotionX_ > unResponseDistance_w
					&& mLastMotionX_ <= unResponseDistance_w_ && showState == 0) {
				return false;
			}
			if (xDiff > mTouchSlop && xDiff > yDiff) {
				isIssuedEvent = true;
				mLastMotionX = x;
			}
			break;
		}
		return isIssuedEvent;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		float x = event.getX();
		float y = event.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			// Log.i("lxl...log...onTouchEvent", "onTouchEvent_move");
			viewShowForActionMove((int) mLastMotionX, (int) x);
			break;
		case MotionEvent.ACTION_UP:
			// Log.i("lxl...log...onTouchEvent", "onTouchEvent_up");
			viewShowForActionUp((int) mLastMotionX, (int) x);
			break;
		}
		return true;
	}

	/**
	 * 根据view移动手势事件设置移动效果�?
	 * 
	 * @param action
	 */
	private void viewShowForActionMove(int startX, int endX) {
		int direction = judgeMoveDirectionForX(startX, endX);
		int valueX = Math.abs(endX - startX);
		switch (getViewShowState()) {
		case -1:
			if (direction == -1) { // 当左view显示并且向左滑时
				leftViewShowActionLeftMove(valueX);
			}
			break;
		case 0:
			if (direction == -1) { // 当中心view显示并且向左滑时
				if (!rightViewOff) {
					centerViewShowActionLeftMove(valueX);
				} else {
					move(centerView, 0, 0);
					move(leftView, leftViewMargin, conterViewWidth
							- leftViewWidth + Math.abs(leftViewMargin));
				}
			} else if (direction == 1) {
				if (!leftViewOff) {
					centerViewShowActionRightMove(valueX);
				} else {
					move(centerView, 0, 0);
					move(rightView,
							conterViewWidth - rightViewhight
									+ Math.abs(rightViewMargin),
							rightViewMargin);
				}
			}
			break;
		case 1:
			if (direction == 1) { // 当右view显示并且向右滑时
				rightViewShowActionRightMove(valueX);
			}
			break;
		}
	}

	/**
	 * 当右view显示并向右滑move的时候，centerview和rightview执行相应的layout�?
	 * 
	 * @param valueX
	 */
	private void rightViewShowActionRightMove(int valueX) {
		if (valueX <= rightViewWidth) {
			move(centerView, -rightViewWidth + valueX, rightViewWidth - valueX);
			// 6.26当右view收起事添加抽屉效�?
			int move = (int) (((float) -rightViewMargin / rightViewWidth) * valueX);
			int rightMove = move + (conterViewWidth - rightViewWidth);
			move(rightView, rightMove, -move);
		} else {
			move(centerView, 0, 0);
		}
	}

	/**
	 * 当centerview显示并向右滑move的时候，centerview和leftview执行相应的layout�?
	 * 
	 * @param valueX
	 */
	private void centerViewShowActionRightMove(int valueX) {
		int leftMove = 0;
		int valueX_ = 0;
		if (valueX <= leftViewWidth) { // 当中心view显示并且向右滑时
			valueX_ = valueX;
			leftView.setVisibility(View.VISIBLE);
			rightView.setVisibility(View.GONE);
			// 添加左view移动抽屉效果6.26
			int move = (int) (((float) Math.abs(leftViewMargin) / leftViewWidth) * valueX);
			leftMove = leftViewMargin + move;
		} else {
			valueX_ = Math.abs(leftViewWidth);
		}
		move(centerView, valueX_, -valueX_);
		move(leftView, leftMove, leftViewWidth - (leftViewWidth + leftMove));
	}

	/**
	 * 当centerview显示并向左滑动move的时候，rightview和centerview执行相应的layout�?
	 * 
	 * @param valueX
	 */
	private void centerViewShowActionLeftMove(int valueX) {
		if (valueX <= rightViewWidth) {
			move(centerView, -valueX, valueX);
			leftView.setVisibility(View.GONE);
			rightView.setVisibility(View.VISIBLE);
			// 添加右view移动抽屉效果6.26
			int move = (int) (((float) Math.abs(rightViewMargin) / rightViewWidth) * valueX);
			int rightMove = conterViewWidth
					- (rightViewWidth + rightViewMargin + move);
			move(rightView, rightMove, rightViewMargin + move);
		} else {
			move(rightView, conterViewWidth - rightViewWidth, 0);
		}
	}

	/**
	 * 当左view显示向左滑动move的时候，centerview和leftviwe执行相应layout�?
	 * 
	 * @param valueX
	 */
	private void leftViewShowActionLeftMove(int valueX) {
		if (valueX <= leftViewWidth) {
			move(centerView, leftViewWidth - valueX, -leftViewWidth + valueX);
			// 6.26当左view收起事添加抽屉效�?
			int move = (int) (((float) leftViewMargin / leftViewWidth) * valueX);
			move(leftView, move, conterViewWidth - leftViewWidth - move);
		} else {
			move(centerView, 0, 0);
		}
	}

	/**
	 * 根据view抬起手势事件设置移动效果�?
	 * 
	 * @param startX
	 * @param endX
	 */
	private void viewShowForActionUp(int startX, int endX) {
		int direction = judgeMoveDirectionForX(startX, endX);
		int valueX = Math.abs(endX - startX);
		switch (getViewShowState()) {
		case -1:
			// Log.i("lxl...log...viewShowForActionUp", "-1" + " " + direction);
			rightView.setVisibility(View.GONE);
			if (direction == -1) {
				leftViewShowActionLeft(valueX);
			} else {
				move(centerView, leftViewWidth, -leftViewWidth);
				move(leftView, 0, 0);
			}
			break;
		case 0:
			// Log.i("lxl...log...viewShowForActionUp", "0" + " " + direction);
			if (direction == -1) {
				if (!rightViewOff) {
					centerViewShowActionLeft(valueX);
				}
			} else if (direction == 1) {
				if (!leftViewOff) {
					centerViewShowActionRight(valueX);
				}
			}
			break;
		case 1:
			// Log.i("lxl...log...viewShowForActionUp", "1" + " " + direction);
			leftView.setVisibility(View.GONE);
			if (direction == 1) {
				rightViewShowActionRight(valueX);
			} else {
				move(centerView, -rightViewWidth, rightViewWidth);
				move(rightView, conterViewWidth - rightViewWidth, 0);
			}
			break;
		}
	}

	/**
	 * 当右view显示并且向右滑动up的时候，rightview和centerview执行相应的动�?
	 * 
	 * @param valueX
	 */
	private void rightViewShowActionRight(int valueX) {
		if (valueX >= rightViewWidth / 2 && valueX < rightViewWidth) { // 当右view显示并且向右滑并且滑动距离大于右view�?��
			moveAnimation(centerView, -rightViewWidth + valueX, 0,
					rightViewWidth - valueX, 0, 0, 100);
			int curX = getViewXY(rightView)[0];
			int forX = ((conterViewWidth - (rightViewWidth + rightViewMargin)) - curX);
			moveAnimation(rightView, curX, 0, forX, 0, 0, 100);
			openContreViewItemFocus();
			showState = 0;
			// if (rightViewOff_) {
			// closeRightView();
			// }
		} else if (valueX < rightViewWidth / 2) { // 当右view显示并且向右滑并且滑动距离大于右view�?��
			moveAnimation(centerView, -rightViewWidth + valueX, 0, -valueX, 0,
					0, 100);
			int curX = getViewXY(rightView)[0];
			int forX = -(curX - (conterViewWidth - rightViewWidth));
			moveAnimation(rightView, curX, 0, forX, 0, 0, 100);
			shieldContreViewItemFocus();
			showState = 1;
		} else {
			centerView.layout(0, 0, conterViewWidth, conterViewhight);
			rightView.layout(
					conterViewWidth - rightViewWidth - rightViewMargin, 0,
					conterViewWidth - rightViewMargin, rightViewhight);
			openContreViewItemFocus();
			showState = 0;
			// if (rightViewOff_) {
			// closeRightView();
			// }
		}
		if(sideViewStateChangeListener != null){
			sideViewStateChangeListener.stateChangeListener(showState) ;
		}
	}

	/**
	 * 当centerview完全显示并且向右滑动up的时候，centerview和leftview执行相应的动�?
	 * 
	 * @param valueX
	 */
	private void centerViewShowActionRight(int valueX) {
		if (valueX >= leftViewWidth / 2 && valueX < leftViewWidth) { // 当中心view显示并且向右滑并且滑动距离大于左view�?��
			// moveAnimation(centerView, valueX, 0, leftViewWidth - valueX, 0,
			// 0,
			// 100);
			int move = (int) (((float) Math.abs(leftViewMargin) / leftViewWidth) * valueX);
			// moveAnimation(leftView, leftViewMargin + move, 0,
			// Math.abs(leftViewMargin) - move, 0, 0, 100);
			move(centerView, leftViewWidth, -leftViewWidth);
			move(leftView, 0, 0);
			shieldContreViewItemFocus();
			showState = -1;
		} else if (valueX < leftViewWidth / 2) { // 当中心view显示并且向右滑并且滑动距离小于左view�?��
			moveAnimation(centerView, valueX, 0, -valueX, 0, 0, 100);
			moveAnimation(leftView, getViewXY(leftView)[0], 0,
					Math.abs(getViewXY(leftView)[0]) + leftViewMargin, 0, 0,
					100);
			openContreViewItemFocus();
			showState = 0;
		} else {
			// centerView.layout(leftViewWidth, 0,
			// conterViewWidth + leftViewWidth, conterViewhight);
			// leftView.layout(0, 0, leftViewWidth, leftViewhight);
			move(centerView, leftViewWidth, -leftViewWidth);
			move(leftView, 0, 0);
			showState = -1;
			shieldContreViewItemFocus();
		}
		if(sideViewStateChangeListener != null){
			sideViewStateChangeListener.stateChangeListener(showState) ;
		}
	}

	/**
	 * 当centerview完全显示并且向左滑动up的时候，centerview和rightview执行相应的动�?
	 * 
	 * @param valueX
	 */
	private void centerViewShowActionLeft(int valueX) {
		if (valueX >= rightViewWidth / 2 && valueX < rightViewWidth) { // 当中心view显示并且向左滑并且滑动距离大于右view�?��
			// moveAnimation(centerView, -valueX, 0, -rightViewWidth + valueX,
			// 0,
			// 0, 100);
			// 6.26添加
			// int curX = getViewXY(rightView)[0];
			// int forX = -(curX - (conterViewWidth - rightViewWidth));
			// moveAnimation(rightView, curX, 0, forX, 0, 0, 100);
			int move = (int) (((float) Math.abs(rightViewMargin) / rightViewWidth) * valueX);
			// moveAnimation(rightView, 0, 0, 0, 0, 0, 10);
			move(centerView, -rightViewWidth, conterViewWidth - rightViewWidth);
			move(rightView, conterViewWidth - rightViewWidth, 0);
			shieldContreViewItemFocus();
			showState = 1;
		} else if (valueX < rightViewWidth / 2) { // 当中心view显示并且向左滑并且滑动距离小于右view�?��
			moveAnimation(centerView, -valueX, 0, valueX, 0, 0, 100);
			int curX = getViewXY(rightView)[0];
			int forX = ((conterViewWidth - (rightViewWidth + rightViewMargin)) - curX);
			moveAnimation(rightView, curX, 0, forX, 0, 0, 100);
			openContreViewItemFocus();
			showState = 0;
		} else {
			move(centerView, -rightViewWidth, conterViewWidth - rightViewWidth);
			move(rightView, conterViewWidth - rightViewWidth, 0);
			// centerView.layout(-rightViewWidth, 0, conterViewWidth
			// - rightViewWidth, conterViewhight);
			// rightView.layout(conterViewWidth - rightViewWidth, 0,
			// conterViewWidth, rightViewhight);
			shieldContreViewItemFocus();
			showState = 1;
		}
		if(sideViewStateChangeListener != null){
			sideViewStateChangeListener.stateChangeListener(showState) ;
		}
	}

	/**
	 * 当左view显示并向左滑动up的时候leftview和centerview显示移动动画�?
	 * 
	 * @param valueX
	 */
	private void leftViewShowActionLeft(int valueX) {
		if (valueX >= leftViewWidth / 2 && valueX < leftViewWidth) { // 当左view显示并且向左滑并且滑动距离大于左view�?��
			moveAnimation(centerView, leftViewWidth - valueX, 0, -leftViewWidth
					+ valueX, 0, 0, 100);
			int move = (int) (((float) Math.abs(leftViewMargin) / leftViewWidth) * valueX);
			moveAnimation(leftView, -move, 0, move + leftViewMargin, 0, 0, 100);
			openContreViewItemFocus();
			showState = 0;
			// if (leftViewOff_) {
			// closeLeftView();
			// }
		} else if (valueX < leftViewWidth / 2) { // 当左view显示并且向左滑并且滑动距离小于左view�?��
			// moveAnimation(centerView, leftViewWidth - valueX, 0, valueX, 0,
			// 0,
			// 100);
			// moveAnimation(leftView, getViewXY(leftView)[0], 0,
			// Math.abs(getViewXY(leftView)[0]), 0, 0, 100);
			Log.i("lxl...log...leftViewShowActionLeft",
					"leftViewShowActionLeft");
			move(centerView, leftViewWidth, -leftViewWidth);
			move(leftView, 0, 0);
			shieldContreViewItemFocus();
			showState = -1;
		} else {
			leftView.layout(leftViewMargin, 0, leftViewWidth + leftViewMargin,
					leftViewhight);
			centerView.layout(0, 0, conterViewWidth, conterViewhight);
			openContreViewItemFocus();
			showState = 0;
			// if (leftViewOff_) {
			// closeLeftView();
			// }
		}
		if(sideViewStateChangeListener != null){
			sideViewStateChangeListener.stateChangeListener(showState) ;
		}
	}

	/**
	 * 获取view当前相对屏幕的坐标�?
	 * 
	 * @param view
	 * @return 坐标int[]，索�?为x坐标�?为y坐标.
	 */
	private int[] getViewXY(View view) {
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		return location;
	}

	/**
	 * 根据X坐标判断移动发方向�?
	 * 
	 * @param startX
	 *            �?��X坐标�?
	 * @param moveX
	 *            结束X坐标�?
	 * @return -1 左滑�? 未滑动，1 右滑�?
	 */
	private int judgeMoveDirectionForX(int startX, int endX) {
		int result = 0;
		if (startX == endX)
			return result;
		result = endX - startX > 0 ? 1 : -1;
		return result;
	}

	/**
	 * view移动动画�?
	 * 
	 * @param view
	 *            要移动的view�?
	 * @param currentX
	 *            移动view当前的坐标（相对屏幕�?
	 * @param fromX
	 *            动画�?��时view左上角的x坐标（相对移动view）�?
	 * @param toX
	 *            动画结束时左上角的x坐标（相对移动view）�?
	 * @param fromY
	 *            动画�?��时左上角的y坐标（相对移动view）�?
	 * @param toY
	 *            动画结束时左上角的y坐标（相对移动view）�?
	 * @param time
	 *            动画执行时间�?
	 */
	private void moveAnimation(View view, int currentX, float fromX, int toX,
			float fromY, float toY, int time) {
		TranslateAnimation translateAnimation = null;
		MoveAnimationListener moveAnimationListener = null;
		translateAnimation = new TranslateAnimation(fromX, toX, fromY, toY);
		translateAnimation.setDuration(time);
		moveAnimationListener = new MoveAnimationListener();
		moveAnimationListener.moveParameter(view, toX, currentX);
		translateAnimation.setAnimationListener(moveAnimationListener);
		view.startAnimation(translateAnimation);
	}

	private int dip2px(float dpValue) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 监听动画状�?事件.
	 * 
	 * @author liuxiaolong
	 * 
	 */
	private class MoveAnimationListener implements AnimationListener {
		private View moveView;
		private int toX;
		private int currentX;

		/**
		 * 添加移动view和移动后的x坐标�?
		 * 
		 * @param moveView
		 *            要移动的view�?
		 * @param toX
		 *            要移动的位置的X坐标�?
		 * @param currentX
		 *            view当前的view�?
		 */
		public void moveParameter(View moveView, int toX, int currentX) {
			this.moveView = moveView;
			this.toX = toX;
			this.currentX = currentX;
		}

		public void onAnimationEnd(Animation animation) {
			moveView.clearAnimation();
			moveView.layout(toX + currentX, 0, moveView.getWidth() + toX
					+ currentX, moveView.getHeight());
			int leftM = 0;
			int rightM = 0;
			if (moveView.getId() == 100) {
				leftM = toX + currentX;
				rightM = conterViewWidth - leftViewWidth;
				move(moveView, leftM, rightM);
			} else if (moveView.getId() == 101) {
				move(moveView, conterViewWidth - rightViewWidth, 0);
			} else {
				leftM = toX + currentX;
				rightM = -(toX + currentX);
				move(moveView, leftM, rightM);
				move(rightView, conterViewWidth - rightViewWidth, 0);
			}
			TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 0); // 防止view动画之后闪烁.
			moveView.setAnimation(anim);
		}

		public void onAnimationRepeat(Animation animation) {

		}

		public void onAnimationStart(Animation animation) {
		}
	}

	private void move(View view, int left, int right) {
		RelativeLayout.LayoutParams params = (LayoutParams) view
				.getLayoutParams();
		params.leftMargin = left;
		params.rightMargin = right;
		view.setLayoutParams(params);
	}

	/**
	 * 屏蔽中心view的子view焦点。
	 */
	private void shieldContreViewItemFocus() {
		centerView.setFocusOff(true);
		centerView.setClickable(true);
		centerView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showCenterView();
				openContreViewItemFocus();
				// if (leftViewOff_) {
				// closeLeftView();
				// }
				// if (rightViewOff_) {
				// closeRightView();
				// }
			}
		});
	}

	/**
	 * 开发中心view的子view的焦点。
	 */
	private void openContreViewItemFocus() {
		centerView.setFocusOff(false);
		centerView.setClickable(false);
	}

	@SuppressWarnings("unused")
	private class ContreView extends LinearLayout {

		private boolean off;

		public ContreView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public void setFocusOff(boolean hasFocus) {
			off = hasFocus;
		}

		@Override
		public boolean onInterceptTouchEvent(MotionEvent ev) {
			// TODO Auto-generated method stub
			return off;
		}

	}

	public interface SideViewStateChangeListener {
		public void stateChangeListener(int state);
	}
}