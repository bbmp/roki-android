//package com.robam.roki.ui.page;
//
//import android.content.Intent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.request.RequestOptions;
//import com.legent.utils.StringUtils;
//import com.legent.utils.graphic.ImageUtils;
//import com.robam.common.pojos.CookingKnowledge;
//import com.robam.roki.R;
//import com.robam.roki.ui.form.VideoActivity;
//import com.tencent.mm.opensdk.modelbiz.WXOpenBusinessView;
//
///**
// * Created by zhoudingjun on 2017/6/5.
// */
//
//public class RecipeVideoPage extends AbsListViewPage<CookingKnowledge> {
//
//
//    private final String typeCode = "cookbookVideo";
//    private LiveListAdapter adapter = new LiveListAdapter();
//
//    @Override
//    protected void initView() {
//        super.initView();
//        rlAbsTab.setVisibility(View.GONE);
//        getLiveListData();
//    }
//
//    @Override
//    protected void bindAdapter() {
//        home_recipe_ll_livelist.setAdapter(adapter);
//    }
//
//    protected void getLiveListData() {
//
//       /* CookbookManager.getInstance().getCookingKnowledge("cookbookVideo",0,3, new Callback<List<CookingKnowledge>>() {
//            @Override
//            public void onSuccess(List<CookingKnowledge> cookingKnowledges) {
//                if (cookingKnowledges == null || cookingKnowledges.size() <= 0)
//                    cookingKnowledges = Lists.newArrayList();
//                if (start == 0) {
//                    dataList.clear();
//                }
//                LogUtils.i("20170610", "size:" + cookingKnowledges.size());
//                dataList.addAll(cookingKnowledges);
//                adapter.notifyDataSetChanged();
//                home_recipe_ll_livelist.onRefreshComplete();
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                home_recipe_ll_livelist.onRefreshComplete();
//                LogUtils.i("20170610", "onFailure:" + t);
//            }
//        });*/
//    }
//
//    @Override
//    void OnClickRight() {
//        super.OnClickRight();
//    }
//
//
//    /**
//     * 下拉刷新
//     */
//    @Override
//    protected void onPullDown() {
//        super.onPullDown();
//        getLiveListData();
//    }
//
//    /**
//     * 上推加载
//     */
//    @Override
//    protected void onPullUp() {
//        super.onPullUp();
//        getLiveListData();
//    }
//
//    /**
//     * adapter 适配器
//     */
//    class LiveListAdapter extends ListAdapter {
//        ViewHolder viewHolder;
//        RequestOptions options = new RequestOptions().placeholder(R.mipmap.img_default);
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            final CookingKnowledge live = dataList.get(position);
//            if (convertView == null) {
//                convertView = inflater.inflate(R.layout.page_recipe_video, null, false);
//                viewHolder = new ViewHolder();
//                viewHolder.imgView = convertView.findViewById(R.id.iv_recipe_imgv_live);
//                viewHolder.title = convertView.findViewById(R.id.tv_title);
//                convertView.setTag(viewHolder);
//            } else {
//                viewHolder = (ViewHolder) convertView.getTag();
//            }
//            int contentType = live.contentType;
//            if (contentType == 0) {
//                ImageUtils.displayImage(cx, live.pictureCoverUrl, viewHolder.imgView);
//                if (!StringUtils.isNullOrEmpty(live.pictureCoverUrl))
//                    ImageUtils.displayImage(cx, live.pictureCoverUrl, viewHolder.imgView, options);
//                else
//                    viewHolder.imgView.setImageDrawable(r.getDrawable(R.drawable.shape_recipe_void_img_bg));
//                viewHolder.title.setText(live.title);
//            } else {
//                ImageUtils.displayImage(cx, live.videoCoverUrl, viewHolder.imgView);
//                if (!StringUtils.isNullOrEmpty(live.videoCoverUrl))
//                    ImageUtils.displayImage(cx, live.videoCoverUrl, viewHolder.imgView, options);
//                else
//                    viewHolder.imgView.setImageDrawable(r.getDrawable(R.drawable.shape_recipe_void_img_bg));
//                viewHolder.title.setText(live.title);
//            }
//
//
//            //播放视频
//            viewHolder.imgView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), VideoActivity.class);
//                    try {
//                        if (!StringUtils.isNullOrEmpty(live.videoCoverUrl)) {
//                            intent.putExtra("vid", live.videoCoverUrl);
//                            startActivity(intent);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            return convertView;
//        }
//    }
//
//    private static class ViewHolder {
//        public ImageView imgView;//背景图
//        public TextView title;//名字
//    }
//
//
//}
