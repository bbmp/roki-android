package com.robam.roki.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.common.collect.Lists;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.legent.Callback;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.Dc;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.Recipe;
import com.robam.common.services.CookbookManager;
import com.robam.roki.R;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.page.RecipeDetailPage;
import com.robam.roki.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.legent.ContextIniter.context;

/**
 * Created by Administrator on 2016/10/11.
 */

public class DeviceCtrlRecipeView extends PullToRefreshListView implements PullToRefreshBase.OnRefreshListener2 {
    ListView listView;
    View headView;
    List<Recipe> dataList = new ArrayList<Recipe>();
    int start = 0;
    int num = 20;
    String type;
    Resources r;
    RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.img_default) //预加载图片
            .error(R.mipmap.img_default) //加载失败图片
            .priority(Priority.HIGH) //优先级
            .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
            .transform(new RoundedCornersTransformation(50, 10)); //圆角

    private RecipeListAdapter adapter = new RecipeListAdapter();

    public DeviceCtrlRecipeView(Context context) {
        super(context);
        init();
    }

    public DeviceCtrlRecipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DeviceCtrlRecipeView(Context context, Mode mode) {
        super(context, mode);
        init();
    }

    public DeviceCtrlRecipeView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
        init();
    }

    private void init() {
        r = getContext().getResources();
        listView = this.getRefreshableView();
        setMode(PullToRefreshBase.Mode.BOTH);//开启上推和下拉
        setOnRefreshListener(this);
        listView.setSelection(r.getColor(R.color.Transparent));//点击效果颜色透明
        listView.setDivider(null);//设置元素item之间分割线空
        //关闭头部和尾部界限
        listView.setHeaderDividersEnabled(true);
        listView.setFooterDividersEnabled(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe recipe = dataList.get(position - 2);
                if (recipe != null) {
                    // ToastUtils.show("暂未开通",Toast.LENGTH_SHORT);
                    RecipeDetailPage.show(recipe.id, RecipeDetailPage.DeviceRecipePage);
                }
            }
        });

        // 下拉刷新时的提示文本设置
        ILoadingLayout startLabels = getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel(r.getString(com.robam.common.R.string.pull_down_refresh));
        startLabels.setRefreshingLabel(r.getString(com.robam.common.R.string.refreshing_down));
        startLabels.setReleaseLabel(r.getString(com.robam.common.R.string.release_down_refresh));
        // 上拉加载更多时的提示文本设置
        ILoadingLayout endLabels = getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel(r.getString(com.robam.common.R.string.pull_up_refresh));
        endLabels.setRefreshingLabel(r.getString(com.robam.common.R.string.refreshing_up));
        endLabels.setReleaseLabel(r.getString(com.robam.common.R.string.release_up_refresh));
        this.setAdapter(adapter);
    }

    public void show() {
        if (headView != null)
            listView.addHeaderView(headView, null, true);
        if (!StringUtils.isNullOrEmpty(type)) {
            getRecipeByDcFromHttp();
        }
    }

    public void setHeadView(View headView) {
        this.headView = headView;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase pullToRefreshBase) {
        onPullDown();
        getRecipeByDcFromHttp();
    }

    protected void onPullDown() {
        start = 0;
    }

    /**
     * 上推加载
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase pullToRefreshBase) {
        onPullUp();
        getRecipeByDcFromHttp();
    }

    protected void onPullUp() {
        start = start + 2;
        start = start * 10;
    }

    // TODO: 2020/6/4
    String devicePlat="";
    /**
     * 根据设备获取菜谱
     */
    public void getRecipeByDcFromHttp() {
        //获取设备菜谱
        RokiRestHelper.getGroundingRecipesByDevice(type, "all", start, num,
                Reponses.ThumbCookbookResponse.class, new RetrofitCallback<Reponses.ThumbCookbookResponse>() {
                    @Override
                    public void onSuccess(Reponses.ThumbCookbookResponse thumbCookbookResponse) {
                        if (null != thumbCookbookResponse) {
                            List<Recipe> recipes = thumbCookbookResponse.cookbooks;
                            if (recipes == null || recipes.size() <= 0) {
                                recipes = Lists.newArrayList();
                                if (start > 0)
                                    ToastUtils.show(R.string.not_more_recipes, Toast.LENGTH_SHORT);
                            }
                            if (start == 0) {
                                dataList.clear();
                            }
                            dataList.addAll(recipes);
                            adapter.notifyDataSetChanged();
                            onRefreshComplete();
                        }
                    }

                    @Override
                    public void onFaild(String err) {
                        onRefreshComplete();
                    }

        });
    }

    class RecipeListAdapter extends BaseAdapter {
        ViewHolder viewHolder;
//        DisplayImageOptions.Builder options = new DisplayImageOptions.Builder();

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Recipe recipe = dataList.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_home_recommandrecipe, null, false);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_img);

                viewHolder.img_device_one = convertView.findViewById(R.id.img_device_one);
                viewHolder.img_device_two = convertView.findViewById(R.id.img_device_two);
                viewHolder.tv_device_name_one = (TextView) convertView.findViewById(R.id.tv_device_name_one);
                viewHolder.tv_device_name_two = (TextView) convertView.findViewById(R.id.tv_device_name_two);

                viewHolder.nameTv = (TextView) convertView.findViewById(R.id.home_recipe_tv_recipename);
                viewHolder.collectView = (TextView) convertView.findViewById(R.id.home_recipe_tv_collect);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
