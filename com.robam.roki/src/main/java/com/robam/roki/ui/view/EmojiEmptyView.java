package com.robam.roki.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.legent.utils.LogUtils;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EmojiEmptyView extends FrameLayout {
    private static final String TAG = "EmojiEmptyView";

    private TextView txtDesc;

    public EmojiEmptyView(Context context) {
        super(context);
        init(context, null);
    }

    public EmojiEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EmojiEmptyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_emoji_empty,
                this, true);
        txtDesc = (TextView) view.findViewById(R.id.txtDesc);
        if (attrs != null) {
            TypedArray ta = cx.obtainStyledAttributes(attrs, R.styleable.EmojiEmpty);
            String desc = ta.getString(R.styleable.EmojiEmpty_description);
            ta.recycle();
            setText(desc);
        } else {
            LogUtils.i(TAG, "EmojiEmpyt attrs null");
        }
    }


    public void setText(String text) {
        txtDesc.setText(text);
    }
    public void setText(SpannableString spanned) {
        txtDesc.setText(spanned);
    }
}
