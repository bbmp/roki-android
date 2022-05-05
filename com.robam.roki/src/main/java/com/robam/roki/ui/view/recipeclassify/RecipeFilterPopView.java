package com.robam.roki.ui.view.recipeclassify;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import com.google.common.collect.Lists;
import com.legent.plat.Plat;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.robam.common.events.RecipeFilterEvent;
import com.robam.roki.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/8.
 */

public class RecipeFilterPopView extends FrameLayout {
    Context cx;
    RecipeFilterPopWindow pop;

    @InjectView(R.id.origincontent)
    GridFilterView origincontent;

 /*   @InjectView(R.id.electrickitchencontent)
    GridFilterView electrickitchencontent;

    @InjectView(R.id.flavorcontent)
    GridFilterView flavorcontent;*/

    List<RecipeFilterItemView> sourceSet = new ArrayList<RecipeFilterItemView>();
    List<RecipeFilterItemView> kitchenSet = new ArrayList<RecipeFilterItemView>();
    List<RecipeFilterItemView> deliciousSet = new ArrayList<RecipeFilterItemView>();
    static Set<String> tempSet = new HashSet();
    List<Integer> temp = new ArrayList<>();

    public RecipeFilterPopView(Context context, RecipeFilterPopWindow pop, Map sourceData, Map electricKitchenData, Map flavorData) {
        super(context);
        init(context, pop, null, sourceData, electricKitchenData, flavorData);
    }

    public RecipeFilterPopView(Context context, RecipeFilterPopWindow pop, AttributeSet attrs, Map sourceData, Map electricKitchenData, Map flavorData) {
        super(context, attrs);
        init(context, pop, attrs, sourceData, electricKitchenData, flavorData);
    }

    public RecipeFilterPopView(Context context, RecipeFilterPopWindow pop, AttributeSet attrs, int defStyle, Map sourceData, Map electricKitchenData, Map flavorData) {
        super(context, attrs, defStyle);
        init(context, pop, attrs, sourceData, electricKitchenData, flavorData);
    }

