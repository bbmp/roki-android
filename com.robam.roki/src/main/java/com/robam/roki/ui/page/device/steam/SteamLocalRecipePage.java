package com.robam.roki.ui.page.device.steam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamModeName;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter.CommonItemDecoration;
import com.robam.roki.ui.adapter.LocalRecipeAdapter;
import com.robam.roki.ui.dialog.AbsSettingDialog;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TestDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robam.common.util.FanLockUtils.mGuid;

/**
 * sq235本地自动菜谱
 */
public class SteamLocalRecipePage extends BasePage {
    public AbsSteamoven steam;
    public String guid;
    @InjectView(R.id.rv_local_recipe)
    RecyclerView rvLocalRecipe;
    @InjectView(R.id.mode_back)
    ImageView modeBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    List<DeviceConfigurationFunctions> mDatas = new ArrayList<>();
    private List<DeviceConfigurationFunctions> functions;
    private List<DeviceConfigurationFunctions> cookBookTops;
    private String mode;
    private String needDescalingParams;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        steam = Plat.deviceService.lookupChild(mGuid);
        needDescalingParams = bd == null ? null : bd.getString(PageArgumentKey.descaling);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
        View view = inflater.inflate(R.layout.abs_steam_local_recipe_layout, container, false);
        ButterKnife.inject(this, view);
        initView();
        initData();
        return view;
    }

    private void initData() {
        List<DeviceConfigurationFunctions> deviceConfigurationFunctions = null;
        for (int i = 0; i < mDatas.size(); i++) {
            String functionCode = mDatas.get(i).functionCode;
            //本地菜谱
            if (functionCode.equals(SteamModeName.localCookbook)) {
                deviceConfigurationFunctions = mDatas.get(i)
                        .subView
                        .subViewModelMap
                        .subViewModelMapSubView
                        .deviceConfigurationFunctions;
            }
        }
        if (functions != null) {
            functions.clear();
        }
        if (cookBookTops != null) {
            cookBookTops.clear();
        }
        functions = new ArrayList<>();
        cookBookTops = new ArrayList<>();
        for (int i = 0; i < deviceConfigurationFunctions.size(); i++) {
            if (!"cookBookTop".equals(deviceConfigurationFunctions.get(i).functionCode)) {
                functions.add(deviceConfigurationFunctions.get(i));
            } else {
                cookBookTops.add(deviceConfigurationFunctions.get(i));
            }
        }

        LocalRecipeAdapter localBookAdapter = new LocalRecipeAdapter(getContext(), functions);
        rvLocalRecipe.setAdapter(localBookAdapter);
        rvLocalRecipe.setHasFixedSize(true);
        //创建StaggeredGridLayoutManager实例
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // 绑定布局管理器
        rvLocalRecipe.addItemDecoration(new CommonItemDecoration(20, 20));
        rvLocalRecipe.setLayoutManager(layoutManager);
        rvLocalRecipe.setItemAnimator(null);

        localBookAdapter.setOnItemRecyclerClick(new LocalRecipeAdapter.OnItemRecyclerClick() {
            @Override
            public void onItemClick(View v, int position) {
                if (steam.waterboxstate == 0) {
                    ToastUtils.show(R.string.device_alarm_water_out, Toast.LENGTH_SHORT);
                    return;
                }
                String functionParams = functions.get(position).functionParams;
                LogUtils.i("202011111338","functionParams::::"+functionParams);
                try {
                    JSONObject jsonObject = new JSONObject(functionParams);
                    mode = jsonObject.optJSONObject("model").optString("value");
                    String setTimeDef = jsonObject.optJSONObject("setTimeDef").optString("value");
                    JSONArray setTimeArray = jsonObject.optJSONObject("setTime").optJSONArray("value");

                    List<Integer> times = new ArrayList<>();
                    for (int i = 0; i < setTimeArray.length(); i++) {
                        Integer time = (Integer) setTimeArray.get(i);
                        times.add(time);
                    }
                    final List<Integer> modeDataTime = TestDatas.createModeDataTime(times);
                    if ("".equals(mode) || !isNumeric(mode)) {
                        return;
                    }
                    if (steam.descaleModeStageValue != 0||steam.WeatherDescalingValue == 1) {
                        descalingDialog();
                        return;
                    }
                    setDurationTime(modeDataTime, Integer.parseInt(setTimeDef), "分钟");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
        tvTitle.setText(title);
    }


    @OnClick(R.id.mode_back)
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.mode_back:
                UIService.getInstance().popBack();
                break;
        }
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    AbsSettingDialog absSettingDialog;

    private void setDurationTime(final List<Integer> listTempture, int num, String str) {
        LogUtils.i("201912061", listTempture.toString());
        List<String> listButton = TestDatas.createButtonText(str, "取消", "开始工作", null);
        absSettingDialog = new AbsSettingDialog<Integer>(getContext(), listTempture, listButton, num);
        absSettingDialog.show(absSettingDialog);
        absSettingDialog.setListener(new AbsSettingDialog.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(Object index) {
                LogUtils.i("202011051456","index:::"+index);
                LogUtils.i("202011051456","hahaha:::");
                send1((int) index);

            }
        });
    }


    public void send1(final int setTime) {
        if (steam.doorState == 0) {
            ToastUtils.show("门未关好，请先关好箱门", Toast.LENGTH_SHORT);
            return;
        }
        if (steam.waterboxstate == 0) {
            ToastUtils.show("水箱已弹出，请确保水箱已放好", Toast.LENGTH_SHORT);
            return;
        }

        if (steam.status == SteamStatus.AlarmStatus) {
            ToastUtils.show("请先解除报警", Toast.LENGTH_SHORT);
            return;
        }
        if (steam.descaleModeStageValue != 0) {
            descalingDialog();
            return;
        }
        if (steam.status == SteamStatus.Off || steam.status == SteamStatus.Wait) {
            steam.setSteamStatus(SteamStatus.On, new VoidCallback() {
                @Override
                public void onSuccess() {
                    LogUtils.i("202011051129", "setTime111:::" + setTime);
                    if (setTime > 255) {
                        int i = setTime & 0xff;//低8位
                        int i1 = (setTime & 0xff00) >> 8;//高8位
                        setAutoMode(i, i1);
                    } else {
                        setAutoMode(setTime);

                    }


                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        } else {

            if (setTime > 255) {
                int i = setTime & 0xff;//低8位
                int i1 = (setTime & 0xff00) >> 8;//高8位
                setAutoMode(i, i1);
            } else {
                setAutoMode(setTime);

            }


        }
    }

    private void setAutoMode(int setTime) {
        steam.setSteamAutoMode(Short.decode(mode), (short) setTime, (short) 0, new VoidCallback() {
            @Override
            public void onSuccess() {
                UIService.getInstance().popBack();
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("202007070123", t.getMessage());
            }
        });


    }

    private void setAutoMode(int setTime, int setTime2) {

        steam.setSteamAutoMode(Short.decode(mode), (short) setTime, (short) setTime2, (short)1, new VoidCallback() {
            @Override
            public void onSuccess() {
                LogUtils.i("202011051129","success");
                UIService.getInstance().popBack();
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("202007070123", t.getMessage());
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (absSettingDialog != null && absSettingDialog.isShowing()) {
            absSettingDialog.dismiss();
        }
    }

    private void descalingDialog() {
        String descalingTitle = null;
        String descalingContent = null;
        String descalingButton = null;
        try {
            if (!"".equals(needDescalingParams)) {
                JSONObject jsonObject = new JSONObject(needDescalingParams);
                JSONObject needDescaling = jsonObject.getJSONObject("needDescaling");
                descalingTitle = needDescaling.getString("title");
                descalingContent = needDescaling.getString("content");
                descalingButton = needDescaling.getString("positiveButton");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_02);
        dialogByType.setTitleText(descalingTitle);
        dialogByType.setContentText(descalingContent);
        dialogByType.setOkBtn(descalingButton, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogByType != null) {
                    dialogByType.dismiss();
                }
            }
        });
        dialogByType.show();
    }
}
