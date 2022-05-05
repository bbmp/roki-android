package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.ui.ext.adapters.ExtBaseAdapter;
import com.legent.ui.ext.views.NestedListView;
import com.legent.utils.graphic.ImageUtils;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.PreSubStep;
import com.robam.common.pojos.Recipe;
import com.robam.roki.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RecipeDetailCookstepView extends FrameLayout {

    @InjectView(R.id.lvSteps)
    NestedListView lvSteps;

    Adapter adapter;
    Recipe book;

    public RecipeDetailCookstepView(Context context) {
        super(context);
        init(context, null);
    }

    public RecipeDetailCookstepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecipeDetailCookstepView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_recipe_detail_cookstep,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
            adapter = new Adapter();
            lvSteps.setAdapter(adapter);
        }
    }

    public void loadData(Recipe book) {
        this.book = book;
        if (book == null) return;

        adapter.loadData(book.getJs_cookSteps());
    }

    class Adapter extends ExtBaseAdapter<CookStep> {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                View view = LayoutInflater.from(getContext()).inflate(
                        R.layout.view_recipe_detail_cookstep_item, parent,
                        false);
                vh = new ViewHolder(view);
                convertView = view;
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            CookStep step = list.get(position);
            PreSubStep preStep = getPreSubstep(position);

            vh.showData(step, preStep != null ? preStep.imageUrl : step.imageUrl);

            return convertView;
        }

        PreSubStep getPreSubstep(int index) {
            if (book.preStep == null) return null;
            List<PreSubStep> preSteps = book.preStep.getPreSubSteps();
            if (preSteps != null) {
                if (index < 0 || index >= preSteps.size())
                    return null;
                else
                    return preSteps.get(index);
            } else {
                return null;
            }
        }

        class ViewHolder {

            @InjectView(R.id.imgStep)
            ImageView imgStep;

            @InjectView(R.id.txtStepDesc)
            TextView txtStepDesc;

            @InjectView(R.id.txtStepIndex)
            TextView txtStepIndex;

            @InjectView(R.id.txtStepCount)
            TextView txtStepCount;

            public ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }

            void showData(CookStep step, String imgUrl) {
                imgStep.setImageDrawable(null);
                ImageUtils.displayImage(getContext(), imgUrl, imgStep);

                txtStepDesc.setText(step.desc);
                txtStepIndex.setText(String.valueOf(step.order));
                txtStepCount.setText(String.format("/%s.", list.size()));
            }
        }
    }


}
