package com.robam.common.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;

import com.robam.common.R;

public class CountdownTimePicker extends FrameLayout {

	public static final NumberPicker.Formatter TWO_DIGIT_FORMATTER = new Formatter() {

		@Override
		public String format(int value) {
			return String.format("%02d", value);
		}
	};

	NumberPicker minutePicker, secondPicker;

	public CountdownTimePicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public CountdownTimePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public CountdownTimePicker(Context context) {
		super(context);
		init(context, null);
	}

	private void init(Context cx, AttributeSet attrs) {
		View root = LayoutInflater.from(cx).inflate(
				R.layout.view_my_timepicker, this, true);
		minutePicker = root.findViewById(R.id.npMinute);
		secondPicker = root.findViewById(R.id.npSecond);
		minutePicker.setMinValue(0);
		minutePicker.setMaxValue(89);
		minutePicker.setFormatter(TWO_DIGIT_FORMATTER);
		secondPicker.setMinValue(0);
		secondPicker.setMaxValue(59);
		secondPicker.setFormatter(TWO_DIGIT_FORMATTER);
	}

	public int getTime() {
		int minute = minutePicker.getValue();
		int second = secondPicker.getValue();
		return (minute * 60 + second);
	}

	public int getCurrentMinute() {
		return minutePicker.getValue();
	}

	public int getCurrentSecond() {
		return secondPicker.getValue();
	}

	public void setTime(int minute, int second) {
		minutePicker.setValue(minute);
		secondPicker.setValue(second);
	}

	public void setMaxTime(int minute, int second) {
		if (minute > 0) {
			minutePicker.setMaxValue(minute);
		}
		if (second > 0) {
			secondPicker.setMaxValue(second);
		}
	}
}
