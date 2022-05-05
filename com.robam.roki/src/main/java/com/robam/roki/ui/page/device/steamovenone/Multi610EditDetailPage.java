package com.robam.roki.ui.page.device.steamovenone;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.icu.util.ValueIterator;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
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
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.google.common.collect.Lists;
import com.hjq.bar.OnTitleBarListener;
import com.legent.plat.Plat;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.roki.R;
import com.robam.roki.broadcast.RecipeStep;
import com.robam.roki.db.model.CookingStepsModel;
import com.robam.roki.db.model.RecipeBean;
import com.robam.roki.db.model.RecipeStepBean;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerPosition;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.model.bean.OvenExpParamBean;
import com.robam.roki.model.bean.SteamOvenBean;
import com.robam.roki.model.bean.SteamOvenModelFunctionParams;
import com.robam.roki.model.helper.HelperRikaData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter3.Rv610StepAdapter;
import com.robam.roki.ui.dialog.type.Dialog_Type_27;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.utils.DateUtil;
import com.robam.roki.utils.DialogUtil;
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
 * 610离线菜谱详情
 */
public class Multi610EditDetailPage extends MyBasePage<MainActivity> {
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
    private Rv610StepAdapter rv610StepAdapter;
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

    float mPosX ;
    float mPosY ;
    float mCurPosX ;
    float mCurPosY ;
    private int mPosition;


    @Override
    protected int getLayoutId() {
        return R.layout.page_multi_610_recipe_edit;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
       getTitleBar().setOnTitleBarListener(this);
        btnAutomatic = findViewById(R.id.btn_automatic);
        rv610Step.setLayoutManager(new LinearLayoutManager(cx, RecyclerView.VERTICAL, false));
        rv610StepAdapter = new Rv610StepAdapter();
        rv610Step.setAdapter(rv610StepAdapter);
        View footViewAdd = getLayoutInflater().inflate(R.layout.item_d610_add_step, new FrameLayout(cx), false);
        rv610StepAdapter.addFooterView(footViewAdd);

        rv610StepAdapter.addChildClickViewIds(R.id.ll_item , R.id.tv_del);
        rv610StepAdapter.addChildLongClickViewIds(R.id.ll_item);
        rv610StepAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull @NotNull BaseQuickAdapter baseQuickAdapter, @NonNull @NotNull View view, int i) {
                if (view.getId() == R.id.ll_item){
//                    rv610StepAdapter.setSelectPosition(i);
                    selectStep(i);
                }else if (view.getId() == R.id.tv_del){
                    if (recipeId != -1){
                        LitePal.delete(RecipeStepBean.class , rv610StepAdapter.getItem(i).getId());
                        rv610StepAdapter.removeAt(i);
                    }else {
                        rv610StepAdapter.removeAt(i);
                    }
                    rv610StepAdapter.setSelectPosition(-1);
                }
            }
        });

        rv610StepAdapter.setOnItemChildLongClickListener(new OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(@NonNull @NotNull BaseQuickAdapter baseQuickAdapter, @NonNull @NotNull View view, int i) {
                if (view.getId() == R.id.ll_item){
                    rv610StepAdapter.setSelectPosition(i);
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
                if (rv610StepAdapter.getItemCount() >= 4){
                    ToastUtils.showShort("多段步骤只能添加三步");
                    return;
                }
                selectStep(-1);
            }
        });
        setOnClickListener(btnAutomatic);

