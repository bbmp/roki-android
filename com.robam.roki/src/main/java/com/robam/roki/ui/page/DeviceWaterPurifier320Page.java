//package com.robam.roki.ui.page;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.net.Uri;
//import android.view.View;
//
//import com.robam.common.pojos.device.WaterPurifier.WaterPurifierJ320;
//import com.robam.roki.R;
//import com.robam.roki.ui.dialog.DialogWaterPurifier321More;
//import com.robam.roki.ui.dialog.WaterPurifierAlarmFilterOutDialog;
//import com.robam.roki.utils.StringConstantsUtil;
//
///**
// * Created by Administrator on 2017/5/9.
// */
//
//public class DeviceWaterPurifier320Page extends AbsDeviceWaterPurifierPage<WaterPurifierJ320> {
//
//    @Override
//    public void setOpenUI(){
//        water_disconnectHintView.setVisibility(View.GONE);
//        water_discont_text.setVisibility(View.GONE);
//        water_power_saving.setVisibility(View.GONE);
//        water_bubble.setVisibility(View.VISIBLE);
//        water_none.setVisibility(View.VISIBLE);
//        content_wave.setVisibility(View.VISIBLE);
//        content_circle.setImageResource(R.mipmap. img_waterpurifiy_circle1);
//        waterpurifiy_lvxin_text.setVisibility(View.VISIBLE);
//        waterpurifiy_lvxin_text.setText("原水TDS : ");
//        water_drink_standard.setVisibility(View.VISIBLE);
//        water_drink_standard.setText(setWaterQuality(purifier.input_tds)+"ppm");
//        water_quality_ml.setText("ppm");
//       // mWaveHelper.start();
//        if (!mWaveHelper.waveFlag){
//            mWaveHelper.start();
//        }
//        water_cankao.setVisibility(View.VISIBLE);
//        //净化中水质
//        content_text_pre.setVisibility(View.VISIBLE);
//        content_text_pre.setText("纯水TDS");
//        content_text_pre.setTextSize(dip2px(7));
//        content_text_level.setVisibility(View.VISIBLE);
//        content_text_level.setText(setTDS(purifier.output_tds));
//        //content_text_level.setText("-");
//        waterpurifier_pic_wash.setVisibility(View.GONE);
//        //当前水质
//        water_before_pre.setVisibility(View.VISIBLE);
//        tv_btn_detail_out.setVisibility(View.GONE);
//        water_before_pre.setText("当前水质：");
//        water_before.setVisibility(View.VISIBLE);
//        // water_before.setText(setWaterQuality(purifier.input_tds));
//        water_before.setTextColor(Color.parseColor("#ffffff"));
//        water_before.setText(setCurrentWaterQuality(purifier.output_tds));
//        bottom_mark.setVisibility(View.VISIBLE);
//        water_jinghua_date.setVisibility(View.VISIBLE);
//        water_time_date.setVisibility(View.GONE);
//        //滤芯显示
//    }
//
//    @Override
//    public void setWashUI(){
//        water_discont_text.setVisibility(View.GONE);
//        water_none.setVisibility(View.VISIBLE);
//        water_bubble.setVisibility(View.VISIBLE);
//        water_power_saving.setVisibility(View.GONE);
//        water_cankao.setVisibility(View.GONE);
//        content_circle.setImageResource(R.mipmap. img_waterpurifiy_circle1);
//        waterpurifiy_lvxin_text.setVisibility(View.VISIBLE);
//        waterpurifiy_lvxin_text.setText("原水TDS : ");
//        water_drink_standard.setVisibility(View.VISIBLE);
//        water_drink_standard.setText(setWaterQuality(purifier.input_tds)+"ppm");
//        water_quality_ml.setText("ppm");
//        //净化中水质
//        content_text_pre.setVisibility(View.GONE);
//        content_text_level.setVisibility(View.GONE);
//        water_quality_ml.setVisibility(View.GONE);
//        waterpurifier_pic_wash.setVisibility(View.VISIBLE);
//       // mWaveHelper.start();
//        if (!mWaveHelper.waveFlag){
//            mWaveHelper.start();
//        }
//        water_cankao.setVisibility(View.GONE);
//        //当前水质
//        water_before_pre.setVisibility(View.GONE);
//        water_before.setVisibility(View.VISIBLE);
//        water_before.setTextColor(Color.parseColor("#ffffff"));
//        tv_btn_detail_out.setVisibility(View.GONE);
//        water_before.setText("正在冲洗滤芯...");
//        content_wave.setVisibility(View.VISIBLE);
//        water_jinghua_date.setVisibility(View.INVISIBLE);
//        water_time_date.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void setCleanUI(){
//        water_discont_text.setVisibility(View.GONE);
//        water_disconnectHintView.setVisibility(View.GONE);//关闭断网提示;
//        water_none.setVisibility(View.VISIBLE);
//        water_bubble.setVisibility(View.VISIBLE);
//        content_circle.setImageResource(R.mipmap. img_waterpurifiy_circle1);
//        water_power_saving.setVisibility(View.GONE);
//        waterpurifiy_lvxin_text.setVisibility(View.VISIBLE);
//        waterpurifiy_lvxin_text.setText("原水TDS : ");
//        water_drink_standard.setVisibility(View.VISIBLE);
//        water_drink_standard.setText(setWaterQuality(purifier.input_tds)+"ppm");
//        water_quality_ml.setText("ppm");
//        //净化中水质
//        content_text_pre.setVisibility(View.VISIBLE);
//        content_text_pre.setText("纯水TDS");
//        content_text_pre.setTextSize(dip2px(7));
//        content_text_level.setVisibility(View.VISIBLE);
//        content_text_level.setText(setTDS(purifier.output_tds));
//        water_quality_ml.setVisibility(View.VISIBLE);
//        waterpurifier_pic_wash.setVisibility(View.GONE);
//       // mWaveHelper.start();//水波开启
//        if (!mWaveHelper.waveFlag){
//            mWaveHelper.start();
//        }
//        water_cankao.setVisibility(View.VISIBLE);
//        water_before.setVisibility(View.VISIBLE);
//        //当前水质
//        water_before_pre.setVisibility(View.GONE);
//        water_before.setTextColor(Color.parseColor("#ffffff"));
//        tv_btn_detail_out.setVisibility(View.GONE);
//        water_before.setText("正在净化水质...");
//        water_jinghua_date.setVisibility(View.VISIBLE);
//        water_time_date.setVisibility(View.VISIBLE);
//        //  getSystemTime();//设置时间
//        water_jinghua_date.setVisibility(View.GONE);
//        water_time_date.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void setAlarmUi(){
//        water_disconnectHintView.setVisibility(View.GONE);
//        water_discont_text.setVisibility(View.GONE);
//        water_none.setVisibility(View.VISIBLE);
//        water_bubble.setVisibility(View.VISIBLE);
//        water_power_saving.setVisibility(View.GONE);
//        content_wave.setVisibility(View.VISIBLE);
//        content_circle.setImageResource(R.mipmap.img_waterpurifiy_circle1);
//        waterpurifiy_lvxin_text.setVisibility(View.VISIBLE);
//        waterpurifiy_lvxin_text.setText("原水TDS : ");
//        water_drink_standard.setVisibility(View.VISIBLE);
//        water_drink_standard.setText(setWaterQuality(purifier.input_tds)+"ppm");
//        water_quality_ml.setText("ppm");
//      //  mWaveHelper.start();//水波开启
//        if (!mWaveHelper.waveFlag){
//            mWaveHelper.start();
//        }
//        water_cankao.setVisibility(View.GONE);
//        //净化中水质
//        content_text_pre.setVisibility(View.VISIBLE);
//        content_text_pre.setText("纯水TDS");
//        content_text_pre.setTextSize(dip2px(7));
//        content_text_level.setVisibility(View.VISIBLE);
//        // content_text_level.setText("优");
//        // content_text_level.setText(setWaterQuality(purifier.output_tds));
//        content_text_level.setText("-");
//        waterpurifier_pic_wash.setVisibility(View.GONE);
//        //当前水质
//        water_before_pre.setVisibility(View.GONE);
//        water_before.setVisibility(View.VISIBLE);
//        tv_btn_detail_out.setVisibility(View.GONE);
//        water_before.setText("报警:"+alarmStr(purifier.alarm));
//        water_before.setTextColor(Color.parseColor("#ff3e18"));
//        bottom_mark.setVisibility(View.VISIBLE);
//        water_jinghua_date.setVisibility(View.INVISIBLE);
//        water_time_date.setVisibility(View.GONE);
//        //滤芯显示
//    }
//    @Override
//    public void setFinishUi(){
//        water_discont_text.setVisibility(View.GONE);
//        water_disconnectHintView.setVisibility(View.GONE);
//        water_none.setVisibility(View.VISIBLE);
//        water_power_saving.setVisibility(View.GONE);
//        content_circle.setImageResource(R.mipmap. img_waterpurifiy_circle1);
//        content_wave.setVisibility(View.VISIBLE);
//        waterpurifiy_lvxin_text.setVisibility(View.VISIBLE);
//        waterpurifiy_lvxin_text.setText("原水TDS : ");
//        water_drink_standard.setVisibility(View.VISIBLE);
//        water_drink_standard.setText(setWaterQuality(purifier.input_tds)+"ppm");
//        water_quality_ml.setText("ppm");
//       // mWaveHelper.start();//水波开启
//        if (!mWaveHelper.waveFlag){
//            mWaveHelper.start();
//        }
//        water_cankao.setVisibility(View.VISIBLE);
//        //净化中水质
//        content_text_pre.setVisibility(View.VISIBLE);
//        content_text_pre.setText("纯水TDS");
//        content_text_pre.setTextSize(dip2px(7));
//        content_text_level.setVisibility(View.VISIBLE);
//        water_quality_ml.setVisibility(View.VISIBLE);
//        // content_text_level.setText(setWaterQuality(purifier.output_tds));
//        content_text_level.setText(setTDS(purifier.output_tds));
//        waterpurifier_pic_wash.setVisibility(View.GONE);
//        //当前水质
//        water_before_pre.setVisibility(View.VISIBLE);
//        water_before.setVisibility(View.VISIBLE);
//        tv_btn_detail_out.setVisibility(View.GONE);
//        // water_before.setText(setWaterQuality(purifier.input_tds));
//        water_before.setTextColor(Color.parseColor("#ffffff"));
//        water_before.setText(setCurrentWaterQuality(purifier.output_tds));
//        bottom_mark.setVisibility(View.VISIBLE);
//        //滤芯显示
//        water_jinghua_date.setVisibility(View.VISIBLE);
//        water_time_date.setVisibility(View.GONE);
//    }
//
//    public void setOffUi(){
//        water_disconnectHintView.setVisibility(View.GONE);
//        water_discont_text.setVisibility(View.GONE);
//        water_none.setVisibility(View.VISIBLE);
//        content_wave.setVisibility(View.VISIBLE);
//        water_power_saving.setVisibility(View.VISIBLE);
//        waterpurifiy_lvxin_text.setVisibility(View.VISIBLE);
//        waterpurifiy_lvxin_text.setText("省电模式中...");
//        water_drink_standard.setVisibility(View.VISIBLE);
//        water_drink_standard.setVisibility(View.GONE);
//        content_circle.setImageResource(R.mipmap.ic_waterpurifier_circle);
//        if (!mWaveHelper.waveFlag){
//            mWaveHelper.start();
//        }
//        water_cankao.setVisibility(View.VISIBLE);
//        water_quality_ml.setText("ppm");
//        //净化中水质
//        content_text_pre.setVisibility(View.VISIBLE);
//        content_text_pre.setText("纯水TDS");
//        content_text_pre.setTextSize(dip2px(7));
//        content_text_level.setVisibility(View.VISIBLE);
//        content_text_level.setText(setTDS(purifier.output_tds));
//        content_text_level.setTextSize(dip2px(30));
//        //content_text_level.setText("-");
//        waterpurifier_pic_wash.setVisibility(View.GONE);
//        //当前水质
//        water_before_pre.setVisibility(View.VISIBLE);
//        tv_btn_detail_out.setVisibility(View.GONE);
//        water_before_pre.setText("当前水质：");
//        water_before.setVisibility(View.VISIBLE);
//        // water_before.setText(setWaterQuality(purifier.input_tds));
//        water_before.setTextColor(Color.parseColor("#ffffff"));
//        water_before.setText(setCurrentWaterQuality(purifier.output_tds));
//        bottom_mark.setVisibility(View.VISIBLE);
//        water_jinghua_date.setVisibility(View.INVISIBLE);
//        water_time_date.setVisibility(View.GONE);
//    }
//
//
//    @Override
//    void setOffOrDisConnectModel() {
//        isCon=true;
//        scrollViewEnable = true;//开启滑动
//        // mWaveHelper.cancel();
//        if (mWaveHelper.waveFlag){
//            mWaveHelper.cancel();
//        }
//        water_cankao.setVisibility(View.GONE);
//        //设置背景的颜色
//        waterpurifier_background.setBackgroundColor(Color.parseColor("#5f97fa"));
//        water_lvxin_use_time.setVisibility(View.GONE);
//        water_disconnectHintView.setVisibility(View.VISIBLE);//断网提示
//        water_none.setVisibility(View.GONE);
//        water_bubble.setVisibility(View.INVISIBLE);
//        waterpurifiy_lvxin_text.setVisibility(View.VISIBLE);
//        waterpurifiy_lvxin_text.setText("原水TDS : ");
//        water_quality_ml.setVisibility(View.VISIBLE);
//        water_drink_standard.setVisibility(View.VISIBLE);
//        water_drink_standard.setText("-ppm");
//        water_quality_ml.setText("ppm");
//        content_text_pre.setVisibility(View.VISIBLE);
//        content_text_pre.setText("纯水TDS");
//        content_text_pre.setTextSize(dip2px(7));
//        content_text_level.setVisibility(View.VISIBLE);
//        content_text_level.setText("-");
//        filterUnconnectStatus();
//        content_circle.setVisibility(View.VISIBLE);
//        content_wave.setVisibility(View.VISIBLE);
//        water_discont_text.setVisibility(View.GONE);
//        water_before_pre.setVisibility(View.VISIBLE);
//        water_before.setVisibility(View.VISIBLE);
//        water_before_pre.setText("当前水质:");
//        tv_btn_detail_out.setVisibility(View.GONE);
//        water_before.setText("-");
//        water_before.setTextColor(Color.parseColor("#ffffff"));
//        water_jinghua_date.setVisibility(View.INVISIBLE);
//        water_time_date.setVisibility(View.GONE);
//        //  filterStatus();
//    }
//
//    //更多点击事件
//    @Override
//    public void onClickMore() {
//        //super.onClickMore();
//        DialogWaterPurifier321More.show(cx,guid);
//    }
//
//    @Override
//    public void filterStatus(){
//        if (purifier.filter_state_pp == 255) {
//            filter_number_1.setBackgroundResource(R.drawable.shape_red_circular);
//            filter_pp_num.setText("已失效");
//            filter_pp_num.invalidate();
//            filter_pp_num.setTextSize(dip2px(6));
//            filter_pp_num.setTextColor(Color.parseColor("#fe0e0e"));
//            water_text_hao1.setTextColor(Color.parseColor("#fe0e0e"));
//            filter_pp_sign.setVisibility(View.GONE);
//            filter_pp_pic.setImageResource(R.mipmap.img_waterpurifier_right);
//        }else{
//            if (setFilterLife(purifier.filter_state_pp) <=10) {
//                filterDate321Status();
//                if (mRokiDialog != null && mRokiDialog.isShow()) return;
//                if (flag5){
//                    waterFilterShow();
//                }
//
//                filter_number_1.setBackgroundResource(R.drawable.shape_red_circular);
//                filter_pp_num.setText(setFilterLife(purifier.filter_state_pp)+"");
//                filter_pp_num.setTextSize(dip2px(6));
//                filter_pp_num.invalidate();
//                //filter_pp_num.setTextSize(dip2px(12));
//                filter_pp_num.setTextColor(Color.parseColor("#fe0e0e"));
//                water_text_hao1.setTextColor(Color.parseColor("#fe0e0e"));
//                filter_pp_sign.setVisibility(View.VISIBLE);
//                filter_pp_sign.setTextColor(Color.parseColor("#fe0e0e"));
//                filter_pp_pic.setImageResource(R.mipmap.img_waterpurifier_right);
//
//            } else {
//                filter_number_1.setBackgroundResource(R.drawable.shape_white_circular);
//                filter_pp_num.setText(setFilterLife(purifier.filter_state_pp) + "");
//                filter_pp_num.setTextSize(dip2px(8));
//                filter_pp_num.invalidate();
//                filter_pp_num.setTextColor(Color.parseColor("#ffffff"));
//                water_text_hao1.setTextColor(Color.parseColor("#ffffff"));
//                filter_pp_sign.setVisibility(View.VISIBLE);
//                filter_pp_sign.setTextColor(Color.parseColor("#ffffff"));
//                filter_pp_pic.setImageResource(R.mipmap.img_waterpurifiy_filterright_white);
//            }
//        }
//
//
//        if (purifier.filter_state_cto == 255) {
//            filter_number_2.setBackgroundResource(R.drawable.shape_red_circular);
//            filter_cto_num.setText("已失效");
//            filter_cto_num.invalidate();
//            filter_cto_num.setTextSize(dip2px(6));
//            filter_cto_num.setTextColor(Color.parseColor("#fe0e0e"));
//            water_text_hao2.setTextColor(Color.parseColor("#fe0e0e"));
//            filter_cto_sign.setVisibility(View.GONE);
//            filter_cto_pic.setImageResource(R.mipmap.img_waterpurifier_right);
//
//        }else{
//            if (setFilterLife(purifier.filter_state_cto) <=10) {
//                filterDate321Status();
//                if (mRokiDialog != null && mRokiDialog.isShow()) return;
//                if (flag2){
//                    waterFilterShow();
//                }
//                filter_number_2.setBackgroundResource(R.drawable.shape_red_circular);
//                filter_cto_num.setText(setFilterLife(purifier.filter_state_cto)+"");
//                filter_cto_num.setTextSize(dip2px(6));
//                filter_cto_num.invalidate();
//                // filter_cto_num.setTextSize(dip2px(12));
//                filter_cto_num.setTextColor(Color.parseColor("#fe0e0e"));
//                water_text_hao2.setTextColor(Color.parseColor("#fe0e0e"));
//                filter_cto_sign.setVisibility(View.VISIBLE);
//                filter_cto_sign.setTextColor(Color.parseColor("#fe0e0e"));
//                filter_cto_pic.setImageResource(R.mipmap.img_waterpurifier_right);
//
//            } else {
//                filter_number_2.setBackgroundResource(R.drawable.shape_white_circular);
//                filter_cto_num.setText(setFilterLife(purifier.filter_state_cto) + "");
//                filter_cto_num.setTextSize(dip2px(8));
//                filter_cto_num.invalidate();
//                filter_cto_num.setTextColor(Color.parseColor("#ffffff"));
//                water_text_hao2.setTextColor(Color.parseColor("#ffffff"));
//                filter_cto_sign.setVisibility(View.VISIBLE);
//                filter_cto_sign.setTextColor(Color.parseColor("#ffffff"));
//                filter_cto_pic.setImageResource(R.mipmap.img_waterpurifiy_filterright_white);
//            }
//        }
//
//        if (purifier.filter_state_ro1 == 255) {
//            filter_number_3.setBackgroundResource(R.drawable.shape_red_circular);
//            filter_ro1_num.setText("已失效");
//            filter_ro1_num.invalidate();
//            filter_ro1_num.setTextSize(dip2px(6));
//            filter_ro1_num.setTextColor(Color.parseColor("#fe0e0e"));
//            water_text_hao3.setTextColor(Color.parseColor("#fe0e0e"));
//            filter_ro1_sign.setVisibility(View.GONE);
//            filter_ro1_pic.setImageResource(R.mipmap.img_waterpurifier_right);
//        }else{
//            if (setFilterLife(purifier.filter_state_ro1) <= 10) {
//                filterDate321Status();
//                if (mRokiDialog != null && mRokiDialog.isShow()) return;
//                if (flag3){
//                    waterFilterShow();
//                }
//                filter_number_3.setBackgroundResource(R.drawable.shape_red_circular);
//                filter_ro1_num.setText(setFilterLife(purifier.filter_state_ro1)+"");
//                filter_ro1_num.setTextSize(dip2px(6));
//                filter_ro1_num.invalidate();
//                //filter_ro1_num.setTextSize(dip2px(12));
//                filter_ro1_num.setTextColor(Color.parseColor("#fe0e0e"));
//                water_text_hao3.setTextColor(Color.parseColor("#fe0e0e"));
//                filter_ro1_sign.setVisibility(View.VISIBLE);
//                filter_ro1_sign.setTextColor(Color.parseColor("#fe0e0e"));
//                filter_ro1_pic.setImageResource(R.mipmap.img_waterpurifier_right);
//            } else {
//                filter_number_3.setBackgroundResource(R.drawable.shape_white_circular);
//                filter_ro1_num.setText(setFilterLife(purifier.filter_state_ro1) + "");
//                filter_ro1_num.setTextSize(dip2px(8));
//                filter_ro1_num.invalidate();
//                filter_ro1_num.setTextColor(Color.parseColor("#ffffff"));
//                water_text_hao3.setTextColor(Color.parseColor("#ffffff"));
//                filter_ro1_sign.setVisibility(View.VISIBLE);
//                filter_ro1_sign.setTextColor(Color.parseColor("#ffffff"));
//                filter_ro1_pic.setImageResource(R.mipmap.img_waterpurifiy_filterright_white);
//            }
//
//        }
//
//        if (purifier.filter_state_ro2 == 255){
//            filter_number_4.setBackgroundResource(R.drawable.shape_red_circular);
//            filter_ro2_num.setText("已失效");
//            filter_ro2_num.invalidate();
//            filter_ro2_num.setTextSize(dip2px(6));
//            filter_ro2_num.setTextColor(Color.parseColor("#fe0e0e"));
//            water_text_hao4.setTextColor(Color.parseColor("#fe0e0e"));
//            filter_ro2_sign.setVisibility(View.GONE);
//            filter_ro2_pic.setImageResource(R.mipmap.img_waterpurifier_right);
//        }else{
//            if (setFilterLife(purifier.filter_state_ro2) <= 10) {
//                filterDate321Status();
//                if (mRokiDialog != null && mRokiDialog.isShow()) return;
//                if (flag4){
//                   waterFilterShow();
//                }
//                filter_number_4.setBackgroundResource(R.drawable.shape_red_circular);
//                filter_ro2_num.setText(setFilterLife(purifier.filter_state_ro2)+"");
//                filter_ro2_num.setTextSize(dip2px(6));
//                filter_ro2_num.invalidate();
//                // filter_ro2_num.setTextSize(dip2px(12));
//                filter_ro2_num.setTextColor(Color.parseColor("#fe0e0e"));
//                water_text_hao4.setTextColor(Color.parseColor("#fe0e0e"));
//                filter_ro2_sign.setVisibility(View.VISIBLE);
//                filter_ro2_sign.setTextColor(Color.parseColor("#fe0e0e"));
//                filter_ro2_pic.setImageResource(R.mipmap.img_waterpurifier_right);
//
//            } else {
//                filter_number_4.setBackgroundResource(R.drawable.shape_white_circular);
//                filter_ro2_num.setText(setFilterLife(purifier.filter_state_ro2) + "");
//                filter_ro2_num.setTextSize(dip2px(8));
//                filter_ro2_num.invalidate();
//                filter_ro2_num.setTextColor(Color.parseColor("#ffffff"));
//                water_text_hao4.setTextColor(Color.parseColor("#ffffff"));
//                filter_ro2_sign.setVisibility(View.VISIBLE);
//                filter_ro2_sign.setTextColor(Color.parseColor("#ffffff"));
//                filter_ro2_pic.setImageResource(R.mipmap.img_waterpurifiy_filterright_white);
//            }
//        }
//
//    }
//
//    //滤芯即将到期提示
//    private void waterFilterShow() {
//        if (mRokiDialog != null){
//            mRokiDialog.setTitleText(R.string.device_water_name);
//            mRokiDialog.setTitleAralmCodeText(R.string.device_alarm_filter_due);
//            mRokiDialog.setContentText(R.string.device_alarm_water_filter_due_content);
//            mRokiDialog.setOkBtn(R.string.ok_sale_service, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mRokiDialog.dismiss();
//                    controlFilterShowFlag();
//                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + StringConstantsUtil.STRING_SERVICE_PHONE));
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//            });
//            mRokiDialog.setCancelBtn(R.string.can_good, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    controlFilterShowFlag();
//                }
//            });
//            mRokiDialog.show();
//        }
//    }
//
//    private void controlFilterShowFlag() {
//        flag1 = false;
//        flag2 = false;
//        flag3 = false;
//        flag4 = false;
//        flag5 = false;
//    }
//
//    public void setBackground() {
//
//        if (setFilterLife(purifier.filter_state_pp) <=10 || setFilterLife(purifier.filter_state_cto) <= 10 || setFilterLife(purifier.filter_state_ro1) <= 10
//                || setFilterLife(purifier.filter_state_ro2) <= 10) {
//            waterpurifier_background.setBackgroundColor(Color.parseColor("#D19990"));
//            water_lvxin_use_time.setVisibility(View.VISIBLE);
//            //filterDateStatus();
//
//        } else {
//            waterpurifier_background.setBackgroundColor(Color.parseColor("#5f97fa"));
//            water_lvxin_use_time.setVisibility(View.GONE);
//        }
//    }
//}
