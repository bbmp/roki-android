package com.robam.roki.ui.adapter3;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.ui.IForm;
import com.robam.roki.R;
import com.robam.roki.ui.bean3.MaterialSectionItem;
import com.robam.roki.ui.widget.view.TextEditTextView;
import com.robam.roki.utils.OnMultiClickListener;
import com.robam.roki.utils.StringUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * @author r210190
 * des 食材布局adapter
 */
public class RvMaterialAdapter extends BaseSectionQuickAdapter<MaterialSectionItem, BaseViewHolder> {

    private float num = 1;
    private float aFloat;
    private int addDown;
    private AppCompatEditText et_materials_number;
    /**
     * 第一个数值
     */
    private int first;
    private boolean isset = false;


    String oldString = "";

    float editFirst ;

    public RvMaterialAdapter(@Nullable List<MaterialSectionItem> data) {
        super(R.layout.item_materials_head, R.layout.item_materials, data);
    }

    @Override
    protected void convertHeader(@NotNull BaseViewHolder holder, @NotNull MaterialSectionItem item) {
        holder.setText(R.id.tv_materials_head, item.title);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, MaterialSectionItem item) {
        if (item.material != null) {
            if (isset){
                num = editFirst / first ;
            }
            if (getItemPosition(item) == 1) {
                try {
                    first = Integer.parseInt(item.material.standardWeight);

                    if (Float.parseFloat(item.material.standardWeight) < 10) {
                        aFloat = 1 / Float.parseFloat(item.material.standardWeight);
                        addDown = 1 ;
                    } else {
                        aFloat = 10 / Float.parseFloat(item.material.standardWeight);
                        addDown = 10 ;
                    }

                } catch (Exception ignored) {
                    ignored.getMessage();
                    aFloat = 0;
                }
                holder.getView(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setNumAdd();
                    }
                });
                holder.getView(R.id.iv_down).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setNumDown();
                    }
                });
                holder.setBackgroundResource(R.id.tv_materials_number, R.drawable.edit_materials_bg)
                        .setBackgroundResource(R.id.ll_num, R.drawable.edit_materials_bg);
                holder.setEnabled(R.id.tv_materials_number, true);
                if (et_materials_number != null) {
                    if ("0".equals(et_materials_number.getText().toString()) || num == 0) {
                        holder.setImageResource(R.id.iv_down, R.drawable.ic_down_material_off);
                        holder.setEnabled(R.id.iv_down, false);
                    } else {
                        holder.setImageResource(R.id.iv_down, R.drawable.ic_down_material);
                        holder.setEnabled(R.id.iv_down, true);
                    }

                    if ("99999".equals(et_materials_number.getText().toString())) {
                        holder.setImageResource(R.id.iv_add, R.drawable.ic_add_material_off);
                        holder.setEnabled(R.id.iv_add, false);
                    } else {
                        holder.setImageResource(R.id.iv_add, R.drawable.ic_add);
                        holder.setEnabled(R.id.iv_add, true);
                    }
                }
            } else {
                holder.setBackgroundResource(R.id.tv_materials_number, 0)
                        .setBackgroundResource(R.id.ll_num, 0);
                holder.setEnabled(R.id.tv_materials_number, false);
            }

            try {
                float weight = Float.parseFloat(item.material.standardWeight);
                String str = String.valueOf(decimalFormat2(weight * num));
                if (weight * num < 1 && weight * num > 0) {
                    str = 1 + "";
                }
//                String str = String.valueOf( decimalFormat2(weight * num)) ;
                String unit = "";
                if(null != item.material.standardUnit ){
                    unit = item.material.standardUnit ;
                }
                if (item.material.popularWeight != null && !item.material.popularWeight.isEmpty()) {
                    float pWeight = Float.parseFloat(item.material.popularWeight);
                    float v = pWeight * num ;
                    if (v < 1 && v> 0) {
                        v = 1 ;
                    }
                    unit = item.material.standardUnit + "(" + String.valueOf((int) v) + item.material.popularUnit + ")";
                }

                if (isset && getItemPosition(item) == 1){
                    holder.setText(R.id.tv_materials_name, item.material.name)
                            .setText(R.id.tv_materials_number, (int) editFirst + "")
                            .setText(R.id.tv_unit, unit)
                            .setVisible(R.id.iv_add, holder.getLayoutPosition() == 1)
                            .setVisible(R.id.iv_down, holder.getLayoutPosition() == 1)
                    ;
                    isset = false ;
                }else {
                    holder.setText(R.id.tv_materials_name, item.material.name)
                            .setText(R.id.tv_materials_number, str)
                            .setText(R.id.tv_unit, unit)
                            .setVisible(R.id.iv_add, holder.getLayoutPosition() == 1)
                            .setVisible(R.id.iv_down, holder.getLayoutPosition() == 1)
                    ;
                }


            } catch (Exception ignored) {
                ignored.getMessage();
                String weight2 = item.material.standardWeight == null ? "" : item.material.standardWeight;
                holder.setText(R.id.tv_materials_name, item.material.name)
                        .setText(R.id.tv_materials_number, weight2)
                        .setVisible(R.id.iv_add, false)
                        .setVisible(R.id.iv_down, false);
            }

            if (getItemPosition(item) == 1) {
                et_materials_number = (AppCompatEditText) holder.getView(R.id.tv_materials_number);
                et_materials_number.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                et_materials_number.addTextChangedListener(textWatcher);
                et_materials_number.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                        boolean isOK = true;
                        switch (actionId) {
                            case EditorInfo.IME_ACTION_NEXT:
                                hideInput();
                                break;
                            default:
                                isOK = false;
                                break;
                        }
                        return isOK;
                    }
                });

                isset = false ;
            }


        }
    }


    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        View v = ((Activity) getContext()).getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Log.i("--", charSequence.toString());
            if (charSequence != null && charSequence.toString() != null) {
                oldString = charSequence.toString();
            }
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            Log.i("--" , charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable != null) {
                if (editable.toString() != null) {
                    if (editable.toString().length() == 0){
                       editFirst = 0 ;
                    }else {
                        editFirst = Float.parseFloat(editable.toString());
                    }
                }
            }
        }
    };

    public void setNumAdd() {
//        num = num + aFloat;
        int i = (int) Float.parseFloat(et_materials_number.getText().toString());
        int edit = (i + addDown) > 99999 ? 99999 : (i + addDown) ;
        if (et_materials_number != null) {
            et_materials_number.setText(edit + "");
            editFirst = edit ;
        }
        isset = true ;
        if ( !isOpen)
        notifyDataSetChanged();
    }

    public void setNumDown() {
//        num = num - aFloat;
        int i = (int) Float.parseFloat(et_materials_number.getText().toString());
        int edit = (i - addDown) < 0 ? 0 : (i - addDown) ;
        if (et_materials_number != null) {
            et_materials_number.setText(edit + "");
            editFirst = edit ;
        }
        isset = true ;
        if ( !isOpen)
            notifyDataSetChanged();
    }


    public void setNum() {
        if (et_materials_number.getText().toString()== null ||"".equals(et_materials_number.getText().toString()) ){
            et_materials_number.setText("0");
            this.num = 0;
        }else {
             editFirst = Integer.parseInt(et_materials_number.getText().toString());
//            this.num = editFirst /first;
        }
        isset = true ;
        notifyDataSetChanged();

    }
    boolean isOpen = false ;
    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen ;

    }

    public static String formatNumber(String number) {
        number = new BigDecimal(number).setScale(0, BigDecimal.ROUND_UP) + "";
        return number;
    }

    private String decimalFormat2(float num) {
        DecimalFormat decimalFormat = new DecimalFormat("#");
        //保留2位小数，.后面的#代表小数点后面的位数,保留3位小数就是#.###
        return decimalFormat.format(num);
    }

}
