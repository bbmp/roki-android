package com.robam.roki.ui.activity3.device.dishwasher;

import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.google.gson.Gson;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.roki.R;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.activity3.device.base.other.VerticalItemDecoration;
import com.robam.roki.ui.activity3.device.dishwasher.adapter.RvDishCourseAdapter;
import com.robam.roki.ui.activity3.device.dishwasher.bean.CourseBean;

/**
 * author : huxw
 * time   : 2022/07/04
 * desc   : 教程界面
 */
public final class DishCourseActivity extends DeviceBaseFuntionActivity {


    private RecyclerView rvCourse;
    private RvDishCourseAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dish_course;
    }

    @Override
    protected void initView() {


        rvCourse = findViewById(R.id.rv_course);
        rvCourse.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
        rvCourse.addItemDecoration(new VerticalItemDecoration(12, this));
        adapter = new RvDishCourseAdapter();
        rvCourse.setAdapter(adapter);
    }

    /**
     * 显示数据
     */
    @Override
    protected void dealData() {
        //设置title
        setTitle(deviceConfigurationFunction.functionName);
        if (deviceConfigurationFunction != null){
            String functionParams = deviceConfigurationFunction.functionParams;
            CourseBean courseBean = new Gson().fromJson(functionParams, CourseBean.class);
            if (courseBean != null && courseBean.corse != null && courseBean.corse.size() != 0){
                adapter.addData(courseBean.corse);
            }
        }
    }

}