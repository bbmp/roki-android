package com.robam.roki.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import android.util.Log;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.hjq.toast.ToastUtils;
import com.legent.events.ActivityResultOnPageEvent;
import com.legent.events.PageChangedEvent;
import com.legent.utils.EventUtils;
import com.robam.base.BaseActivity;
import com.robam.base.BaseDialog;
import com.robam.roki.net.base.BaseBean;
import com.robam.roki.ui.activity3.ImageSelectActivity;
import com.robam.roki.ui.mdialog.MenuDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.app.Activity.RESULT_OK;

public class PickImageHelperTwo {

    public final static String CAMERA_TEXT = "相机";
    public final static String ALBUM_TEXT = "相册";
    private String PHOTO_FILE_NAME = "output_image.jpg";
    private final int PHOTO_REQUEST_CAREMA = 0x12;
    public static final int PHOTO_REQUEST_GALLERY = 0x125;
    private final int PHOTO_REQUEST_CUT = 0x1;

    Activity act;
    PickCallbackTwo callback;
    String strCamera = CAMERA_TEXT;
    String strAlbum = ALBUM_TEXT;
    private Uri cropImageUri;
    private File outputImage;

    private File cameFile;
    private Uri imageUri;

    private Uri cameraUri;
    /**
     * 允许选择最大的图片张数
     */
    private int maxSelect;

    public PickImageHelperTwo(Activity act, PickCallbackTwo callback) {
        this.act = act;
        this.callback = callback;
    }

    public void setText(String cameraText, String albumText) {
        this.strCamera = cameraText;
        this.strAlbum = albumText;
    }

