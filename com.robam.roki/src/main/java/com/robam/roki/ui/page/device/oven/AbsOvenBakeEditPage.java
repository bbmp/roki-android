package com.robam.roki.ui.page.device.oven;

import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.KeyEvent;
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
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.DiyCookbookList;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.DeviceOvenDiyParams;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.dialog.AbsOvenDIYDialog;
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

/**
 * 编辑菜谱
 */

public class AbsOvenBakeEditPage extends BasePage {
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
    @InjectView(R.id.tv_roast)
    TextView tvRoast;

    @InjectView(R.id.tv_temp_unit)
    TextView tvTempUnit;
    @InjectView(R.id.tv_min_unit)
    TextView tvMinUnit;

    String mGuid;
    AbsOven oven;
    List<DeviceConfigurationFunctions> mDatas;
    private String dt;
    private long userId;
    List<DeviceOvenDiyParams> deviceList = new ArrayList<>();

    public AbsOvenDIYDialog absOvenDIYDialog;
    private String opRoast;


    DiyCookbookList diyCookbookList;
    private int id;
    boolean tag = false;
    private List<Integer> keyList;
    private IRokiDialog dialogByType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.abs_oven_bake_edit, container, false);
        ButterKnife.inject(this, view);
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        diyCookbookList = bd == null ? null : (DiyCookbookList) bd.getSerializable(PageArgumentKey.Bean);
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        oven = Plat.deviceService.lookupChild(mGuid);
        userId = Plat.accountService.getCurrentUserId();
        //限制字数250字
        etRecipeContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(250)});
        selectCode();
        initData();
        return view;
    }


    private void initData() {
        if (oven != null) {
            dt = oven.getDt();
        }

        tvTitleName.setText("编辑菜谱");
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
                    Map<String, HashMap<String, DeviceOvenDiyParams>> json2Map = JsonUtils.getJson2Map(modelStr);
                    keyList = new ArrayList<>();
                    Set<String> strings = json2Map.keySet();
                    for (String str : strings) {
                        keyList.add(Integer.valueOf(str));
                    }
                    Collections.sort(keyList);

                    for (int j = 0; j < keyList.size(); j++) {
                        JSONObject obj = model.getJSONObject(keyList.get(j) + "");
                        String value = obj.getString("value");
                        String hasRotate = obj.getString("hasRotate");
                        String paramType = obj.getString("paramType");
                        String defaultTemp = obj.getString("defaultTemp");
                        String defaultMinute = obj.getString("defaultMinute");
                        JSONArray timeArray = obj.getJSONObject("time").getJSONArray("value");
                        JSONArray tempArray = obj.getJSONObject("temp").getJSONArray("value");
                        DeviceOvenDiyParams deviceOvenDiyParams = new DeviceOvenDiyParams();
                        deviceOvenDiyParams.setValue(value);
                        deviceOvenDiyParams.setHasRotate(hasRotate);
                        deviceOvenDiyParams.setParamType(paramType);
                        deviceOvenDiyParams.setDefaultTemp(defaultTemp);
                        deviceOvenDiyParams.setDefaultMinute(defaultMinute);
                        deviceOvenDiyParams.setTimeList(timeJson2List(timeArray));
                        deviceOvenDiyParams.setTempList(tempJson2List(tempArray));
                        deviceList.add(deviceOvenDiyParams);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtils.i("20200311123456", e.getMessage());
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
            int openRotate = diyCookbookList.openRotate;
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
            if (openRotate == 1) {
                tvRoast.setText("开");
            } else {
                tvRoast.setText("关");
            }
        }

    }

    private Map<String, String> map;

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


    private void setDialogParam() {
        if (deviceList == null) {
            return;
        }
        List<String> listButton = TestDatas.createDialogText("", "℃", "分钟", "取消", "确定");
        absOvenDIYDialog = new AbsOvenDIYDialog(cx, deviceList, keyList, listButton);

        absOvenDIYDialog.showDiyDialog(absOvenDIYDialog);
        absOvenDIYDialog.setListener(new AbsOvenDIYDialog.PickListener() {
                                         @Override
                                         public void onCancel() {

                                         }

                                         @Override
                                         public void onConfirm(DeviceOvenDiyParams deviceOvenDiyParams, Integer integer2,
                                                               Integer integer, Integer integer1, boolean isOpen, boolean isClose) {
                                             LogUtils.i("20200428011","integer2::"+integer2);
                                             LogUtils.i("20200428011","integer::"+integer);
                                             LogUtils.i("20200428011","integer1::"+integer1);
                                             tvModeName.setText(deviceOvenDiyParams.getValue());
                                             tvTemp.setText(String.valueOf(integer));
                                             tvTempUnit.setText("℃");
                                             tvMin.setText(String.valueOf(integer1));
                                             tvMinUnit.setText("分钟");
                                             if (isOpen) {
                                                 opRoast = "1";
                                                 tvRoast.setText("开");
                                             } else {
                                                 tvRoast.setText("关");
                                                 opRoast = "0";
                                             }
                                         }
                                     }
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);

        if (absOvenDIYDialog != null) {
            absOvenDIYDialog.dismiss();
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
                String recipeContent = etRecipeContent.getText().toString().trim();//描述
                String modeName = tvModeName.getText().toString().trim();//模式
                String temp = tvTemp.getText().toString().trim();//温度
                String time = tvMin.getText().toString().trim(); //时间
                String roast = tvRoast.getText().toString().trim();//旋转烤
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

                if (modeCode.equals("--") || temp.equals("--") || time.equals("--") || roast.equals("--")) {
                    ToastUtils.show("请设置参数信息", Toast.LENGTH_LONG);
                    return;
                }
                saveOrEditRecipe(String.valueOf(userId), dt, recipeName, modeCode, temp,
                        time, "1", opRoast, recipeContent, String.valueOf(id));

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
    private void saveOrEditRecipe(String userId, String deviceType, String name, String modeCode,
                                  String temp, String min, String hasRotate, String openRotate,
                                  String cookbookDesc, String id) {
        RokiRestHelper.Update035Recipe(Long.parseLong(userId), deviceType, name, modeCode, temp, min,
                hasRotate, openRotate, cookbookDesc, Long.parseLong(id), new Callback<Reponses.Update035Recipe>() {
                    @Override
                    public void onSuccess(Reponses.Update035Recipe update035Recipe) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (tag) {
                                    AbsOvenBakeDiyDetailPage.instance.close();
                                }
                                AbsOvenBakeDiyListPage.instance.initFresh();

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

        dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        dialogByType.setTitleText("退出编辑");
        dialogByType.setContentText("如果离开，此次编辑内容将会丢失，确认离开？");
        dialogByType.setOkBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogByType.dismiss();
            }
        });
        dialogByType.setCancelBtn("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIService.getInstance().popBack().popBack();
                dialogByType.dismiss();
            }
        });
        dialogByType.show();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}


