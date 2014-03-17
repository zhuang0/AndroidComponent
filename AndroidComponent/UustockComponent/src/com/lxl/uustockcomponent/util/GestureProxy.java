package com.lxl.uustockcomponent.util;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

/**
 * 对MotionEvent进行方向判断。
 * 
 * @author liuxiaolong
 * 
 */
public class GestureProxy implements OnGestureListener {
	private GestureDetector mGestureDetector;
	private GestureListener gestureListener;

	public GestureProxy(Context context, GestureListener gestureListener) {
		mGestureDetector = new GestureDetector(context, this);
		this.gestureListener = gestureListener;
	}

	/**
	 * 设置手势判定
	 * 
	 * @param mEvent坐标事件
	 *            .
	 * 
	 */
	public boolean setMotionEvent(MotionEvent mEvent) {
		if (mEvent.getAction() == MotionEvent.ACTION_DOWN) {
	//		Log.i("dispatchTouchEvent...", "ACTION_DOWN");
		} else if (mEvent.getAction() == MotionEvent.ACTION_MOVE) {
	//		Log.i("dispatchTouchEvent...", "ACTION_MOVE");
		} else if (mEvent.getAction() == MotionEvent.ACTION_UP) {
	//		Log.i("dispatchTouchEvent...", "ACTION_UP");
		}
		return mGestureDetector.onTouchEvent(mEvent);
	}

	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
	//	Log.i("lxl...log...setMotionEvent", e.getX() + " " + e.getY());
		return true;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		final int FLING_MIN_DISTANCE = 100, FLING_MIN_VELOCITY = 200;
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			gestureListener.gestureLeft(e1.getX() - e2.getX());
	//		Log.i("MyGesture", "Fling left");
		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			gestureListener.gestureRight(e1.getX() - e2.getX());
	//		Log.i("MyGesture", "Fling right");
		} else if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE
				&& Math.abs(velocityY) > FLING_MIN_VELOCITY) {
			gestureListener.gestureDown(e1.getX() - e2.getX());
	//		Log.i("MyGesture", "Fling down");
		} else if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE
				&& Math.abs(velocityY) > FLING_MIN_VELOCITY) {
			gestureListener.gestureUp(e1.getX() - e2.getX());
	//		Log.i("MyGesture", "Fling up");
		}
		return true;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public interface GestureListener {
		public void gestureLeft(Float distance);

		public void gestureRight(Float distance);

		public void gestureUp(Float distance);

		public void gestureDown(Float distance);
	}

}
