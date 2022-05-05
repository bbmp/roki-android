package com.robam.roki.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageKey;
import com.robam.roki.utils.DialogUtil;


public class UserCancelAccountPage extends BasePage implements View.OnClickListener {


    private ImageView mIvCancelSetttingBack;
    private TextView mTvAccoutLogOff;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cancel_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mIvCancelSetttingBack.setOnClickListener(this);
        mTvAccoutLogOff.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_cancel_settting_back:
                UIService.getInstance().popBack();
                break;
            case R.id.tv_accout_log_off:
                accout_logout_setting();
                break;
        }
    }

    private void accout_logout_setting() {
        final IRokiDialog dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_25);
        dialog.setTitleText(R.string.user_cancel_account_dialog);
        dialog.show();
        dialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                UIService.getInstance().postPage(PageKey.VerifyMobilePhone);
            }
        });
    }

    private void initView(View view) {
        mIvCancelSetttingBack = (ImageView) view.findViewById(R.id.iv_cancel_settting_back);
        mTvAccoutLogOff = (TextView) view.findViewById(R.id.tv_accout_log_off);
    }

}