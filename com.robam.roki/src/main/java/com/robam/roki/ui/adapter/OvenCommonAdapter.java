package com.robam.roki.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.roki.R;
import com.robam.roki.ui.page.device.oven.MyGridView;


import java.util.List;

/**
 * Created by Dell on 2018/7/5.
 */

public class OvenCommonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 0位置的数据
     */
    public static final int GridSHOW = 0;

    public static final int GridMORE = 1;

    public static final int ITEMSHOW = 2;


    List<DeviceConfigurationFunctions> mainList;
    List<DeviceConfigurationFunctions> otherList;
    AbsDishWasher washer;
    private LayoutInflater mLayoutInflater;

    Context cx;

    private int currentType = GridSHOW; //当前类型

    private String deviceType = "";
    private AbsOven oven;

    public OvenCommonAdapter(Context context, List<DeviceConfigurationFunctions> mainList, List<DeviceConfigurationFunctions> otherList) {
        this.cx = context;
        this.mainList = mainList;
        this.otherList = otherList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public OvenCommonAdapter(Context context, List<DeviceConfigurationFunctions> mainList, List<DeviceConfigurationFunctions> otherList, String deviceType) {
        this.cx = context;
        this.mainList = mainList;
        this.otherList = otherList;
        this.deviceType = deviceType;
        this.mLayoutInflater = LayoutInflater.from(context);
    }
    public OvenCommonAdapter(Context context, List<DeviceConfigurationFunctions> mainList, List<DeviceConfigurationFunctions> otherList, AbsOven oven) {
        this.cx = context;
        this.mainList = mainList;
        this.otherList = otherList;
        this.deviceType = deviceType;
        this.oven = oven;
        this.mLayoutInflater = LayoutInflater.from(context);
    }



    public OvenCommonAdapter(Context context, List<DeviceConfigurationFunctions> mainList, List<DeviceConfigurationFunctions> otherList, AbsDishWasher washer) {
        this.cx = context;
        this.mainList = mainList;
        this.otherList = otherList;
        this.washer = washer;
        this.mLayoutInflater = LayoutInflater.from(context);
        EventUtils.regist(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == GridSHOW) {
            if ("XS855".equals(deviceType)) {
                return new OvenComViewHolder(mLayoutInflater.inflate(R.layout.oven_grid_mode_xs855, null));
            } else {

                if (oven!=null) {
                    if (oven.getDc().equals("RDKX")&&mainList.size()==3) {
                        return new OvenComViewHolder(mLayoutInflater.inflate(R.layout.oven_grid_mode_2, null));
                    }else{
                        return new OvenComViewHolder(mLayoutInflater.inflate(R.layout.oven_grid_mode, null));
                    }

                }else{
                    return new OvenComViewHolder(mLayoutInflater.inflate(R.layout.oven_grid_mode, null));
                }

            }
        } else if (viewType == GridMORE) {
            return new OvenMoreViewHolder(mLayoutInflater.inflate(R.layout.oven_more_mode_show, null));
        } else if (viewType == ITEMSHOW) {//item_otherfunc_page
            return new OvenItemViewHolder(mLayoutInflater.inflate(R.layout.oven_recycleview_show, null));
        }
        return null;
    }

    OvenMoreViewHolder moreViewHolder;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == GridSHOW) {
            OvenComViewHolder ovenComViewHolder = (OvenComViewHolder) holder;
            ovenComViewHolder.setData(mainList);
        } else if (getItemViewType(position) == GridMORE) {
            moreViewHolder = (OvenMoreViewHolder) holder;
        } else if (getItemViewType(position) == ITEMSHOW) {
            OvenItemViewHolder ovenItemViewHolder = (OvenItemViewHolder) holder;
            ovenItemViewHolder.setData(otherList);
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
    }

    boolean isCon = false;

    public void setDisCon(boolean isCon) {
        this.isCon = isCon;
    }

    //关闭更多列表
    public void removeMoreView() {
        moreViewHolder.setRemoveView();
        this.notifyDataSetChanged();
    }

    //更多列表
    class OvenMoreViewHolder extends RecyclerView.ViewHolder {
        MyGridView myGridView;
        private List<DeviceConfigurationFunctions> moreL;

        public OvenMoreViewHolder(View itemView) {
            super(itemView);
            myGridView = itemView.findViewById(R.id.gv_show);
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
        }
    }


    //前三的菜布局
    class OvenComViewHolder extends RecyclerView.ViewHolder {
        GridView gv;
        ImageView moreTup;
        ImageView moreTdown;
        LinearLayout moreV;
        boolean falg = true;
        private List<DeviceConfigurationFunctions> mainList;

        public OvenComViewHolder(View itemView) {
            super(itemView);
            gv = itemView.findViewById(R.id.gv_show);
            moreTup = itemView.findViewById(R.id.v_more_up);
            moreTdown = itemView.findViewById(R.id.v_more_down);
            moreV = itemView.findViewById(R.id.more_v);
        }

        private void setData(final List<DeviceConfigurationFunctions> mainList) {
            this.mainList = mainList;
            if (mainList.size() == 0) {
                moreV.setVisibility(View.GONE);
                return;
            }
            MyGridViewAdapter myGridViewAdapter = new MyGridViewAdapter();
            gv.setAdapter(myGridViewAdapter);

            if (!"more".equals(mainList.get(mainList.size() - 1).functionCode)) {
                moreV.setVisibility(View.GONE);
            }
            moreV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (washer != null) {
                        if (washer.StoveLock == 1) {
                            ToastUtils.show("请先到机器端解除童锁", Toast.LENGTH_LONG);
                            return;
                        }
                    }
                    if (!isCon) {
                        if (falg) {
                            moreTdown.setVisibility(View.INVISIBLE);
                            moreTup.setVisibility(View.VISIBLE);
                            falg = false;
                        } else {
                            moreTdown.setVisibility(View.VISIBLE);
                            moreTup.setVisibility(View.INVISIBLE);
                            falg = true;
                        }
                    }
                    if (gridViewOnclickLister != null) {
                        gridViewOnclickLister.onGridClick("more");
                    }


                }
            });
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                return i;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup viewGroup) {
                try {
                    ViewHolderI holder = null;
                    if (convertView == null) {
                        //没缓存
                        convertView = mLayoutInflater.inflate(R.layout.oven_item_show, null);

                        holder = new ViewHolderI();
                        holder.iv = convertView.findViewById(R.id.iv_channel);
                        holder.tv = convertView.findViewById(R.id.tv_channel);
                        convertView.setTag(holder);
                    } else {
                        //有缓存
                        holder = (ViewHolderI) convertView.getTag();
                    }
                    //绑定viewholder数据

                    String functionName = mainList.get(position).functionName;
                    if (functionName.contains("n")) {
                        String[] split = functionName.split("n");
                        String frontText;
                        String afterText = null;
                        if (0 < split.length) {
                            String front = split[0];
                            frontText = front.substring(0, front.length() - 1);
                            if (1 < split.length) {
                                afterText = split[1];
                            }
                            String name = frontText + "\n" + afterText;
                            holder.tv.setText(name);
                        }
                    } else {
                        holder.tv.setText(functionName);
                    }
                    if (TextUtils.equals(functionName, "更多")) {
                        holder.iv.setVisibility(View.GONE);
                        holder.tv.setVisibility(View.GONE);
                    } else {
                        holder.iv.setVisibility(View.VISIBLE);
                        holder.tv.setVisibility(View.VISIBLE);
                    }
                    Glide.with(cx).load(mainList.get(position).backgroundImg).into(holder.iv);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return convertView;
            }
        }

        class ViewHolderI {
            ImageView iv;
            TextView tv;

            public ViewHolderI() {

            }
        }
    }


    //烤模式等列表
    class OvenItemViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recycle;
        List<DeviceConfigurationFunctions> otherList;

        public OvenItemViewHolder(View itemView) {
            super(itemView);
            recycle = itemView.findViewById(R.id.recycle);
        }

        private void setData(List<DeviceConfigurationFunctions> otherList) {
            this.otherList = otherList;
            for (int i = 0; i < otherList.size(); i++) {
                LogUtils.i("20190314", "otherList:::" + otherList.get(i).toString());
            }
            recycle.setLayoutManager(new LinearLayoutManager(cx, LinearLayoutManager.VERTICAL, false));
            recycle.setAdapter(new ItemshowAdapter());
        }

        class ItemshowAdapter extends RecyclerView.Adapter<ItemshowAdapter.ItemViewHolder> {


            @Override
            public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = mLayoutInflater.inflate(R.layout.item_otherfunc_page, null);

                return new ItemViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ItemViewHolder holder, final int position) {
                ItemViewHolder itemViewHolder = holder;
                Glide.with(cx).load(otherList.get(position).backgroundImg).into(itemViewHolder.mImageView);
                itemViewHolder.mTvName.setText(otherList.get(position).functionName);
                itemViewHolder.mTvDesc.setText(otherList.get(position).msg);
                holder.itemView.setTag(otherList.get(position).functionCode);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                    mTvName = itemView.findViewById(R.id.tv_name);
                    mImageView = itemView.findViewById(R.id.iv_view);
                    mTvDesc = itemView.findViewById(R.id.tv_desc);
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

    public GridViewOnclick gridViewOnclickLister;

    public ItemViewOnclick itemViewOnclickLister;

    public void setGridViewOnclickLister(GridViewOnclick gridViewOnclickLister) {
        this.gridViewOnclickLister = gridViewOnclickLister;
    }

    public void setItemViewOnclickLister(ItemViewOnclick itemViewOnclickLister) {
        this.itemViewOnclickLister = itemViewOnclickLister;
    }


}
