package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.legent.utils.LogUtils;
import com.robam.common.Utils;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RecipeCookingStoveStatusView extends FrameLayout {

    @InjectView(R.id.txtValue)
    TextView txtValue;

    public RecipeCookingStoveStatusView(Context context) {
        super(context);
        init(context, null);
    }

    public RecipeCookingStoveStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecipeCookingStoveStatusView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_recipe_cooking_device_status_stove,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
    }

    public void setLevel(short level) {

        boolean isRoki = Utils.getDefaultFan() != null;

        LogUtils.i("20171130","isRoki:"+ isRoki);

        if (isRoki){
            if (Utils.getDefaultStove()==null) return;
       /* if (Utils.getDefaultStove().getStoveModel().equals(IRokiFamily.R9B39)||Utils.getDefaultStove().getStoveModel().equals(IRokiFamily.R9B39)){
            {
                if (level==0||level==1){
                    level=1;
                }else if (level==2||level==3){
                    level=2;
                }else if (level==4||level==5){
                    level=3;
                }else if (level==6){
                    level=4;
                }else if (level==7||level==8||level==9){
                    level=5;
                }
            }
        }*/
        }
        String str = String.format("P%s", level);
        if (!isRoki) {
            if (level == Stove.PowerLevel_0)
                str = "关";
            else if (level <= Stove.PowerLevel_3)
                str = "小火";
            else if (level <= Stove.PowerLevel_6)
                str = "中火";
            else str = "大火";
        }
        txtValue.setText(str);
    }

}
