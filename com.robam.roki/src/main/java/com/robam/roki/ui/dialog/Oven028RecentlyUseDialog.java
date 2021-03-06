package com.robam.roki.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.legent.dao.DaoHelper;
import com.legent.plat.Plat;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.pojos.OvenUserAction;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.Oven028;
import com.robam.common.pojos.device.Oven.OvenMode;
import com.robam.roki.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by yinwei on 2017/8/19.
 */

public class Oven028RecentlyUseDialog extends AbsDialog {
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.listview)
    ListView listView;
    @InjectView(R.id.start_cook)
    TextView start_cook;

    public static Oven028RecentlyUseDialog dlg;
    Context cx;
    private View customView;
    AbsOven oven;
    int positionlocal = -1;
    List<OvenUserAction> list = new ArrayList<>();
    List<OvenUserAction> listNew = new ArrayList<>();
    long userid = Plat.accountService.getCurrentUserId();
    private int last_item;
    private View oldView = null;

    public Oven028RecentlyUseDialog(Context context, AbsOven oven) {
        super(context, R.style.Dialog_Micro_FullScreen);
        this.cx = context;
        if (IRokiFamily.RR075.equals(oven.getDt())) {
            this.oven = oven;
        } else {
            this.oven = oven;
        }

        try {
            this.listNew = DaoHelper.getWhereEq(OvenUserAction.class, "userid", userid);
            LogUtils.i("20170824", "userId::" + userid + "listNew::" + this.listNew.size());
            if (listNew.size() > 3) {
                for (int i = 0; i < listNew.size() - 3; i++) {
                    try {
                        DaoHelper.deleteWhereEq(OvenUserAction.class, "timeDate", listNew.get(i).getTimeDate());
                    } catch (Exception e) {
                        Log.e("Oven028recently", "error:" + e.getMessage());
                    }
                }
            }
            this.list = DaoHelper.getWhereEq(OvenUserAction.class, "userid", userid);
            Collections.reverse(this.list);
            init();
        } catch (Exception e) {
            Log.e("Oven028recently", "error:" + e.getMessage());
        }
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_micro526_recetly_use;
    }

    @Override
    protected void initView(View view) {
        this.customView = view;
    }

    private void init() {
        ButterKnife.inject(this, customView);
        title.setText("????????????");
        start_cook.setText("??????");
        listView.setAdapter(new OvenAdapter(cx, list));
        //????????????
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionlocal = position;
                View itemView = view;
                itemView.setBackgroundColor(Color.parseColor("#252525"));
                LogUtils.i("20170828", "last_item::" + last_item);
                if (last_item != -1 && last_item != position) {
                    if (oldView == null) {
                        //???????????????????????????
                    } else {
                        LogUtils.i("20170828", "position::" + position);
                        oldView.setBackgroundColor(Color.parseColor("#00000000"));
                    }
                }
                oldView = itemView;
                last_item = position;
            }
        });
    }

    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {
        LogUtils.i("20171717", "event::" + event.pojo.getID());
    }

    //??????
    @OnClick(R.id.imgreturn)
    public void onClickBack() {
        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    //????????????
    @OnClick(R.id.start_cook)
    public void onClickStartCook() {
        if (list.size() == 0) {
            ToastUtils.show("????????????????????????", Toast.LENGTH_SHORT);
            return;
        } else {

            if (listener != null) {
                if (positionlocal == -1) {
                    ToastUtils.show("????????????????????????", Toast.LENGTH_SHORT);
                    return;
                } else {
                    listener.onConfirm(list.get(positionlocal));
                }
            }
        }

        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    static public Oven028RecentlyUseDialog show(Context context, AbsOven oven) {
        dlg = new Oven028RecentlyUseDialog(context, oven);
        Window win = dlg.getWindow();
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(wl);
        dlg.show();
        return dlg;
    }

    public interface PickListener {

        void onCancel();

        void onConfirm(OvenUserAction ovenuserAction);
    }

    private Oven028RecentlyUseDialog.PickListener listener;

    public void setPickListener(Oven028RecentlyUseDialog.PickListener listener) {
        this.listener = listener;
    }

    private class OvenAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater inflater;
        private List<OvenUserAction> list = new ArrayList<OvenUserAction>();

        public OvenAdapter(Context mContext, List<OvenUserAction> list) {
            this.mContext = mContext;
            inflater = LayoutInflater.from(mContext);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.dialog_oven028_recently_use_item, null);
            TextView time = convertView.findViewById(R.id.txtTime);//??????
            ImageView imgMode = convertView.findViewById(R.id.imgMode);//??????
            TextView tempUp = convertView.findViewById(R.id.oven_recently_tempUp);//????????????
            TextView temp = convertView.findViewById(R.id.oven_recently_temp);//???????????????
            TextView tempDown = convertView.findViewById(R.id.oven_recently_tempDown);
            TextView timecook = convertView.findViewById(R.id.oven_recently_time);//??????
            String[] strTime = list.get(position).getTimeDate().split(",");
            time.setText(strTime[0] + "\n" + strTime[1]);
            imgMode.setImageResource(getModelText(position).res);
            if ("????????????".equals(list.get(position).getName())) {
                tempUp.setVisibility(View.GONE);
                tempDown.setVisibility(View.GONE);
                temp.setVisibility(View.VISIBLE);
                temp.setText("??????");
            } else {
                if (!"EXP".equals(list.get(position).getName())) {
                    tempUp.setVisibility(View.GONE);
                    tempDown.setVisibility(View.GONE);
                    temp.setVisibility(View.VISIBLE);
                    temp.setText(list.get(position).getTemperature() + "???");
                } else {
                    temp.setVisibility(View.GONE);
                    tempUp.setVisibility(View.VISIBLE);
                    tempDown.setVisibility(View.VISIBLE);
                    tempUp.setText(list.get(position).getTemperUp() + "???");
                    tempDown.setText(list.get(position).getTemperDown() + "???");
                }
            }

            timecook.setText(list.get(position).getTimeCook() + "");
            return convertView;
        }
    }


    /*public ModelInfo getModel075Text(int position){
        ModelInfo info = new ModelInfo();
        int res = 0;
        switch (list.get(position).getMode()){
            case OvenMode.KUAIRE://??????
                info.res = R.mipmap.ic_075_kuaire;
                break;
            case OvenMode.FENGBEIKAO://?????????
                info.res = R.mipmap.ic_075_fengbeikao;
                break;
            case OvenMode.BEIKAO://??????
                info.res = R.mipmap.ic_075_beikao;
                break;
            case OvenMode.DIJIARE://?????????
                info.res = R.mipmap.ic_075_dijiare;
                break;
            case OvenMode.FENGSHANKAO://?????????
                info.res = R.mipmap.ic_075_fengshankao;
                break;
            case OvenMode.KAOSHAO://??????
                info.res = R.mipmap.ic_075_kaosao;
                break;
            case OvenMode.QIANGKAOSHAO://?????????
                info.res = R.mipmap.ic_075_qiangkaosao;
                break;
            case OvenMode.EXP://EXP
                info.res = R.mipmap.ic_075_exp;
                break;
            default:
                break;
        }
        return info;
    }*/

    public ModelInfo getModelText(int position) {
        ModelInfo info = new ModelInfo();
        int res = 0;
        if ("????????????".equals(list.get(position).getName())) {
            switch (list.get(position).getMode()) {
                case OvenMode.BEEF://??????
                    info.res = R.mipmap.ic_026ovenmain_beef_white;
                    break;
                case OvenMode.BREAD://??????
                    info.res = R.mipmap.ic_026ovenmain_bread_white;
                    break;
                case OvenMode.BISCUITS://??????
                    info.res = R.mipmap.ic_026ovenmain_biscuits_white;
                    break;
                case OvenMode.CHICKENWINGS://??????
                    info.res = R.mipmap.ic_026ovenmain_chicken_white;
                    break;
                case OvenMode.CAKE://??????
                    info.res = R.mipmap.ic_026ovenmain_cake_white;
                    break;
                case OvenMode.PIZZA://??????
                    info.res = R.mipmap.ic_026ovenmain_pizza_white;
                    break;
                case OvenMode.GRILLEDSHRIMP://???
                    info.res = R.mipmap.ic_026ovenmain_shrimp_white;
                    break;
                case OvenMode.ROASTFISH://??????
                    info.res = R.mipmap.ic_026ovenmain_fish_white;
                    break;
                case OvenMode.SWEETPOTATO://??????
                    info.res = R.mipmap.ic_026ovenmain_sweetpotato_white;
                    break;
                case OvenMode.CORN://??????
                    info.res = R.mipmap.ic_026ovenmain_corn_white;
                    break;
                case OvenMode.STREAKYPORK://?????????
                    info.res = R.mipmap.ic_026ovenmain_wuhuarou_white;
                    break;
                case OvenMode.VEGETABLES://??????
                    info.res = R.mipmap.ic_026ovenmain_vegetables_white;
                    break;
                default:
                    break;
            }
        } else {
            switch (list.get(position).getMode()) {
                case OvenMode.KUAIRE://??????
                    info.res = R.mipmap.ic_oven028_recently_fasetheat_new;
                    break;
                case OvenMode.FENGBEIKAO://?????????
                    info.res = R.mipmap.ic_oven028_recently_fengbeikao_new;
                    break;
                case OvenMode.BEIKAO://??????
                    info.res = R.mipmap.ic_oven028_recently_beikao;
                    break;
                case OvenMode.DIJIARE://?????????
                    info.res = R.mipmap.ic_oven028_recently_bottomheat;
                    break;
                case OvenMode.JIEDONG://??????
                    info.res = R.mipmap.ic_oven028_recently_unfreeze;
                    break;
                case OvenMode.FENGSHANKAO://?????????
                    info.res = R.mipmap.ic_oven028_recently_fengshankao;
                    break;
                case OvenMode.KAOSHAO://??????
                    info.res = R.mipmap.ic_oven028_recently_kaosao;
                    break;
                case OvenMode.QIANGKAOSHAO://?????????
                    info.res = R.mipmap.ic_oven028_recently_qingkaosao;
                    break;
                case OvenMode.EXP://EXP
                    info.res = R.mipmap.ic_oven028_recently_exp;
                    break;
                case OvenMode.KUAISUYURE://????????????
                    info.res = R.mipmap.ic_oven028_recently_kuaisuyure;
                    break;
                case OvenMode.JIANKAO://??????
                    info.res = R.mipmap.ic_oven028_recently_jiankao;
                    break;
                case OvenMode.GUOSHUHONGGAN://????????????
                    info.res = R.mipmap.ic_oven028_recently_guoshufry;
                    break;
                case OvenMode.FAJIAO://??????
                    info.res = R.mipmap.ic_oven028_recently_fa;
                    break;
                case OvenMode.SHAJUN://??????
                    info.res = R.mipmap.ic_oven028_recently_shajun;
                    break;
                case OvenMode.BAOWEN:
                    info.res = R.mipmap.img_oven028_baowen_mode;
                    break;
                default:
                    break;
            }
        }

        return info;
    }

    class ModelInfo {
        int res;
    }

}
