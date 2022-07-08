package com.robam.roki.ui.page.curve


import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robam.base.BaseActivity
import com.robam.roki.R
import com.robam.roki.net.OnRequestListener
import com.robam.roki.request.api.FlavouringApi
import com.robam.roki.request.bean.FlavouringArray
import com.robam.roki.request.bean.FlavouringBean
import com.robam.roki.request.bean.UnitListBean
import com.robam.roki.ui.activity3.AppActivity
import com.robam.roki.ui.page.recipedetail.RecipeDetailPage
import kotlinx.android.synthetic.main.activity_flavouring.*
import kotlinx.android.synthetic.main.include_title_bar.*
import kotlinx.android.synthetic.main.item_flavouring.view.*
interface IFavoring{
    fun getChooseIndex(pos:Int)
}

class FlavouringActivity : AppActivity(), OnRequestListener {



    lateinit var mDataLis:ArrayList<FlavouringArray>

    var ifAccessory=false
    lateinit var mFlavouringApi:FlavouringApi
    override fun getLayoutId(): Int=R.layout.activity_flavouring

    companion object{
        val IFACCESSORY="ifAccessory"
        val FLAVOURING="flavouring"
        val POSITION="position"
    }

    private  val TAG = "FlavouringActivity"


    var pos=-1

    var mTextWatcher=object:TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.apply {
                if (length!=0&& toString().trim().isNotEmpty()){
                    mFlavouringApi.getSearch(R.layout.activity_flavouring,s.toString(),ifAccessory)
                    act_flavouring_btn_confirm.visibility=View.VISIBLE
                }else{
                    page_flavouring_food_list_result.visibility=View.GONE
                    if (length==0){
                        act_flavouring_btn_confirm.visibility = View.GONE
                    }else {
                        act_flavouring_btn_confirm.visibility = View.VISIBLE
                    }
                    mDataLis.clear()
                    mFlavourAdapter.notifyDataSetChanged()

                }

            }
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }

    var mpos=-1;


    lateinit var mUnitListBean:UnitListBean
    lateinit var mFlavourAdapter:FlavourAdapter
    override fun initView() {
        mFlavouringApi= FlavouringApi(this)
        mFlavouringApi.getAllType(R.id.flavor)
        ifAccessory=intent.getBooleanExtra(IFACCESSORY,false)

        pos=intent.getIntExtra(POSITION,0)
        img_back.setOnClickListener {
           finish()
        }
        mDataLis=ArrayList()
        if(ifAccessory){
            tv_title.text="佐料"
            page_flavouring_food_et.hint = "请输入佐料名称"
        }else{
            tv_title.text="食材"
            page_flavouring_food_et.hint = "请输入食材名称"
        }

        RecipeDetailPage.setStatusBarColor(activity, R.color.white)
        RecipeDetailPage.setLightStatusBar(activity, true)

        act_flavouring_btn_confirm.setOnClickListener {
            var intent=Intent()
            if (mpos==-1||mDataLis.isEmpty()){
                var mFlavouringArray=FlavouringArray()
                mFlavouringArray.name=page_flavouring_food_et.text.toString()
                mFlavouringArray.units=mUnitListBean.payload
                mFlavouringArray.id=null
                intent.putExtra(FLAVOURING,mFlavouringArray)
            }
            intent.putExtra(POSITION,pos)
            setResult(RESULT_OK,intent)
            finish()

        }
        page_flavouring_food_list_result.layoutManager= LinearLayoutManager(context);


        page_flavouring_food_et.addTextChangedListener(mTextWatcher)


        mFlavourAdapter= FlavourAdapter(mDataLis, object : IFavoring {
            override fun getChooseIndex(posChoose: Int) {
                var intent=Intent()
                mpos=posChoose
                if (mDataLis[mpos].units?.isEmpty() == true) {
                    mDataLis[mpos].units=mUnitListBean.payload
                }

                intent.putExtra(FLAVOURING, mDataLis[mpos])
                intent.putExtra(POSITION,pos)
                setResult(RESULT_OK,intent)
                finish()
//                page_flavouring_food_et.removeTextChangedListener(mTextWatcher)
//                page_flavouring_food_et.setText(mDataLis[pos].name)
//                page_flavouring_food_list_result.visibility=View.GONE
////                act_flavouring_btn_confirm.visibility=View.VISIBLE
//                page_flavouring_food_et.addTextChangedListener(mTextWatcher)
            }
        })
        page_flavouring_food_list_result.adapter=mFlavourAdapter
    }

    override fun initData() {

    }
    class FlavourAdapter(var mDataLis:ArrayList<FlavouringArray>, var mIFavoring:IFavoring): RecyclerView.Adapter<FlavourAdapter.FlavourViewHolder>() {



        class FlavourViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
        override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): FlavourViewHolder {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_flavouring, viewGroup, false)
            return FlavourViewHolder(view)
        }

        override fun onBindViewHolder(p0: FlavourViewHolder, p1: Int) {
            with(p0.itemView){
                item_flavouring_txt.text=mDataLis[p1].name
                setOnClickListener {
                    mIFavoring?.getChooseIndex(p1)
                }
            }

        }

        override fun getItemCount(): Int=mDataLis.size


    }

    override fun onFailure(requestId: Int, requestCode: Int, msg: String?, data: Any?) {

    }

    override fun onSaveCache(requestId: Int, requestCode: Int, paramObject: Any?) {

    }

    override fun onSuccess(requestId: Int, requestCode: Int, paramObject: Any?) {

        if (requestId==R.layout.activity_flavouring){


            if (paramObject is FlavouringBean) {

                    if (paramObject.payload.isNotEmpty()) {

                        mDataLis.clear()
                        mDataLis.addAll(paramObject.payload)
                        mFlavourAdapter.notifyDataSetChanged()
                        act_flavouring_btn_confirm.visibility = View.GONE
                        page_flavouring_food_list_result.visibility = View.VISIBLE
                    } else {
                        mDataLis.clear()
                        mFlavourAdapter.notifyDataSetChanged()
                        page_flavouring_food_list_result.visibility = View.GONE
                        act_flavouring_btn_confirm.visibility = View.VISIBLE
                    }
            }
        }else if (R.id.flavor==requestId){

            if (paramObject is UnitListBean){
                mUnitListBean=paramObject;
                Log.e("单位",mUnitListBean.payload.toString())
            }



        }
    }

}