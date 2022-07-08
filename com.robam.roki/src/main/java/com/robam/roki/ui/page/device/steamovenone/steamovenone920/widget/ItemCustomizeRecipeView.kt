package com.robam.roki.ui.page.device.steamovenone.steamovenone920.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.legent.ui.ext.views.SearchEditView
import com.legent.utils.api.ToastUtils
import com.robam.roki.R
import com.robam.roki.request.bean.FlavouringArray
import com.robam.roki.request.param.MaterialDtoList
import com.robam.roki.request.param.StepDtoList
import com.robam.roki.ui.adapter.IntToSmallChineseNumber
import com.robam.roki.ui.page.curve.DishesPreparedByStepActivity
import com.robam.roki.ui.page.curve.FlavouringActivity
import com.robam.roki.ui.widget.dialog.UnitCheck
import com.robam.roki.ui.widget.dialog.UnitPickerDialog
import com.robam.roki.utils.NumberUtils
import com.robam.roki.utils.NumberUtils.int2chineseNum
import com.xiaomi.push.it
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener
import kotlinx.android.synthetic.main.activity_recipe_success.*
import kotlinx.android.synthetic.main.inlcude_add_food_material_view.view.*
import kotlinx.android.synthetic.main.item_customize_recipe_material_view.view.*
import kotlinx.android.synthetic.main.item_customize_recipe_step_view.view.*
import kotlinx.android.synthetic.main.item_customize_recipe_view.view.*

class StepViewHolder(item:View):RecyclerView.ViewHolder(item)
class MaterialAdapter(var mList:ArrayList<MaterialDtoList>):RecyclerView.Adapter<StepViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): StepViewHolder {
        return StepViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_customize_recipe_material_view,p0,false))
    }

    override fun onBindViewHolder(p0: StepViewHolder, @SuppressLint("RecyclerView") p1: Int) {

        with(p0.itemView){
            item_customize_recipe_name.text = mList[p1].materialName
            if (item_customize_recipe_number.tag!=null){
                item_customize_recipe_number.setText(item_customize_recipe_number.tag as String)
            }else{
                item_customize_recipe_number.setText( mList[p1].weight)
                item_customize_recipe_number.addTextChangedListener(object:TextWatcher{
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                    override fun afterTextChanged(s: Editable?) {
                        mList[p1].weight=s.toString()
                        item_customize_recipe_number.tag=mList[p1].weight
                    }

                })



            }
            item_customize_recipe_unit.text=mList[p1].unitName




         item_customize_img.setOnClickListener {



                if (    mList[p1].mUnitsList!=null) {
                    Log.e("单位",    mList[p1].mUnitsList.toString())
                }
             mList[p1]?.mUnitsList?.let {
                if (it.size>0) {
                    var mUnitPickerDialog = UnitPickerDialog(context, object : UnitCheck {
                        override fun choose(pos: Int) {
                            item_customize_recipe_unit.text = it?.get(pos)?.name
                            mList[p1].unitId = it[pos]?.id
                            mList[p1].unitName= it?.get(pos)?.name.toString()
                        }

                    },     mList[p1].mUnitsList)
                    mUnitPickerDialog.create()
                    mUnitPickerDialog.show()
                }

            }
        }
        }

    }

    override fun getItemCount(): Int = mList.size


}


class StepAdapter(var mStepDtoList:ArrayList<StepDtoList>):RecyclerView.Adapter<StepViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): StepViewHolder {
        return StepViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_customize_recipe_step_view,p0,false))
    }

    override fun onBindViewHolder(p0: StepViewHolder, @SuppressLint("RecyclerView") p1: Int) {
        with(p0.itemView){

            mStepDtoList[p1].stepImg?.let {
                if (!TextUtils.isEmpty(it))
                item_customize_recipe_step_view.setImageview(it) } ?:run{


            }


            item_customize_recipe_step_view.setIHasChoosePic(object :IHasChoosePic{
                override fun setImage(path: String) {
                    mStepDtoList[p1].stepImg=path
                }

            })
            item_customize_recipe_description.setText(mStepDtoList[p1].description)
            item_customize_recipe_step.text= "步骤"+ IntToSmallChineseNumber.ToCH(p1+1)
            item_customize_recipe_description.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    mStepDtoList[p1].description=s.toString()
                }
            })
        }
    }

    override fun getItemCount(): Int = mStepDtoList.size


}
interface IGetViewData {
    fun getMaterialData():ArrayList<MaterialDtoList>

    fun getRecipeStepStep():ArrayList<StepDtoList>

    fun setMaterialData( mList:ArrayList<MaterialDtoList>,isPreview: Boolean=false)

    fun setRecipeStepStep(mList:ArrayList<StepDtoList>,isPreview: Boolean=false)
}
class ItemCustomizeRecipeView:RelativeLayout,IGetViewData {

    constructor(context: Context):super(context){
        initView(null,0);
    }

    constructor(context: Context,attrs: AttributeSet):super(context,attrs){
        initView(attrs,0);
    }

    constructor(context: Context,attrs: AttributeSet,defStyleAttr:Int):super(context,attrs){
        initView(attrs,defStyleAttr);
    }


    public  fun addMaterial(mMaterialDto: MaterialDtoList) {
        var pos= mMaterialDtoList.size
        mMaterialDtoList.add(mMaterialDto)
        item_customize_image_add.visibility=View.INVISIBLE
        mMaterialAdapter.notifyItemChanged(pos)
    }

