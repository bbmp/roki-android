package com.robam.roki.ui.view.networkoptimization;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import com.robam.common.pojos.DeviceGroupList;
import com.robam.common.pojos.DeviceItemList;

import java.util.List;

public class DeviceGridView extends GridView {

	protected DeviceAdapter adapter;

	public DeviceGridView(Context cx) {
		super(cx);
	}

	public DeviceGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public DeviceGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	protected void init(Context cx, AttributeSet attrs) {
	}

	public void loadData(final List<List<DeviceItemList>> deviceList) {
		if (adapter == null) {
			adapter = new DeviceAdapter(getContext());
			this.setAdapter(adapter);
		}
		adapter.loadData(deviceList);
	}

}
