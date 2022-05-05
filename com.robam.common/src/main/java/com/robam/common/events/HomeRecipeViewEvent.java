package com.robam.common.events;

/**
 * Created by rent on 2016/9/16.
 */

public class HomeRecipeViewEvent {
    public static final int RecipeFavoriteChange = 1;//菜谱收藏
    public static final int ThemeFavoriteChange = 2;//主题收藏
    public static final int ClearMyFavorite = 3;//清空我的收藏事件
    public static final int RecipeDetailPageBackToTheme = 4;//菜谱详情页面退出主题
    public static final int ThemeDetailPageBackToHomeRecipe = 5;//主题详情页面退出
    public static final int RecipeDetailPageBackToDynamicShow = 6;//菜谱详情退出动态厨艺
    public static final int DynamicShowToHomePage = 7;//动态厨艺退出至主页面
    public static final int LiveListToHomePage = 8;//视频列表退出至主页面
    public static final int RecipeDetailPageBackToDeviceRecipePage = 9;//菜谱详情退出至设备页面
    public static final int DeviceRecipeBackHomePage=10;//设备菜谱返回主页面
    public static final int RecipeDetailPageBackToHomePage =11;//菜谱详情页面退出home
    public static final int BackToMoreRecipe =12;//更多菜谱返回
    public static final int RecipeShowMomnet=13;//晒菜谱厨艺事件
    public static final int ThemeDetailBackMyCollect=14;//主题详情页返回我的收藏

    public int flag = 0;

    public HomeRecipeViewEvent(int f) {
        this.flag = f;
    }
}
