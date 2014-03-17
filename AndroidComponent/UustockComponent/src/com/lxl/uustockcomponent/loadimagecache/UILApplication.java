package com.lxl.uustockcomponent.loadimagecache;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.CompressFormat;
import android.os.Build;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com) <uses-permission
 *         android:name="android.permission.INTERNET"/> <uses-permission
 *         android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 */
public class UILApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
		MobclickAgent.setDebugMode( true );
	}

	public static void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getIndividualCacheDirectory(context);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				// 如果图片尺寸大于了这个参数，那么就会这按照这个参数对图片大小进行限制并缓存
				.memoryCacheExtraOptions(480, 800)
				.discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null)
				.threadPoolSize(5)
				// 异步线程数
				.threadPriority(Thread.NORM_PRIORITY - 1)
				// 线程优先级
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				// default
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024)) // 内存缓存大小
				.memoryCacheSize(2 * 1024 * 1024) // 内存缓存大小
				.discCache(new UnlimitedDiscCache(cacheDir)) // 硬盘缓存路径
				.discCacheSize(50 * 1024 * 1024) // 硬盘缓存大小
				.discCacheFileCount(100) // 缓存文件数
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
				.imageDownloader(new BaseImageDownloader(context)) // default
				.imageDecoder(new BaseImageDecoder(true)) // default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.build();
		ImageLoader.getInstance().init(config);
	}
}