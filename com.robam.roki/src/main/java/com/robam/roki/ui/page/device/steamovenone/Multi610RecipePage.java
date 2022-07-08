package com.robam.roki.ui.page.device.steamovenone;

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
import com.robam.common.events.SteamOvenOpenDoorSteamEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.io.device.TerminalType;
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
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.utils.StringUtil;

import org.json.JSONException;
import org.litepal.LitePal;

import java.util.List;

/**
 * @author r210190
 * 610离线菜谱详情
 */
public class Multi610RecipePage extends MyBasePage<MainActivity> {
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
    AbsSteameOvenOne steameOvenOne;
    List<DeviceConfigurationFunctions> mDatas;
    private long userId;

    float mPosX ;
    float mPosY ;
    float mCurPosX ;
    float mCurPosY ;
    /**
     * 选中的多段菜谱
     */
    private RecipeBean recipeBean;
    private int mPosition;

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
                    bundle.putInt(Multi610EditDetailPage.RECIPEID , recipeId);
                    UIService.getInstance().postPage(PageKey.Multi610EditDetailPage , bundle);
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


//        rv610Recipe.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        mPosX = event.getX();
//                        mPosY = event.getY();
//
//                            break;
//                    case MotionEvent.ACTION_MOVE:
//                        mCurPosX = event.getX();
//                        mCurPosY = event.getY();
//
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        View view2 = rv610Recipe.findChildViewUnder(mCurPosX, mCurPosY);
//                        if (view2 == null) {
//                            return false;
//                        }
//
//                        RecyclerView.ViewHolder childViewHolder2 = rv610Recipe.getChildViewHolder(view2);
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
//                            rv610RecipeAdapter.setRightDelPosition(mPosition);
//                            return true;
//
//                        } else if (mCurPosX - mPosX < 0
//                                && (Math.abs(mCurPosX - mPosX) > 3)) {
//                            //向左滑动
////                                ToastUtils.showShort("向左滑动");
//                            rv610RecipeAdapter.setDelPosition(mPosition);
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
        List<RecipeBean> recipeBeans = LitePal.where("device = ? and  user_id = ?", "D610", Plat.accountService.getCurrentUserId() + "").find(RecipeBean.class);
        rv610RecipeAdapter.addData(recipeBeans);
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        steameOvenOne = Plat.deviceService.lookupChild(guid);
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
//        needDescalingParams = bd == null ? null : bd.getString(PageArgumentKey.descaling);
        userId = Plat.accountService.getCurrentUserId();
    }

    @Subscribe
    public void onEvent(PageBackEvent event) {
        if ("Multi610EditDetailPage".equals(event.getPageName())) {
            List<RecipeBean> recipeBeans = LitePal.where("device = ? and  user_id = ?", "D610", Plat.accountService.getCurrentUserId() + "").find(RecipeBean.class);
            rv610RecipeAdapter.setNewInstance(recipeBeans);
            rv610RecipeAdapter.setSelectPosition(-1);
            btnAutomatic.setEnabled(false);
        }
    }

    @Override
    public void onRightClick(View view) {
        getArguments().putInt(Multi610EditDetailPage.RECIPEID, -1);
        UIService.getInstance().postPage(PageKey.Multi610EditDetailPage, getArguments());
    }

    @Override
    public void onClick(View view) {
        if (view == btnAutomatic) {
            steam_on();
        }
    }

    private void searchRecipe(String recipeName) {
        if (StringUtil.isEmpty(recipeName)) {
            List<RecipeBean> recipeBeans = LitePal.where("device = ? and  user_id = ?", "D610", Plat.accountService.getCurrentUserId() + "").find(RecipeBean.class);
            rv610RecipeAdapter.setNewInstance(recipeBeans);
        } else {
            List<RecipeBean> recipeBeans = LitePal.where("device = ? and  user_id = ? and recipe_names like ?", "D610", Plat.accountService.getCurrentUserId() + "", "%" + recipeName + "%").find(RecipeBean.class);
            rv610RecipeAdapter.setNewInstance(recipeBeans);
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
        if (steameOvenOne.doorStatusValue == 1 ) {
            ToastUtils.show("门未关好，请检查并确认关好门", Toast.LENGTH_SHORT);
            return;
        }
        List<RecipeStepBean> recipeStepList = recipeBean.getRecipeStepList();
        boolean isWater = false ; //多段是否含有需要用水箱的

        for (RecipeStepBean step :
                recipeStepList) {
            if (step.getWork_mode() == 13
                    || step.getWork_mode() == 14
                    || step.getWork_mode() == 15
                    || step.getWork_mode() == 16
                    || step.getWork_mode() == 20
                    || step.getWork_mode() == 21
                    || step.getWork_mode() == 22
                    || step.getWork_mode() == 17
                    || step.getWork_mode() == 18
            ){
                isWater = true ;
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

                for (int i = 1; i <= recipeBean.getRecipeStepList().size(); i++) {
                    msg.putOpt(MsgParams.SteameOvenSectionOfTheStep_Key + i, 2 + i);//段步骤 key
                    msg.putOpt(MsgParams.SteameOvenSectionOfTheStep_Length + i, 9);//段步骤 value
                    msg.putOpt(MsgParams.SteameOvenMode + i, recipeStepList.get(i - 1).getWork_mode());//第一段 mode
                    msg.putOpt(MsgParams.SteameOvenTemp + i, recipeStepList.get(i - 1).getTemperature());
                    msg.putOpt(MsgParams.SteameOvenTime + i, recipeStepList.get(i - 1).getTime());
                    msg.putOpt(MsgParams.SteameOvenTemp2 + i, recipeStepList.get(i - 1).getTemperature2());//
                    msg.putOpt(MsgParams.SteameOvenTime2 + i, recipeStepList.get(i - 1).getTime());//
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
}
