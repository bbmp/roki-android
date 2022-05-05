package com.robam.roki.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.LogUtils;
import com.robam.roki.R;

import java.util.List;

/**
 * Created by Dell on 2018/10/18.
 */

public class SerilizerCommonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    /**
     * 0位置的数据
     */
    public static final int GridSHOW = 0;

    public static final int ITEMSHOW = 1;

    private List<DeviceConfigurationFunctions> configData;

    List<DeviceConfigurationFunctions> mainList;
    List<DeviceConfigurationFunctions> otherList;

    private LayoutInflater mLayoutInflater;

    Context cx;

    private int currentType = GridSHOW; //当前类型

    public SerilizerCommonAdapter(Context context, List<DeviceConfigurationFunctions> mainList,List<DeviceConfigurationFunctions> otherList){
        this.cx = context;
        this.mainList = mainList;
        this.otherList = otherList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == GridSHOW) {
            return new OvenComViewHolder(mLayoutInflater.inflate(R.layout.serilizer_grid_mode, null));
        }else if (viewType == ITEMSHOW){//item_otherfunc_page
            return new OvenItemViewHolder(mLayoutInflater.inflate(R.layout.oven_recycleview_show, null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)== GridSHOW){
            OvenComViewHolder ovenComViewHolder = (OvenComViewHolder) holder;
            ovenComViewHolder.setData(mainList);
        }else if (getItemViewType(position)==ITEMSHOW){
            OvenItemViewHolder ovenItemViewHolder = (OvenItemViewHolder) holder;
            ovenItemViewHolder.setData(otherList);
        }
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
            default:
                break;
        }
        return currentType;
    }

    @Override
    public int getItemCount() {
        return 2;
    }



    class OvenComViewHolder extends RecyclerView.ViewHolder{
        GridView gv;
        private List<DeviceConfigurationFunctions> mainList;
        public OvenComViewHolder(View itemView) {
            super(itemView);
            gv = itemView.findViewById(R.id.gv_show);
        }

        private void setData(final List<DeviceConfigurationFunctions> mainList){
            this.mainList = mainList;
            LogUtils.i("20181018","mainList2222::"+mainList.toString());
            gv.setNumColumns(mainList.size());
            gv.setAdapter(new MyGridViewAdapter());
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
                return i;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup viewGroup) {
               ViewHolder holder = null;
                if (convertView == null) {
                    //没缓存
                    convertView = mLayoutInflater.inflate(R.layout.oven_item_show, null);
                    holder = new OvenComViewHolder.ViewHolder();
                    holder.iv = convertView.findViewById(R.id.iv_channel);
                    holder.tv = convertView.findViewById(R.id.tv_channel);
                    convertView.setTag(holder);
                } else {
                    //有缓存
                    holder = (ViewHolder) convertView.getTag();
                }
                //绑定viewholder数据
                holder.tv.setText(mainList.get(position).functionName);
                Glide.with(cx).load(mainList.get(position).backgroundImg).into(holder.iv);
                return convertView;
            }
        }

        class ViewHolder {
            ImageView iv;
            TextView tv;
        }
    }


    //烤模式等列表
    class OvenItemViewHolder extends  RecyclerView.ViewHolder{

        RecyclerView recycle;
        List<DeviceConfigurationFunctions> otherList;

        public OvenItemViewHolder(View itemView) {
            super(itemView);
            recycle = itemView.findViewById(R.id.recycle);

        }

        private void setData(List<DeviceConfigurationFunctions> otherList){
            this.otherList = otherList;
            LogUtils.i("20181018","otherList22222::"+otherList.toString());
            recycle.setLayoutManager(new LinearLayoutManager(cx, LinearLayoutManager.VERTICAL, false));
            recycle.setAdapter(new ItemshowAdapter());
        }

        class ItemshowAdapter extends RecyclerView.Adapter<ItemshowAdapter.ItemViewHolder>{


            @Override
            public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ItemViewHolder(mLayoutInflater.inflate(R.layout.item_otherfunc_page,null));
            }

            @Override
            public void onBindViewHolder(ItemshowAdapter.ItemViewHolder holder, final int position) {
               ItemViewHolder itemViewHolder = holder;
                Glide.with(cx).load(otherList.get(position).backgroundImg).into(itemViewHolder.mImageView);
                itemViewHolder.mTvName.setText(otherList.get(position).functionName);
                itemViewHolder.mTvDesc.setText(otherList.get(position).msg);
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

    public GridViewOnclick gridViewOnclickLister;

    public ItemViewOnclick itemViewOnclickLister;

    public void setGridViewOnclickLister(GridViewOnclick gridViewOnclickLister) {
        this.gridViewOnclickLister = gridViewOnclickLister;
    }

    public void setItemViewOnclickLister(ItemViewOnclick itemViewOnclickLister) {
        this.itemViewOnclickLister = itemViewOnclickLister;
    }

}
