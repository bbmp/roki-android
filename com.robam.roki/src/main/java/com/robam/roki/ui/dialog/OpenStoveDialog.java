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
 * Created by yinwei on 2017/9/22.
 */

public class OpenStoveDialog extends AbsDialog {
   /* static public void show(Context cx, Recipe book) {
        dlg = new OpenStoveDialog(cx, book);
        dlg.show();
    }*/
     static OpenStoveDialog dlg;
    static public OpenStoveDialog showDialog(Context cx, Recipe book,int index,Stove.StoveHead stoveHead) {
        dlg = new  OpenStoveDialog(cx, book,index,stoveHead);
        dlg.show();
        return dlg;
    }


    @InjectView(R.id.txtCookNoStove)
    TextView txtCookNoStove;
    @InjectView(R.id.open_txt)
            TextView open_txt;

    Recipe recipe;
    int index;
    private Stove.StoveHead stoveHead;

    /*public OpenStoveDialog(Context cx, Recipe book) {
        super(cx, R.style.Theme_Dialog_FullScreen);
        ViewUtils.setFullScreen(cx, this);
        this.recipe = book;
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                EventUtils.regist(OpenStoveDialog.this);
            }
        });

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                EventUtils.unregist(OpenStoveDialog.this);
            }
        });
    }*/

    public OpenStoveDialog(Context cx, Recipe book, int index, Stove.StoveHead stoveHead) {
        super(cx, R.style.Theme_Dialog_FullScreen);
        ViewUtils.setFullScreen(cx, this);
        this.recipe = book;
        this.index = index;
        this.stoveHead = stoveHead;
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                EventUtils.regist(OpenStoveDialog.this);
            }
        });

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                EventUtils.unregist(OpenStoveDialog.this);
            }
        });
        if (stoveHead!=null){
            if (stoveHead.isLeft()){
                open_txt.setText("等待左炉头开火");
            }else {
                open_txt.setText("等待右炉头开火");
            }
        }
    }


    @Subscribe
    public void onEvent(StoveStatusChangedEvent event) {
      //  LogUtils.i("20171025","evetn:"+event.pojo.leftHead.status+"  "+event.pojo.rightHead.status);
        Stove stove = event.pojo;
        /*if (stove.leftHead != null && stove.leftHead.status == StoveStatus.StandyBy) {
            startCook(stove.leftHead);
        } else if (stove.rightHead != null && stove.rightHead.status == StoveStatus.StandyBy) {
            startCook(stove.rightHead);
        }*/

        if (stove.leftHead.ihId==stoveHead.ihId){
            if (stove.leftHead.status == StoveStatus.StandyBy)
              startCook(stove.leftHead);
        }else{
            if (stove.rightHead.status == StoveStatus.StandyBy)
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
      //  LogUtils.i("20171024","head:"+head.ihId+"stoveHead:"+stoveHead.ihId);
        if (stoveHead.ihId!=head.ihId){
           // ToastUtils.show("您之前选择的不是这个炉头，请开启另一个", Toast.LENGTH_SHORT);
            return;
        }else{
            Recipe newRecipe = null;
            try {
                newRecipe = DaoHelper.getDao(Recipe.class).queryForId(recipe.id);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
           // LogUtils.i("20170911","index::"+index);
            if (index!=0){
                MobileStoveCookTaskService.getInstance().start(head, newRecipe,String.valueOf(index));
            }else{
               // MobileStoveCookTaskService.getInstance().start(head, newRecipe);
            }
            this.dismiss();
        }
    }
}
