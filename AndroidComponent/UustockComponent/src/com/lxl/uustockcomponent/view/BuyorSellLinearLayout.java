package com.lxl.uustockcomponent.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 当退出软键盘的时候，布局最外层的view的高度会发生变化，一般以这种方式来监听键盘弹出事件，
 * 此view会回调此事件，（建议此view和scrollView联合应用，在键盘弹出的时候调用scrollView.scrollTo,
 * 来达到键盘弹出时，布局向上去的效果）
 * 
 * @author liuxiaolong
 * 
 */
public class BuyorSellLinearLayout extends LinearLayout {
	private SizeChangeListener sizeChangeListener;

	public BuyorSellLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setSizeChangeListener(SizeChangeListener sizeChangeListener) {
		this.sizeChangeListener = sizeChangeListener;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		if (sizeChangeListener != null) {
			sizeChangeListener.change(w, h, oldw, oldh);
		}
		super.onSizeChanged(w, h, oldw, oldh);
	}

	public interface SizeChangeListener {
		void change(int w, int h, int oldw, int oldh);
	}
}
