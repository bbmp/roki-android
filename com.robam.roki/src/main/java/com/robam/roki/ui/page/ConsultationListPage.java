package com.robam.roki.ui.page;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.legent.Callback;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.graphic.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.robam.common.events.UMPushInfoEvent;
import com.robam.common.pojos.RecipeConsultation;
import com.robam.common.services.CookbookManager;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.view.NewRoundBitmapDisplayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/22.
 */

public class ConsultationListPage extends AbsListViewPage<RecipeConsultation> implements AdapterView.OnItemClickListener {
    private List<RecipeConsultation> list;//咨询列表
    RecipeConsultationAdapter adapter = new RecipeConsultationAdapter();
    final int size=20;
    int page=0;
    @Override
    protected void initbefore() {
        super.initbefore();
        setTitle("文章资讯");
    }

    @Override
    protected void initAfter() {
        super.initAfter();
    }

    @Override
    protected void initView() {
        super.initView();
        getInitData();
        home_recipe_ll_livelist.setOnItemClickListener(this);
        //home_recipe_ll_livelist.setMode(PullToRefreshBase.Mode.DISABLED);//禁止上推和下拉
        // getListData();
    }


    @Subscribe
    public void onEvent(UMPushInfoEvent event) {
        initView();
    }

    /**
     * 导航栏左侧点击
     */
    @Override
    void OnClickLeft() {
        super.OnClickLeft();
        UIService.getInstance().popBack();
    }

    @Override
    protected void bindAdapter() {
        home_recipe_ll_livelist.setAdapter(adapter);
    }

   /* *//**
     * 获取咨询列表数据
     *//*
    void getListData() {
        list = (List<RecipeConsultation>) bundle.getSerializable(PageArgumentKey.RecipeConsultation);
        dataList.addAll(list);
        adapter.notifyDataSetChanged();
    }*/

    void getInitData(){
        list = (List<RecipeConsultation>) bundle.getSerializable(PageArgumentKey.RecipeConsultation);
        dataList.addAll(list);
    }


    //下拉刷新
    @Override
    public void onPullDownToRefresh(PullToRefreshBase pullToRefreshBase) {
        super.onPullDownToRefresh(pullToRefreshBase);
       // getData();
        getUpData();
    }

    //上拉加载更多
    @Override
    public void onPullUpToRefresh(PullToRefreshBase pullToRefreshBase) {
        super.onPullUpToRefresh(pullToRefreshBase);
         getData();
       // getUpData();
    }

    void getUpData(){
        CookbookManager.getInstance().getConsultationList(0,20,new Callback<List<RecipeConsultation>>() {
            @Override
            public void onSuccess(List<RecipeConsultation> list1) {
                if (list1 == null || list1.size() == 0)
                    return;
                list.clear();
                for (int i = 0; i <list1.size() ; i++) {
                    list.add(list1.get(i));
                }
                dataList.clear();
                dataList.addAll(list);
                adapter.notifyDataSetChanged();
                ToastUtils.show("刷新成功",Toast.LENGTH_SHORT);
                home_recipe_ll_livelist.onRefreshComplete();
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("刷新失败", Toast.LENGTH_SHORT);
            }
        });
    }


    void getData(){
        page=page+1;
        CookbookManager.getInstance().getConsultationList(page,size,new Callback<List<RecipeConsultation>>() {
            @Override
            public void onSuccess(List<RecipeConsultation> list1) {

                if (list1 == null || list1.size() == 0)
                    return;
                for (int i = 0; i <list1.size() ; i++) {
                    list.add(list1.get(i));
                }
                dataList.clear();
                dataList.addAll(list);
                adapter.notifyDataSetChanged();
                ToastUtils.show("刷新成功",Toast.LENGTH_SHORT);
                home_recipe_ll_livelist.onRefreshComplete();
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("刷新失败", Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.WebTitle, dataList.get(position - 1).title);
        bd.putString(PageArgumentKey.Url, dataList.get(position - 1).contentUrl);
        UIService.getInstance().postPage(PageKey.WebClientNew, bd);
    }

    class RecipeConsultationAdapter extends ListAdapter {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ViewHolder viewHolder;
        DisplayImageOptions.Builder options = new DisplayImageOptions.Builder();

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RecipeConsultation consultation = dataList.get(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.view_home_consutationlist, null, false);
                viewHolder = new ViewHolder(cx);
                viewHolder.img = convertView.findViewById(R.id.home_consutation_imgv_recommendrecipe);
                viewHolder.title = convertView.findViewById(R.id.home_consutation_tv_title);
                viewHolder.time = convertView.findViewById(R.id.tv_time);
                viewHolder.desc = convertView.findViewById(R.id.con_desc);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (!StringUtils.isNullOrEmpty(consultation.imageUrl)){
                ImageUtils.displayImage(consultation.imageUrl, viewHolder.img, options.displayer(new NewRoundBitmapDisplayer(20))
                        .showImageOnLoading(R.mipmap.img_default).build());
            } else
                viewHolder.img.setImageDrawable(r.getDrawable(R.mipmap.img_default));

                viewHolder.title.setText(consultation.title);
                String insertTime = consultation.insertTime;
                long mmTime = Long.parseLong(insertTime);
                String time = sdf.format(new Date(mmTime));
                viewHolder.time.setText(time);
            LogUtils.i("2018-316","dd::"+consultation.description);
                viewHolder.desc.setText(consultation.description);
            return convertView;
        }
    }

    private static class ViewHolder {
        Context cx;

        public ViewHolder(Context cx) {
            this.cx = cx;
        }

        ImageView img;
        TextView title;
        TextView time;
        TextView desc;
    }
}
