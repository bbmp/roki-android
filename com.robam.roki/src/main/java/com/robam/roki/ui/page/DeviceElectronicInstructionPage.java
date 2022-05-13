//package com.robam.roki.ui.page;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.legent.ui.UIService;
//import com.legent.ui.ext.BasePage;
//import com.legent.utils.api.ToastUtils;
//import com.robam.roki.R;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//
///**
// * Created by 14807 on 2018/2/26.
// */
//
//public class DeviceElectronicInstructionPage extends BasePage {
//
//    @InjectView(R.id.iv_back)
//    ImageView mIvBack;
//    @InjectView(R.id.tv_click)
//    TextView mBtnClick;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.page_electronic_instruction, container, false);
//
//        ButterKnife.inject(this, view);
//        return view;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.reset(this);
//    }
//
//    @OnClick(R.id.iv_back)
//    public void onMIvBackClicked() {
//        UIService.getInstance().popBack();
//    }
//
//    @OnClick(R.id.tv_click)
//    public void onMTvClickClicked() {
//        ToastUtils.show(R.string.please_look_forward_opening, Toast.LENGTH_LONG);
//    }
//}
