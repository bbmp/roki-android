//package com.legent.ui.ext;
//
//import android.content.res.Configuration;
//import android.content.res.Resources;
//
//import com.legent.ui.AbsActivity;
//import com.legent.ui.R;
//
//public abstract class BaseActivity extends AbsActivity {
//
//
//	@Override
//	protected void setContentView() {
//		setContentView(R.layout.abs_activity);
//	}
//
//	@Override
//	public Resources getResources() {
//		Resources res = super.getResources();
//		//非默认值
//		if (res.getConfiguration().fontScale != 1) {
//			Configuration newConfig = new Configuration();
//			newConfig.setToDefaults();//设置默认
//			res.updateConfiguration(newConfig, res.getDisplayMetrics());
//		}
//		return res;
//	}
//
//}
