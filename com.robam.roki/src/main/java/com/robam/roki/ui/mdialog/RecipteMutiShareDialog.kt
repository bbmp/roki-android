package com.robam.roki.ui.mdialog

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.legent.plat.Plat
import com.legent.plat.pojos.device.IDevice
import com.legent.plat.services.DeviceService
import com.legent.utils.qrcode.QrUtils
import com.robam.roki.R
import com.robam.roki.db.model.RecipeStepBean
import com.robam.roki.ui.activity3.stampToDate
import com.robam.roki.ui.adapter3.Rv610RecipeStepAdapter
import kotlinx.android.synthetic.main.recipte_mutil_share_diaog_layout.*
import java.io.File

class RecipteMutiShareDialog(context: Context,var recipeName:String,
                             var list:List<RecipeStepBean>,var url:String="https://www.zhihu.com/",var guid:String) :Dialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipte_mutil_share_diaog_layout)
        dialog_recipe_name_user.text=Plat.accountService.currentUser.name
        dialog_recipe_date_user.text=stampToDate(System.currentTimeMillis().toString(),"yyyy/MM/dd")
        dialog_recipe_name.text=recipeName
        Plat.accountService.currentUser.figureUrl?.let {
            Glide.with(context).load(it).into(dialog_recipe_icon)
        }

        if (guid.contains("920")){
            Glide.with(context).load(R.mipmap.device_920_image).into(dialog_recipe_device_img)
        }else if (guid.contains("620")){
            Glide.with(context).load(R.mipmap.icon_620_device).into(dialog_recipe_device_img)
        }else if (guid.contains("610")){
            Glide.with(context).load(R.drawable.ic_device_610).into(dialog_recipe_device_img)
        }

        var idevice=DeviceService.getInstance().queryById<IDevice>(guid)

        if (idevice!=null) {
            dialog_recipe_device_name.text = idevice.categoryName + "·" + idevice.dispalyType
        }
        this.window?.decorView?.isDrawingCacheEnabled = true;
        this.window?.decorView?.buildDrawingCache();

        rl_share_recipe.isDrawingCacheEnabled = true

        val rv610StepAdapter = Rv610RecipeStepAdapter()
        dialog_recipe_step.adapter=rv610StepAdapter
        dialog_recipe_step.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        rv610StepAdapter.addData(list)
        dialog_recipe_qccode.post {
            Glide.with(context).load(QrUtils.create2DCode(url)).into(dialog_recipe_qccode)
        }

    }



//    public fun  saveImg() {
////mCardView为被截图view
//        rl_share_recipe.setDrawingCacheEnabled(true);
//    Bitmap drawingCache = mCardView.getDrawingCache();
//    drawingCache = Bitmap.createBitmap(drawingCache);
//    mCardView.setDrawingCacheEnabled(false);
//
//    File file = new File(FileUtils.getImageCachePath(getContext()), UUID.randomUUID() + ".jpg");
//    ImageUtils.writeBitmap(file.getAbsolutePath(), drawingCache, 100);
//    String dbUri = CommonUtils.saveToDb(getContext(), file.getAbsolutePath());
//
//    if (dbUri != null) {
//        ToastUtil.show(getContext(), R.string.save_image_success);
//    } else {
//        ToastUtil.show(getContext(), R.string.save_image_fail);
//    }
//
//
//
//    }





}