    public fun setPreviewState(isPreview:Boolean){
        if (isPreview){
            item_customize_image_add.visibility=View.GONE
            item_customize_recipe_view_text.visibility=View.GONE

        }else{
            item_customize_image_add.visibility=View.VISIBLE
            item_customize_recipe_view_text.visibility=View.VISIBLE
        }
    }

    /**
     * 0表示材料
     */
    var type:Int=0;


    fun jumpTo(pos:Int=0){
        var intent= Intent(context, FlavouringActivity::class.java)
        intent.putExtra(FlavouringActivity.IFACCESSORY,false)
        intent.putExtra(FlavouringActivity.POSITION,pos)
        (context as Activity).startActivityForResult(intent, DishesPreparedByStepActivity.FOODCOOD)
    }

//    fun setMaterial(isPreview: Boolean=true){
//            if (!isPreview) {
//                item_customize_recipe_view_text.visibility = View.VISIBLE
//                item_customize_image_add.visibility = View.INVISIBLE
//            }else{
//                item_customize_recipe_view_text.visibility = View.GONE
//                item_customize_image_add.visibility = View.GONE
//            }
//    }

    fun setVisiable(){
        item_customize_recipe_view_text.visibility=View.VISIBLE
        item_customize_recipe_view_text.setOnClickListener {
            jumpTo();
        }
  }

    private  var mMaterialDtoList : ArrayList<MaterialDtoList> = ArrayList()
    lateinit var mStepAdapter:StepAdapter
    lateinit var mMaterialAdapter:MaterialAdapter
    private  var mStepDtoList : ArrayList<StepDtoList> = ArrayList()
    private fun initView(attrs: AttributeSet?,defStyleAttr:Int) {
        LayoutInflater.from(context).inflate(R.layout.item_customize_recipe_view,this)
        val a = context.obtainStyledAttributes(attrs, R.styleable.ItemCustomizeRecipeView, defStyleAttr, 0)
        type=a.getInteger(R.styleable.ItemCustomizeRecipeView_recipe_type,0)
        val menuCreator = SwipeMenuCreator { _, rightMenu, _ ->
            val deleteItem = SwipeMenuItem(context)
            deleteItem
                .setBackground(resources.getDrawable(R.drawable.layer_list_delete_drawable))
                .setTextColor(Color.WHITE) // 文字颜色。
                .setTextSize(15) // 文字大小。
                .setWidth(140).height = ViewGroup.LayoutParams.MATCH_PARENT
            rightMenu.addMenuItem(deleteItem)
        }

        item_swipe_menu_view_rc.setSwipeMenuCreator(menuCreator)

        val mMenuItemClickListener = SwipeMenuItemClickListener { swipeMenuBridge ->
            swipeMenuBridge.closeMenu()
            val adapterPosition = swipeMenuBridge.adapterPosition // RecyclerView的Item的position。
            when (swipeMenuBridge.position) {
                0 -> {
                    if (type == 0) {
                        mMaterialDtoList.removeAt(adapterPosition)
                        if (mMaterialDtoList.size == 0) {
                            item_customize_image_add.visibility = View.VISIBLE
                            item_customize_recipe_view_text.visibility = View.GONE
                        }
                        mMaterialAdapter.notifyDataSetChanged()
                    }else{
                        mStepDtoList.removeAt(adapterPosition)
                        if (mStepDtoList.size == 0) {
                            item_customize_image_add.visibility = View.VISIBLE
                            item_customize_recipe_view_text.visibility = View.GONE
                        }
                        mStepAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
        item_swipe_menu_view_rc.setSwipeMenuItemClickListener(mMenuItemClickListener)

        if (type==0){
            item_customize_txt_name.text="食材"
           var  mMaterialDtoListItem= MaterialDtoList(null,
                "鸡蛋",
                1,
                1,
                "",
                ""
            )
            mMaterialDtoList.add(mMaterialDtoListItem)
            mMaterialAdapter=MaterialAdapter(mMaterialDtoList)
            setVisiable()
            item_swipe_menu_view_rc.layoutManager=LinearLayoutManager(context)
            item_swipe_menu_view_rc.adapter=mMaterialAdapter
//            item_customize_image_add.setOnClickListener {
//                jumpTo()
//            }
        }else{
            item_customize_txt_name.text="烹饪步骤"
            item_customize_recipe_view_text.text="+添加步骤"
            mStepAdapter=StepAdapter(mStepDtoList)
            item_swipe_menu_view_rc.layoutManager=LinearLayoutManager(context)
            item_swipe_menu_view_rc.adapter=mStepAdapter
            addStep()
            item_customize_image_add.setOnClickListener {
                addStep()
            }
        }
        a.recycle()
    }

    private fun addStep(){
        mStepDtoList.add(StepDtoList())
        mStepAdapter.notifyDataSetChanged()
        item_customize_image_add.visibility= View.INVISIBLE
        item_customize_recipe_view_text.visibility=View.VISIBLE
        item_customize_recipe_view_text.setOnClickListener {
            mStepDtoList.add(StepDtoList())
            mStepAdapter.notifyDataSetChanged()
        }
    }

    override fun getMaterialData(): ArrayList<MaterialDtoList> {
        return mMaterialDtoList;
    }
    override fun getRecipeStepStep(): ArrayList<StepDtoList> {
        return mStepDtoList;
    }

    override fun setMaterialData(mList: ArrayList<MaterialDtoList>,isPreview: Boolean) {
        mMaterialDtoList.addAll(mList)
        mMaterialAdapter.notifyDataSetChanged()
    }

    override fun setRecipeStepStep(mStepDtoList: ArrayList<StepDtoList>,isPreview: Boolean) {
        mStepDtoList.addAll(mStepDtoList)
        mStepAdapter.notifyDataSetChanged()

    }
}