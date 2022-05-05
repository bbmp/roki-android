package com.robam.roki.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.robam.common.pojos.device.Steamoven.Steam209;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.common.ui.UiHelper;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.DeviceWorkMsg;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.view.SpecialWheelView;
import com.robam.roki.ui.view.TemlWheelView;
import com.robam.roki.ui.view.TimeWheelView;
import com.robam.roki.utils.DialogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rosicky on 15/12/15.
 */
public class DeviceSteamOvenSettingPage extends HeadPage {


    Steam209 steam;
    //    SpecialListAdatper adatper;
//    View.OnClickListener clickListener;
    List<String> specials;

    int selected = -1;

    public View view;

    private short time;
    private short temp;
    private short cookbook;

    private Map<String, Short> cookbookMap = new HashMap<String, Short>();

    @InjectView(R.id.wheelView)
    LinearLayout wheelView;
    @InjectView(R.id.btnConfirm)
    TextView btnConfirm;
    @InjectView(R.id.wv1)
    SpecialWheelView wv1;
    @InjectView(R.id.wv2)
    TemlWheelView wv2;
    @InjectView(R.id.wv3)
    TimeWheelView wv3;
    IRokiDialog mRokiDialog = null;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        steam = Plat.deviceService.lookupChild(guid);

        contentView = inflater.inflate(R.layout.page_device_steamoven_setting,
                container, false);
        ButterKnife.inject(this, contentView);
        wv1.setOnSelectListener(wv1_Listener);
        wv1.setDefault(0);
        initData();
        mRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
        return contentView;
    }

    private void initData() {
        cookbookMap.put(cx.getString(R.string.device_steam_model_roulei), (short) 2);
        cookbookMap.put(cx.getString(R.string.device_steam_model_shucai), (short) 7);
        cookbookMap.put(cx.getString(R.string.device_steam_model_shuizhengdan), (short) 4);
        cookbookMap.put(cx.getString(R.string.device_oven_model_haixian), (short) 3);
        cookbookMap.put(cx.getString(R.string.device_steam_model_gaodian), (short) 5);
        cookbookMap.put(cx.getString(R.string.device_steam_model_miantiao), (short) 8);
        cookbookMap.put(cx.getString(R.string.device_steam_model_tijin), (short) 6);
        cookbookMap.put(cx.getString(R.string.device_steamOvenOne_name_jiedong), (short) 9);
        specials = new ArrayList<String>();
        specials.add(cx.getString(R.string.device_steam_model_shucai));
        specials.add(cx.getString(R.string.device_steam_model_shuizhengdan));
        specials.add(cx.getString(R.string.device_steam_model_roulei));
        specials.add(cx.getString(R.string.device_oven_model_haixian));
        specials.add(cx.getString(R.string.device_steam_model_gaodian));
        specials.add(cx.getString(R.string.device_steam_model_miantiao));
        specials.add(cx.getString(R.string.device_steam_model_tijin));
        specials.add(cx.getString(R.string.device_steamOvenOne_name_jiedong));
        wv1.setData(specials);
        wv1.setDefault(0);
    }


    private String tempPar;

    private void hanToEnglish(SpecialWheelView wv1) {
        if (cx.getString(R.string.device_steam_model_roulei).equals(wv1.getSelectedText())) {
            tempPar = "Meat";
        } else if (cx.getString(R.string.device_steam_model_shucai).equals(wv1.getSelectedText())) {
            tempPar = "Vegetables";
        } else if (cx.getString(R.string.device_steam_model_tijin).equals(wv1.getSelectedText())) {
            tempPar = "Sinew";
        } else if (cx.getString(R.string.device_steam_model_shuizhengdan).equals(wv1.getSelectedText())) {
            tempPar = "Steamedegg";
        } else if (cx.getString(R.string.device_oven_model_haixian).equals(wv1.getSelectedText())) {
            tempPar = "Seafood";
        } else if (cx.getString(R.string.device_steam_model_miantiao).equals(wv1.getSelectedText())) {
            tempPar = "Noodles";
        } else if (cx.getString(R.string.device_steam_model_gaodian).equals(wv1.getSelectedText())) {
            tempPar = "Pastry";
        } else if (cx.getString(R.string.device_steamOvenOne_name_jiedong).equals(wv1.getSelectedText())) {
            tempPar = "Unfreeze";
        }
    }

    SpecialWheelView.OnSelectListener wv1_Listener = new SpecialWheelView.OnSelectListener() {
        @Override
        public void endSelect(int index, Object item) {
            List<?> list1 = getList2(item);
            List<?> list2 = getList3(item);
            wv2.setData(list1);
            wv3.setData(list2);
            int def1 = 0, def2 = 0;
            if (index == 0) {
                def1 = 3;
                def2 = 35;
            } else if (index == 1) {
                def1 = 10;
                def2 = 20;
            } else if (index == 2) {
                def1 = 15;
                def2 = 40;
            } else if (index == 3 || index == 4) {
                def1 = 10;
                def2 = 25;
            } else if (index == 5) {
                def1 = 5;
                def2 = 25;
            } else if (index == 6) {
                def1 = 10;
                def2 = 55;
            } else if (index == 7) {
                def1 = 0;
                def2 = 35;
            }
            wv2.setDefault(def1);
            wv3.setDefault(def2);
        }

        @Override
        public void selecting(int index, Object item) {
        }
    };

    protected List<?> getList2(Object item) {
        List<Integer> list = Lists.newArrayList();
        String s = (String) item;
        if (s.equals(cx.getString(R.string.device_steam_model_shucai))) {
            for (int i = 95; i <= 100; i++) {
                list.add(i);
            }
        } else if (s.equals(cx.getString(R.string.device_steam_model_shuizhengdan))) {
            for (int i = 85; i <= 95; i++) {
                list.add(i);
            }
        } else if (s.equals(cx.getString(R.string.device_steam_model_roulei))) {
            for (int i = 85; i <= 100; i++) {
                list.add(i);
            }
        } else if (s.equals(cx.getString(R.string.device_oven_model_haixian))) {
            for (int i = 75; i <= 95; i++) {
                list.add(i);
            }
        } else if (s.equals(cx.getString(R.string.device_steam_model_gaodian))) {
            for (int i = 85; i <= 95; i++) {
                list.add(i);
            }
        } else if (s.equals(cx.getString(R.string.device_steam_model_miantiao))) {
            for (int i = 85; i <= 95; i++) {
                list.add(i);
            }
        } else if (s.equals(cx.getString(R.string.device_steam_model_tijin))) {
            for (int i = 90; i <= 100; i++) {
                list.add(i);
            }
        } else if (s.equals(cx.getString(R.string.device_steamOvenOne_name_jiedong))) {
            for (int i = 55; i <= 65; i++) {
                list.add(i);
            }
        }
        return list;
    }

    protected List<?> getList3(Object item) {
        List<Integer> list = Lists.newArrayList();
        String s = (String) item;
        if (s.equals(cx.getString(R.string.device_steam_model_shucai))) {
            for (int i = 5; i <= 45; i++) {
                list.add(i);
            }
        } else if (s.equals(cx.getString(R.string.device_steam_model_shuizhengdan))) {
            for (int i = 5; i <= 45; i++) {
                list.add(i);
            }
        } else if (s.equals(cx.getString(R.string.device_steam_model_roulei))) {
            for (int i = 5; i <= 60; i++) {
                list.add(i);
            }
        } else if (s.equals(cx.getString(R.string.device_oven_model_haixian))) {
            for (int i = 5; i <= 45; i++) {
                list.add(i);
            }
        } else if (s.equals(cx.getString(R.string.device_steam_model_gaodian))) {
            for (int i = 5; i <= 45; i++) {
                list.add(i);
            }
        } else if (s.equals(cx.getString(R.string.device_steam_model_miantiao))) {
            for (int i = 5; i <= 45; i++) {
                list.add(i);
            }
        } else if (s.equals(cx.getString(R.string.device_steam_model_tijin))) {
            for (int i = 5; i <= 60; i++) {
                list.add(i);
            }
        } else if (s.equals(cx.getString(R.string.device_steamOvenOne_name_jiedong))) {
            for (int i = 5; i <= 60; i++) {
                list.add(i);
            }
        }
        return list;
    }

    @OnClick(R.id.btnConfirm)
    public void onClickConfirm() {
        hanToEnglish(wv1);
        if (steam.status == SteamStatus.AlarmStatus) {
            mRokiDialog.setContentText(R.string.device_alarm_ststus);
            mRokiDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
            mRokiDialog.show();
            return;
        } else if (steam.doorState == 0) {
            mRokiDialog.setContentText(R.string.device_alarm_gating_content);
            mRokiDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
            mRokiDialog.show();
            return;
        }

        if (UiHelper.checkAuthWithDialog(getContext(), PageKey.UserLogin)) {
            time = Short.valueOf(wv3.getSelectedText());
            temp = Short.valueOf(wv2.getSelectedText());
            cookbook = cookbookMap.get(wv1.getSelectedText());
            Bundle bundle = new Bundle();
            DeviceWorkMsg msg = new DeviceWorkMsg();
            msg.setType(wv1.getSelectedText());
            msg.setMode(cookbookMap.get(wv1.getSelectedText()));
//            msg.setType(specials.get(selected - 3));
//            msg.setMode(cookbookMap.get(specials.get(selected - 3)));
            msg.setTemperature(wv2.getSelectedText());
            msg.setTime(wv3.getSelectedText());
            bundle.putSerializable("msg", msg);
            bundle.putString(PageArgumentKey.Guid, steam.getID());
            UIService.getInstance().postPage(PageKey.DeviceSteamWorking, bundle);
        }
    }
