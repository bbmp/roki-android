//package com.legent.ui.ext.views;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.TextView;
//
//import com.legent.ui.R;
//
//public class TextLine extends AbsLineView implements OnClickListener {
//
//	public interface OnClickCallback {
//		void onClick(View view, String value);
//	}
//
//	public TextLine(Context context) {
//		super(context);
//	}
//
//	public TextLine(Context context, AttributeSet attrs) {
//		super(context, attrs);
//	}
//
//	public TextLine(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//	}
//
//	protected View imgArrow;
//	protected TextView txtValue;
//
//	protected boolean clickable;
//	protected OnClickCallback callback;
//
//	@Override
//	protected void init(Context cx, AttributeSet attrs) {
//		super.init(cx, attrs);
//		LayoutInflater.from(cx).inflate(R.layout.view_text_line, pnlMain, true);
//
//		imgArrow = findViewById(R.id.imgArrow);
//		txtValue = findViewById(R.id.txtValue);
//
//		if (attrs != null) {
//			TypedArray ta = cx.obtainStyledAttributes(attrs,
//					R.styleable.TextLine);
//			String value = ta.getString(R.styleable.TextLine_text);
//			clickable = ta.getBoolean(R.styleable.TextLine_clickable, true);
//			ta.recycle();
//
//			txtValue.setText(value);
//		} else {
//			clickable = true;
//		}
//
//		pnlLine.setClickable(clickable);
//		imgArrow.setVisibility(clickable ? View.VISIBLE : View.GONE);
//		if (clickable) {
//			pnlLine.setOnClickListener(this);
//		}
//	}
//
//	@Override
//	public void onClick(View view) {
//		int id = view.getId();
//		if (id == R.id.pnlLine) {
//			if (callback != null) {
//				callback.onClick(this, getText());
//			}
//		}
//	}
//
//	public void setOnClickCallback(OnClickCallback callback) {
//		this.callback = callback;
//	}
//
//	public String getText() {
//		return txtValue.getText().toString();
//	}
//
//	public void setText(int resId) {
//		txtValue.setText(resId);
//	}
//
//	public void setText(String value) {
//		txtValue.setText(value);
//	}
//
//	@Override
//	public void setContentDisible(boolean isDisabled) {
//		pnlLine.setEnabled(!isDisabled);
//	}
//
//}
