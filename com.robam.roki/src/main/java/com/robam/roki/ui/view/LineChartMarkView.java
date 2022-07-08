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

public class LineChartMarkView extends MarkerView {

    private TextView tv_name;
    private TextView tvValue;
    private IAxisValueFormatter xAxisValueFormatter;
    DecimalFormat df = new DecimalFormat(".00");

    public LineChartMarkView(Context context, IAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.layout_markview);
        this.xAxisValueFormatter = xAxisValueFormatter;
        tv_name = findViewById(R.id.tv_name);
        tvValue = findViewById(R.id.tv_value);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        //展示自定义X轴值 后的X轴内容
//        tvDate.setText(xAxisValueFormatter.getFormattedValue(e.getX(), null));
//        tvValue.setText("温度：" + df.format(e.getY()));
//        String strTime = xAxisValueFormatter.getFormattedValue(e.getX(), null);
        String strTime;
        if (e.getX() < 60) {
            strTime = (int) e.getX() + "s";
        } else {
            strTime = (int) e.getX() / 60 + "min" + (int) e.getX() % 60 + "s";
        }

//        tvValue.setText(e.getY() + "℃\n\n" + strTime);
        tvValue.setText((int)e.getY() + "℃");


        super.refreshContent(e, highlight);
    }

    public void setTvValue(String strValue) {
        tvValue.setText(strValue);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

}
