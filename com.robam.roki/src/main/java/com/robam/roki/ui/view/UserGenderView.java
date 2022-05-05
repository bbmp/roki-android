package com.robam.roki.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UserGenderView extends FrameLayout {

    @InjectView(R.id.imgGender)
    ImageView imgGender;
    @InjectView(R.id.imgRadio)
    ImageView imgRadio;

    boolean isChecked;

    public UserGenderView(Context context) {
        super(context);
        init(context, null);
    }

    public UserGenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public UserGenderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_user_gender,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
            if (attrs != null) {
                TypedArray ta = cx.obtainStyledAttributes(attrs,
                        R.styleable.UserGender);
                boolean gender = ta.getBoolean(R.styleable.UserGender_gender, true);
                ta.recycle();

                imgGender.setImageResource(gender ? R.drawable.ic_user_gender_girl : R.drawable.ic_user_gender_boy);
            }
        }
    }

    @Override
    public void setSelected(boolean selected) {
        isChecked = selected;
        super.setSelected(selected);
        imgGender.setSelected(selected);
        imgRadio.setSelected(selected);
    }

    public boolean isChecked() {
        return isChecked;
    }

}
