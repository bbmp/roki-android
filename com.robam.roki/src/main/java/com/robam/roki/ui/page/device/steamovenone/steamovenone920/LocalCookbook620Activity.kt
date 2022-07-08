package com.robam.roki.ui.page.device.steamovenone.steamovenone920

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.tabs.TabLayout
import com.google.common.eventbus.Subscribe
import com.legent.plat.events.PageBackEvent
import com.robam.roki.R
import com.robam.roki.ui.PageArgumentKey
import com.robam.roki.ui.activity3.AppActivity
import com.robam.roki.ui.page.device.steamovenone.steamovenone920.fragment.CustomRecipeFragment
import com.robam.roki.ui.page.device.steamovenone.steamovenone920.fragment.LocalNewCookBookFragment
import com.tencent.mm.opensdk.utils.Log
import kotlinx.android.synthetic.main.activity_local_cookbook620.*
import kotlinx.android.synthetic.main.include_title_bar.*

class LocalCookbook620Activity : AppActivity(), TabLayout.OnTabSelectedListener {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView()
//    }


    var customRecipeFragment:Fragment?=null
    var localNewCookBookFragment:Fragment?=null
//    private var manager: FragmentManager? = null
//    private var transaction: FragmentTransaction? = null
//    private var currentFragment: Fragment? = null

    override fun getLayoutId(): Int =R.layout.activity_local_cookbook620
    private val list: ArrayList<String> = ArrayList()

//    @Subscribe
//    fun finishPage(mPageBackEvent: PageBackEvent){
//        if (mPageBackEvent.pageName.equals("Local620CookbookDetailPage")){
//            finish()
//        }
//    }

    companion object{
        @JvmStatic
        val bdLocalCookbook="BDLOCALCOOKBOOK"
    }
    var guid=""

    override fun initView() {
        var bundle=intent.getBundleExtra(bdLocalCookbook)
        guid= bundle.getString(PageArgumentKey.Guid).toString()

        list.add("本地 ")
        list.add("自定义")
//        var list={"本地","自定义"}
        tab_title.addTab(tab_title.newTab().setText(list[0]));
        tab_title.addTab(tab_title.newTab().setText(list[1]));

        //标签选中监听器
        tab_title.addOnTabSelectedListener(this);

        img_back.setOnClickListener {
            finish()
        }
        tv_title.text="自动菜谱"
        initLocalFragment()
//        showFragment(1)
//        showFragment(0)
    }

    override fun initData() {




    }




    private  val TAG = "LocalCookbook620Activit"
    override fun onTabSelected(tab: TabLayout.Tab?) {
        Log.e(TAG,"onTabSelected")
        tab?.position?.let { showFragment(it) }
    }


    private fun showFragment(position:Int){
        if (position ==1){
            initCustomFragment()
        }else if (position ==0){
            initLocalFragment()
        }

    }
    //隐藏Fragment
    private fun hideFragment(fragmentTransaction:FragmentTransaction ){
        //隐藏MessageFragment
        if(customRecipeFragment != null){
            fragmentTransaction.hide(customRecipeFragment!!);
        }
        //隐藏PersonFragment
        if(localNewCookBookFragment != null){
            fragmentTransaction.hide(localNewCookBookFragment!!);
        }

    }


    private fun initCustomFragment(){
        var fragmentTransaction = supportFragmentManager.beginTransaction();
        if(customRecipeFragment == null){
            customRecipeFragment =  CustomRecipeFragment.newInstance(guid);
            fragmentTransaction.add(R.id.local_cookbook_fragment,
                customRecipeFragment as CustomRecipeFragment
            )
        }
        hideFragment(fragmentTransaction);
        fragmentTransaction.show(customRecipeFragment!!)
        fragmentTransaction.commit()
    }

    private fun initLocalFragment(){
        var fragmentTransaction = supportFragmentManager.beginTransaction();
        if(localNewCookBookFragment == null){
            localNewCookBookFragment =  LocalNewCookBookFragment.newInstance(intent.getBundleExtra(bdLocalCookbook));
            fragmentTransaction.add(R.id.local_cookbook_fragment,
                localNewCookBookFragment as LocalNewCookBookFragment
            )
        }
        hideFragment(fragmentTransaction);
        fragmentTransaction.show(localNewCookBookFragment!!)
        fragmentTransaction.commit()
    }

    //正确的做法
    override fun onTabUnselected(p0: TabLayout.Tab?) {
        Log.e(TAG,"onTabUnselected")
    }

    override fun onTabReselected(p0: TabLayout.Tab?) {
        Log.e(TAG,"onTabReselected")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode== RESULT_OK&&requestCode==1000)
            finish()
    }

}