package com.robam.roki.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.tts.TTSEngine;
import com.google.common.collect.Lists;
import com.legent.plat.pojos.device.AbsDevice;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.IForm;
import com.legent.ui.ext.adapters.ExtPageAdapter;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.speech.SpeechManager;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.pojos.device.microwave.MicroWaveStatus;
import com.robam.roki.R;
import com.robam.roki.utils.DensityUtil;
import com.robam.roki.utils.DeviceSelectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 自动烹饪 new
 */

public class RecipeDetailAutoView extends FrameLayout implements ViewPager.OnPageChangeListener {
    @InjectView(R.id.recipe_step_show)
    public ListView recipeStepShow;

    @InjectView(R.id.viewpage_show_img)
    MyViewPager showImg;

//    @InjectView(R.id.speak_show)
//    ImageView speakShow;

    @InjectView(R.id.mode_show)
    LinearLayout modeShow;

    /* @InjectView(R.id.scroll_view)
     ScrollView  scrollView;*/
    public CookStep cookStep;//菜谱步骤对象
    public ArrayList<CookStep> cookStepTemp;//菜谱步骤对象
    public Recipe recipe;
    private Context cx;
    public int step;//当前步骤号
    public AbsDevice device;//设备信息
    public Map<String, com.robam.common.paramCode> paramMap;//参数map
    private com.robam.common.paramCode paramCode;
    public ExtPageAdapter adapter;
    public CardAdapter cardAdapter;
    private IDevice iDevice;
    public RecipeParamShowView recipeParamShowView;
    final static short STOVEWORK = 15;
    final static short POTWORK = 20;
    final static short HASHEATFINISH = 16;

    final static String stoveImg = "https://oss.myroki.com/cookbook/cooking/default/rrqz.jpg";

    final static String ovenImg = "https://oss.myroki.com/cookbook/cooking/default/rdkx.jpg";

    final static String micrImg = "https://oss.myroki.com/cookbook/cooking/default/rwbl.jpg";

    final static String steamImg = "https://oss.myroki.com/cookbook/cooking/default/rzql.jpg";

