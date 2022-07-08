package com.robam.roki.ui.activity3.device.pot;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.MainFunc;
import com.legent.plat.pojos.device.OtherFunc;
import com.legent.ui.UIService;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.CureFinishEvent;
import com.robam.common.events.PotAlarmEvent;
import com.robam.common.events.PotDotEvent;
import com.robam.common.events.PotStatusChangedEvent;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.activity3.device.base.DeviceBaseActivity;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.activity3.device.base.DevicePMenuActivity;
import com.robam.roki.ui.activity3.device.base.other.FuncCodeKey;
import com.robam.roki.ui.activity3.device.base.other.PotDialogUtils;
import com.robam.roki.ui.activity3.device.base.other.VerticalItemDecoration;
import com.robam.roki.ui.activity3.device.fan.FanMoreActivity;
import com.robam.roki.ui.activity3.device.pot.adapter.RvPotFuntionAdapter;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.form.CookCurveActivity;
import com.robam.roki.utils.PotTempTipsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * author : huxw
 * time   : 2022/06/07
 * desc   : 无人锅功能界面
 */
public final class PotFunctionActivity<MPot extends Pot> extends DeviceBaseActivity implements RvPotFuntionAdapter.FuntionOnClickListener {
    private MPot pot;
    /**
     * 功能区adapter
     */
    private RecyclerView rvFunction;
    /**
     * 功能区adapter
     */
    private RvPotFuntionAdapter adapter;
    /**
     * 温度栏
     */
    private LinearLayout llPotTemp;
    private TextView tvPotTemp,tvPotContext;
    private AppCompatImageView ivDeviceImg;
    /**
     * 顶部提示：电量、烹饪记录
     */
    private TextView tv_low_power, tv_cooking_records;
    //配置信息
    Reponses.DeviceResponse mDeviceResponse;
    //弹出窗控制类
    PotDialogUtils potDialogUtils;
    //曲线id
    private long curveId = 0;
    private Stove stove;

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (pot == null || !Objects.equal(pot.getID(), event.device.getID())) {
            return;
        }
        //设置设备状态
        setConnectedState(!event.isConnected);
        if (!event.isConnected) {
            toast(R.string.device_new_connected);
        }
    }

    @Subscribe
    public void onEvent(PotAlarmEvent event) {
        short alarmId = event.alarmId;
        if (alarmId == 1) {
            tv_low_power.setVisibility(View.VISIBLE);
        } else {
            tv_low_power.setVisibility(View.INVISIBLE);
        }
    }

    @Subscribe
    public void onEvent(PotStatusChangedEvent event) {
        if (pot == null || !Objects.equal(pot.getID(), event.pojo.getID()))
            return;
        pot = (MPot) event.pojo;
        setConnectedState(!pot.isConnected());
        updataPotState();
    }
    //上条曲线记录结束
    @Subscribe
    public void onEvent(CureFinishEvent event) {
        curveId = 0;
    }
    private void updataPotState(){
        adapter.potNotify(pot);
        //稳定显示
        if (pot.tempUp >= 130) {
            llPotTemp.setVisibility(View.VISIBLE);
        } else {
            llPotTemp.setVisibility(View.INVISIBLE);
        }
        if (mDeviceResponse.modelMap.backgroundFunc.deviceConfigurationFunctions.size() > 0 && !TextUtils.isEmpty(mDeviceResponse.modelMap.backgroundFunc.deviceConfigurationFunctions.get(0).functionParams)) {
            PotTempTipsUtil.updateState(ivDeviceImg, tvPotTemp, tvPotContext, mDeviceResponse.modelMap.backgroundFunc.deviceConfigurationFunctions.get(0).functionParams, pot, getContext());
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pot_function;
    }

    @Override
    protected void initView() {
        rvFunction = findViewById(R.id.rv_function);
        rvFunction.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //设置功能区块间距
        rvFunction.addItemDecoration(new VerticalItemDecoration(18, getContext()));
        adapter = new RvPotFuntionAdapter();
        rvFunction.setAdapter(adapter);
        findViewById(R.id.iv_device_switch).setVisibility(View.INVISIBLE);
        tv_low_power = findViewById(R.id.tv_low_power);
        tv_cooking_records = findViewById(R.id.tv_cooking_records);
        setOnClickListener(R.id.tv_low_power, R.id.tv_cooking_records);

        llPotTemp = findViewById(R.id.ll_pot_temp);
        tvPotTemp = findViewById(R.id.tv_pot_temp);
        tvPotContext = findViewById(R.id.tv_pot_context);
        ivDeviceImg = findViewById(R.id.iv_device_img);

        potDialogUtils = new PotDialogUtils(this);

    }

    /**
     * 数据参数获取成功
     *
     * @param deviceResponse
     */
    @Override
    public void onLoadData(Reponses.DeviceResponse deviceResponse) {
        if (deviceResponse == null) {
            toast("设备参数为空");
            return;
        }
        if (mDevice instanceof Pot) {
            pot = (MPot) mDevice;
        }
        mDeviceResponse = deviceResponse;
        //设置标题栏名称
        setTitle(mDevice.getName() == null || mDevice.getName().equals(mDevice.getCategoryName()) ? mDevice.getDispalyType() : mDevice.getName());

        //背景图片
        String bgImgUrl = deviceResponse.viewBackgroundImg;
        //设置背景图片
        setDeviceBgImg(bgImgUrl);

        //主功能区数据
        MainFunc mainFunc = deviceResponse.modelMap.mainFunc;
        List<DeviceConfigurationFunctions> mainFuncList = mainFunc.deviceConfigurationFunctions;
        //主功能排序
//        mainFuncList = SortUtils.funSort(mainFuncList, 0);

        //其他功能区
        OtherFunc otherFunc = deviceResponse.modelMap.otherFunc;
        List<DeviceConfigurationFunctions> deviceConfigurationFunctions = otherFunc.deviceConfigurationFunctions;

        adapter.addData(mainFuncList);
        adapter.addData(deviceConfigurationFunctions);
        //添加点击事件
        adapter.addFuntionOnClickListener(this);
        //原始界面加载完成
        loadSuccess = true;
//        adapter.potNotify(pot);
        //传默认灶头
        openPageQuery(1);

        AbsFan fan = Plat.deviceService.lookupChild(pot.getParent().getID());
        Stove childStove = fan.getChildStove();
        if (childStove instanceof Stove) {
            String dc = childStove.getDc();
            //电磁灶
            if (IDeviceType.RDCZ.equals(dc)) {

                //燃气灶
            } else if (IDeviceType.RRQZ.equals(dc)) {
                stove = childStove;
            }
        }
    }


    /**
     * 数据参数获取失败
     */
    @Override
    public void onFail() {
        toast("设备参数请求失败");
        finish();
    }

    /**
     * 点击事件
     *
     * @param func
     */
    @Override
    public void onClick(DeviceConfigurationFunctions func) {
        String functionCode = func.functionCode;
//        toast(functionCode);
        switch (functionCode) {
            case FuncCodeKey.QUICKSUSTAINFRIED:
                potContinueFast();
                break;
            case FuncCodeKey.TENSECONDSFRY:
                potTenSecondFast();
                break;
            case FuncCodeKey.AUTOCOOKING:
                List<AbsFan> fanList = Utils.getFan();
                List<Stove> stoves = new ArrayList<>();
                for (int i = 0; i < fanList.size(); i++) {
                    if (fanList.get(i).getChildStove() != null) {
                        stoves.add((Stove) fanList.get(i).getChildStove());
                    }
                }
                if (!pot.isConnected()) {
                    ToastUtils.show(R.string.oven_dis_con, Toast.LENGTH_SHORT);
                    return;
                }
                if (stoves.size() == 0) {
                    ToastUtils.show(R.string.device_stove_not, Toast.LENGTH_SHORT);
                    return;
                }
                bundle.putString(PageArgumentKey.RecipeId, IDeviceType.RZNG);
                Intent intent = new Intent(getContext() , DevicePMenuActivity.class);
                intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bundle);
                startActivity(intent);
                break;
            case FuncCodeKey.CURVECOOKING:
//                Glide.with(getContext()).asGif().load(R.mipmap.pot_state_high).into(ivDeviceImg);
//                llPotTemp.setVisibility(View.VISIBLE);
//                tvPotTemp.setText("400");
//                tvPotContext.setText("油温过热！");
                if(curveId!=0){//有曲线正在记录
                    bundle.putString(PageArgumentKey.Guid, mGuid);
                    bundle.putString(PageArgumentKey.stove,stove.getGuid().getGuid());
                    bundle.putInt(PageArgumentKey.HeadId, 1);
                    bundle.putLong(PageArgumentKey.curveId, curveId);
                    bundle.putString(PageArgumentKey.pot, pot.getID());
                    intent = new Intent(getContext() , PotCookingCurveActivity.class);
                    intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bundle);
                    startActivity(intent);
                    return;
                }
//                if (stove.leftHead.status <= 0 && stove.rightHead.status <= 0) {
//                    ToastUtils.show("炉头均处于关闭状态", Toast.LENGTH_SHORT);
//                    return;
//                }
//                potDialogUtils.setStatus(stove);
//                potDialogUtils.stoveHeadSelect(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        toast(v.getTag().toString());
//                        Intent intent = new Intent(getContext() , PotCookingCurveActivity.class);
//                        intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bundle);
//                        startActivity(intent);
//                    }
//                });

                if (stove.rightHead.status <= 0) {
                    ToastUtils.show("智能炉头处于关闭状态", Toast.LENGTH_SHORT);
                    return;
                }
                query(1);
                break;
            case FuncCodeKey.FAVORITE:
                intent = new Intent(this , PotMyFavouriteActivity.class);
                intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bundle);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_low_power:
                toast("低电量");
                potDialogUtils.showBatteryReplace(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_low_power.setVisibility(View.GONE);
                    }
                });
                break;
            case R.id.tv_cooking_records:
                toast("烹饪记录");
                break;

        }
    }

    private void potContinueFast() {
        if (!pot.isConnected()) {
            toast(R.string.device_new_connected);
            return;
        }
        Pot.Interaction interaction = new Pot.Interaction();
        interaction.key = 6;
        interaction.length = 1;
        if (pot.potESPMode == 1) {
            interaction.value = new int[]{0};
        } else {
            interaction.value = new int[]{1};
        }
        List<Pot.Interaction> data = new ArrayList<>();
        data.add(interaction);
        pot.setInteraction(data, new VoidCallback() {
            @Override
            public void onSuccess() {
                pot.potESPMode = Short.parseShort(interaction.value[0]+"");
                adapter.potNotify(pot);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show(t.getMessage(), Toast.LENGTH_SHORT);
            }
        });

    }

    private void potTenSecondFast() {
        if (!pot.isConnected()) {
            toast(R.string.device_new_connected);
            return;
        }
        Pot.Interaction interaction = new Pot.Interaction();
        interaction.key = 6;
        interaction.length = 1;
        if (pot.potESPMode == 2) {
            interaction.value = new int[]{0};
        } else {
            interaction.value = new int[]{2};
        }
        List<Pot.Interaction> data = new ArrayList<>();
        data.add(interaction);
        pot.setInteraction(data, new VoidCallback() {
            @Override
            public void onSuccess() {
                pot.potESPMode = Short.parseShort(interaction.value[0]+"");
                adapter.potNotify(pot);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show(t.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * 启动曲线
     */
    private void potStartRecordCurve() {
        if (!pot.isConnected()) {
            toast(R.string.device_new_connected);
            return;
        }
        Pot.Interaction interaction = new Pot.Interaction();
        interaction.key = 2;
        interaction.length = 2;
        interaction.value = new int[]{1,1};
        List<Pot.Interaction> data = new ArrayList<>();
        data.add(interaction);
        pot.setInteraction(data, new VoidCallback() {
            @Override
            public void onSuccess() {
                bundle.putString(PageArgumentKey.Guid, mGuid);
                bundle.putString(PageArgumentKey.stove,stove.getGuid().getGuid());
                bundle.putInt(PageArgumentKey.HeadId, 1);
                bundle.putLong(PageArgumentKey.curveId, curveId);
                bundle.putString(PageArgumentKey.pot, pot.getID());
                Intent intent = new Intent(getContext() , PotCookingCurveActivity.class);
                intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bundle);
                startActivity(intent);
            }

            @Override
            public void onFailure(Throwable t) {
                curveId = 0;
                ToastUtils.show(t.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }

    private void potBindHead() {
        if (!pot.isConnected()) {
            toast(R.string.device_new_connected);
            return;
        }
        Pot.Interaction interaction = new Pot.Interaction();
        interaction.key = 5;
        interaction.length = 1;
//        interaction.value = new int[]{0};//左
        interaction.value = new int[]{1};//右
        List<Pot.Interaction> data = new ArrayList<>();
        data.add(interaction);
        pot.setInteraction(data, new VoidCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show(t.getMessage(), Toast.LENGTH_SHORT);
            }
        });

    }

    /**
     * 更多
     */
    @Override
    protected void toMore() {
        super.toMore();
        //TODO
//        potBindHead();
    }
    //打开页面请求是否有正在进行的曲线
    private void openPageQuery(int headId) {
        RokiRestHelper.cookingCurvequery(mGuid,
                new Callback<com.robam.common.io.cloud.Reponses.CookingCurveQueryRes>() {
                    @Override
                    public void onSuccess(com.robam.common.io.cloud.Reponses.CookingCurveQueryRes rcReponse) {
                        if (rcReponse.payload != null) {
                            curveId = rcReponse.payload.curveCookbookId;
                        }
                    }
                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
    }
    private void query(int headId) {
        RokiRestHelper.cookingCurvequery(mGuid,
                new Callback<com.robam.common.io.cloud.Reponses.CookingCurveQueryRes>() {

                    @Override
                    public void onSuccess(com.robam.common.io.cloud.Reponses.CookingCurveQueryRes rcReponse) {
                        Log.d("20211129", rcReponse.toString());
                        if (rcReponse.payload == null) {
                            cookingCurveSave(headId);
                        } else {
                            curveId = rcReponse.payload.curveCookbookId;
                            bundle.putString(PageArgumentKey.Guid, mGuid);
                            bundle.putString(PageArgumentKey.stove,stove.getGuid().getGuid());
                            bundle.putInt(PageArgumentKey.HeadId, headId);
                            bundle.putLong(PageArgumentKey.curveId, curveId);
                            bundle.putString(PageArgumentKey.pot, pot.getID());
                            Intent intent = new Intent(getContext() , PotCookingCurveActivity.class);
                            intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bundle);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
    }

    private void cookingCurveSave(int headId) {
        String deviceGuid = stove.getGuid().getGuid();
        int id = 0;
        String model = "";
        String setTemp = "";
        String setTime = "";
        RokiRestHelper.cookingCurveSave(deviceGuid, id, model, setTemp, setTime, headId,
                new Callback<com.robam.common.io.cloud.Reponses.CookingCurveSaveRes>() {

                    @Override
                    public void onSuccess(com.robam.common.io.cloud.Reponses.CookingCurveSaveRes rcReponse) {
                        Log.d("20211129", rcReponse.toString());
                        if (rcReponse.payload != 0) {
                            curveId = rcReponse.payload;

                            potStartRecordCurve();

                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.show(t.getMessage(), Toast.LENGTH_SHORT);
                    }
                });

    }
}