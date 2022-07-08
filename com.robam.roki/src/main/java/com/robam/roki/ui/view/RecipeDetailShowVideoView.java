package com.robam.roki.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.legent.plat.pojos.device.IDevice;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.roki.R;
import com.robam.roki.ui.adapter3.JZMediaIjk;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.video.JzvdStdRoundVolume;
import com.robam.roki.ui.widget.view.PlayerView;
import com.robam.roki.utils.IjkPlayerView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jzvd.JzvdStd;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.robam.roki.R.id.recipe_step_pic;

/**
 * Created by yinwei on 2017/11/29.
 */

public class RecipeDetailShowVideoView extends FrameLayout implements PlayerView.onPlayListener {


    Context cx;
    Recipe recipe;
    int step;
    public CookStep cookStep;//菜谱步骤对象
    public List<String> cookSteps;
    IDevice iDevice;
    JzvdStdRoundVolume jz_video;
    String videoUrl;
    private MultiTransformation options = new MultiTransformation<>(new CenterCrop(),
            new RoundedCornersTransformation(30, 0));

    public RecipeDetailShowVideoView(Context context, List<String> cookSteps, int step, IDevice iDevice) {
        super(context);
        this.cx = context;
        this.cookSteps = cookSteps;
        this.step = step;
        this.iDevice = iDevice;
        init(context);
    }

    public RecipeDetailShowVideoView(Context context, String videoUrl) {
        super(context);
        this.cx = context;
        this.videoUrl = videoUrl;

        init(context);
    }

    private void init(Context cx) {
        View view = LayoutInflater.from(cx).inflate(R.layout.recipe_show_video_view, this, true);
        jz_video = view.findViewById(R.id.jz_video);
        onRefresh();
    }
    public void onRefresh() {
        if (videoUrl != null) {
            jz_video.setUp(videoUrl
                    , "", JzvdStd.SCREEN_NORMAL, JZMediaIjk.class);
            Glide.with(getContext())
                    .setDefaultRequestOptions(
                            new RequestOptions()
                                    .frame(1000000)
                                    .centerCrop()
//                                            .error(R.mipmap.eeeee)//可以忽略
//                                            .placeholder(R.mipmap.ppppp)//可以忽略
                    )
                    .load(videoUrl)
                    .into(jz_video.posterImageView);
        }
    }

    public void  startVideo() throws Exception {
        if (videoUrl != null) {
            jz_video.startVideo();
        }
    }

    public void  pauseVideo() throws Exception {
        jz_video.mediaInterface.pause();
        jz_video.onStatePause();
    }
}
