package com.robam.roki.ui.adapter;

import android.content.Context;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
public class SteamOvenCommonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * 0位置的数据
     */
    public static final int GridSHOW = 0;

    public static final int GridMORE = 1;

    public static final int ITEMSHOW = 2;

    private List<DeviceConfigurationFunctions> configData;

    List<DeviceConfigurationFunctions> mainList;
    List<DeviceConfigurationFunctions> otherList;
    List<DeviceConfigurationFunctions> moreList = new ArrayList<>();

    private LayoutInflater mLayoutInflater;

    Context cx;

    private int currentType = GridSHOW; //当前类型

    public SteamOvenCommonAdapter(Context context, List<DeviceConfigurationFunctions> mainList, List<DeviceConfigurationFunctions> otherList) {
        this.cx = context;
        this.mainList = mainList;
        this.otherList = otherList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == GridSHOW) {
            return new SteamOvenCommonAdapter.SteamOvenComViewHolder(mLayoutInflater.inflate(R.layout.steamoven_grid_mode, null));
        } else if (viewType == GridMORE) {
            return new SteamOvenCommonAdapter.SteamOvenMoreViewHolder(mLayoutInflater.inflate(R.layout.steamoven_more_mode_show, null));
        } else if (viewType == ITEMSHOW) {//item_otherfunc_page
            return new SteamOvenCommonAdapter.SteamOvenItemViewHolder(mLayoutInflater.inflate(R.layout.oven_recycleview_show, null));
        }
        return null;
    }

    SteamOvenCommonAdapter.SteamOvenMoreViewHolder moreViewHolder;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == GridSHOW) {
            SteamOvenCommonAdapter.SteamOvenComViewHolder steamOvenComViewHolder = (SteamOvenCommonAdapter.SteamOvenComViewHolder) holder;
            steamOvenComViewHolder.setData(mainList);
        } else if (getItemViewType(position) == GridMORE) {
            moreViewHolder = (SteamOvenCommonAdapter.SteamOvenMoreViewHolder) holder;
        } else if (getItemViewType(position) == ITEMSHOW) {
            SteamOvenCommonAdapter.SteamOvenItemViewHolder steamOvenItemViewHolder = (SteamOvenCommonAdapter.SteamOvenItemViewHolder) holder;
            steamOvenItemViewHolder.setData(otherList);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
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
    public void upMoreView(List<DeviceConfigurationFunctions> moreList) {
        moreViewHolder.setAddView(moreList);
        this.notifyDataSetChanged();
        LogUtils.i("20180731", " upMoreView:");
    }

    //关闭更多列表
    public void removeMoreView() {
        moreViewHolder.setRemoveView();
        this.notifyDataSetChanged();
    }

    //更多列表
    class SteamOvenMoreViewHolder extends RecyclerView.ViewHolder {
        MyGridView myGridView;
        private List<DeviceConfigurationFunctions> moreL;

        public SteamOvenMoreViewHolder(View itemView) {
            super(itemView);
            myGridView = (MyGridView) itemView.findViewById(R.id.gv_show);
        }

        protected void setAddView(List<DeviceConfigurationFunctions> moreList) {
            moreL = moreList;
            myGridView.setVisibility(View.VISIBLE);
            OvenMoreGridAdapter moreGridAdapter = new OvenMoreGridAdapter(cx, moreL);
            myGridView.setAdapter(moreGridAdapter);
            myGridView.startAnimation(AnimationUtils.loadAnimation(cx, R.anim.in_from_right));
            myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    view.setTag(moreL.get(position).functionCode);
                    if (gridViewOnclickLister != null) {
                        gridViewOnclickLister.onGridClick(moreL.get(position).functionCode);
                    }
                }
            });
        }

        protected void setRemoveView() {
            myGridView.setVisibility(View.GONE);
            // moreAdd.startAnimation(AnimationUtils.loadAnimation(cx, R.anim.out_from_right));
        }
    }


    //前三的菜布局
    class SteamOvenComViewHolder extends RecyclerView.ViewHolder {
        GridView gv;
        ImageView moreTup;
        ImageView moreTdown;
        LinearLayout moreV;
        boolean falg = true;
        private List<DeviceConfigurationFunctions> mainList;

        public SteamOvenComViewHolder(View itemView) {
            super(itemView);
            gv = (GridView) itemView.findViewById(R.id.gv_show);
            moreTup = (ImageView) itemView.findViewById(R.id.v_more_up);
            moreTdown = (ImageView) itemView.findViewById(R.id.v_more_down);
            moreV = (LinearLayout) itemView.findViewById(R.id.more_v);
        }

        private void setData(final List<DeviceConfigurationFunctions> mainList) {
            this.mainList = mainList;
            if (mainList.size()==0){
                moreV.setVisibility(View.GONE);
                return;
            }
            gv.setAdapter(new SteamOvenCommonAdapter.SteamOvenComViewHolder.MyGridViewAdapter());
            if (!"more".equals(mainList.get(mainList.size()-1).functionCode)){
                moreV.setVisibility(View.GONE);
            }else{
                moreV.setVisibility(View.VISIBLE);
            }
            if (mainList.size()==3) {
                moreV.setVisibility(View.VISIBLE);
            }else{
                moreV.setVisibility(View.GONE);
            }

            moreV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (falg) {
                        moreTdown.setVisibility(View.INVISIBLE);
                        moreTup.setVisibility(View.VISIBLE);
                        falg = false;
                    } else {
                        moreTdown.setVisibility(View.VISIBLE);
                        moreTup.setVisibility(View.INVISIBLE);
                        falg = true;
                    }

                    if (gridViewOnclickLister != null) {
                        gridViewOnclickLister.onGridClick("more");
                    }
                }
            });
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //  ToastUtils.show(position+"", Toast.LENGTH_SHORT);
                    view.setTag(mainList.get(position).functionCode);
                    if (gridViewOnclickLister != null) {
                        gridViewOnclickLister.onGridClick(mainList.get(position).functionCode);
                    }
                }
            });
        }

        //gridview适配器
        class MyGridViewAdapter extends BaseAdapter {
            @Override
            public int getCount() {
                return mainList.size() < 0 ? 0 : mainList.size();
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
                try {
                    SteamOvenCommonAdapter.SteamOvenComViewHolder.ViewHolder holder = null;
                    if (convertView == null) {
                        //没缓存
                        convertView = mLayoutInflater.inflate(R.layout.oven_item_show, null);
                        holder = new SteamOvenCommonAdapter.SteamOvenComViewHolder.ViewHolder();
                        holder.iv = (ImageView) convertView.findViewById(R.id.iv_channel);
                        holder.tv = (TextView) convertView.findViewById(R.id.tv_channel);
                        convertView.setTag(holder);
                    } else {
                        //有缓存
                        holder = (SteamOvenCommonAdapter.SteamOvenComViewHolder.ViewHolder) convertView.getTag();
                    }
                    //绑定viewholder数据
                    holder.tv.setText(mainList.get(position).functionName);
                    Glide.with(cx).load(mainList.get(position).backgroundImg).into(holder.iv);
                } catch (Exception e) {
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
    class SteamOvenItemViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recycle;
        List<DeviceConfigurationFunctions> otherList;

        public SteamOvenItemViewHolder(View itemView) {
            super(itemView);
            recycle = (RecyclerView) itemView.findViewById(R.id.recycle);

        }

        private void setData(List<DeviceConfigurationFunctions> otherList) {
            this.otherList = otherList;
            recycle.setLayoutManager(new LinearLayoutManager(cx, LinearLayoutManager.VERTICAL, false));
            recycle.setAdapter(new SteamOvenCommonAdapter.SteamOvenItemViewHolder.ItemshowAdapter());
        }

        class ItemshowAdapter extends RecyclerView.Adapter<SteamOvenCommonAdapter.SteamOvenItemViewHolder.ItemshowAdapter.ItemViewHolder> {

            @Override
            public SteamOvenCommonAdapter.SteamOvenItemViewHolder.ItemshowAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new SteamOvenCommonAdapter.SteamOvenItemViewHolder.ItemshowAdapter.ItemViewHolder(mLayoutInflater.inflate(R.layout.item_otherfunc_page, null));
            }

            @Override
            public void onBindViewHolder(SteamOvenCommonAdapter.SteamOvenItemViewHolder.ItemshowAdapter.ItemViewHolder holder, final int position) {
                SteamOvenCommonAdapter.SteamOvenItemViewHolder.ItemshowAdapter.ItemViewHolder itemViewHolder = holder;
                Glide.with(cx).load(otherList.get(position).backgroundImg).into(itemViewHolder.mImageView);
                itemViewHolder.mTvName.setText(otherList.get(position).functionName);
                itemViewHolder.mTvDesc.setText(otherList.get(position).msg);
                holder.itemView.setTag(otherList.get(position).functionCode);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ToastUtils.show(""+position,Toast.LENGTH_SHORT);
                        if (itemViewOnclickLister != null) {
                            itemViewOnclickLister.onItemClick(otherList.get(position).functionCode);
                        }
                    }
                });
            }

            @Override
            public int getItemCount() {
                return otherList.size() < 0 ? 0 : otherList.size();
            }

            class ItemViewHolder extends RecyclerView.ViewHolder {
                ImageView mImageView;
                TextView mTvDesc;
                TextView mTvName;

                public ItemViewHolder(View itemView) {
                    super(itemView);
                    mTvName = (TextView) itemView.findViewById(R.id.tv_name);
                    mImageView = (ImageView) itemView.findViewById(R.id.iv_view);
                    mTvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
                }
            }
        }


    }

    public interface GridViewOnclick {
        void onGridClick(String pos);
    }

    public interface ItemViewOnclick {
        void onItemClick(String pos);
    }

    public SteamOvenCommonAdapter.GridViewOnclick gridViewOnclickLister;

    public SteamOvenCommonAdapter.ItemViewOnclick itemViewOnclickLister;

    public void setGridViewOnclickLister(SteamOvenCommonAdapter.GridViewOnclick gridViewOnclickLister) {
        this.gridViewOnclickLister = gridViewOnclickLister;
    }

    public void setItemViewOnclickLister(SteamOvenCommonAdapter.ItemViewOnclick itemViewOnclickLister) {
        this.itemViewOnclickLister = itemViewOnclickLister;
    }
}
