package com.robam.roki.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.legent.Callback;
import com.legent.ui.ext.HeadPage;
import com.legent.ui.ext.adapters.ExtBaseAdapter;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.MaterialFrequency;
import com.robam.common.services.CookbookManager;
import com.robam.roki.R;
import com.robam.roki.ui.view.RecipeSeasoningFrequencyView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sylar on 15/6/14.
 */
public class RecipeSeasoningPage extends HeadPage {

    @InjectView(R.id.listview)
    ListView listview;

    Adapter adapter;

    @Override
    protected View onCreateContentView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.page_recipe_seasoning, viewGroup, false);
        ButterKnife.inject(this, view);
        adapter = new Adapter();
        listview.setAdapter(adapter);
        initData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    void initData() {
        ProgressDialogHelper.setRunning(cx, true);
        CookbookManager.getInstance().getAccessoryFrequencyForMob(new Callback<List<MaterialFrequency>>() {
            @Override
            public void onSuccess(List<MaterialFrequency> list) {
                ProgressDialogHelper.setRunning(cx, false);
                adapter.loadData(list);
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.showThrowable(t);
            }
        });
    }

    class Adapter extends ExtBaseAdapter<MaterialFrequency> {

        Ordering<MaterialFrequency> ordering = Ordering.natural().nullsFirst().onResultOf(new Function<MaterialFrequency, Integer>() {
            @Override
            public Integer apply(MaterialFrequency materialFrequency) {
                return materialFrequency.frequency;
            }
        }).reverse();

        @Override
        public void loadData(List<MaterialFrequency> list) {
            list = ordering.sortedCopy(list);
            super.loadData(list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                convertView = LayoutInflater.from(cx).inflate(R.layout.view_recipe_seasoning_item, parent, false);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            MaterialFrequency mf = getEntity(position);
            vh.showData(mf, position == 0);

            return convertView;
        }

        class ViewHolder {
            @InjectView(R.id.edtName)
            TextView txtName;
            @InjectView(R.id.txtHot)
            TextView txtHot;
            @InjectView(R.id.frequencyView)
            RecipeSeasoningFrequencyView frequencyView;

            ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }

            void showData(MaterialFrequency mf, boolean isHot) {
                txtName.setText(mf.name);
                frequencyView.setValue(mf.frequency);
                txtHot.setVisibility(isHot ? View.VISIBLE : View.GONE);
            }
        }
    }
}
