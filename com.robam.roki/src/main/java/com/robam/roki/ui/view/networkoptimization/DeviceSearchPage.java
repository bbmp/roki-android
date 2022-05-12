package com.robam.roki.ui.view.networkoptimization;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legent.Callback;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.DeviceGroupList;
import com.robam.common.pojos.DeviceItemList;
import com.robam.common.pojos.DeviceType;
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DeviceSearchPage extends HeadPage {
    @InjectView(R.id.edtSearch)
    EditText edtSearch;
    @InjectView(R.id.recipeView)
    DeviceGridView deviceGridView;

    @InjectView(R.id.txt_cantfinddevice1)
    TextView txt_cantfinddevice1;

    @InjectView(R.id.lin_cantfinddevice)
    LinearLayout lin_cantfinddevice;

    List<List<DeviceItemList>> deviceList = new ArrayList<List<DeviceItemList>>();
    List<List<DeviceItemList>> sumDeviceList = new ArrayList<List<DeviceItemList>>();
    List<DeviceGroupList> groupList = new ArrayList<DeviceGroupList>();
    List<DeviceGroupList> sumGroupList = new ArrayList<DeviceGroupList>();

    @Override
    protected View onCreateContentView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.frame_device_search, viewGroup, false);
        ButterKnife.inject(this, view);
        RokiRestHelper.getNetworkDeviceInfoRequest("roki", null, null, Reponses.NetworkDeviceInfoResponse.class, new RetrofitCallback<Reponses.NetworkDeviceInfoResponse>() {
            @Override
            public void onSuccess(Reponses.NetworkDeviceInfoResponse networkDeviceInfoResponse) {
                if (null != networkDeviceInfoResponse && null != networkDeviceInfoResponse.deviceGroupList) {
                    sumGroupList = networkDeviceInfoResponse.deviceGroupList;
                    for (int i = 0; i < sumGroupList.size(); i++) {
                        sumDeviceList.add(sumGroupList.get(i).getDeviceItemLists());
                    }
                }
            }

            @Override
            public void onFaild(String err) {

            }
        });
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                @Override
                                                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                                                        if (v.getText() != null) {
                                                            onSearch(v.getText().toString());
                                                        }
                                                        return true;
                                                    }
                                                    return false;
                                                }
                                            }

        );
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.tvCancel)
    public void onClickCancel() {
        UIService.getInstance().popBack();
    }

    void onSearch(String searchWorld) {
//        ProgressDialogHelper.setRunning(cx, true);
//        RokiRestHelper.getNetworkDeviceInfoRequest("roki", null, searchWorld, new Callback<List<DeviceGroupList>>() {
//            @Override
//            public void onSuccess(List<DeviceGroupList> deviceGroupLists) {
//                ProgressDialogHelper.setRunning(cx, false);
//                deviceList.clear();
//                groupList.clear();
//                for (int i = 0; i < deviceGroupLists.size(); i++) {
//                    deviceGroupLists.get(i).save2db();
//                }
//                groupList.addAll(deviceGroupLists);
//                for (int i = 0; i < deviceGroupLists.size(); i++) {
//                    List<DeviceItemList> deviceItemList = deviceGroupLists.get(i).getDeviceItemLists();
//                    for (int j = 0;j < deviceItemList.size(); j++) {
//                        deviceItemList.get(j).tag = deviceGroupLists.get(i).name;
//                    }
//                    deviceList.add(deviceItemList);
//                }
//                loadBooks(deviceList);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                ProgressDialogHelper.setRunning(cx, false);
//                ToastUtils.showThrowable(t);
//            }
//        });
        if (StringUtils.containsChinese(searchWorld)) {
            searchType(searchWorld);
        } else {
            searchDetail(searchWorld);
        }
    }

    private void searchDetail(String searchWorld) {
        for (int i = 0; i < sumDeviceList.size(); i++) {
            List<DeviceItemList> detailList = sumDeviceList.get(i);
            List<DeviceItemList> realList = new ArrayList<DeviceItemList>();
            for (int j = 0; j < detailList.size(); j++) {
                if (detailList.get(j).getName().contains(searchWorld)) {
                    detailList.get(j).tag = sumGroupList.get(i).name;
                    realList.add(detailList.get(j));
                }
            }
            deviceList.add(realList);
            loadBooks(deviceList);
        }
    }

    private void searchType(String searchWorld) {
        List<DeviceItemList> realList = new ArrayList<DeviceItemList>();
        for (int i = 0; i < sumGroupList.size(); i++) {
            if (sumGroupList.get(i).name.contains(searchWorld)) {
                realList.addAll(sumGroupList.get(i).getDeviceItemLists());
            }
        }
        deviceList.add(realList);
        loadBooks(deviceList);
    }

    protected void loadBooks(List<List<DeviceItemList>> deviceList) {
        if (deviceList.size() == 0) {
            deviceGridView.loadData(deviceList);
            txt_cantfinddevice1.setVisibility(View.VISIBLE);
            lin_cantfinddevice.setVisibility(View.VISIBLE);
            return;
        }
        txt_cantfinddevice1.setVisibility(View.GONE);
        lin_cantfinddevice.setVisibility(View.GONE);
        deviceGridView.loadData(deviceList);
    }

    @OnClick(R.id.lin_cantfinddevice)
    public void onClickCantFindDevice() {
        UIService.getInstance().postPage(PageKey.CantfindDevice);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


}
