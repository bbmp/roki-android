package com.robam.roki.ui.dialog;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Rent on 2016/05/31.
 */
public class SelectDialog extends AbsDialog {
    @InjectView(R.id.list_device_se)
    ListView listView;

    Context cx;
    List<IDevice> list;
    SelectAdpater adpater;
    private static SelectDialog dlg;
    public SelectDialog(Context context,List<IDevice> list) {
        super(context, R.style.Dialog_FullScreen01);
        this.cx = context;
        this.list = list;
        init();
    }

    private void init(){
        adpater = new SelectAdpater(list);
        listView.setAdapter(adpater);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onConfirm(list.get(position));
                if (dlg!=null && dlg.isShowing()){
                    dlg.dismiss();
                }
            }
        });
    }

    @Override
    protected int getViewResId() {
        return R.layout.select_device_recipe;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    static public SelectDialog show(Context cx,List<IDevice> list) {
         dlg = new SelectDialog(cx,list);
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);

        Window win = dlg.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = (int) (dm.widthPixels * 0.6);
        lp.height = (int) (dm.heightPixels * 0.4);
        lp.alpha = 0.75f;
        win.setAttributes(lp);
        dlg.show();return dlg;
    }

    private int positionDevice;

    class SelectAdpater extends BaseAdapter {

        private List<IDevice> listAdapter;

        public SelectAdpater(List<IDevice> list){
            this.listAdapter=list;
        }

        @Override
        public int getCount() {
            return listAdapter.size();
        }

        @Override
        public Object getItem(int position) {
            return listAdapter.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            if (convertView==null){
                convertView=View.inflate(cx,R.layout.view_select_device_item,null);
                viewHolder= new ViewHolder();
                viewHolder.deviceName = convertView.findViewById(R.id.device_txt);
                convertView.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.deviceName.setText(listAdapter.get(position).getID().substring(1,5));
            positionDevice=position;
            return convertView;
        }
    }

    static class ViewHolder {
        TextView deviceName;
    }

    private PickListener listener;

    public interface PickListener{
        void onConfirm(IDevice dev);
    }

    public void setPickListener(PickListener listener) {
        this.listener = listener;
    }

}

