package com.robam.roki.ui.page.device.steam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamModeName;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.listener.OnRecyclerViewItemLongClickListener;
import com.robam.roki.model.bean.DeviceSteamDiyParams;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.SteamDiyListAdapter;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.JsonUtils;

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
 * 235diy主页面
 */
public class SteamDiyListPage extends BasePage {
    @InjectView(R.id.mode_back)
    ImageView modeBack;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.iv_add_new)
    ImageView ivAddNew;
    @InjectView(R.id.rv_bake_list)
    RecyclerView rvBakeList;
    @InjectView(R.id.iv_add)
    ImageView ivAdd;
    @InjectView(R.id.rl_add)
    RelativeLayout rlAdd;


    String guid;
    List<DeviceConfigurationFunctions> mDatas = new ArrayList<>();
    private List<Integer> keyList;
    List<DeviceSteamDiyParams> deviceList = new ArrayList<>();

    private List<DiyCookbookList> diyCookbookLists;
    protected AbsSteamoven steam;
    String dt;
    String dc;
    long userId = Plat.accountService.getCurrentUserId();



    public static SteamDiyListPage instance = null;
    SteamDiyListAdapter steamDiyListAdapter;
    private IRokiDialog dialogByType;
    private String needDescalingParams;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.abs_steam_list_page, container, false);
        Bundle bd = getArguments();
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
        steam = Plat.deviceService.lookupChild(guid);
        needDescalingParams = bd == null ? null : bd.getString(PageArgumentKey.descaling);
        instance=this;
        ButterKnife.inject(this, view);
        initView();
        initData();
        return view;
    }

    private void initData() {
        tvName.setText(title);
        if (steam != null) {
            dt = steam.getDt();
            dc = steam.getDc();
        }
        for (int i = 0; i < mDatas.size(); i++) {
            String functionCode = mDatas.get(i).functionCode;
            //本地菜谱
            if (functionCode.equals(SteamModeName.diy)) {
                DeviceConfigurationFunctions deviceConfigurationFunctions = mDatas.get(i);
                String functionParams = deviceConfigurationFunctions.functionParams;
                try {
                    JSONObject jsonObject = new JSONObject(functionParams);
                    JSONObject model = jsonObject.getJSONObject("model");
                    String modelStr = model.toString();
                    Map<String, HashMap<String, DeviceSteamDiyParams>> json2Map = JsonUtils.getJson2Map(modelStr);

                    keyList = new ArrayList<>();

                    Set<String> strings = json2Map.keySet();
                    for (String str : strings) {
                        keyList.add(Integer.valueOf(str));
                    }
                    Collections.sort(keyList);
                    for (int j = 0; j < keyList.size(); j++) {
                        for (Map.Entry<String, HashMap<String, DeviceSteamDiyParams>> stringHashMapEntry : json2Map.entrySet()) {
                            if (String.valueOf(keyList.get(j)).equals(stringHashMapEntry.getKey())) {
                                HashMap<String, DeviceSteamDiyParams> value = stringHashMapEntry.getValue();
                                DeviceSteamDiyParams deviceOvenDiyParams = new DeviceSteamDiyParams();
                                deviceOvenDiyParams.setValue(String.valueOf(value.get("value")));
                                deviceOvenDiyParams.setParamType(String.valueOf(value.get("paramType")));
                                deviceOvenDiyParams.setImg(String.valueOf(value.get("img")));
                                deviceList.add(deviceOvenDiyParams);

                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }


        getListData();


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


    private void initView() {

    }


    private void setAdapter() {
        steamDiyListAdapter = new SteamDiyListAdapter(cx, steam, diyCookbookLists, deviceList,keyList, new OnRecyclerViewItemClickListener() {
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


        rvBakeList.setAdapter(steamDiyListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(cx,
                LinearLayoutManager.VERTICAL, false);
        rvBakeList.setLayoutManager(linearLayoutManager);
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        if (dialogByType!=null) {
            dialogByType.dismiss();
        }
    }

    @OnClick({R.id.mode_back, R.id.rl_add,R.id.iv_add_new})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mode_back:
                UIService.getInstance().popBack();
            break;
            case R.id.rl_add:
            case R.id.iv_add_new:
                Bundle bundle = new Bundle();
                bundle.putString(PageArgumentKey.Guid, guid);
                bundle.putString(PageArgumentKey.title, "新增菜谱");
                bundle.putSerializable(PageArgumentKey.List, (Serializable) mDatas);
                UIService.getInstance().postPage(PageKey.SteamDiyEdit, bundle);

            break;
        }
    }


    private void ItemEvent(View view) {
        DiyCookbookList diyCookbookList = diyCookbookLists.get((Integer) view.getTag());
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, guid);
        bd.putSerializable(PageArgumentKey.Bean, diyCookbookList);
        bd.putSerializable(PageArgumentKey.List, (Serializable) mDatas);
        bd.putString(PageArgumentKey.descaling, needDescalingParams);
        UIService.getInstance().postPage(PageKey.SteamDiyDetail, bd);

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

    public void initFresh() {
        getListData();
    }
}
