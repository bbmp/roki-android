package com.robam.roki.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.legent.ui.ext.HeadPage;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/14.
 */
abstract public class AbsModifyTextPage extends HeadPage {
    @InjectView(R.id.edtText)
    protected EditText editText;
    @InjectView(R.id.txtConfirm)
    protected TextView txtConfirm;

    abstract void initData(Bundle bd);

    abstract void onConfirm();

    @Override
    protected View onCreateContentView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.abs_pgae_modify_text, viewGroup, false);
        ButterKnife.inject(this, view);

        editText.requestFocus();
        initData(getArguments());

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.txtConfirm)
    public void onClick() {
        try {
            onConfirm();
        } catch (Exception e) {
            ToastUtils.showException(e);
        }
    }


}
