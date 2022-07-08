package com.robam.roki.ui.page.device.integratedStove;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemChildLongClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.hjq.toast.ToastUtils;
import com.legent.plat.Plat;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.robam.common.events.IntegStoveStatusChangedEvent;
import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;
import com.robam.common.pojos.device.integratedStove.IntegStoveStatus;
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
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter3.Rv610StepAdapter;
import com.robam.roki.ui.adapter3.RvIntegraStepAdapter;
import com.robam.roki.ui.dialog.type.Dialog_Type_27;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.utils.DateUtil;
import com.robam.roki.utils.StringUtil;
import com.robam.roki.utils.TestDatas;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author r210190
 * 多段菜谱
 */
public class MultiEditDetailPage extends MyBasePage<MainActivity> {
    public static String RECIPEID = "RECIPE_ID";
    private List<DeviceConfigurationFunctions> mDatas;
    String[] strings = new String[]{"零" ,"一" , "二"  ,"三" ,"四" ,"五" ,"六" ,"七" };
    @InjectView(R.id.recipe_name)
    EditText recipeName;
    @InjectView(R.id.tv_info)
    TextView multiInfo;
    /**
     * 步骤recyclerView
     */
    @InjectView(R.id.rv_610_step)
    RecyclerView rv610Step;
    /**
     * 步骤adapter
     */
    private RvIntegraStepAdapter rvIntegraStepAdapter;
    /**
     * 携带菜谱id
     */
    private int recipeId = -1;
    private AppCompatButton btnAutomatic;
    /**
     * 多段设置dialog
     */
    Dialog_Type_27 dialog ;
    /**'
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
    /**
     * 设备GUID
     */
    String guid;
    /**
     * 设备
     */
    AbsIntegratedStove integratedStove;

    PengpaiZhengParamBean ppzParams = null;

    /**
     * @param event
     */
    @Subscribe
    public void onEvent(IntegStoveStatusChangedEvent event) {
        if (integratedStove == null || !Objects.equal(integratedStove.getID(), event.pojo.getID())) {
            return;
        }
        integratedStove = event.pojo ;
        if (integratedStove.getID().equals(event.pojo.getID())){
            if(integratedStove.workState != IntegStoveStatus.workState_free && integratedStove.workState != IntegStoveStatus.workState_complete){
                UIService.getInstance().popBack().popBack();
                UIService.getInstance().postPage(PageKey.IntegratedStoveWorkPage, getArguments());
            }
        }

    }
    @Override
    protected int getLayoutId() {
        return R.layout.page_multi_integra_recipe_edit;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
       getTitleBar().setOnTitleBarListener(this);
        btnAutomatic = findViewById(R.id.btn_automatic);
        rv610Step.setLayoutManager(new LinearLayoutManager(cx, RecyclerView.VERTICAL, false));
        rvIntegraStepAdapter = new RvIntegraStepAdapter();
        rv610Step.setAdapter(rvIntegraStepAdapter);
        View footViewAdd = getLayoutInflater().inflate(R.layout.item_integra_add_step, new FrameLayout(cx), false);
        rvIntegraStepAdapter.addFooterView(footViewAdd);

        rvIntegraStepAdapter.addChildClickViewIds(R.id.ll_item , R.id.tv_del);
        rvIntegraStepAdapter.addChildLongClickViewIds(R.id.ll_item);
        rvIntegraStepAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull @NotNull BaseQuickAdapter baseQuickAdapter, @NonNull @NotNull View view, int i) {
                if (view.getId() == R.id.ll_item){
//                    rv610StepAdapter.setSelectPosition(i);
                    selectStep(i);
                }else if (view.getId() == R.id.tv_del){
                    if (recipeId != -1){
                        LitePal.delete(RecipeStepBean.class , rvIntegraStepAdapter.getItem(i).getId());
                        rvIntegraStepAdapter.removeAt(i);
                    }else {
                        rvIntegraStepAdapter.removeAt(i);
                    }
                    rvIntegraStepAdapter.setSelectPosition(-1);
                }
            }
        });

        rvIntegraStepAdapter.setOnItemChildLongClickListener(new OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(@NonNull @NotNull BaseQuickAdapter baseQuickAdapter, @NonNull @NotNull View view, int i) {
                if (view.getId() == R.id.ll_item){
                    rvIntegraStepAdapter.setSelectPosition(i);
                }

                return true;
            }
        });
