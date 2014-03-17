package com.lxl.uustockcomponent.activity;

import android.app.ActivityGroup;
import android.os.Bundle;


public abstract class BaseActivityGroup extends ActivityGroup {
	private boolean isInit = true;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		findView();
		registeredEvents();
		loadView();
	}


	public void onResume() {
	    super.onResume();
	}
	public void onPause() {
	    super.onPause();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (isInit) {
			init();
			isInit = false;
		}
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	// 在baseActivity执行onCreate时执行该方法，预定义找到view.
	public abstract void findView();

	// 在baseActivity执行onCreate时执行该方法，预定义为view设置事件.
	public abstract void registeredEvents();
	
	//加载view.
	public abstract void loadView();

	// 在activity对显示的view渲染完毕并且屏幕有焦点的时候调用改方法.
	public abstract void init();
}
