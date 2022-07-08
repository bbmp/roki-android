package com.robam.roki.net.request.bean



data class RecipeCustomBean(
    val datas: List<RecipeCustomData>,
    val hasNext: Boolean,
    val msg: String,
    val rc: Int,
    val totalPage: Int,
    val totalSize: Int
)

data class RecipeCustomData(
    val coverImg: String,
    val createDateTime: String,
    val dataStatus: Int,
    val id: Int,
    val name: String,
    val refCookbookMultiId: Int,
    val updateDateTime: String,
    val userId: Int,
    val time:Int
){



}
