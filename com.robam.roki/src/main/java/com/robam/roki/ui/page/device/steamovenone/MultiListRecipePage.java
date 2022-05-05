package com.robam.roki.ui.page.device.steamovenone;

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
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.RCReponse;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamOvenOpenDoorSteamEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.Requests;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.TerminalType;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.roki.R;
import com.robam.roki.db.model.RecipeBean;
import com.robam.roki.db.model.RecipeStepBean;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter3.Rv610RecipeAdapter;
import com.robam.roki.ui.adapter3.Rv610StepAdapter;
import com.robam.roki.ui.adapter3.RvMultiRecipeAdapter;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.utils.StringUtil;

import org.json.JSONException;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * 多段菜谱
 */
public class MultiListRecipePage extends MyBasePage<MainActivity> {
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
    private RvMultiRecipeAdapter rvMultiRecipeAdapter;

    String guid;
    AbsSteameOvenOne steameOvenOne;
    List<DeviceConfigurationFunctions> mDatas;
    private long userId;

    /**
     * 选中的多段菜谱
     */
//    private RecipeBean recipeBean;
    private Reponses.multiRecipeList recipeBean;

    @Override
    protected int getLayoutId() {
        return R.layout.page_multi_recipe_list;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        getTitleBar().setOnTitleBarListener(this);
        recipeName = (EditText) findViewById(R.id.recipe_name);
        rv610Recipe = (RecyclerView) findViewById(R.id.rv_610_recipe);
        btnAutomatic = (AppCompatButton) findViewById(R.id.btn_automatic);
        rv610Recipe.setLayoutManager(new LinearLayoutManager(cx, RecyclerView.VERTICAL, false));
        rvMultiRecipeAdapter = new RvMultiRecipeAdapter();
        rv610Recipe.setAdapter(rvMultiRecipeAdapter);

        recipeName.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
                | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

        );
        recipeName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
//                    searchRecipe(v.getText().toString());
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
                    UIService.getInstance().postPage(PageKey.MultiAddEditDetailPage, bundle);
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

