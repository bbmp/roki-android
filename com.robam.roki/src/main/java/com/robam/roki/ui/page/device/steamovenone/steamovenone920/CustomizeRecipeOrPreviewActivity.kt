package com.robam.roki.ui.page.device.steamovenone.steamovenone920

import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.hjq.toast.ToastUtils
import com.legent.VoidCallback
import com.legent.events.ActivityResultOnPageEvent
import com.legent.utils.EventUtils
import com.robam.roki.R
import com.robam.roki.manage.AliyunOSSManager
import com.robam.roki.manage.ThreadPoolManager
import com.robam.roki.net.OnRequestListener
import com.robam.roki.net.request.bean.MultiStepDto
import com.robam.roki.net.request.bean.RecipeDetailBean
import com.robam.roki.request.api.RecipeApi
import com.robam.roki.request.bean.FlavouringArray
import com.robam.roki.request.param.MaterialDtoList
import com.robam.roki.request.param.RecipeSaveParam
import com.robam.roki.ui.PageArgumentKey
import com.robam.roki.ui.activity3.AppActivity
import com.robam.roki.ui.activity3.stampToDate
import com.robam.roki.ui.dialog.ImageShareDialog.Companion.url
import com.robam.roki.ui.page.curve.DishesPreparedByStepActivity
import com.robam.roki.ui.page.curve.FlavouringActivity
import com.robam.roki.ui.page.recipedetail.RecipeDetailPage
import com.xiaomi.push.it
import com.xiasuhuei321.loadingdialog.view.LoadingDialog
import kotlinx.android.synthetic.main.activity_customize_recipe_or_preview.*
import kotlinx.android.synthetic.main.include_title_bar.*
import kotlinx.android.synthetic.main.item_customize_recipe_view.view.*

/**
 * @Anthor:Tian
 * @Date:2020/10/5
 * @Description:防止双击
 */
abstract class NoDoubleClickListener : View.OnClickListener {
    companion object {
        const val MIN_CLICK_DELAY_TIME = 2000 //两个点击最小间隔
    }

    var lastClickTime: Long = 0
    override fun onClick(v: View?) {
        var curTime = System.currentTimeMillis()
        if (curTime - lastClickTime > MIN_CLICK_DELAY_TIME) { //只有大于才会走逻辑
            lastClickTime = curTime
            noDoubleClick()
        }
    }

    abstract fun noDoubleClick()
}
/**
 *
 * 自定义菜谱以及预览菜谱
 */
class CustomizeRecipeOrPreviewActivity : AppActivity(), OnRequestListener {


    private lateinit var  mRecipeApi: RecipeApi
    override fun getLayoutId(): Int =R.layout.activity_customize_recipe_or_preview


    lateinit var ld:LoadingDialog
    override fun showDialog(){

        runOnUiThread {
         ld= LoadingDialog(this)
        ld.setLoadingText("上传中")
            .setInterceptBack(false)
            .closeSuccessAnim()
            .setRepeatCount(1)
            .show()
        }

    }

    companion object{

        var MULTISTEPDTO="multistepdto"

        var RECIPEBEAN="recipebean"

        var CREATEDATE="createDate"

        var RECIPEDETAILBEAN="RecipeDetailBean"
    }

    var refMultiId=0;

    var guidDevice=""

    var isClick=false;
    lateinit var multiStepDtoList: ArrayList<MultiStepDto>
    lateinit var mRecipeSaveParam:RecipeSaveParam