//            ImageUtils.displayImage(recipe.imgMedium, viewHolder.imageView, options.displayer(new NewRoundBitmapDisplayer(20))
//                    .showImageOnLoading(R.mipmap.img_default).build());
            GlideApp.with(getContext())
                    .load(recipe.imgMedium)
                    .apply(options)
                    .into(viewHolder.imageView);

            viewHolder.nameTv.setText(recipe.name);
            viewHolder.collectView.setText(recipe.collectCount + "人收藏");
            List<Dc> js_dcs = recipe.getJs_dcs();
            List<Dc> listBefore = null;
            if (0 != js_dcs.size()) {
                if (listBefore != null) {
                    listBefore.clear();
                }
                listBefore = ListUtils.getListBefore(js_dcs);


                for (int i = 0; i < listBefore.size(); i++) {
                    if (listBefore.size()==1) {
                        viewHolder.img_device_one.setVisibility(View.VISIBLE);
                        viewHolder.tv_device_name_one.setVisibility(View.VISIBLE);
                        viewHolder.img_device_two.setVisibility(View.GONE);
                        viewHolder.tv_device_name_two.setVisibility(View.GONE);
                        switch (listBefore.get(0).getName()) {
                            case DeviceType.RDKX:
                                viewHolder.tv_device_name_one.setText("烤");
                                viewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                break;
                            case DeviceType.RZQL:
                                viewHolder.tv_device_name_one.setText("蒸");
                                viewHolder.img_device_one.setImageResource(R.mipmap.img_zql_collection);
                                break;
                            case DeviceType.RWBL:
                                viewHolder.tv_device_name_one.setText("微");
                                viewHolder.img_device_one.setImageResource(R.mipmap.img_wbl_collection);
                                break;
                            case DeviceType.RRQZ:
                                viewHolder.tv_device_name_one.setText("灶");
                                viewHolder.img_device_one.setImageResource(R.mipmap.img_rzz_collection);
                                break;
                            case DeviceType.RZKY:
                                viewHolder.tv_device_name_one.setText("一体");
                                viewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                break;
                            case DeviceType.RIKA:
                                viewHolder.tv_device_name_one.setText("RIKA");
                                viewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                break;
                            case DeviceType.KZNZ:
                                viewHolder.tv_device_name_one.setText("智能灶");
                                viewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                break;


                        }

                    }else{
                        viewHolder.img_device_one.setVisibility(View.VISIBLE);
                        viewHolder.tv_device_name_one.setVisibility(View.VISIBLE);
                        viewHolder.img_device_two.setVisibility(View.VISIBLE);
                        viewHolder.tv_device_name_two.setVisibility(View.VISIBLE);
                        switch (listBefore.get(0).getName()) {
                            case DeviceType.RDKX:
                                viewHolder.tv_device_name_one.setText("烤");
                                viewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                break;
                            case DeviceType.RZQL:
                                viewHolder.tv_device_name_one.setText("蒸");
                                viewHolder.img_device_one.setImageResource(R.mipmap.img_zql_collection);
                                break;
                            case DeviceType.RWBL:
                                viewHolder.tv_device_name_one.setText("微");
                                viewHolder.img_device_one.setImageResource(R.mipmap.img_wbl_collection);
                                break;
                            case DeviceType.RRQZ:
                                viewHolder.tv_device_name_one.setText("灶");
                                viewHolder.img_device_one.setImageResource(R.mipmap.img_rzz_collection);
                                break;
                            case DeviceType.RZKY:
                                viewHolder.tv_device_name_one.setText("一体");
                                viewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                break;
                            case DeviceType.RIKA:
                                viewHolder.tv_device_name_one.setText("RIKA");
                                viewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                break;
                            case DeviceType.KZNZ:
                                viewHolder.tv_device_name_one.setText("智能灶");
                                viewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                break;

                        }
                        switch (listBefore.get(1).getName()) {
                            case DeviceType.RDKX:
                                viewHolder.tv_device_name_two.setText("烤");
                                viewHolder.img_device_two.setImageResource(R.mipmap.img_dkx_collection);
                                break;
                            case DeviceType.RZQL:
                                viewHolder.tv_device_name_two.setText("蒸");
                                viewHolder.img_device_two.setImageResource(R.mipmap.img_zql_collection);
                                break;
                            case DeviceType.RWBL:
                                viewHolder.tv_device_name_two.setText("微");
                                viewHolder.img_device_two.setImageResource(R.mipmap.img_wbl_collection);
                                break;
                            case DeviceType.RRQZ:
                                viewHolder.tv_device_name_two.setText("灶");
                                viewHolder.img_device_two.setImageResource(R.mipmap.img_rzz_collection);
                                break;
                            case DeviceType.RZKY:
                                viewHolder.tv_device_name_two.setText("一体");
                                viewHolder.img_device_two.setImageResource(R.mipmap.img_dkx_collection);
                                break;
                            case DeviceType.RIKA:
                                viewHolder.tv_device_name_two.setText("RIKA");
                                viewHolder.img_device_two.setImageResource(R.mipmap.img_dkx_collection);
                                break;
                            case DeviceType.KZNZ:
                                viewHolder.tv_device_name_one.setText("智能灶");
                                viewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                break;


                        }

                    }
                }




            }
            return convertView;
        }
    }

    private static class ViewHolder {
        ImageView imageView;
        ImageView img_device_one;
        ImageView img_device_two;
        TextView nameTv;
        TextView collectView;
        TextView tv_device_name_one;
        TextView tv_device_name_two;
    }
}
