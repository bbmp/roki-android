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

import com.legent.dao.DaoHelper;
import com.legent.plat.Plat;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.SteamUserAction;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.Steam228;
import com.robam.common.pojos.device.Steamoven.Steam275;
import com.robam.common.pojos.device.Steamoven.SteamMode;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.utils.DialogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
/**
 * Created by yinwei on 2017/9/7.
 */

public class Steam228RecentlyUseDialog extends AbsDialog {
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.listview)
    ListView listView;

    public static Steam228RecentlyUseDialog dlg;
    Context cx;
    private View customView;
    AbsSteamoven steam;
    int  positionlocal=-1;
    List<SteamUserAction> list=new ArrayList<>();
    List<SteamUserAction> listNew=new ArrayList<>();
    long userid= Plat.accountService.getCurrentUserId();
    private int last_item;
    private View oldView=null;
    IRokiDialog rokiDialog=null;
    public Steam228RecentlyUseDialog(Context context,AbsSteamoven steam) {
        super(context, R.style.Dialog_Micro_FullScreen);
        this.cx=context;
        if ("RS275".equals(steam.getDt())){
            this.steam = steam;
        }else{
            this.steam = steam;
        }
        try{
            this.listNew = DaoHelper.getWhereEq(SteamUserAction.class,"userid",userid);
            LogUtils.i("20170824","listNew::"+this.listNew.size());
            if (listNew.size()>3){
                for (int i=0;i<listNew.size()-3;i++){
                    try {
                        DaoHelper.deleteWhereEq(SteamUserAction.class,"timeDate",listNew.get(i).getTimeDate());
                    }catch (Exception e){
                        Log.e("Oven028recently","error:"+e.getMessage());
                    }
                }
            }
            this.list= DaoHelper.getWhereEq(SteamUserAction.class,"userid",userid);
            Collections.reverse(this.list);
            init();
        }catch (Exception e){
            Log.e("Oven028recently","error:"+e.getMessage());
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
        rokiDialog= RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_02);
        title.setText("最近使用");
        listView.setAdapter(new Steam228RecentlyUseDialog.OvenAdapter(cx, list));
        //点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionlocal=position;
                View itemView=view;
                itemView.setBackgroundColor(Color.parseColor("#252525"));
                LogUtils.i("20170828","last_item::"+last_item);
                if (last_item != -1 && last_item != position){
                    if (oldView == null){
                        //为空不进行任何操作
                    }else{
                        LogUtils.i("20170828","position::"+position);
                        oldView.setBackgroundColor(Color.parseColor("#00000000"));
                    }
                }
                oldView = itemView;
                last_item = position;
            }
        });
    }

    //返回
    @OnClick(R.id.imgreturn)
    public void onClickBack(){
        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    //开始烹饪
    @OnClick(R.id.start_cook)
    public void onClickStartCook(){
        if (steam.doorState==0){
            rokiDialog.setTitleText("门未关好");
            rokiDialog.setContentText("门未关好，请检查并确认关好门");
            rokiDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rokiDialog.dismiss();
                }
            });
            rokiDialog.setCanceledOnTouchOutside(false);
            rokiDialog.show();
            return;
        }

        if (list.size()==0){
            ToastUtils.show("无最近使用的参数", Toast.LENGTH_SHORT);
            return;
        }else{

            if (listener!=null){
                if (positionlocal==-1){
                    ToastUtils.show("请选择下发的参数",Toast.LENGTH_SHORT);
                    return;
                }else{
                    listener.onConfirm(list.get(positionlocal));
                }
            }
        }

        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    static public Steam228RecentlyUseDialog show(Context context, AbsSteamoven  steam) {
        dlg = new  Steam228RecentlyUseDialog(context,steam);
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

        void onConfirm(SteamUserAction steamuserAction);
    }

    private  Steam228RecentlyUseDialog.PickListener listener;

    public void setPickListener( Steam228RecentlyUseDialog.PickListener listener) {
        this.listener = listener;
    }

    private class OvenAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater inflater;
        private List<SteamUserAction> list=new ArrayList<SteamUserAction>();
        public  OvenAdapter(Context mContext, List<SteamUserAction> list){
            this.mContext=mContext;
            inflater=LayoutInflater.from(mContext);
            this.list=list;
        }

        @Override
        public int getCount() {
            return list==null ? 0 : list.size();
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
            TextView time = convertView.findViewById(R.id.txtTime);//时间
            ImageView imgMode = convertView.findViewById(R.id.imgMode);//图标
            TextView temp = convertView.findViewById(R.id.oven_recently_temp);//火力和重量
            TextView timecook= convertView.findViewById(R.id.oven_recently_time);//单位
            String[] strTime=list.get(position).getTimeDate().split(",");
            time.setText(strTime[0]+"\n"+strTime[1]);
            imgMode.setImageResource(getModelText(position).res);

            temp.setVisibility(View.VISIBLE);
            temp.setText(list.get(position).getTemperature()+"℃");
            timecook.setText(list.get(position).getTimeCook()+"");
            return convertView;
        }
    }

    public Steam228RecentlyUseDialog.ModelInfo getModelText(int position){
        Steam228RecentlyUseDialog.ModelInfo info = new Steam228RecentlyUseDialog.ModelInfo();
        int res = 0;
        switch (list.get(position).getMode()){
            case SteamMode.FRESHSTEAM://鲜嫩蒸
                info.res = R.mipmap.img_xianneizheng;
                break;
            case SteamMode.NATRITIVE://营养蒸
                info.res = R.mipmap.yingyag_icon;
                break;
            case SteamMode.STRONG_STEAM://强力蒸
                if("RS275".equals(steam.getDt())){
                    info.res = R.mipmap.ic_275_suzheng;
                }else{
                    info.res = R.mipmap.qiangli_icon;
                }
                break;
            case SteamMode.FASTSTEAMSLOWSTEAM://快蒸慢炖
                info.res = R.mipmap.img_steam228_recently_faststeamslow;
                break;
            case SteamMode.UNFREEZE://解冻
                info.res = R.mipmap.img_steam228_recently_unfreeze;
                break;
            case SteamMode.KEEPTEMTURE://保温
                info.res = R.mipmap.img_oven028_baowen_mode;
                break;
            case SteamMode.FERMENT://发酵
                info.res = R.mipmap.faxiao_icon;
                break;
            case SteamMode.STERLIZERSTEAM ://杀菌
                info.res = R.mipmap.shajun_icon;
                break;
            //275新增加的一些
            case SteamMode.SEAFOOD://鱼类
                info.res = R.mipmap.ic_275_yu;
                break;
            case SteamMode.STEAMEDBREAD://面食
                info.res = R.mipmap.ic_275_mianshi;
                break;
            case SteamMode.EGG://蛋类
                info.res = R.mipmap.ic_275_dan;
                break;
            case SteamMode.VEGE://蔬菜
                info.res = R.mipmap.ic_275_shucai;
                break;
            case SteamMode.SteamClean:
                info.res = R.mipmap.ic_275_chugou;
                break;
            default:
                break;
        }
        return info;
    }

    class ModelInfo {
        int res;
    }
}
