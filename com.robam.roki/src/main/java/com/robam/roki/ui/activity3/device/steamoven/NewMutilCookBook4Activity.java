package com.robam.roki.ui.activity3.device.steamoven;

import static com.robam.common.io.device.MsgParams.ArgumentNumber;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.RCReponse;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamOvenOpenDoorSteamEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.Requests;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.MsgParamsNew;
import com.robam.common.io.device.TerminalType;
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.roki.R;
import com.robam.roki.db.model.RecipeBean;
import com.robam.roki.db.model.RecipeStepBean;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.activity3.device.steamoven.adapter.RvMultiRecipeNewAdapter;
import com.robam.roki.ui.adapter3.Rv610StepAdapter;
import com.robam.roki.ui.page.device.integratedStove.SteamOvenHelper;
import com.robam.roki.ui.page.device.steamovenone.MultiAddEditDetailPage;
import com.robam.roki.utils.DialogUtil;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * MultiListRecipePage
 */
public class NewMutilCookBook4Activity extends DeviceBaseFuntionActivity {
    /**
     * 菜名搜索
     */
    private EditText recipeName;
    /**
     * 菜谱adapter
     */
    private SwipeMenuRecyclerView rvSteamRecipe;
    /**
     * 开始
     */
    private AppCompatButton btnAutomatic;
    /**
     * 菜谱adapter
     */
    private RvMultiRecipeNewAdapter rvMultiRecipeAdapter;

    String guid;
    AbsSteameOvenOne steameOvenOne;
    List<DeviceConfigurationFunctions> mDatas;
    private long userId;

    /**
     * 选中的多段菜谱
     */
//    private RecipeBean recipeBean;
    private Reponses.multiRecipeList recipeBean;

    String needDescalingParams;
    IRokiDialog dialogByType;
    private void descalingDialog() {
        if (dialogByType == null) {
            dialogByType = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_02);
        }
        if (dialogByType.isShow()) {
            return;
        }
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
        dialogByType.setTitleText(descalingTitle);
        dialogByType.setContentText(descalingContent);
        final IRokiDialog finalDialogByType = dialogByType;
        dialogByType.setOkBtn(descalingButton, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalDialogByType != null) {
                    finalDialogByType.dismiss();
                }
            }
        });
        dialogByType.show();
    }
    @Override
    protected int getLayoutId() {
        return  R.layout.activity_new_mutil_cook_book4;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        recipeName = (EditText) findViewById(R.id.recipe_name);
        rvSteamRecipe = (SwipeMenuRecyclerView) findViewById(R.id.rv_mutil_steam_recipe);
        btnAutomatic = (AppCompatButton) findViewById(R.id.btn_automatic);
        rvSteamRecipe.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rvMultiRecipeAdapter = new RvMultiRecipeNewAdapter();
        rvSteamRecipe.setAdapter(rvMultiRecipeAdapter);

        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
        recipeName.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
                | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

        );
        recipeName.setOnEditorActionListener((v, actionId, event) -> {
            if ((actionId == 0 || actionId == 3) && event != null) {
//                    searchRecipe(v.getText().toString());
            }
            return false;
        });
        recipeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
