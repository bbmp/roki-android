package com.robam.roki.ui.page.device.cook;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.CookerStatusChangeEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.pojos.Accessories;
import com.robam.common.pojos.KuPrepareSteps;
import com.robam.common.pojos.PayLoadKuF;
import com.robam.common.pojos.device.cook.AbsCooker;
import com.robam.common.services.StoreService;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/6/5.
 * 厨电管理
 */

public class DeviceDetailCookerPage extends BasePage {
    @InjectView(R.id.co_detail_bg)
    ImageView co_detail_bg;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_device_model_name)
    TextView tvDeviceModelName;
    @InjectView(R.id.co_name)
    TextView coName;
    @InjectView(R.id.co_time)
    TextView coTime;
    @InjectView(R.id.co_tip)
    RecyclerView coTip;
    @InjectView(R.id.co_start)
    TextView coStart;

    String guid;
    int bg = R.mipmap.ic_detail_cooker_bg;
    String recipeId;
    StoreService ss = StoreService.getInstance();
    PayLoadKuF payLoadKuF;
    List<Accessories>  accessories = new ArrayList<>();
    List<Accessories>  inde = new ArrayList<>();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    dealData(payLoadKuF);
                    break;
                default:
                    break;
            }
        }
    };
    @InjectView(R.id.pre_recyc)
    RecyclerView preRecyc;
    @InjectView(R.id.tip)
    TextView tip;

    private String timeStr(int time) {
        int ti = time / 60;
        int sec = time % 60;
        return ti + "分"+sec+"秒";
    }

    AbsCooker absCooker;
    RecipeStepAdapter recipeStepAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        recipeId = bd == null ? null : bd.getString(PageArgumentKey.RecipeId);
        LogUtils.i("20180813", "recipeId:" + recipeId);
        absCooker = Plat.deviceService.lookupChild(guid);
        View view = inflater.inflate(R.layout.device_detail_cooker_page, container, false);
        ButterKnife.inject(this, view);
        initView();
        initDate();
        return view;
    }

    StringBuffer str = new StringBuffer();
    private void dealData(PayLoadKuF payLoadKuF){
        try {
            coName.setText(payLoadKuF.name);
            coTime.setText("时长 " + timeStr(payLoadKuF.totalTime));
            if (inde!=null){
                accessories.addAll(inde);
            }
            stepAdapter.setData(accessories);
            recipeStepAdapter.setData(payLoadKuF.kuPrepareStepses);
            tip.setText("*面板上红色圆环闪烁后，点击按键，开始工作");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDate() {
        if (recipeId != null) {
            ss.getKuFRecipeDetailInte(recipeId, new Callback<Reponses.GetKuFRecipeDetailResonse>() {
                @Override
                public void onSuccess(Reponses.GetKuFRecipeDetailResonse getKuFRecipeDetailResonse) {
                    if (getKuFRecipeDetailResonse != null) {
                        LogUtils.i("20180612", "sty::" + getKuFRecipeDetailResonse.toString());
                        payLoadKuF = getKuFRecipeDetailResonse.payLoadKuFs;
                        accessories = payLoadKuF.ingredients;
                        inde = payLoadKuF.accessories;
                        Message msg = Message.obtain();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("20180612", "t::" + t);
                }
            });
        }
    }

    IRokiDialog iRokiDialog;
    StepAdapter stepAdapter;
    private void initView() {
        iRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_02);
        Glide.with(cx)
                .asBitmap()
                .load(bg)
                .fitCenter()
                .into(co_detail_bg);
        LinearLayoutManager layoutManager = new LinearLayoutManager(cx);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(cx);
        preRecyc.setLayoutManager(layoutManager);
        coTip.setLayoutManager(layoutManager1);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        layoutManager1.setOrientation(OrientationHelper.VERTICAL);
        stepAdapter = new StepAdapter(cx);
        recipeStepAdapter = new RecipeStepAdapter(cx);
        preRecyc.setAdapter(stepAdapter);
        coTip.setAdapter(recipeStepAdapter);
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }


    @OnClick({R.id.iv_back, R.id.co_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;
            case R.id.co_start:
                if (!absCooker.isConnected()) {
                    ToastUtils.show("电磁炉已离线", Toast.LENGTH_SHORT);
                    return;
                }
                if (absCooker.powerStatus == 0) {
                    ToastUtils.show("请先在设备上开机", Toast.LENGTH_SHORT);
                    return;
                }
                if (absCooker.powerStatus == 3) {
                    ToastUtils.show("请先让电磁炉停止工作,再进行操作", Toast.LENGTH_SHORT);
                    return;
                }

                //  UIService.getInstance().popBack().popBack().popBack();
                if (absCooker.powerStatus == 3) {
                    ToastUtils.show("请先让电磁炉停止工作，在进行操作", Toast.LENGTH_SHORT);
                    return;
                }

                // UIService.getInstance().postPage(PageKey.DeviceCookerRecipe,bd);
                sendCommand();
                iRokiDialog.setTitleText("提示");
                iRokiDialog.setContentText("10分钟内未轻触按键，菜谱将无效！");
                iRokiDialog.setOkBtn("知道了", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (iRokiDialog != null && iRokiDialog.isShow()) {
                            iRokiDialog.dismiss();
                        }
                    }
                });
                iRokiDialog.show();
                break;
        }
    }


    private void sendCommand() {

        absCooker.setRecipeCookerWork(Short.decode(recipeId), new VoidCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.show("菜谱下发成功", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("菜谱下发失败，请重新下发", Toast.LENGTH_SHORT);
            }
        });
    }

    @Subscribe
    public void onEvent(CookerStatusChangeEvent event) {
        if (absCooker == null || !Objects.equal(absCooker.getID(), event.pojo.getID()))
            return;
        LogUtils.i("20180804", "event::" + event.pojo.powerStatus);
        if (absCooker.powerStatus == 3 && absCooker.recipeStatus == 1) {
            UIService.getInstance().popBack().popBack().popBack();
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, guid);
            bd.putString(PageArgumentKey.RecipeId, recipeId);
            UIService.getInstance().postPage(PageKey.DeviceSubCooker, bd);
            if (iRokiDialog != null && iRokiDialog.isShow()) {
                iRokiDialog.dismiss();
            }
        }
    }

    class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
        Context cx;
        LayoutInflater mInflater;
        List<Accessories>  accessories=new ArrayList<>();
        public StepAdapter(Context cx) {
            this.cx = cx;
            mInflater = LayoutInflater.from(cx);
        }

        public void setData(List<Accessories>  accessories){
            this.accessories = accessories;
            notifyDataSetChanged();
        }

        @Override
        public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.pre_recipe_step, parent, false);
            StepViewHolder stepViewHolder = new StepViewHolder(view);
            return stepViewHolder;
        }

        @Override
        public void onBindViewHolder(StepViewHolder holder, int position) {
             holder.mName.setText(accessories.get(position).name);
             holder.mUnit.setText(accessories.get(position).dosage+accessories.get(position).unit);
        }

        @Override
        public int getItemCount() {
            return accessories==null?0:accessories.size();
        }

        class StepViewHolder extends RecyclerView.ViewHolder {
            TextView mName;
            TextView mUnit;
            public StepViewHolder(View itemView) {
                super(itemView);
                mName = itemView.findViewById(R.id.m_name);
                mUnit = itemView.findViewById(R.id.m_unit);
            }
        }
    }


    class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepHolder> {
        Context cx;
        LayoutInflater mInflater;
        List<KuPrepareSteps>  accessories=new ArrayList<>();
        public RecipeStepAdapter(Context cx) {
            this.cx = cx;
            mInflater = LayoutInflater.from(cx);
        }

        public void setData(List<KuPrepareSteps>  accessories){
            this.accessories = accessories;
            notifyDataSetChanged();
        }

        @Override
        public RecipeStepHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.cooker_pre_step_show, parent, false);
            RecipeStepHolder stepViewHolder = new RecipeStepHolder(view);
            return stepViewHolder;
        }

        @Override
        public void onBindViewHolder(RecipeStepHolder holder, int position) {
           holder.preDesc.setText(accessories.get(position).description);
        }

        @Override
        public int getItemCount() {
            return accessories==null?0:accessories.size();
        }

        class RecipeStepHolder extends RecyclerView.ViewHolder {
            TextView preDesc;
            public RecipeStepHolder(View itemView) {
                super(itemView);
                preDesc = itemView.findViewById(R.id.txt_show);
            }
        }
    }



}
