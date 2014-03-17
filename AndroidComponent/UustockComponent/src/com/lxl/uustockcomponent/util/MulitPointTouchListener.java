package com.lxl.uustockcomponent.util;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * <hn>用于实现{@link #ImageView}的多点触控缩放功能</hn><li>{@link #ImageView} 及其子类均可使用</li>
 * <li>请尽量增加{@link #ImageView}控件的宽度和高度</li> <li>为 {@link #ImageView}注册触摸事件已生效<br>
 * &emsp;&emsp;
 * {@code imageView.setOnTouchListener(new MulitPointTouchListener ());}</li>
 * 
 * @author Angleline
 * @since 2013-11-13
 * 
 */
public class MulitPointTouchListener implements OnTouchListener {
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	/**
	 * <b>默认</b>&emsp;状态标志
	 */
	private static final int NONE = 0;
	/**
	 * <b>拖拽</b>&emsp;状态标志
	 */
	private static final int DRAG = 1;
	/**
	 * <b>缩放</b>&emsp;状态标志
	 */
	private static final int ZOOM = 2;
	private int mode = NONE;

	private PointF start = new PointF();
	private PointF mid = new PointF();
	private float oldDist = 1f;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView view = (ImageView) v;
		view.setScaleType(ScaleType.MATRIX);
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			matrix.set(view.getImageMatrix());
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			mode = DRAG;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x, event.getY()
						- start.y);
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = newDist / oldDist;
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			}
			break;
		}
		view.setImageMatrix(matrix);
		return true;
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
}