//                    searchRecipe(s.toString());
                }
            }
        });
        rvMultiRecipeAdapter.addChildClickViewIds(R.id.tv_right, R.id.tv_del);
        rvMultiRecipeAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.tv_right) {
//                    Reponses.multiRecipeList item = rvMultiRecipeAdapter.getItem(position);
//                    int recipeId =(int) item.id;
                    Bundle bundle = getArguments();
//                    Bundle bundle = new Bundle();
                    bundle.putInt(MultiAddEditDetailPage.RECIPEID, (int) rvMultiRecipeAdapter.getItem(position).id);
                    bundle.putSerializable("Item", rvMultiRecipeAdapter.getItem(position));
//                    UIService.getInstance().postPage(PageKey.MultiAddEditDetailPage, bundle);
                    Intent intent=new Intent(NewMutilCookBook4Activity.this, AddMutilEditCookBookActivity.class);
                    intent.putExtra(DeviceBaseFuntionActivity.BUNDLE,bundle);
                    startActivity(intent);
                } else if (view.getId() == R.id.tv_del) {
                    deleteMultiRecipe((int) rvMultiRecipeAdapter.getItem(position).id, position);
                }
            }
        });
        rvMultiRecipeAdapter.addChildLongClickViewIds(R.id.stb_step_top);

        rvMultiRecipeAdapter.addOnLeftTouchListener(new Rv610StepAdapter.OnLeftTouchListener() {
            @Override
            public void onLeftTouch(int position) {
                rvMultiRecipeAdapter.setDelPosition(position);
            }
        });

        rvMultiRecipeAdapter.addOnSelectListener(new RvMultiRecipeNewAdapter.OnSelectListener() {

            @Override
            public void onSelect(int position) {
                recipeBean = rvMultiRecipeAdapter.getItem(position - 1);
                rvMultiRecipeAdapter.setSelectPosition(position);
                if (rvMultiRecipeAdapter.getSelectPosition() != -1) {
                    btnAutomatic.setEnabled(true);
                } else {
                    btnAutomatic.setEnabled(false);
                }
            }
        });
        setOnClickListener(btnAutomatic);
        btnAutomatic.setEnabled(false);
        View head_view = LinearLayout.inflate(getContext(), R.layout.head_multi_new_recipe, null);
        rvMultiRecipeAdapter.addHeaderView(head_view);
        head_view.setOnClickListener(v -> {
            Bundle bundle = getArguments();
            bundle.putInt(MultiAddEditDetailPage.RECIPEID, -1);
            Intent intent=new Intent(NewMutilCookBook4Activity.this, AddMutilEditCookBookActivity.class);
            intent.putExtra(DeviceBaseFuntionActivity.BUNDLE,bundle);
            startActivity(intent);
//                UIService.getInstance().postPage(PageKey.MultiAddEditDetailPage, bundle);
        });
    }

    private Bundle getArguments(){
        Bundle bundle=getIntent().getBundleExtra(DeviceBaseFuntionActivity.BUNDLE);
        return bundle;
    }

    String title;
    @Override
    protected void initData() {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        steameOvenOne = Plat.deviceService.lookupChild(guid);
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
        needDescalingParams= bd == null ? null : bd.getString(PageArgumentKey.descaling);
        List<RecipeBean> recipeBeans = LitePal.where("device = ? and  user_id = ?", "D610", Plat.accountService.getCurrentUserId() + "").find(RecipeBean.class);
        LogUtils.i("20220407","共："+recipeBeans.size()+"道多段菜谱");

        if (recipeBeans != null && recipeBeans.size() > 0) {
            getLocalRecipe(recipeBeans);
        }

        userId = Plat.accountService.getCurrentUserId();
        getRecipeData();
    }

    @Subscribe
    public void onEvent(PageBackEvent event) {
        if ("MultiAddEditDetailPage".equals(event.getPageName())) {
            getRecipeData();
            btnAutomatic.setEnabled(false);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == btnAutomatic) {

            if (steameOvenOne instanceof AbsSteameOvenOneNew){
                setSteamOvenOneMultiStepMode620((AbsSteameOvenOneNew)steameOvenOne);
            }else {
                steam_on();
            }
        }
    }

    private void setSteamOvenOneMultiStepMode620(AbsSteameOvenOneNew steameOvenOne ) {

        if (!SteamOvenHelper.isDoorState(steameOvenOne.doorState)){
            com.hjq.toast.ToastUtils.show("门未关好，请检查并确认关好门");
            return;

        }

        List<Reponses.saveMultiRecipeList> recipeStepList = recipeBean.multiStepDtoList;
        if (recipeStepList==null){
            ToastUtils.show("多段添加不能少于2步", Toast.LENGTH_LONG);
            return;
        }
        //多段是否含有需要用水箱的

        if (recipeStepList.size()<=1){
            ToastUtils.show("多段添加不能少于2步",Toast.LENGTH_LONG);
            return;
        }
        boolean isWater = false ;
        for (Reponses.saveMultiRecipeList step :
                recipeStepList) {
            if (SteamOvenHelper.isWater(SteamOvenModeEnum.match(Integer.parseInt(step.modelCode)))) {
                isWater = true ;
            }
        }
        if (isWater) {
            if (SteamOvenHelper.isDescale(steameOvenOne.descaleFlag)) {
//                com.hjq.toast.ToastUtils.show("设备需要除垢后才能继续工作，请先除垢");

                descalingDialog();
                return ;
            }
            if (!SteamOvenHelper.isWaterBoxState(steameOvenOne.waterBoxState)) {
                com.hjq.toast.ToastUtils.show("水箱已弹出，请检查水箱状态");
                return ;
            }
            if (!SteamOvenHelper.isWaterLevelState(steameOvenOne.waterLevelState)) {
                com.hjq.toast.ToastUtils.show("水箱缺水，请加水");
                return  ;
            }
        }
        if (recipeStepList != null && recipeStepList.size() != 0) {
            try {
                Msg msg = steameOvenOne.newReqMsg(MsgKeys.setDeviceAttribute_Req);
//                msg.putOpt(MsgParams.TerminalType, steameOvenOne.terminalType);
//                msg.putOpt(UserId, steameOvenOne.getSrcUser());
//                msg.putOpt(MsgParams.categoryCode , IntegratedStoveConstant.STEAME_OVEN_ONE);
                msg.putOpt(ArgumentNumber, recipeStepList.size()*4+3);
                msg.putOpt(MsgParamsNew.type , 2) ;
                //一体机电源控制
                msg.putOpt(MsgParamsNew.powerCtrlKey, 2);
                msg.putOpt(MsgParamsNew.powerCtrlLength, 1);
                msg.putOpt(MsgParamsNew.powerCtrl, 1);
                //一体机工作控制
                msg.putOpt(MsgParamsNew.workCtrlKey, 4);
                msg.putOpt(MsgParamsNew.workCtrlLength, 1);
                msg.putOpt(MsgParamsNew.workCtrl, 1);
                //预约时间
//                msg.putOpt(MsgParamsNew.setOrderMinutesKey, 5);
//                msg.putOpt(MsgParamsNew.setOrderMinutesLength, 1);
//                msg.putOpt(MsgParamsNew.setOrderMinutes, 0);
                //段数
                msg.putOpt(MsgParamsNew.sectionNumberKey, 100) ;
                msg.putOpt(MsgParamsNew.sectionNumberLength, 1) ;
                msg.putOpt(MsgParamsNew.sectionNumber, recipeStepList.size() ) ;
//                msg.putOpt(MsgParamsNew.sectionNumber, recipeStepList.size() ) ;
                for (int i = 0; i < recipeStepList.size(); i++) {
                    Reponses.saveMultiRecipeList bean = recipeStepList.get(i);
                    //模式
                    msg.putOpt(MsgParamsNew.modeKey + i, 101 + i *10  ) ;
                    msg.putOpt(MsgParamsNew.modeLength + i, 1) ;
                    msg.putOpt(MsgParamsNew.mode + i,Integer.parseInt( bean.modelCode)) ;
                    //温度上温度
                    msg.putOpt(MsgParamsNew.setUpTempKey + i  , 102 + i *10 );
                    msg.putOpt(MsgParamsNew.setUpTempLength + i, 1);
                    msg.putOpt(MsgParamsNew.setUpTemp + i ,Integer.parseInt(bean.temperature));
                    //时间
//                    int time =Integer.parseInt(bean.time)*60;
                    int time =bean.getTime(guid) * 60;
                    msg.putOpt(MsgParamsNew.setTimeKey + i , 104 + i *10 );
                    msg.putOpt(MsgParamsNew.setTimeLength + i, 1);
                    short lowTime = time > 255 ? (short) (time & 0Xff):(short)time;
//                    final short lowTime = time > 255 ? (short) (time & 0Xff):(short)time;
                    if (time<=255){
                        msg.putOpt(MsgParamsNew.setTime0b+i, lowTime);
                    }else{
                        msg.putOpt(MsgParamsNew.setTimeKey+i, 104 + i *10);
                        msg.putOpt(MsgParamsNew.setTimeLength+i, 2);
                        short ltime = (short)(time & 0xff);
                        msg.putOpt(MsgParamsNew.setTime0b+i, ltime);
                        short htime = (short) ((time >> 8) & 0Xff);
                        msg.putOpt(MsgParamsNew.setTime1b+i, htime);
                    }
//                    msg.putOpt(MsgParamsNew.setTime + i, bean.getTime()*60);
                    //蒸汽量
                    msg.putOpt(MsgParamsNew.steamKey + i, 106 + i *10 );
                    msg.putOpt(MsgParamsNew.steamLength + i , 1);
                    msg.putOpt(MsgParamsNew.steam + i, Integer.parseInt(bean.steamQuantity));
                }


                steameOvenOne.setSteamOvenOneMultiStepMode(msg, new VoidCallback() {
                    @Override
                    public void onSuccess() {
//                        UIService.getInstance().popBack();
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }





    }

    private void steam_on() {
        if (steameOvenOne.powerState == SteamOvenOnePowerStatus.Off ||
                steameOvenOne.powerState == SteamOvenOnePowerStatus.Wait
        ) {
            steameOvenOne.setSteameOvenStatus_on(new VoidCallback() {
                @Override
                public void onSuccess() {
                    setSteamOvenOneMultiStepMode();
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("20200319", "开机：" + t.getMessage());
                }
            });
        } else {
            setSteamOvenOneMultiStepMode();

        }
    }

    private void setSteamOvenOneMultiStepMode() {
        if (steameOvenOne.doorStatusValue == 1) {
            ToastUtils.show("门未关好，请检查并确认关好门", Toast.LENGTH_SHORT);
            return;
        }
        List<Reponses.saveMultiRecipeList> recipeStepList = recipeBean.multiStepDtoList;
        boolean isWater = false; //多段是否含有需要用水箱的

        for (Reponses.saveMultiRecipeList step :
                recipeStepList) {
            int workMode = Integer.parseInt(step.modelCode);
            if (workMode == 13
                    || workMode == 14
                    || workMode == 15
                    || workMode == 16
                    || workMode == 20
                    || workMode == 21
                    || workMode == 22
                    || workMode == 17
                    || workMode == 18
            ) {
                isWater = true;
            }
        }
        if (isWater
        ) {
            if (steameOvenOne.WaterStatus == 1) {
                ToastUtils.showShort(R.string.device_alarm_water_out);
                return;
            }
            if (steameOvenOne.alarm == 16) {
                ToastUtils.show("水箱缺水，请加水", Toast.LENGTH_SHORT);
                return;
            }
        }
        if (recipeStepList != null && recipeStepList.size() != 0) {
            try {
                Msg msg = steameOvenOne.newReqMsg(MsgKeys.setSteameOvenAutoRecipeMode610_Req);
                msg.putOpt(MsgParams.TerminalType, TerminalType.getType());
                msg.putOpt(MsgParams.UserId, steameOvenOne.getSrcUser(userId));
                msg.putOpt(MsgParams.ArgumentNumber, recipeStepList.size() + 1);

                msg.putOpt(MsgParams.steameOvenTotalNumberOfSegments_Key, 2);//总段数 key
                msg.putOpt(MsgParams.steameOvenTotalNumberOfSegments_Length, 1);//总段数 length
                msg.putOpt(MsgParams.steameOvenTotalNumberOfSegments_Value, recipeStepList.size());

                for (int i = 1; i <= recipeBean.multiStepDtoList.size(); i++) {
                    msg.putOpt(MsgParams.SteameOvenSectionOfTheStep_Key + i, 2 + i);//段步骤 key
                    msg.putOpt(MsgParams.SteameOvenSectionOfTheStep_Length + i, 9);//段步骤 value
                    msg.putOpt(MsgParams.SteameOvenMode + i, recipeStepList.get(i - 1).modelCode);//第一段 mode
                    msg.putOpt(MsgParams.SteameOvenTemp + i, recipeStepList.get(i - 1).temperature);
                    msg.putOpt(MsgParams.SteameOvenTime + i, recipeStepList.get(i - 1).getTime(guid));
                    msg.putOpt(MsgParams.SteameOvenTemp2 + i, recipeStepList.get(i - 1).downTemperature);//
                    msg.putOpt(MsgParams.SteameOvenTime2 + i, recipeStepList.get(i - 1).getTime(guid));//
                }
                steameOvenOne.setSteamOvenOneMultiStepMode(msg, new VoidCallback() {
                    @Override
                    public void onSuccess() {
//                        UIService.getInstance().popBack();
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showShort(t.getMessage());
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Subscribe
    public void onEvent(SteamOvenOpenDoorSteamEvent event) {
        if (steameOvenOne == null || !Objects.equal(steameOvenOne.getID(), event.pojo.getID()))
            return;
        steameOvenOne = (AbsSteameOvenOne) event.pojo;
    }

    private void getRecipeData() {
        RokiRestHelper.getMultiRecipe(userId + "", 0, 100, steameOvenOne.getDt() ,new Callback<Reponses.MultiRecipeResponse>() {
            @Override
            public void onSuccess(Reponses.MultiRecipeResponse recipes) {
                if (recipes.datas != null && recipes.datas.size() > 0) {
                    rvMultiRecipeAdapter.getData().clear();
                    rvMultiRecipeAdapter.addData(recipes.datas);
                    rvMultiRecipeAdapter.setSelectPosition(-1);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void deleteMultiRecipe(int id, int position) {
        RokiRestHelper.deleteMultiRecipe(id, new Callback<RCReponse>() {
            @Override
            public void onSuccess(RCReponse recipes) {
                com.hjq.toast.ToastUtils.show("删除成功");
                rvMultiRecipeAdapter.removeAt(position);
                rvMultiRecipeAdapter.setDelPosition(-1);
            }

            @Override
            public void onFailure(Throwable t) {
                com.hjq.toast.ToastUtils.show("删除失败");
            }
        });
    }

    private void getLocalRecipe(List<RecipeBean> recipeBeans) {
        for (int i = 0; i < recipeBeans.size(); i++) {
            recipeBeans.get(i).getRecipe_id();
            List<RecipeStepBean> RecipeStepBeans = LitePal.where("recipeBean_id = ?", recipeBeans.get(i).getId() + "").find(RecipeStepBean.class);
//            int times = LitePal.where("recipeBean_id = ?", recipeBeans.get(i).getId() + "").sum(RecipeStepBean.class, "time", int.class);
            saveMultiRecipe(RecipeStepBeans, recipeBeans.get(i).getRecipe_names());
            LogUtils.i("20220407","共："+recipeBeans.size()+"道菜谱，第"+i+"道菜："+recipeBeans.get(i).getRecipe_names());
        }
        LitePal.deleteAll(RecipeBean.class, "device = ? and  user_id = ?", "D610", Plat.accountService.getCurrentUserId() + "");
//        LitePal.deleteAll(RecipeBean.class);
        getRecipeData();
    }

    private void saveMultiRecipe(List<RecipeStepBean> recipeBeans, String name) {
        Requests.saveMultiRecipeRequest reqBody = new Requests.saveMultiRecipeRequest();
        reqBody.userId = Plat.accountService.getCurrentUserId();
        reqBody.name = name;
        reqBody.deviceGuid = guid;
        List<Requests.saveMultiRecipeList> multiStepDtoList = new ArrayList<>();
        for (int i = 0; i < recipeBeans.size(); i++) {
            Requests.saveMultiRecipeList item = new Requests.saveMultiRecipeList();
            item.downTemperature = recipeBeans.get(i).getTemperature2() + "";
            item.modelCode = recipeBeans.get(i).getWork_mode() + "";
            item.modelName = recipeBeans.get(i).getFunction_name();
            item.no = i + 1 + "";
            item.steamQuantity = recipeBeans.get(i).getSteam_flow() + "";
            item.temperature = recipeBeans.get(i).getTemperature() + "";
            item.time = recipeBeans.get(i).getTime() + "";
            multiStepDtoList.add(item);
        }
        reqBody.multiStepDtoList = multiStepDtoList;

        RokiRestHelper.saveMultiRecipe(reqBody, new Callback<RCReponse>() {
            @Override
            public void onSuccess(RCReponse recipes) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    protected void dealData() {

    }

}