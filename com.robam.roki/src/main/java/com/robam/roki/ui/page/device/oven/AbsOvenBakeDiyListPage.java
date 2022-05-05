package com.robam.roki.ui.page.device.oven;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.listener.OnRecyclerViewItemLongClickListener;
import com.robam.roki.model.bean.DeviceOvenDiyParams;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.OvenBakeDiyListAdapter;
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
 * RQ035烤箱 烘烤diy列表页
 */

public class AbsOvenBakeDiyListPage extends BasePage {


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

    long userId = Plat.accountService.getCurrentUserId();
    String dt;
    String dc;
    protected String mGuid;
    protected AbsOven oven;
    private List<DiyCookbookList> diyCookbookLists;
    private OvenBakeDiyListAdapter ovenBakeDiyListAdapter;
    private String mTitle;
    private List<DeviceConfigurationFunctions> mDatas;
    public static AbsOvenBakeDiyListPage instance = null;
    List<DeviceOvenDiyParams> deviceList = new ArrayList<>();
    private List<Integer> keyList;
    private IRokiDialog dialogByType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.abs_oven_bake_list_page, container, false);
        ButterKnife.inject(this, view);
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        mTitle = bd == null ? null : bd.getString(PageArgumentKey.title);
        oven = Plat.deviceService.lookupChild(mGuid);
        instance = this;
        initData();
        return view;
    }

    public void initFresh() {
        getListData();
    }

    private void initData() {
        if (oven != null) {
            dt = oven.getDt();
            dc = oven.getDc();
        }

        for (int i = 0; i < mDatas.size(); i++) {
            if ("diy".equals(mDatas.get(i).functionCode)) {
                String functionParams = mDatas.get(i).functionParams;
                try {
                    JSONObject jsonObject = new JSONObject(functionParams);
                    JSONObject model = jsonObject.getJSONObject("model");
                    String modelStr = model.toString();
                    Map<String, HashMap<String, DeviceOvenDiyParams>> json2Map = JsonUtils.getJson2Map(modelStr);
                    keyList = new ArrayList<>();

                    Set<String> strings = json2Map.keySet();
                    for (String str : strings) {
                        keyList.add(Integer.valueOf(str));
                    }
                    Collections.sort(keyList);

                    for (int j = 0; j < keyList.size(); j++) {
                        JSONObject obj = model.getJSONObject(keyList.get(j) + "");
                        String value = obj.getString("value");
                        String hasRotate = obj.getString("hasRotate");
                        String img = obj.getString("img");
                        String paramType = obj.getString("paramType");
                        String defaultTemp = obj.getString("defaultTemp");
                        String defaultMinute = obj.getString("defaultMinute");
                        JSONArray timeArray = obj.getJSONObject("time").getJSONArray("value");
                        JSONArray tempArray = obj.getJSONObject("temp").getJSONArray("value");
                        DeviceOvenDiyParams deviceOvenDiyParams = new DeviceOvenDiyParams();
                        deviceOvenDiyParams.setValue(value);
                        deviceOvenDiyParams.setHasRotate(hasRotate);
                        deviceOvenDiyParams.setParamType(paramType);
                        deviceOvenDiyParams.setDefaultTemp(defaultTemp);
                        deviceOvenDiyParams.setDefaultMinute(defaultMinute);
                        deviceOvenDiyParams.setTimeList(timeJson2List(timeArray));
                        deviceOvenDiyParams.setTempList(tempJson2List(tempArray));
                        deviceOvenDiyParams.setImg(img);
                        deviceList.add(deviceOvenDiyParams);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtils.i("20191115", e.getMessage());
                }


            }
        }
        getListData();
        tvName.setText(mTitle);
    }

    private List<Integer> timeJson2List(JSONArray array) throws JSONException {
        List<Integer> timelist = new ArrayList<>();
        for (int j = 0; j < array.length(); j++) {
            Integer tim = (Integer) array.get(j);
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        if (dialogByType != null) {
            dialogByType.dismiss();
        }
    }


    private void getListData() {
        if (diyCookbookLists != null) {
            diyCookbookLists.clear();
        }
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
        ovenBakeDiyListAdapter = new OvenBakeDiyListAdapter(cx,diyCookbookLists, deviceList, keyList, new OnRecyclerViewItemClickListener() {
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

    private void ItemEvent(View view) {
        DiyCookbookList diyCookbookList = diyCookbookLists.get((Integer) view.getTag());
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, mGuid);
        bd.putSerializable(PageArgumentKey.Bean, diyCookbookList);
        bd.putSerializable(PageArgumentKey.List, (Serializable) mDatas);
        UIService.getInstance().postPage(PageKey.AbsOvenBakeDiyDetail, bd);

    }

    @OnClick({R.id.mode_back, R.id.rl_add, R.id.iv_add_new})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mode_back:
                UIService.getInstance().popBack();
                break;
            case R.id.rl_add:
            case R.id.iv_add_new:
                Bundle bundle = new Bundle();
                bundle.putString(PageArgumentKey.Guid, mGuid);
                bundle.putSerializable(PageArgumentKey.List, (Serializable) mDatas);
                UIService.getInstance().postPage(PageKey.AbsOvenBakeEdit, bundle);

                break;
        }
    }


}
