package com.robam.roki.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.legent.Callback;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.api.ViewUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.common.pojos.Advert;
import com.robam.common.services.StoreService;
import com.robam.roki.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by zhaiyuanyi on 15/11/12.
 */
public class LostRecipeDialog extends AbsDialog {
    @InjectView(R.id.layout_lost_recipe)
    ImageView recipe;
    @InjectView(R.id.btn_cancel)
    ImageView mImgView_Cancel;
    private static LostRecipeDialog dlg = null;

    static public void newInstance(Context cx) {
        if (dlg == null) {
            dlg = new LostRecipeDialog(cx);
        }
    }

    static public Dialog getInstance() {
        if (dlg != null)
            return dlg;
        else
            return null;
    }

    static public void show(Context cx) {
        if (dlg == null) {
            dlg = new LostRecipeDialog(cx);
        }
        dlg.show();
    }

    public LostRecipeDialog(Context context) {
        super(context, R.style.maintain_home_dialog_style);
        ViewUtils.setFullScreen(context, this);
    }

    @OnClick(R.id.layout_lost_recipe)
    public void onClick() {
        jmp2WechatSubscription(getContext());
        //dialog_dismiss();
    }

    @OnClick(R.id.btn_cancel)
    public void onClickbtn() {
        // 点击，跳转到公众号，再修改；
        //jmp2WechatSubscription(getContext());
        dialog_dismiss();
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_lost_recipe;
    }

    @Override
    protected void initView(View view) {
        initAdvertData();
        ButterKnife.inject(this, view);
        final GestureDetector gd = new GestureDetector(getContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    private static final int FLING_MIN_DISTANCE = 200;
                    private static final int FLING_MIN_VELOCITY = 200;

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        if (Math.abs(e2.getY() - e1.getY()) > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                            if (velocityY < 0) {
                                if (LostRecipeDialog.getInstance() != null)
                                    LostRecipeDialog.getInstance().dismiss();return true;
                            }else{
                                return true;
                            }
                        }else{
                            return false;
                        }
                    }
                });

        recipe.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gd.onTouchEvent(event);
            }
        });

    }

    private void initAdvertData() {
        StoreService.getInstance().getHomeAdvertsForMob(new Callback<List<Advert.MobAdvert>>() {
            @Override
            public void onSuccess(List<Advert.MobAdvert> mobAdverts) {
                recipe.setTag(mobAdverts.get(0).content);
                ImageUtils.displayImage(cx, mobAdverts.get(0).getImgUrl(), recipe);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void dialog_dismiss() {
        dismiss();
    }

    public void jmp2WechatSubscription(Context cx) {
        if(recipe.getTag()==null)
            return;
        Uri uri = Uri.parse((String) recipe.getTag());
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        cx.startActivity(it);
    }
}
