package com.robam.roki.ui.view.recipeclassify;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.Tag;
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;
import com.robam.roki.utils.ToolUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class RecipeClassiflyItemView extends FrameLayout {
    @InjectView(R.id.img_recipe)
    ImageView img_recipe;
    String NetImgUrls;
    Tag tag;

    public RecipeClassiflyItemView(Context context, Tag tag) {
        super(context);
        init(context, null, tag);
    }

    public RecipeClassiflyItemView(Context context, AttributeSet attrs, Tag tag) {
        super(context, attrs);
        init(context, attrs, tag);
    }

    public RecipeClassiflyItemView(Context context, AttributeSet attrs, int defStyle, Tag tag) {
        super(context, attrs, defStyle);
        init(context, attrs, tag);
    }

    public void init(Context context, AttributeSet attrs, Tag tag) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipeclassiflyitemview,
                this, true);
        this.tag = tag;
        NetImgUrls = tag.imageUrl;
        LogUtils.i("20170306", "NetImgUrls:" + NetImgUrls);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
            this.tag = tag;
            NetImgUrls = tag.imageUrl;
            LogUtils.i("20170306", "NetImgUrls:" + NetImgUrls);
            if (NetImgUrls != null) {
                Glide.with(context).load(NetImgUrls).into(img_recipe);
            } else {

            }
        }
    }

    @OnClick(R.id.img_recipe)
    public void clickimg_recipe() {
        LogUtils.i("20170807", "Tag::" + tag.getParent().getName());
        selectClassify(tag);
        ToolUtils.logEvent("菜谱", "点击:" + tag.getParent().getName() + ":" +tag.getName(), "roki_美食");
        Bundle bundle = new Bundle();
        bundle.putParcelable("Tag", tag);
        UIService.getInstance().postPage(PageKey.RecipeClassifySearch, bundle);
    }

    private void selectClassify(Tag tag) {
        if ("热门推荐".equals(tag.getParent().getName())) {
            hantoEngHot(tag.getName());
//            ToolUtils.sendAnalystics(mTracker,"Hot",temp,"Classification");
        } else if ("食材分类".equals(tag.getParent().getName())) {
            hantoEngMaterial(tag.getName());
//            ToolUtils.sendAnalystics(mTracker,"Material",temp,"Classification");
            LogUtils.i("20170808", "tag.name::" + tag.getName() + "  temp::::::" + temp);
        } else if ("菜系分类".equals(tag.getParent().getName())) {
            hantoEngStyleofCooking(tag.getName());
//            ToolUtils.sendAnalystics(mTracker,"StyleofCooking",temp,"Classification");
            LogUtils.i("20170808", "tag.name::" + tag.getName() + "  temp::::::" + temp);
        } else if ("厨电分类".equals(tag.getParent().getName())) {
            hantoEngKitchen(tag.getName());
//            ToolUtils.sendAnalystics(mTracker,"Kitchen",temp,"Classification");
        } else if ("口味分类".equals(tag.getParent().getName())) {
            hantoEngTaste(tag.getName());
//            ToolUtils.sendAnalystics(mTracker,"Taste",temp,"Classification");
        }
    }

    private String temp = null;

    private void hantoEngHot(String str) {
        if ("瘦身菜".equals(str)) {
            temp = "SlimingRecipe";
        } else if ("养生菜".equals(str)) {
            temp = "HealthRecipe";
        } else if ("失传菜".equals(str)) {
            temp = "LostRecipe";
        } else if ("创意菜".equals(str)) {
            temp = "Creativerecipe";
        }
    }

    private void hantoEngMaterial(String str) {
        if ("豆制品".equals(str)) {
            temp = "BeanFood";
        } else if ("果蔬类".equals(str)) {
            temp = "GardenStuff";
        } else if ("红肉类".equals(str)) {
            temp = "RedMeat";
        } else if ("蛋禽类".equals(str)) {
            temp = "LayingHen";
        } else if ("甜品类".equals(str)) {
            temp = "Sweets";
        } else if ("水产品".equals(str)) {
            temp = "AguaticProduct";
        } else if ("主食类".equals(str)) {
            temp = "StapleFood";
        }
    }

    private void hantoEngStyleofCooking(String str) {
        if ("鲁菜".equals(str)) {
            temp = "ShanDongRecipe";
        } else if ("粤菜".equals(str)) {
            temp = "GuangZhouRecipe";
        } else if ("西餐".equals(str)) {
            temp = "EuropeanFood";
        } else if ("湘菜".equals(str)) {
            temp = "HuNanRecipe";
        } else if ("浙菜".equals(str)) {
            temp = "ZhejiangRecipe";
        } else if ("闽菜".equals(str)) {
            temp = "FujianRecipe";
        } else if ("徽菜".equals(str)) {
            temp = "AnHuiRecipe";
        } else if ("川菜".equals(str)) {
            temp = "SiChuanRecipe";
        } else if ("苏菜".equals(str)) {
            temp = "JiangSuRecipe";
        }
    }

    private void hantoEngKitchen(String str) {
        if ("蒸烤专用".equals(str)) {
            temp = "ZKZY";
        } else if ("蒸汽炉".equals(str)) {
            temp = "ZQL";
        } else if ("微波炉".equals(str)) {
            temp = "WBL";
        } else if ("电烤箱".equals(str)) {
            temp = "KX";
        } else if ("灶具".equals(str)) {
            temp = "ZJ";
        }

    }

    private void hantoEngTaste(String str) {
        if ("果香".equals(str)) {
            temp = "FruityFlavor";
        } else if ("奶香".equals(str)) {
            temp = "MilkFlavor";
        } else if ("酸".equals(str)) {
            temp = "Sour";
        } else if ("麻酱".equals(str)) {
            temp = "SesamePaste";
        } else if ("清淡".equals(str)) {
            temp = "Insipidity";
        } else if ("孜然味".equals(str)) {
            temp = "Cumin";
        } else if ("苦香".equals(str)) {
            temp = "Cascarilla";
        } else if ("咖喱".equals(str)) {
            temp = "Curry";
        } else if ("黑椒".equals(str)) {
            temp = "BlackPepper";
        } else if ("香辣".equals(str)) {
            temp = "Spicy";
        } else if ("麻辣".equals(str)) {
            temp = "Pungent";
        } else if ("甜辣".equals(str)) {
            temp = "SweetChilli";
        } else if ("鲜".equals(str)) {
            temp = "Fresh";
        } else if ("怪".equals(str)) {
            temp = "Odd";
        } else if ("五香".equals(str)) {
            temp = "FiveSpices";
        } else if ("辣".equals(str)) {
            temp = "Pepper";
        } else if ("酸辣".equals(str)) {
            temp = "VinegarPepper";
        } else if ("茄汁".equals(str)) {
            temp = "TomatoJuice";
        } else if ("咸鲜".equals(str)) {
            temp = "Salty";
        } else if ("鱼香".equals(str)) {
            temp = "FragrantFish";
        } else if ("蒜香".equals(str)) {
            temp = "Garlic";
        } else if ("芥末".equals(str)) {
            temp = "Mustard";
        } else if ("姜汁".equals(str)) {
            temp = "GingerJuice";
        } else if ("酱香".equals(str)) {
            temp = "Soysuce";
        } else if ("微辣".equals(str)) {
            temp = "Spicy";
        } else if ("香".equals(str)) {
            temp = "SweetSmelling";
        } else if ("葱香".equals(str)) {
            temp = "CongXiang";
        } else if ("酸甜".equals(str)) {
            temp = "SourSweet";
        } else if ("豆瓣".equals(str)) {
            temp = "Valve";
        } else if ("咸".equals(str)) {
            temp = "Salty";
        } else if ("苦".equals(str)) {
            temp = "Bitter";
        } else if ("麻".equals(str)) {
            temp = "Hemp";
        } else if ("甜".equals(str)) {
            temp = "Sweet";
        } else if ("糊辣".equals(str)) {
            temp = "Spicy";
        } else if ("椒麻".equals(str)) {
            temp = "PepperHemp";
        } else if ("清甜".equals(str)) {
            temp = "FreshSweet";
        }


    }

}
