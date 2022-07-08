package com.robam.roki.ui.page.device.integratedStove;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.hjq.toast.ToastUtils;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.RCReponse;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.robam.common.events.IntegStoveStatusChangedEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.MsgParamsNew;
import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;
import com.robam.common.pojos.device.integratedStove.IntegStoveStatus;
import com.robam.common.pojos.device.integratedStove.IntegratedStoveConstant;
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum;
import com.robam.roki.R;
import com.robam.roki.db.model.RecipeBean;
import com.robam.roki.db.model.RecipeStepBean;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter3.Rv610StepAdapter;
import com.robam.roki.ui.adapter3.RvMultiRecipeAdapter;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.device.steamovenone.MultiAddEditDetailPage;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;
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
    private RvMultiRecipeAdapter rvIntegraRecipeAdapter;

    String guid;
    AbsIntegratedStove integratedStove;
    List<DeviceConfigurationFunctions> mDatas;
    private long userId;
    /**
     * 选中的多段菜谱
     */
//    private RecipeBean recipeBean;
    private Reponses.multiRecipeList recipeBean;
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
        return R.layout.page_multi_integra_recipe_list;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        getTitleBar().setOnTitleBarListener(this);
        recipeName = (EditText) findViewById(R.id.recipe_name);
        rv610Recipe = (RecyclerView) findViewById(R.id.rv_610_recipe);
        btnAutomatic = (AppCompatButton) findViewById(R.id.btn_automatic);
        rv610Recipe.setLayoutManager(new LinearLayoutManager(cx, RecyclerView.VERTICAL, false));
        rvIntegraRecipeAdapter = new RvMultiRecipeAdapter();
        rv610Recipe.setAdapter(rvIntegraRecipeAdapter);
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
        rvIntegraRecipeAdapter.addChildClickViewIds(R.id.tv_right, R.id.tv_del);
        rvIntegraRecipeAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.tv_right) {

                    Bundle bundle = getArguments();
                    bundle.putInt(MultiAddEditDetailPage.RECIPEID, (int) rvIntegraRecipeAdapter.getItem(position).id);
                    bundle.putSerializable("Item", rvIntegraRecipeAdapter.getItem(position));
                    UIService.getInstance().postPage(PageKey.MultiAddEditDetailPage, bundle);
                } else if (view.getId() == R.id.tv_del) {
                    deleteMultiRecipe((int) rvIntegraRecipeAdapter.getItem(position).id, position);
                }
            }
        });
        rvIntegraRecipeAdapter.addChildLongClickViewIds(R.id.stb_step_top);
        rvIntegraRecipeAdapter.addOnLeftTouchListener(new Rv610StepAdapter.OnLeftTouchListener() {
            @Override
            public void onLeftTouch(int position) {
                rvIntegraRecipeAdapter.setDelPosition(position);
            }
        });

        rvIntegraRecipeAdapter.addOnSelectListener(new RvMultiRecipeAdapter.OnSelectListener() {

            @Override
            public void onSelect(int position) {
                recipeBean = rvIntegraRecipeAdapter.getItem(position - 1);
                rvIntegraRecipeAdapter.setSelectPosition(position);
                if (rvIntegraRecipeAdapter.getSelectPosition() != -1) {
                    btnAutomatic.setEnabled(true);
                } else {
                    btnAutomatic.setEnabled(false);
                }
            }
        });
        setOnClickListener(btnAutomatic);
        btnAutomatic.setEnabled(false);

        View head_view = LinearLayout.inflate(cx, R.layout.head_multi_recipe, null);
        rvIntegraRecipeAdapter.addHeaderView(head_view);
        head_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getArguments();
                bundle.putInt(MultiAddEditDetailPage.RECIPEID, -1);
                UIService.getInstance().postPage(PageKey.MultiAddEditDetailPage, bundle);
            }
        });
    }

    String needDescalingParams;
    @Override
    protected void initData() {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        integratedStove = Plat.deviceService.lookupChild(guid);
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);

        needDescalingParams = bd == null ? null : bd.getString(PageArgumentKey.descaling);
        userId = Plat.accountService.getCurrentUserId();

