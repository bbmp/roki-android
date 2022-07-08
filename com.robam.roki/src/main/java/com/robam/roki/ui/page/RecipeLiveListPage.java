package com.robam.roki.ui.page;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.graphic.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.robam.common.events.HomeRecipeViewEvent;
import com.robam.common.events.UMPushVideoEvent;
import com.robam.common.pojos.RecipeLiveList;
import com.robam.common.services.CookbookManager;
import com.robam.common.util.RecipeRequestIdentification;
import com.robam.roki.R;
import com.robam.roki.ui.form.VideoActivity;

import java.util.List;

/**
 * Created by as on 2016/8/3.
 */

public class RecipeLiveListPage extends AbsListViewPage<RecipeLiveList> {
    private LiveListAdapter adapter = new LiveListAdapter();

    @Override
    protected void initView() {
        super.initView();
        getLiveListData();
        setTitle("视频");
        listView.setDivider(null);
    }

    //在当前页面UM推送消息时重新加载
    @Subscribe
    public void onEvent(UMPushVideoEvent event) {
        initView();
    }

    @Override
    protected void bindAdapter() {
        home_recipe_ll_livelist.setAdapter(adapter);
    }

    @Override
    protected void setTitle(String title) {
        super.setTitle("视频");
    }

    protected void getLiveListData() {
        CookbookManager.getInstance().getRecipeLiveList(start, num, new Callback<List<RecipeLiveList>>() {
            @Override
            public void onSuccess(List<RecipeLiveList> recipeLiveLists) {
                if (recipeLiveLists == null || recipeLiveLists.size() <= 0)
                    recipeLiveLists = Lists.newArrayList();
                if (start == 0) {
                    dataList.clear();
                }
                dataList.addAll(recipeLiveLists);
                adapter.notifyDataSetChanged();
                home_recipe_ll_livelist.onRefreshComplete();
            }

            @Override
            public void onFailure(Throwable t) {
                home_recipe_ll_livelist.onRefreshComplete();
            }
        });
    }

    @Override
    void OnClickRight() {
        super.OnClickRight();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            EventUtils.postEvent(new HomeRecipeViewEvent(HomeRecipeViewEvent.LiveListToHomePage));
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 下拉刷新
     */
    @Override
    protected void onPullDown() {
        super.onPullDown();
        getLiveListData();
    }

    /**
     * 上推加载
     */
    @Override
    protected void onPullUp() {
        super.onPullUp();
        getLiveListData();
    }

    /**
     * 返回操作
     */
    @Override
    void OnClickLeft() {
        EventUtils.postEvent(new HomeRecipeViewEvent(HomeRecipeViewEvent.LiveListToHomePage));
        super.OnClickLeft();
        UIService.getInstance().popBack();
    }

    /**
     * adapter 适配器
     */
    class LiveListAdapter extends ListAdapter {
        ViewHolder viewHolder;
        DisplayImageOptions.Builder options = new DisplayImageOptions.Builder();

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final RecipeLiveList live = dataList.get(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.view_home_live_list_item, null, false);
                viewHolder = new ViewHolder();
                viewHolder.imgView = convertView.findViewById(R.id.home_recipe_imgv_live);
                viewHolder.title = convertView.findViewById(R.id.home_recipe_tv_live_title);
                viewHolder.desc = convertView.findViewById(R.id.home_recipe_tv_live_detail);
                viewHolder.recipe_detail = convertView.findViewById(R.id.home_recipe_tv_live_check);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ImageUtils.displayImage(live.imgUrl, viewHolder.imgView);
            if (!StringUtils.isNullOrEmpty(live.imgUrl))
                ImageUtils.displayImage(live.imgUrl, viewHolder.imgView, options.showImageOnLoading(R.mipmap.img_default).build());
            else
                viewHolder.imgView.setImageDrawable(r.getDrawable(R.mipmap.img_default));
            viewHolder.title.setText(live.name);
            viewHolder.desc.setText(live.desc);
            //查看菜谱详情
            viewHolder.recipe_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecipeDetailPage.show(live.id, RecipeDetailPage.LiveRecipeShow, RecipeRequestIdentification.RECIPE_VIDEO);
                }
            });
            //播放视频
            viewHolder.imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  Bundle bundle = new Bundle();
                    //  http://v.youku.com/v_show/id_XMTYyMzk0NzQ2MA==.html
                    // bundle.putString(PageArgumentKey.CONSTANCE_PLAY_ID,live.uri.split("id_")[1].split(".html")[0]);
                    //  bundle.putString(PageArgumentKey.CONSTANCE_PLAY_ID,live.name);
                    //  UIService.getInstance().postPage(PageKey.VodPlayer,bundle);
                 //   Intent intent = new Intent(getActivity(), PlayerActivity.class);
                    Intent intent = new Intent(getActivity(), VideoActivity.class);
                    try {
                        if (! StringUtils.isNullOrEmpty(live.videoId)){
                            LogUtils.i("20170810","live;::"+live.videoId);
                            intent.putExtra("vid", live.videoId);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        ToastUtils.show("解析数据异常", Toast.LENGTH_SHORT);
                        e.printStackTrace();
                        LogUtils.i("20170815","e::"+e.getMessage());
                    }

                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView imgView;//背景图
        public TextView title;//标题
        public TextView desc;//详细
        public TextView recipe_detail;//查看菜谱
    }
}