//        rv610StepAdapter.addOnLeftTouchListener(new Rv610StepAdapter.OnLeftTouchListener() {
//            @Override
//            public void onLeftTouch(int position) {
//                rv610StepAdapter.setSelectPosition(position);
//            }
//        });
        footViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rvIntegraStepAdapter.getItemCount() >= 4){
                    ToastUtils.show("多段步骤只能添加三步");
                    return;
                }
                selectStep(-1);
            }
        });
        setOnClickListener(btnAutomatic);
    }

    @Override
    protected void initData() {
        Bundle bd = getArguments();
        if (bd != null) {
            recipeId = bd.getInt(RECIPEID , -1);
            mDatas = (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
            guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
            integratedStove = Plat.deviceService.lookupChild(guid);
            if (recipeId != -1 ){
                 firstRecipe = LitePal.where("id = ?", recipeId + "").findFirst(RecipeBean.class);
                rvIntegraStepAdapter.addData(firstRecipe.getRecipeStepList());
                recipeName.setText(firstRecipe.getRecipe_names());
//                recipeName.setEnabled(false);
                multiInfo.setText( firstRecipe.getRecipeStepTimes() + "min");
            }

        }

    }


    @Override
    public void onClick(View view) {
        if (view == btnAutomatic){
            if (StringUtil.isEmpty(recipeName.getText().toString())){
                ToastUtils.show("请输入菜谱名称");
                return;
            }
            if (recipeId != -1){
                ContentValues values = new ContentValues();
                values.put("recipe_names", recipeName.getText().toString());
                LitePal.update(RecipeBean.class , values , recipeId) ;
                ToastUtils.show("保存成功");
                UIService.getInstance().popBack();
                return;
            }
            String recipeId = Plat.accountService.getCurrentUserId() + DateUtil.getCurrentTime(DateUtil.PATTERN_NO_BLANK);
            RecipeBean d610Recipe = new RecipeBean(recipeId ,recipeName.getText().toString(), Plat.accountService.getCurrentUserId(), integratedStove.getDispalyType(), System.currentTimeMillis());
            d610Recipe.save();
            RecipeBean recipeBean = LitePal.where("recipe_id = ?", recipeId).findFirst(RecipeBean.class);
            List<RecipeStepBean> datas = rvIntegraStepAdapter.getData();
            for (RecipeStepBean bean:
                    datas) {
//                bean.setRecipe_id(recipeBean.getRecipe_id());
                bean.setRecipebean(recipeBean);
                bean.save();
            }
            ToastUtils.show("保存成功");
            UIService.getInstance().popBack();
        }
    }

    /**
     * 显示dialog
     */
    private void selectStep(int position ) {
         dialog = RokiDialogFactory.createDialogByType27(cx, mDatas, new OnItemClickListener() {
             @Override
             public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                 DeviceConfigurationFunctions item = dialog.mDeviceModelAdapter.getItem(position);
                 SteamOvenModeEnum match = SteamOvenModeEnum.catchMessage(item.functionName);
                 if (match == SteamOvenModeEnum.ZHIKONGZHENG){
                     pengpaiMode( position);
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
//        Dialog_Type_27 dialog27 = (Dialog_Type_27) this.dialog;
        dialog.setOkBtn(R.string.ok_btn, new Dialog_Type_27.OnItemSelectOkListener() {
            @Override
            public void onOkListener(String steam, String time ,String temp) {
                RecipeStepBean recipeStepBean = new RecipeStepBean();
                recipeStepBean.setFunction_code(functionCode);
                recipeStepBean.setFunction_name(functionName);
                recipeStepBean.setFunction_params(functionParams.toString());
                recipeStepBean.setWork_mode(mode);
                if (SteamOvenModeEnum.match(mode) == SteamOvenModeEnum.ZHIKONGZHENG){
                    int selectedItem = dialog.getmLoopViewFront().getSelectedItem();
                    String s = ppzParams.param.setSteam.value.get(selectedItem);
                    short steam1 = Short.parseShort(s);
                    recipeStepBean.setSteam_flow( steam1);

                    recipeStepBean.setTemperature(Integer.parseInt(temp));
                }else {
                    recipeStepBean.setSteam_flow( 0);
                    recipeStepBean.setTemperature(Integer.parseInt(steam));
                }

                recipeStepBean.setTime(Integer.parseInt(time));
                if (recipeId != -1 ){
                    if (position != -1){
                        recipeStepBean.update(rvIntegraStepAdapter.getItem(position).getId());
                        firstRecipe = LitePal.where("id = ?", recipeId + "").findFirst(RecipeBean.class);
                        rvIntegraStepAdapter.setNewInstance(firstRecipe.getRecipeStepList());
                    }else {
                        recipeStepBean.setRecipebean(firstRecipe);
                        recipeStepBean.save();
                        rvIntegraStepAdapter.addData(recipeStepBean);
                    }
                    firstRecipe = LitePal.where("id = ?", recipeId + "").findFirst(RecipeBean.class);
                    multiInfo.setText( firstRecipe.getRecipeStepTimes() + "min");
                }else {
                    if (position != -1){
                        rvIntegraStepAdapter.setData(position , recipeStepBean);
                    }else {
                        rvIntegraStepAdapter.addData(recipeStepBean);
                    }
                }

                dialog.dismiss();
            }
        });

    }

    //模式选择参数写入
    private void modelSelectItemEvent(int position) {

        try {
            if (mDatas != null  && mDatas.size() > 0) {
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
                        dialog.setVisible(false);
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
                                        Log.i("9YC03" , contentFront );

                                    }
                                }, null, new OnItemSelectedListenerRear() {
                                    @Override
                                    public void onItemSelectedRear(String contentRear) {
                                        Log.i("9YC03" , contentRear );
                                    }
                                });
                        return;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pengpaiMode(int position){
        try {
            ppzParams = JsonUtils.json2Pojo(mDatas.get(position).functionParams, PengpaiZhengParamBean.class);
            if (ppzParams != null) {
                functionCode = mDatas.get(position).functionCode;
                functionName = mDatas.get(position).functionName;
                functionParams = new JSONObject(mDatas.get(position).functionParams);
                List<String> tempDown = ppzParams.param.setSteam.title;
                List<Integer> tempUp = ppzParams.param.setTemp.value;
                List<Integer> time = ppzParams.param.setTime.value;

                String tempUpDefault = ppzParams.param.defaultSetTemp.value;
                String timeDefault = ppzParams.param.defaultSetTime.value;
                String defSteamFlow = ppzParams.param.defaultSetSteam.value;

                mode = Short.parseShort(ppzParams.param.model.value);
                int deNum1 = Integer.parseInt(tempUpDefault) - tempUp.get(0);
                int deNum3 = Integer.parseInt(timeDefault) - time.get(0);

                dialog.setVisible(true);
                dialog.setWheelViewData(
                        tempDown,
//                        HelperRikaData.getTempData3(tempDown),
                        HelperRikaData.getTempDataEXPCentener(tempUp, HelperRikaData.getTempData3(tempUp).get(deNum1)),
                        HelperRikaData.getTimeData4(time),
                         "null",
                        false,
                        1,
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
//                dialog.getmLoopViewCenter().setListenerPosition(new OnItemSelectedListenerPosition() {
//                    @Override
//                    public void onItemSelectedRear(int position) {
//                        if (position <= HelperRikaData.getTempData3(tempUp).size() - 20){
//                            dialog.getmLoopViewFront().setItems(HelperRikaData.getTempData3(tempUp));
//                            dialog.getmLoopViewFront().setCurrentPosition(position + 20);
//                        }else {
//                            dialog.getmLoopViewFront().setItems(HelperRikaData.getTempData3(tempUp));
//                            dialog.getmLoopViewFront().setCurrentPosition(HelperRikaData.getTempData3(tempUp).size()-1);
//                        }
//                    }
//                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dialog != null && dialog.isShow()){
            dialog.dismiss();
        }
        EventUtils.postEvent(new PageBackEvent("Multi610EditDetailPage"));
        ButterKnife.reset(this);
    }
}
