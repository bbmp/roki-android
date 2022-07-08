package com.robam.roki.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import com.google.common.collect.Lists;
import com.j256.ormlite.stmt.query.In;
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
import com.robam.roki.utils.IjkPlayerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 自动烹饪 new
 */

public class RecipeDetailAuto2View extends FrameLayout implements ViewPager.OnPageChangeListener {
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
    private int type = 0;
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


    public RecipeDetailAuto2View(Context context, ArrayList<CookStep> cookStepTemp, IDevice iDevice) {
        super(context);
        this.cx = context;
        this.iDevice = iDevice;
        this.cookStepTemp = cookStepTemp;
        this.cookStep = cookStepTemp.get(0);
        init(context);
    }

    public RecipeDetailAuto2View(Context context, ArrayList<CookStep> cookStepTemp, IDevice iDevice, int type) {
        super(context);
        this.cx = context;
        this.iDevice = iDevice;
        this.cookStepTemp = cookStepTemp;
        this.cookStep = cookStepTemp.get(0);
        this.type = type;
        init(context);
    }


    public RecipeDetailAuto2View(Context context, ArrayList<CookStep> cookStepTemp, IDevice iDevice, int type, int step) {
        super(context);
        this.cx = context;
        this.iDevice = iDevice;
        this.cookStepTemp = cookStepTemp;
        this.cookStep = cookStepTemp.get(0);
        this.type = type;
        this.step = step;
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
        recipeStepShow.setSelection(step);

        //参数设置页面 无设备不显示参数
        modeShow.setVisibility(INVISIBLE);
        //图片展示的页面
        if (type == 0) {
            View rl_image_video = view.findViewById(R.id.rl_image_video);
            if (cookStepTemp.get(0).stepVideo != null) {
//                setViewLayoutParams(rl_image_video, 210);
//                setViewLayoutParams(rl_image_video);
                float scale = getScale(cookStepTemp.get(0).stepVideo);
                if (scale != 0) {
                    WindowManager wm = (WindowManager) getContext()
                            .getSystemService(Context.WINDOW_SERVICE);
                    int width = wm.getDefaultDisplay().getWidth();
                    setViewLayoutParams(rl_image_video, (int) ((width - DensityUtil.dip2px(getContext(), 30)) * scale));
                } else {
                    setViewLayoutParams(rl_image_video, DensityUtil.dip2px(getContext(), 210));
                }
            } else {
                if (cookStepTemp.get(0).imageUrl != null && !TextUtils.isEmpty(cookStepTemp.get(0).imageUrl)) {
                    setViewLayoutParams(rl_image_video, DensityUtil.dip2px(getContext(), 350));

                } else {
                    rl_image_video.setVisibility(GONE);
                }
            }
        } else {
            View rl_image_video = view.findViewById(R.id.rl_image_video);
            if (cookStepTemp.get(0).stepVideo != null) {
                float scale = getScale(cookStepTemp.get(0).stepVideo);
                if (scale != 0) {
                    WindowManager wm = (WindowManager) getContext()
                            .getSystemService(Context.WINDOW_SERVICE);
                    int width = wm.getDefaultDisplay().getWidth();
                    setViewLayoutParams(rl_image_video, (int) ((width / 2 - DensityUtil.dip2px(getContext(), 30)) * scale));
                } else {
                    setViewLayoutParams(rl_image_video, DensityUtil.dip2px(getContext(), 210));
                }
//                setViewLayoutParams(rl_image_video);
            } else {
//                setViewLayoutParams(rl_image_video, 350);

            }
        }
        adapter = new ExtPageAdapter();//构建adapter
        showImg.setAdapter(adapter);
        list = Lists.newArrayList();
        for (CookStep step : cookStepTemp) {
            if (step.stepVideo != null) {
                list.add(new RecipeDetailShowVideoView(cx, step.stepVideo));
            } else {
                list.add(new RecipeDetailShowImgView(cx, step.imageUrl));
            }
        }
        adapter.loadViews(list);
        showImg.setCurrentItem(step);
        startVideo(step);
    }


