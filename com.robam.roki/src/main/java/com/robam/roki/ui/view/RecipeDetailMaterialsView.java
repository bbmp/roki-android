package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.robam.common.pojos.Material;
import com.robam.common.pojos.Recipe;
import com.robam.roki.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RecipeDetailMaterialsView extends FrameLayout {

    @InjectView(R.id.tableMain)
    TableLayout tableMain;
    @InjectView(R.id.tableSlave)
    TableLayout tableSlave;

    Recipe book;

    public RecipeDetailMaterialsView(Context context) {
        super(context);
        init(context, null);
    }

    public RecipeDetailMaterialsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecipeDetailMaterialsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_recipe_detail_materials,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
    }

    public void loadData(Recipe book) {
        this.book = book;
        if (book == null || book.materials == null) return;

        Context cx = getContext();

        View view;
        ViewHolder vh;
        List<Material> main = book.materials.getMain();
        if (main != null) {
            for (Material material : main) {
                view = LayoutInflater.from(cx).inflate(R.layout.view_recipe_detail_materilas_item, null, false);
                vh = new ViewHolder(view, material);
                view.setTag(vh);

                tableMain.addView(view);
            }
        }
        List<Material> slave = book.materials.getAccessory();
        if (slave != null) {
            for (Material material : slave) {
                view = LayoutInflater.from(cx).inflate(R.layout.view_recipe_detail_materilas_item, null, false);
                vh = new ViewHolder(view, material);
                view.setTag(vh);

                tableSlave.addView(view);
            }
        }
    }


    class ViewHolder {
        @InjectView(R.id.edtName)
        TextView txtName;
        @InjectView(R.id.txtValue)
        TextView txtValue;

        ViewHolder(View view, Material material) {
            ButterKnife.inject(this, view);
            txtName.setText(material.name);
            txtValue.setText(material.toString());
        }


    }
}
