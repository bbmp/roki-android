package com.robam.roki.ui.page.device.integratedStove;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.IntegStoveStatusChangedEvent;
import com.robam.common.events.SteamOvenOpenDoorSteamEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.MsgParamsNew;
import com.robam.common.io.device.TerminalType;
import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;
import com.robam.common.pojos.device.integratedStove.IntegStoveStatus;
import com.robam.common.pojos.device.integratedStove.IntegratedStoveConstant;
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.roki.R;
import com.robam.roki.db.model.RecipeBean;
import com.robam.roki.db.model.RecipeStepBean;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter3.Rv610RecipeAdapter;
import com.robam.roki.ui.adapter3.Rv610StepAdapter;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.device.steamovenone.Multi610EditDetailPage;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.utils.StringUtil;

import org.json.JSONException;
import org.litepal.LitePal;

import java.util.List;

import static com.robam.common.io.device.MsgParams.ArgumentNumber;
import static com.robam.common.io.device.MsgParams.UserId;

/**
 * @author r210190
 * 多段菜谱
 */
public class MultiRecipePage extends MyBasePage<MainActivity> {
    /**
     * 菜名搜索
     */
    private EditText recipeName;
    /**
     * 菜谱adapter
     */
    private RecyclerView rv610Recipe;
    /**
     * 开始
     */
    private AppCompatButton btnAutomatic;
    /**
     * 菜谱adapter
     */
    private Rv610RecipeAdapter rv610RecipeAdapter;

