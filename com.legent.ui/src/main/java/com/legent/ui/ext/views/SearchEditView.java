//package com.legent.ui.ext.views;
//
//import android.content.Context;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//
//import com.legent.ui.R;
//
//public class SearchEditView extends RelativeLayout {
//
//	EditText edit;
//	ImageView imgClear;
//	OnTextChangedListener listener;
//
//	public SearchEditView(Context context) {
//		super(context);
//		init(null);
//	}
//
//	public SearchEditView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		init(attrs);
//	}
//	public SearchEditView(Context context, AttributeSet attrs, int defaultStyle) {
//		super(context, attrs, defaultStyle);
//		init(attrs);
//	}
//
//	private void init(AttributeSet attrs) {
//
//		LayoutInflater.from(getContext()).inflate(R.layout.view_search_edit,
//				this, true);
//
//		edit = findViewById(R.id.txtSearch);
//		imgClear = findViewById(R.id.imgClear);
//		imgClear.setVisibility(View.GONE);
//
//		edit.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				String txt = s.toString();
//				imgClear.setVisibility(txt == null || txt.length() == 0 ? View.GONE
//						: View.VISIBLE);
//				SearchEditView.this.onTextChanged(txt);
//			}
//		});
//
//		imgClear.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				edit.setText(null);
//			}
//		});
//	}
//
//	public void setOnTextChangedListener(OnTextChangedListener listener) {
//		this.listener = listener;
//	}
//
//	protected void onTextChanged(String text) {
//		if (listener != null)
//			listener.onTextChanged(text);
//	}
//
//	public interface OnTextChangedListener {
//		void onTextChanged(String text);
//	}
//}
