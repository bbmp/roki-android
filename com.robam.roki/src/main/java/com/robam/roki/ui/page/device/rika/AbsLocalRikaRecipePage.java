package com.robam.roki.ui.page.device.rika;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.roki.R;
import com.robam.roki.model.device.rika.CookBookTag;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter.LocalRikaBookAdapter;
import com.robam.roki.ui.dialog.AbsSettingDialog;
import com.robam.roki.utils.CommonItemDecoration;
import com.robam.roki.utils.TestDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: lixin
 * @email: lx86@myroki.com
 * @date: 2020/5/28.
 * @PS:
 */
public class AbsLocalRikaRecipePage extends Fragment {
    ArrayList<CookBookTag> tags;
    RecyclerView rv_view;
    private String guid;
    private AbsRika rika;
    private String mode;
    long userId = Plat.accountService.getCurrentUserId();

    public static Fragment instance(ArrayList<CookBookTag> tags, String guid) {
        AbsLocalRikaRecipePage fragment = new AbsLocalRikaRecipePage();
        if (tags != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("tags", tags);
            bundle.putString(PageArgumentKey.Guid, guid);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.local_rika_cook_item, null);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_view = view.findViewById(R.id.rv_view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            tags = bundle.getParcelableArrayList("tags");
            guid = bundle.getString(PageArgumentKey.Guid);
            rika = Plat.deviceService.lookupChild(guid);
        }
        if (tags != null && tags.size() > 0) {
            LocalRikaBookAdapter localBookAdapter = new LocalRikaBookAdapter(getContext(), tags);
            rv_view.setAdapter(localBookAdapter);
            rv_view.setHasFixedSize(true);
            //创建StaggeredGridLayoutManager实例
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            // 绑定布局管理器
            rv_view.addItemDecoration(new CommonItemDecoration(20, 20));
            rv_view.setLayoutManager(layoutManager);
            rv_view.setItemAnimator(null);

            localBookAdapter.setOnItemRecyclerClick(new LocalRikaBookAdapter.OnItemRecyclerClick() {
                @Override
                public void onItemClick(View v, int position) {
                    String functionParams = tags.get(position).functionParams;
                    String functionCode = tags.get(position).functionCode;
                    sendMul(functionCode);
                    try {
                        JSONArray minute;
                        String defaultMinute;
                        JSONObject jsonObject = new JSONObject(functionParams);
                        if (IRokiFamily.RIKAY.equals(rika.getDp())) {
                            mode = jsonObject.getJSONObject("model").getString("value");
                            minute = jsonObject.getJSONObject("setTime").getJSONArray("value");
                            defaultMinute = jsonObject.getJSONObject("setTimeDef").getString("value");
                        } else {
                            JSONObject params = jsonObject.getJSONObject("params");
                            mode = params.getJSONObject("mode").getString("value");
                            minute = params.getJSONObject("minute").getJSONArray("value");
                            defaultMinute = params.getJSONObject("defaultMinute").getString("value");
//                        String hasDb = params.getJSONObject("hasDb").getString("value");
                        }
                        List<Integer> times = new ArrayList<>();
                        for (int i = 0; i < minute.length(); i++) {
                            Integer time = (Integer) minute.get(i);
                            times.add(time);
                        }
                        final List<Integer> modeDataTime = TestDatas.createModeDataTime(times);

                        if ("".equals(mode) || !isNumeric(mode)) {
                            return;
                        }
//                        if (isUsePlat(mode)) {
//                            //需要插入挡板
//                            if (oven.PlatInsertStatueValue==0) {
//                                ToastUtils.show(hasDb, Toast.LENGTH_SHORT);
//                            }else{
                                setDurationTime(modeDataTime, Integer.parseInt(defaultMinute), "分钟", mode);
//                            }
//
//                        }else{
//                            //不需要插入挡板
//                            if (oven.PlatInsertStatueValue==0) {
//                                setDurationTime(modeDataTime, Integer.parseInt(defaultMinute), "分钟");
//                            }else{
//                                ToastUtils.show(hasDb, Toast.LENGTH_SHORT);
//                            }
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (absSettingDialog != null) {
            absSettingDialog.dismiss();
        }
    }

    AbsSettingDialog absSettingDialog;

    private void setDurationTime(final List<Integer> listTempture, int num, String str, final String mode) {
        LogUtils.i("201912061", listTempture.toString());
        List<String> listButton = TestDatas.createButtonText(str, "取消", "开始工作", null);
        absSettingDialog = new AbsSettingDialog<Integer>(getContext(), listTempture, listButton, num);
        absSettingDialog.show(absSettingDialog);
        absSettingDialog.setListener(new AbsSettingDialog.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(Object index) {
                send2(mode, (int)index);

            }
        });
    }

    //发送统计
    private void sendMul(String code) {
        CloudHelper.getReportCode(userId, guid, code, rika.getDt(), new Callback<Reponses.GetReportResponse>() {
            @Override
            public void onSuccess(Reponses.GetReportResponse getReportResponse) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    public void send1(final int setTime) {

//        if (rika.status == OvenStatus.Off) {
//            rika.setOvenStatus(OvenStatus.On, new VoidCallback() {
//                @Override
//                public void onSuccess() {
//                    setAutoMode((short) setTime);
//
//                }
//
//                @Override
//                public void onFailure(Throwable t) {
//
//                }
//            });
//        } else {
//            setAutoMode((short) setTime);
//        }
    }

    private void setAutoMode(short setTime) {
//        oven.setOvenAutoRunMode(Short.decode(mode), setTime, new VoidCallback() {
//            @Override
//            public void onSuccess() {
//                LogUtils.i("20191108", "成功"+mode);
//                UIService.getInstance().popBack();
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                LogUtils.i("20191023", t.getMessage());
//            }
//        });

    }


    private boolean isUsePlat(String mode) {

        Short modeDecode = Short.decode(mode);


        boolean isReturn = false;
        //全腔模式 不需要挡板
        if (modeDecode <= 32) {
            isReturn = false;
        } else if (modeDecode > 32) {
            isReturn = true;
        }

        return isReturn;
    }


    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    private void send2(final String mode, final int time) {
        if (rika.steamOvenWorkStatus != RikaStatus.STEAMOVEN_ON) {
            rika.setSteamWorkStatus((short) 1, RikaStatus.STEAMQVEN_CATEGORYCODE, (short) 1,
                    (short) 49, (short) 1, RikaStatus.STEAMOVEN_ON, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            rika.setSteamOvenAutoRecipeModel(MsgKeys.setRikaOveAutoRecipe_Req, (short) 1, RikaStatus.STEAMQVEN_CATEGORYCODE, (short)1,
                                    (short)49, (short)2, Short.parseShort(mode), (short) time, new VoidCallback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                        }
                                    });
                        }

                        @Override
                        public void onFailure(Throwable t) {
                        }
                    });
        } else {
            rika.setSteamOvenAutoRecipeModel(MsgKeys.setRikaOveAutoRecipe_Req, (short) 1, RikaStatus.STEAMQVEN_CATEGORYCODE, (short)1,
                    (short)49, (short)2, Short.parseShort(mode), (short) time, new VoidCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
        }
    }

}