    String guid;
    AbsIntegratedStove integratedStove;
    List<DeviceConfigurationFunctions> mDatas;
    private long userId;
    /**
     * 选中的多段菜谱
     */
    private RecipeBean recipeBean;
    private int mPosition;

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
                UIService.getInstance().popBack();
                UIService.getInstance().postPage(PageKey.IntegratedStoveWorkPage, getArguments());
            }
        }

    }
    @Override
    protected int getLayoutId() {
        return R.layout.page_multi_610_recipe_list;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        getTitleBar().setOnTitleBarListener(this);
        recipeName = (EditText) findViewById(R.id.recipe_name);
        rv610Recipe = (RecyclerView) findViewById(R.id.rv_610_recipe);
        btnAutomatic = (AppCompatButton) findViewById(R.id.btn_automatic);
        rv610Recipe.setLayoutManager(new LinearLayoutManager(cx, RecyclerView.VERTICAL, false));
        rv610RecipeAdapter = new Rv610RecipeAdapter();
        rv610Recipe.setAdapter(rv610RecipeAdapter);
//        rv610RecipeAdapter.addChildClickViewIds(R.id.stb_step_top,R.id.tv_del );
        recipeName.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
                | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

        );
        recipeName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    searchRecipe(v.getText().toString());
                }
                return false;
            }
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
                    searchRecipe(s.toString());
                }
            }
        });
        rv610RecipeAdapter.addChildClickViewIds(R.id.stb_step_top, R.id.tv_del);
        rv610RecipeAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.stb_step_top) {
                    RecipeBean item = rv610RecipeAdapter.getItem(position);
                    int recipeId = item.getId();
                    Bundle bundle = getArguments();
                    bundle.putInt(MultiEditDetailPage.RECIPEID , recipeId);
                    UIService.getInstance().postPage(PageKey.MultiEditDetailPage , bundle);
                } else if (view.getId() == R.id.tv_del) {
                    LitePal.delete(RecipeBean.class, rv610RecipeAdapter.getItem(position).getId());
                    rv610RecipeAdapter.removeAt(position);
                    rv610RecipeAdapter.setDelPosition(-1);
                }
            }
        });
        rv610RecipeAdapter.addChildLongClickViewIds(R.id.stb_step_top);
        rv610RecipeAdapter.addOnLeftTouchListener(new Rv610StepAdapter.OnLeftTouchListener() {
            @Override
            public void onLeftTouch(int position) {
                rv610RecipeAdapter.setDelPosition(position);
            }
        });
        rv610RecipeAdapter.addOnSelectListener(new Rv610RecipeAdapter.OnSelectListener() {

            @Override
            public void onSelect(int position) {
                recipeBean = rv610RecipeAdapter.getItem(position) ;
                rv610RecipeAdapter.setSelectPosition(position);
                if (rv610RecipeAdapter.getSelectPosition() != -1){
                    btnAutomatic.setEnabled(true);
                }else {
                    btnAutomatic.setEnabled(false);
                }
            }
        });
        setOnClickListener(btnAutomatic);
        btnAutomatic.setEnabled(false);

    }

    @Override
    protected void initData() {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        integratedStove = Plat.deviceService.lookupChild(guid);
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
//        needDescalingParams = bd == null ? null : bd.getString(PageArgumentKey.descaling);
        userId = Plat.accountService.getCurrentUserId();

        List<RecipeBean> recipeBeans = LitePal.where("device = ? and  user_id = ?", integratedStove.getDispalyType(), Plat.accountService.getCurrentUserId() + "").find(RecipeBean.class);
        rv610RecipeAdapter.addData(recipeBeans);

    }

    @Subscribe
    public void onEvent(PageBackEvent event) {
        if ("Multi610EditDetailPage".equals(event.getPageName())) {
            List<RecipeBean> recipeBeans = LitePal.where("device = ? and  user_id = ?", integratedStove.getDispalyType(), Plat.accountService.getCurrentUserId() + "").find(RecipeBean.class);
            rv610RecipeAdapter.setNewInstance(recipeBeans);
            rv610RecipeAdapter.setSelectPosition(-1);
            btnAutomatic.setEnabled(false);
        }
    }

    @Override
    public void onRightClick(View view) {
        getArguments().putInt(Multi610EditDetailPage.RECIPEID, -1);
        UIService.getInstance().postPage(PageKey.MultiEditDetailPage, getArguments());
    }

    @Override
    public void onClick(View view) {
        if (view == btnAutomatic) {
            setSteamOvenOneMultiStepMode();
        }
    }

    private void searchRecipe(String recipeName) {
        if (StringUtil.isEmpty(recipeName)) {
            List<RecipeBean> recipeBeans = LitePal.where("device = ? and  user_id = ?", integratedStove.getDispalyType(), Plat.accountService.getCurrentUserId() + "").find(RecipeBean.class);
            rv610RecipeAdapter.setNewInstance(recipeBeans);
        } else {
            List<RecipeBean> recipeBeans = LitePal.where("device = ? and  user_id = ? and recipe_names like ?", integratedStove.getDispalyType(), Plat.accountService.getCurrentUserId() + "", "%" + recipeName + "%").find(RecipeBean.class);
            rv610RecipeAdapter.setNewInstance(recipeBeans);
        }
    }


    private void setSteamOvenOneMultiStepMode() {
        if ( SteamOvenHelper.isWork(integratedStove.workState) ) {
            ToastUtils.showShort("设备已占用");
            return;
        }
        if ( !SteamOvenHelper.isDoorState(integratedStove.doorState) ) {
            ToastUtils.showShort("门未关好，请检查并确认关好门");
            return;
        }
        List<RecipeStepBean> recipeStepList = recipeBean.getRecipeStepList();
        //多段是否含有需要用水箱的
        boolean isWater = false ;
        for (RecipeStepBean step :
                recipeStepList) {
            if (SteamOvenHelper.isWater(SteamOvenModeEnum.match(step.getWork_mode())) ){
                isWater = true ;
            }
        }
        if (isWater
        ) {
            if (SteamOvenHelper.isDescale(integratedStove.descaleFlag)) {
                ToastUtils.showShort("设备需要除垢后才能继续工作，请先除垢");
                return;
            }
            if (!SteamOvenHelper.isWaterBoxState(integratedStove.waterBoxState)){
                ToastUtils.showShort("水箱已弹出，请检查水箱状态");
                return;
            }
            if (!SteamOvenHelper.isWaterLevelState(integratedStove.waterLevelState)){
                ToastUtils.showShort("水箱缺水，请加水");
                return;
            }
        }
        if (recipeStepList != null && recipeStepList.size() != 0) {
            try {
                Msg msg = integratedStove.newReqMsg(MsgKeys.setDeviceAttribute_Req);
                msg.putOpt(MsgParams.TerminalType, integratedStove.terminalType);
                msg.putOpt(UserId, integratedStove.getSrcUser());
                msg.putOpt(MsgParams.categoryCode , IntegratedStoveConstant.STEAME_OVEN_ONE);
                msg.putOpt(ArgumentNumber, 3 + recipeStepList.size()*4);
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
                    RecipeStepBean bean = recipeStepList.get(i);
                    //模式
                    msg.putOpt(MsgParamsNew.modeKey + i, 101 + i *10  ) ;
                    msg.putOpt(MsgParamsNew.modeLength + i, 1) ;
                    msg.putOpt(MsgParamsNew.mode + i, bean.getWork_mode()) ;
                    //温度上温度
                    msg.putOpt(MsgParamsNew.setUpTempKey + i  , 102 + i *10 );
                    msg.putOpt(MsgParamsNew.setUpTempLength + i, 1);
                    msg.putOpt(MsgParamsNew.setUpTemp + i ,bean.getTemperature());
                    //时间
                    msg.putOpt(MsgParamsNew.setTimeKey + i , 104 + i *10 );
                    msg.putOpt(MsgParamsNew.setTimeLength + i, 1);
                    msg.putOpt(MsgParamsNew.setTime + i, bean.getTime());
                    //蒸汽量
                    msg.putOpt(MsgParamsNew.steamKey + i, 106 + i *10 );
                    msg.putOpt(MsgParamsNew.steamLength + i , 1);
                    msg.putOpt(MsgParamsNew.steam + i, bean.getSteam_flow());
                }

                integratedStove.setSteamOvenOneMultiStepMode(msg, new VoidCallback() {
                    @Override
                    public void onSuccess() {
//                        UIService.getInstance().popBack();
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

    @Subscribe
    public void onEvent(SteamOvenOpenDoorSteamEvent event) {
//        if (steameOvenOne == null || !Objects.equal(steameOvenOne.getID(), event.pojo.getID()))
//            return;
//        steameOvenOne = (AbsSteameOvenOne) event.pojo;
    }
}
