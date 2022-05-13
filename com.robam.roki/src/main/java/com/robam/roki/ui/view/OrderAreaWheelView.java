//package com.robam.roki.ui.view;
//
//import android.content.Context;
//import android.util.AttributeSet;
//
//import com.google.common.base.Objects;
//import com.google.common.collect.Lists;
//import com.legent.ui.ext.views.SimpleAreaWheelView;
//
//import java.util.List;
//
///**
// * Created by sylar on 15/8/13.
// */
//public class OrderAreaWheelView extends SimpleAreaWheelView {
//    public OrderAreaWheelView(Context cx) {
//        super(cx);
//        init();
//    }
//
//    public OrderAreaWheelView(Context cx, AttributeSet attrs) {
//        super(cx, attrs);
//        init();
//    }
//
//    List<String> list1 = Lists.newArrayList();
//
//    void init() {
//        list1.addAll(Lists.newArrayList("北京"));
//    }
//
//    @Override
//    protected List<?> getList1() {
//        return list1;
//    }
//
//    @Override
//    protected List<?> getList2(Object province) {
//        String city = province.toString();
//        List<String> res = Lists.newArrayList();
//
//        if (Objects.equal(city, "广州")) {
//            res.add("广州");
//            res.add("深圳");
//            return res;
//        } else if (Objects.equal(city, "浙江")) {
//            res.add("杭州");
//            return res;
//        } else {
//            return super.getList2(province);
//        }
//    }
//
//    @Override
//    protected List<?> getList3(Object city) {
//        return super.getList3(city);
//    }
//}
