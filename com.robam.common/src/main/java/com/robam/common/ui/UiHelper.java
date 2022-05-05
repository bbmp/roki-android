package com.robam.common.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.DialogHelper;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.NetworkUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.R;
import com.robam.common.events.HomeRecipeViewEvent;
import com.robam.common.pojos.AbsRecipe;
import com.robam.common.pojos.Recipe;
import com.robam.common.services.CookbookManager;

public class UiHelper {
    private static final String TAG = "UiHelper";

    public static String second2String(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;

        String strTime = String.format("%s:%s", String.format("%02d", min),
                String.format("%02d", sec));

        return strTime;
    }

    public static boolean isNetworkConnected(Context cx) {
        return NetworkUtils.isConnect(cx);
    }

    public static boolean checkAuth(String loginPageKey) {
        boolean isAuth = Plat.accountService.isLogon();
        if (isAuth) {
            return true;
        } else {
            UIService.getInstance().postPage(loginPageKey);
            return false;
        }
    }

    public static boolean checkAuthWithDialog(Context cx, final String loginPageKey) {
        boolean isAuth = Plat.accountService.isLogon();
        if (isAuth) {
            return true;
        } else {
            DialogHelper.newDialog_OkCancel(cx, "您还未登录，快去登录吧~", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        UIService.getInstance().postPage(loginPageKey);
                    }
                }
            }).show();
            return false;
        }
    }

    // ----------------------------------------Recipe-------------------------------------------

    public static void onToday(final AbsRecipe book) {

        if (book == null) return;

        CookbookManager cm = CookbookManager.getInstance();

        if (book.allowDistribution) {
            if (book.isToday) {
                cm.deleteTodayCookbook(book.id, new VoidCallback() {

                    @Override
                    public void onSuccess() {
                        book.isToday = false;
                        ToastUtils.showShort("已经从购物车中移除!");
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showShort(t.getMessage());

                    }
                });
            } else {
                cm.addTodayCookbook(book.id, new VoidCallback() {

                    @Override
                    public void onSuccess() {
                        book.isToday = true;
                        ToastUtils.showShort("已经加入到购物车!");
                    }

                    @Override
                    public void onFailure(Throwable t) {

                        ToastUtils.showShort(t.getMessage());
                    }
                });

            }
        } else {
            ToastUtils.showShort("失传菜博大精深" + "\n" +
                    "暂不支持加入购物车");
        }
    }

    public static void onFavority(final Recipe book, final ImageView imageView, final TextView textView) {
        CookbookManager cm = CookbookManager.getInstance();
        if (book != null) {
            LogUtils.i("onFavority", " book collected:" + book.collected);

            if (book.collected) {
                cm.deleteFavorityCookbooks(book.id, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        if (imageView != null) {
                            imageView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                        }
                        if (textView != null) {
                            textView.setText("收藏 " + (book.collectCount - 1));
                        }
                        book.collected = false;
                        book.setTrue(false);
                        ToastUtils.showShort("已取消收藏");
                        EventUtils.postEvent(new HomeRecipeViewEvent(HomeRecipeViewEvent.RecipeFavoriteChange));
                        EventUtils.postEvent(new HomeRecipeViewEvent(HomeRecipeViewEvent.RecipeDetailPageBackToTheme));
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showShort(t.getMessage());
                        LogUtils.i(TAG, t.getMessage());
                    }
                });
            } else {
                cm.addFavorityCookbooks(book.id, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        if (imageView != null) {
                            imageView.setImageResource(R.drawable.ic_baseline_favorite_24);
                        }
                        if (textView != null) {
                            textView.setText("收藏 " + (book.collectCount));
                        }
                        book.collected = true;
                        book.setTrue(true);
                        ToastUtils.showShort("收藏成功");
                        EventUtils.postEvent(new HomeRecipeViewEvent(HomeRecipeViewEvent.RecipeFavoriteChange));
                        EventUtils.postEvent(new HomeRecipeViewEvent(HomeRecipeViewEvent.RecipeDetailPageBackToTheme));
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showShort(t.getMessage());
                        LogUtils.i(TAG, t.getMessage());
                    }
                });

            }
        }
    }


}