    public void init(Context context, RecipeFilterPopWindow pop, AttributeSet attrs, Map sourceData, Map electricKitchenData, Map flavorData) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_filter_pop,
                this, true);
        cx = context;
        this.pop = pop;
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
            sourceSet.clear();
            RecipeFilterPopView.RecipeFilterDataAdapter adapter = new RecipeFilterPopView.RecipeFilterDataAdapter();
            origincontent.setAdapter(adapter);
            adapter.appendData(sourceData, (short) 1);
           // electrickitchencontent.setAdapter(new RecipeFilterDataAdapter(electricKitchenData, (short) 2));
           // flavorcontent.setAdapter(new RecipeFilterDataAdapter(flavorData, (short) 3));
        }
    }

    class RecipeFilterDataAdapter extends BaseAdapter {
        Map<Integer, String> fillterData = new HashMap<Integer, String>();
        List<Integer> listId = Lists.newArrayList();
        short type;

        public void appendData(Map fillterData, short type) {
            this.fillterData = fillterData;
            for (Object in : fillterData.keySet()) {
                String s = (String) fillterData.get(in);
                // LogUtils.i("20170423", "s::" + s.toString());
            }
            Set<Integer> set = fillterData.keySet();
            listId = new ArrayList<Integer>(set);
            for (int i = 0; i < listId.size(); i++) {
                LogUtils.i("20170423", "listId:" + listId.get(i) + "listsize:" + listId.size());
            }
            this.type = type;
            if (Plat.DEBUG) {
                LogUtils.i("20170420", "listId:" + listId.size());
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            try {
                LogUtils.var(fillterData.size() + " :print");
            } catch (Exception e) {
                if (e != null)
                    LogUtils.var(e.getMessage());
            }
            return fillterData.size();
        }
        /*else if (type == 2) {//chudian
                kitchenSet.add(recipeFilterItemView);
            } else if (type == 3) {//kouwei
                deliciousSet.add(recipeFilterItemView);
            }*/
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (listId == null || listId.size() <= 0)
                return new View(cx);


         /*   RecipeFilterItemView recipeFilterItemView = new RecipeFilterItemView(cx, fillterData.get(listId.get(position)), tempSet);
            recipeFilterItemView.setTag(listId.get(position));
            if (type == 1) {//laiyuan
                sourceSet.add(recipeFilterItemView);
            }
            return recipeFilterItemView;
*/
            // LogUtils.i("20170505","par:"+parent.getChildCount()+"pos:"+position);
        return  null;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {

            return fillterData.get(position);
        }
    }

    //重置
    @OnClick(R.id.btnReset)
    public void onclicReset() {
        if (sourceSet.size() > 0) {
            for (int i = 0; i < sourceSet.size(); i++) {
                sourceSet.get(i).restoreFilterItemView();
            }
            tempSet.clear();
        }
        if (kitchenSet.size() > 0) {
            for (int i = 0; i < kitchenSet.size(); i++) {
                kitchenSet.get(i).restoreFilterItemView();
            }
            tempSet.clear();
        }
        if (deliciousSet.size() > 0) {
            for (int i = 0; i < deliciousSet.size(); i++) {
                deliciousSet.get(i).restoreFilterItemView();
            }
            tempSet.clear();
        }

    }

    //确定
    @OnClick(R.id.btnConfirm)
    public void onclicConfirm() {
        tempSet.clear();
        String tag1 = null;
        RecipeFilterEvent recipeFilterEvent = new RecipeFilterEvent();
        if (sourceSet.size() > 0) {
            for (int i = 0; i < sourceSet.size(); i++) {
                LogUtils.i("20170508","sourceSet::"+sourceSet.get(i).getFilterValue());
            }
            List<Integer> list = Lists.newArrayList();
            for (int i = 0; i < sourceSet.size(); i++) {
                //  LogUtils.i("20170420","sourceSet.....:"+sourceSet.get(i).isClickble+"  sourceset:"+sourceSet.get(i).getTag());
                if (sourceSet.get(i).isClickble == true) {
                    Integer tag = 0;
                    try {
                        tag = (Integer) sourceSet.get(i).getTag();
                        tag1 = sourceSet.get(i).getFilterValue();
                        tempSet.add(tag1);
                        LogUtils.i("2017042", "temset:" + tag1);
                    } catch (Exception e) {
                    } finally {
                        list.add(tag);
                    }
                }
            }
            recipeFilterEvent.setSource(list);
        }
        if (kitchenSet.size() > 0) {
            if (Plat.DEBUG) {
                LogUtils.i("20170420", "sourceSet:" + kitchenSet.size());
            }
            List<Integer> list = Lists.newArrayList();
            for (int i = 0; i < kitchenSet.size(); i++) {
                if (kitchenSet.get(i).isClickble == true) {
                    Integer tag = 0;
                    try {
                        tag = (Integer) kitchenSet.get(i).getTag();
                        temp.add(tag);
                        tag1 = kitchenSet.get(i).getFilterValue();
                        tempSet.add(tag1);
                    } catch (Exception e) {
                    } finally {
                        list.add(tag);
                    }
                }
            }
            recipeFilterEvent.setKitchen(list);
        }
        if (deliciousSet.size() > 0) {
            if (Plat.DEBUG) {
                LogUtils.i("20170420", "sourceSet:" + deliciousSet.size());
            }
            List<Integer> list = Lists.newArrayList();
            for (int i = 0; i < deliciousSet.size(); i++) {
                if (deliciousSet.get(i).isClickble == true) {
                    Integer tag = 0;
                    try {
                        tag = (Integer) deliciousSet.get(i).getTag();
                        temp.add(tag);
                        tag1 = deliciousSet.get(i).getFilterValue();
                        tempSet.add(tag1);
                    } catch (Exception e) {
                    } finally {
                        list.add(tag);
                    }
                }
            }
            recipeFilterEvent.setDelicious(list);
        }
        EventUtils.postEvent(recipeFilterEvent);
        pop.dismiss();
    }


}
