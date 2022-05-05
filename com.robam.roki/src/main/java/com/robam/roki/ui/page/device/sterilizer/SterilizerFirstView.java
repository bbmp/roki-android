package com.robam.roki.ui.page.device.sterilizer;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Sterilizer.AbsSterilizer;
import com.robam.common.pojos.device.Sterilizer.Steri826;
import com.robam.common.pojos.device.Sterilizer.Steri829;
import com.robam.roki.R;
import com.robam.roki.ui.adapter.OvenCommonAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Dell on 2018/10/18.
 */

public class SterilizerFirstView extends FrameLayout {
    List<DeviceConfigurationFunctions> mainList;
    List<DeviceConfigurationFunctions> otherList;
    List<DeviceConfigurationFunctions> bgList;
    Context cx;

    @InjectView(R.id.oven_offline_txt)
    TextView ovenOfflineTxt;
    @InjectView(R.id.oven_func)
    GridView ovenFunc;
    @InjectView(R.id.oven_func_show)
    RecyclerView ovenFuncShow;

    @InjectView(R.id.param_show)
    SterilizerParamShowView paramShow;

    OvenCommonAdapter ovenCommonAdapter;
    private String temperature;
    private String humidity;
    private String ozone;
    private String bacteria;

    private String deviceType;

    public SterilizerFirstView(Context context, List<DeviceConfigurationFunctions> mainList, List<DeviceConfigurationFunctions> otherList
            , List<DeviceConfigurationFunctions> bgList, String deviceType) {
        super(context);
        this.cx = context;
        this.mainList = mainList;
        this.otherList = otherList;
        this.bgList = bgList;
        this.deviceType = deviceType;
        initView();
    }

    public SterilizerFirstView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SterilizerFirstView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(cx).inflate(R.layout.sterilizer_first_view, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        ovenCommonAdapter = new OvenCommonAdapter(cx, mainList, otherList, deviceType);
        ovenFuncShow.setAdapter(ovenCommonAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(cx, 1);
        ovenFuncShow.setLayoutManager(layoutManager);
        ovenCommonAdapter.setGridViewOnclickLister(new OvenCommonAdapter.GridViewOnclick() {
            @Override
            public void onGridClick(String pos) {
                if (onclickMainLister != null) {
                    onclickMainLister.onclickMain(pos);
                }
            }
        });
        ovenCommonAdapter.setItemViewOnclickLister(new OvenCommonAdapter.ItemViewOnclick() {

            @Override
            public void onItemClick(String pos) {
                if (onclickMainLister != null) {
                    onclickMainLister.onclickOther(pos);
                }
            }
        });


        for (int i = 0; i < bgList.size(); i++) {
            if (bgList.get(i).functionCode.equals("temperature")) {
                temperature = bgList.get(i).functionName;
            }
            if (bgList.get(i).functionCode.equals("humidity")) {
                humidity = bgList.get(i).functionName;
            }
            if (bgList.get(i).functionCode.equals("ozone")) {
                ozone = bgList.get(i).functionName;
            }
            if (bgList.get(i).functionCode.equals("bacteria")) {
                bacteria = bgList.get(i).functionName;
            }
        }
        if (temperature != null || humidity != null || ozone != null || bacteria != null) {

            paramShow.upDataViewName(temperature, humidity, ozone, bacteria);
        }

    }

    public void setUpData(List<DeviceConfigurationFunctions> moreList) {
        ovenCommonAdapter.upMoreView(moreList);
    }

    public void removeMoreView() {
        ovenCommonAdapter.removeMoreView();
    }


    protected void disConnect(boolean isCon) {
        if (isCon) {
            paramShow.upDataView("--", "--", "--", "--");
            ovenOfflineTxt.setVisibility(View.VISIBLE);
            ovenCommonAdapter.setDisCon(true);
        } else {

            ovenOfflineTxt.setVisibility(View.INVISIBLE);
            ovenCommonAdapter.setDisCon(false);
        }
    }

    protected void upDataView(AbsSterilizer absSterilizer) {
        if (IRokiFamily.RR829.equals(absSterilizer.getDt())) {
            paramShow.upDataView(((Steri829) absSterilizer).temp,
                                                        ((Steri829) absSterilizer).hum,
                                                        ((Steri829) absSterilizer).ozone,
                                                        ((Steri829) absSterilizer).germ);
        } else {
            paramShow.upDataView(((Steri826) absSterilizer).temp,
                                                        ((Steri826) absSterilizer).hum,
                                                        ((Steri826) absSterilizer).ozone,
                                                        ((Steri826) absSterilizer).germ);


        }
    }

    protected interface OnClickMian {

        void onclickMain(String str);

        void onclickOther(String str);
    }

    protected OnClickMian onclickMainLister;

    protected void setOnclickMainLister(OnClickMian onclickMainLister) {
        this.onclickMainLister = onclickMainLister;
    }

}
