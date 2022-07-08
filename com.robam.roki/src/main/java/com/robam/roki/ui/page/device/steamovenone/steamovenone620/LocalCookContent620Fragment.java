package com.robam.roki.ui.page.device.steamovenone.steamovenone620;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamOvenOpenDoorSteamEvent;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.CommonItemDecoration;
import com.robam.roki.ui.adapter.LocalBookAdapter;
import com.robam.roki.ui.adapter3.Rv610LocalRecipe2Adapter;
import com.robam.roki.ui.adapter3.Rv920LocalRecipeTitleAdapter;
import com.robam.roki.ui.dialog.AbsSettingDialog;
import com.robam.roki.ui.page.device.oven.CookBookTag;
import com.robam.roki.ui.page.device.steamovenone.Local610CookbookDetailPage;
import com.robam.roki.ui.page.device.steamovenone.steamovenone920.Local620CookbookDetailActivity;
import com.robam.roki.ui.page.device.steamovenone.steamovenone920.LocalCookbook620Activity;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TestDatas;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalCookContent620Fragment extends Fragment {
    ArrayList<CookBookTag> tags;
    RecyclerView rv_view;
    private String guid;
    private String needDescalingParams;
    private String dt;
    private AbsSteameOvenOneNew absSteameOvenOne;
    private String mode;
    private IRokiDialog descalingTipsDialog;

    private RecyclerView titleRecycleView;
    private Bundle bundle;

    public static Fragment instance(ArrayList<CookBookTag> tags, String guid,String needDescalingParams) {
        com.robam.roki.ui.page.device.steamovenone.LocalCookContentFragment fragment = new com.robam.roki.ui.page.device.steamovenone.LocalCookContentFragment();
        if (tags != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("tags", tags);
            bundle.putString(PageArgumentKey.Guid, guid);
            bundle.putString(PageArgumentKey.descaling, needDescalingParams);
            fragment.setArguments(bundle);
        }
        return fragment;
    }


    public static Fragment instance(ArrayList<CookBookTag> tags, String guid,String needDescalingParams , String dt) {
        LocalCookContent620Fragment fragment = new LocalCookContent620Fragment();
        if (tags != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("tags", tags);
            bundle.putString(PageArgumentKey.Guid, guid);
            bundle.putString(PageArgumentKey.descaling, needDescalingParams);
            bundle.putString(PageArgumentKey.dt, dt);
            fragment.setArguments(bundle);
        }
        return fragment;
    }
    public static Fragment instance(ArrayList<CookBookTag> tags, String guid,String needDescalingParams , String dt ,String title) {

        LocalCookContent620Fragment fragment = new LocalCookContent620Fragment();
        if (tags != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("tags", tags);
            bundle.putString(PageArgumentKey.Guid, guid);
            bundle.putString(PageArgumentKey.descaling, needDescalingParams);
            bundle.putString(PageArgumentKey.dt, dt);
            bundle.putString(PageArgumentKey.title, title);
            fragment.setArguments(bundle);
        }
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.local_cook_item, null);
    }

    boolean allTimeUseTag;
    private static final String TAG = "LocalCookConte";
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_view = view.findViewById(R.id.rv_view);

