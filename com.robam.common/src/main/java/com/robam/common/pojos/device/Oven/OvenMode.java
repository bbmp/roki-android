package com.robam.common.pojos.device.Oven;

/**
 * Created by zhoudingjun on 2016/7/23.
 */
public interface OvenMode {
    String DEFAULTMODE = "无模式";
    String FASTHEAT = "快热";
    String WINDBAKED = "风焙烤";
    String BAKED = "焙烤";
    String BOTTOMHEATING = "底加热";
    String UNFREEZE = "解冻";
    String FANBAKED = "风扇烤";
    String BARBECUE = "烤烧";
    String STRONGBARBECUE = "强烤烧";
    String FASTORDERHEAT = "快速预热";


    //自动模式
    short None = 0;
    short BEEF = 1;//牛肉
    short BREAD = 2;//面包
    short BISCUITS = 3;//饼干
    short CHICKENWINGS = 4;//鸡翅
    short CAKE = 5;//蛋糕
    short PIZZA = 6;//披萨
    short GRILLEDSHRIMP = 7;//虾
    short ROASTFISH = 8;//烤鱼
    short SWEETPOTATO = 9;//红薯
    short CORN = 10;//玉米
    short STREAKYPORK = 11;//五花肉
    short VEGETABLES = 12;//蔬菜
    short CRISPY_ROST_DUCK = 13;//脆皮烤鸭
    short FRUIT_ROST_DUCK = 14;//果香烤鸭
    short BACON_ORGAN_BAKED_POTATOES = 15;//培根风琴烤土豆
    short RACK_OF_LAMB = 16;//迷迭香烤羊排
    short SEAFOOD_SOUP = 17;//酥皮海鲜浓汤
    short BROWNIE = 18;//布朗尼蛋糕
    short EGG_TARTS = 19;//葡式蛋挞
    short LEMON_PIE = 20;//柠檬派
    short WUREN_MOON_CAKE = 21;//广式五仁月饼
    short TOMATO_BREADED_SHRIMP = 22;//番茄面包虾
    short ROAST_STEAK = 23;//烤牛排
    short TIN_FRIED_FISH = 24;//锡香烤鱼
    short CHICKEN_WINGS = 25;//孜然鸡翅
    short PORK_CHOPS = 26;//芝心猪排
    short DRIED_BEEF = 27;//牛肉干
    short BACON_VEGETABLES_ROLLS = 28;//培根蔬菜卷
    short INTESTINAL_PACKAGE = 29;//肠仔包
    short TOAST = 30;//烤面包片
    short BACKED_PIZZA = 31;//烤披萨
    short GRILLED_SHRIMP = 32;//烤虾
    short BAKED_SWEET_POTATO = 33;//烤红薯
    short ROASTED_COM = 34;//烤玉米
    short ROASTED_PORK = 35;//烤五花肉
    short BRAZILIAN_BARBECUE = 36;//巴西烤肉
    short CHEESE_VEGETABLES = 37;//奶酪焗时蔬
    short CHERRY_TOMATO = 38;//奶酪烤鸡胸配樱桃番茄
    short CHEESE_SEAFOOD_RISOTTO = 39;//芝士海鲜焗饭
    short SWEETHEART_CAKE = 40;//老婆饼
    short PARMA_HAM_LASAGNA = 41;//帕尔马火腿千层面
    short GRILLED_SALMON = 42;//香烤三文鱼
    short TINFOIL_GRILLED_RIBS = 43;//锡纸烤排骨
    short ASSORTED_SKEWERS = 44;//什锦烤串
    short CRANBERRY_BISCUITS = 45;//蔓越莓饼干
    short DURIAN_CRISP = 46;//榴莲酥
    short BUTTER_COOKIE = 47;//黄油曲奇


    short KUAIRE = 1;//快热
    short FENGBEIKAO = 2;//风焙烤
    short BEIKAO = 3;//焙烤
    short DIJIARE = 4;//底加热
    short JIEDONG = 5;//解冻
    short FENGSHANKAO = 6;//风扇烤
    short KAOSHAO = 7;//烤烧
    short QIANGKAOSHAO = 8;//强烤烧
    short EXP = 9;//EXP
    short KUAISUYURE = 10;//快速预热
    short JIANKAO = 11;//煎烤
    short GUOSHUHONGGAN = 12;//果蔬烘干
    short FAJIAO = 13;//发酵
    short SHAJUN = 14;//杀菌
    short BAOWEN = 15;//保温
    short SCFJ_KR = 16;//上层分区快热
    short SCFJ_FSK = 17;//上层分区风扇考
    short SCFJ_FBK = 18;//上层分区风焙烤
    short SCFJ_SK = 19;//上层分区烧烤
    short XCFJ_FBK = 20;//下层分区风焙烤
    short XCFJ_JK = 21;//下层分区煎烤
    short SKR_XFBK = 22;//上快热+下风焙烤
    short SFBK_XFBK = 23;//上风焙烤下风焙烤
    short SFBKXJK = 24;//上风焙烤下煎烤
    short SSK_XFBK = 25;//上烧烤下风焙烤


}
