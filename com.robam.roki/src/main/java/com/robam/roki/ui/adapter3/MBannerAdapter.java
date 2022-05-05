package com.robam.roki.ui.adapter3;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.ui.bean3.BannerBean;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.utils.OnMultiClickListener;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

public class MBannerAdapter extends BannerAdapter<BannerBean.DataDTO ,MBannerAdapter.BannerViewHolder > {

    public MBannerAdapter(List<BannerBean.DataDTO> datas) {
        super(datas);
    }

    @Override
    public BannerViewHolder onCreateHolder(ViewGroup viewGroup, int i) {
        ImageView imageView = new ImageView(viewGroup.getContext());
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new BannerViewHolder(imageView);

    }

    @Override
    public void onBindView(BannerViewHolder bannerViewHolder, BannerBean.DataDTO item, int i, int i1) {
        GlideApp.with(bannerViewHolder.imageView)
                .load(item.imageUrl)
//                .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                .into(bannerViewHolder.imageView);
        if (onItemClicklinstener != null){
            bannerViewHolder.imageView.setOnClickListener(new OnMultiClickListener() {
                @Override
                protected void onMoreClick(View view) {
                    onItemClicklinstener.onItemClick(item);
                }

                @Override
                protected void onMoreErrorClick() {
                    ToastUtils.showShort("请不要连续点击");
                }
            });

        }
//        bannerViewHolder.imageView.setImageResource(data.imageRes);
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public BannerViewHolder(@NonNull ImageView view) {
            super(view);
            this.imageView = view;
        }
    }
    OnItemClicklinstener onItemClicklinstener ;
    public interface OnItemClicklinstener{
        void onItemClick(BannerBean.DataDTO data);
    }
    public void addOnItemClicklinstener(OnItemClicklinstener onItemClicklinstener){
        this.onItemClicklinstener = onItemClicklinstener ;
    }

}