//        titleRecycleView=view.findViewById(R.id.ry_title);
         bundle = getArguments();
        if (bundle != null) {

            tags = (ArrayList<CookBookTag>) bundle.getSerializable("tags");
            guid = bundle.getString(PageArgumentKey.Guid);
            needDescalingParams = bundle.getString(PageArgumentKey.descaling);
            dt = bundle.getString(PageArgumentKey.dt);
            absSteameOvenOne = Plat.deviceService.lookupChild(guid);
        }
        if ("DB610".equals(dt) || "B610D".equals(dt)|| "DB620".equals(dt)||"cq920".equals(dt)){
            rv_view.setLayoutManager(new GridLayoutManager(getContext() , 2 , RecyclerView.VERTICAL ,false));
            Rv610LocalRecipe2Adapter rv610LocalRecipeAdapter = new Rv610LocalRecipe2Adapter();
            rv_view.setAdapter(rv610LocalRecipeAdapter);



            rv610LocalRecipeAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull @NotNull View view, int i) {
                   Bundle  bundle = getArguments();
                    bundle.putInt(Local610CookbookDetailPage.POSITION, i);
                    if ("cq920".equalsIgnoreCase(dt)) {
                        Intent intent=new Intent(getActivity(), Local620CookbookDetailActivity.class);
                        intent.putExtras(bundle);
                        getActivity().startActivityForResult(intent,1000);
                    }else{
                        UIService.getInstance().postPage(PageKey.Local620CookbookDetailPage, bundle);
                    }
                }
            });
            rv610LocalRecipeAdapter.addData(tags);
        }else {
            Log.e(TAG,"进来了");
            if (tags != null && tags.size() > 0) {
                LocalBookAdapter localBookAdapter = new LocalBookAdapter(getContext(), tags);
                rv_view.setAdapter(localBookAdapter);
                rv_view.setHasFixedSize(true);
                //创建StaggeredGridLayoutManager实例
                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                // 绑定布局管理器
                rv_view.addItemDecoration(new CommonItemDecoration(20, 20));
                rv_view.setLayoutManager(layoutManager);
                rv_view.setItemAnimator(null);

                localBookAdapter.setOnItemRecyclerClick(new LocalBookAdapter.OnItemRecyclerClick() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Log.e(TAG,"进来了点击 ");
                        String functionParams = tags.get(position).functionParams;


                        try {
                            JSONObject params = new JSONObject(functionParams);
                            mode = params.getJSONObject("model").getString("value");
                            String timeDef = params.getJSONObject("setTimeDef").getString("value");
                            JSONArray timeArray = params.getJSONObject("setTime").getJSONArray("value");
                            List<Integer> times = new ArrayList<>();
                            for (int i = 0; i < timeArray.length(); i++) {
                                Integer time = (Integer) timeArray.get(i);
                                times.add(time);
                            }
                            final List<Integer> modeDataTime = TestDatas.createModeDataTime(times);
                            if ("".equals(mode) || !isNumeric(mode)) {
                                return;
                            }
                            int defaultMinIndex = 0;
//                        for (int i = 0; i < modeDataTime.size(); i++) {
//                            if (String.valueOf(modeDataTime.get(i)).equals(timeDef)) {
//                                defaultMinIndex = i;
//                            }
//                        }
                            defaultMinIndex = Integer.parseInt(timeDef);

                            if (absSteameOvenOne.doorStatusValue == 1) {
                                ToastUtils.show("门未关好，请检查并确认关好门", Toast.LENGTH_SHORT);
                                return;
                            }

                            if ("".equals(params.optString("allTimeUse"))) {
                                allTimeUseTag = false;
                            } else {
                                allTimeUseTag = true;
                            }
                            //0 不需要 1 需要
//                        if (absSteameOvenOne.weatherDescalingValue != 0) {
//                            if (allTimeUseTag) {
//                                setDurationTime(modeDataTime, defaultMinIndex, "分钟");
//                            } else {
//                                descalingDialog();
//                            }
//                        } else {
                            setDurationTime(modeDataTime, defaultMinIndex, "分钟",Long.parseLong(mode));
//                        }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (absSettingDialog != null) {
            absSettingDialog.dismiss();
        }
        if (descalingTipsDialog!=null) {
            descalingTipsDialog.dismiss();
        }
    }

    AbsSettingDialog absSettingDialog;

    private void setDurationTime(final List<Integer> listTempture, int num, String str,long id) {
        List<String> listButton = TestDatas.createButtonText(str, "取消", "开始工作", null);
        absSettingDialog = new AbsSettingDialog<Integer>(getContext(), listTempture, listButton, num);
        absSettingDialog.show(absSettingDialog);
        absSettingDialog.setListener(new AbsSettingDialog.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(Object index) {
                LogUtils.i("201909091111", "index::" + (int) index);
                sendCommand((int) index,id);
            }
        });
    }




    public void sendCommand(final int setTime,long id) {
        if (absSteameOvenOne.powerState == 2||absSteameOvenOne.powerState==1||absSteameOvenOne.powerState==0) {

            setLocalRecipeCommand(setTime,id);
//            absSteameOvenOne.setSteameOvenStatus_on(new VoidCallback() {
//                @Override
//                public void onSuccess() {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            setLocalRecipeCommand(setTime);
//                        }
//                    }, 500);
//
//
//                }
//
//                @Override
//                public void onFailure(Throwable t) {
//                    LogUtils.i("20200319", "开机：" + t.getMessage());
//                }
//            });
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


    private void setLocalRecipeCommand(int setTime,long id) {
        int arg = 0;
        if (setTime > 255) {
            arg = 1;
        }
        absSteameOvenOne.setSteamOvenAutoRecipe((int) id, setTime*60, new VoidCallback() {

            @Override
            public void onSuccess() {
                UIService.getInstance().popBack();
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("2020040704", "localRecipe:::fail:::" + t.getMessage());
            }
        });

    }
    //开门事件
    @Subscribe
    public void onEvent(SteamOvenOpenDoorSteamEvent event) {
        if (absSteameOvenOne == null || !Objects.equal(absSteameOvenOne.getID(), event.pojo.getID()))
            return;
        absSteameOvenOne = (AbsSteameOvenOneNew) event.pojo;
    }



    private void descalingDialog() {
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
        descalingTipsDialog = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_02);
        descalingTipsDialog.setTitleText(descalingTitle);
        descalingTipsDialog.setContentText(descalingContent);
        descalingTipsDialog.setOkBtn(descalingButton, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (descalingTipsDialog != null) {
                    descalingTipsDialog.dismiss();
                }
            }
        });
        descalingTipsDialog.show();


    }

}

