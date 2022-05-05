package com.robam.roki.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.ext.views.CheckBoxView;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.FanStatusComposite;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RuanWei on 2019/9/26.
 */

public class FanGestureControlAdapter extends RecyclerView.Adapter<FanGestureControlViewHolder> implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private LayoutInflater mInfalter;
    private Context mContext;
    private List<DeviceConfigurationFunctions> data;
    private OnItemClickListener mClickListener;
    private ItemCheckListener onItemChildCheckListener;
    boolean isChecked;
    AbsFan fan;

    FanStatusComposite fanStatusComposite = new FanStatusComposite();

    @Override
    public void onClick(View v) {
        if (mClickListener != null) {
            mClickListener.onItemClick(v, (int) v.getTag());
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (onItemChildCheckListener != null) {
            onItemChildCheckListener.onItemCheck(buttonView, isChecked);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int postion);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }


    public interface ItemCheckListener {
        void onItemCheck(CompoundButton buttonView, boolean isChecked);
    }

    public void setOnItemChildCheckListener(ItemCheckListener itemChildCheckListener) {
        this.onItemChildCheckListener = itemChildCheckListener;

    }


    public FanGestureControlAdapter(Context mContext, List<DeviceConfigurationFunctions> data, boolean isChecked, AbsFan fan) {
        mInfalter = LayoutInflater.from(mContext);
        this.data = data;
        this.isChecked = isChecked;
        this.fan = fan;
    }

    @Override
    public FanGestureControlViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInfalter.inflate(R.layout.item_fan_gesture_control, parent, false);
        FanGestureControlViewHolder fanGestureControlViewHolder = new FanGestureControlViewHolder(view);
        return fanGestureControlViewHolder;
    }

    @Override
    public void onBindViewHolder(final FanGestureControlViewHolder holder, final int position) {
        holder.tvGestureTitle.setText(data.get(position).functionName);
        if ("gestureOff".equals(data.get(position).functionCode)) {
            holder.cbCheck.setChecked(false);
        } else if ("gestureOn".equals(data.get(position).functionCode)) {
            holder.cbCheck.setChecked(true);
        }
        try {
            String functionParams = data.get(position).functionParams;
            JSONObject jsonObject = new JSONObject(functionParams);
            JSONObject param = jsonObject.getJSONObject("param");
            String title = param.getJSONObject("title").getString("value");
            String tips = param.getJSONObject("tips").getString("value");
            String desc = param.getJSONObject("desc").getString("value");

            holder.tvGestureTitle.setText(title);
            holder.tvGestureMsg.setText(tips);
            holder.tvGestureDetail.setText(desc);
            holder.cbCheck.setChecked(isChecked);

            holder.cbCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    fanStatusComposite.gestureControlSwitch = (short) (holder.cbCheck.isChecked() ? 1 : 0);
                    List<Integer> listKey = new ArrayList<>();
                    listKey.add(13);
                    fan.setFanCombo(fanStatusComposite, (short) 1, listKey, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            LogUtils.i("201909309999", "---成功");
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            LogUtils.i("201909309999", t.getMessage());
                        }

                    });


                }
            });


//                    List<Integer> listKey = new ArrayList<>();
//                    listKey.add(13);
//                    fan.setFanCombo(fanStatusComposite, (short) 1, listKey, new VoidCallback() {
//                        @Override
//                        public void onSuccess() {
//
//                        }
//
//                        @Override
//                        public void onFailure(Throwable t) {
//
//                        }
//
//                    });
//
//
//                }


//            holder.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    onItemChildCheckListener.onItemCheck(buttonView, isChecked);
////                    FanStatusComposite fanStatusComposite = new FanStatusComposite();
////                    fanStatusComposite.gestureControlSwitch = 1;
////                    fan.setGestureControl(fanStatusComposite, new VoidCallback() {
////                        @Override
////                        public void onSuccess() {
////                            LogUtils.i("2019092611111111", "成功");
////                        }
////
////                        @Override
////                        public void onFailure(Throwable t) {
////                            LogUtils.i("2019092611111111", t.getMessage());
////                        }
////                    });
//
//
//
//
//                }
//            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return data.size() > 0 ? data.size() : 0;
    }


}

class FanGestureControlViewHolder extends RecyclerView.ViewHolder {
    TextView tvGestureTitle;
    CheckBoxView cbCheck;
    TextView tvGestureMsg;
    TextView tvGestureDetail;


    public FanGestureControlViewHolder(View itemView) {
        super(itemView);
        tvGestureTitle = itemView.findViewById(R.id.tv_gesture_title);
        cbCheck = itemView.findViewById(R.id.cb_check);
        tvGestureMsg = itemView.findViewById(R.id.tv_gesture_msg);
        tvGestureDetail = itemView.findViewById(R.id.tv_gesture_detail);

    }


}
