package com.lxl.uustockcomponent.loadimagecache;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 本类对异步加载图片缓存的第三方类库进行了封装。
 * 
 * @author liuxiaolong
 * 
 */
public class ImageLoadUtil {

	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public ImageLoadUtil(int init, int loading, int errer) {
		setImageOptions(init, loading, errer);
	}

	/**
	 * 加载网路图片到imageview上。
	 * 
	 * @param url
	 *            图片的url。
	 * @param imageView
	 *            显示图片的imageview。
	 */
	public void loadImage(String url, ImageView imageView) {
		imageLoader.displayImage(url, imageView, options, animateFirstListener);
	}

	/**
	 * 设置图片加载器
	 * 
	 * @param init
	 *            图片加载前初始显示的图片。
	 * @param loading
	 *            图片加载中显示的图片。
	 * @param errer
	 *            加载错误时显示的图片。
	 */
	private void setImageOptions(int init, int loading, int errer) {
		options = new DisplayImageOptions.Builder().showStubImage(init)
				.showImageForEmptyUri(loading).showImageOnFail(errer)
				.cacheInMemory(true).cacheOnDisc(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();
		animateFirstListener = new AnimateFirstDisplayListener();
	}

	/**
	 * 清除内存缓存。
	 */
	public void clearMemoryCache() {
		imageLoader.clearMemoryCache();
	}

	/**
	 * 停止加载图片
	 */
	public void stop() {
		imageLoader.stop();
	}

	/**
	 * 清除硬盘缓存。
	 */
	public void clearDiscCache() {
		imageLoader.clearDiscCache();
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
