package com.robam.roki.ui.page.device.steamovenone;

import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.stmt.query.In;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.DiyCookbookList;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.DeviceOvenDiyParams;
import com.robam.roki.model.bean.OvenExpParamBean;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.dialog.AbsOvenExpDialog;
import com.robam.roki.ui.dialog.AbsSettingDialog;
import com.robam.roki.ui.dialog.AbsSteamOvenDiyDialog;
import com.robam.roki.ui.dialog.AbsSteamOvenTempTimeDialog;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TestDatas;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 蒸烤一体机c915
 * 烹饪diy编辑页
 */
public class SteamOvenEditPage extends BasePage {
    @InjectView(R.id.mode_back)
    ImageView ModeBack;
    @InjectView(R.id.tv_title_name)
    TextView tvTitleName;

    @InjectView(R.id.iv_delete)
    ImageView ivDelete;
    @InjectView(R.id.et_recipe_name)
    EditText etRecipeName;
    @InjectView(R.id.et_recipe_content)
    EditText etRecipeContent;
    @InjectView(R.id.rl_par_set)
    RelativeLayout rlParSet;
    @InjectView(R.id.temp2_layout)
    RelativeLayout temp2Layout;

    @InjectView(R.id.tv_mode_name)
    TextView tvModeName;
    @InjectView(R.id.tv_temp)
    TextView tvTemp;
    @InjectView(R.id.tv_temp2)
    TextView tvTemp2;
    @InjectView(R.id.tv_min)
    TextView tvMin;

    @InjectView(R.id.tv_temp_unit)
    TextView tvTempUnit;
    @InjectView(R.id.tv_min_unit)
    TextView tvMinUnit;

    String mGuid;
    AbsSteameOvenOne absSteameOvenOne;
    private String dt;
    private long userId;
    List<DeviceOvenDiyParams> deviceList;

