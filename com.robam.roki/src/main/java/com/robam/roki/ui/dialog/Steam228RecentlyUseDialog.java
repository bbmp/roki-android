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
        title.setText("????????????");
        listView.setAdapter(new Steam228RecentlyUseDialog.OvenAdapter(cx, list));
        //????????????
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionlocal=position;
                View itemView=view;
                itemView.setBackgroundColor(Color.parseColor("#252525"));
                LogUtils.i("20170828","last_item::"+last_item);
                if (last_item != -1 && last_item != position){
                    if (oldView == null){
                        //???????????????????????????
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

    //??????
    @OnClick(R.id.imgreturn)
    public void onClickBack(){
        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    //????????????
    @OnClick(R.id.start_cook)
    public void onClickStartCook(){
        if (steam.doorState==0){
            rokiDialog.setTitleText("????????????");
            rokiDialog.setContentText("??????????????????????????????????????????");
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
            ToastUtils.show("????????????????????????", Toast.LENGTH_SHORT);
            return;
        }else{

            if (listener!=null){
                if (positionlocal==-1){
                    ToastUtils.show("????????????????????????",Toast.LENGTH_SHORT);
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
            TextView time = convertView.findViewById(R.id.txtTime);//??????
            ImageView imgMode = convertView.findViewById(R.id.imgMode);//??????
            TextView temp = convertView.findViewById(R.id.oven_recently_temp);//???????????????
            TextView timecook= convertView.findViewById(R.id.oven_recently_time);//??????
            String[] strTime=list.get(position).getTimeDate().split(",");
            time.setText(strTime[0]+"\n"+strTime[1]);
            imgMode.setImageResource(getModelText(position).res);

            temp.setVisibility(View.VISIBLE);
            temp.setText(list.get(position).getTemperature()+"???");
            timecook.setText(list.get(position).getTimeCook()+"");
            return convertView;
        }
    }

    public Steam228RecentlyUseDialog.ModelInfo getModelText(int position){
        Steam228RecentlyUseDialog.ModelInfo info = new Steam228RecentlyUseDialog.ModelInfo();
        int res = 0;
        switch (list.get(position).getMode()){
            case SteamMode.FRESHSTEAM://?????????
                info.res = R.mipmap.img_xianneizheng;
                break;
            case SteamMode.NATRITIVE://?????????
                info.res = R.mipmap.yingyag_icon;
                break;
            case SteamMode.STRONG_STEAM://?????????
                if("RS275".equals(steam.getDt())){
                    info.res = R.mipmap.ic_275_suzheng;
                }else{
                    info.res = R.mipmap.qiangli_icon;
                }
                break;
            case SteamMode.FASTSTEAMSLOWSTEAM://????????????
                info.res = R.mipmap.img_steam228_recently_faststeamslow;
                break;
            case SteamMode.UNFREEZE://??????
                info.res = R.mipmap.img_steam228_recently_unfreeze;
                break;
            case SteamMode.KEEPTEMTURE://??????
                info.res = R.mipmap.img_oven028_baowen_mode;
                break;
            case SteamMode.FERMENT://??????
                info.res = R.mipmap.faxiao_icon;
                break;
            case SteamMode.STERLIZERSTEAM ://??????
                info.res = R.mipmap.shajun_icon;
                break;
            //275??????????????????
            case SteamMode.SEAFOOD://??????
                info.res = R.mipmap.ic_275_yu;
                break;
            case SteamMode.STEAMEDBREAD://??????
                info.res = R.mipmap.ic_275_mianshi;
                break;
            case SteamMode.EGG://??????
                info.res = R.mipmap.ic_275_dan;
                break;
            case SteamMode.VEGE://??????
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
