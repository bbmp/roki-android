package com.robam.roki.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.legent.utils.api.ToastUtils
import com.robam.roki.R
import com.robam.roki.request.bean.RecipeCurveSuccessBean
import com.robam.roki.request.bean.UnitListBean
import com.robam.roki.ui.page.curve.DishesPreparedByStepActivity.Companion.FOODCOOD
import com.robam.roki.ui.page.curve.DishesPreparedByStepActivity.Companion.SEASONCOOD
import com.robam.roki.ui.page.curve.FlavouringActivity
import com.robam.roki.ui.widget.dialog.UnitCheck
import com.robam.roki.ui.widget.dialog.UnitPickerDialog
import kotlinx.android.synthetic.main.inlcude_add_food_material_view.view.*

class DishedPreparedViewViewHolder(view:View): RecyclerView.ViewHolder(view)
class DishedPreparedAdapter(var mContext:Activity,
                            var mListData:ArrayList<RecipeCurveSuccessBean.MaterialList>,

                            var mUnitListBean: UnitListBean?):


    RecyclerView.Adapter<DishedPreparedViewViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): DishedPreparedViewViewHolder {
        return getViewHolder(R.layout.inlcude_add_food_material_view,viewGroup)
    }
    fun setUnitListBean(mUnitListBean: UnitListBean){
        this.mUnitListBean=mUnitListBean
    }
    private fun getViewHolder(layoutId:Int,viewGroup:ViewGroup):DishedPreparedViewViewHolder{

       var holder=DishedPreparedViewViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(layoutId, viewGroup, false))
        holder.setIsRecyclable(false)
        return holder
    }




    private  val TAG = "DishedPreparedAdapter"
    override fun onBindViewHolder(p0: DishedPreparedViewViewHolder, @SuppressLint("RecyclerView") p1: Int) {
        var IFACCESSORY=false
        Log.e(TAG,mListData[p1].toString()+"---")
        with(p0.itemView){
            if (mListData[p1].materialType ==3){
                include_add_food_material_txt_name.text="食材:"
                if (TextUtils.isEmpty(mListData[p1].materialName)) {
                    include_add_food_material_txt.text = "如鸡蛋"
                }
                include_add_food_material_number_title_txt.text="食材用量:"
                 IFACCESSORY=false
            }else if(mListData[p1].materialType ==1){
                include_add_food_material_txt_name.text="佐料:"
                if (TextUtils.isEmpty(mListData[p1].materialName)) {
                    include_add_food_material_txt.text = "如盐"
                }
                include_add_food_material_number_title_txt.text="佐料用量:"
                IFACCESSORY=true
            }


            include_add_food_material_txt_name.setOnClickListener {
                var intent= Intent(context, FlavouringActivity::class.java)
                intent.putExtra(FlavouringActivity.IFACCESSORY,IFACCESSORY)
                intent.putExtra(FlavouringActivity.POSITION,p1)
                if (mListData[p1].materialType ==3){
                    mContext.startActivityForResult(intent,FOODCOOD)
                }else{
                    mContext.startActivityForResult(intent,SEASONCOOD)
                }

            }

            include_add_food_material_txt.setOnClickListener {
                var intent= Intent(context, FlavouringActivity::class.java)
                intent.putExtra(FlavouringActivity.IFACCESSORY,IFACCESSORY)
                intent.putExtra(FlavouringActivity.POSITION,p1)
                if (mListData[p1].materialType ==3){
                    mContext.startActivityForResult(intent,FOODCOOD)
                }else{
                    mContext.startActivityForResult(intent,SEASONCOOD)
                }
            }

            if (!TextUtils.isEmpty(mListData[p1].materialName)) {
                include_add_food_material_txt.text = mListData[p1].materialName
            }
            if (include_add_food_material_dosage_et.tag!=null&&include_add_food_material_dosage_et.tag is TextWatcher){
                include_add_food_material_dosage_et.addTextChangedListener(include_add_food_material_dosage_et.tag as TextWatcher)
            }else {
                if (mListData[p1].weight!=null&&mListData[p1].weight.toInt()!=0) {
                    include_add_food_material_dosage_et.setText(mListData[p1].weight.toString())
                }
                include_add_food_material_dosage_et.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        Log.e(TAG,"beforeTextChanged$s")
                        if (include_add_food_material_txt.text.equals("如鸡蛋")){
                                 ToastUtils.show("请选择食材", Toast.LENGTH_LONG)
                            return
                        }
                        if (include_add_food_material_txt.text.equals("如盐")){
                            ToastUtils.show("请选择作料", Toast.LENGTH_LONG)
                            return
                        }
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {

                        Log.e(TAG,"onTextChanged$s")
                    }

                    override fun afterTextChanged(s: Editable?) {
                        Log.e(TAG, "onTextChanged$s")
                        if (include_add_food_material_txt.text.equals("如鸡蛋")){
                            ToastUtils.show("请选择食材", Toast.LENGTH_LONG)
//                            include_add_food_material_dosage_et.setText("")
                            return
                        }

                        if (include_add_food_material_txt.text.equals("如盐")){
                            ToastUtils.show("请选择作料", Toast.LENGTH_LONG)
//                            include_add_food_material_dosage_et.setText("")
                            return
                        }


                        if (!TextUtils.isEmpty(s.toString())) {
                            mListData[p1].weight = s.toString().toInt()
                        }
                    }
                })
            }
            include_add_food_material_dosage_unit.setText(mListData[p1]?.unitName)

            include_add_food_material_dosage_unit.addTextChangedListener(object :TextWatcher{
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
                    mListData[p1]?.unitName=s.toString()
                }


            })
            include_add_food_material_dosage_unit.setOnClickListener {

                if (mListData[p1]?.units==null) {
                    mListData[p1]?.units= ArrayList()
                    mListData[p1]?.units?.clear()
                    mUnitListBean?.let { it1 -> mListData[p1]?.units?.addAll(it1.payload) }
                }
                mListData[p1]?.units?.let {
                    if (it.size>0) {
                        var mUnitPickerDialog = UnitPickerDialog(mContext, object : UnitCheck {
                            override fun choose(pos: Int) {
                                include_add_food_material_dosage_unit.setText( it?.get(pos)?.name)
                                mListData[p1].unitId = it[pos]?.id.toString()
                            }
                        }, mListData[p1].units)
                        mUnitPickerDialog.create()
                        mUnitPickerDialog.show()
                    }

                }


            }

        }

    }


    override fun getItemCount(): Int=mListData.size
}