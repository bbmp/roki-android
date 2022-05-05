package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.robam.common.Utils;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RecipeCookingFanStatusView extends FrameLayout {

    @InjectView(R.id.txtValue)
    TextView txtValue;

    public RecipeCookingFanStatusView(Context context) {
        super(context);
        init(context, null);
    }

    public RecipeCookingFanStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecipeCookingFanStatusView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_recipe_cooking_device_status_fan,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
    }

    public void setLevel(short level) {
        boolean isRoki = Utils.hasRokiDevice();

        String str = "关";
        if (level > AbsFan.PowerLevel_0) {
            if (level <= AbsFan.PowerLevel_2) {
                str = "弱";
            } else if (level == AbsFan.PowerLevel_3) {
                str = isRoki ? "强" : "中";
            } else if (level == AbsFan.PowerLevel_6) {
                str = isRoki ? "爆" : "强";
            }
        }

        txtValue.setText(str);
    }

}
