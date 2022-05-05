package com.robam.roki.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceCollectionChangedEvent;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.adapters.ExtBaseAdapter;
import com.legent.utils.LogUtils;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.view.EmojiEmptyView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by sylar on 15/6/14.
 * 厨电管理
 */
public class DeviceManagerPage extends MyBasePage<MainActivity> {

    @InjectView(R.id.emptyView)
    EmojiEmptyView emptyView;
    @InjectView(R.id.mainView)
    LinearLayout mainView;
    @InjectView(R.id.listview)
    ListView listview;
    Adapter adapter;
    @InjectView(R.id.img_back)
    ImageView imgBack;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Subscribe
    public void onEvent(DeviceCollectionChangedEvent event) {
        initData();
    }


    @OnItemClick(R.id.listview)
    public void onItemClickDevice(AdapterView<?> parent, View view, int position, long id) {
        IDevice dev = adapter.getEntity(position);
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, dev.getID());
        UIService.getInstance().postPage(PageKey.DeviceDetail, bd);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initData() {
        adapter = new Adapter();
        listview.setAdapter(adapter);
        boolean isEmpty = Plat.deviceService.isEmpty();
        switchView(isEmpty);
        if (isEmpty) return;
        List<IDevice> list = Plat.deviceService.queryAll();
        adapter.loadData(list);
    }


    void switchView(boolean isEmpty) {
        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        mainView.setVisibility(!isEmpty ? View.VISIBLE : View.GONE);
//        addDeviceView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
//        addDeviceView.setEnabled(isEmpty);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.page_device_manager, container, false);
//        ButterKnife.inject(this, view);
//        adapter = new Adapter();
//        listview.setAdapter(adapter);
////        initData();
//
//        return view;
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.page_device_manager;
    }

    @Override
    protected void initView() {

    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        UIService.getInstance().popBack();
    }


    class Adapter extends ExtBaseAdapter<IDevice> {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder vh;
            if (convertView == null) {
                convertView = LayoutInflater.from(cx).inflate(R.layout.view_device_manager_item, parent, false);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            IDevice dev = list.get(position);
            vh.showData(dev);

            return convertView;
        }

        class ViewHolder {
            @InjectView(R.id.edtName)
            TextView txtName;

            ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }

            void showData(IDevice dev) {
                LogUtils.i("20170327", "dev:" + dev.getID());
                String deviceName;
                if (dev.getCategoryName() != null) {
                    deviceName = dev.getCategoryName();
                    txtName.setText(deviceName);
                } else {
                    txtName.setText("");
                }
            }

        }
    }
}

