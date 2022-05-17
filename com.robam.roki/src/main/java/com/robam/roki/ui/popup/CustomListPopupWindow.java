//package com.robam.roki.ui.popup;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import android.content.Context;
//import android.graphics.drawable.BitmapDrawable;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.PopupWindow.OnDismissListener;
//import android.widget.TextView;
//
//import com.robam.common.pojos.RecipeTheme;
//import com.robam.roki.R;
//
///**
// * 自定义popuwindow
// * 2017-8-6 <br/>
// */
//public class CustomListPopupWindow extends PopupWindow implements OnDismissListener{
//
//    private ListView mListView;
//    private List<String> mTimeModels=new ArrayList<>();
//    private List<RecipeTheme> themeList=new ArrayList<>();
//    private ItemClickCallBack mItemCallBack;
//    private Context mContext;
//    private int checkedUId=-1;
//    private View rootView;
//    private DataAdapter mAdapter=null;
//
//    /**
//     *
//     * @param context
//     * @param mTimeModels 数据集
//     * @param checkedUId 被选中的item
//     * @param mItemCallBack item的点击事件
//     */
//    //public CustomListPopupWindow(Context context,List<String> mTimeModels,int checkedUId,ItemClickCallBack mItemCallBack){
//    //    super();
//    //    this.mContext=context;
//    //    this.mTimeModels=mTimeModels;
//    //    this.checkedUId=checkedUId;
//    //    this.mItemCallBack=mItemCallBack;
//    //    initPop();
//    //}
//
//
//    public CustomListPopupWindow(Context context, List<String> mTimeModels, List<RecipeTheme> themeList,int checkedUId, ItemClickCallBack mItemCallBack){
//        super();
//        this.mContext=context;
//        this.mTimeModels=mTimeModels;
//        this.themeList=themeList;
//        this.checkedUId=checkedUId;
//        this.mItemCallBack=mItemCallBack;
//        initPop();
//    }
//
//
//
//    private void initPop() {
//        rootView = LayoutInflater.from(mContext).inflate(R.layout.popup_content_layout, null);
//        mListView= rootView.findViewById(R.id.lv_popupWindow);
//        this.setContentView(rootView);
//        this.setWidth(LayoutParams.WRAP_CONTENT);
//        this.setHeight(LayoutParams.WRAP_CONTENT);
//        this.setFocusable(true);
//        this.setOnDismissListener(this);
//        this.setBackgroundDrawable(new BitmapDrawable());//不加这行代码后果很严重
////        this.setAnimationStyle(R.style.ActionSheetDialogAnimation);
//        this.setContentView(rootView);
//        this.mAdapter=new DataAdapter();
//        this.mListView.setAdapter(mAdapter);
//        this.mListView.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView parent, View view,
//                                    int position, long id) {
//                if (mItemCallBack != null) {
//                    String time=mTimeModels.get(position);
//                    Long themeId = themeList.get(position).getID();
//                    mItemCallBack.callBack(time,themeId);
//                }
//            }
//        });
//    }
//
//    private class DataAdapter extends BaseAdapter {
//
//        class ViewHolder {
//            TextView tv_name;
//        }
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//
//
//        @Override
//        public int getCount() {
//            if (mTimeModels != null) {
//                return mTimeModels.size();
//            }
//            return 0;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            if (mTimeModels != null) {
//                return mTimeModels.get(position);
//            }
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder viewHolder;
//            String time =mTimeModels.get(position);
//            if (convertView == null) {
//                viewHolder = new ViewHolder();
//                convertView = LayoutInflater.from(mContext).inflate(R.layout.popup_list_item, null);
//
//                viewHolder.tv_name= convertView.findViewById(R.id.tv_time_data);
//                convertView.setTag(viewHolder);
//            } else {
//                viewHolder = (ViewHolder) convertView.getTag();
//            }
//            //绑定数据
//            long mmTime = Long.parseLong(time);
//            String format = sdf.format(new Date(mmTime));
//            viewHolder.tv_name.setText("第"+format+"期");
//            return convertView;
//        }
//    }
//
//    //public interface ItemClickCallBack {
//    //    void callBack(String time);
//    //}
//
//    public interface ItemClickCallBack {
//        void callBack(String time,Long themeId);
//    }
//
//
//    private int dp2Px(Context context, float dp) {
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (dp * scale + 0.5f);
//    }
//
//    @Override
//    public void onDismiss() {
//        // TODO Auto-generated method stub
//
//    }
//
//}
