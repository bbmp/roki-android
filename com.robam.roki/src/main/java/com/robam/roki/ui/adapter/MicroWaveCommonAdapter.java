package com.robam.roki.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.LogUtils;
import com.robam.roki.R;
import com.robam.roki.ui.page.device.oven.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2018/7/5.
 */

public class MicroWaveCommonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    /**
     * 0位置的数据
     */
    public static final int GridSHOW = 0;

    public static final int GridMORE = 1;

    public static final int ITEMSHOW = 2;

    private List<DeviceConfigurationFunctions> configData;

    List<DeviceConfigurationFunctions> mainList;
    List<DeviceConfigurationFunctions> otherList;
    List<DeviceConfigurationFunctions>  moreList=new ArrayList<>();

    private  LayoutInflater mLayoutInflater;

    Context cx;

    private int currentType = GridSHOW; //当前类型

    public MicroWaveCommonAdapter(Context context, List<DeviceConfigurationFunctions> mainList,
                                  List<DeviceConfigurationFunctions> otherList){
        this.cx = context;
        this.mainList = mainList;
        this.otherList = otherList;
        this.mLayoutInflater = LayoutInflater.from(context);
        LogUtils.i("20180913","MAIN:" + mainList.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == GridSHOW) {
            return new MicroWaveViewHolder(mLayoutInflater.inflate(R.layout.steamoven_grid_mode, null));
        }else if (viewType == GridMORE){
            return new MicroWaveMoreViewHolder(mLayoutInflater.inflate(R.layout.steamoven_more_mode_show,null));
        } else if (viewType == ITEMSHOW){//item_otherfunc_page
            return new MicroWaveItemViewHolder(mLayoutInflater.inflate(R.layout.oven_recycleview_show, null));
        }
        return null;
    }

    MicroWaveMoreViewHolder moreViewHolder;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)== GridSHOW){
            MicroWaveViewHolder microWaveViewHolder = (MicroWaveViewHolder) holder;
            microWaveViewHolder.setData(mainList);
        }else if (getItemViewType(position)==GridMORE){
            moreViewHolder = (MicroWaveMoreViewHolder) holder;
        } else if (getItemViewType(position)==ITEMSHOW){
            MicroWaveItemViewHolder microWaveItemViewHolder = (MicroWaveItemViewHolder) holder;
            microWaveItemViewHolder.setData(otherList);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case GridSHOW:
                currentType = GridSHOW;
                break;
            case ITEMSHOW:
                currentType = ITEMSHOW;
                break;
            case GridMORE:
                currentType = GridMORE;
                break;
            default:
                break;
        }
        return currentType;
    }

    //打开更多列表
    public void upMoreView(List<DeviceConfigurationFunctions> moreList){
        moreViewHolder.setAddView(moreList);
        this.notifyDataSetChanged();
        LogUtils.i("20180731"," upMoreView:");
    }

    //关闭更多列表
    public void removeMoreView(){
        moreViewHolder.setRemoveView();
        this.notifyDataSetChanged();
    }

    //更多列表
    class MicroWaveMoreViewHolder extends RecyclerView.ViewHolder{
        MyGridView myGridView;
        private List<DeviceConfigurationFunctions> moreL;

        public MicroWaveMoreViewHolder(View itemView) {
            super(itemView);
            myGridView = itemView.findViewById(R.id.gv_show);
        }

        protected void setAddView(List<DeviceConfigurationFunctions> moreList){
            moreL = moreList;
            myGridView.setVisibility(View.VISIBLE);
            OvenMoreGridAdapter moreGridAdapter = new OvenMoreGridAdapter(cx, moreL);
            myGridView.setAdapter(moreGridAdapter);
            myGridView.startAnimation(AnimationUtils.loadAnimation(cx, R.anim.in_from_right));
            myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    view.setTag(moreL.get(position).functionCode);
                    if (gridViewOnclickLister!=null){
                        gridViewOnclickLister.onGridClick(moreL.get(position).functionCode);
                    }
                }
            });
        }

        protected void setRemoveView(){
            myGridView.setVisibility(View.GONE);
            // moreAdd.startAnimation(AnimationUtils.loadAnimation(cx, R.anim.out_from_right));
        }
    }


    //前三的菜布局
    class MicroWaveViewHolder extends RecyclerView.ViewHolder{
        GridView gv;
        ImageView moreTup;
        ImageView moreTdown;
        LinearLayout moreV;
        boolean falg = true;
        private List<DeviceConfigurationFunctions> mainList;
        public MicroWaveViewHolder(View itemView) {
            super(itemView);
            gv = itemView.findViewById(R.id.gv_show);
            moreTup = itemView.findViewById(R.id.v_more_up);
            moreTdown = itemView.findViewById(R.id.v_more_down);
            moreV = itemView.findViewById(R.id.more_v);
        }

        private void setData(final List<DeviceConfigurationFunctions> mainList){
            this.mainList = mainList;
            gv.setAdapter(new MicroWaveViewHolder.MyGridViewAdapter());
            moreV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (falg){
                        moreTdown.setVisibility(View.INVISIBLE);
                        moreTup.setVisibility(View.VISIBLE);
                        falg =false;
                    }else{
                        moreTdown.setVisibility(View.VISIBLE);
                        moreTup.setVisibility(View.INVISIBLE);
                        falg = true;
                    }

                    if (gridViewOnclickLister!=null){
                        gridViewOnclickLister.onGridClick("more");
                    }
                }
            });
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //  ToastUtils.show(position+"", Toast.LENGTH_SHORT);
                    view.setTag(mainList.get(position).functionCode);
                    if (gridViewOnclickLister!=null){
                        gridViewOnclickLister.onGridClick(mainList.get(position).functionCode);
                    }
                }
            });
        }

        //gridview适配器
        class MyGridViewAdapter extends BaseAdapter {
            @Override
            public int getCount() {
                return mainList.size()<0 ? 0:mainList.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup viewGroup) {
                try{
                    MicroWaveViewHolder.ViewHolder holder = null;
                    if (convertView == null) {
                        //没缓存
                        convertView = mLayoutInflater.inflate(R.layout.oven_item_show, null);
                        holder = new MicroWaveViewHolder.ViewHolder();
                        holder.iv = convertView.findViewById(R.id.iv_channel);
                        holder.tv = convertView.findViewById(R.id.tv_channel);
                        convertView.setTag(holder);
                    } else {
                        //有缓存
                        holder = (MicroWaveViewHolder.ViewHolder) convertView.getTag();
                    }
                    //绑定viewholder数据
                    holder.tv.setText(mainList.get(position).functionName);
                    Glide.with(cx).load(mainList.get(position).backgroundImg).into(holder.iv);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return convertView;
            }
        }

        class ViewHolder {
            ImageView iv;
            TextView tv;
        }
    }




    //烤模式等列表
    class MicroWaveItemViewHolder extends  RecyclerView.ViewHolder{

        RecyclerView recycle;
        List<DeviceConfigurationFunctions> otherList;

        public MicroWaveItemViewHolder(View itemView) {
            super(itemView);
            recycle = itemView.findViewById(R.id.recycle);

        }

        private void setData(List<DeviceConfigurationFunctions> otherList){
            this.otherList = otherList;
            recycle.setLayoutManager(new LinearLayoutManager(cx, LinearLayoutManager.VERTICAL, false));
            recycle.setAdapter(new MicroWaveItemViewHolder.ItemshowAdapter());
        }

        class ItemshowAdapter extends RecyclerView.Adapter<MicroWaveItemViewHolder.ItemshowAdapter.ItemViewHolder>{

            @Override
            public MicroWaveItemViewHolder.ItemshowAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new MicroWaveItemViewHolder.ItemshowAdapter.ItemViewHolder(mLayoutInflater.inflate(R.layout.item_otherfunc_page,null));
            }

            @Override
            public void onBindViewHolder(MicroWaveItemViewHolder.ItemshowAdapter.ItemViewHolder holder, final int position) {
                MicroWaveItemViewHolder.ItemshowAdapter.ItemViewHolder itemViewHolder = holder;
                Glide.with(cx).load(otherList.get(position).backgroundImg).into(itemViewHolder.mImageView);
                itemViewHolder.mTvName.setText(otherList.get(position).functionName);
                itemViewHolder.mTvDesc.setText(otherList.get(position).msg.trim());
                holder.itemView.setTag(otherList.get(position).functionCode);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ToastUtils.show(""+position,Toast.LENGTH_SHORT);
                        if (itemViewOnclickLister!=null){
                            itemViewOnclickLister.onItemClick(otherList.get(position).functionCode);
                        }
                    }
                });
            }

            @Override
            public int getItemCount() {
                return otherList.size()<0?0:otherList.size();
            }

            class ItemViewHolder extends RecyclerView.ViewHolder{
                ImageView mImageView;
                TextView mTvDesc;
                TextView mTvName;
                public ItemViewHolder(View itemView) {
                    super(itemView);
                    mTvName = itemView.findViewById(R.id.tv_name);
                    mImageView = itemView.findViewById(R.id.iv_view);
                    mTvDesc = itemView.findViewById(R.id.tv_desc);
                }
            }
        }


    }

    public interface GridViewOnclick{
        void onGridClick(String pos);
    }

    public interface ItemViewOnclick{
        void onItemClick(String pos);
    }

    public MicroWaveCommonAdapter.GridViewOnclick gridViewOnclickLister;

    public MicroWaveCommonAdapter.ItemViewOnclick itemViewOnclickLister;

    public void setGridViewOnclickLister(MicroWaveCommonAdapter.GridViewOnclick gridViewOnclickLister) {
        this.gridViewOnclickLister = gridViewOnclickLister;
    }

    public void setItemViewOnclickLister(MicroWaveCommonAdapter.ItemViewOnclick itemViewOnclickLister) {
        this.itemViewOnclickLister = itemViewOnclickLister;
    }
}