    /**
     * 重设 view 的高度
     */
    public void setViewLayoutParams(View view, int nHeight) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
//        int height = DensityUtil.dip2px(getContext(), nHeight);
        if (lp.height != nHeight) {
            lp.height = nHeight;
            view.setLayoutParams(lp);
        }
    }

    /**
     * 重设 view 的高度
     */
    public void setViewLayoutParams(View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        int i = lp.width * 1080 / 1920;
        if (lp.height != i) {
            lp.height = i;
            view.setLayoutParams(lp);
        }
    }

    private float getScale(String url) {
        Pattern pattern = Pattern.compile("w(\\d+)h(\\d+).mp4");//正则表达式
        Matcher matcher = pattern.matcher(url);
//判断是否匹配到子串
        if (matcher.find()) {
            //宽(w)=1980,高(h)=1080
            String w = matcher.group(1);
            String h = matcher.group(2);
            LogUtils.i("getScale", w + "-----" + h);
            return Float.parseFloat(h) / Float.parseFloat(w);
        } else {
            return 0;
        }
    }

    private void startVideo(int step) {
        try {
            if (list.get(step) instanceof RecipeDetailShowVideoView) {
                ((RecipeDetailShowVideoView) list.get(step)).startVideo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        this.step = step;
        cardAdapter.notifyDataSetChanged();
        isCanH(step);
        LogUtils.i("202010108888", "recipeStepShow::::step:::::" + step);
        recipeStepShow.setSelection(step);

    }


    public void onfreshViewRevert(int step) {
        for (int i = 0; i <= step; i++) {
            View view = lmap.get(i);
            if (view == null || view.getTag() == null) {
                return;
            }
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
            myViewHolder.recipe_cooking_show.setText("");
        }

    }


    public void onfreshNoDeviceView(int step) {
        View view = lmap.get(step);
        MyViewHolder myViewHolder = (MyViewHolder) view.getTag();
        myViewHolder.item_bg.setBackgroundResource(R.drawable.item_bg_conner_no_device_select);
        myViewHolder.img_pause.setVisibility(View.VISIBLE);
        myViewHolder.iv_img_pause.setVisibility(View.INVISIBLE);
        myViewHolder.pause_desc.setVisibility(View.INVISIBLE);
        myViewHolder.card_bg.setCardBackgroundColor(Color.parseColor("#4D000000"));
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
        public int position = -1;

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

            if (position != step) {
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
//                myViewHolder.recipe_cooking_show.setText("已完成");
            } else {
                myViewHolder.item_bg.setBackgroundResource(R.drawable.item_bg_conner_no_device_select);
                myViewHolder.img_pause.setVisibility(View.VISIBLE);
                myViewHolder.iv_img_pause.setVisibility(View.INVISIBLE);
                myViewHolder.pause_desc.setVisibility(View.INVISIBLE);
                myViewHolder.card_bg.setCardBackgroundColor(Color.parseColor("#4D000000"));
                myViewHolder.recipe_cooking_show.setVisibility(GONE);
                myViewHolder.recipe_progress.setVisibility(View.INVISIBLE);
                myViewHolder.recipe_desc.setTextColor(Color.parseColor("#000000"));
                myViewHolder.no_device_status.setVisibility(VISIBLE);
            }

            String txt = (position + 1) + "/" + cookSteps.size() + " " + cookSteps.get(position).desc;

            myViewHolder.recipe_desc.setText(txt);
            String stepNote = cookSteps.get(position).stepNote;
            if (TextUtils.isEmpty(stepNote)) {
                myViewHolder.tips_show.setVisibility(View.GONE);
            } else {
                myViewHolder.tips_show.setVisibility(View.VISIBLE);
                myViewHolder.tips.setText("小贴士：" + cookSteps.get(position).stepNote);
            }
            myViewHolder.recipe_cooking_show.setVisibility(INVISIBLE);
            myViewHolder.no_device_status.setVisibility(INVISIBLE);
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