    AbsSteamOvenDiyDialog absSteameOvenDialog;
    AbsSteamOvenDiyDialog diyTypeModeDialog;
    AbsSteamOvenDiyDialog diyTypeTimeTempDialog;
    private int id;
    boolean tag = false;
    private IRokiDialog dialogByType;
    String mTitle;
    String diyType;
    DiyCookbookList diyCookbookList;
    boolean showDialog;
    boolean showBackTwo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_steam_oven_diy_edit, container, false);
        ButterKnife.inject(this, view);
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        deviceList = bd == null ? null : (List<DeviceOvenDiyParams>) bd.getSerializable(PageArgumentKey.List);
        diyCookbookList = bd == null ? null : (DiyCookbookList) bd.getSerializable(PageArgumentKey.Bean);
        mTitle = bd == null ? null : bd.getString(PageArgumentKey.title);
        showDialog = !TextUtils.equals(mTitle, "新增菜谱");
        showBackTwo = TextUtils.equals(mTitle, "编辑菜谱");
        diyType = bd == null ? null : bd.getString(PageArgumentKey.diyType);
        absSteameOvenOne = Plat.deviceService.lookupChild(mGuid);
        userId = Plat.accountService.getCurrentUserId();
        //限制字数250字
        etRecipeContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(250)});
        initData();
        return view;
    }

    private void initData() {

        tvTitleName.setText(mTitle);
        if (absSteameOvenOne != null) {
            dt = absSteameOvenOne.getDt();
        }

        if (diyCookbookList != null) {
            tag = true;
            id = diyCookbookList.id;
            String name = diyCookbookList.name;
            String cookbookDesc = diyCookbookList.cookbookDesc;
            String modeCode = diyCookbookList.modeCode;
            String temp = diyCookbookList.temp;
            if(temp.contains("℃")){
                temp=temp.substring(0,temp.indexOf("℃"));
            }
            String minute = diyCookbookList.minute;
            String downTemp = diyCookbookList.tempDown;
            etRecipeName.setText(name);
            etRecipeContent.setText(cookbookDesc);
            tvModeName.setText(code2Mode(modeCode));

            tvMin.setText(minute);
            if (!TextUtils.isEmpty(downTemp)) {
                tvTemp2.setText("下：" + downTemp);
                tvTemp.setText("上：" + temp +"℃ 下：" + downTemp);
            } else {
                tvTemp.setText(temp);
                temp2Layout.setVisibility(View.GONE);
            }
        }

    }

    private String code2Mode(String code) {
        String modeName = null;
        for (int i = 0; i < deviceList.size(); i++) {
            if (String.valueOf(deviceList.get(i).getCode()).equals(code)) {
                modeName = deviceList.get(i).getValue();
            }
        }
        return modeName;
    }

    private int ModeName2Code(String modeName) {
        int code = 0;
        for (int i = 0; i < deviceList.size(); i++) {
            if (deviceList.get(i).getValue().equals(modeName)) {
                code = deviceList.get(i).getCode();
            }
        }
        return code;
    }


    private void setDialogParam() {
        if (deviceList == null) {
            return;
        }
        List<String> listButton;
        if (TextUtils.equals(diyType, "1")) {
            listButton = TestDatas.createButtonText("", "取消", "确定", null);
            final AbsSettingDialog absSettingDialog = new AbsSettingDialog<DeviceOvenDiyParams>(getContext(), deviceList, listButton, 0);
            absSettingDialog.show(absSettingDialog);
            absSettingDialog.setListener(new AbsSettingDialog.PickListener() {
                @Override
                public void onCancel() {

                }

                @Override
                public void onConfirm(Object index) {

                    if (deviceList.get((Integer) index).getDownTemp() != null) {
                        setEXPDialogParam((Integer) index);
                        return;
                    }
                    List<String> listButton1 = TestDatas.createDialogText("", "℃", "分钟", "取消", "开始工作");
                    AbsSteamOvenTempTimeDialog absSteamOvenTempTimeDialog = new AbsSteamOvenTempTimeDialog(cx, deviceList, listButton1, (Integer) index);
                    absSteamOvenTempTimeDialog.showDiyDialog(absSteamOvenTempTimeDialog);
                    absSteamOvenTempTimeDialog.setListener(new AbsSteamOvenTempTimeDialog.PickListener() {
                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onConfirm(DeviceOvenDiyParams deviceOvenDiyParams, Integer integer2, Integer integer, Integer integer1) {
                            tvModeName.setText(deviceOvenDiyParams.getValue());
                            tvTemp.setText(String.valueOf(integer));
                            if (integer2 != -1) {
                                tvTemp2.setText("下：" + integer2);
                                tvTemp.setText("上：" + integer +"℃ 下：" + integer2);
                            } else {
                                temp2Layout.setVisibility(View.GONE);
                                tvTemp.setText(String.valueOf(integer));
                            }
                            tvMin.setText(String.valueOf(integer1));
                        }
                    });
                }
            });
            return;
        }
        listButton = TestDatas.createDialogText("", "℃", "分钟", "取消", "确定");
        absSteameOvenDialog = new AbsSteamOvenDiyDialog(cx, deviceList, listButton);
        LogUtils.i("20200922", "deviceList::" + deviceList);
        absSteameOvenDialog.setListener(new AbsSteamOvenDiyDialog.PickListener() {
            @Override
            public void onCancel() {
                absSteameOvenDialog.dismiss();
            }

            @Override
            public void onConfirm(DeviceOvenDiyParams deviceOvenDiyParams, Integer integer1, Integer integer2, Integer integer3) {
                absSteameOvenDialog.dismiss();
                tvModeName.setText(deviceOvenDiyParams.getValue());
                if (integer3 != -1) {
                    tvTemp2.setText("下：" + integer3);
                    tvTemp.setText("上：" + integer1+"℃ 下：" + integer2);
                } else {
                    temp2Layout.setVisibility(View.GONE);
                    tvTemp.setText(String.valueOf(integer1));
                }
                tvMin.setText(String.valueOf(integer2));
            }
        });
        absSteameOvenDialog.showDiyDialog(absSteameOvenDialog);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);

        if (absSteameOvenDialog != null) {
            absSteameOvenDialog.dismiss();
        }
        if (dialogByType != null) {
            dialogByType.dismiss();
        }
    }


    @OnClick({R.id.btn_save_recipe, R.id.mode_back, R.id.iv_delete, R.id.rl_par_set})
    public void OnClick(View view) {
        switch (view.getId()) {
            //保存菜谱
            case R.id.btn_save_recipe:
                String recipeName = etRecipeName.getText().toString().trim();//菜谱名称
                String recipeDesc = etRecipeContent.getText().toString().trim();//描述
                String modeName = tvModeName.getText().toString().trim();//模式
                String temp = tvTemp.getText().toString().trim();//温度\
                if (temp.startsWith("上")) {
                    temp = temp.substring(2);
                    if(temp.contains("下")){
                        temp = temp.substring(0, temp.indexOf("℃ "));
                    }
                }
                String temp2 = "";
                if (TextUtils.equals("EXP", modeName)) {
                    temp2 = tvTemp2.getText().toString().trim();//温度
                    if (temp2.startsWith("下")) {
                        temp2 = temp2.substring(2);
                    }
                }
                String time = tvMin.getText().toString().trim(); //时间

                if ("".equals(recipeName)) {
                    ToastUtils.show("请填写菜谱名称", Toast.LENGTH_SHORT);
                    return;
                }
                if ("--".equals(modeName) || "--".equals(temp) || "--".equals(time)) {
                    ToastUtils.show("请设置参数信息", Toast.LENGTH_LONG);
                    return;
                }
                saveOrEditRecipe(userId, dt, recipeName, String.valueOf(ModeName2Code(modeName)), temp, time, recipeDesc, id + "", temp2);

                break;
            case R.id.mode_back:
                exitDialog();
                break;
            //删除
            case R.id.iv_delete:
                deleteRecipe(userId);
                break;
            case R.id.rl_par_set:
                setDialogParam();
                break;


        }
    }


    //编辑菜谱
    private void saveOrEditRecipe(long userId, String deviceType, String name, String modeCode, String temp,
                                  String min, String cookbookDesc, String id, String temp2) {
        if (!TextUtils.isEmpty(temp2)) {
            RokiRestHelper.updateCQ908Recipe(userId, deviceType, name, modeCode, temp, temp2, min, cookbookDesc, Long.parseLong(id),
                    new Callback<Reponses.Update035Recipe>() {
                        @Override
                        public void onSuccess(Reponses.Update035Recipe update035Recipe) {

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (tag) {
                                        SteamOvenDiyDetailPage.instance.close();
                                    }
                                    DiyCookbookListPage.instance.initFresh();

                                    UIService.getInstance().popBack();

                                }
                            }, 500);


                        }

                        @Override
                        public void onFailure(Throwable t) {
                            t.printStackTrace();

                        }
                    });
            return;
        }
        RokiRestHelper.update915Recipe(userId, deviceType, name, modeCode, temp, min, cookbookDesc, Long.parseLong(id),
                new Callback<Reponses.Update035Recipe>() {
                    @Override
                    public void onSuccess(Reponses.Update035Recipe update035Recipe) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (tag) {
                                    SteamOvenDiyDetailPage.instance.close();
                                }
                                DiyCookbookListPage.instance.initFresh();

                                UIService.getInstance().popBack();

                            }
                        }, 500);


                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();

                    }
                });


    }

    //删除菜谱
    private void deleteRecipe(Long userId) {
        RokiRestHelper.Delete035Recipe(userId, new Callback<Reponses.Update035Recipe>() {
            @Override
            public void onSuccess(Reponses.Update035Recipe update035Recipe) {
                UIService.getInstance().popBack();
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();

            }
        });

    }

    //退出时弹框
    private void exitDialog() {
        String recipeName = etRecipeName.getText().toString().trim();//菜谱名称
        String recipeDesc = etRecipeContent.getText().toString().trim();//描述
        String modeName = tvModeName.getText().toString().trim();//模式
        if (!TextUtils.isEmpty(recipeName) || !TextUtils.isEmpty(recipeDesc) || !TextUtils.equals(modeName, "--")) {
            showDialog = true;
        }
        if (!showDialog) {
            UIService.getInstance().popBack();
            return;
        }
        dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        dialogByType.setTitleText("退出编辑");
        dialogByType.setContentText("如果离开，此次编辑内容将会丢失，确认离开？");
        dialogByType.setOkBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogByType.dismiss();
            }
        });
        dialogByType.setCancelBtn("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showBackTwo) {
//                    UIService.getInstance().popBack().popBack();
                    UIService.getInstance().popBack();
                } else {
                    UIService.getInstance().popBack();
                }
                dialogByType.dismiss();
            }
        });
        dialogByType.show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            exitDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected AbsOvenExpDialog absOvenExpDialog;
    private void setEXPDialogParam(final int index) {
        DeviceOvenDiyParams ovenParams = deviceList.get(index);

        if (ovenParams != null) {
            List<Integer> tempUp = ovenParams.getUpTemp();
            List<Integer> tempDown = ovenParams.getDownTemp();
            List<Integer> time = ovenParams.getTimeList();


            String tempUpDefault = ovenParams.getUpTempDefault();

            String timeDefault = ovenParams.getMinuteDefault();

            String tempDiff = ovenParams.getTempDiff();
            String tempStart = ovenParams.getTempStart();
            String downMin = ovenParams.getTempMin();
            int deNum1 = Integer.parseInt(tempUpDefault) - tempUp.get(0);
            int deDiff = Integer.parseInt(tempDiff);
            int deNum2 = deNum1 - deDiff;
            int deNum3 = Integer.parseInt(timeDefault) - time.get(0);
            int deStart = Integer.parseInt(tempStart);
            int min = Integer.parseInt(downMin);


            List<String> listButton = TestDatas.createExpDialogText("℃", "℃", "分钟", "");
            absOvenExpDialog = new AbsOvenExpDialog(cx, tempUp, tempDown, time, listButton, deNum1, deNum2, deNum3, deDiff, deStart, min);
            absOvenExpDialog.showDialog(absOvenExpDialog);
            absOvenExpDialog.setListener(new AbsOvenExpDialog.PickListener() {
                @Override
                public void onCancel() {

                }

                @Override
                public void onConfirm(int index1, int index2, int index3) {
                    tvModeName.setText("EXP");
                    tvTemp.setText(String.valueOf(index1));
                    if (index2 != -1) {
                        tvTemp2.setText("下：" + index2);
                        tvTemp.setText("上：" + index1+"℃ 下：" + index2);
                    } else {
                        temp2Layout.setVisibility(View.GONE);
                        tvTemp.setText(String.valueOf(index1));
                    }
                    tvMin.setText(String.valueOf(index3));
                }
            });
        }
    }
}
