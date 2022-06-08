//package com.robam.roki.ui.page;
//
//import android.os.Bundle;
//import androidx.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.handmark.pulltorefresh.library.ILoadingLayout;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.legent.ui.ext.BasePage;
//import com.legent.utils.LogUtils;
//import com.robam.common.pojos.Recipe;
//import com.robam.common.pojos.Recipe3rd;
//import com.robam.common.pojos.Tag;
//import com.robam.common.pojos.device.fan.IFan;
//import com.robam.roki.R;
//import com.robam.roki.ui.view.EmojiEmptyView;
//import com.robam.roki.ui.view.PullToRefreshStaggeredGridView;
//import com.robam.roki.ui.view.RecipeGridView;
//
//import java.util.List;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//
//abstract public class AbsRecipeGridPage extends BasePage implements PullToRefreshBase.OnRefreshListener2<RecipeGridView> {
//   int start = 0;
//     int limit = 10;
//    @InjectView(R.id.emptyView)
//    EmojiEmptyView emptyView;
//
//    @InjectView(R.id.gridView)
//    public PullToRefreshStaggeredGridView gridView;
//    int res = R.layout.abs_page_recipe_grid;
//    @InjectView(R.id.home_recipe_live_imgv_return)
//    public ImageView mHomeRecipeLiveImgvReturn;
//    @InjectView(R.id.home_recipe_live_title)
//    public TextView mHomeRecipeLiveTitle;
//    @InjectView(R.id.listview_titlebar_ll_right)
//    public LinearLayout mListviewTitlebarLlRight;
//
//    public Tag tag;
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Bundle bd = getArguments();
//        tag = (Tag) (bd == null ? null : bd.getParcelable("Tag"));
//        View view = inflater.inflate(R.layout.abs_page_recipe_grid, container,
//                false);
//        if (!view.isInEditMode()) {
//            ButterKnife.inject(this, view);
//            if (emptyView != null){
//                emptyView.setText(getTextWhenEmpty());
//                initData();
//            }
//        }
//        ButterKnife.inject(this, view);
//        return view;
//    }
//
//
//    protected int getLayoutResource() {
//        return res;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.reset(this);
//    }
//
//    protected void initData() {
//        gridView.setMode(PullToRefreshBase.Mode.BOTH);
//        gridView.setOnRefreshListener(this);
//        // 下拉刷新时的提示文本设置
//        ILoadingLayout startLabels = gridView.getLoadingLayoutProxy(true, false);
//        startLabels.setPullLabel(r.getString(com.robam.common.R.string.pull_down_refresh));
//        startLabels.setRefreshingLabel(r.getString(com.robam.common.R.string.refreshing_down));
//        startLabels.setReleaseLabel(r.getString(com.robam.common.R.string.release_down_refresh));
//        // 上拉加载更多时的提示文本设置
//        ILoadingLayout endLabels = gridView.getLoadingLayoutProxy(false, true);
//        endLabels.setPullLabel(r.getString(com.robam.common.R.string.pull_up_refresh));
//        endLabels.setRefreshingLabel(r.getString(com.robam.common.R.string.refreshing_up));
//        endLabels.setReleaseLabel(r.getString(com.robam.common.R.string.release_up_refresh));
//    }
//
//    protected String getTextWhenEmpty() {
//        return null;
//    }
//
//    protected void loadBooks(int modelType, List<Recipe> books) {
//
//    }
//
//    protected void loadBooks(int modelType, List<Recipe> books, List<Recipe3rd> books3rd) {
//    }
//
//    public void switchView(boolean isEmpty) {
//        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
//        gridView.setVisibility(!isEmpty ? View.VISIBLE : View.GONE);
//    }
//
//    /**
//     * 下拉监听
//     */
//    @Override
//    public void onPullDownToRefresh(PullToRefreshBase<RecipeGridView> pullToRefreshBase) {
//        start = 0;
//    }
//
//    /**
//     * 上推监听
//     */
//    @Override
//    public void onPullUpToRefresh(PullToRefreshBase<RecipeGridView> pullToRefreshBase) {
//        start++;
//    }
//}
