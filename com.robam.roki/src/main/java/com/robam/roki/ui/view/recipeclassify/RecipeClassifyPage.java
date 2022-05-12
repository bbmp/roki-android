package com.robam.roki.ui.view.recipeclassify;

import android.content.Context;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.legent.plat.io.cloud.RetrofitCallback;
import com.robam.common.io.cloud.Reponses;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.ClassifyTagRecipePage;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.view.linkrecipetag.LinkageRecyclerView;
import com.robam.roki.ui.view.linkrecipetag.adapter.viewholder.LinkagePrimaryViewHolder;
import com.robam.roki.ui.view.linkrecipetag.adapter.viewholder.LinkageSecondaryFooterViewHolder;
import com.robam.roki.ui.view.linkrecipetag.adapter.viewholder.LinkageSecondaryHeaderViewHolder;
import com.robam.roki.ui.view.linkrecipetag.adapter.viewholder.LinkageSecondaryViewHolder;
import com.robam.roki.ui.view.linkrecipetag.bean.BaseGroupedItem;
import com.robam.roki.ui.view.linkrecipetag.contract.ILinkagePrimaryAdapterConfig;
import com.robam.roki.ui.view.linkrecipetag.contract.ILinkageSecondaryAdapterConfig;
import com.legent.Callback;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.Group;
import com.robam.common.pojos.Tag;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.model.bean.RecipeTagGroupItem;
import com.robam.roki.ui.page.AbsPage;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 全部智能菜谱
 * Created by sylar on 15/6/14.
 */
public class RecipeClassifyPage extends MyBasePage<MainActivity> {
    private static final String TAG = "RecipeClassifyPage";

    @InjectView(R.id.home_recipe_live_title)//标题 部分
            TextView home_recipe_live_title;

    @InjectView(R.id.home_recipe_live_imgv_return)//导航栏返回
            ImageView home_recipe_live_imgv_return;

    @InjectView(R.id.linkage_rv)
    LinkageRecyclerView linkageRecyclerView;

    protected List<Group> groups = new ArrayList<Group>();
    private List<RecipeTagGroupItem> recipeTagGroupItemList;

    private static final int SPAN_COUNT_FOR_GRID_MODE = 2;
    private static final int MARQUEE_REPEAT_LOOP_MODE = -1;
    private static final int MARQUEE_REPEAT_NONE_MODE = 0;

//
//    @Override
//    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
//        View view = layoutInflater.inflate(R.layout.classify_main_page, viewGroup, false);
//        ButterKnife.inject(this, view);
//        getRecipeClassifyData();
//        return view;
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.classify_main_page;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        getRecipeClassifyData();
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        MobApp.getmFirebaseAnalytics().setCurrentScreen(getActivity(), "菜谱分+类页", null);
//    }

    /**
     * 导航栏左侧点击
     */
    @OnClick(R.id.home_recipe_live_imgv_return)
    void OnClickLeft() {
        UIService.getInstance().popBack();
    }

    //获取菜谱分类数据
    private void getRecipeClassifyData() {
        RokiRestHelper.getStoreCategory(Reponses.StoreCategoryResponse.class, new RetrofitCallback<Reponses.StoreCategoryResponse>() {
            @Override
            public void onSuccess(Reponses.StoreCategoryResponse storeCategoryResponse) {
                if (null != storeCategoryResponse) {
                    List<Group> result = storeCategoryResponse.cookbookTagGroups;
                    LogUtils.i(TAG, "result:" + result.toString());
                    groups.clear();
                    groups = result;
                    recipeTagGroupItemList = new ArrayList<>();
                    if (result != null) {
                        for (Group group : result) {
                            group.save2db();
                            RecipeTagGroupItem recipeTagGroupItem = new RecipeTagGroupItem(true, group.name.substring(0, 2));
                            recipeTagGroupItem.isHeader = true;
                            recipeTagGroupItem.header = group.name.substring(0, 2);
                            recipeTagGroupItemList.add(recipeTagGroupItem);
                            LogUtils.i(TAG, "group name:" + group.name + " type:" + group.type + " group toString :" + group.toString());
                            for (Tag recipeTag : group.getTags()) {
                                RecipeTagGroupItem.ItemInfo itemInfo = new RecipeTagGroupItem.ItemInfo(group.name.substring(0, 2), group.name.substring(0, 2), recipeTag.id, recipeTag.imageUrl, recipeTag.name);
                                itemInfo.setTitle(group.name.substring(0, 2));
                                itemInfo.setGroup(group.name.substring(0, 2));
                                itemInfo.setName(recipeTag.name);
                                itemInfo.setId(recipeTag.id);
                                itemInfo.setType(group.type);
                                RecipeTagGroupItem recipeTagGroupItem1 = new RecipeTagGroupItem(itemInfo);
                                recipeTagGroupItem1.isHeader = false;
                                recipeTagGroupItemList.add(recipeTagGroupItem1);
                            }
                        }
                    }
                    linkageRecyclerView.init(recipeTagGroupItemList, new ElemeLinkagePrimaryAdapterConfig(), new ElemeLinkageSecondaryAdapterConfig());
                    linkageRecyclerView.setGridMode(true);
                }
            }

            @Override
            public void onFaild(String err) {

            }
        });
    }

