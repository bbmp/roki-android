package com.robam.roki.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.legent.plat.Plat;
import com.legent.plat.pojos.device.AbsDevice;
import com.legent.plat.pojos.device.IDevice;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.graphic.ImageUtils;
import com.legent.utils.speech.SpeechManager;
import com.robam.common.Utils;
import com.robam.common.io.device.MsgParams;
import com.robam.common.paramCode;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneModel;
import com.robam.common.util.DeviceUtils;
import com.robam.common.util.TimeUtils;
import com.robam.roki.R;
import com.robam.roki.ui.RecipeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/15.
 */
public class RecipeStepDetailView extends FrameLayout {
    @InjectView(R.id.recipe_step_tv_currentstep)//每步骤步骤号
            TextView recipe_step_tv_currentstep;
    @InjectView(R.id.recipe_step_tv_totalstep)//总步骤数
            TextView recipe_step_tv_totalstep;
    @InjectView(R.id.recipe_step_linear_complete)//完成提示
    public LinearLayout recipe_step_linear_complete;
    @InjectView(R.id.recipe_step_tv_stepdetail)//菜谱详情文字
    public TextView recipe_step_tv_stepdetail;
    @InjectView(R.id.recipe_step_img_speak)//声控图标
            ImageView recipe_step_img_speak;
    @InjectView(R.id.recipe_step_relative)//下半部分
            RelativeLayout recipe_step_relative;
    @InjectView(R.id.recipe_step_linear_model)//模式选择框
            LinearLayout recipe_step_linear_model;
    @InjectView(R.id.recipe_step_img_bg)//下半部分背景
            ImageView recipe_step_img_bg;

    private List<CookStep> cookStepList;
    private Context cx;
    public int step;//当前步骤号
    public CookStep cookStep;//菜谱步骤对象

    public boolean hasCategory;//是否有无品类
    public boolean isUserHasDevice;//当前用户是否含有对应设备
    public boolean isCurrentDeviceHasPlat;//当前用户设备是否含有平台

    public String category;//当前品类
    public String categoryName;
    public AbsDevice device;//设备信息
    public Map<String, paramCode> paramMap;//参数map
    private com.robam.common.paramCode paramCode;
    public String type;
    public String typeDp;
    private IDevice iDevice;
    int number;
    public RecipeStepDetailView(Context context, CookStep recipeSteps,List<CookStep> cookStepList,int step) {
        super(context);
        this.cx = context;
        this.step = step;
        this.cookStep = recipeSteps;
        this.cookStepList = cookStepList;
        init(context);
       // LogUtils.i("20170711","id:"+recipe.id);
    }
    private PickListener listemer;

    public void setListemer(PickListener listemer) {
        this.listemer = listemer;
    }

    public interface PickListener {

        void onCancel();

        void onConfirm(String typeDt);
    }

    void init(Context cx) {
        View view = LayoutInflater.from(cx).inflate(R.layout.page_recipe_step,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
            refresh();
        }
    }

    private Handler myHandler = new Handler();

