package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.ext.adapters.ExtBaseAdapter;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.Material;
import com.robam.common.services.CookbookManager;
import com.robam.roki.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TrolleyMaterialView extends FrameLayout {

    @InjectView(R.id.listview)
    ListView listView;

    Context cx;
    Adapter adapter;

    public TrolleyMaterialView(Context context) {
        super(context);
        init(context, null);
    }

    public TrolleyMaterialView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TrolleyMaterialView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {
        this.cx = cx;
        View view = LayoutInflater.from(cx).inflate(R.layout.view_trolley_material,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);

            adapter = new Adapter();
            listView.setAdapter(adapter);
        }
    }

    public void loadData(List<Material> materials) {
        adapter.loadData(materials);
    }

    class Adapter extends ExtBaseAdapter<Material> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder vh = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(cx).inflate(R.layout.view_trolley_material_item, parent, false);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            Material material = list.get(position);
            vh.showData(material);
            return convertView;
        }


        class ViewHolder {
            @InjectView(R.id.imgFlag)
            ImageView imgFlag;
            @InjectView(R.id.txtDesc)
            TextView txtDesc;
            @InjectView(R.id.strickoutView)
            View strickoutView;

            Material material;

            ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }

            public void showData(Material material) {
                this.material = material;
                txtDesc.setText(getMaterialString(material));
                txtDesc.setSelected(material.isRemove);
                strickoutView.setVisibility(material.isRemove ? VISIBLE : INVISIBLE);
                imgFlag.setVisibility(material.isRemove ? INVISIBLE : VISIBLE);
            }


            @OnClick(R.id.layout)
            public void onClick() {
                if (material == null) return;
                if (!material.isRemove) {
                    CookbookManager.getInstance().deleteMaterialsFromToday(material.serviceId, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            material.isRemove = !material.isRemove;
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            ToastUtils.showThrowable(t);
                        }
                    });
                } else {
                    CookbookManager.getInstance().addMaterialsToToday(material.serviceId, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            material.isRemove = !material.isRemove;
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            ToastUtils.showThrowable(t);
                        }
                    });

                }

            }

        }
    }

    static String getMaterialString(Material material) {
        StringBuilder sb = new StringBuilder();
        sb.append(material.name);
        if (!Strings.isNullOrEmpty(material.popularWeight) && !"0".equals(material.popularWeight) && !Strings.isNullOrEmpty(material.popularUnit)) {
            sb.append(material.popularWeight + "").append(material.popularUnit);
        }
        if (!Strings.isNullOrEmpty(material.standardWeight) && !"0".equals(material.standardWeight) && !Strings.isNullOrEmpty(material.standardUnit)) {
            sb.append("(").append(material.standardWeight + "").append(material.standardUnit).append(")");
        }
        return sb.toString();
    }
}