    final static String ovStImg = "https://oss.myroki.com/cookbook/cooking/default/rytj.jpg";
    /**
     * 横竖屏标签 0：竖屏 1 ：横屏
     */
    private  int type  = 0 ;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    break;
                default:
                    break;
            }
        }
    };
    private ArrayList<View> list;


    public RecipeDetailAutoView(Context context, ArrayList<CookStep> cookStepTemp, IDevice iDevice) {
        super(context);
        this.cx = context;
        this.iDevice = iDevice;
        this.cookStepTemp = cookStepTemp;
        this.cookStep = cookStepTemp.get(0);
        init(context);
    }

    public RecipeDetailAutoView(Context context, ArrayList<CookStep> cookStepTemp, IDevice iDevice , int type) {
        super(context);
        this.cx = context;
        this.iDevice = iDevice;
        this.cookStepTemp = cookStepTemp;
        this.cookStep = cookStepTemp.get(0);
        this.type = type ;
        init(context);
    }

    public void setiDevice(IDevice iDevice) {
        this.iDevice = iDevice;
    }

    public IDevice getiDevice() {
        return iDevice;
    }

    List<String> urlTemp = new ArrayList<>();

    void init(Context cx) {

        View view = LayoutInflater.from(cx).inflate(type == 0 ? R.layout.view_recipe_auto : R.layout.view_recipe_auto_h,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }

        //listView展示的页面
        cardAdapter = new CardAdapter(cx, cookStepTemp);
        recipeStepShow.setAdapter(cardAdapter);
        if (step < cardAdapter.getCount()) {
            recipeStepShow.setSelection(step);
        }
        //参数设置页面
        recipeParamShowView = new RecipeParamShowView(cx, cookStepTemp, iDevice);
        modeShow.addView(recipeParamShowView);
        if ("".equals(cookStepTemp.get(0).getDc())) {
            modeShow.setVisibility(View.GONE);
        } else {
            modeShow.setVisibility(View.VISIBLE);
        }
        //图片展示的页面
        if (type == 0) {
            View rl_image_video = view.findViewById(R.id.rl_image_video);
            if (cookStepTemp.get(0).stepVideo != null) {
                setViewLayoutParams(rl_image_video, 210);
            } else {
                setViewLayoutParams(rl_image_video, 350);
            }
        }
        adapter = new ExtPageAdapter();//构建adapter
        showImg.setAdapter(adapter);

        list = Lists.newArrayList();
//        urlTemp = setImgData();
//        for (int i = 0; i < urlTemp.size(); i++) {
//            RecipeDetailShowImgView recipeDetailShowImgView = new RecipeDetailShowImgView(cx, urlTemp, i, iDevice);
//            list.add(recipeDetailShowImgView);
//        }
        for (CookStep step : cookStepTemp) {
            if (step.stepVideo != null) {
                list.add(new RecipeDetailShowVideoView(cx, step.stepVideo));
            } else {
                list.add(new RecipeDetailShowImgView(cx, step.imageUrl));
            }
        }
        adapter.loadViews(list);
        if (step<adapter.getCount()) {
            showImg.setCurrentItem(step);
            startVideo(step);
        }
    }
    /**
     * 重设 view 的高度
     */
    public void setViewLayoutParams(View view, int nHeight) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        int height = DensityUtil.dip2px(cx, nHeight);
        if (lp.height != height) {
            lp.height = height;
            view.setLayoutParams(lp);
        }
    }

    private  void  startVideo(int step){
        try {
            if (list.get(step) instanceof RecipeDetailShowVideoView) {
                ((RecipeDetailShowVideoView) list.get(step)).startVideo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<String> setImgData() {
        List<String> url = new ArrayList<>();
        List<String> str = new ArrayList<>();
        if ("".equals(cookStepTemp.get(0).imageUrl)) {
            switch (iDevice.getDc()) {
                case "RRQZ":
                case "RDCZ":
                    url.add(stoveImg);
                    break;
                case "RDKX":
                    url.add(ovenImg);
                    break;
                case "RZQL":
                    url.add(steamImg);
                    break;
                case "RWBL":
                    url.add(micrImg);
                    break;
                case "RZKY":
                    url.add(ovStImg);
                    break;
                default:
                    break;
            }

        } else {
            url.add(cookStepTemp.get(0).imageUrl);
        }

        for (int i = 1; i < cookStepTemp.size(); i++) {
            if ("".equals(cookStepTemp.get(i).imageUrl)) {
                url.add(url.get(i - 1));
            } else {
                url.add(cookStepTemp.get(i).imageUrl);
            }
        }

        str.add(url.get(0));
        for (int i = 1; i < url.size(); i++) {
            if (url.get(i).equals(url.get(i - 1))) {

            } else {
                str.add(url.get(i));
            }
        }

        return str;
    }


    private void isCanH(int step) {
        if (step == 0) {
            showImg.setCurrentItem(step);
            startVideo(step);
        } else {
            if (cookStepTemp.get(step).imageUrl.equals(cookStepTemp.get(step - 1).imageUrl) || "".equals(cookStepTemp.get(step).imageUrl)) {
                for (int i = step; i > 0; i--) {
                    if (cookStepTemp.get(i).imageUrl.equals(cookStepTemp.get(i - 1).imageUrl) || "".equals(cookStepTemp.get(i).imageUrl)) {

                    } else {
                        showImg.setCurrentItem(i);
                        startVideo(i);
                        break;
                    }
                }
            } else {
                showImg.setCurrentItem(step);
                startVideo(step);
            }
        }


    }

    public void onfresh(int step) {
        if ("".equals(cookStepTemp.get(step).getDc())) {
            modeShow.setVisibility(View.GONE);
        } else {
            modeShow.setVisibility(View.VISIBLE);
            recipeParamShowView.setiDevice(getiDevice());
            recipeParamShowView.onfresh(step);
        }

        stepTemp = step;
        isCanH(step);
        LogUtils.i("202010108888","recipeStepShow::::step:::::"+step);
        recipeStepShow.setSelection(step);

    }


    public void onfreshView(short status, int step, short currentSec, short timeTotal, boolean flag, int tempu) {
        View view = lmap.get(step);
        if (view == null || view.getTag() == null){
            return;
        }
        MyViewHolder myViewHolder = (MyViewHolder) view.getTag();
        if (myViewHolder == null) return;
        LogUtils.i("20180225", "getDC:::" + cookStepTemp.get(step).getDc());

        if (cookStepTemp.get(step).getDc() == null)
            return;
        if (step != myViewHolder.pos) {
            return;
        }
        LogUtils.i("20180225", "status:" + status + " step:" + step + " currentSec:"
                + currentSec + " timeTotal:" + timeTotal + " flag:" + flag + " tempu:" + tempu);

        onfreshViewRevert(step - 1);//当前步骤之前的全部重制
        if (currentSec == 0) {
            myViewHolder.img_pause.setVisibility(View.INVISIBLE);
            myViewHolder.recipe_progress.setVisibility(View.INVISIBLE);
            myViewHolder.recipe_desc.setTextColor(Color.parseColor("#666666"));
            myViewHolder.card_bg.setCardBackgroundColor(Color.parseColor("#ffffff"));
            myViewHolder.recipe_cooking_show.setVisibility(VISIBLE);
            myViewHolder.ivStop.setVisibility(GONE);
            myViewHolder.recipe_cooking_show.setText("已完成");
            return;
        }

        myViewHolder.recipe_desc.setTextColor(Color.parseColor("#000000"));
        myViewHolder.tips_img.setImageResource(R.mipmap.c_tips_2);
        myViewHolder.tips.setTextColor(Color.parseColor("#303030"));
        myViewHolder.item_bg.setBackgroundColor(getResources().getColor(R.color.transparent));
        myViewHolder.ivStop.setVisibility(INVISIBLE);
        if (myViewHolder.card_bg != null)
            myViewHolder.card_bg.setCardBackgroundColor(Color.parseColor("#EFCE17"));

        switch (status) {
            case OvenStatus.PreHeat:
                myViewHolder.recipe_cooking_show.setVisibility(VISIBLE);
                myViewHolder.img_pause.setVisibility(View.INVISIBLE);
                myViewHolder.ivStop.setVisibility(View.VISIBLE);
                myViewHolder.recipe_progress.setVisibility(View.INVISIBLE);
                myViewHolder.recipe_cooking_show.setTextColor(Color.parseColor("#000000"));
                myViewHolder.recipe_cooking_show.setText("预热中 " + currentSec + "℃");
                break;
            case MicroWaveStatus.Run:
                myViewHolder.recipe_cooking_show.setVisibility(VISIBLE);
                myViewHolder.img_pause.setVisibility(View.INVISIBLE);
                myViewHolder.ivStop.setVisibility(View.VISIBLE);
                myViewHolder.recipe_cooking_show.setTextColor(Color.parseColor("#000000"));
                myViewHolder.recipe_cooking_show.setText(DeviceSelectUtils.getInstance().timeStardard(currentSec));
                myViewHolder.recipe_progress.setVisibility(View.VISIBLE);
                myViewHolder.recipe_progress.setMax(timeTotal);
                myViewHolder.recipe_progress.setProgress(timeTotal - currentSec);
                break;
            case OvenStatus.Working:
                myViewHolder.recipe_cooking_show.setVisibility(VISIBLE);
                myViewHolder.img_pause.setVisibility(View.INVISIBLE);
                myViewHolder.ivStop.setVisibility(View.VISIBLE);
                myViewHolder.recipe_cooking_show.setTextColor(Color.parseColor("#000000"));
                myViewHolder.recipe_cooking_show.setText(DeviceSelectUtils.getInstance().timeStardard(currentSec));
                myViewHolder.recipe_progress.setVisibility(View.VISIBLE);
                myViewHolder.recipe_progress.setMax(timeTotal * 60);
                myViewHolder.recipe_progress.setProgress((timeTotal * 60) - currentSec);
                break;
            case OvenStatus.Pause:
                myViewHolder.item_bg.setBackgroundResource(R.drawable.item_bg_conner);
                myViewHolder.img_pause.setVisibility(View.VISIBLE);
                myViewHolder.ivStop.setVisibility(View.INVISIBLE);
                myViewHolder.iv_img_pause.setVisibility(View.VISIBLE);
                myViewHolder.pause_desc.setVisibility(View.VISIBLE);
                myViewHolder.recipe_cooking_show.setText("暂停中");
                if (flag) {
                    myViewHolder.pause_desc.setText("预热完成，放入食材后继续");
                } else {
                    myViewHolder.pause_desc.setText("点击继续");
                }
                break;
            case OvenStatus.Off:
                if (!"".equals(cookStepTemp.get(step).getDc())) {
                    myViewHolder.recipe_cooking_show.setVisibility(VISIBLE);
                    myViewHolder.img_pause.setVisibility(View.INVISIBLE);
                    myViewHolder.ivStop.setVisibility(View.INVISIBLE);
                    myViewHolder.item_bg.setBackgroundResource(R.drawable.item_bg_conner_no_device);
                    myViewHolder.recipe_cooking_show.setTextColor(Color.parseColor("#000000"));
                    myViewHolder.recipe_cooking_show.setText(DeviceSelectUtils.getInstance().timeStardard(currentSec));
                    myViewHolder.recipe_progress.setVisibility(View.INVISIBLE);
                    myViewHolder.recipe_cooking_show.setVisibility(View.INVISIBLE);
                }
                break;
            case STOVEWORK:
                myViewHolder.img_pause.setVisibility(View.INVISIBLE);
                myViewHolder.ivStop.setVisibility(View.INVISIBLE);
                myViewHolder.recipe_cooking_show.setTextColor(Color.parseColor("#000000"));
                myViewHolder.recipe_cooking_show.setText(DeviceSelectUtils.getInstance().timeStardard(currentSec));
                myViewHolder.recipe_progress.setVisibility(View.VISIBLE);
                myViewHolder.recipe_progress.setMax(timeTotal);
                myViewHolder.recipe_progress.setProgress(timeTotal - currentSec);
                break;
            case POTWORK:
                myViewHolder.img_pause.setVisibility(View.INVISIBLE);
                myViewHolder.ivStop.setVisibility(View.INVISIBLE);
                myViewHolder.recipe_cooking_show.setTextColor(Color.parseColor("#000000"));
                myViewHolder.recipe_cooking_show.setText(tempu + "℃");
                myViewHolder.recipe_progress.setVisibility(View.INVISIBLE);
                break;
            default:
                if (!cookStepTemp.get(step).getDc().equals("")) {
                    myViewHolder.recipe_cooking_show.setVisibility(VISIBLE);
                    myViewHolder.img_pause.setVisibility(View.INVISIBLE);
                    myViewHolder.ivStop.setVisibility(View.INVISIBLE);
                    myViewHolder.item_bg.setBackgroundResource(R.drawable.item_bg_conner_no_device);
                    myViewHolder.recipe_cooking_show.setTextColor(Color.parseColor("#000000"));
                    myViewHolder.recipe_cooking_show.setText(DeviceSelectUtils.getInstance().timeStardard(currentSec));
                    myViewHolder.recipe_progress.setVisibility(View.INVISIBLE);
                    myViewHolder.recipe_cooking_show.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    public void onfreshViewForNext(short status, int step, short temp) {
        LogUtils.i("20180124", "step:" + step + "lmap:" + lmap.get(step));
        View view = lmap.get(step);
        if (view == null) return;
        MyViewHolder myViewHolder = (MyViewHolder) view.getTag();
        if (myViewHolder == null)
            return;

        if (temp < 6) {
            myViewHolder.recipe_cooking_show.setText("即将开始");
            myViewHolder.recipe_cooking_show.setTextColor(Color.parseColor("#EFCE17"));
        }
    }

    int stepTemp;

    public void onfreshViewRevert(int step) {
        for (int i = 0; i <= step; i++) {
            View view = lmap.get(i);
            MyViewHolder myViewHolder = (MyViewHolder) view.getTag();
            myViewHolder.img_pause.setVisibility(View.INVISIBLE);
            myViewHolder.recipe_progress.setVisibility(View.INVISIBLE);
            myViewHolder.item_bg.setBackgroundResource(R.drawable.item_bg_conner_no_device);
            myViewHolder.card_bg.setCardBackgroundColor(Color.parseColor("#ffffff"));
            myViewHolder.recipe_desc.setTextColor(Color.parseColor("#666666"));
            //明天修改这个地方
            myViewHolder.item_bg.setBackgroundColor(getResources().getColor(R.color.transparent));
            myViewHolder.recipe_cooking_show.setTextColor(Color.parseColor("#000000"));
            myViewHolder.no_device_status.setVisibility(View.GONE);
            myViewHolder.recipe_cooking_show.setVisibility(VISIBLE);
            myViewHolder.tips_img.setImageResource(R.mipmap.c_tips_1);
            myViewHolder.tips.setTextColor(Color.parseColor("#888888"));
            myViewHolder.recipe_cooking_show.setText("已完成");
            myViewHolder.ivStop.setVisibility(GONE);
        }

    }

    public void onfreshNoDeviceView(int step) {
        View view = lmap.get(step);
        MyViewHolder myViewHolder ;
        if (view == null || view.getTag() == null){
            return;
        }
//        if (view == null){
////            cardAdapter.get
//        }else {
            myViewHolder = (MyViewHolder) view.getTag();
//        }

//        myViewHolder.item_bg.setBackgroundResource(R.drawable.item_bg_conner_no_device_select);
        myViewHolder.item_bg.setBackgroundResource(R.drawable.item_bg_conner);
        myViewHolder.img_pause.setVisibility(View.VISIBLE);
        myViewHolder.iv_img_pause.setVisibility(View.INVISIBLE);
        myViewHolder.pause_desc.setVisibility(View.INVISIBLE);
        myViewHolder.card_bg.setCardBackgroundColor(Color.parseColor("#EFCE17"));
        myViewHolder.recipe_cooking_show.setVisibility(GONE);
        myViewHolder.recipe_progress.setVisibility(View.INVISIBLE);
        myViewHolder.recipe_desc.setTextColor(Color.parseColor("#000000"));
        myViewHolder.no_device_status.setVisibility(VISIBLE);
    }


    HashMap<Integer, View> lmap = new HashMap<>();

    //语音识别
    public void onSpeakClick(final int step) {
        if (StringUtils.isNullOrEmpty(cookStepTemp.get(step).desc))
            return;


        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SpeechManager.getInstance().startSpeaking(cookStepTemp.get(step).desc);
            }
        }, 100);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class CardAdapter extends BaseAdapter {
        Context cx;
        ArrayList<CookStep> cookSteps = null;
        public int position = -1 ;

        private LayoutInflater inflater;

        public CardAdapter(Context cx, ArrayList<CookStep> recipe) {
            this.cx = cx;
            cookSteps = recipe;
            inflater = LayoutInflater.from(cx);
        }

        @Override
        public int getCount() {
            return cookSteps == null ? 0 : cookSteps.size();
        }

        @Override
        public Object getItem(int position) {
            return cookSteps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder myViewHolder = null;
            if (lmap.get(position) == null) {
                convertView = inflater.inflate(R.layout.recipe_step_page_list, null);
                myViewHolder = new MyViewHolder(convertView);
                convertView.setTag(myViewHolder);
                lmap.put(position, convertView);
                myViewHolder.pos = position;
            } else {
                convertView = lmap.get(position);
                myViewHolder = (MyViewHolder) convertView.getTag();
                myViewHolder.pos = position;
                lmap.put(position, convertView);
            }
            String txt = (position + 1) + "/" + cookSteps.size() + " " + cookSteps.get(position).desc;
//            SpannableString textSpan = new SpannableString(txt);
//            if (position >= 9) {
//                textSpan.setSpan(new AbsoluteSizeSpan(100), 0, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            } else {
//                textSpan.setSpan(new AbsoluteSizeSpan(100), 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            }
            myViewHolder.recipe_desc.setText(txt);
            String stepNote = cookSteps.get(position).stepNote;
            if (TextUtils.isEmpty(stepNote)) {
                myViewHolder.tips_show.setVisibility(View.GONE);
            } else {
                myViewHolder.tips_show.setVisibility(View.VISIBLE);
                myViewHolder.tips.setText("小贴士：" + cookSteps.get(position).stepNote);
            }
//            if (iDevice == null){
//                if (position == 0){
//                    myViewHolder.item_bg.setBackgroundColor(Color.parseColor("#EFCE17"));
//                }
//            }
            return convertView;
        }
    }

    public class MyViewHolder {
        public TextView recipe_desc;
        public TextView recipe_cooking_show;
        public ProgressBar recipe_progress;
        public RelativeLayout item_bg;
        public LinearLayout img_pause;
        public CardView card_bg;
        public LinearLayout no_device_status;
        public TextView pause_desc;
        public TextView tips;
        public int pos;
        public ImageView iv_img_pause;
        public LinearLayout tips_show;
        public ImageView tips_img;
        public ImageView ivStop;
        public MyViewHolder(View view) {
            recipe_desc = view.findViewById(R.id.recipe_desc);
            recipe_cooking_show = view.findViewById(R.id.recipe_cooking_show);
            recipe_progress = view.findViewById(R.id.recipe_progress);
            item_bg = view.findViewById(R.id.item_bg);
            img_pause = view.findViewById(R.id.img_pause);
            pause_desc = view.findViewById(R.id.pause_desc);
            card_bg = view.findViewById(R.id.card_bg);
            tips = view.findViewById(R.id.tips);
            tips_show = view.findViewById(R.id.tips_show);
            tips_img = view.findViewById(R.id.tips_img);
            no_device_status = view.findViewById(R.id.no_device_status);
            iv_img_pause = view.findViewById(R.id.iv_img_pause);
            ivStop = view.findViewById(R.id.iv_stop);
        }
    }

}
