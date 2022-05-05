

package com.robam.roki.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Strings;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.SoftInputUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.api.ViewUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.Group;
import com.robam.common.pojos.History;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.Tag;
import com.robam.common.services.CookbookManager;
import com.robam.common.util.RecipeRequestIdentification;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.page.RecipeDetailPage;
import com.robam.roki.ui.view.RecipeSearchTitleView;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.MotherListUtils;
import com.robam.roki.utils.ToolUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author wwq
 * des ：搜索dialog
 */
public class RecipeSearchDialog extends AbsDialog {
    private static final String TAG = "RecipeSearchDialog";

    private List<History> mHistoryList;

    public interface OnSearchCallback {
        void onSearchWord(String word);
    }

    public static RecipeSearchDialog dlg;

    static public RecipeSearchDialog show(Context cx, OnSearchCallback callback) {
        dlg = new RecipeSearchDialog(cx, callback);
        dlg.show();
        return dlg;
    }

    @InjectView(R.id.title_sear)
    RecipeSearchTitleView title;

    @InjectView(R.id.id_mom_beby)
    TagFlowLayout id_mom_beby;

    @InjectView(R.id.id_recommend)
    TagFlowLayout id_recommend;

    @InjectView(R.id.id_cooked)
    TagFlowLayout id_cooked;

    @InjectView(R.id.ll_search_history)
    LinearLayout llSearchHistory;

    @InjectView(R.id.iv_delete_search_history)
    ImageView ivDeleteSearchHistory;

    OnSearchCallback onSearchCallback;
    ArrayList<Recipe> mRecipeNames = new ArrayList<>();

    //Context cx;
    private RecipeSearchDialog(Context cx, OnSearchCallback callback) {
        super(cx, R.style.Theme_Dialog_FullScreen);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ViewUtils.setFullScreen(cx, this);
        this.onSearchCallback = callback;
        this.cx = cx;
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_search_recipe;
    }

    @Override
    protected void onStart() {
        super.onStart();
        SoftInputUtils.showSoftInput(cx, title.getEdtSearch());
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
        RecipeSearchTitleView.OnCancelSearchCallback titleCallback = new RecipeSearchTitleView.OnCancelSearchCallback() {

            @Override
            public void onCancel() {
                RecipeSearchDialog.this.dismiss();
            }

            @Override
            public void onWordChanged(String word) {
                onSearchWord(word);
            }

            @Override
            public void onSearchReturn() {
                onReturnPop();
            }
        };
        title.setOnCancelSearchCallback(titleCallback);
        SoftInputUtils.show(cx, view);
        id_cooked.setVisibility(View.VISIBLE);
        initData();
    }

