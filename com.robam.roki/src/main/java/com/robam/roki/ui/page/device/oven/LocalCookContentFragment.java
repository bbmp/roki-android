package com.robam.roki.ui.page.device.oven;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.LocalBookAdapter;
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


public class LocalCookContentFragment extends Fragment {
    ArrayList<CookBookTag> tags;
    RecyclerView rv_view;
    private String guid;
    private AbsOven oven;
    private String mode;

    public static Fragment instance(ArrayList<CookBookTag> tags, String guid) {
        LocalCookContentFragment fragment = new LocalCookContentFragment();
        if (tags != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("tags", tags);
            bundle.putString(PageArgumentKey.Guid, guid);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.local_cook_item, null);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_view = view.findViewById(R.id.rv_view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            tags = (ArrayList<CookBookTag>) bundle.getSerializable("tags");
            guid = bundle.getString(PageArgumentKey.Guid);
            oven = Plat.deviceService.lookupChild(guid);
        }
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
                    String functionParams = tags.get(position).functionParams;
                    try {
                        JSONObject jsonObject = new JSONObject(functionParams);
                        JSONObject params = jsonObject.getJSONObject("params");
                        mode = params.getJSONObject("mode").getString("value");
                        JSONArray minute = params.getJSONObject("minute").getJSONArray("value");
                        String defaultMinute = params.getJSONObject("defaultMinute").getString("value");

                        if (guid.contains("Q082A")) {
                            List<Integer> times = new ArrayList<>();
                            for (int i = 0; i < minute.length(); i++) {
                                Integer time = (Integer) minute.get(i);
                                times.add(time);
                            }
                            final List<Integer> modeDataTime = TestDatas.createModeDataTime(times);

                            if ("".equals(mode) || !isNumeric(mode)) {
                                return;
                            }
                            setDurationTime(modeDataTime, Integer.parseInt(defaultMinute), "分钟");
                        } else {

                            List<Integer> times = new ArrayList<>();
                            for (int i = 0; i < minute.length(); i++) {
                                Integer time = (Integer) minute.get(i);
                                times.add(time);
                            }
                            final List<Integer> modeDataTime = TestDatas.createModeDataTime(times);

                            if ("".equals(mode) || !isNumeric(mode)) {
                                return;
                            }
                            if (guid.contains("RQ035")) {
                                if (isUsePlat(mode)) {
                                    String hasDb = params.getJSONObject("").getString("value");
                                    //需要插入挡板
                                    if (oven.PlatInsertStatueValue == 0) {
                                        ToastUtils.show(hasDb, Toast.LENGTH_SHORT);
                                    } else {
                                        setDurationTime(modeDataTime, Integer.parseInt(defaultMinute), "分钟");
                                    }

                                } else {
                                    String hasDb = params.getJSONObject("").getString("value");
                                    //不需要插入挡板
                                    if (oven.PlatInsertStatueValue == 0) {
                                        setDurationTime(modeDataTime, Integer.parseInt(defaultMinute), "分钟");
                                    } else {
                                        ToastUtils.show(hasDb, Toast.LENGTH_SHORT);
                                    }
                                }
                            }

                        }


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

    private void setDurationTime(final List<Integer> listTempture, int num, String str) {
        List<String> listButton = TestDatas.createButtonText(str, "取消", "开始工作", null);
        absSettingDialog = new AbsSettingDialog<Integer>(getContext(), listTempture, listButton, num);
        absSettingDialog.show(absSettingDialog);
        absSettingDialog.setListener(new AbsSettingDialog.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(Object index) {
                send1((int) index);

            }
        });
    }


    public void send1(final int setTime) {

        if (oven.status == OvenStatus.Off) {
            oven.setOvenStatus(OvenStatus.On, new VoidCallback() {
                @Override
                public void onSuccess() {
                    setAutoMode((short) setTime);

                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        } else {
            setAutoMode((short) setTime);
        }
    }

    private void setAutoMode(short setTime) {
        oven.setOvenAutoRunMode(Short.decode(mode), setTime, new VoidCallback() {
            @Override
            public void onSuccess() {
                UIService.getInstance().popBack();
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20191023", t.getMessage());
                ToastUtils.show("指令下发失败了哟,请重新下发", Toast.LENGTH_SHORT);
            }
        });

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
        return isNum.matches();
    }


}