    private class ElemeLinkagePrimaryAdapterConfig implements ILinkagePrimaryAdapterConfig {

        private Context mContext;

        @Override
        public void setContext(Context context) {
            mContext = context;
        }

        @Override
        public int getLayoutId() {
            return R.layout.default_adapter_linkage_primary;
        }

        @Override
        public int getGroupTitleViewId() {
            return R.id.tv_group;
        }

        @Override
        public int getRootViewId() {
            return R.id.layout_group;
        }

        @Override
        public void onBindViewHolder(LinkagePrimaryViewHolder holder, boolean selected, String title) {
            LogUtils.i(TAG, "onBindViewHolde title:" + title);
            TextView tvTitle = ((TextView) holder.mGroupTitle);
//            tvTitle.setTextSize(16);
            tvTitle.setText(title.substring(0, 2));
//            tvTitle.setBackgroundColor(mContext.getResources().getColor(
//                    selected ? com.robam.roki.ui.view.linkrecipetag.R.color.colorPurple : com.robam.roki.ui.view.linkrecipetag.R.color.colorWhite));
            tvTitle.setTextColor(ContextCompat.getColor(mContext,
                    selected ? R.color.roki_main_color : R.color.roki_general_text_color));
            tvTitle.setEllipsize(selected ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.END);
            tvTitle.setFocusable(selected);
            tvTitle.setFocusableInTouchMode(selected);
            tvTitle.setMarqueeRepeatLimit(selected ? MARQUEE_REPEAT_LOOP_MODE : MARQUEE_REPEAT_NONE_MODE);
        }

        @Override
        public void onItemClick(LinkagePrimaryViewHolder holder, View view, String title) {
            //TODO
        }

    }

    private class ElemeLinkageSecondaryAdapterConfig implements
            ILinkageSecondaryAdapterConfig<RecipeTagGroupItem.ItemInfo> {

        private Context mContext;

        @Override
        public void setContext(Context context) {
            mContext = context;
        }

        @Override
        public int getGridLayoutId() {
            return R.layout.item_recipe_tag_adapter_secondary_grid;
        }

        @Override
        public int getLinearLayoutId() {
            return R.layout.item_recipe_tag_adapter_secondary_linear;
        }

        @Override
        public int getHeaderLayoutId() {
            return R.layout.default_adapter_linkage_secondary_header;
        }

        @Override
        public int getFooterLayoutId() {
            return 0;
        }

        @Override
        public int getHeaderTextViewId() {
            return R.id.secondary_header;
        }

        @Override
        public int getSpanCountOfGridMode() {
            return SPAN_COUNT_FOR_GRID_MODE;
        }

        @Override
        public void onBindViewHolder(final LinkageSecondaryViewHolder holder,
                                     final BaseGroupedItem<RecipeTagGroupItem.ItemInfo> item) {

            LogUtils.i(TAG, "item header:" + item.header + " item.info.getName:" + item.info.getName() + " item Title:" + item.info.getTitle() + " info id:" + item.info.getId());
            ((TextView) holder.getView(R.id.tv_recipe_tag_name)).setText(item.info.getName());
            holder.getView(R.id.iv_goods_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle boudle = new Bundle();
                    boudle.putSerializable(ClassifyTagRecipePage.TAG_RECIPE, item.info);
                    LogUtils.i(TAG, "item info:" + item.info);
                    UIService.getInstance().postPage(PageKey.ClassifyTagRecipe, boudle);
                }
            });

        }

        @Override
        public void onBindHeaderViewHolder(LinkageSecondaryHeaderViewHolder holder,
                                           BaseGroupedItem<RecipeTagGroupItem.ItemInfo> item) {

            ((TextView) holder.getView(R.id.secondary_header)).setText(item.header.substring(0, 2));
        }

        @Override
        public void onBindFooterViewHolder(LinkageSecondaryFooterViewHolder holder,
                                           BaseGroupedItem<RecipeTagGroupItem.ItemInfo> item) {

        }
    }


}