    override fun initView() {

        guidDevice=intent.getStringExtra(PageArgumentKey.Guid)
        img_back.setOnClickListener {
            finish()
        }
        RecipeDetailPage.setStatusBarColor(activity, R.color.white)
        RecipeDetailPage.setLightStatusBar(
            activity, true
        )
        if (intent.hasExtra(RECIPEBEAN)){
            multiStepDtoList= intent.getSerializableExtra(RECIPEBEAN) as ArrayList<MultiStepDto>
            act_step_text.text =stampToDate(intent.getStringExtra(CREATEDATE),"yyyy-MM-dd HH:mm:ss")
        }
        tv_title.text = getString(R.string.customize_recipe)
        mRecipeApi = RecipeApi(this)

        refMultiId = intent.getIntExtra(MULTISTEPDTO, 1);
        act_customize_recipe_view.setContext()
        act_recipe_cook_list.setOnClickListener {
            var intent=Intent(this, RecipeListActivity::class.java)
            intent.putExtra(PageArgumentKey.Guid,guidDevice)
            intent.putExtra(RecipeListActivity.ISCHOOSE,true)
            startActivityForResult(intent,0x123)
        }

        act_save_recipe.setOnClickListener(object : NoDoubleClickListener() {
            override fun noDoubleClick() {
                var name=act_customize_recipe_et.text.toString();
                var mCoverImage = act_customize_recipe_view.getImagePath()

                if (TextUtils.isEmpty(mCoverImage)){
                    ToastUtils.show("请上传封面图片")
                    return
                }
                var materialDtoList = act_customize_material.getMaterialData()
                if (materialDtoList==null||materialDtoList.size==0){
                    ToastUtils.show("请至少选择一种食材")
                    return
                }
                materialDtoList.forEach {
                    if (TextUtils.isEmpty(it.materialName)){
                        ToastUtils.show("请输入食材名称")
                        return
                    }
                    if (TextUtils.isEmpty(it.weight)){
                        ToastUtils.show("请输入食材数量")
                        return
                    }
                }
                var recipeStepStep = act_customize_step.getRecipeStepStep()
                if (recipeStepStep==null||recipeStepStep.size==0){
                    ToastUtils.show("请至少添加一个步骤")
                    return
                }

                recipeStepStep.forEach {
                    if (!TextUtils.isEmpty(it.stepImg)) {

                    }else{
                        ToastUtils.show("请上传步骤图片")
                        return

                    }

                    if (TextUtils.isEmpty(it.description)){
                        ToastUtils.show("请输入步骤描述")
                        return
                    }
                }
                if (TextUtils.isEmpty(act_step_text.text.toString())){
                    ToastUtils.show("未导入烹饪记录")
                    return
                }
                ThreadPoolManager.getInstance().execute {
                    var url = AliyunOSSManager.getInstance().uploadFile(
                        AliyunOSSManager.getOSSImgName(
                            "cover",
                            System.currentTimeMillis().toString()
                        ), mCoverImage
                    )
                    Log.e("地址1",mCoverImage+"0000"+url);
                    recipeStepStep.forEach {
                        var path=AliyunOSSManager.getOSSImgName(
                            "920step",
                            System.currentTimeMillis().toString()
                        )
                        it.stepImg = AliyunOSSManager.getInstance().uploadFile(
                            path, it.stepImg
                        )
                    }
                    //清除额外的数据
                    mRecipeSaveParam = RecipeSaveParam(
                        url, materialDtoList,
                        name, refMultiId, recipeStepStep,guidDevice
                    )
                    materialDtoList.forEach {
                        it.mUnitsList?.clear()
                    }
                    if (!isClick) {
                        isClick=true;
                        showDialog()
                        mRecipeApi.setSaveRecipeList(
                            R.layout.activity_recipte_list,
                            mRecipeSaveParam
                        )
                    }
                }
            }
        })

        act_customize_device_name.text ="蒸烤一体机·CQ920"
        act_customize_preview.setOnClickListener {
                var mCoverImage = act_customize_recipe_view.getImagePath()
                var materialDtoList = act_customize_material.getMaterialData()
                var recipeStepStep = act_customize_step.getRecipeStepStep()

                if (TextUtils.isEmpty(mCoverImage)){
                    ToastUtils.show("请上传封面图片")
                    return@setOnClickListener
                }

                if (materialDtoList==null||materialDtoList.size==0){
                    ToastUtils.show("请至少选择一种食材")
                    return@setOnClickListener
                }
                materialDtoList.forEach {
                    if (TextUtils.isEmpty(it.materialName)){
                        ToastUtils.show("请输入食材名称")
                        return@setOnClickListener
                    }
                    if (TextUtils.isEmpty(it.weight)){
                        ToastUtils.show("请输入食材数量")
                        return@setOnClickListener
                    }
                }

                if (recipeStepStep==null||recipeStepStep.size==0){
                    ToastUtils.show("请至少添加一个步骤")
                    return@setOnClickListener
                }

                recipeStepStep.forEach {
                    if (!TextUtils.isEmpty(it.stepImg)) {

                    }else{
                        ToastUtils.show("请上传步骤图片")
                        return@setOnClickListener

                    }

                    if (TextUtils.isEmpty(it.description)){
                        ToastUtils.show("请输入步骤描述")
                        return@setOnClickListener
                    }
                }
                if (TextUtils.isEmpty(act_step_text.text.toString())){
                    ToastUtils.show("未导入烹饪记录")
                    return@setOnClickListener
                }

                var mIntent = Intent(this, RecipePreviewActivity::class.java)

                mRecipeSaveParam = RecipeSaveParam(mCoverImage, materialDtoList,
                    act_customize_recipe_et.text.toString(), refMultiId, recipeStepStep, guidDevice)
                mIntent.putExtra(RECIPEDETAILBEAN, mRecipeSaveParam)
                mIntent.putExtra(PageArgumentKey.Guid, guidDevice)
                mIntent.putExtra(RecipePreviewActivity.RECIPEPREVIEW,multiStepDtoList)
                startActivityForResult(mIntent,0x1234)
            }
            act_customize_recipe_et.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 0) {
                        act_customize_preview.isEnabled = false;
                        act_save_recipe.isEnabled = false;
                    } else {
                        act_save_recipe.isEnabled = true;
                        act_customize_preview.isEnabled = true;
                    }
                }

            })



        if (intent.hasExtra(MULTISTEPDTO)) {
            mRecipeApi.getRecipeDetail(R.id.act_customize_preview, refMultiId)
        }

    }
    override fun initData() {}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EventUtils.postEvent(ActivityResultOnPageEvent(requestCode, resultCode, data))
        if (resultCode == RESULT_OK) {
            if (requestCode == DishesPreparedByStepActivity.FOODCOOD) {
                var mFoodData = data?.getSerializableExtra(FlavouringActivity.FLAVOURING) as FlavouringArray

                var mMaterialDtoList:MaterialDtoList?=null

                if (mFoodData.units==null||mFoodData.units?.size == 0) {
                    mMaterialDtoList =
                        mFoodData.id?.let {
                            MaterialDtoList(
                                it,
                                mFoodData.name.toString(),
                                1,
                                56,
                                "g",
                                "")
                        }

                } else {
                    mMaterialDtoList = mFoodData.units?.get(0)?.id?.let {
                        mFoodData.id?.let { it1 ->
                            MaterialDtoList(
                                it1,
                                mFoodData.name.toString(),
                                1,
                                it,
                                mFoodData?.units?.get(0)?.name.toString(),
                                ""
                            )
                        }


                    }

                    if (mMaterialDtoList==null){
                        mMaterialDtoList= MaterialDtoList(null,
                            mFoodData.name.toString(),
                            1,
                            mFoodData.units?.get(0)?.id!!,
                            mFoodData?.units?.get(0)?.name.toString(),
                            ""
                        )
                    }


                }
                mFoodData.units?.let { mMaterialDtoList?.mUnitsList?.addAll(it) }
                act_customize_material.setVisiable()
                if (mMaterialDtoList != null) {
                    act_customize_material.addMaterial(mMaterialDtoList)
                }

            }else if (requestCode == 0x123){
                var mData=data?.getSerializableExtra(MULTISTEPDTO) as com.robam.roki.net.request.bean.Data
                multiStepDtoList= mData.multiStepDtoList
                refMultiId=mData.id
                if (multiStepDtoList.size>0) {
                    act_step_text.text = stampToDate( mData.createDateTime,"yyyy-MM-dd HH:mm:ss")
                }

        }else if (requestCode==0x1234){
              setResult(RESULT_OK)
              finish()
            }
    }
    }



    lateinit var mRecipeDetailBean:RecipeDetailBean
    override fun onFailure(requestId: Int, requestCode: Int, msg: String?, data: Any?) {
        when(requestId){
        R.layout.activity_recipte_list->{
            isClick=false
            msg?.let {
                ToastUtils.show(it)

            }

//            finish()
        }
        }
    }
    override fun onSaveCache(requestId: Int, requestCode: Int, paramObject: Any?) {}
    override fun onSuccess(requestId: Int, requestCode: Int, paramObject: Any?) {
        when(requestId){
            R.layout.activity_recipte_list->{
                isClick=false

                runOnUiThread {
                    ld.close()
                    ToastUtils.show("保存成功")
                    setResult(RESULT_OK)
                    finish()
                }
            }
            R.id.act_customize_preview->{
                paramObject?.let {
                    if (paramObject is RecipeDetailBean){
                        this.mRecipeDetailBean=paramObject
                        paramObject.payload.apply {
                                act_customize_recipe_et.setText(name)
                                act_customize_recipe_view.setImageview(coverImg)
                                act_customize_material.setMaterialData(materialDtoList)

                                act_customize_step.setRecipeStepStep(stepDtoList)
                                act_customize_device_name.text=deviceTypeName
                            }
                        }
                        }

                    }
                }
    }

//    private fun isPreviewUI(){
//        act_save_recipe.isEnabled = true;
//        act_customize_preview.visibility = View.GONE
//        act_customize_material.setPreviewState(true)
//        act_customize_step.setPreviewState(true)
//        act_recipe_must_have.visibility = View.INVISIBLE
//        icon_must_have_record.visibility = View.VISIBLE
//        act_customize_recipe_view.isEnabled = false
//        act_customize_recipe_view.setDelete()
//        act_customize_recipe_et.isEnabled = false
//        act_customize_material.isEnabled = false
//        act_customize_material.setMaterial(true)
//        act_customize_step.isEnabled = false
//    }
}
