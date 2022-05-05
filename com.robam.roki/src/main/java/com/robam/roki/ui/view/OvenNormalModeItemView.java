package com.robam.roki.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.plat.pojos.dictionary.DeviceType;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by linxiaobin on 2015/12/19.
 */
public class OvenNormalModeItemView extends FrameLayout {

    @InjectView(R.id.imageKind)
    ImageView imageKind;
    @InjectView(R.id.txtKindTitle)
    TextView txtTitle;

    public OvenNormalModeItemView(Context context) {
        super(context);
        init(context,null);
    }

    public OvenNormalModeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);

    }

    public OvenNormalModeItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs);

    }

    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_oven_normal_unlisted_item,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);

            if (attrs != null) {
                TypedArray ta = cx.obtainStyledAttributes(attrs,
                        R.styleable.DeviceOvenNormalItem);
                String title = ta.getString(R.styleable.DeviceOvenNormalItem_title);
                int imgResid = ta.getResourceId(R.styleable.DeviceOvenNormalItem_imgSource, 0);
                ta.recycle();
                txtTitle.setText(title);
                imageKind.setImageResource(imgResid);
            }
        }
    }

    public void loadData(DeviceType devType) {
        if (devType == null) return;
        txtTitle.setText(devType.getName());
//        if (devType.tag != null) {
//            txt.setText(devType.tag.toString());
//        }
    }
}
