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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
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
    RecyclerView listview;
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


    @Override
    public void onResume() {
        super.onResume();
        MobApp.getmFirebaseAnalytics().setCurrentScreen(getActivity(), "厨电管理页", null);
    }

    @Override
    protected void initData() {
        adapter = new Adapter();
        listview.setAdapter(adapter);
        boolean isEmpty = Plat.deviceService.isEmpty();
        switchView(isEmpty);
        if (isEmpty) return;
        List<IDevice> list = Plat.deviceService.queryAll();
        adapter.setList(list);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull View view, int i) {
                IDevice dev = adapter.getItem(i);
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, dev.getID());
                UIService.getInstance().postPage(PageKey.DeviceDetail, bd);
            }
        });
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
        listview.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        UIService.getInstance().popBack();
    }


    static class Adapter extends BaseQuickAdapter<IDevice, BaseViewHolder> {
        public Adapter() {
            super(R.layout.view_device_manager_item);
        }


        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, IDevice iDevice) {
            if (iDevice.getCategoryName() != null) {
                baseViewHolder.setText(R.id.edtName, iDevice.getCategoryName());
            } else {
                baseViewHolder.setText(R.id.edtName, "");
            }
        }

    }
}

