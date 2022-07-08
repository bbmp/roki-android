package com.robam.roki.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.robam.roki.R;

import java.text.DecimalFormat;

public class LineChartShowView extends MarkerView {

    private TextView tv_name;
    private TextView tvValue;
    private IAxisValueFormatter xAxisValueFormatter;
    DecimalFormat df = new DecimalFormat(".00");

    public LineChartShowView(Context context, IAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.layout_show_view);
        this.xAxisValueFormatter = xAxisValueFormatter;
        tv_name = findViewById(R.id.tv_name);
        tvValue = findViewById(R.id.tv_value);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
    }

    public void setTvValue(String strValue){
        tvValue.setText(strValue);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

}
