package com.robam.roki.ui.page.device.steamovenone.steamovenone920.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.robam.roki.R
import com.robam.roki.net.OnRequestListener
import com.robam.roki.net.request.bean.RecipeCustomBean
import com.robam.roki.net.request.bean.RecipeCustomData
import com.robam.roki.request.api.RecipeApi
import com.robam.roki.ui.PageArgumentKey
import com.robam.roki.ui.form.MainActivity.RESULT_OK
import com.robam.roki.ui.form.MainActivity.activity
import com.robam.roki.ui.page.device.steamovenone.steamovenone920.CustomizeRecipeOrPreviewActivity
import com.robam.roki.ui.page.device.steamovenone.steamovenone920.RecipePreviewActivity
import com.xiaomi.push.dp
import com.xiaomi.push.it
import kotlinx.android.synthetic.main.fragment_custom_recipte.*
import kotlinx.android.synthetic.main.item_custom_recipe.view.*



private const val GUID = "guid"

class CustomRecipeHolder(item:View):RecyclerView.ViewHolder(item)
class CustomRecipeAdapter(var mList:ArrayList<RecipeCustomData>,var guid: String,val mActivity: Activity): RecyclerView.Adapter<CustomRecipeHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomRecipeHolder {

        return CustomRecipeHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_custom_recipe,p0,false))
    }

    override fun onBindViewHolder(p0: CustomRecipeHolder, p1: Int) {

        with(p0.itemView) {

             Glide.with(context).load(mList[p1].coverImg).apply(RequestOptions().dontAnimate().transform(GlideRoundTransform(15)))
                 .error(R.mipmap.icon_recipe_default)
                 .into(item_custom_recipe_image)
            item_custom_recipe_name.text=mList[p1].name
            item_custom_recipe_rl.setOnClickListener {
                var intent=Intent(context,RecipePreviewActivity::class.java)
                intent.putExtra(RecipePreviewActivity.ID, mList[p1].id)
                intent.putExtra(RecipePreviewActivity.ISCUSTOMRECIPE,true)
                intent.putExtra(RecipePreviewActivity.GUIDDEVICE,guid)
//                intent.putExtra(RecipePreviewActivity.RECIPEPREVIEW,mList[p1].time)
                mActivity.startActivityForResult(intent,0x12345)
            }
        }
    }

    override fun getItemCount(): Int =mList.size



}

class CustomRecipeFragment : Fragment(), OnRequestListener {

    private var guid: String? = null


    private lateinit var mRecipeApi:RecipeApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            guid = it.getString(GUID)

            Log.e(TAG,guid)
        }

    }

    val TAG="CustomRecipeFragment"



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_custom_recipte, container, false)
    }

    lateinit var mCustomRecipeAdapter:CustomRecipeAdapter
    var mDataList:ArrayList<RecipeCustomData> = ArrayList()


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==0x12345&&resultCode== RESULT_OK) {
            frg_custom_rc_list.refresh()
        }
    }



    var page=0;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        frg_custom_btn_create_recipe.setOnClickListener {
            var intent=Intent(context,CustomizeRecipeOrPreviewActivity::class.java)
            intent.putExtra(PageArgumentKey.Guid,guid)
            startActivity(intent)
        }

        mCustomRecipeAdapter= guid?.let { activity?.let { it1 ->
            CustomRecipeAdapter(mDataList, it,
                it1
            )
        } }!!
        frg_custom_rc_list.layoutManager=GridLayoutManager(context,2)
        frg_custom_rc_list.adapter=mCustomRecipeAdapter

        frg_custom_rc_list.setPullRefreshEnabled(true);
        frg_custom_rc_list.setLoadingMoreEnabled(true);

        frg_custom_rc_list.setLoadingListener(object: XRecyclerView.LoadingListener{
            override fun onRefresh() {
                page=0;
                guid?.let { mRecipeApi.getRecipeCustomList(R.layout.item_custom_recipe,page=page,name="", deviceGuid = it) }
            }

            override fun onLoadMore() {
                guid?.let { mRecipeApi.getRecipeCustomList(R.layout.item_custom_recipe,page=++page,name="", deviceGuid = it) }
            }


        })
        mRecipeApi= RecipeApi(this)

        guid?.let { mRecipeApi.getRecipeCustomList(R.layout.item_custom_recipe,page=page,name="", deviceGuid = it) }






    }

    override fun onResume() {
        super.onResume()



    }
    companion object {

        @JvmStatic
        fun newInstance(guid: String) =
            CustomRecipeFragment().apply {
                arguments = Bundle().apply {
                    putString(GUID, guid)

                }
            }
    }

    override fun onFailure(requestId: Int, requestCode: Int, msg: String?, data: Any?) {
        if (requestId==R.layout.item_custom_recipe) {
            frg_custom_rc_list.refreshComplete()
        }
    }

    override fun onSaveCache(requestId: Int, requestCode: Int, paramObject: Any?) {

    }

    override fun onSuccess(requestId: Int, requestCode: Int, paramObject: Any?) {

        if (requestId==R.layout.item_custom_recipe){
            frg_custom_rc_list.refreshComplete()
            paramObject?.let {
                if (paramObject is RecipeCustomBean){
                    if (page==0){
                        mDataList.clear()
                        mDataList.addAll(paramObject.datas)
                        mCustomRecipeAdapter.notifyDataSetChanged()
                    }else{
                        mDataList.addAll(paramObject.datas)
                        mCustomRecipeAdapter.notifyDataSetChanged()
                    }



                }


            }




        }
    }
}