    /**
     * 語音播放
     */
    @OnClick(R.id.recipe_step_img_speak)
    public void onSpeakClick() {
        if (StringUtils.isNullOrEmpty(cookStep.desc))
            return;
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SpeechManager.getInstance().startSpeaking(cookStep.desc);
            }
        }, 100);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        refresh();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (visibility == VISIBLE) {
            refresh();
        }
    }

    public void refresh() {
        if (cookStep == null) return;
        ImageUtils.displayImage(getContext(), cookStep.imageUrl, recipe_step_img_bg);//设置背景图片
        if (cookStep.dc == null || "".equals(cookStep.dc)) {//判断菜谱是否韩有品类
            noHasDeviceMode();
            hasCategory = false;
        } else {
            LogUtils.i("20171117","type:"+type);
            hasCategory = true;
            if (cookStep.dc.contains("||")){
              /*  List<IDevice> listdefault = splitMethod(cookStep.dc);

                if (listdefault!=null){
                    category = listdefault.get(0).getDc();
                }else{*/
                    String[] dc = cookStep.dc.split("\\|\\|");
                    category = dc[0];
              //  }
            }else{
                category = cookStep.dc;
            }
            recipe_step_linear_model.setVisibility(GONE);
            isUserHasDevice = true;
            getDeviceInfoFromHttp(category);
            LogUtils.i("20171115","category::"+category);
            category = cookStep.dc;
            /*if (DeviceType.RDKX.equals(category)) {
                categoryName = new String("电烤箱");
            } else if (DeviceType.RZQL.equals(category)) {
                categoryName = new String("蒸汽炉");
            } else if (DeviceType.RWBL.equals(category)) {
                categoryName = new String("微波炉");
            }*/

        }
        recipe_step_tv_currentstep.setText(String.valueOf(step + 1));//当前步骤
        recipe_step_tv_totalstep.setText(cookStepList.size()+"");//总步骤
        recipe_step_tv_stepdetail.setText(cookStep.desc);
    }

    public void setParamView(IDevice iDevice,int deviceNumber){
        this.iDevice = iDevice;
        number = deviceNumber;
        if (deviceNumber>1){
            onlyOneDevice(iDevice);
        }else{
            isUserHasDevice = DeviceUtils.isHasCategory(category);
            LogUtils.i("20170426","gg:"+isUserHasDevice);
            if (isUserHasDevice) {//判断当前用户是否含有对应设备
                getDeviceInfoFromHttp(category);//判断设备对应平台参数是否正确
            } else {
                noHasDeviceMode();
            }
            //追加
            if (hasCategory && (!isUserHasDevice || !isCurrentDeviceHasPlat)) {//当菜谱有设备，显示默认
                Map<String, paramCode> map = RecipeUtil.getRecipeDefaultStepParam(cookStep);
                if (map != null && map.size() != 0) {
                    setParamLinear(map,category);
                }
            }
        }
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<IDevice> splitMethod(String category){
        List<IDevice> list = Plat.deviceService.queryAll();
        List<IDevice> listtemp=new ArrayList<>();
        if (category.contains("||")){
            LogUtils.i("20171115","category.contains:)"+category.contains("||"));
            LogUtils.i("20171115","category:"+category);
            String[] dcStr = category.split("\\|\\|");
            for (int i = 0; i < dcStr.length; i++) {
                LogUtils.i("20171115","dcStr:"+dcStr[i]);
                for (int j = 0; j < list.size(); j++) {
                    LogUtils.i("20171115","list::"+list.get(j).getDp());
                    if (dcStr[i].equals(list.get(j).getDc())){
                        listtemp.add(list.get(j));
                    }
                }
            }
        }else{
            for (int i = 0; i < list.size(); i++) {
                if (category.equals(list.get(i).getDc())){
                    listtemp.add(list.get(i));
                }
            }
        }
        return listtemp;
    }


    private void onlyOneDevice(IDevice iDevice){
        if(iDevice==null)return;
        isUserHasDevice = DeviceUtils.isHasCategory(iDevice.getDc());
        LogUtils.i("20171116","isUserHasdevice:"+isUserHasDevice);
        if (isUserHasDevice) {//判断当前用户是否含有对应设备
            if (iDevice!=null){
                getDeviceInfoFromHttp(iDevice.getDc(),iDevice);//判断设备对应平台参数是否正确
            }
        } else {
            noHasDeviceMode();
        }
        //追加
        if (hasCategory && (!isUserHasDevice || !isCurrentDeviceHasPlat)) {//当菜谱有设备，显示默认
            Map<String, paramCode> map = RecipeUtil.getRecipeDefaultStepParam(cookStep);
            if (map != null && map.size() != 0) {
                setParamLinear(map,iDevice.getDc());
            }
        }
    }


    public String getType() {
        return type;
    }

    /**
     * 创建单线
     *
     * @return
     */
    private View newLine() {
        View view = new View(cx);
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(Utils.dip2px(cx, 1f), LayoutParams.MATCH_PARENT);
        ll.setMargins(0, Utils.dip2px(cx, 2f), 0, Utils.dip2px(cx, 2f));
        view.setLayoutParams(ll);
        view.setBackgroundColor(cx.getResources().getColor(R.color.c02));
        return view;
    }

    /**
     * 创建特定linear
     * isTime 表示是否是时间格式
     */
    private LinearLayout newLinear(String name, String value, boolean isTime) {
        if (name == null)
            name = "";
        if (value == null)
            value = "";
        LinearLayout linearLayout = new LinearLayout(cx);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView tv1 = new TextView(cx);
        LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llparams.gravity = Gravity.CENTER_HORIZONTAL;
        tv1.setLayoutParams(llparams);
        tv1.setText(name);
        tv1.setTextColor(Color.parseColor("#000000"));
        tv1.setTextSize(18);
        tv1.setTextColor(Color.parseColor("#000000"));
        TextView tv2 = new TextView(cx);

        LinearLayout.LayoutParams llparams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llparams2.setMargins(0, Utils.dip2px(cx, 7), 0, 0);
        tv2.setLayoutParams(llparams);

        if (isTime)
            TimeUtils.setMinAndSecByUnity(tv2, Long.parseLong(value));
        else
            tv2.setText(value);
            tv2.setTextSize(16);
            tv2.setTextColor(Color.parseColor("#000000"));
            linearLayout.addView(tv1);
            linearLayout.addView(tv2);

            return linearLayout;
    }

    /**
     * 有联动设备模式
     */
    private void hasDeviceMode() {

    }

    /**
     * 无联动设备模式
     */
    private void noHasDeviceMode() {
        recipe_step_linear_model.setVisibility(GONE);
    }


    /**
     * 远程获取设备DP并找对应参数并封装
     */
    private void getDeviceInfoFromHttp(String dc,IDevice iDevice) {

        if (DeviceType.RDKX.equals(dc)) {//电烤箱
            type =iDevice.getDt();
            LogUtils.i("20171116","电烤箱type::"+type);
            paramMap = RecipeUtil.getRecipeStepParamByDP(cookStep, iDevice.getDp());
            if (paramMap == null || paramMap.size() == 0) {
                noHasDeviceMode();
                isCurrentDeviceHasPlat = false;
            } else {
                isCurrentDeviceHasPlat = true;
                setParamLinear(paramMap,dc);
            }

        } else if (DeviceType.RZQL.equals(dc)) {//蒸汽炉
            LogUtils.i("20171117","RQZL");
            type =iDevice.getDt();
            paramMap = RecipeUtil.getRecipeStepParamByDP(cookStep, iDevice.getDp());
            LogUtils.i("20171117","RQZL+paramMap: "+paramMap);
            if (paramMap == null || paramMap.size() == 0) {
                noHasDeviceMode();
                LogUtils.i("20171117","cur+false");
                isCurrentDeviceHasPlat = false;
            } else {
                LogUtils.i("20171117","cur+true");
                isCurrentDeviceHasPlat = true;
                setParamLinear(paramMap,dc);
            }
        } else if (DeviceType.RWBL.equals(dc)) {//微波炉
            type =iDevice.getDt();
            paramMap = RecipeUtil.getRecipeStepParamByDP(cookStep,iDevice.getDp());
            if (paramMap == null || paramMap.size() == 0) {
                noHasDeviceMode();
                isCurrentDeviceHasPlat = false;
            } else {
                isCurrentDeviceHasPlat = true;
                setParamLinear(paramMap,dc);
            }

        } else if (DeviceType.RZKY.equals(dc)) {//一体机
          //  ToastUtils.show("一体机", Toast.LENGTH_SHORT);
            type = iDevice.getDt();
            paramMap = RecipeUtil.getRecipeStepParamByDP(cookStep,iDevice.getDp());
            if (paramMap == null || paramMap.size() == 0) {
                noHasDeviceMode();
                isCurrentDeviceHasPlat = false;
            } else {
                isCurrentDeviceHasPlat = true;
                setParamLinear(paramMap,dc);
            }
        }
    }

    /**
     * 设置参数
     */
    private void setParamLinear(Map<String, paramCode> map,String dc) {
        recipe_step_linear_model.setVisibility(VISIBLE);
        recipe_step_linear_model.removeAllViews();
        if (DeviceType.RDKX.equals(dc)) {//电烤箱
            hasDeviceMode();
            //解析数据，并写入页面
                paramCode paramCode =  map.get(MsgParams.OvenMode);
                recipe_step_linear_model.addView(newLinear(paramCode.codeName, paramCode.valueName, false));
                if (paramCode.value!=9){
                    for (String key : map.keySet()) {
                        if (!MsgParams.OvenMode.equals(key)) {
                            recipe_step_linear_model.addView(newLine());
                            paramCode param = map.get(key);
                            if (MsgParams.OvenTime.equals(key)){
                                recipe_step_linear_model.addView(newLinear(param.codeName, param.value + "", true));
                            }else if(MsgParams.OvenTempUp.equals(key)){
                                    continue;
                            }else if (MsgParams.OvenTempBelow.equals(key)){
                                    continue;
                            } else {
                                recipe_step_linear_model.addView(newLinear(param.codeName, param.value + "°c", false));

                            }
                        }
                    }
                }else{
                    for (String key : map.keySet()) {
                        if (!MsgParams.OvenMode.equals(key)) {
                            recipe_step_linear_model.addView(newLine());
                            paramCode param = map.get(key);
                            if (MsgParams.OvenTime.equals(key)){
                                recipe_step_linear_model.addView(newLinear(param.codeName, param.value + "", true));
                            }else if(MsgParams.OvenTemp.equals(key)) {
                                continue;
                            } else {
                                recipe_step_linear_model.addView(newLinear(param.codeName, param.value + "°c", false));
                            }
                        }
                    }
                }


        } else if (DeviceType.RZQL.equals(dc)) {//蒸汽炉
            hasDeviceMode();
            //解析数据，并写入页面
            short i = 0;
            for (String key : map.keySet()) {
                if (i != 0)
                    recipe_step_linear_model.addView(newLine());
                i++;
                paramCode param = map.get(key);
                if (MsgParams.SteamTime.equals(key))
                    recipe_step_linear_model.addView(newLinear(param.codeName, param.value + "", true));
                else
                    recipe_step_linear_model.addView(newLinear(param.codeName, param.value + "°c", false));
            }
        } else if (DeviceType.RWBL.equals(dc)) {//微波炉
            hasDeviceMode();
            //解析数据，并写入页面
            paramCode paramCode = map.get(MsgParams.MicroWaveMode);

            recipe_step_linear_model.addView(newLinear(paramCode.codeName, paramCode.valueName, false));
            for (String key : map.keySet()) {
                if (!MsgParams.MicroWaveMode.equals(key)) {
                    recipe_step_linear_model.addView(newLine());
                    paramCode param = map.get(key);
                    if (MsgParams.MicroWaveTime.equals(key))//时间
                        recipe_step_linear_model.addView(newLinear(param.codeName, param.value + "", true));
                    else if (MsgParams.MicroWavePower.equals(key))//功率
                        recipe_step_linear_model.addView(newLinear("火力", param.value + "", false));
                    else if (MsgParams.MicroWaveWeight.equals(key))//重量
                        recipe_step_linear_model.addView(newLinear(param.codeName, param.value + "g", false));
                }
            }

        } else if (DeviceType.RZKY.equals(dc)) {//一体机
            hasDeviceMode();
            //解析数据，并写入页面
            paramCode paramCode =  map.get(MsgParams.OvenSteamMode);
            LogUtils.i("20171223","paramCode:"+paramCode);
            if (paramCode != null){
                recipe_step_linear_model.addView(newLinear(paramCode.codeName, paramCode.valueName, false));
                if (paramCode.value!= SteamOvenOneModel.EXP){
                    for (String key : map.keySet()) {
                        if (!MsgParams.OvenSteamMode.equals(key)) {
                            recipe_step_linear_model.addView(newLine());
                            paramCode param = map.get(key);
                            if (MsgParams.OvenSteamTime.equals(key)){
                                recipe_step_linear_model.addView(newLinear(param.codeName, param.value + "", true));
                            }else if (MsgParams.OvenSteamUp.equals(key)) {
                                continue;
                            }else if (MsgParams.OvenSteamBelow.equals(key)){
                                continue;
                            }
                            else {
                                recipe_step_linear_model.addView(newLinear(param.codeName, param.value + "°c", false));
                            }
                        }
                    }
                }else{
                    for (String key : map.keySet()) {
                        if (!MsgParams.OvenSteamMode.equals(key)) {
                            recipe_step_linear_model.addView(newLine());
                            paramCode param = map.get(key);
                            if (MsgParams.OvenSteamTime.equals(key)) {
                                recipe_step_linear_model.addView(newLinear(param.codeName, param.value + "", true));
                            } else if (MsgParams.OvenSteamTemp.equals(key)){
                                continue;
                            }else{
                                recipe_step_linear_model.addView(newLinear(param.codeName, param.value + "°c", false));
                            }
                        }
                    }
                }

            }

        }
    }

    /**
     * 远程获取设备DP并找对应参数并封装
     */
    private void getDeviceInfoFromHttp(String category) {
        if (DeviceType.RDKX.equals(category)) {//电烤箱
            if (Utils.getDefaultOven()!=null){
                type = Utils.getDefaultOven().getDt();
                typeDp = Utils.getDefaultOven().getDp();
            }else{
                typeDp = cookStep.getjs_PlatformCodes().get(0).platCode;
            }
            paramMap = RecipeUtil.getRecipeStepParamByDP(cookStep,typeDp);
            LogUtils.i("20171117","parammap:"+paramMap);
            if (paramMap == null || paramMap.size() == 0) {
                noHasDeviceMode();
                isCurrentDeviceHasPlat = false;
            } else {
                isCurrentDeviceHasPlat = true;
                setParamLinear(paramMap,category);
            }

        } else if (DeviceType.RZQL.equals(category)) {//蒸汽炉
            if (Utils.getDefaultSteam()!=null){
                type = Utils.getDefaultSteam().getDt();
                typeDp = Utils.getDefaultSteam().getDp();
            }else{
                typeDp = cookStep.getjs_PlatformCodes().get(0).platCode;
            }
            paramMap = RecipeUtil.getRecipeStepParamByDP(cookStep, typeDp);
            if (paramMap == null || paramMap.size() == 0) {
                noHasDeviceMode();
                isCurrentDeviceHasPlat = false;
            } else {
                isCurrentDeviceHasPlat = true;
                setParamLinear(paramMap,category);
            }
        } else if (DeviceType.RWBL.equals(category)) {//微波炉
            if (Utils.getDefaultMicrowave()!=null){
                type = Utils.getDefaultMicrowave().getDt();
                typeDp = Utils.getDefaultMicrowave().getDp();
            }else{
                typeDp = cookStep.getjs_PlatformCodes().get(0).platCode;
            }
            paramMap = RecipeUtil.getRecipeStepParamByDP(cookStep, typeDp);
            if (paramMap == null || paramMap.size() == 0) {
                noHasDeviceMode();
                isCurrentDeviceHasPlat = false;
            } else {
                isCurrentDeviceHasPlat = true;
                setParamLinear(paramMap,category);
            }

        } else if (DeviceType.RZKY.equals(category)) {
            if (Utils.getDefaultSteameOven()!=null){
                type = Utils.getDefaultSteameOven().getDt();
                typeDp =  Utils.getDefaultSteameOven().getDp();
            }else {
                typeDp = cookStep.getjs_PlatformCodes().get(0).platCode;
            }
            paramMap = RecipeUtil.getRecipeStepParamByDP(cookStep, typeDp);
            if (paramMap == null || paramMap.size() == 0) {
                noHasDeviceMode();
                isCurrentDeviceHasPlat = false;
            } else {
                isCurrentDeviceHasPlat = true;
                setParamLinear(paramMap,category);
            }
        }
    }

    /**
     * 设置参数
     */
    private void setParamLinear(Map<String, paramCode> map) {
        recipe_step_linear_model.setVisibility(VISIBLE);
        recipe_step_linear_model.removeAllViews();
        if (DeviceType.RDKX.equals(cookStep.dc)) {//电烤箱
            hasDeviceMode();
            //解析数据，并写入页面
            paramCode paramCode =   map.get(MsgParams.OvenMode);
            recipe_step_linear_model.addView(newLinear(paramCode.codeName, paramCode.valueName, false));
            for (String key : map.keySet()) {
                if (!MsgParams.OvenMode.equals(key)) {
                    recipe_step_linear_model.addView(newLine());
                    paramCode param = map.get(key);
                    if (MsgParams.OvenTime.equals(key)){
                        recipe_step_linear_model.addView(newLinear(param.codeName, param.value + "", true));
                    }else {
                        if (param.value == 0){

                        }else {
                            recipe_step_linear_model.addView(newLinear(param.codeName, param.value + "°c", false));
                        }
                    }
                }
            }

        } else if (DeviceType.RZQL.equals(cookStep.dc)) {//蒸汽炉
            hasDeviceMode();
            //解析数据，并写入页面
            short i = 0;
            for (String key : map.keySet()) {
                if (i != 0)
                    recipe_step_linear_model.addView(newLine());
                i++;
                paramCode param = map.get(key);
                if (MsgParams.SteamTime.equals(key))
                    recipe_step_linear_model.addView(newLinear(param.codeName, param.value + "", true));
                else
                    recipe_step_linear_model.addView(newLinear(param.codeName, param.value + "°c", false));
            }
        } else if (DeviceType.RWBL.equals(cookStep.dc)) {//微波炉
            hasDeviceMode();
            //解析数据，并写入页面
            paramCode paramCode = map.get(MsgParams.MicroWaveMode);

            recipe_step_linear_model.addView(newLinear(paramCode.codeName, paramCode.valueName, false));
            for (String key : map.keySet()) {

                if (!MsgParams.MicroWaveMode.equals(key)) {
                    recipe_step_linear_model.addView(newLine());
                    paramCode param = map.get(key);
                    if (MsgParams.MicroWaveTime.equals(key))//时间
                        recipe_step_linear_model.addView(newLinear(param.codeName, param.value + "", true));
                    else if (MsgParams.MicroWavePower.equals(key))//功率
                        recipe_step_linear_model.addView(newLinear("火力", param.value + "", false));
                    else if (MsgParams.MicroWaveWeight.equals(key))//重量
                        recipe_step_linear_model.addView(newLinear(param.codeName, param.value + "g", false));
                }
            }

        } else if (DeviceType.RXDG.equals(cookStep.dc)) {//消毒柜

        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventUtils.regist(this);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventUtils.unregist(this);
    }
}