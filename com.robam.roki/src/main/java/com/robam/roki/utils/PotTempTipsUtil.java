package com.robam.roki.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.legent.utils.JsonUtils;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.roki.R;
import com.robam.roki.model.bean.PotOilTempParams;

public class PotTempTipsUtil {
    public static void updateState(ImageView imgState, TextView tvTempShow, TextView tvOperateTips, String functionParams, Pot pot, Context cx) {

        try {
            PotOilTempParams.FunctionParamsBean functionParamsBean = JsonUtils.json2Pojo(functionParams, PotOilTempParams.FunctionParamsBean.class);

            if (pot.tempUp >= functionParamsBean.tips.get(0).min && pot.tempUp < functionParamsBean.tips.get(0).max) {
                Glide.with(cx).asGif().load(R.mipmap.pot_state_common).into(imgState);
                tvTempShow.setText(pot.tempUp + "°");
                tvOperateTips.setText(functionParamsBean.tips.get(0).tips);
            } else if (pot.tempUp >= functionParamsBean.tips.get(1).min && pot.tempUp < functionParamsBean.tips.get(1).max) {
                Glide.with(cx).asGif().load(R.mipmap.pot_state_common).into(imgState);
                tvTempShow.setText(pot.tempUp + "°");
                tvOperateTips.setText(functionParamsBean.tips.get(1).tips);
            } else if (pot.tempUp >= functionParamsBean.tips.get(2).min && pot.tempUp < functionParamsBean.tips.get(2).max) {
                Glide.with(cx).asGif().load(R.mipmap.pot_state_common).into(imgState);
                tvTempShow.setText(pot.tempUp + "°");
                tvOperateTips.setText(functionParamsBean.tips.get(2).tips);
            } else if (pot.tempUp >= functionParamsBean.tips.get(3).min && pot.tempUp < functionParamsBean.tips.get(3).max) {
                Glide.with(cx).asGif().load(R.mipmap.pot_state_high).into(imgState);
                tvTempShow.setText(pot.tempUp + "°");
                tvOperateTips.setText(functionParamsBean.tips.get(3).tips);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
