package com.robam.roki.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2019/3/14.
 */

public class TipDialog extends Dialog{

    @InjectView(R.id.txt_desc)
    TextView txtDesc;
    @InjectView(R.id.txt_ok)
    TextView txtOk;

    String context = null;
    String ok = null;

    public TipDialog(Context context, String txtDesc, String txtok) {
        super(context, R.style.ios_dialog_Common);
        this.context = txtDesc;
        this.ok = txtok;
        initView();
    }



    protected void initView() {
        setContentView(R.layout.tip_common_dialog);
        ButterKnife.inject(this);
        init();
    }

    public interface SetOkOnClickLister{
        void confirm();
    }

    private SetOkOnClickLister setOkOnClickLister;

    public void setSetOkOnClickLister(SetOkOnClickLister setOkOnClickLister){
        this.setOkOnClickLister = setOkOnClickLister;
    }

    private void init() {
        txtDesc.setText(context);
        txtOk.setText(ok);
    }



    @OnClick(R.id.txt_ok)
    public void onViewClicked() {
        if (setOkOnClickLister!=null){
            setOkOnClickLister.confirm();
        }
    }
}
