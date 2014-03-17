package com.lxl.uustockcomponent.view;

import java.util.ArrayList;
import java.util.List;

import com.lxl.uustockcomponent.R;
import com.lxl.uustockcomponent.adapter.ViewPageAdapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class TitleViewPage extends RelativeLayout implements
		OnPageChangeListener {
	ViewPager viewPager;
	ViewPageAdapter viewPageAdapter;
	LinearLayout bottomLayout;
	List<View> points = new ArrayList<View>();
	Context context;

	public TitleViewPage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
	}

	public TitleViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
	}

	private void initView() {
		viewPageAdapter = new ViewPageAdapter();
		viewPager = new ViewPager(context);
		viewPager.setOnPageChangeListener(this);
		bottomLayout = new LinearLayout(context);
		RelativeLayout.LayoutParams layoutParam1 = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		RelativeLayout.LayoutParams layoutParam2 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParam2.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParam2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		addView(viewPager, layoutParam1);
		addView(bottomLayout, layoutParam2);
	}

	/**
	 * 设置表示点的个数。
	 * 
	 * @param number
	 */
	private void initBottomPoint(int number) {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < number; i++) {
			ImageView imageView = new ImageView(context);
			bottomLayout.addView(imageView, layoutParams);
			points.add(imageView);
		}
	}

	/**
	 * 设置底部点显示状态。
	 * 
	 * @param index
	 */
	private void bottomPointShowState(int index) {
		int size = points.size();
		for (int i = 0; i < size; i++) {
			if (index == i) {
				((ImageView) (points.get(i)))
						.setImageResource(R.drawable.u_viewpage_bottom_2);
			} else {
				((ImageView) (points.get(i)))
						.setImageResource(R.drawable.u_viewpage_bottom_1);
			}
		}
	}

	/**
	 * 添加viewpage要展示的view。
	 * 
	 * @param views
	 */
	public void setShowViews(List<View> views) {
		viewPageAdapter.setShowView(views);
		viewPager.setAdapter(viewPageAdapter);
		initBottomPoint(views.size());
		bottomPointShowState(0);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		bottomPointShowState(arg0);
	}

}
