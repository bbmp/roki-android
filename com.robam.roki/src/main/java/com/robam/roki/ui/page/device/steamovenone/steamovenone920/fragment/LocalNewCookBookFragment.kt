package com.robam.roki.ui.page.device.steamovenone.steamovenone920.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.legent.plat.pojos.device.DeviceConfigurationFunctions
import com.robam.roki.R
import com.robam.roki.model.bean.CookbookGroup
import com.robam.roki.ui.PageArgumentKey
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity.FUNCTION
import com.robam.roki.ui.page.device.oven.CookBookTag
import com.robam.roki.ui.page.device.steamovenone.steamovenone620.PagerCook620Item
import com.robam.roki.ui.widget.view.PagerSlidingTabStrip
import kotlinx.android.synthetic.main.fragment_local_new_cook_book.*
import kotlinx.android.synthetic.main.roki_device_toolbar.view.*

private const val BUNNDEL = "bundle"



class LocalNewCookBookFragment : Fragment() {
    var groups: ArrayList<CookbookGroup> = ArrayList()
    private var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bundle = it.getBundle(BUNNDEL)

        }
    }


    var guid: String? = null
    var needDescalingParams: String? = null
    var dt: String? = null

    lateinit var mDeviceConfigurationFunctions:DeviceConfigurationFunctions
    private lateinit var localCookbookList: ArrayList<DeviceConfigurationFunctions>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        guid = if (bundle == null) null else bundle?.getString(PageArgumentKey.Guid)

        mDeviceConfigurationFunctions=(bundle?.getSerializable(FUNCTION) as  DeviceConfigurationFunctions)
        localCookbookList = mDeviceConfigurationFunctions.subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions as ArrayList<DeviceConfigurationFunctions>
        needDescalingParams = if (bundle == null) null else bundle?.getString(PageArgumentKey.descaling)
        dt =mDeviceConfigurationFunctions.deviceType

        initData();






    }

    private  val TAG = "LocalNewCookBookFragmen"
    private val mTab: ArrayList<PagerCook620Item> = java.util.ArrayList()
    fun initData(){
        for (i in localCookbookList?.indices) {
            if ("ckno" != localCookbookList!![i].functionCode) {
                val cookbookGroup = CookbookGroup()
                cookbookGroup.setId(localCookbookList!![i].id)
                cookbookGroup.setFunctionCode(localCookbookList!![i].functionCode)
                cookbookGroup.setFunctionName(localCookbookList!![i].subView.title)
                cookbookGroup.setDeviceCategory(localCookbookList!![i].deviceCategory)
                cookbookGroup.setDeviceType(localCookbookList!![i].deviceType)
                val functions =
                    localCookbookList!![i].subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions
                val cookBookTags: MutableList<CookBookTag> = ArrayList()
                for (j in functions.indices) {
                    val cookBookbean = CookBookTag()
                    val deviceConfigurationFunctions = functions[j]
                    cookBookbean.setId(deviceConfigurationFunctions.id)
                    cookBookbean.setFunctionCode(deviceConfigurationFunctions.functionCode)
                    cookBookbean.setFunctionName(deviceConfigurationFunctions.functionName)
                    cookBookbean.setDeviceCategory(deviceConfigurationFunctions.deviceCategory)
                    cookBookbean.setDeviceType(deviceConfigurationFunctions.deviceType)
                    cookBookbean.setBackgroundImg(deviceConfigurationFunctions.backgroundImg)
                    cookBookbean.backgroundImghH = deviceConfigurationFunctions.backgroundImgH
                    cookBookbean.setFunctionParams(deviceConfigurationFunctions.functionParams)
                    cookBookTags.add(cookBookbean)
                }
                cookbookGroup.cookBookTagList = cookBookTags
                groups.add(cookbookGroup)
            }
        }
        for (i in groups.indices) {
            mTab.add(PagerCook620Item(groups[i].getFunctionName()))
            var table=table_layout_title.newTab().setCustomView(R.layout.item_custom_local_cook_book_fragment)
            table.view?.findViewById<TextView>(R.id.item_custom_local_book_name).text = groups[i].getFunctionName()
            if (i==0) {
                table.view?.findViewById<TextView>(R.id.item_custom_local_book_name)
                    .setTextColor(resources.getColor(R.color.local_title_select_new))
                table.view?.findViewById<TextView>(R.id.item_custom_local_book_name).textSize=resources.getDimension(R.dimen.sp_8)
            }
            table_layout_title.addTab(table)
        }
        val adapter = ViewPagerAdapter(
            childFragmentManager
        )
        view_pager_local_page.adapter = adapter

        view_pager_local_page.addOnPageChangeListener(object :
            ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
               for(index in 0..table_layout_title.tabCount){
                   var tab=table_layout_title.getTabAt(index)
                   val view1 = tab?.view ?: return
                   val txt1 = view1.findViewById<TextView>(R.id.item_custom_local_book_name) ?: return
                   if (index==position){

                       txt1.setTextColor(resources.getColor(R.color.local_title_select_new))
                       txt1.textSize=resources.getDimension(R.dimen.sp_8)
                   }else{
                       txt1.setTextColor(resources.getColor(R.color.local_title_un_select))
                       txt1.textSize=resources.getDimension(R.dimen.sp_6)

                   }
               }

            }


        })
        table_layout_title.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.e(TAG,"onTabSelected")
                val view1 = tab?.view ?: return
                val txt1 = view1.findViewById<TextView>(R.id.item_custom_local_book_name) ?: return
                txt1.setTextColor(resources.getColor(R.color.local_title_select))
                view_pager_local_page.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.e(TAG,"onTabUnselected")
                val view1 = tab?.view ?: return
                val txt1 = view1.findViewById<TextView>(R.id.item_custom_local_book_name) ?: return
                txt1.setTextColor(resources.getColor(R.color.local_title))


            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.e(TAG,"onTabReselected")

            }


        })

    }


    inner class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm), PagerSlidingTabStrip.TabItemName {
        override fun getItem(position: Int): Fragment {
            return if (mTab[position].createFragment(
                            groups[position]
                                .cookBookTagList as java.util.ArrayList<CookBookTag?>,
                            guid,
                            needDescalingParams) == null
                    ) {
                        mTab[position].createFragment(null, "", "", dt)
                    } else mTab[position].createFragment(
                        groups[position]
                            .cookBookTagList as java.util.ArrayList<CookBookTag?>,
                        guid,
                        needDescalingParams,
                        dt
                    )


        }

        override fun getCount(): Int {
            return mTab.size
        }

        override fun getTabName(position: Int): String {
            return mTab.get(position).getmTitle()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_local_new_cook_book, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: Bundle) =
            LocalNewCookBookFragment().apply {
                arguments = Bundle().apply {
                    putBundle(BUNNDEL, param1)

                }
            }
    }
}