//
//    class SpecialListAdatper extends BaseAdapter {
//
//        Context context;
//        View.OnClickListener listener;
//        List<String> list;
//        int length;
//
//        public SpecialListAdatper(Context context, View.OnClickListener listener, List<String> specials) {
//            this.context = context;
//            this.listener = listener;
//            this.list = specials;
//            length = list.size();
//        }
//
//        @Override
//        public int getCount() {
//            return 7 + length;
//        }
//
//        @Override
//        public Boolean getItem(int i) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            view = LayoutInflater.from(context).inflate(R.layout.view_steam_oven_set_list_item, null);
//            ImageView img = (ImageView) view.findViewById(R.id.img);
//            if (i > 2 && i < 11) {
//                img.setImageResource(chooseRes(i - 3, selected == i));
////                img.setBackground(getResources().getDrawable(chooseRes(i - 3, false)));
//                view.setTag(i);
//                view.setOnClickListener(listener);
//            }
//            return view;
//        }
//    }

//    private int chooseRes(int i, boolean isSelected) {
//        int ans = 0;
//        switch (i) {
//            case 0:
////                ans = R.drawable.selector_steam_vege;
//                ans = isSelected ? R.mipmap.img_steamoven_vege_selected : R.mipmap.img_steamoven_vege_unselect;
//                break;
//            case 1:
//                ans = isSelected ? R.mipmap.img_steamoven_egg_selected : R.mipmap.img_steamoven_egg_unselect;
//                break;
//            case 2:
//                ans = isSelected ? R.mipmap.img_steamoven_meat_selected : R.mipmap.img_steamoven_meat_unselect;
//                break;
//            case 3:
//                ans = isSelected ? R.mipmap.img_steamoven_seafood_selected : R.mipmap.img_steamoven_seafood_unselect;
//                break;
//            case 4:
//                ans = isSelected ? R.mipmap.img_steamoven_cake_selected : R.mipmap.img_steamoven_cake_unselect;
//                break;
//            case 5:
//                ans = isSelected ? R.mipmap.img_steamoven_noddle_selected : R.mipmap.img_steamoven_noddle_unselect;
//                break;
//            case 6:
//                ans = isSelected ? R.mipmap.img_steamoven_sinew_selected : R.mipmap.img_steamoven_sinew_unselect;
//                break;
//            case 7:
//                ans = isSelected ? R.mipmap.img_steamoven_jiedong_selected : R.mipmap.img_steamoven_jiedong_unselect;
//                break;
//        }
//        return ans;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
