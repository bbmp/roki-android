//package com.legent.ui.ext.views;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.legent.ui.UI;
//import com.legent.ui.R;
//
//public class ButtonLine extends AbsLineView implements OnClickListener {
//
//	protected TextView txtValue;
//	protected Button button;
//	protected OnClickCallback callback;
//	protected String value, btnText;
//
//	public ButtonLine(Context context) {
//		super(context);
//	}
//	public ButtonLine(Context context, AttributeSet attrs) {
//		super(context, attrs);
//	}
//	public ButtonLine(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//	}
//
//	@Override
//	protected void init(Context cx, AttributeSet attrs) {
//		super.init(cx, attrs);
//		LayoutInflater.from(cx).inflate(R.layout.view_button_line, pnlMain,
//				true);
//		txtValue = findViewById(R.id.txtValue);
//		button = findViewById(R.id.btnButton);
//
//		if (attrs == null) {
//			btnText = "click here";
//		} else {
//			TypedArray ta = cx.obtainStyledAttributes(attrs,
//					R.styleable.ButtonLine);
//			value = ta.getString(R.styleable.ButtonLine_text);
//			btnText = ta.getString(R.styleable.ButtonLine_buttonText);
//			ta.recycle();
//		}
//
//		txtValue.setText(value);
//		button.setText(btnText);
//		button.setOnClickListener(this);
//	}
//
//	public void setOnClickCallback(OnClickCallback callback) {
//		this.callback = callback;
//	}
//
//	@Override
//	public void onClick(View view) {
//
//		int id = view.getId();
//		if (id == R.id.btnButton) {
//			if (callback != null) {
//				callback.onClick(ButtonLine.this, getText());
//			}
//		}
//
//	}
//
//	public String getText() {
//		return txtValue.getText().toString();
//	}
//
//	public void setText(int resId) {
//		this.value = UI.getString(getContext(), resId);
//		txtValue.setText(resId);
//	}
//
//	public void setText(String value) {
//		this.value = value;
//		txtValue.setText(value);
//	}
//
//	public String getButtonText() {
//		return button.getText().toString();
//	}
//
//	public void setButtonText(String text) {
//		this.btnText = text;
//		button.setText(text);
//	}
//
//	public void setButtonText(int resId) {
//		this.btnText = UI.getString(getContext(), resId);
//		button.setText(resId);
//	}
//
//	@Override
//	public void setContentDisible(boolean isDisabled) {
//		button.setEnabled(!isDisabled);
//	}
//
//	public interface OnClickCallback {
//		void onClick(View view, String value);
//	}
//
//}
