//package com.legent.ui.ext.views;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.text.Editable;
//import android.text.InputFilter;
//import android.text.InputType;
//import android.text.TextWatcher;
//import android.util.AttributeSet;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.EditText;
//
//import com.google.common.base.Strings;
//import com.legent.ui.UI;
//import com.legent.ui.R;
//
//public class EditLine extends AbsLineView implements OnClickListener,
//		View.OnFocusChangeListener, TextWatcher {
//
//	protected View imgClear;
//	protected EditText editValue;
//	protected boolean editable, isPassword, isMiltiLine;
//	protected String value, hint;
//	protected int maxLength, minLines;
//	protected OnEditCallback callback;
//
//	public EditLine(Context context) {
//		super(context);
//	}
//	public EditLine(Context context, AttributeSet attrs) {
//		super(context, attrs);
//	}
//	public EditLine(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//	}
//
//	@Override
//	protected void init(Context cx, AttributeSet attrs) {
//		super.init(cx, attrs);
//		LayoutInflater.from(cx).inflate(R.layout.view_edit_line, pnlMain, true);
//		imgClear = findViewById(R.id.imgClear);
//		editValue = findViewById(R.id.editValue);
//
//		if (attrs == null) {
//			editable = true;
//			isPassword = false;
//			isMiltiLine = false;
//			hint = "type something";
//			minLines = 1;
//
//		} else {
//			TypedArray ta = cx.obtainStyledAttributes(attrs,
//					R.styleable.EditLine);
//			editable = ta.getBoolean(R.styleable.EditLine_editable, true);
//			isPassword = ta.getBoolean(R.styleable.EditLine_isPassword, false);
//			isMiltiLine = ta
//					.getBoolean(R.styleable.EditLine_isMiltiLine, false);
//			value = ta.getString(R.styleable.EditLine_text);
//			hint = ta.getString(R.styleable.EditLine_hint);
//			maxLength = ta.getInt(R.styleable.EditLine_textMaxLength,
//					Integer.MAX_VALUE);
//			minLines = ta.getInt(R.styleable.EditLine_minLines, 1);
//			ta.recycle();
//		}
//
//		setText(value);
//		setEditHint(hint);
//		setEditable(editable);
//		setIsPassoword(isPassword);
//		setEditMaxLength(maxLength);
//		imgClear.setVisibility(View.GONE);
//
//		if (isMiltiLine) {
//			pnlLine.setGravity(Gravity.TOP | Gravity.LEFT);
//			editValue.setGravity(Gravity.LEFT | Gravity.TOP);
//			editValue.setMinLines(minLines);
//			editValue.setScrollbarFadingEnabled(true);
//			editValue.setBackgroundResource(R.drawable.shape_rectange_bg);
//		}
//
//		editValue.addTextChangedListener(this);
//		editValue.setOnFocusChangeListener(this);
//		imgClear.setOnClickListener(this);
//	}
//
//	public void setOnEditCallback(OnEditCallback callback) {
//		this.callback = callback;
//	}
//
//	public EditText getEditText() {
//		return editValue;
//	}
//
//	public String getText() {
//		return editValue.getText().toString();
//	}
//
//	public void setText(String value) {
//		this.value = value;
//		editValue.setText(value);
//	}
//
//	public void setText(int resId) {
//		this.value = UI.getString(getContext(), resId);
//		editValue.setText(resId);
//	}
//
//	public void setEditHint(int resid) {
//		this.hint = UI.getString(getContext(), resid);
//		editValue.setHint(hint);
//	}
//
//	public void setEditHint(String hint) {
//		this.hint = hint;
//		editValue.setHint(hint);
//	}
//
//	public void setInputType(int inputType) {
//		editValue.setInputType(inputType);
//	}
//
//	public void setEditable(boolean editable) {
//		this.editable = editable;
//		editValue.setCursorVisible(editable);// 隐藏光标
//		editValue.setFocusable(editable);// 失去焦点
//		editValue.setFocusableInTouchMode(editable);// 虚拟键盘隐藏
//	}
//
//	public void setIsPassoword(boolean isPassword) {
//
//		this.isPassword = isPassword;
//		if (isPassword)
//			setInputType(InputType.TYPE_CLASS_TEXT
//					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
//		else
//			setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//	}
//
//	public void setEditMaxLength(int maxLength) {
//		editValue.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
//				maxLength) });
//	}
//
//	@Override
//	public void onFocusChange(View view, boolean isFocused) {
//		setClearVisible();
//	}
//
//	@Override
//	public void afterTextChanged(Editable arg0) {
//		setClearVisible();
//
//		if (callback != null) {
//			value = arg0.toString();
//			callback.onTextChanged(EditLine.this, value);
//		}
//	}
//
//	@Override
//	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//			int arg3) {
//
//	}
//
//	@Override
//	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//
//	}
//
//	@Override
//	public void onClick(View view) {
//		if (R.id.imgClear == view.getId()) {
//			editValue.setText(null);
//		}
//	}
//
//	private void setClearVisible() {
//		String value = editValue.getText().toString();
//		if (!Strings.isNullOrEmpty(value) && editValue.isFocused()) {
//			imgClear.setVisibility(View.VISIBLE);
//		} else {
//			imgClear.setVisibility(View.GONE);
//		}
//	}
//
//	@Override
//	public void setContentDisible(boolean isDisabled) {
//		editValue.setEnabled(!isDisabled);
//	}
//
//	public interface OnEditCallback {
//		void onTextChanged(View view, String value);
//	}
//
//}
