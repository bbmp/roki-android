package com.robam.roki.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.legent.dao.DaoHelper;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ViewUtils;
import com.robam.common.events.StoveStatusChangedEvent;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.roki.R;
import com.robam.roki.service.MobileStoveCookTaskService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/17.
 */
public class ChooseStoveByWaitDialog extends AbsDialog {

    static public void show(Context cx, Recipe book) {
        ChooseStoveByWaitDialog dlg = new ChooseStoveByWaitDialog(cx, book);
        dlg.show();
    }

    static public void showDialog(Context cx, Recipe book,int index) {
        ChooseStoveByWaitDialog dlg = new ChooseStoveByWaitDialog(cx, book,index);
        dlg.show();
    }


    @InjectView(R.id.txtCookNoStove)
    TextView txtCookNoStove;

    Recipe recipe;
    int index;

    public ChooseStoveByWaitDialog(Context cx, Recipe book) {
        super(cx, R.style.Theme_Dialog_FullScreen);
        ViewUtils.setFullScreen(cx, this);
        this.recipe = book;
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                EventUtils.regist(ChooseStoveByWaitDialog.this);
            }
        });

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                EventUtils.unregist(ChooseStoveByWaitDialog.this);
            }
        });
    }

    public ChooseStoveByWaitDialog(Context cx, Recipe book,int index) {
        super(cx, R.style.Theme_Dialog_FullScreen);
        ViewUtils.setFullScreen(cx, this);
        this.recipe = book;
        this.index = index;
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                EventUtils.regist(ChooseStoveByWaitDialog.this);
            }
        });

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                EventUtils.unregist(ChooseStoveByWaitDialog.this);
            }
        });
    }


    @Subscribe
    public void onEvent(StoveStatusChangedEvent event) {

        Stove stove = event.pojo;
        LogUtils.i("20171225","event--------------------:"+stove.leftHead.status);
        LogUtils.i("20171225","event----:"+stove.rightHead.status);
        if (stove.leftHead != null && stove.leftHead.status == StoveStatus.StandyBy) {
            startCook(stove.leftHead);
        } else if (stove.rightHead != null && stove.rightHead.status == StoveStatus.StandyBy) {
            startCook(stove.rightHead);
        }
    }


    @OnClick(R.id.layout)
    public void onClick() {
        this.dismiss();
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_cook_choose_stove_by_wait;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
        txtCookNoStove.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        txtCookNoStove.getPaint().setAntiAlias(true);
    }

    //进去看看
    @OnClick(R.id.txtCookNoStove)
    public void onClickNoStove() {
//        startCook(null);
        MobileStoveCookTaskService.getInstance().start(null, recipe,"0");
        this.dismiss();
    }


    void startCook(Stove.StoveHead head) {
        Recipe newRecipe = null;
        try {
            newRecipe = DaoHelper.getDao(Recipe.class).queryForId(recipe.id);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        LogUtils.i("20171225","index::"+index);
        if (index!=0){
            MobileStoveCookTaskService.getInstance().start(head, newRecipe,String.valueOf(index));
        }else{
            //MobileStoveCookTaskService.getInstance().start(head, newRecipe);
        }
        this.dismiss();
    }
}
