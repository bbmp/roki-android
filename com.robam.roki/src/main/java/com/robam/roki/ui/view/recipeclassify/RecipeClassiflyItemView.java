//package com.robam.roki.ui.view.recipeclassify;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.legent.ui.UIService;
//import com.legent.utils.LogUtils;
//import com.robam.common.pojos.Tag;
//import com.robam.roki.R;
//import com.robam.roki.ui.PageKey;
//import com.robam.roki.utils.ToolUtils;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//
//
//public class RecipeClassiflyItemView extends FrameLayout {
//    @InjectView(R.id.img_recipe)
//    ImageView img_recipe;
//    String NetImgUrls;
//    Tag tag;
//
//    public RecipeClassiflyItemView(Context context, Tag tag) {
//        super(context);
//        init(context, null, tag);
//    }
//
//    public RecipeClassiflyItemView(Context context, AttributeSet attrs, Tag tag) {
//        super(context, attrs);
//        init(context, attrs, tag);
//    }
//
//    public RecipeClassiflyItemView(Context context, AttributeSet attrs, int defStyle, Tag tag) {
//        super(context, attrs, defStyle);
//        init(context, attrs, tag);
//    }
//
//    public void init(Context context, AttributeSet attrs, Tag tag) {
//        View view = LayoutInflater.from(context).inflate(R.layout.recipeclassiflyitemview,
//                this, true);
//        this.tag = tag;
//        NetImgUrls = tag.imageUrl;
//        LogUtils.i("20170306", "NetImgUrls:" + NetImgUrls);
//        if (!view.isInEditMode()) {
//            ButterKnife.inject(this, view);
//            this.tag = tag;
//            NetImgUrls = tag.imageUrl;
//            LogUtils.i("20170306", "NetImgUrls:" + NetImgUrls);
//            if (NetImgUrls != null) {
//                Glide.with(context).load(NetImgUrls).into(img_recipe);
//            } else {
//
//            }
//        }
//    }
//
//    @OnClick(R.id.img_recipe)
//    public void clickimg_recipe() {
//        LogUtils.i("20170807", "Tag::" + tag.getParent().getName());
//        selectClassify(tag);
//        ToolUtils.logEvent("??????", "??????:" + tag.getParent().getName() + ":" +tag.getName(), "roki_??????");
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("Tag", tag);
//        UIService.getInstance().postPage(PageKey.RecipeClassifySearch, bundle);
//    }
//
//    private void selectClassify(Tag tag) {
//        if ("????????????".equals(tag.getParent().getName())) {
//            hantoEngHot(tag.getName());
////            ToolUtils.sendAnalystics(mTracker,"Hot",temp,"Classification");
//        } else if ("????????????".equals(tag.getParent().getName())) {
//            hantoEngMaterial(tag.getName());
////            ToolUtils.sendAnalystics(mTracker,"Material",temp,"Classification");
//            LogUtils.i("20170808", "tag.name::" + tag.getName() + "  temp::::::" + temp);
//        } else if ("????????????".equals(tag.getParent().getName())) {
//            hantoEngStyleofCooking(tag.getName());
////            ToolUtils.sendAnalystics(mTracker,"StyleofCooking",temp,"Classification");
//            LogUtils.i("20170808", "tag.name::" + tag.getName() + "  temp::::::" + temp);
//        } else if ("????????????".equals(tag.getParent().getName())) {
//            hantoEngKitchen(tag.getName());
////            ToolUtils.sendAnalystics(mTracker,"Kitchen",temp,"Classification");
//        } else if ("????????????".equals(tag.getParent().getName())) {
//            hantoEngTaste(tag.getName());
////            ToolUtils.sendAnalystics(mTracker,"Taste",temp,"Classification");
//        }
//    }
//
//    private String temp = null;
//
//    private void hantoEngHot(String str) {
//        if ("?????????".equals(str)) {
//            temp = "SlimingRecipe";
//        } else if ("?????????".equals(str)) {
//            temp = "HealthRecipe";
//        } else if ("?????????".equals(str)) {
//            temp = "LostRecipe";
//        } else if ("?????????".equals(str)) {
//            temp = "Creativerecipe";
//        }
//    }
//
//    private void hantoEngMaterial(String str) {
//        if ("?????????".equals(str)) {
//            temp = "BeanFood";
//        } else if ("?????????".equals(str)) {
//            temp = "GardenStuff";
//        } else if ("?????????".equals(str)) {
//            temp = "RedMeat";
//        } else if ("?????????".equals(str)) {
//            temp = "LayingHen";
//        } else if ("?????????".equals(str)) {
//            temp = "Sweets";
//        } else if ("?????????".equals(str)) {
//            temp = "AguaticProduct";
//        } else if ("?????????".equals(str)) {
//            temp = "StapleFood";
//        }
//    }
//
//    private void hantoEngStyleofCooking(String str) {
//        if ("??????".equals(str)) {
//            temp = "ShanDongRecipe";
//        } else if ("??????".equals(str)) {
//            temp = "GuangZhouRecipe";
//        } else if ("??????".equals(str)) {
//            temp = "EuropeanFood";
//        } else if ("??????".equals(str)) {
//            temp = "HuNanRecipe";
//        } else if ("??????".equals(str)) {
//            temp = "ZhejiangRecipe";
//        } else if ("??????".equals(str)) {
//            temp = "FujianRecipe";
//        } else if ("??????".equals(str)) {
//            temp = "AnHuiRecipe";
//        } else if ("??????".equals(str)) {
//            temp = "SiChuanRecipe";
//        } else if ("??????".equals(str)) {
//            temp = "JiangSuRecipe";
//        }
//    }
//
//    private void hantoEngKitchen(String str) {
//        if ("????????????".equals(str)) {
//            temp = "ZKZY";
//        } else if ("?????????".equals(str)) {
//            temp = "ZQL";
//        } else if ("?????????".equals(str)) {
//            temp = "WBL";
//        } else if ("?????????".equals(str)) {
//            temp = "KX";
//        } else if ("??????".equals(str)) {
//            temp = "ZJ";
//        }
//
//    }
//
//    private void hantoEngTaste(String str) {
//        if ("??????".equals(str)) {
//            temp = "FruityFlavor";
//        } else if ("??????".equals(str)) {
//            temp = "MilkFlavor";
//        } else if ("???".equals(str)) {
//            temp = "Sour";
//        } else if ("??????".equals(str)) {
//            temp = "SesamePaste";
//        } else if ("??????".equals(str)) {
//            temp = "Insipidity";
//        } else if ("?????????".equals(str)) {
//            temp = "Cumin";
//        } else if ("??????".equals(str)) {
//            temp = "Cascarilla";
//        } else if ("??????".equals(str)) {
//            temp = "Curry";
//        } else if ("??????".equals(str)) {
//            temp = "BlackPepper";
//        } else if ("??????".equals(str)) {
//            temp = "Spicy";
//        } else if ("??????".equals(str)) {
//            temp = "Pungent";
//        } else if ("??????".equals(str)) {
//            temp = "SweetChilli";
//        } else if ("???".equals(str)) {
//            temp = "Fresh";
//        } else if ("???".equals(str)) {
//            temp = "Odd";
//        } else if ("??????".equals(str)) {
//            temp = "FiveSpices";
//        } else if ("???".equals(str)) {
//            temp = "Pepper";
//        } else if ("??????".equals(str)) {
//            temp = "VinegarPepper";
//        } else if ("??????".equals(str)) {
//            temp = "TomatoJuice";
//        } else if ("??????".equals(str)) {
//            temp = "Salty";
//        } else if ("??????".equals(str)) {
//            temp = "FragrantFish";
//        } else if ("??????".equals(str)) {
//            temp = "Garlic";
//        } else if ("??????".equals(str)) {
//            temp = "Mustard";
//        } else if ("??????".equals(str)) {
//            temp = "GingerJuice";
//        } else if ("??????".equals(str)) {
//            temp = "Soysuce";
//        } else if ("??????".equals(str)) {
//            temp = "Spicy";
//        } else if ("???".equals(str)) {
//            temp = "SweetSmelling";
//        } else if ("??????".equals(str)) {
//            temp = "CongXiang";
//        } else if ("??????".equals(str)) {
//            temp = "SourSweet";
//        } else if ("??????".equals(str)) {
//            temp = "Valve";
//        } else if ("???".equals(str)) {
//            temp = "Salty";
//        } else if ("???".equals(str)) {
//            temp = "Bitter";
//        } else if ("???".equals(str)) {
//            temp = "Hemp";
//        } else if ("???".equals(str)) {
//            temp = "Sweet";
//        } else if ("??????".equals(str)) {
//            temp = "Spicy";
//        } else if ("??????".equals(str)) {
//            temp = "PepperHemp";
//        } else if ("??????".equals(str)) {
//            temp = "FreshSweet";
//        }
//
//
//    }
//
//}
