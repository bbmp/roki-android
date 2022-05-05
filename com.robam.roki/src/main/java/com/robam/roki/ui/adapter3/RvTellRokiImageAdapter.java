package com.robam.roki.ui.adapter3;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.robam.common.pojos.Recipe;
import com.robam.common.util.NumberUtil;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.ui.extension.GlideApp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;
import java.util.List;

import jp.wasabeef.glide.transformations.MaskTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author r210190
 * des 告诉Roki图片adapter
 */
public class RvTellRokiImageAdapter extends BaseQuickAdapter<File, BaseViewHolder> {

    private MultiTransformation options = new MultiTransformation<>(new CenterCrop(),
            new RoundedCornersTransformation(30, 0));

    public RvTellRokiImageAdapter() {
        super(R.layout.item_tell_roki_image);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, File item) {
        if (item != null){
            ImageView ivTagRecipe = (ImageView) holder.getView(R.id.iv_tell_roki_camera);
            GlideApp.with(getContext())
                    .load(item)
                    .apply(RequestOptions.bitmapTransform(options))
                    .into(ivTagRecipe);
            holder.setVisible(R.id.iv_close , true);
        }else {
            ImageView ivTagRecipe = (ImageView) holder.getView(R.id.iv_tell_roki_camera);
            GlideApp.with(getContext())
                    .load(R.mipmap.icon_tell_roki_camera)
                    .apply(RequestOptions.bitmapTransform(options))
                    .into(ivTagRecipe);
            holder.setVisible(R.id.iv_close , false);
            if (holder.getLayoutPosition() == 3){
                holder.setVisible(R.id.iv_tell_roki_camera , false);
            }else {
                holder.setVisible(R.id.iv_tell_roki_camera , true);
            }
        }
    }

//    @Override
//    public void setList(@Nullable Collection<? extends File> list) {
//        super.setList(list);
//    }

//    public static String getFilePathByUri(Context context, Uri uri) {
//        String path = null;
//        // 4.4及之后的 是以 content:// 开头的，比如 content://com.android.providers.media.documents/document/image%3A235700
//        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (DocumentsContract.isDocumentUri(context, uri)) {
//                if (isExternalStorageDocument(uri)) {
//                    // ExternalStorageProvider
//                    final String docId = DocumentsContract.getDocumentId(uri);
//                    final String[] split = docId.split(":");
//                    final String type = split[0];
//                    if ("primary".equalsIgnoreCase(type)) {
//                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
//                        return path;
//                    }
//                } else if (isDownloadsDocument(uri)) {
//                    // DownloadsProvider
//                    final String id = DocumentsContract.getDocumentId(uri);
//                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
//                            Long.valueOf(id));
//                    path = getDataColumn(context, contentUri, null, null);
//                    return path;
//                } else if (isMediaDocument(uri)) {
//                    // MediaProvider
//                    final String docId = DocumentsContract.getDocumentId(uri);
//                    final String[] split = docId.split(":");
//                    final String type = split[0];
//                    Uri contentUri = null;
//                    if ("image".equals(type)) {
//                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                    } else if ("video".equals(type)) {
//                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                    } else if ("audio".equals(type)) {
//                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                    }
//                    final String selection = "_id=?";
//                    final String[] selectionArgs = new String[]{split[1]};
//                    path = getDataColumn(context, contentUri, selection, selectionArgs);
//                    return path;
//                }
//            }
//        }else {
//            // 以 file:// 开头的
//            if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
//                path = uri.getPath();
//                return path;
//            }
//            // 以 content:// 开头的，比如 content://media/extenral/images/media/17766
//            if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//                Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
//                if (cursor != null) {
//                    if (cursor.moveToFirst()) {
//                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                        if (columnIndex > -1) {
//                            path = cursor.getString(columnIndex);
//                        }
//                    }
//                    cursor.close();
//                }
//                return path;
//            }
//        }
//        return null;
//    }

}
