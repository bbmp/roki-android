package com.robam.roki.ui.dialog;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.api.ViewUtils;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.roki.R;
import com.robam.roki.service.MobileStoveCookTaskService;
import com.robam.roki.ui.Helper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sylar on 15/7/10.
 */
public class NoStoveDialog extends AbsDialog {

    @InjectView(R.id.tv_cooking)
    TextView tvCooking;
    @InjectView(R.id.tv_can_go)
    TextView tvCanGo;
    @InjectView(R.id.layout)
    LinearLayout layout;

    public static boolean isStoveNull = false;
    int index;
    static public void show(Context cx, Recipe book) {
        NoStoveDialog dlg = new NoStoveDialog(cx, book);
        dlg.show();
    }

    static public void showNotic(Context cx, Recipe book,int index) {
        NoStoveDialog dlg = new NoStoveDialog(cx, book);
        dlg.show();
    }

    Recipe book;

    public NoStoveDialog(Context cx, Recipe book) {
        super(cx, R.style.Theme_Dialog_FullScreen);
        ViewUtils.setFullScreen(cx, this);
        this.book = book;
    }
   /* public NoStoveDialog(Context cx, Recipe book,int index) {
        super(cx, R.style.Theme_Dialog_FullScreen);
        ViewUtils.setFullScreen(cx, this);
        this.book = book;
        this.index = index;
    }*/


    @Override
    protected int getViewResId() {
        return R.layout.dialog_no_stove;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
        tvCanGo.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvCanGo.getPaint().setAntiAlias(true);
    }


    @OnClick(R.id.layout)
    public void onClick() {
        this.dismiss();
    }



    void startCook(Stove.StoveHead head) {
      //  if (index==0){
            Helper.startCook(head, book);
      /*  }else{
            MobileStoveCookTaskService.getInstance().start(null, book,String.valueOf(index));
        }*/
        this.dismiss();
    }


    @OnClick({R.id.tv_cooking, R.id.tv_can_go})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cooking://继续烹饪
                startCook(null);
                break;
            case R.id.tv_can_go://进去看看
                isStoveNull = true;
                MobileStoveCookTaskService.getInstance().start(null, book,"0");
                this.dismiss();
                break;
        }
    }
}
