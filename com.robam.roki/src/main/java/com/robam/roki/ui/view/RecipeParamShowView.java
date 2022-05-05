package com.robam.roki.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legent.dao.DaoHelper;
import com.legent.plat.pojos.device.IDevice;
import com.legent.utils.LogUtils;
import com.robam.common.io.device.MsgParams;
import com.robam.common.paramCode;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.device.Oven.OvenMode;
import com.robam.roki.R;
import com.robam.roki.ui.RecipeUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class RecipeParamShowView extends FrameLayout {

    @InjectView(R.id.mode)
    TextView mode;
    @InjectView(R.id.mode_show)
    TextView modeShow;
    @InjectView(R.id.tempture)
    TextView tempture;
    @InjectView(R.id.tempture_show)
    TextView temptureShow;
    @InjectView(R.id.time)
    TextView time;
    @InjectView(R.id.time_show)
    TextView timeShow;
    @InjectView(R.id.power)
    TextView power;
    @InjectView(R.id.power_show)
    TextView powerShow;
    @InjectView(R.id.param_one)
    LinearLayout paramOne;
    @InjectView(R.id.param_two)
    LinearLayout paramTwo;
    @InjectView(R.id.param_three)
    LinearLayout paramThree;
    @InjectView(R.id.param_four)
    LinearLayout paramFour;

    ArrayList<CookStep> cookSteps;
    CookStep cookStep;
    Context cx;
    IDevice iDevice;
    public boolean isSlice;

    public RecipeParamShowView(Context context, ArrayList<CookStep> cookSteps, IDevice iDevice) {
        super(context);
        this.cookSteps = cookSteps;
        this.cx = context;
        this.iDevice = iDevice;
        init();
    }

    public void init() {
        View view = LayoutInflater.from(cx).inflate(R.layout.recipe_param_show_view, this, true);
        ButterKnife.inject(this, view);
        initView();
    }

    private void initView() {
        getDeviceInfoFromHttp(iDevice, 0);
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    public void onfresh(int step) {
        getDeviceInfoFromHttp(iDevice, step);
    }

    public void setiDevice(IDevice iDevice) {
        this.iDevice = iDevice;
    }


    public Map<String, paramCode> paramMap;//参数map

    String dp;
    String deviceCategory;

    private void getDeviceInfoFromHttp(IDevice iDevice, int step) {
        try {
            cookStep = DaoHelper.getDao(CookStep.class).queryForId(cookSteps.get(step).getID());
            if (cookStep != null && cookStep.getjs_PlatformCodes().size() == 0) {
                setParam(null, iDevice.getDc());
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (iDevice != null) {
            dp = iDevice.getDp();
            paramMap = RecipeUtil.getRecipeStepParamByDP(cookStep, dp);

            LogUtils.i("202070106", "11111:::" + paramMap.toString());
            LogUtils.i("202070106", "dp:::" + dp);
            setParam(paramMap, iDevice.getDc());
        } else {
            dp = cookSteps.get(step).getjs_PlatformCodes().get(0).platCode;
            deviceCategory = cookSteps.get(step).getjs_PlatformCodes().get(0).deviceCategory;
            paramMap = RecipeUtil.getRecipeStepParamByDP(cookStep, dp);
            LogUtils.i("202070106", "22222:::" + paramMap.toString());
            setParam(paramMap, deviceCategory);
        }
    }

    private void setParam(Map<String, paramCode> map, String dc) {
        switch (dc) {
            case DeviceType.RDKX://烤箱
                setRDKXParam(map);
                break;
            case DeviceType.RZQL://蒸汽炉
                setRZQLParam(map);
                break;
            case DeviceType.RZKY://一体机
                setRZKYParam(map);
                break;
            case DeviceType.RWBL://微波炉
                setRWBLParam(map);
                break;
            case DeviceType.RRQZ://燃气灶
                setRRQZParam(map);
                break;
            case DeviceType.RYYJ:
                setRRQZParam(map);
                break;
            default:
                break;
        }
    }

    private void setRRQZParam(Map<String, paramCode> map) {
        if (map == null) {
            paramFour.setVisibility(View.GONE);
            paramOne.setVisibility(View.GONE);
            paramTwo.setVisibility(View.GONE);
            paramThree.setVisibility(View.GONE);

        } else {
            paramFour.setVisibility(View.GONE);
            paramOne.setVisibility(View.VISIBLE);
            paramTwo.setVisibility(View.VISIBLE);
            paramThree.setVisibility(View.VISIBLE);
            //风量
            paramCode paramMode = map.get("fanGear");
            if (paramMode == null) {
                return;
            }
            mode.setText(paramMode.value + "");
            modeShow.setText("风量");
            //火力
            paramCode paramTemp = map.get("stoveGear");
            tempture.setText("P" + paramTemp.value);
            temptureShow.setText("火力");

            //时间
            paramCode needTime = map.get("needTime");
            time.setText(needTime.value < 60 ? (needTime.value + "S") :( (needTime.value / 60) + "min") );
            timeShow.setText("时间");
        }

    }

    private void setRDKXParam(Map<String, paramCode> map) {//还有EXP模式没考虑
        if (map == null) {
            paramFour.setVisibility(View.GONE);
            paramOne.setVisibility(View.GONE);
            paramTwo.setVisibility(View.GONE);
            paramThree.setVisibility(View.GONE);
            return;
        }
        paramCode paramMode = map.get("OvenMode");
        if (paramMode == null) return;
        if (paramMode != null && paramMode.value == 9) {
            paramFour.setVisibility(View.VISIBLE);
            paramOne.setVisibility(View.VISIBLE);
            paramTwo.setVisibility(View.VISIBLE);
            paramThree.setVisibility(View.VISIBLE);
            //模式
            mode.setText(paramMode.valueName);
            //上管温度
            paramCode paramTempUp = map.get("OvenTempUp");
            tempture.setText(paramTempUp.value + "℃");
            temptureShow.setText(paramTempUp.codeName);
            //下管温度
            paramCode paramTempBelow = map.get("OvenTempBelow");
            time.setText(paramTempBelow.value + "℃");
            timeShow.setText(paramTempBelow.codeName);
            //时间
            paramCode paramTime = map.get("OvenTime");
            power.setText((paramTime.value / 60) + "min");
            powerShow.setText(paramTime.codeName);
        } else {
            paramFour.setVisibility(View.GONE);
            paramOne.setVisibility(View.VISIBLE);
            paramTwo.setVisibility(View.VISIBLE);
            paramThree.setVisibility(View.VISIBLE);
            //模式
            mode.setText(paramMode.valueName);
            LogUtils.i("20200814", "paramMode.value:" + paramMode.value);

            if (OvenMode.SCFJ_KR == paramMode.value || OvenMode.SCFJ_FSK == paramMode.value ||
                    OvenMode.SCFJ_FBK == paramMode.value || OvenMode.SCFJ_SK == paramMode.value) {

                //上层分区温度
                paramCode paramTemp = map.get("OvenUpLayerTemp");
                if (paramTemp != null) {
                    tempture.setText(paramTemp.value + "℃");
                    temptureShow.setText(paramTemp.codeName);
                }
                //上层分区时间
                paramCode paramTime = map.get("OvenUpLayerTime");
                if (paramTime != null) {
                    time.setText((paramTime.value / 60) + "min");
                    timeShow.setText(paramTime.codeName);
                }

            } else if (OvenMode.XCFJ_FBK == paramMode.value || OvenMode.XCFJ_JK == paramMode.value) {
                //下层分区温度
                paramCode paramTemp = map.get("OvenDownLayerTemp");
                if (paramTemp != null) {
                    tempture.setText(paramTemp.value + "℃");
                    temptureShow.setText(paramTemp.codeName);
                }
                //下层分区时间
                paramCode paramTime = map.get("OvenDownLayerTime");
                if (paramTime != null) {
                    time.setText((paramTime.value / 60) + "min");
                    timeShow.setText(paramTime.codeName);
                }
            } else {
                //温度
                paramCode paramTemp = map.get("OvenTemp");
                if (paramTemp != null) {
                    tempture.setText(paramTemp.value + "℃");
                    temptureShow.setText(paramTemp.codeName);
                }
                //时间
                paramCode paramTime = map.get("OvenTime");
                if (paramTime != null) {
                    time.setText((paramTime.value / 60) + "min");
                    timeShow.setText(paramTime.codeName);
                }
            }
        }
    }

    private void setRZQLParam(Map<String, paramCode> map) {

        if (map == null) {
            paramFour.setVisibility(View.GONE);
            paramOne.setVisibility(View.GONE);
            paramTwo.setVisibility(View.GONE);
            paramThree.setVisibility(View.GONE);
            return;
        }

        paramOne.setVisibility(View.GONE);
        paramFour.setVisibility(View.GONE);
        paramTwo.setVisibility(View.VISIBLE);
        paramThree.setVisibility(View.VISIBLE);
        //温度
        paramCode paramTemp = map.get("SteamTemp");
        if(paramTemp != null){
            tempture.setText(paramTemp.value + "℃");
        }

        //时间
        paramCode paramTime = map.get("SteamTime");
        if(paramTime != null){
            time.setText((paramTime.value / 60) + "min");
        }
    }

    private void setRZKYParam(Map<String, paramCode> map) {

        if (map == null) {
            paramFour.setVisibility(View.GONE);
            paramOne.setVisibility(View.GONE);
            paramTwo.setVisibility(View.GONE);
            paramThree.setVisibility(View.GONE);
            return;
        }

        paramCode paramMode = map.get("OvenSteamMode");
        if (paramMode != null && "EXP".equals(paramMode.valueName)) {

            paramFour.setVisibility(View.VISIBLE);
            paramOne.setVisibility(View.VISIBLE);
            paramTwo.setVisibility(View.VISIBLE);
            paramThree.setVisibility(View.VISIBLE);
            //模式
            mode.setText(paramMode.valueName);
            //上管温度
            paramCode paramTempUp = map.get("OvenSteamUp");
            tempture.setText(paramTempUp.value + "℃");
            temptureShow.setText(paramTempUp.codeName);
            //下管温度
            paramCode paramTempBelow = map.get("OvenSteamBelow");
            time.setText(paramTempBelow.value + "℃");
            timeShow.setText(paramTempBelow.codeName);
            //时间
            paramCode paramTime = map.get("OvenSteamTime");
            power.setText((paramTime.value / 60) + "min");
            powerShow.setText(paramTime.codeName);
        } else {
            paramFour.setVisibility(View.GONE);
            paramOne.setVisibility(View.VISIBLE);
            paramTwo.setVisibility(View.VISIBLE);
            paramThree.setVisibility(View.VISIBLE);
            if (paramMode != null) {
                //模式
                mode.setText(paramMode.valueName);
                //温度
                paramCode paramTemp = map.get("OvenSteamTemp");
                tempture.setText(paramTemp.value + "℃");
                //时间
                paramCode paramTime = map.get("OvenSteamTime");
                time.setText((paramTime.value / 60) + "min");
            }
        }
    }

    private void setRWBLParam(Map<String, paramCode> map) {
        if (map == null) {
            paramFour.setVisibility(View.GONE);
            paramOne.setVisibility(View.GONE);
            paramTwo.setVisibility(View.GONE);
            paramThree.setVisibility(View.GONE);
            return;
        }

        paramOne.setVisibility(View.VISIBLE);
        paramTwo.setVisibility(View.VISIBLE);
        paramThree.setVisibility(View.VISIBLE);
        paramFour.setVisibility(View.VISIBLE);
        //模式
        paramCode paramMode = map.get(MsgParams.MicroWaveMode);
        mode.setText(paramMode.valueName);
        modeShow.setText(paramMode.codeName);

        paramCode paramTime = map.get(MsgParams.MicroWaveTime);
        tempture.setText((paramTime.value / 60) + "min");
        temptureShow.setText(paramTime.codeName);

        paramCode paramTemp = map.get(MsgParams.MicroWaveWeight);
        time.setText(paramTemp.value + "");
        timeShow.setText(paramTemp.codeName);

        paramCode paramPower = map.get(MsgParams.MicroWavePower);
        power.setText(paramPower.valueName);
        powerShow.setText(paramPower.codeName);

    }
}