    void initData() {
        long userId = Plat.accountService.getCurrentUserId();
        CookbookManager.getInstance().getCookbookSearchHistory(userId,
                new Callback<Reponses.HistoryResponse>() {
                    @Override
                    public void onSuccess(Reponses.HistoryResponse historyResponse) {
                        if (historyResponse == null) {
                            return;
                        }
                        initHistoryData(historyResponse.historyList);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.i("20180418", " t:" + t);
                    }
                });

        RokiRestHelper.getStoreCategory(new Callback<List<Group>>() {
            @Override
            public void onSuccess(List<Group> result) {
                if (result == null || result.size() == 0) {
                    return;
                }
                for (int i = 0; i < result.size(); i++) {
                    if (cx.getString(R.string.maternal_and_child_series).equals
                            (result.get(i).getName())) {
                        List<Tag> tags = result.get(i).getTags();
                        int pageNo=0;
                        int pageSize =20;
                        for (Tag tag : tags) {
                            RokiRestHelper.getCookbooksByTag(tag.getID(),pageNo,pageSize, new Callback<Reponses.CookbooksResponse>() {

                                @Override
                                public void onSuccess(Reponses.CookbooksResponse cookbooksResponse) {
                                    List<Recipe> cookbooks = cookbooksResponse.cookbooks;
                                    if (cookbooks != null && cookbooks.size() > 0) {
                                        for (int i = 0; i < cookbooks.size(); i++) {
                                            mRecipeNames.add(cookbooks.get(i));
                                        }
                                        addData(mRecipeNames);
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {

                                    ToastUtils.showThrowable(t);
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });

        CookbookManager.getInstance().getHotKeysForCookbook(
                new Callback<List<String>>() {

                    @Override
                    public void onSuccess(final List<String> result) {
                        if (result == null || result.size() == 0) {
                            return;
                        }
                        id_recommend.setAdapter(new TagAdapter<String>(result) {
                            @Override
                            public View getView(FlowLayout parent, int position, String s) {
                                TextView tv = (TextView) LayoutInflater.from(cx).inflate(R.layout.search_show,
                                        id_recommend, false);
                                tv.setText(s);
                                return tv;
                            }
                        });
                        id_recommend.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                            @Override
                            public boolean onTagClick(View view, int position, FlowLayout parent) {
                                Bundle bundle = new Bundle();
                                bundle.putString(PageArgumentKey.text, result.get(position));
                                UIService.getInstance().postPage(PageKey.RecipeSearch, bundle);
                                CookbookManager.getInstance().saveHistoryKeysForCookbook(result.get(position));
                                dismiss();
                                return true;
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
    }

    //历史搜索数据
    private void initHistoryData(final List<History> historyList) {
        if (!Plat.accountService.isLogon()) {
            final List<String> words = CookbookManager.getInstance().getHistoryKeysForCookbook();
            if (null == words || words.size() == 0){
                llSearchHistory.setVisibility(View.GONE);
                id_cooked.setVisibility(View.GONE);
                return;
            }
            llSearchHistory.setVisibility(View.VISIBLE);
            id_cooked.setVisibility(View.VISIBLE);
            id_cooked.setAdapter(new TagAdapter<String>(words) {

                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(cx).inflate(R.layout.search_show,
                            id_cooked, false);
                    tv.setText(s);
                    return tv;
                }
            });
            id_cooked.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {

                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    if (words != null || words.size() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putString(PageArgumentKey.text, words.get(position));
                        UIService.getInstance().postPage(PageKey.RecipeSearch, bundle);
                        dismiss();
                    }

                    return true;
                }
            });
        } else {

            if (null == historyList || historyList.size() == 0){
                llSearchHistory.setVisibility(View.GONE);
                id_cooked.setVisibility(View.GONE);
            } else {
                llSearchHistory.setVisibility(View.VISIBLE);
                id_cooked.setVisibility(View.VISIBLE);
                List<History> histories = null;
                if (historyList.size() > 5) {
                    histories = historyList.subList(0, 5);
                }else {
                    histories = historyList;
                }
                id_cooked.setAdapter(new TagAdapter<History>(histories) {
                    @Override
                    public View getView(FlowLayout parent, int position, History history) {
                        TextView tv = (TextView) LayoutInflater.from(cx).inflate(R.layout.search_show,
                                id_cooked, false);
                        tv.setText(history.value);
                        return tv;
                    }
                });

                id_cooked.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {

                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        if (historyList != null && historyList.size() > 0) {
                            Bundle bundle = new Bundle();
                            bundle.putString(PageArgumentKey.text, historyList.get(position).value);
                            UIService.getInstance().postPage(PageKey.RecipeSearch, bundle);
                            dismiss();
                        }

                        return true;
                    }
                });

            }
        }
    }

    private void addData(final ArrayList<Recipe> recipeNames) {

        final List<Recipe> recipes = MotherListUtils.randomElement(recipeNames, 5);
        id_mom_beby.setAdapter(new TagAdapter<Recipe>(recipes) {

            @Override
            public View getView(FlowLayout parent, int position, Recipe recipe) {
                TextView tv = (TextView) LayoutInflater.from(cx).inflate(R.layout.search_show,
                        id_recommend, false);
                tv.setText(recipe.name);
                return tv;
            }
        });
        id_mom_beby.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {

            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (recipes != null && recipes.size() > 0) {
                    RecipeDetailPage.show(recipeNames.get(position), recipeNames.get(position).id, RecipeDetailPage.unKnown,
                            RecipeRequestIdentification.RECIPE_INDIVIDUATION_RECOMMEND);
                    dismiss();
                }

                return true;
            }
        });
    }

    @OnClick(R.id.iv_delete_search_history)
    public void onDeleteSearchHistory() {
        ToolUtils.hideSoftInput((Activity) cx);
        final IRokiDialog dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_26);
        dialog.setTitleText("确定要删除搜索记录吗？");
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
                if (!Plat.accountService.isLogon()) {
                    CookbookManager.getInstance().clearHistoryKeyForCookbook();
                    llSearchHistory.setVisibility(View.GONE);
                    id_cooked.setVisibility(View.GONE);
                    return;
                }
                long userId = Plat.accountService.getCurrentUserId();
                String userName = Plat.accountService.getCurrentUser().getName();
                CookbookManager.getInstance().deleteCookbookSearchHistory(userName, userId, new Callback<Reponses.DeleteHistoryResponse>() {
                    @Override
                    public void onSuccess(Reponses.DeleteHistoryResponse deleteHistoryResponse) {
                        LogUtils.i(TAG, " onSuccess rc:" + deleteHistoryResponse.rc + " msg:" + deleteHistoryResponse.msg);
                        llSearchHistory.setVisibility(View.GONE);
                        id_cooked.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.show("删除历史记录失败", Toast.LENGTH_SHORT);
                        LogUtils.i(TAG, "onFailure:+msg:删除历史记录失败");
                    }
                });
            }
        });


    }

    void onSearchWord(String word) {
        if (Strings.isNullOrEmpty(word)) {
            return;
        }
        RecipeSearchDialog.this.dismiss();
        Bundle bundle = new Bundle();
        bundle.putString(PageArgumentKey.text, word);
        if (onSearchCallback != null) {
            CookbookManager.getInstance().saveHistoryKeysForCookbook(word);
            onSearchCallback.onSearchWord(word);
        }
        UIService.getInstance().postPage(PageKey.RecipeSearch, bundle);
    }

    void onReturnPop() {
        if (dlg != null && dlg.isShowing()) {
            dlg.dismiss();
        }
    }

}
