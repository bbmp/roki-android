package com.robam.roki.ui.dialog;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.api.DisplayUtils;
import com.legent.utils.api.ViewUtils;
import com.robam.common.Utils;
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
public class ChooseStoveByManualDialog extends AbsDialog {

    static public void show(Context cx, Recipe book) {
        ChooseStoveByManualDialog dlg = new ChooseStoveByManualDialog(cx, book);
        dlg.show();
    }

    static public void showSelectStove(Context cx, Recipe book,int index) {
        ChooseStoveByManualDialog dlg = new ChooseStoveByManualDialog(cx, book,index);
        dlg.show();
    }


    Recipe book;
    int diameter;

    @InjectView(R.id.txtCookNoStove)
    TextView txtCookNoStove;
    @InjectView(R.id.txtLeft)
    TextView txtLeft;
    @InjectView(R.id.txtRight)
    TextView txtRight;
    @InjectView(R.id.layout)
    LinearLayout layout;

    int index;

    public ChooseStoveByManualDialog(Context cx, Recipe book) {
        super(cx, R.style.Theme_Dialog_FullScreen);
        ViewUtils.setFullScreen(cx, this);
        this.book = book;
    }

    public ChooseStoveByManualDialog(Context cx, Recipe book,int index) {
        super(cx, R.style.Theme_Dialog_FullScreen);
        ViewUtils.setFullScreen(cx, this);
        this.book = book;
        this.index = index;
    }


    @Override
    protected int getViewResId() {
        return R.layout.dialog_cook_choose_stove_by_manual;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
        txtCookNoStove.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        txtCookNoStove.getPaint().setAntiAlias(true);

        computeSize();
        txtLeft.getLayoutParams().width = diameter;
        txtLeft.getLayoutParams().height = diameter;
        txtRight.getLayoutParams().width = diameter;
        txtRight.getLayoutParams().height = diameter;
    }


    @OnClick(R.id.layout)
    public void onClick() {
        this.dismiss();
    }

    //進去看看
    @OnClick(R.id.txtCookNoStove)
    public void onClickNoStove() {
//        startCook(null);
        MobileStoveCookTaskService.getInstance().start(null, book,"0");
        this.dismiss();
    }

    @OnClick(R.id.txtLeft)
    public void onClickLeft() {
        Stove stove = Utils.getDefaultStove();
        startCook(stove == null ? null : stove.leftHead);
    }

    @OnClick(R.id.txtRight)
    public void onClickRight() {
        Stove stove = Utils.getDefaultStove();
        startCook(stove == null ? null : stove.rightHead);
    }

    void startCook(Stove.StoveHead head) {
        if (index!=0){
            MobileStoveCookTaskService.getInstance().start(head,book,String.valueOf(index));
        }else{
            Helper.startCook(head, book);
        }
        this.dismiss();
    }

    void computeSize() {
        int width = DisplayUtils.getScreenWidthPixels(getContext());
        diameter = (width - DisplayUtils.dip2px(getContext(), 24 * 3)) / 2;
    }

}