//        rv610Step.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        mPosX = event.getX();
//                        mPosY = event.getY();
//
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        mCurPosX = event.getX();
//                        mCurPosY = event.getY();
//
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        View view2 = rv610Step.findChildViewUnder(mCurPosX, mCurPosY);
//                        if (view2 == null) {
//                            return false;
//                        }
//
//                        RecyclerView.ViewHolder childViewHolder2 = rv610Step.getChildViewHolder(view2);
//
////                        mItemLayout = childViewHolder.itemView;
////                            mItemLayout.setClickable(true);
//                        mPosition = childViewHolder2.getAdapterPosition();
////                            if (mCurPosY - mPosY > 0
////                                    && (Math.abs(mCurPosY - mPosY) > 25)) {
////                                //向下滑動
////
////                            } else if (mCurPosY - mPosY < 0
////                                    && (Math.abs(mCurPosY - mPosY) > 25)) {
////                                //向上滑动
////                            }
//                        if (mCurPosX - mPosX > 0
//                                && (Math.abs(mCurPosX - mPosX) > 3)) {
//                            //向右滑動
////                                ToastUtils.showShort("向右滑动");
//                            rv610StepAdapter.setSelectPosition(mPosition);
//                            return true;
//
//                        } else if (mCurPosX - mPosX < 0
//                                && (Math.abs(mCurPosX - mPosX) > 3)) {
//                            //向左滑动
////                                ToastUtils.showShort("向左滑动");
//                            rv610StepAdapter.setSelectPosition(mPosition);
//                            return true;
//                        }
//                        break;
//                }
//                return false;
//            }
//        });
    }

    @Override
    protected void initData() {
        Bundle bd = getArguments();
        if (bd != null) {
            recipeId = bd.getInt(RECIPEID , -1);
            mDatas = (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
            if (recipeId != -1 ){
                 firstRecipe = LitePal.where("id = ?", recipeId + "").findFirst(RecipeBean.class);
                rv610StepAdapter.addData(firstRecipe.getRecipeStepList());
                recipeName.setText(firstRecipe.getRecipe_names());
//                recipeName.setEnabled(false);
                multiInfo.setText("共" + strings[firstRecipe.getRecipeStepList().size()]+"段 " + firstRecipe.getRecipeStepTimes() + "min");
            }

        }

    }


    @Override
    public void onClick(View view) {
        if (view == btnAutomatic){
            if (StringUtil.isEmpty(recipeName.getText().toString())){
                ToastUtils.showShort("请输入菜谱名称");
                return;
            }
            if (recipeId != -1){
                ContentValues values = new ContentValues();
                values.put("recipe_names", recipeName.getText().toString());
                LitePal.update(RecipeBean.class , values , recipeId) ;
                ToastUtils.showShort("保存成功");
                UIService.getInstance().popBack();
                return;
            }
            String recipeId = Plat.accountService.getCurrentUserId() + DateUtil.getCurrentTime(DateUtil.PATTERN_NO_BLANK);
            RecipeBean d610Recipe = new RecipeBean(recipeId ,recipeName.getText().toString(), Plat.accountService.getCurrentUserId(), "D610", System.currentTimeMillis());
            d610Recipe.save();
            RecipeBean recipeBean = LitePal.where("recipe_id = ?", recipeId).findFirst(RecipeBean.class);
            List<RecipeStepBean> datas = rv610StepAdapter.getData();
            for (RecipeStepBean bean:
                    datas) {
//                bean.setRecipe_id(recipeBean.getRecipe_id());
                bean.setRecipebean(recipeBean);
                bean.save();
            }
            ToastUtils.showShort("保存成功");
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
                 if ("EXP".equals(item.functionName)){
                     expMode( position);
                 }else {
                     modelSelectItemEvent(position);
                 }
                 dialog.mDeviceModelAdapter.setSelectPosition(position);
             }
         });
        dialog.show();
        modelSelectItemEvent(0);
        dialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Dialog_Type_27 dialog27 = (Dialog_Type_27) this.dialog;
        dialog27.setOkBtn(R.string.ok_btn, new Dialog_Type_27.OnItemSelectOkListener() {
            @Override
            public void onOkListener(String temperature, String time ,String temp2) {
                Log.i("D610"  ,  "---------------" + temperature +" ------" + time);
                RecipeStepBean recipeStepBean = new RecipeStepBean();
                recipeStepBean.setFunction_code(functionCode);
                recipeStepBean.setFunction_name(functionName);
                recipeStepBean.setFunction_params(functionParams.toString());
                recipeStepBean.setWork_mode(mode);
                recipeStepBean.setTemperature(Integer.parseInt(temperature));
                recipeStepBean.setTemperature2( StringUtil.isEmpty(temp2) ? 0 :Integer.parseInt(temp2));
                recipeStepBean.setTime(Integer.parseInt(time));
                if (recipeId != -1 ){
//                    recipeStepBean.setRecipe_id(firstRecipe);
                    if (position != -1){
                        recipeStepBean.update(rv610StepAdapter.getItem(position).getId());
//                        rv610StepAdapter.setData(position , recipeStepBean);
                        firstRecipe = LitePal.where("id = ?", recipeId + "").findFirst(RecipeBean.class);
                        rv610StepAdapter.setNewInstance(firstRecipe.getRecipeStepList());
                    }else {
                        recipeStepBean.setRecipebean(firstRecipe);
                        recipeStepBean.save();
                        rv610StepAdapter.addData(recipeStepBean);
                    }
                    firstRecipe = LitePal.where("id = ?", recipeId + "").findFirst(RecipeBean.class);
                    multiInfo.setText("共" + strings[firstRecipe.getRecipeStepList().size()]+"段 " + firstRecipe.getRecipeStepTimes() + "min");
                }else {
                    if (position != -1){
                        rv610StepAdapter.setData(position , recipeStepBean);
                    }else {
                        rv610StepAdapter.addData(recipeStepBean);
                    }
                }
//                firstRecipe = LitePal.where("id = ?", recipeId + "").findFirst(RecipeBean.class);
//                multiInfo.setText("共" + strings[firstRecipe.getRecipeStepList().size()]+"段 " + firstRecipe.getRecipeStepTimes() + "min");
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
                                        Log.i("610" , contentFront );

                                    }
                                }, null, new OnItemSelectedListenerRear() {
                                    @Override
                                    public void onItemSelectedRear(String contentRear) {
                                        Log.i("610" , contentRear );
                                    }
                                });
                        return;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void expMode(int position){
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
                        LogUtils.i("position"  , "-----" + position ) ;
                        List<String> tempDataEXPCentener = HelperRikaData.getTempDataEXPCentener(tempDown, HelperRikaData.getTempData3(tempUp).get(position));
                        dialog.getmLoopViewCenter().setItems(tempDataEXPCentener);
                        dialog.getmLoopViewCenter().setCurrentPosition(0);
//                        if (position >= 20){
//                            dialog.getmLoopViewCenter().setItems(HelperRikaData.getTempData3(tempDown));
//                            dialog.getmLoopViewCenter().setCurrentPosition(position-20);
//                        }else {
//                            dialog.getmLoopViewCenter().setItems(HelperRikaData.getTempData3(tempDown));
//                            dialog.getmLoopViewCenter().setCurrentPosition(0);
//                        }
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