    @Subscribe
    public void onEvent(ActivityResultOnPageEvent event) {

        int requestCode = event.requestCode;
        int resultCode = event.resultCode;
        Intent data = event.intent;
        Log.e("20190107", "requestCode:" + requestCode + " resultCode:" + resultCode);
        switch (requestCode) {
            // 如果是直接从相册获取
            case PHOTO_REQUEST_GALLERY:// 1
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
            case PHOTO_REQUEST_CAREMA: // 2
                if (resultCode == RESULT_OK) {
                    crop(imageUri);
                } else {
                    Toast.makeText(act, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                }

                break;
            case PHOTO_REQUEST_CAREMA + 1:
                if (resultCode == RESULT_OK) {
                    setPicToViewCame(cameraUri);
                } else {
                    Toast.makeText(act, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                }
                break;
            // 取得裁剪后的图片
            case PHOTO_REQUEST_CUT:// 3
                Log.e("20190107", "取得裁剪后的图片:" + data);
                EventUtils.unregist(PickImageHelperTwo.this);
                setPicToView(data);
                break;
            case (PHOTO_REQUEST_GALLERY + 1):
                if (resultCode == RESULT_OK) {
                    // 从相册返回的数据
                    if (data != null) {

                        Uri uri = data.getData();
                        setPicToView(uri);//直接保存
                    }
                }
                break;

            default:
                break;

        }
    }

    public void showPickDialog(int code, int maxSelect) {
        this.maxSelect = maxSelect;
        EventUtils.regist(PickImageHelperTwo.this);
        EventUtils.postEvent(new PageChangedEvent("dialogshow"));
        List<String> data = new ArrayList<>();
        data.add(CAMERA_TEXT);
        data.add(ALBUM_TEXT);
        new MenuDialog.Builder(act)
                .setList(data)
                .setListener(new MenuDialog.OnListener<String>() {

                    @Override
                    public void onSelected(BaseDialog dialog, int position, String string) {
                        if (CAMERA_TEXT.equals(string)) {
                            if (code != PHOTO_REQUEST_GALLERY) {
                                camera(PHOTO_REQUEST_CAREMA + 1);
                            } else {
                                camera();
                            }
                        } else if (ALBUM_TEXT.equals(string)) {
                            if (code == PHOTO_REQUEST_GALLERY)
                                gallery(PHOTO_REQUEST_GALLERY);
                            else
                                gallery(PHOTO_REQUEST_GALLERY + 1);
                        }
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        EventUtils.postEvent(new PageChangedEvent("dialogdismiss"));
                    }
                })
                .show();

        EventUtils.postEvent(new PageChangedEvent("dialogdismiss"));

    }

    public void showPickDialog(String title) {
        EventUtils.regist(PickImageHelperTwo.this);
        EventUtils.postEvent(new PageChangedEvent("dialogshow"));
        List<String> data = new ArrayList<>();
        data.add(CAMERA_TEXT);
        data.add(ALBUM_TEXT);
        new MenuDialog.Builder(act)
                .setList(data)
                .setListener(new MenuDialog.OnListener<String>() {

                    @Override
                    public void onSelected(BaseDialog dialog, int position, String string) {
                        if (CAMERA_TEXT.equals(string)) {
                            camera();
                        } else if (ALBUM_TEXT.equals(string)) {

                            gallery(PHOTO_REQUEST_GALLERY);
                        }
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        EventUtils.postEvent(new PageChangedEvent("dialogdismiss"));
                    }
                })
                .show();

        EventUtils.postEvent(new PageChangedEvent("dialogdismiss"));

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


    public void camera(int code) {
        if (code == PHOTO_REQUEST_CAREMA + 1) {
            cameFile = new File(act.getExternalCacheDir(), System.currentTimeMillis() + "" + PHOTO_FILE_NAME);
            try {
                if (!cameFile.exists()) {
                    cameFile.createNewFile();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT >= 24) {
//                    第一个参数Context，第二个参数随便写个字符串，第三个参数就是我们刚刚创建的File对象

                cameraUri = FileProvider.getUriForFile(act, "roki", cameFile);
            } else {
                cameraUri = Uri.fromFile(cameFile);
            }
            //启动相机程序
            PermissionsUtils.checkPermission(act, Manifest.permission.CAMERA, PermissionsUtils.CODE_RECIPE_DETAIL_CAMERA);
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");// 设置action
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);//指定图片的输出地址
            act.startActivityForResult(intent, code);
        } else {
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
            act.startActivityForResult(intent, code);
        }
    }

    /*
     * 从相册获取
     */
    public void gallery(int code) {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        if (code == PHOTO_REQUEST_GALLERY) {
            act.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
        } else if (code == PHOTO_REQUEST_GALLERY + 1) {
//            act.startActivityForResult(intent, PHOTO_REQUEST_GALLERY + 1);
            /**
             * 新写支持多选
             */
            if (act instanceof BaseActivity) {
                ImageSelectActivity.start((BaseActivity) act, maxSelect, new ImageSelectActivity.OnPhotoSelectListener() {

                    @Override
                    public void onSelected(List<String> data) {
                        if (callback!=null){
                            callback.onPickComplete((ArrayList<String>) data);
                        }
//                        ToastUtils.show("选择了" + data.toString());
                    }

                    @Override
                    public void onCancel() {
//                        ToastUtils.show("取消了");
                    }
                });
            } else {
                act.startActivityForResult(intent, PHOTO_REQUEST_GALLERY + 1);
            }

        }
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

    protected void setPicToViewCame(Uri data) {
        // 从剪切图片返回的数据
        if (data != null) {

            callback.onPickComplete(cameFile.getAbsolutePath());

        }
    }


    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    protected void setPicToView(Uri data) {

        // 从剪切图片返回的数据
        if (data != null) {

            callback.onPickComplete(getRealFilePath(act, data));

        }
        try {
            // 将临时文件删除
            outputImage.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                    callback.onPickComplete(bitmap);
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

    public interface PickCallbackTwo {
        void onPickComplete(Bitmap bmp);

        void onPickComplete(String bmp);

        default void onPickComplete(ArrayList<String> files){

        };
    }
}
