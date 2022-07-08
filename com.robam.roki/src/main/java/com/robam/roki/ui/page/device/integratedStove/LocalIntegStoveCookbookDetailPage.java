package com.robam.roki.ui.page.device.integratedStove;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamOvenOpenDoorSteamEvent;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.device.oven.CookBookTag;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.view.wheelview.LoopView;
import com.robam.roki.utils.TestDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.InjectView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 本地自动菜谱
 */
public class LocalIntegStoveCookbookDetailPage extends MyBasePage<MainActivity> {
    public final static String POSITION = "position" ;
    private MultiTransformation options = new MultiTransformation<>(new CenterCrop(),
            new RoundedCornersTransformation(20, 0));
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    private ImageView ivRecipeImg;
    private LoopView wheelViewRear;
    private AppCompatButton btnStart;

    String guid;
    String needDescalingParams;
    private AbsSteameOvenOne absSteameOvenOne;
    private List<CookBookTag> localCookbookList;
    private int position;
    private String mode;
    private String tab;
    private JSONObject params;

    @Override
    protected int getLayoutId() {
        return R.layout.page_local_cookbook_610_detail;
    }

    @Override
    protected void initView() {
        ivRecipeImg = (ImageView) findViewById(R.id.iv_recipe_img);
        wheelViewRear = (LoopView) findViewById(R.id.wheel_view_rear);
        btnStart = (AppCompatButton) findViewById(R.id.btn_start);
        setOnClickListener(btnStart ,ivBack);
    }

    @Override
    protected void initData() {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        localCookbookList = (List<CookBookTag>) bd.getSerializable("tags");
        needDescalingParams = bd == null ? null : bd.getString(PageArgumentKey.descaling);
        position = bd.getInt(POSITION);
        tab = bd.getString(PageArgumentKey.title);
        absSteameOvenOne = Plat.deviceService.lookupChild(guid);
        String functionParams = localCookbookList.get(position).functionParams;
        try {
            tvTitle.setText(localCookbookList.get(position).functionName);
                        GlideApp.with(getContext())
                    .load(localCookbookList.get(position).backgroundImgH)
                    .placeholder(R.mipmap.icon_recipe_default)
                    .error(R.mipmap.icon_recipe_default)
                    .apply(RequestOptions.bitmapTransform(options))
                    .into(ivRecipeImg
                    );
             params = new JSONObject(functionParams);
            mode = params.getJSONObject("model").getString("value");
            String timeDef = params.getJSONObject("setTimeDef").getString("value");
            JSONArray timeArray = params.getJSONObject("setTime").getJSONArray("value");
            List<Integer> times = new ArrayList<>();
            for (int i = 0; i < timeArray.length(); i++) {
                Integer time = (Integer) timeArray.get(i);
                times.add(time);
            }
            final List<String> modeDataTime = TestDatas.createModeDataTime2(times);
            if ("".equals(mode) || !isNumeric(mode)) {
                return;
            }
//            int defaultMinIndex = Integer.parseInt(timeDef);
            int defaultMinIndex = TestDatas.createModeDefTimePosition(modeDataTime ,timeDef);
            wheelViewRear.setItems(modeDataTime);
            wheelViewRear.setInitPosition(defaultMinIndex);
            wheelViewRear.setDividerColor(Color.parseColor("#e1e1e1"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * 开始工作
     */
    public void sendCommand() {
        if ("蒸功能".equals(tab) || "加湿烤".equals(tab)){
            if (absSteameOvenOne.WaterStatus == 1){
                ToastUtils.showShort(R.string.device_alarm_water_out);
                return;
            }
            if (absSteameOvenOne.alarm == 16 ) {
                ToastUtils.show("水箱缺水，请加水", Toast.LENGTH_SHORT);
                return;
            }
        }
        if ("多段组合".equals(tab)  && position != 3 ){
            if (absSteameOvenOne.WaterStatus == 1){
                ToastUtils.showShort(R.string.device_alarm_water_out);
                return;
            }
            if (absSteameOvenOne.alarm == 16 ) {
                ToastUtils.show("水箱缺水，请加水", Toast.LENGTH_SHORT);
                return;
            }
        }
        if (absSteameOvenOne.doorStatusValue == 1  ) {
            ToastUtils.show("门未关好，请检查并确认关好门", Toast.LENGTH_SHORT);
            return;
        }
        String itemsContent = wheelViewRear.getItemsContent(wheelViewRear.getSelectedItem());
        int setTime = Integer.parseInt(itemsContent);
        if (absSteameOvenOne.powerState == SteamOvenOnePowerStatus.Off ||
                absSteameOvenOne.powerState == SteamOvenOnePowerStatus.Wait
        ) {
            absSteameOvenOne.setSteameOvenStatus_on(new VoidCallback() {
                @Override
                public void onSuccess() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setLocalRecipeCommand(setTime);
                        }
                    }, 500);


                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("20200319", "开机：" + t.getMessage());
                }
            });
        } else {
            setLocalRecipeCommand(setTime);
        }
    }

    private void setLocalRecipeCommand(int setTime) {
        int arg = 0;
        if (setTime > 255) {
            arg = 1;
        }
        absSteameOvenOne.setLocalRecipeMode((short) arg, Short.parseShort(mode), setTime, (short) 0, new VoidCallback() {

            @Override
            public void onSuccess() {
                UIService.getInstance().popBack().popBack();
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("2020040704", "localRecipe:::fail:::" + t.getMessage());
            }
        });

    }

    /**
     * 开始时间监听
     * @param event
     */
    @Subscribe
    public void onEvent(SteamOvenOpenDoorSteamEvent event) {
        if (absSteameOvenOne == null || !Objects.equal(absSteameOvenOne.getID(), event.pojo.getID()))
            return;
        absSteameOvenOne = (AbsSteameOvenOne) event.pojo;
    }

    @Override
    public void onClick(View view) {
        if (view == ivBack){
            UIService.getInstance().popBack();
        }else if (view == btnStart){
            if (absSteameOvenOne.doorStatusValue == 1) {
                ToastUtils.show("门未关好，请检查并确认关好门", Toast.LENGTH_SHORT);
                return;
            }

            sendCommand();
        }
    }



    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
