package com.robam.roki.ui.page.device.steamovenone;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.DiyCookbookList;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenModeName;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.listener.OnRecyclerViewItemLongClickListener;
import com.robam.roki.model.bean.DeviceOvenDiyParams;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.OvenBakeDiyListAdapter;
import com.robam.roki.ui.adapter.SteamOvenDiyListAdapter;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.JsonUtils;
import com.robam.roki.utils.TestDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * c915 c908
 * diy菜谱
 */
public class DiyCookbookListPage extends BasePage {
    @InjectView(R.id.mode_back)
    ImageView modeBack;
    @InjectView(R.id.tv_name)
    TextView tvName;

    @InjectView(R.id.rv_bake_list)
    RecyclerView rvBakeList;

    @InjectView(R.id.rl_add)
    RelativeLayout rlAdd;

    @InjectView(R.id.ll_add)
    LinearLayout llAdd;

    @InjectView(R.id.iv_add_new)
    ImageView ivAddNew;
    String mGuid;
    private DeviceConfigurationFunctions mDatas;
    private String mTitle;
    AbsSteameOvenOne absSteameOvenOne;
    public static DiyCookbookListPage instance = null;
    long userId = Plat.accountService.getCurrentUserId();
    String dt;
    String dc;
    private List<Integer> keyList;
    private List<DiyCookbookList> diyCookbookLists;
    List<DeviceOvenDiyParams> deviceList = new ArrayList<>();
    private SteamOvenDiyListAdapter ovenBakeDiyListAdapter;
    private IRokiDialog dialogByType;
    private String diyType="";
    private String needDescalingParams;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_diy_cookbook, container, false);
        ButterKnife.inject(this, view);
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        mDatas = bd == null ? null : (DeviceConfigurationFunctions) bd.getSerializable(PageArgumentKey.Bean);
        mTitle = bd == null ? null : bd.getString(PageArgumentKey.title);
        absSteameOvenOne = Plat.deviceService.lookupChild(mGuid);
        needDescalingParams = bd == null ? null : bd.getString(PageArgumentKey.descaling);
        instance = this;
        initData();
        return view;
    }

    private void initData() {
        if (absSteameOvenOne != null) {
            dt = absSteameOvenOne.getDt();
            dc = absSteameOvenOne.getDc();
        }
        String functionParams = mDatas.functionParams;
        try {
            JSONObject jsonObject = new JSONObject(functionParams);
            JSONObject model = jsonObject.optJSONObject("model");

            String modelStr = model.toString();
            Map<String, HashMap<String, DeviceOvenDiyParams>> json2Map = JsonUtils.getJson2Map(modelStr);
            keyList = new ArrayList<>();
            Set<String> strings = json2Map.keySet();
            for (String str : strings) {
                keyList.add(Integer.valueOf(str));
            }
            Collections.sort(keyList);
            for (int j = 0; j < keyList.size(); j++) {
                DeviceOvenDiyParams deviceOvenDiyParams = new DeviceOvenDiyParams();
                JSONObject obj = model.getJSONObject(keyList.get(j) + "");
                String value = obj.getString("value");
                String img = obj.getString("img");

                deviceOvenDiyParams.setCode(keyList.get(j));
                deviceOvenDiyParams.setValue(value);
                JSONObject diyTypeObj = jsonObject.optJSONObject("diyType");
                if (diyTypeObj!=null) {
                    diyType = diyTypeObj.optString("value");
                }
                //EXP
                if (keyList.get(j) == 10) {

                    JSONArray upTempArray = obj.optJSONObject("upTemp").optJSONArray("value");
                    String upTempDefault = obj.optJSONObject("upTempDefault").optString("value");
                    JSONArray downTempArray = obj.optJSONObject("downTemp").optJSONArray("value");
                    String downTempDefault = obj.optJSONObject("downTempDefault").optString("value");
                    JSONArray minute = obj.optJSONObject("minute").optJSONArray("value");
                    String minuteDefault = obj.optJSONObject("minuteDefault").optString("value");
                    String tempDiff = obj.optJSONObject("tempDiff").optString("value");
                    String tempStart = obj.optJSONObject("tempStart").optString("value");
                    String tempMin = obj.optJSONObject("tempMin").optString("value");
                    deviceOvenDiyParams.setUpTemp(tempJson2List(upTempArray));
                    deviceOvenDiyParams.setUpTempDefault(upTempDefault);
                    deviceOvenDiyParams.setDownTemp(tempJson2List(downTempArray));
                    deviceOvenDiyParams.setDownTempDefault(downTempDefault);
                    deviceOvenDiyParams.setMinute(timeJson2List(minute));
                    deviceOvenDiyParams.setTimeList(timeJson2List(minute));
                    deviceOvenDiyParams.setMinuteDefault(minuteDefault);
                    deviceOvenDiyParams.setDefaultMinute(minuteDefault);
                    deviceOvenDiyParams.setTempDiff(tempDiff);
                    deviceOvenDiyParams.setTempStart(tempStart);
                    deviceOvenDiyParams.setTempMin(tempMin);
                    deviceOvenDiyParams.setImg(img);
                } else {
                    String defaultTemp = obj.optString("defaultTemp");
                    String defaultMinute = obj.optString("defaultMinute");
                    JSONArray timeArray = obj.optJSONObject("time").optJSONArray("value");
                    JSONArray tempArray = obj.optJSONObject("temp").optJSONArray("value");

                    deviceOvenDiyParams.setDefaultTemp(defaultTemp);
                    deviceOvenDiyParams.setDefaultMinute(defaultMinute);
                    deviceOvenDiyParams.setTimeList(timeJson2List(timeArray));
                    deviceOvenDiyParams.setTempList(tempJson2List(tempArray));
                    deviceOvenDiyParams.setImg(img);

                }

                deviceList.add(deviceOvenDiyParams);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        getListData();
        tvName.setText(mTitle);
    }


    private List<Integer> timeJson2List(JSONArray time) throws JSONException {
        List<Integer> timelist = new ArrayList<>();
        for (int j = 0; j < time.length(); j++) {
            Integer tim = (Integer) time.get(j);
            timelist.add(tim);
        }

        return TestDatas.createModeDataTime(timelist);
    }

    private List<Integer> tempJson2List(JSONArray temp) throws JSONException {
        List<Integer> templist = new ArrayList<>();
        for (int j = 0; j < temp.length(); j++) {
            Integer tem = (Integer) temp.get(j);
            templist.add(tem);
        }

        return TestDatas.createModeDataTemp(templist);
    }

    public void initFresh() {
        getListData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dialogByType != null) {
            dialogByType.dismiss();
        }
    }

    //获取菜谱
    private void getListData() {
        if (diyCookbookLists != null) {
            diyCookbookLists.clear();
        }
        LogUtils.i("20200520", "userId::" + userId);
        LogUtils.i("20200520", "dt::" + dt);
        RokiRestHelper.getDiyRecipe(userId, dt, new Callback<Reponses.GetRecipeDiyCookbook>() {
            @Override
            public void onSuccess(Reponses.GetRecipeDiyCookbook getRecipeDiyCookbook) {
                diyCookbookLists = getRecipeDiyCookbook.diyCookbookLists;
                if (diyCookbookLists.size() == 0) {
                    rvBakeList.setVisibility(View.GONE);
                    rlAdd.setVisibility(View.VISIBLE);
                } else {
                    rvBakeList.setVisibility(View.VISIBLE);
                    rlAdd.setVisibility(View.GONE);
                    setAdapter();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();

            }
        });
    }

    private void setAdapter() {
        LogUtils.i("2020062802","deviceList::"+deviceList);
        LogUtils.i("2020062802","keyList::"+keyList);
        ovenBakeDiyListAdapter = new SteamOvenDiyListAdapter(cx, diyCookbookLists, deviceList, keyList, new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view) {
                ItemEvent(view);
            }
        }, new OnRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClick(View view) {
                DiyCookbookList diyCookbookList = diyCookbookLists.get((Integer) view.getTag());
                int id = diyCookbookList.id;
                showDialog((long) id);
            }
        }
        );
        rvBakeList.setAdapter(ovenBakeDiyListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(cx,
                LinearLayoutManager.VERTICAL, false);
        rvBakeList.setLayoutManager(linearLayoutManager);
    }


    private void ItemEvent(View view) {
        DiyCookbookList diyCookbookList = diyCookbookLists.get((Integer) view.getTag());
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.title, "编辑菜谱");
        bd.putString(PageArgumentKey.Guid, mGuid);
        bd.putSerializable(PageArgumentKey.List, (Serializable) deviceList);
        bd.putSerializable(PageArgumentKey.Bean, diyCookbookList);
        bd.putString(PageArgumentKey.descaling, needDescalingParams);
        bd.putSerializable(PageArgumentKey.diyType, diyType.equals("")?"0":"1");
        UIService.getInstance().postPage(PageKey.SteamOvenDiyDetail, bd);

    }


    private void showDialog(final long id) {
        dialogByType = new RokiDialogFactory().createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        dialogByType.setTitleText("提示");
        dialogByType.setContentText("确定删除吗？");
        dialogByType.setOkBtn("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecipe(id);
                dialogByType.dismiss();
            }
        });

        dialogByType.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogByType.dismiss();
            }
        });

        dialogByType.show();
    }


    //删除菜谱
    private void deleteRecipe(Long userId) {
        RokiRestHelper.Delete035Recipe(userId, new Callback<Reponses.Update035Recipe>() {
            @Override
            public void onSuccess(Reponses.Update035Recipe update035Recipe) {
                ToastUtils.show("删除成功", Toast.LENGTH_SHORT);
                getListData();

            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();

            }
        });

    }


    @OnClick({R.id.mode_back, R.id.iv_add_new, R.id.rl_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mode_back:
                UIService.getInstance().popBack();
                break;
            case R.id.iv_add_new:
            case R.id.rl_add:
                Bundle bundle = new Bundle();
                bundle.putString(PageArgumentKey.title, "新增菜谱");
                bundle.putString(PageArgumentKey.Guid, mGuid);
                bundle.putSerializable(PageArgumentKey.List, (Serializable) deviceList);
                bundle.putSerializable(PageArgumentKey.diyType, diyType.equals("")?"0":"1");
                UIService.getInstance().postPage(PageKey.SteamOvenEdit, bundle);
                break;
        }
    }
}
