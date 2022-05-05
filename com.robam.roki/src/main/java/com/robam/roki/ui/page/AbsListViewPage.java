//package com.robam.roki.ui.page;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.handmark.pulltorefresh.library.ILoadingLayout;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshListView;
//import com.legent.ui.ext.BasePage;
//import com.robam.roki.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//
///**
// * Created by rent on 2016/8/3. 通用列表页面
// */
//
//public class AbsListViewPage<Type> extends BasePage implements PullToRefreshBase.OnRefreshListener2 {
//    @InjectView(R.id.home_recipe_ll_livelist)//数据列表 LinearLayout ,注意 ：此处不能用瀑布流模型 rent
//            PullToRefreshListView home_recipe_ll_livelist;
//    @InjectView(R.id.home_recipe_live_title)//标题 部分
//            TextView home_recipe_live_title;
//    @InjectView(R.id.listview_titlebar_ll_right)//导航栏右侧
//            LinearLayout listview_titlebar_ll_right;
//    @InjectView(R.id.home_recipe_live_imgv_return)//导航栏返回
//            ImageView home_recipe_live_imgv_return;
//
//    ListView listView;
//    View contentView;
//    LayoutInflater inflater;
//    int start = 0;
//    int num = 10;
//    List<Type> dataList = new ArrayList<Type>();
//    Bundle bundle;
//    ViewGroup container;
//    @InjectView(R.id.rl_abs_tab)
//    RelativeLayout rlAbsTab;
//    protected ArrayAdapter adapter;
//    @InjectView(R.id.iv_arw_left)
//    ImageView ivArwLeft;
//    @InjectView(R.id.iv_arw_right)
//    ImageView ivArwRight;
//
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        bundle = getArguments();
//        if (inflater == null)
//            inflater = LayoutInflater.from(cx);
//        this.inflater = inflater;
//        this.container = container;
//        init();
//        return contentView;
//    }
//
//    private void init() {
//        contentView = inflater.inflate(R.layout.view_home_listview, container, false);
//        ButterKnife.inject(this, contentView);
//        listView = home_recipe_ll_livelist.getRefreshableView();
//        initbefore();
//        initView();
//        initAfter();
//    }
//
//    protected void initView() {
//        bindAdapter();
//        home_recipe_ll_livelist.setMode(PullToRefreshBase.Mode.BOTH);//开启上推和下拉
//        home_recipe_ll_livelist.setOnRefreshListener(this);
//        listView.setSelection(r.getColor(R.color.Transparent));//点击效果颜色透明
//        listView.setDivider(null);//设置元素item之间分割线空
//        //关闭头部和尾部界限
//        listView.setHeaderDividersEnabled(true);
//        listView.setFooterDividersEnabled(false);
//
//        // 下拉刷新时的提示文本设置
//        ILoadingLayout startLabels = home_recipe_ll_livelist.getLoadingLayoutProxy(true, false);
//        startLabels.setPullLabel(r.getString(com.robam.common.R.string.pull_down_refresh));
//        startLabels.setRefreshingLabel(r.getString(com.robam.common.R.string.refreshing_down));
//        startLabels.setReleaseLabel(r.getString(com.robam.common.R.string.release_down_refresh));
//        // 上拉加载更多时的提示文本设置
//        ILoadingLayout endLabels = home_recipe_ll_livelist.getLoadingLayoutProxy(false, true);
//        endLabels.setPullLabel(r.getString(com.robam.common.R.string.pull_up_refresh));
//        endLabels.setRefreshingLabel(r.getString(com.robam.common.R.string.refreshing_up));
//        endLabels.setReleaseLabel(r.getString(com.robam.common.R.string.release_up_refresh));
//    }
//
//    protected void initbefore() {
//
//    }
//
//    protected void initAfter() {
//
//    }
//
//    protected void bindAdapter() {
//    }
//
//
//    protected void setTitle(String title) {
//        home_recipe_live_title.setText(title);
//    }
//
//    protected String getTitle(){
//        String text = (String) home_recipe_live_title.getText();
//        return text;
//    }
//
//    protected void setRightLinearLayout(View child) {
//        listview_titlebar_ll_right.addView(child);
//    }
//
//    /**
//     * 标题右侧按钮
//     */
//    @OnClick(R.id.listview_titlebar_ll_right)
//    void OnClickRight() {
//    }
//
//    /**
//     * 标题左侧返回按钮
//     */
//    @OnClick(R.id.home_recipe_live_imgv_return)
//    void OnClickLeft() {
//
//    }
//
//    /**
//     * 下拉刷新
//     */
//    @Override
//    public void onPullDownToRefresh(PullToRefreshBase pullToRefreshBase) {
//        onPullDown();
//    }
//
//    protected void onPullDown() {
//        start = 0;
//    }
//
//    /**
//     * 上推加载
//     */
//    @Override
//    public void onPullUpToRefresh(PullToRefreshBase pullToRefreshBase) {
//        onPullUp();
//    }
//
//    protected void onPullUp() {
//        start = start + 10;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.reset(this);
//    }
//
//
//    class ListAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return dataList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return position;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            return null;
//        }
//    }
//}