        rvMultiRecipeAdapter.addOnSelectListener(new RvMultiRecipeAdapter.OnSelectListener() {

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

        View head_view = LinearLayout.inflate(cx, R.layout.head_multi_recipe, null);
        rvMultiRecipeAdapter.addHeaderView(head_view);
        head_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getArguments();
                bundle.putInt(MultiAddEditDetailPage.RECIPEID, -1);
                UIService.getInstance().postPage(PageKey.MultiAddEditDetailPage, bundle);
            }
        });
    }

    @Override
    protected void initData() {
//        List<RecipeBean> recipeBeans = LitePal.where("device = ? and  user_id = ?", "D610", Plat.accountService.getCurrentUserId() + "").find(RecipeBean.class);
//        if(recipeBeans!=null&&recipeBeans.size()>0){
//            getLocalRecipe(recipeBeans);
//        }
//        rvMultiRecipeAdapter.addData(recipeBeans);
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        steameOvenOne = Plat.deviceService.lookupChild(guid);
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);

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
            steam_on();
        }
    }

    private void steam_on() {
        if (steameOvenOne.powerStatus == SteamOvenOnePowerStatus.Off ||
                steameOvenOne.powerStatus == SteamOvenOnePowerStatus.Wait
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
                    msg.putOpt(MsgParams.SteameOvenTime + i, recipeStepList.get(i - 1).time);
                    msg.putOpt(MsgParams.SteameOvenTemp2 + i, recipeStepList.get(i - 1).downTemperature);//
                    msg.putOpt(MsgParams.SteameOvenTime2 + i, recipeStepList.get(i - 1).time);//
                }
                steameOvenOne.setSteamOvenOneMultiStepMode(msg, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        UIService.getInstance().popBack();
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
        RokiRestHelper.getMultiRecipe(userId + "", 0, 100, new Callback<Reponses.MultiRecipeResponse>() {
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
        RokiRestHelper.deleteMultiRecipe(id,  new Callback<RCReponse>() {
            @Override
            public void onSuccess(RCReponse recipes) {
                ToastUtils.show("删除成功");
                rvMultiRecipeAdapter.removeAt(position);
                rvMultiRecipeAdapter.setDelPosition(-1);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("删除失败");
            }
        });
    }

    private void getLocalRecipe(List<RecipeBean> recipeBeans ) {
        for (int i = 0;i<recipeBeans.size()-1;i++){
            recipeBeans.get(i).getRecipe_id();
            List<RecipeStepBean> RecipeStepBeans = LitePal.where("recipeBean_id = ?" , recipeBeans.get(i).getId()+"" ).find(RecipeStepBean.class);
            int times = LitePal.where("recipeBean_id = ?" , recipeBeans.get(i).getId()+"" ).sum(RecipeStepBean.class , "time" ,int.class);

        }


//        Requests.saveMultiRecipeRequest reqBody = new Requests.saveMultiRecipeRequest();
//        reqBody.userId = Plat.accountService.getCurrentUserId();
//        reqBody.name = recipeName.getText().toString();
//        reqBody.deviceGuid = mGuid;
//        if (recipeId != -1) {
//            reqBody.id = recipeId;
//        }
//        for (int i = 0; i < rv610StepAdapter.getItemCount() - 1; i++) {
//            Requests.saveMultiRecipeList item = new Requests.saveMultiRecipeList();
//            item.downTemperature = rv610StepAdapter.getItem(i).getTemperature2() + "";
////                item.modelCode = rv610StepAdapter.getItem(i).getFunction_code();
//            item.modelCode = rv610StepAdapter.getItem(i).getWork_mode() + "";
//            item.modelName = rv610StepAdapter.getItem(i).getFunction_name();
//            item.no = i + 1 + "";
//            item.steamQuantity = rv610StepAdapter.getItem(i).getSteam_flow() + "";
//            item.temperature = rv610StepAdapter.getItem(i).getTemperature() + "";
//            item.time = rv610StepAdapter.getItem(i).getTime() + "";
//            multiStepDtoList.add(item);
//        }
//        reqBody.multiStepDtoList = multiStepDtoList;
//        RokiRestHelper.saveMultiRecipe(reqBody, new Callback<RCReponse>() {
//            @Override
//            public void onSuccess(RCReponse recipes) {
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });
    }

    private void saveMultiRecipe(List<RecipeBean> recipeBeans ) {
        for (int i = 0;i<recipeBeans.size()-1;i++){
            recipeBeans.get(i).getRecipe_id();
            List<RecipeStepBean> RecipeStepBeans = LitePal.where("recipeBean_id = ?" , recipeBeans.get(i).getId()+"" ).find(RecipeStepBean.class);
            int times = LitePal.where("recipeBean_id = ?" , recipeBeans.get(i).getId()+"" ).sum(RecipeStepBean.class , "time" ,int.class);

        }


//        Requests.saveMultiRecipeRequest reqBody = new Requests.saveMultiRecipeRequest();
//        reqBody.userId = Plat.accountService.getCurrentUserId();
//        reqBody.name = recipeName.getText().toString();
//        reqBody.deviceGuid = mGuid;
//        if (recipeId != -1) {
//            reqBody.id = recipeId;
//        }
//        for (int i = 0; i < rv610StepAdapter.getItemCount() - 1; i++) {
//            Requests.saveMultiRecipeList item = new Requests.saveMultiRecipeList();
//            item.downTemperature = rv610StepAdapter.getItem(i).getTemperature2() + "";
////                item.modelCode = rv610StepAdapter.getItem(i).getFunction_code();
//            item.modelCode = rv610StepAdapter.getItem(i).getWork_mode() + "";
//            item.modelName = rv610StepAdapter.getItem(i).getFunction_name();
//            item.no = i + 1 + "";
//            item.steamQuantity = rv610StepAdapter.getItem(i).getSteam_flow() + "";
//            item.temperature = rv610StepAdapter.getItem(i).getTemperature() + "";
//            item.time = rv610StepAdapter.getItem(i).getTime() + "";
//            multiStepDtoList.add(item);
//        }
//        reqBody.multiStepDtoList = multiStepDtoList;
//        RokiRestHelper.saveMultiRecipe(reqBody, new Callback<RCReponse>() {
//            @Override
//            public void onSuccess(RCReponse recipes) {
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });
    }


}
