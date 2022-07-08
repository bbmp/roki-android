package com.legent.ui.ext;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.SkinAppCompatDelegateImpl;

import com.legent.ui.AbsActivity;
import com.legent.ui.R;
import com.legent.ui.ext.utils.StatusBarCompat;

import java.util.Random;

import skin.support.content.res.SkinCompatResources;
import skin.support.utils.SkinStatusBarUtils;
import skin.support.widget.SkinCompatSupportable;

public abstract class BaseActivity extends AbsActivity implements SkinCompatSupportable {

	/** Activity 回调集合 */
	private SparseArray<OnActivityCallback> mActivityCallbacks;
	@Override
	protected void setContentView() {
		//沉浸式
		SkinStatusBarUtils.translucent(this);
		//状态栏字体颜色
		StatusBarCompat.updateStatusTextColor(this);
		setContentView(R.layout.abs_activity);
	}

	@Override
	public Resources getResources() {
		Resources res = super.getResources();
		//非默认值
		if (res.getConfiguration().fontScale != 1) {
			Configuration newConfig = new Configuration();
			newConfig.setToDefaults();//设置默认
			res.updateConfiguration(newConfig, res.getDisplayMetrics());
		}

//		initFontScale();

		return res;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
		// 查看源码得知 startActivity 最终也会调用 startActivityForResult
		super.startActivityForResult(intent, requestCode, options);
	}

	/**
	 * startActivityForResult 方法优化
	 */

	public void startActivityForResult(Class<? extends Activity> clazz, OnActivityCallback callback) {
		startActivityForResult(new Intent(this, clazz), null, callback);
	}

	public void startActivityForResult(Intent intent, OnActivityCallback callback) {
		startActivityForResult(intent, null, callback);
	}

	public void startActivityForResult(Intent intent, @Nullable Bundle options, OnActivityCallback callback) {
		if (mActivityCallbacks == null) {
			mActivityCallbacks = new SparseArray<>(1);
		}
		// 请求码必须在 2 的 16 次方以内
		int requestCode = new Random().nextInt((int) Math.pow(2, 16));
		mActivityCallbacks.put(requestCode, callback);
		startActivityForResult(intent, requestCode, options);
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		OnActivityCallback callback;
		if (mActivityCallbacks != null && (callback = mActivityCallbacks.get(requestCode)) != null) {
			callback.onActivityResult(resultCode, data);
			mActivityCallbacks.remove(requestCode);
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public interface OnActivityCallback {

		/**
		 * 结果回调
		 *
		 * @param resultCode        结果码
		 * @param data              数据
		 */
		void onActivityResult(int resultCode, @Nullable Intent data);
	}

	@Override
	public void applySkin() {
		StatusBarCompat.updateStatusTextColor(this);
	}

	@Override
	public AppCompatDelegate getDelegate() {
		return SkinAppCompatDelegateImpl.get(this, this);
	}


}
