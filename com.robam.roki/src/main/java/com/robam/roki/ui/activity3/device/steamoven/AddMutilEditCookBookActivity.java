package com.robam.roki.ui.activity3.device.steamoven;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.common.collect.Lists;
import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.pojos.RCReponse;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.Requests;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum;
import com.robam.roki.R;
import com.robam.roki.db.model.RecipeBean;
import com.robam.roki.db.model.RecipeStepBean;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerPosition;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.model.bean.OvenExpParamBean;
import com.robam.roki.model.bean.PengpaiZhengParamBean;
import com.robam.roki.model.bean.SteamOvenModelFunctionParams;
import com.robam.roki.model.helper.HelperRikaData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.activity3.device.steamoven.adapter.RvSteamAddStepAdapter;
import com.robam.roki.ui.adapter3.Rv610StepAdapter;
import com.robam.roki.ui.bean3.DeleteMultiItemBean;
import com.robam.roki.ui.dialog.type.Dialog_Type_27;
import com.robam.roki.ui.helper3.http.IRokiApiService;
import com.robam.roki.utils.StringUtil;
import com.robam.roki.utils.TestDatas;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddMutilEditCookBookActivity extends DeviceBaseFuntionActivity {

    public static String RECIPEID = "RECIPE_ID";
    private List<DeviceConfigurationFunctions> mDatas;
    String[] strings = new String[]{"零", "一", "二", "三", "四", "五", "六", "七"};
//    @InjectView(R.id.recipe_name)
    EditText recipeName;
//    @InjectView(R.id.tv_info)
    TextView multiInfo;
//    @InjectView(R.id.tb_title)
   TitleBar tbTitle;

    /**
     * 步骤recyclerView
     */

    /**
     * 步骤adapter
     */
    private RvSteamAddStepAdapter rv610StepAdapter;
    /**
     * 携带菜谱id
     */
    private int recipeId = -1;
    private AppCompatButton btnAutomatic;
    /**
     * 多段设置dialog
     */
    Dialog_Type_27 dialog;
    /**
     * '
     * 专业模式exp dialog
     */
    private List<Integer> setTempList;
    private List<Integer> setTimeList;
    private String functionCode;
    private String functionName;
    private JSONObject functionParams;
    private int mode;
    /**
     * 已有菜谱
     */
    private RecipeBean firstRecipe;

    private String mGuid;
    private List<Requests.saveMultiRecipeList> multiStepDtoList = new ArrayList<>();
    /**
     * 澎湃蒸参数
     */
    private PengpaiZhengParamBean ppzParams;

    private RecyclerView rv610Step;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_mutil_cook_book;
    }

    View footViewAdd;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        getTitleBar().setOnTitleBarListener(this);
        btnAutomatic = findViewById(R.id.btn_automatic);
        tbTitle=findViewById(R.id.tb_title);
        recipeName=findViewById(R.id.recipe_name);
        multiInfo=findViewById(R.id.tv_info);
        rv610Step=findViewById(R.id.rv_610_step);
        rv610Step.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rv610StepAdapter = new RvSteamAddStepAdapter();
        rv610Step.setAdapter(rv610StepAdapter);
        footViewAdd = getLayoutInflater().inflate(R.layout.item_steam_new_add_step, new FrameLayout(this), false);
        rv610StepAdapter.addFooterView(footViewAdd);

        rv610StepAdapter.addChildClickViewIds(R.id.ll_item, R.id.tv_del);
        rv610StepAdapter.addChildLongClickViewIds(R.id.ll_item);
        rv610StepAdapter.setOnItemChildClickListener((baseQuickAdapter, view, i) -> {
            if (view.getId() == R.id.ll_item) {
//                    rv610StepAdapter.setSelectPosition(i);
                selectStep(i);
            } else if (view.getId() == R.id.tv_del) {
                if (recipeId != -1) {
                    if (rv610StepAdapter.getItem(i).isLocal()) {
                        rv610StepAdapter.removeAt(i);
                        setMultiInfo();
                    } else {
                        deleteMultiItem(recipeId, rv610StepAdapter.getItem(i).getId());
                    }
                } else {
                    rv610StepAdapter.removeAt(i);
                }
                rv610StepAdapter.setSelectPosition(-1);
            }
        });

        rv610StepAdapter.setOnItemChildLongClickListener((baseQuickAdapter, view, i) -> {
            if (view.getId() == R.id.ll_item) {
                rv610StepAdapter.setSelectPosition(i);
            }

            return true;
        });

        footViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rv610StepAdapter.getItemCount() >= 4) {
                    ToastUtils.show("多段步骤只能添加三步");
                    return;
                }
                selectStep(-1);
            }
        });
        setOnClickListener(btnAutomatic);

    }


    private Bundle getArguments(){
        Bundle bundle=getIntent().getBundleExtra(DeviceBaseFuntionActivity.BUNDLE);
        return bundle;
    }

    /**
     *  func.subView
     * //                        .subViewModelMap
     * //                        .subViewModelMapSubView
     * //                        .deviceConfigurationFunctions
     */
    @Override
    protected void initData() {
        Bundle bd = getArguments();
        if (bd != null) {
            mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
            mDatas = bd == null ? null : ((DeviceConfigurationFunctions) bd.getSerializable(DeviceBaseFuntionActivity.FUNCTION)).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
            recipeId = bd.getInt(RECIPEID, -1);
            Reponses.multiRecipeList Item = (Reponses.multiRecipeList) bd.getSerializable("Item");
            if (recipeId != -1) {
                tbTitle.setTitle("编辑多段");
                recipeName.setText(Item.name);
                int time = 0;
                if (Item.multiStepDtoList != null && Item.multiStepDtoList.size() > 0) {
                    for (int i = 0; i < Item.multiStepDtoList.size(); i++) {
                        RecipeStepBean recipeStepBean = new RecipeStepBean();
                        recipeStepBean.setFunction_code(Item.multiStepDtoList.get(i).modelCode);
                        recipeStepBean.setFunction_name(Item.multiStepDtoList.get(i).modelName);
                        recipeStepBean.setTemperature(Integer.parseInt(Item.multiStepDtoList.get(i).temperature));
                        recipeStepBean.setTemperature2(StringUtil.isEmpty(Item.multiStepDtoList.get(i).downTemperature) ? 0 : Integer.parseInt(Item.multiStepDtoList.get(i).downTemperature));
                        recipeStepBean.setTime(Item.multiStepDtoList.get(i).getTime(mGuid));
                        recipeStepBean.setId(Integer.parseInt(Item.multiStepDtoList.get(i).no));
                        recipeStepBean.setSteam_flow(Integer.parseInt(Item.multiStepDtoList.get(i).steamQuantity));
                        recipeStepBean.setWork_mode(Integer.parseInt(Item.multiStepDtoList.get(i).modelCode));
                        rv610StepAdapter.addData(recipeStepBean);
                        time += Item.multiStepDtoList.get(i).getTime(mGuid);
                    }
                }
//                multiInfo.setText("共" + strings[Item.multiStepDtoList.size()] + "段 " + time + "min");
                setMultiInfo();

            }

        }
        //本地读取模式数据
//        String multModes_data = PreferenceUtils.getString("multModes_data", "");
//        mDatas = JsonUtils.json2List(multModes_data, DeviceConfigurationFunctions.class);
//        mGuid = PreferenceUtils.getString("610Guid", "mGuid");
    }

    @Override
    protected void dealData() {

    }


    @Override
    public void onClick(View view) {
        if (view == btnAutomatic) {
            if (StringUtil.isEmpty(recipeName.getText().toString())) {
                ToastUtils.show("请输入菜谱名称");
                return;
            }

            Requests.saveMultiRecipeRequest reqBody = new Requests.saveMultiRecipeRequest();
            reqBody.userId = Plat.accountService.getCurrentUserId();
            reqBody.name = recipeName.getText().toString();
            reqBody.deviceGuid = mGuid;
            if (recipeId != -1) {
                reqBody.id = recipeId;
            }
            if (rv610StepAdapter.getItemCount()- 1<=1){
                ToastUtils.show("多段添加不能少于2步");
                return;
            }
            for (int i = 0; i < rv610StepAdapter.getItemCount() - 1; i++) {
                Requests.saveMultiRecipeList item = new Requests.saveMultiRecipeList();
                item.downTemperature = rv610StepAdapter.getItem(i).getTemperature2() + "";
//                item.modelCode = rv610StepAdapter.getItem(i).getFunction_code();
                item.modelCode = rv610StepAdapter.getItem(i).getWork_mode() + "";
                item.modelName = rv610StepAdapter.getItem(i).getFunction_name();
                item.no = i + 1 + "";
                item.steamQuantity = rv610StepAdapter.getItem(i).getSteam_flow() + "";
                item.temperature = rv610StepAdapter.getItem(i).getTemperature() + "";
                if (mGuid.contains("DB620") || mGuid.contains("CQ920")){
                    item.time = rv610StepAdapter.getItem(i).getTime() * 60 + "";
                }else {
                    item.time = rv610StepAdapter.getItem(i).getTime() + "";
                }

                multiStepDtoList.add(item);
            }
            reqBody.multiStepDtoList = multiStepDtoList;
            saveMultiRecipe(reqBody);

        }
    }

    /**
     * 显示dialog
     */
    private void selectStep(int position) {
        dialog = RokiDialogFactory.createDialogByType27(this, mDatas, new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                DeviceConfigurationFunctions item = dialog.mDeviceModelAdapter.getItem(position);
                if ("EXP".equals(item.functionName)) {
                    expMode(position);
                } else  if ("澎湃蒸".equals(item.functionName)){
                    pengpaiMode(position);
                }else {
                    modelSelectItemEvent(position);
                }
                dialog.mDeviceModelAdapter.setSelectPosition(position);
            }
        });
        dialog.show();
        if (SteamOvenModeEnum.catchMessage(mDatas.get(0).functionName) == SteamOvenModeEnum.ZHIKONGZHENG){
            pengpaiMode(0);
        }else {
            modelSelectItemEvent(0);
        }
        dialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Dialog_Type_27 dialog27 = (Dialog_Type_27) this.dialog;
        dialog27.setOkBtn(R.string.ok_btn, new Dialog_Type_27.OnItemSelectOkListener() {
            @Override
            public void onOkListener(String temperature, String time, String temp2) {
                Log.i("D610", "---------------" + temperature + " ------" + time);
                RecipeStepBean recipeStepBean = new RecipeStepBean();
                recipeStepBean.setFunction_code(functionCode);
                recipeStepBean.setFunction_name(functionName);
                //澎湃蒸 对应 蒸汽量 温度 时间
                short steam1  = 0;
                if (SteamOvenModeEnum.catchMessage(functionName) == SteamOvenModeEnum.ZHIKONGZHENG){
                    recipeStepBean.setFunction_params(functionParams.toString());
                    recipeStepBean.setWork_mode(mode);
                    recipeStepBean.setTemperature(Integer.parseInt(temp2));
                    recipeStepBean.setTime(Integer.parseInt(time));
                    int selectedItem = dialog.getmLoopViewFront().getSelectedItem();
                    String s = ppzParams.param.setSteam.value.get(selectedItem);
                    steam1 = Short.parseShort(s);
                    recipeStepBean.setSteam_flow( steam1);
                }else {
                    recipeStepBean.setFunction_params(functionParams.toString());
                    recipeStepBean.setWork_mode(mode);
                    recipeStepBean.setTemperature(Integer.parseInt(temperature));
                    recipeStepBean.setTemperature2(StringUtil.isEmpty(temp2) ? 0 : Integer.parseInt(temp2));
                }
                recipeStepBean.setTime(Integer.parseInt(time));
                recipeStepBean.setLocal(true);
                if (recipeId != -1) {
                    if (position != -1) {
                        rv610StepAdapter.getItem(position).setFunction_code(functionCode);
                        rv610StepAdapter.getItem(position).setFunction_name(functionName);
                        rv610StepAdapter.getItem(position).setFunction_params(functionParams.toString());
                        rv610StepAdapter.getItem(position).setWork_mode(mode);
                        rv610StepAdapter.getItem(position).setSteam_flow(steam1);
                        rv610StepAdapter.getItem(position).setTemperature(Integer.parseInt(temperature));
                        rv610StepAdapter.getItem(position).setTemperature2(StringUtil.isEmpty(temp2) ? 0 : Integer.parseInt(temp2));
                        rv610StepAdapter.getItem(position).setTime(Integer.parseInt(time));
                        rv610StepAdapter.notifyDataSetChanged();
                    } else {
                        recipeStepBean.setRecipebean(firstRecipe);
                        recipeStepBean.save();
                        rv610StepAdapter.addData(recipeStepBean);
                    }
                } else {
                    if (position != -1) {
                        rv610StepAdapter.setData(position, recipeStepBean);
                    } else {
                        rv610StepAdapter.addData(recipeStepBean);
                    }
                }
                setMultiInfo();
                dialog.dismiss();
            }
        });

    }

    private void setMultiInfo() {
        int time = 0;
        for (int i = 0; i < rv610StepAdapter.getData().size(); i++) {
            time += rv610StepAdapter.getData().get(i).getTime();
        }
        multiInfo.setText(time + "min");
        if (rv610StepAdapter.getData().size() == 3) {
            footViewAdd.setVisibility(View.GONE);
        } else {
            footViewAdd.setVisibility(View.VISIBLE);
        }
    }

    //模式选择参数写入
    private void modelSelectItemEvent(int position) {

        try {
            if (mDatas != null && mDatas.size() > 0) {
                //                    }
                functionCode = mDatas.get(position).functionCode;
                functionName = mDatas.get(position).functionName;
                functionParams = new JSONObject(mDatas.get(position).functionParams);
                ArrayList<DeviceConfigurationFunctions> descList = Lists.newArrayList();
                descList.clear();
                descList.add(mDatas.get(position));
                String freshSteamedParams = mDatas.get(position).functionParams;
                SteamOvenModelFunctionParams functionParams = JsonUtils.json2Pojo(freshSteamedParams, SteamOvenModelFunctionParams.class);
                int mSteamModel = Short.parseShort(functionParams.getParam().getModel().getValue());
                mode = Short.parseShort(functionParams.getParam().getModel().getValue());
                String tempDefaultValue = functionParams.getParam().getDefaultSetTemp().getValue();
                String timeDefaultValue = functionParams.getParam().getDefaultSetTime().getValue();
                short newDefaultTemp = (Short.parseShort(tempDefaultValue));
                short newDefaultTime = (Short.parseShort(timeDefaultValue));
                setTempList = functionParams.getParam().getSetTemp().getValue();
                setTimeList = functionParams.getParam().getSetTime().getValue();
                //拿到时间温度的索引值


                int indexTemp = newDefaultTemp - setTempList.get(0);
                int indexTime = newDefaultTime - setTimeList.get(0);
                dialog.set2Visible(false);
                dialog.setWheelViewData(
                        HelperRikaData.getTempData3(setTempList),
                        null,
                        HelperRikaData.getTimeData4(setTimeList),
                        descList,
                        false,
                        indexTemp,
                        0,
                        indexTime,
                        new OnItemSelectedListenerFrone() {
                            @Override
                            public void onItemSelectedFront(String contentFront) {
                                Log.i("610", contentFront);

                            }
                        }, null, new OnItemSelectedListenerRear() {
                            @Override
                            public void onItemSelectedRear(String contentRear) {
                                Log.i("610", contentRear);
                            }
                        });
                return;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void expMode(int position) {
        OvenExpParamBean ovenParams = null;
        try {
            ovenParams = JsonUtils.json2Pojo(mDatas.get(position).functionParams, OvenExpParamBean.class);
            if (ovenParams != null) {
                functionCode = mDatas.get(position).functionCode;
                functionName = mDatas.get(position).functionName;
                functionParams = new JSONObject(mDatas.get(position).functionParams);
                List<Integer> tempUp = ovenParams.getParam().getUpTemp().getValue();
                List<Integer> tempDown = ovenParams.getParam().getDownTemp().getValue();
                List<Integer> time = ovenParams.getParam().getMinute().getValue();
                String tempUpDefault = ovenParams.getParam().getUpTempDefault().getValue();

                String timeDefault = ovenParams.getParam().getMinuteDefault().getValue();
                String tempDiff = ovenParams.getParam().getTempDiff().getValue();
                String tempStart = ovenParams.getParam().getTempStart().getValue();
                String downMin = ovenParams.getParam().getTempMin().getValue();
                String descTips = ovenParams.getParam().getDesc().getValue();

                List<Integer> tempUpList = TestDatas.createTempertureDatas(tempUp);
                List<Integer> timeList = TestDatas.createModeDataTime(time);
                mode = Short.parseShort(ovenParams.getParam().getModel().getValue());
                int deNum1 = Integer.parseInt(tempUpDefault) - tempUp.get(0);
                int deDiff = Integer.parseInt(tempDiff);
                int deNum2 = deNum1 - deDiff;
                int deNum3 = Integer.parseInt(timeDefault) - time.get(0);
                int deStart = Integer.parseInt(tempStart);
                int min = Integer.parseInt(downMin);
                dialog.set2Visible(true);
                dialog.setWheelViewData(
                        HelperRikaData.getTempData3(tempUp),
//                        HelperRikaData.getTempData3(tempDown),
                        HelperRikaData.getTempDataEXPCentener(tempDown, HelperRikaData.getTempData3(tempUp).get(deNum1)),
                        HelperRikaData.getTimeData4(time),
                        "null",
                        false,
                        deNum1,
                        0,
                        deNum3,
                        new OnItemSelectedListenerFrone() {
                            @Override
                            public void onItemSelectedFront(String contentFront) {
                                Log.i("610", contentFront);

                            }
                        }, new OnItemSelectedListenerCenter() {
                            @Override
                            public void onItemSelectedCenter(String contentCenter) {

                            }
                        }, new OnItemSelectedListenerRear() {
                            @Override
                            public void onItemSelectedRear(String contentRear) {
                                Log.i("610", contentRear);
                            }
                        });
                dialog.getmLoopViewFront().setListenerPosition(new OnItemSelectedListenerPosition() {
                    @Override
                    public void onItemSelectedRear(int position) {
                        LogUtils.i("position", "-----" + position);
                        List<String> tempDataEXPCentener = HelperRikaData.getTempDataEXPCentener(tempDown, HelperRikaData.getTempData3(tempUp).get(position));
                        dialog.getmLoopViewCenter().setItems(tempDataEXPCentener);
                        dialog.getmLoopViewCenter().setCurrentPosition(0);

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 澎湃蒸
     * @param position
     */
    private void pengpaiMode(int position){
        try {
            ppzParams = JsonUtils.json2Pojo(mDatas.get(position).functionParams, PengpaiZhengParamBean.class);
            if (ppzParams != null) {
                functionCode = mDatas.get(position).functionCode;
                functionName = mDatas.get(position).functionName;
                functionParams = new JSONObject(mDatas.get(position).functionParams);
                List<String> steamDown = ppzParams.param.setSteam.title;
                List<Integer> tempUp = ppzParams.param.setTemp.value;
                List<Integer> time = ppzParams.param.setTime.value;

                String tempUpDefault = ppzParams.param.defaultSetTemp.value;
                String timeDefault = ppzParams.param.defaultSetTime.value;
                String defSteamFlow = ppzParams.param.defaultSetSteam.value;

                mode = Short.parseShort(ppzParams.param.model.value);
                int deNum1 = Integer.parseInt(tempUpDefault) - tempUp.get(0);
                int deNum3 = Integer.parseInt(timeDefault) - time.get(0);
//                int steam = Integer.parseInt(defSteamFlow) - steamDown.get(0);
                int index=0;
                for (int i = 0; i < steamDown.size(); i++) {
                    if (defSteamFlow.equals(steamDown.get(i))){
                        index=i;
                    }
                }


                dialog.setVisible(true);
                dialog.setWheelViewData(
                        steamDown,
                        HelperRikaData.getTimeData4(tempUp),
                        HelperRikaData.getTimeData4(time),
                        "null",
                        false,
                        index,
                        deNum1,
                        deNum3,
                        new OnItemSelectedListenerFrone() {
                            @Override
                            public void onItemSelectedFront(String contentFront) {

                            }
                        }, new OnItemSelectedListenerCenter() {
                            @Override
                            public void onItemSelectedCenter(String contentCenter) {

                            }
                        }, new OnItemSelectedListenerRear() {
                            @Override
                            public void onItemSelectedRear(String contentRear) {
                            }
                        });
                dialog.getmLoopViewFront().setListenerPosition(new OnItemSelectedListenerPosition() {
                    @Override
                    public void onItemSelectedRear(int position) {
                        LogUtils.i("position"  , "-----" + position ) ;
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        EventUtils.postEvent(new PageBackEvent("MultiAddEditDetailPage"));
        ButterKnife.reset(this);
        if (dialog != null && dialog.isShow()) {
            dialog.dismiss();
        }
        super.onDestroy();


    }



    private void saveMultiRecipe(Requests.saveMultiRecipeRequest reqBody) {
        RokiRestHelper.saveMultiRecipe(reqBody, new Callback<RCReponse>() {
            @Override
            public void onSuccess(RCReponse recipes) {
                ToastUtils.show("保存成功");
                 finish();
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("保存失败");
                finish();
            }
        });
    }

    private void deleteMultiItem(int id, int position) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Plat.serverOpt.getRestfulBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IRokiApiService api = retrofit.create(IRokiApiService.class);
        Call<DeleteMultiItemBean> callRequest = api.deleteMultiItemBean(id, position);
        callRequest.enqueue(new retrofit2.Callback<DeleteMultiItemBean>() {
            @Override
            public void onResponse(Call<DeleteMultiItemBean> call, retrofit2.Response<DeleteMultiItemBean> response) {
                if (response.body().rc == 0) {
                    com.hjq.toast.ToastUtils.show("删除成功");
                    rv610StepAdapter.getData().remove(position - 1);
                    rv610StepAdapter.notifyDataSetChanged();
                    setMultiInfo();
                } else if (response.body().rc == 1) {
                    com.hjq.toast.ToastUtils.show(response.body().msg);
                }
            }

            @Override
            public void onFailure(Call<DeleteMultiItemBean> call, Throwable throwable) {
                com.hjq.toast.ToastUtils.show("删除失败");
            }
        });
    }
}