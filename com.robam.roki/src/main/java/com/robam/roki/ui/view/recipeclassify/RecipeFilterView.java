package com.robam.roki.ui.view.recipeclassify;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import com.google.common.collect.Lists;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.robam.common.events.RecipeFilterEvent;
import com.robam.roki.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by zhoudingjun on 2016/12/30.
 */

public class RecipeFilterView extends FrameLayout {


    Context cx;
    RecipeFilterPopWindow pop;

    @InjectView(R.id.origincontent)
    GridFilterView origincontent;

    @InjectView(R.id.electrickitchencontent)
    GridFilterView electrickitchencontent;

    @InjectView(R.id.flavorcontent)
    GridFilterView flavorcontent;

    List<RecipeFilterItemView> sourceSet = new ArrayList<RecipeFilterItemView>();
    List<RecipeFilterItemView> kitchenSet = new ArrayList<RecipeFilterItemView>();
    List<RecipeFilterItemView> deliciousSet = new ArrayList<RecipeFilterItemView>();
   static HashMap<String,Boolean> tempSet = new HashMap<String,Boolean>();

    Map<Integer, String> source = new HashMap<Integer, String>();
    Map<Integer, String> electricKitchen = new HashMap<Integer, String>();
    Map<Integer, String> flavors = new HashMap<Integer, String>();

    public RecipeFilterView(Context context, RecipeFilterPopWindow pop, Map sourceData, Map electricKitchenData, Map flavorData) {
        super(context);
        init(context, pop, null, sourceData, electricKitchenData, flavorData);
    }

    public RecipeFilterView(Context context, RecipeFilterPopWindow pop, AttributeSet attrs, Map sourceData, Map electricKitchenData, Map flavorData) {
        super(context, attrs);
        init(context, pop, attrs, sourceData, electricKitchenData, flavorData);
    }

    public RecipeFilterView(Context context, RecipeFilterPopWindow pop, AttributeSet attrs, int defStyle, Map sourceData, Map electricKitchenData, Map flavorData) {
        super(context, attrs, defStyle);
        init(context, pop, attrs, sourceData, electricKitchenData, flavorData);
    }

    public void init(Context context, RecipeFilterPopWindow pop, AttributeSet attrs, Map sourceData, Map electricKitchenData, Map flavorData) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_filter_layout,
                this, true);
        cx = context;
        this.pop = pop;
        this.source=sourceData;
        this.electricKitchen=electricKitchenData;
        this.flavors=flavorData;
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
            origincontent.setAdapter(new RecipeFilterDataAdapter(sourceData, (short) 1));
            electrickitchencontent.setAdapter(new RecipeFilterDataAdapter(electricKitchenData, (short) 2));
            flavorcontent.setAdapter(new RecipeFilterDataAdapter(flavorData, (short) 3));
        }

    }

    class RecipeFilterDataAdapter extends BaseAdapter {
        Map<Integer, String> fillterData = new HashMap<Integer, String>();
        List<Integer> listId = Lists.newArrayList();
        short type;

        RecipeFilterDataAdapter(Map fillterData, short type) {
            this.fillterData = fillterData;
            Set<Integer> set = fillterData.keySet();
            listId = new ArrayList<Integer>(set);
            this.type = type;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (listId == null || listId.size() <= 0)
                return new View(cx);
            RecipeFilterItemView recipeFilterItemView = new RecipeFilterItemView(cx, fillterData.get(listId.get(position)),tempSet);
            recipeFilterItemView.setTag(listId.get(position));
            if (type == 1) {//laiyuan
                sourceSet.add(recipeFilterItemView);
            } else if (type == 2) {//chudian
                kitchenSet.add(recipeFilterItemView);
            } else if (type == 3) {//kouwei
                deliciousSet.add(recipeFilterItemView);
            }
            return recipeFilterItemView;
        }

        @Override
        public int getCount() {
            return listId.size();
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
        RecipeFilterEvent recipeFilterEvent = new RecipeFilterEvent();
        tempSet.clear();
        LogUtils.i("20170508","tempSet:"+tempSet.size());
        String tagselect=null;
        List<RecipeFilterItemView> tempSourceSet=new ArrayList<RecipeFilterItemView>();
        List<RecipeFilterItemView> tempkitchenSet=new ArrayList<RecipeFilterItemView>();
        List<RecipeFilterItemView> tempdeliciousSet=new ArrayList<RecipeFilterItemView>();
        if (sourceSet.size() > 0) {
            List<Integer> list = Lists.newArrayList();
            for (int j = 0; j < sourceSet.size(); j++) {
                if (!sourceSet.get(j+1).getFilterValue().equals(sourceSet.get(j).getFilterValue())){
                   tempSourceSet=sourceSet.subList(j,j+source.size());
                    break;
                }
            }

            for (int i=0; i < tempSourceSet.size(); i++) {
                   if (tempSourceSet.get(i).isClickble == true) {
                       Integer tag = 0;
                       try {
                           tag = (Integer)tempSourceSet.get(i).getTag();
                           tagselect=tempSourceSet.get(i).getFilterValue();
                           tempSet.put(tagselect,tempSourceSet.get(i).isClickble);
                       } catch (Exception e) {
                       } finally {
                           list.add(tag);
                       }
                   }
            }
            recipeFilterEvent.setSource(list);
        }




        if (kitchenSet.size() > 0) {
            List<Integer> list = Lists.newArrayList();
            for (int i = 0; i < kitchenSet.size(); i++) {
                if (!kitchenSet.get(i+1).getFilterValue().equals(kitchenSet.get(i).getFilterValue())){
                    tempkitchenSet=kitchenSet.subList(i,i+electricKitchen.size());
                    break;
                }
            }
            for (int i = 0; i < tempkitchenSet.size(); i++) {
                if (tempkitchenSet.get(i).isClickble == true) {
                    Integer tag = 0;
                    try {
                        tag = (Integer) tempkitchenSet.get(i).getTag();
                        tagselect=tempkitchenSet.get(i).getFilterValue();
                        tempSet.put(tagselect,tempkitchenSet.get(i).isClickble);
                    } catch (Exception e) {
                    } finally {
                        list.add(tag);
                    }
                }
            }
            recipeFilterEvent.setKitchen(list);
        }
        if (deliciousSet.size() > 0) {
            List<Integer> list = Lists.newArrayList();
            for (int i = 0; i <deliciousSet.size() ; i++) {
                if (!deliciousSet.get(i).getFilterValue().equals(deliciousSet.get(i+1).getFilterValue())){
                    tempdeliciousSet=deliciousSet.subList(i,i+flavors.size());
                    break;
                }
            }
            for (int i = 0; i < tempdeliciousSet.size(); i++) {
                if (tempdeliciousSet.get(i).isClickble == true) {
                    Integer tag = 0;
                    try {
                        tag = (Integer) tempdeliciousSet.get(i).getTag();
                        tagselect=tempdeliciousSet.get(i).getFilterValue();
                        tempSet.put(tagselect,tempdeliciousSet.get(i).isClickble);
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
