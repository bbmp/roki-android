package com.robam.roki.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.util.Util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class GlideRoundTransform extends BitmapTransformation {

    private static final String ID = GlideRoundTransform.class.getName();
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    final GradientDrawable drawable;



    public GlideRoundTransform(GradientDrawable drawable) {
        this.drawable = drawable;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Bitmap transform(
            @NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {

        Bitmap bitmap = TransformationUtils.roundedCorners(pool, toTransform, (int)drawable.getCornerRadius());

        Canvas canvas = new Canvas(bitmap);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);

        canvas.setBitmap(null);
        return bitmap;
    }

    @Override
    public int hashCode() {
        return Util.hashCode(ID.hashCode(), drawable.hashCode());
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);

        {
            byte[] radiusData = ByteBuffer.allocate(4).putInt(drawable.hashCode()).array();
            messageDigest.update(radiusData);
        }

    }
}
