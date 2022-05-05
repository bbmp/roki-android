package com.robam.roki.ui.page.device.steam;

import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.DiyCookbookList;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.DeviceSteamDiyParams;
import com.robam.roki.ui.dialog.AbsSteamDIYDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.JsonUtils;
import com.robam.roki.utils.TestDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SteamDiyEditPage extends BasePage {
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

    @InjectView(R.id.tv_mode_name)
    TextView tvModeName;
    @InjectView(R.id.tv_temp)
    TextView tvTemp;
    @InjectView(R.id.tv_min)
    TextView tvMin;

    @InjectView(R.id.tv_temp_unit)
    TextView tvTempUnit;
    @InjectView(R.id.tv_min_unit)
    TextView tvMinUnit;
    String mGuid;

    DiyCookbookList diyCookbookList;
    List<DeviceConfigurationFunctions> mDatas;
    AbsSteamoven steam;
    private long userId;
    String dt;
    private List<Integer> keyList;
    List<DeviceSteamDiyParams> deviceList = new ArrayList<>();
    boolean tag = false;
    private int id;
    public AbsSteamDIYDialog absSteamDIYDialog;
    private IRokiDialog dialogByType;
    boolean showDialog;
    boolean showBackTwo;
    String mTitle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.abs_steam_edit, container, false);
        ButterKnife.inject(this, view);
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        diyCookbookList = bd == null ? null : (DiyCookbookList) bd.getSerializable(PageArgumentKey.Bean);
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        steam = Plat.deviceService.lookupChild(mGuid);
        userId = Plat.accountService.getCurrentUserId();
        //限制字数250字
        mTitle = bd == null ? null : bd.getString(PageArgumentKey.title);
        showDialog = !TextUtils.equals(mTitle, "新增菜谱");
        showBackTwo = TextUtils.equals(mTitle, "编辑菜谱");
        etRecipeContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(250)});
        selectCode();
        initData();
        return view;
    }

    private void selectCode() {
        for (int i = 0; i < mDatas.size(); i++) {
            if ("diy".equals(mDatas.get(i).functionCode)) {
                String functionParams = mDatas.get(i).functionParams;
                try {
                    JSONObject jsonObject = new JSONObject(functionParams);
                    JSONObject model = jsonObject.getJSONObject("model");
                    map = new HashMap<>();
                    Iterator<String> keys = model.keys();
                    while (keys.hasNext()) {
                        String next = keys.next();
                        String mode = model.getJSONObject(next).getString("value");
                        map.put(next, mode);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

    }

    private void initData() {
        if (keyList!=null) {
            keyList.clear();
        }
        if (deviceList!=null) {
            deviceList.clear();
        }
        if (steam != null) {
            dt = steam.getDt();
        }

        tvTitleName.setText(mTitle);
        if (mDatas == null) {
            return;
        }


        for (int i = 0; i < mDatas.size(); i++) {
            if ("diy".equals(mDatas.get(i).functionCode)) {
                String functionParams = mDatas.get(i).functionParams;

                try {

                    JSONObject jsonObject = new JSONObject(functionParams);
                    JSONObject model = jsonObject.getJSONObject("model");
                    String modelStr = model.toString();

                    Map<String, HashMap<String, DeviceSteamDiyParams>> json2Map = JsonUtils.getJson2Map(modelStr);

                    keyList = new ArrayList<>();

                    Set<String> strings = json2Map.keySet();
                    for (String str : strings) {
                        keyList.add(Integer.valueOf(str));
                    }
                    Collections.sort(keyList);

                    for (int j = 0; j < keyList.size(); j++) {
                        JSONObject obj = model.getJSONObject(keyList.get(j) + "");
                        String value = obj.getString("value");
                        String paramType = obj.getString("paramType");
                        String defaultTemp = obj.getString("defaultTemp");
                        String defaultMinute = obj.getString("defaultMinute");
                        JSONArray timeArray = obj.getJSONObject("time").getJSONArray("value");
                        JSONArray tempArray = obj.getJSONObject("temp").getJSONArray("value");
                        DeviceSteamDiyParams deviceOvenDiyParams = new DeviceSteamDiyParams();
                        deviceOvenDiyParams.setValue(value);
                        deviceOvenDiyParams.setParamType(paramType);
                        deviceOvenDiyParams.setDefaultTemp(defaultTemp);
                        deviceOvenDiyParams.setDefaultMinute(defaultMinute);
                        deviceOvenDiyParams.setTimeList(timeJson2List(timeArray));
                        deviceOvenDiyParams.setTempList(tempJson2List(tempArray));
                        deviceList.add(deviceOvenDiyParams);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }


        if (diyCookbookList != null) {
            tag = true;
            id = diyCookbookList.id;
            String name = diyCookbookList.name;
            String cookbookDesc = diyCookbookList.cookbookDesc;
            String modeCode = diyCookbookList.modeCode;
            String temp = diyCookbookList.temp;
            String minute = diyCookbookList.minute;
            etRecipeName.setText(name);
            etRecipeContent.setText(cookbookDesc);
            String modeName = null;
            for (Map.Entry<String, String> str : map.entrySet()) {
                if (str.getKey().equals(modeCode)) {
                    modeName = str.getValue();
                }
            }
            tvModeName.setText(modeName);
            tvTemp.setText(temp);
            tvTempUnit.setText("℃");
            tvMin.setText(minute);
            tvMinUnit.setText("分钟");

        }
    }

    int code;

    private void setDialogParam() {
        if (deviceList == null) {
            return;
        }
        List<String> listButton = TestDatas.createDialogText("", "℃", "分钟", "取消", "确定");
        absSteamDIYDialog = new AbsSteamDIYDialog(
                cx,
                deviceList,
                keyList,
                listButton,
                0,
                0,
                0
        );
        absSteamDIYDialog.showDiyDialog(absSteamDIYDialog);
        absSteamDIYDialog.setListener(
                new AbsSteamDIYDialog.PickListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onConfirm(DeviceSteamDiyParams deviceOvenDiyParams, Integer integer2, Integer integer, Integer integer1) {
                        tvModeName.setText(deviceOvenDiyParams.getValue());
                        code = integer2;
                        tvTemp.setText(integer + "");
                        tvTempUnit.setText("℃");
                        tvMin.setText(integer1 + "");
                        tvMinUnit.setText("分钟");
                    }


                }
        );

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);

        if (absSteamDIYDialog != null) {
            absSteamDIYDialog.dismiss();
        }
        if (dialogByType != null) {
            dialogByType.dismiss();
        }
    }
    private Map<String, String> map;
    @OnClick({R.id.btn_save_recipe, R.id.mode_back, R.id.iv_delete, R.id.rl_par_set})
    public void OnClick(View view) {
        switch (view.getId()) {
            //保存菜谱
            case R.id.btn_save_recipe:
                String recipeName = etRecipeName.getText().toString().trim();//菜谱名称
                String recipeContent = etRecipeContent.getText().toString().trim();//描述
                String modeName = tvModeName.getText().toString().trim();//模式
                String temp = tvTemp.getText().toString().trim();//温度
                String time = tvMin.getText().toString().trim(); //时间

                String modeCode = "";
                for (Map.Entry<String, String> str : map.entrySet()) {
                    if ((str.getValue()).equals(modeName)) {
                        modeCode = str.getKey();
                    }
                }
                if ("".equals(recipeName)) {
                    ToastUtils.show("请填写菜谱名称", Toast.LENGTH_SHORT);
                    return;
                }
                if ("".equals(recipeContent)) {
                    ToastUtils.show("请填写菜谱描述", Toast.LENGTH_SHORT);
                    return;
                }

                if (modeCode.equals("--") || temp.equals("--") || time.equals("--")) {
                    ToastUtils.show("请设置参数信息", Toast.LENGTH_LONG);
                    return;
                }
                saveOrEditRecipe(String.valueOf(userId), dt, recipeName, modeCode, temp,
                        time, recipeContent, String.valueOf(id));
                break;
                //返回
            case R.id.mode_back:
                exitDialog();
                break;
            //删除
            case R.id.iv_delete:
                deleteRecipe(userId);
                break;
            //参数设置
            case R.id.rl_par_set:
                setDialogParam();
                break;


        }
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
        dialogByType.setOkBtn("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIService.getInstance().popBack();
                dialogByType.dismiss();
            }
        });
        dialogByType.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogByType.dismiss();
            }
        });
        dialogByType.show();

    }


    private List<Integer> timeJson2List(JSONArray array) throws JSONException {
        List<Integer> timelist = new ArrayList<>();
        for (int j = 0; j < array.length(); j++) {
            Integer tim = (Integer) array.get(j);
            timelist.add(tim);
        }

        return TestDatas.createModeDataTime(timelist);
    }

    private List<Integer> tempJson2List(JSONArray temp) throws JSONException {
        List<Integer> templist = new ArrayList<>();
        for (int j = 0; j < temp.length(); j++) {
            Integer tem = (Integer) temp.get(j);
            templist.add(tem);
        }

        return TestDatas.createModeDataTemp(templist);
    }



    //编辑菜谱
    private void saveOrEditRecipe(String userId, String deviceType, String name, String modeCode,
                                  String temp, String min,
                                  String cookbookDesc, String id) {
        RokiRestHelper.Update035Recipe(Long.parseLong(userId), deviceType, name, modeCode, temp, min,
                "", "", cookbookDesc, Long.parseLong(id), new Callback<Reponses.Update035Recipe>() {
                    @Override
                    public void onSuccess(Reponses.Update035Recipe update035Recipe) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (tag) {
                                    SteamDiyDetailPage.instance.close();
                                }
                                SteamDiyListPage.instance.initFresh();

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
}