//        List<RecipeBean> recipeBeans = LitePal.where("device = ? and  user_id = ?", integratedStove.getDispalyType(), Plat.accountService.getCurrentUserId() + "").find(RecipeBean.class);
//        rvIntegraRecipeAdapter.addData(recipeBeans);
        getRecipeData();
    }

    @Subscribe
    public void onEvent(PageBackEvent event) {
        if ("MultiAddEditDetailPage".equals(event.getPageName())) {
//            List<RecipeBean> recipeBeans = LitePal.where("device = ? and  user_id = ?", integratedStove.getDispalyType(), Plat.accountService.getCurrentUserId() + "").find(RecipeBean.class);
//            rvIntegraRecipeAdapter.setNewInstance(recipeBeans);
//            rvIntegraRecipeAdapter.setSelectPosition(-1);
//            btnAutomatic.setEnabled(false);
            getRecipeData();
        }
    }

    @Override
    public void onRightClick(View view) {
//        getArguments().putInt(Multi610EditDetailPage.RECIPEID, -1);
//        UIService.getInstance().postPage(PageKey.MultiEditDetailPage, getArguments());
    }

    @Override
    public void onClick(View view) {
        if (view == btnAutomatic) {
            setSteamOvenOneMultiStepMode();
        }
    }

    private void searchRecipe(String recipeName) {
//        if (StringUtil.isEmpty(recipeName)) {
//            List<RecipeBean> recipeBeans = LitePal.where("device = ? and  user_id = ?", integratedStove.getDispalyType(), Plat.accountService.getCurrentUserId() + "").find(RecipeBean.class);
//            rvIntegraRecipeAdapter.setNewInstance(recipeBeans);
//        } else {
//            List<RecipeBean> recipeBeans = LitePal.where("device = ? and  user_id = ? and recipe_names like ?", integratedStove.getDispalyType(), Plat.accountService.getCurrentUserId() + "", "%" + recipeName + "%").find(RecipeBean.class);
//            rvIntegraRecipeAdapter.setNewInstance(recipeBeans);
//        }
    }

    IRokiDialog dialogByType;
    private void descalingDialog() {
        if (needDescalingParams==null){
            return;
        }
        if (dialogByType == null) {
            dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_02);
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
    private void setSteamOvenOneMultiStepMode() {
        if ( SteamOvenHelper.isWork(integratedStove.workState) ) {
            ToastUtils.show("设备已占用");
            return;
        }
        if ( !SteamOvenHelper.isDoorState(integratedStove.doorState) ) {
            ToastUtils.show("门未关好，请检查并确认关好门");
            return;
        }
        List<Reponses.saveMultiRecipeList> recipeStepList = recipeBean.multiStepDtoList;
        //多段是否含有需要用水箱的
        boolean isWater = false ;
        for (Reponses.saveMultiRecipeList step :
                recipeStepList) {
            if (SteamOvenHelper.isWater(SteamOvenModeEnum.match(Integer.parseInt(step.modelCode))) ){
                isWater = true ;
            }
        }
        if (isWater
        ) {
            if (SteamOvenHelper.isDescale(integratedStove.descaleFlag)) {
//                ToastUtils.show("设备需要除垢后才能继续工作，请先除垢");

                descalingDialog();
                return;
            }
            if (!SteamOvenHelper.isWaterBoxState(integratedStove.waterBoxState)){
                ToastUtils.show("水箱已弹出，请检查水箱状态");
                return;
            }
            if (!SteamOvenHelper.isWaterLevelState(integratedStove.waterLevelState)){
                ToastUtils.show("水箱缺水，请加水");
                return;
            }
        }
        if (recipeStepList != null && recipeStepList.size() != 0) {
            try {
                Msg msg = integratedStove.newReqMsg(MsgKeys.setDeviceAttribute_Req);
                msg.putOpt(MsgParams.TerminalType, integratedStove.terminalType);
                msg.putOpt(UserId, integratedStove.getSrcUser());
                msg.putOpt(MsgParams.categoryCode , IntegratedStoveConstant.STEAME_OVEN_ONE);
                //多加一个参数 需要把菜谱id置为0
                msg.putOpt(ArgumentNumber, 3 + recipeStepList.size()*4 + 1);
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
                    msg.putOpt(MsgParamsNew.mode + i, bean.modelCode) ;
                    //温度上温度
                    msg.putOpt(MsgParamsNew.setUpTempKey + i  , 102 + i *10 );
                    msg.putOpt(MsgParamsNew.setUpTempLength + i, 1);
                    msg.putOpt(MsgParamsNew.setUpTemp + i ,bean.temperature);
                    //时间
                    msg.putOpt(MsgParamsNew.setTimeKey + i , 104 + i *10 );
                    msg.putOpt(MsgParamsNew.setTimeLength + i, 1);
                    msg.putOpt(MsgParamsNew.setTime + i, bean.getTime(integratedStove.getGuid().getGuid()));
                    //蒸汽量
                    msg.putOpt(MsgParamsNew.steamKey + i, 106 + i *10 );
                    msg.putOpt(MsgParamsNew.steamLength + i , 1);
                    msg.putOpt(MsgParamsNew.steam + i, bean.steamQuantity);
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


    private void getRecipeData() {
        RokiRestHelper.getMultiRecipe(userId + "", 0, 100, integratedStove.getDt() ,new Callback<Reponses.MultiRecipeResponse>() {
            @Override
            public void onSuccess(Reponses.MultiRecipeResponse recipes) {
                if (recipes.datas != null && recipes.datas.size() > 0) {
                    rvIntegraRecipeAdapter.getData().clear();
                    rvIntegraRecipeAdapter.addData(recipes.datas);
                    rvIntegraRecipeAdapter.setSelectPosition(-1);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void deleteMultiRecipe(int id, int position) {
        RokiRestHelper.deleteMultiRecipe(id,  new Callback<RCReponse>() {
            @Override
            public void onSuccess(RCReponse recipes) {
                com.hjq.toast.ToastUtils.show("删除成功");
                rvIntegraRecipeAdapter.removeAt(position);
                rvIntegraRecipeAdapter.setDelPosition(-1);
            }

            @Override
            public void onFailure(Throwable t) {
                com.hjq.toast.ToastUtils.show("删除失败");
            }
        });
    }


}
