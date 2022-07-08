package com.robam.roki.ui.page.curve

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.HandlerCompat.postDelayed
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

import com.legent.events.ActivityResultOnPageEvent

import com.legent.utils.EventUtils
import com.legent.utils.api.ToastUtils
import com.robam.roki.R
import com.robam.roki.manage.AliyunOSSManager
import com.robam.roki.manage.ThreadPoolManager
import com.robam.roki.net.OnRequestListener
import com.robam.roki.request.api.FlavouringApi
import com.robam.roki.request.api.RecipeSuccessApi
import com.robam.roki.request.api.UploadFileApi
import com.robam.roki.request.bean.*
import com.robam.roki.ui.activity3.AppActivity
import com.robam.roki.ui.adapter.DishedPrepareStepAdapter
import com.robam.roki.ui.adapter.DishedPreparedAdapter
import com.robam.roki.ui.adapter.IChooseImage
import com.robam.roki.ui.adapter.getTimeFormat
import com.robam.roki.ui.form.MainActivity.activity
import com.robam.roki.ui.page.curve.FlavouringActivity.Companion.FLAVOURING
import com.robam.roki.ui.page.recipedetail.RecipeDetailPage
import com.robam.roki.utils.PermissionsUtils
import com.robam.roki.utils.PickImageHelperTwo
import com.robam.roki.utils.audio.IOnTouchListener
import com.robam.roki.utils.audio.player.PlayerCallback
import com.robam.roki.utils.audio.player.PlayerState
import com.robam.roki.utils.audio.player.RokiMediaPlayer
import com.xiaomi.push.it
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener
import com.zlw.main.recorderlib.RecordManager
import com.zlw.main.recorderlib.recorder.RecordHelper
import com.zlw.main.recorderlib.recorder.listener.RecordStateListener
import kotlinx.android.synthetic.main.activity_dishes_prepared_by_step.*
import kotlinx.android.synthetic.main.include_title_bar.*
import kotlinx.android.synthetic.main.widget_add_cooked_step_view.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DishesPreparedByStepActivity : AppActivity(), OnRequestListener, IChooseImage {
    lateinit var pickHelper: PickImageHelperTwo
    lateinit var mUploadFileApi: UploadFileApi
    var mRecipeCurveSuccessBean: RecipeCurveSuccessBean?=null
    private var simpleAudioPlayer = RokiMediaPlayer()

    lateinit var mFlavouringApi: FlavouringApi
    private fun playAudio(path: String) {
        simpleAudioPlayer.play(path)
        simpleAudioPlayer.addCallback(object : PlayerCallback {
            override fun onPlay() {
                Log.e(TAG, "onPlay----")
            }

            override fun onProgress(progress: Int) {
                Log.e(TAG, "$progress----")
                mDishedPrepareStepAdapter.data[mPosAudio].status=3
                mDishedPrepareStepAdapter.data[mPosAudio].playTime=progress;
                runOnUiThread {
                    mDishedPrepareStepAdapter.notifyItemChanged(mPosAudio,"1")
                }
            }

            override fun onPause() {

            }

            override fun onStop() {
                Log.e(TAG, "onStop----")
            }
        })

    }
    companion object {
        val FOODCOOD = 0x122;
        val SEASONCOOD = 0x128;
        val UPDATECURVESTEBACK= "updateCurveBack"
        val UPDATECURVESTEPREQUEST = "updateCurveStepRequest"
        val ID = "id"
    }
    private lateinit var mRecipeSuccessApi: RecipeSuccessApi
    var model=0
    lateinit var  mDishedPreparedAdapter:DishedPreparedAdapter
    var mListDataFood:ArrayList<RecipeCurveSuccessBean.MaterialList> = ArrayList()

    override fun getLayoutId(): Int {
      return R.layout.activity_dishes_prepared_by_step
    }
    private fun initFood(){
        mListDataFood.clear()
        mRecipeCurveSuccessBean?.payload?.let {
            it.materialList?.let { list ->
                for (curveMaterialDtoList in list) {
                    if (curveMaterialDtoList.materialType == 3) {
                        mListDataFood.add(curveMaterialDtoList)
                    }
                }
            }
        }?:run{
            mRecipeCurveSuccessBean?.payload?.materialList= ArrayList()
        }


            context?.let {  context->

                if ( page_dishes_prepare_list_by_step_food.adapter==null) {
                    mDishedPreparedAdapter= DishedPreparedAdapter(this,mListDataFood,mUnitListBean)

                    page_dishes_prepare_list_by_step_food.layoutManager = LinearLayoutManager(context)
                    val menuCreator = SwipeMenuCreator { _, rightMenu, _ ->
                        val deleteItem = SwipeMenuItem(context)
                        var mDrawable = resources.getDrawable(R.drawable.layer_list_delete_drawable);
                        deleteItem
                                .setBackground(mDrawable)
                                .setTextColor(Color.argb(0xff, 0xC6, 0xC7, 0xC9)) // 文字颜色。
                                .setTextSize(15) // 文字大小。
                                .setWidth(resources.getDimension(R.dimen.dp_35).toInt()).height = ViewGroup.LayoutParams.MATCH_PARENT
                        rightMenu.addMenuItem(deleteItem)
                    }
                    val mMenuItemClickListener =
                            SwipeMenuItemClickListener { swipeMenuBridge ->
                                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                                swipeMenuBridge.closeMenu()
                                val direction = swipeMenuBridge.direction // 左侧还是右侧菜单。
                                val adapterPosition = swipeMenuBridge.adapterPosition // RecyclerView的Item的position。
                                val menuPosition = swipeMenuBridge.position // 菜单在RecyclerView的Item中的Position。
                                when (menuPosition) {
                                    0 -> {
                                        mListDataFood.removeAt(adapterPosition)
                                        mDishedPreparedAdapter.notifyDataSetChanged()
                                    }
                                }
                            }

                    page_dishes_prepare_list_by_step_food.setSwipeMenuCreator(menuCreator)
                    page_dishes_prepare_list_by_step_food.setSwipeMenuItemClickListener(mMenuItemClickListener)
                    page_dishes_prepare_list_by_step_food.adapter = mDishedPreparedAdapter;
                }else{
                    mDishedPreparedAdapter.notifyDataSetChanged()
                }
            }



            page_add_food_material.setOnClickListener {
                mListDataFood.add(RecipeCurveSuccessBean.MaterialList(3))
                mDishedPreparedAdapter.notifyDataSetChanged()
            }




    }

    private  val TAG = "DishesPreparedByStepAct"

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EventUtils.postEvent(ActivityResultOnPageEvent(requestCode, resultCode, data))
        Log.e(TAG,"onActivityResult$requestCode---$resultCode")
        if (resultCode== RESULT_OK){
            if (requestCode==FOODCOOD){

                var mFoodData=data?.getSerializableExtra(FLAVOURING) as FlavouringArray

                var pos=data?.getIntExtra(FlavouringActivity.POSITION,0);
                mListDataFood[pos].materialName=mFoodData.name
                mListDataFood[pos].id=mFoodData.id.toString()
                if (mFoodData.units==null||mFoodData.units?.size==0) {
                    mListDataFood[pos].unitName = "g"
                    mListDataFood[pos].unitId = "1"
                }else{

                    mFoodData.units?.get(0).let {

                        mListDataFood[pos].unitName = it?.name
                        mListDataFood[pos].unitId = it?.id.toString()
                    }
                }


//                mListDataFood[pos].units?.apply {
//                        clear()
//                        mFoodData?.units?.let { addAll(it) }
//                } ?: run {
//                    mListDataFood[pos].units = ArrayList()
//                    mFoodData?.units?.let {
//                        mListDataFood[pos].units?.addAll(it)
//                    }
//                }

                mFoodData?.units?.let { mListDataFood[pos].units?.addAll(it) }

                mDishedPreparedAdapter.notifyDataSetChanged()

            }else if(requestCode==SEASONCOOD){
                var mSeaonData=data?.getSerializableExtra(FLAVOURING) as FlavouringArray
                var pos=data?.getIntExtra(FlavouringActivity.POSITION,0);
                mListSeason[pos].materialName=mSeaonData.name.toString()
                mListSeason[pos].id=mSeaonData.id.toString()
                if (mListSeason[pos].units==null||mListSeason[pos].units?.size==0) {
                    mListSeason[pos].unitName = "g"
                    mListSeason[pos].unitId = "1"
                }else{
                    mListSeason[pos].units?.get(0).let {

                        mListSeason[pos].unitName = it?.name
                        mListSeason[pos].unitId = it?.id.toString()
                    }
                }
//                mListSeason[pos]?.units?.let {
//                    if (it.size >0) {
//                        mListSeason[pos].units?.apply {
//                            clear()
//                            mSeaonData?.units?.let { addAll(it) }
//                        } ?: run {
//                            mListSeason[pos].units = ArrayList()
//                            mSeaonData?.units?.let {
//                                mListSeason[pos].units?.addAll(it)
//                            }
//                        }
//                    }
//                }
                mSeaonData?.units?.let { mListSeason[pos].units?.addAll(it) }
                mDishedPreparedSeasonAdapter.notifyDataSetChanged()
            }
        }


    }
    lateinit var mDishedPreparedSeasonAdapter:DishedPreparedAdapter
    var mListSeason:ArrayList<RecipeCurveSuccessBean.MaterialList> = ArrayList();
    private fun initSeason(){
        mListSeason.clear()
        mRecipeCurveSuccessBean?.payload?.materialList?.let {
            for (curveMaterialDtoList in it) {
                if (curveMaterialDtoList.materialType ==1){
                    mListSeason.add(curveMaterialDtoList)
                }
            }
        }?:run{
            mRecipeCurveSuccessBean?.payload?.materialList= ArrayList()
        }


            context?.let {  context->
                if (page_dishes_prepare_list_by_step_seasoning.adapter==null) {
                    mDishedPreparedSeasonAdapter = DishedPreparedAdapter(this, mListSeason,mUnitListBean)

                    val menuCreator = SwipeMenuCreator { _, rightMenu, _ ->
                        val deleteItem = SwipeMenuItem(context)
                        var mDrawable = resources.getDrawable(R.drawable.layer_list_delete_drawable);
                        deleteItem
                                .setBackground(mDrawable)
                                .setTextColor(Color.WHITE) // 文字颜色。
                                .setTextSize(15) // 文字大小。
                                .setWidth(140).height = ViewGroup.LayoutParams.MATCH_PARENT
                        rightMenu.addMenuItem(deleteItem)
                    }


                    val mMenuItemClickListener =
                            SwipeMenuItemClickListener { swipeMenuBridge ->
                                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                                swipeMenuBridge.closeMenu()
                                val direction = swipeMenuBridge.direction // 左侧还是右侧菜单。
                                val adapterPosition = swipeMenuBridge.adapterPosition // RecyclerView的Item的position。
                                val menuPosition = swipeMenuBridge.position // 菜单在RecyclerView的Item中的Position。
                                when (menuPosition) {
                                    0 -> {
                                        mListSeason.removeAt(adapterPosition)
                                        mDishedPreparedSeasonAdapter.notifyDataSetChanged()
                                    }
                                }
                            }
                    page_dishes_prepare_list_by_step_seasoning.setSwipeMenuItemClickListener(mMenuItemClickListener)
                    page_dishes_prepare_list_by_step_seasoning.setSwipeMenuCreator(menuCreator)
                    page_dishes_prepare_list_by_step_seasoning.layoutManager = LinearLayoutManager(context)
                    page_dishes_prepare_list_by_step_seasoning.adapter = mDishedPreparedSeasonAdapter;
                }else{

                    mDishedPreparedSeasonAdapter.notifyDataSetChanged()
                }

            }
            page_add_food_seasoning.setOnClickListener {
                mListSeason.add(RecipeCurveSuccessBean.MaterialList(1))
                mDishedPreparedSeasonAdapter.notifyDataSetChanged()

            }

}

    fun RecordAudioSuccess(){
        RecordManager.getInstance().setRecordResultListener { filePath->
            ThreadPoolManager.getInstance().execute{
                var url= AliyunOSSManager.getInstance().uploadFile(AliyunOSSManager.getOSSVoiceName("curve",System.currentTimeMillis().toString()),filePath.absolutePath)
                if (mDishedPrepareStepAdapter.data.size>mPosAudio) {
                    mDishedPrepareStepAdapter.data[mPosAudio].voiceUrl = url
                }
            }
        }
    }

    var mPosAudio=0

    private var mTimerTask: TimerTask? = null
    var  mTimer = Timer()

   private fun RecordAudioListener(){

        RecordManager.getInstance().setRecordStateListener(object: RecordStateListener {
            override fun onStateChange(p0: RecordHelper.RecordState?) {
                Log.e(TAG, "录制$mPosAudio-----$p0")
                p0?.let {
                    if (it == RecordHelper.RecordState.RECORDING){
                        var  mTimer = Timer()
                        mTimerTask = object : TimerTask() {
                            override fun run() {
                                runOnUiThread {

                                }


                            }
                        }
                        mTimer?.schedule(mTimerTask, 0, 10)
                    }else if (it == RecordHelper.RecordState.FINISH||it == RecordHelper.RecordState.IDLE){
                        Log.e(TAG, "---------$it$mPosAudio")
                        mTimerTask?.cancel()
                        postDelayed({
                            runOnUiThread {
                                if (mDishedPrepareStepAdapter.data?.size>mPosAudio) {
                                    mDishedPrepareStepAdapter.data[mPosAudio].status = 2;
                                    Log.e(TAG, "---------$it" + mPosAudio + "---" + mDishedPrepareStepAdapter.data[mPosAudio].status)
                                    mDishedPrepareStepAdapter.notifyItemChanged(mPosAudio, "1")
                                }
                            }
                        },1000)
                    }
                }
            }
            override fun onError(p0: String?) {
            }
        })
    }

    lateinit var mDishedPrepareStepAdapter: DishedPrepareStepAdapter

    private var isRecording=false
    var workTime=0L
    var timeRecord=0L
    private fun initStep(){
            context?.let {
                if (page_dishes_prepare_list_by_step.adapter==null) {
                    mDishedPrepareStepAdapter = DishedPrepareStepAdapter(
                            this, this,object :IOnTouchListener {

                            override fun onTouch(
                                pos: Int,
                                v: View,
                                event: MotionEvent,
                                isPress: Boolean
                            ): Boolean {
                                mPosAudio=pos
                                when (event.action) {
                                    MotionEvent.ACTION_DOWN -> {
                                    Log.e(TAG, "ACTION_DOWN")
                                        timeRecord=System.currentTimeMillis()
                                        workTime=System.currentTimeMillis()
                                    }
                                    MotionEvent.ACTION_MOVE -> {
                                        if (RecordManager.getInstance().state == RecordHelper.RecordState.STOP&&
                                            RecordManager.getInstance().state != RecordHelper.RecordState.RECORDING
                                            || RecordManager.getInstance().state == RecordHelper.RecordState.IDLE
                                            &&System.currentTimeMillis()-timeRecord>300) {
                                            RecordManager.getInstance().start();
                                        }


                                        if (RecordManager.getInstance().state == RecordHelper.RecordState.RECORDING) {
                                            runOnUiThread {
                                                mDishedPrepareStepAdapter.data[mPosAudio].status = 1;
                                                mDishedPrepareStepAdapter.data[mPosAudio].time = (System.currentTimeMillis()-workTime).toInt();
                                                Log.e(TAG, "ACTION_MOVE"+mDishedPrepareStepAdapter.data[mPosAudio].time)
                                                mDishedPrepareStepAdapter.notifyItemChanged(
                                                    mPosAudio,
                                                    "1"
                                                )
                                            }
                                        }
                                    }


                                    MotionEvent.ACTION_UP -> {
                                        timeRecord=System.currentTimeMillis()
                                        if (RecordManager.getInstance().state == RecordHelper.RecordState.RECORDING) {
                                            RecordManager.getInstance().stop();

                                        runOnUiThread {
                                            mDishedPrepareStepAdapter.data[mPosAudio].status = 2;
                                            mDishedPrepareStepAdapter.notifyItemChanged(
                                                mPosAudio,
                                                "1"
                                            )
                                        }
                                        }

                                    }

                                }
                                return true
                            }



                            override fun onClick(view: View, pos: Int) {
                                mPosAudio=pos
                                when(view.id){
                                    R.id.widget_add_cooked_view_play_audio->{
                                        mDishedPrepareStepAdapter.data.get(mPosAudio).voiceUrl?.let { it1 ->
                                            playAudio(
                                                it1
                                            )
                                        }
                                    }
                                    R.id.widget_add_cooked_view_step_audio_icon_delete_play_audio->{
                                        if (simpleAudioPlayer?.state== PlayerState.Playing){
                                            simpleAudioPlayer?.stop()
                                        }
                                        mDishedPrepareStepAdapter.data[mPosAudio].status=4
                                        mDishedPrepareStepAdapter.notifyItemChanged(mPosAudio,"1")
                                    }
                                }

                            }
                        })

                    val menuCreator = SwipeMenuCreator { _, rightMenu, _ ->
                        val deleteItem = SwipeMenuItem(context)
                        deleteItem
                                .setBackground(resources.getDrawable(R.drawable.layer_list_delete_drawable))
                                .setTextColor(Color.WHITE) // 文字颜色。
                                .setTextSize(15) // 文字大小。
                                .setWidth(140).height = ViewGroup.LayoutParams.MATCH_PARENT
                        rightMenu.addMenuItem(deleteItem)
                    }
                    val mMenuItemClickListener =
                            SwipeMenuItemClickListener { swipeMenuBridge ->
                                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                                swipeMenuBridge.closeMenu()
                                val direction = swipeMenuBridge.direction // 左侧还是右侧菜单。
                                val adapterPosition = swipeMenuBridge.adapterPosition // RecyclerView的Item的position。
                                val menuPosition = swipeMenuBridge.position // 菜单在RecyclerView的Item中的Position。
                                when (menuPosition) {
                                    0 -> {
                                        mDishedPrepareStepAdapter.data.removeAt(adapterPosition)
                                        mDishedPrepareStepAdapter.notifyDataSetChanged()
                                    }
                                }
                            }
                    page_dishes_prepare_list_by_step.setSwipeMenuItemClickListener(mMenuItemClickListener)
                    page_dishes_prepare_list_by_step.setSwipeMenuCreator(menuCreator)
                    page_dishes_prepare_list_by_step.layoutManager = LinearLayoutManager(it)
                    page_dishes_prepare_list_by_step.adapter = mDishedPrepareStepAdapter
                }else{
                    mDishedPrepareStepAdapter.notifyDataSetChanged()
                }
                RecordAudioSuccess()
                RecordAudioListener()
            }
        act_add_step_cooking.setOnClickListener {
            mDishedPrepareStepAdapter.addData(RecipeCurveSuccessBean.PrepareStepList())
//            mCurvePrepareStepDtoList.add()
//            mDishedPrepareStepAdapter.notifyDataSetChanged()
        }

        mRecipeCurveSuccessBean?.payload?.prepareStepList?.let {
//            mCurvePrepareStepDtoList.addAll(it)
            mDishedPrepareStepAdapter.setNewData(it)
        }?:run{
            mRecipeCurveSuccessBean?.payload?.prepareStepList= ArrayList()
        }


    }





    private fun choosePicture(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val selfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            if (selfPermission == 0) {
                pickHelper?.showPickDialog("")
            } else {
                PermissionsUtils.checkPermission(
                    this,
                    Manifest.permission.CAMERA,
                    PermissionsUtils.CODE_USER_INFO_SHARE
                )
            }
        } else {
            pickHelper?.showPickDialog("")
        }
    }
    override fun initView() {
        if (intent.hasExtra(ID)){
            id=intent.getIntExtra(ID,0)
        }else {
            intent.getSerializableExtra(UPDATECURVESTEPREQUEST)?.let {
                mRecipeCurveSuccessBean = it as RecipeCurveSuccessBean
            }
        }

        pickHelper = PickImageHelperTwo(activity, object : PickImageHelperTwo.PickCallbackTwo {
                override fun onPickComplete(bmp: Bitmap?) {
                    if (bmp == null) {
                        return
                    }

                    val fileDirPath: String? = context?.filesDir?.absolutePath
                    val filePath = fileDirPath + File.separator +
                            SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                                .format(System.currentTimeMillis()) + ".jpg"
                    bitmap2Path(bmp, filePath)

                    ThreadPoolManager.getInstance().execute {
                        var url = AliyunOSSManager.getInstance().uploadFile(
                            AliyunOSSManager.getOSSImgName("cuvetStep" , System.currentTimeMillis().toString()),
                            filePath
                        )

                        mRecipeCurveSuccessBean?.payload?.imageCover = url

                        runOnUiThread {
                            act_add_cooked_view_picture_view_delete.visibility = View.VISIBLE
                            act_step_by_step_image_add.visibility = View.GONE

                            Glide.with(this@DishesPreparedByStepActivity).load(url).apply(
                                RequestOptions()
                                    .transforms(
                                        CenterCrop(), RoundedCorners(10)
                                    )
                            ).into(page_choose_pick_image)
                        }
                    }
                }

                override fun onPickComplete(bmp: String?) {

                }


            })

        mFlavouringApi=FlavouringApi(this)

        mFlavouringApi.getAllType(R.id.flavor)
        RecipeDetailPage.setStatusBarColor(activity, R.color.white)
        RecipeDetailPage.setLightStatusBar(
            activity, true
        )
        img_back.setOnClickListener {
            finish()
        }

        btn_save_dishes_prepared_by_step.setOnClickListener {
//            if (TextUtils.isEmpty( mRecipeCurveSuccessBean?.payload?.imageCover)){
//                ToastUtils.show("请上传菜谱封面",Toast.LENGTH_LONG)
//                return@setOnClickListener
//            }

//            if (TextUtils.isEmpty(page_choose_pick_image_introduction.text.toString())){
//                ToastUtils.show("请输入菜谱描述",Toast.LENGTH_LONG)
//                return@setOnClickListener
//            }
//            mDishedPrepareStepAdapter.data.forEach {
//
//                if (TextUtils.isEmpty(it.description)){
//                    ToastUtils.show("请输入步骤描述",Toast.LENGTH_LONG)
//                    return@setOnClickListener
//                }
//                if (TextUtils.isEmpty(it.imageUrl)){
//                    ToastUtils.show("请上传步骤封面",Toast.LENGTH_LONG)
//                    return@setOnClickListener
//                }
//            }

//            mListSeason.forEach {
//                if (TextUtils.isEmpty(it.materialName)|| it.materialName?.contains("如") == true){
//                    ToastUtils.show("请先选择佐料",Toast.LENGTH_LONG)
//                    return@setOnClickListener
//                }
//                if (TextUtils.isEmpty(it.unitName)){
//                    ToastUtils.show("请选择单位名称",Toast.LENGTH_LONG)
//                    return@setOnClickListener
//                }
//
//                if (TextUtils.isEmpty(it.weight.toString())||it.weight==0){
//                    ToastUtils.show("请填写佐料所需的数量",Toast.LENGTH_LONG)
//                    return@setOnClickListener
//                }
//
//            }


//            mListDataFood.forEach {
//
//                if (TextUtils.isEmpty(it.materialName)|| it.materialName?.contains("如") == true){
//                    ToastUtils.show("请先选择食材",Toast.LENGTH_LONG)
//                    return@setOnClickListener
//                }
//                if (TextUtils.isEmpty(it.weight.toString())||it.weight.toInt()==0){
//                    ToastUtils.show("请填写食材所需的数量",Toast.LENGTH_LONG)
//                    return@setOnClickListener
//                }
//
//
//                if (TextUtils.isEmpty(it.unitName)){
//                    ToastUtils.show("请选择单位名称",Toast.LENGTH_LONG)
//                    return@setOnClickListener
//                }
//
//
//
//            }


                mRecipeCurveSuccessBean?.payload?.prepareStepList?.clear()
                mRecipeCurveSuccessBean?.payload?.materialList?.clear()
                mRecipeCurveSuccessBean?.payload?.prepareStepList?.addAll(mDishedPrepareStepAdapter.data)
                mRecipeCurveSuccessBean?.payload?.materialList?.addAll(mListSeason)
                mRecipeCurveSuccessBean?.payload?.materialList?.addAll(mListDataFood)

            mRecipeCurveSuccessBean?.payload?.materialList?.forEach {

                if (it.id==null){
                    it.materialType=null;
                }
            }
                mRecipeCurveSuccessBean?.payload?.introduction =
                    page_choose_pick_image_introduction.text.toString()
                mRecipeCurveSuccessBean?.let { it1 -> mRecipeSuccessApi.submitCookingCurve(R.id.auto_work_layout, it1) }


        }

        act_add_cooked_view_picture_view_delete.setOnClickListener {
            act_step_by_step_image_add.visibility = View.VISIBLE

            act_add_cooked_view_picture_view_delete.visibility = View.GONE

            Glide.with(this).load(R.drawable.shape_image_bg)
                    .into(page_choose_pick_image)
            page_choose_pick_image.background = getDrawable(R.drawable.shape_image_bg)


        }
        mUploadFileApi= UploadFileApi(this)
        mRecipeSuccessApi   = RecipeSuccessApi(this);


        mRecipeCurveSuccessBean?.payload?.curveCookbookId?.toLong()?.let {
            mRecipeSuccessApi.query(R.id.page_dishes_prepare_list_by_step_seasoning, it)
        }?: run{
            mRecipeSuccessApi.query(R.id.page_dishes_prepare_list_by_step_seasoning, id.toLong())
        }
    }

     var id:Int = 0


    override fun initData() {
        tv_title.text=mRecipeCurveSuccessBean?.payload?.name
        if (!TextUtils.isEmpty(mRecipeCurveSuccessBean?.payload?.imageCover)) {
           context?.let {
           act_add_cooked_view_picture_view_delete.visibility = View.VISIBLE
           act_step_by_step_image_add.visibility = View.GONE
           Glide.with(it).load(mRecipeCurveSuccessBean?.payload?.imageCover)
                        .into(page_choose_pick_image)
                }
            }




            page_choose_pick_image.setOnClickListener {

                choosePicture()
            }
            if (!TextUtils.isEmpty(mRecipeCurveSuccessBean?.payload?.imageCover)) {
                act_add_cooked_view_picture_view_delete.visibility = View.VISIBLE
                act_step_by_step_image_add.visibility = View.GONE
//                Glide.with(context).load(mRecipeCurveSuccessBean?.payload?.imageCover)
//                    .into(page_choose_pick_image)
                Glide.with(context).load(mRecipeCurveSuccessBean?.payload?.imageCover)
                    .apply( RequestOptions().transforms( CenterCrop(),  RoundedCorners(10))).into(page_choose_pick_image)

            }

            mRecipeCurveSuccessBean?.payload?.introduction?.let {
                page_choose_pick_image_introduction.setText(it)
            }

        page_choose_pick_image_introduction.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                mRecipeCurveSuccessBean?.payload?.introduction=s.toString()

            }

        })
        initFood()
        initStep()
        initSeason()
    }

    override fun onFailure(
        requestId: Int,
        requestCode: Int,
        paramString1: String?,
        data: Any?
    ) {

    }

    override fun onSaveCache(requestId: Int, requestCode: Int, paramObject: Any?) {

    }

    override fun onSuccess(requestId: Int, requestCode: Int, paramObject: Any?) {

        if (R.id.page_choose_pick_image==requestId){

            if (paramObject is UpFileBean){
                mRecipeCurveSuccessBean?.payload?.imageCover=paramObject.path

                act_add_cooked_view_picture_view_delete.visibility=View.VISIBLE
                act_step_by_step_image_add.visibility=View.GONE

                Glide.with(context).load(paramObject.path).apply( RequestOptions()
                    .transforms( CenterCrop(),  RoundedCorners(10)
                    )).into(page_choose_pick_image)

            }
        }else if (R.id.widget_add_cooked_view_picture_view==requestId){
            if (paramObject is UpFileBean){
                mDishedPrepareStepAdapter.data[imagePos]?.imageUrl=paramObject.path
//                Glide.with(this).load(paramObject.path).into(page_choose_pick_image)
                mDishedPrepareStepAdapter.notifyItemChanged(imagePos)
            }
        }else if (R.id.audio_input_rl==requestId){
            if (paramObject is UpFileBean){
                mDishedPrepareStepAdapter.data[audioFilePos]?.voiceUrl=paramObject.path
                mDishedPrepareStepAdapter.notifyItemChanged(audioFilePos)
            }
        }else if(R.id.page_dishes_prepare_list_by_step_seasoning==requestId){

            if (paramObject is RecipeCurveSuccessBean) {
                mRecipeCurveSuccessBean = paramObject

                initData()

            }


        }else if (R.id.auto_work_layout==requestId){
            if (paramObject is SubmitCurveBean) {
                ToastUtils.show("保存成功", Toast.LENGTH_LONG)

                var mIntent = Intent()
                mIntent.putExtra(UPDATECURVESTEBACK, mRecipeCurveSuccessBean)
                setResult(RESULT_OK, mIntent)
                finish()
            }
        }else if (R.id.flavor==requestId){
            if (paramObject is UnitListBean){
                mUnitListBean=paramObject;
                mUnitListBean?.let {
                    mDishedPreparedAdapter.setUnitListBean(it)
                    mDishedPreparedSeasonAdapter.setUnitListBean(it)
                }

                Log.e("单位",mUnitListBean?.payload.toString())
            }

        }
    }


     var mUnitListBean:UnitListBean ?=null
    var imagePos=0
    var audioFilePos=0
    override fun uploadFile(filePath: String, pos: Int) {
        this.audioFilePos=pos
        mUploadFileApi.uploadFile(R.id.audio_input_rl,filePath,"4")
    }

    override fun choose(pos: Int, bmp: Bitmap) {
        if (bmp == null) {
            return
        }

        this.imagePos=pos
        val fileDirPath: String? = context?.filesDir?.absolutePath
        val filePath = fileDirPath + File.separator +
                SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                    .format(System.currentTimeMillis()) + ".jpg"
        bitmap2Path(bmp,filePath)

        ThreadPoolManager.getInstance().execute{
            var url= AliyunOSSManager.getInstance().uploadFile(AliyunOSSManager.getOSSVideoName("cuvetStep"+System.currentTimeMillis()),filePath)
            mDishedPrepareStepAdapter.data[imagePos]?.imageUrl=url
            runOnUiThread {
                mDishedPrepareStepAdapter.notifyItemChanged(imagePos)
            }
        }
    }
}
