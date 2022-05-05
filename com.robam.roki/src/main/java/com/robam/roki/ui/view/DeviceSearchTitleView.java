package com.robam.roki.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.legent.utils.api.SoftInputUtils;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DeviceSearchTitleView extends FrameLayout {

    public interface OnCancelSearchCallback {
        void onCancel();

        void onWordChanged(String word);
    }

    public DeviceSearchTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DeviceSearchTitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @InjectView(R.id.txtCanel)
    TextView txtCanel;

    @InjectView(R.id.edtSearch)
    EditText edtSearch;

    OnCancelSearchCallback callback;
    boolean isEdit;

    void init(Context cx, AttributeSet attrs) {
        View view = LayoutInflater.from(cx).inflate(
                R.layout.view_device_search_title, this, true);

        if (!isInEditMode()) {
            ButterKnife.inject(this, view);
            edtSearch.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
                    | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            edtSearch.addTextChangedListener(txtWtcher);

            toggleSoftInput();
        }
    }

    @OnClick(R.id.txtCanel)
    public void onCancel() {
        if (callback != null) {
            callback.onCancel();
        }
    }

    private void toggleSoftInput() {
        SoftInputUtils.show(getContext(), edtSearch);
    }

    public void setOnCancelSearchCallback(OnCancelSearchCallback callabck) {
        this.callback = callabck;
    }

    public void setWord(final String word) {
        edtSearch.setText(word);
    }

    TextWatcher txtWtcher = new TextWatcher() {

        @Override
        public void onTextChanged(final CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            msgCount++;
            mHandler.sendEmptyMessageDelayed(MSGCODE, 1000);
        }
    };

    void search(String word) {
        if (callback != null) {
            callback.onWordChanged(word);
        }
    }

    int msgCount = 0;
    final int MSGCODE = 0;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MSGCODE) {
                if (msgCount == 1) {
                    search(edtSearch.getText().toString());
                    msgCount = 0;
                } else {
                    msgCount--;
                }

            }
        }
    };
}
