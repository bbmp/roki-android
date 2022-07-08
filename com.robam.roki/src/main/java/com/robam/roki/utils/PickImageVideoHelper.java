package com.robam.roki.utils;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.google.common.eventbus.Subscribe;
import com.robam.base.BaseDialog;
import com.legent.events.ActivityResultOnPageEvent;
import com.legent.events.ActivityResultOnVideoEvent;
import com.legent.events.PageChangedEvent;
import com.legent.utils.EventUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.ui.mdialog.MenuDialog;
import com.robam.roki.utils.audio.PickUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PickImageVideoHelper {

    public final static String CAMERA_TEXT = "相机";
    public final static String ALBUM_TEXT = "相册";
    public final static String ALBUM_VIDEO = "视频";
    private String PHOTO_FILE_NAME = "output_image.jpg";
    private final int PHOTO_REQUEST_CAREMA = 0;
    private final int PHOTO_REQUEST_GALLERY = 1;
    private final int VIDEO_REQUEST_GALLERY = 2;
    private final int PHOTO_REQUEST_CUT = 3;

    Activity act;
    PickImgCallbackTwo imgCallback;
    PickVideoCallbackTwo videoCallback;
    String strCamera = CAMERA_TEXT;
    String strAlbum = ALBUM_TEXT;
    private Uri cropImageUri;
    private File outputImage;
    private Uri imageUri;

    public PickImageVideoHelper(Activity act, PickImgCallbackTwo imgCallback,PickVideoCallbackTwo videoCallback) {
        this.act = act;
        this.imgCallback = imgCallback;
        this.videoCallback = videoCallback;
    }

    public void setText(String cameraText, String albumText) {
        this.strCamera = cameraText;
        this.strAlbum = albumText;
    }

    @Subscribe
    public void onEvent(ActivityResultOnVideoEvent event) {

        int requestCode = event.requestCode;
        int resultCode = event.resultCode;
        Intent data = event.intent;
        Log.e("20190107", "requestCode:" + requestCode + " resultCode:" + resultCode);
        switch (requestCode) {
            // 如果是直接从相册获取
            case PHOTO_REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    // 从相册返回的数据
                    if (data != null) {
                        // 得到图片的全路径
                        Uri uri = data.getData();
                        crop(uri);//裁剪图片
                    }
                }
                break;
            // 如果是调用相机拍照时
            case PHOTO_REQUEST_CAREMA:
                if (resultCode == RESULT_OK) {
                    crop(imageUri);
                } else {
                    Toast.makeText(act, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                }

                break;
            //视频
            case VIDEO_REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    // 从相册返回的数据
                    if (data != null) {
                        Uri uri = data.getData();
                        // 得到视频的全路径
                        setVideoPicToView(uri);

                    }
                }
                break;
            // 取得裁剪后的图片
            case PHOTO_REQUEST_CUT:// 3
                Log.e("20190107", "取得裁剪后的图片:" + data);
                EventUtils.unregist(PickImageVideoHelper.this);
                setPicToView(data);
                break;
            default:
                break;

        }
    }

    public void showPickDialog(String title) {
        EventUtils.regist(PickImageVideoHelper.this);
//        EventUtils.postEvent(new PageChangedEvent("dialogshow"));
        List<String> data = new ArrayList<>();
        data.add(CAMERA_TEXT);
        data.add(ALBUM_TEXT);
        data.add(ALBUM_VIDEO);

        new MenuDialog.Builder(act)
                .setList(data)
                .setListener(new MenuDialog.OnListener<String>() {

                    @Override
                    public void onSelected(BaseDialog dialog, int position, String string) {
                        if (CAMERA_TEXT.equals(string)) {
                            camera();
                        } else if (ALBUM_TEXT.equals(string)) {
                            gallery();
                        } else if (ALBUM_VIDEO.equals(string)) {
                            choiceVideo();
                        }
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
//                        EventUtils.postEvent(new PageChangedEvent("dialogdismiss"));
                    }
                })
                .show();
    }


    /*
     * 从相机获取
     */
    public void camera() {
        outputImage = new File(act.getExternalCacheDir(), PHOTO_FILE_NAME);

        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
//                    第一个参数Context，第二个参数随便写个字符串，第三个参数就是我们刚刚创建的File对象

            imageUri = FileProvider.getUriForFile(act, "roki", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        //启动相机程序
        PermissionsUtils.checkPermission(act, Manifest.permission.CAMERA, PermissionsUtils.CODE_RECIPE_DETAIL_CAMERA);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");// 设置action
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//指定图片的输出地址
        act.startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }


    /*
     * 从相册获取
     */
    public void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        act.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /*
     * 获取视频
     */
    public void choiceVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*");
        act.startActivityForResult(intent, VIDEO_REQUEST_GALLERY);
    }

    /*
     * 剪切图片
     */
    private void crop(Uri uri) {
        File CropPhoto = new File(act.getExternalCacheDir(), "crop_image.jpg");
        try {
            if (CropPhoto.exists()) {
                CropPhoto.delete();
            }
            CropPhoto.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cropImageUri = Uri.fromFile(CropPhoto);
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.putExtra("crop", "true");
        // 裁剪框的比例，4：3
        intent.putExtra("aspectX", 100);
        intent.putExtra("aspectY", 101);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 600);
        intent.putExtra("circleCrop", "false");
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        act.startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
     */
    protected void setPicToView(Intent data) {

        // 从剪切图片返回的数据
        if (data != null) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(act.getContentResolver()
                        .openInputStream(cropImageUri));
                if (bitmap != null) {
                    imgCallback.onPickComplete(bitmap);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            // 将临时文件删除
            outputImage.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void setVideoPicToView(Uri data) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(PickUtils.getPath(act,data));
            Bitmap bitmap = retriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            if (bitmap != null) {
                videoCallback.onPickComplete(bitmap,PickUtils.getPath(act,data));
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }


    public interface PickImgCallbackTwo {
        void onPickComplete(Bitmap bmp);
    }
    public interface PickVideoCallbackTwo {
        void onPickComplete(Bitmap bmp,String path);
    }
}
