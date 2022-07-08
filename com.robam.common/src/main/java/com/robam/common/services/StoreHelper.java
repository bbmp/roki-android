package com.robam.common.services;

import android.util.Log;

import com.google.common.collect.Lists;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.legent.dao.DaoHelper;
import com.robam.common.io.cloud.Reponses.CookbooksResponse;
import com.robam.common.io.cloud.Reponses.HomeAdvertsForPadResponse;
import com.robam.common.pojos.AbsRecipe;
import com.robam.common.pojos.Advert;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.Recipe3rd;
import com.robam.common.pojos.Tag;
import com.robam.common.pojos.Tag_Recipe;
import com.robam.common.pojos.Tag_Recipe3rd;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by sylar on 15/7/7.
 */
public class StoreHelper {
    private static final String TAG = "dao";

//   未调用
//    static public List<Recipe> getCookooksByTag(Tag tag) {
//        // "SELECT * FROM `tinybook` WHERE `id` IN (SELECT `book_id` FROM `tag_book` WHERE `tag_id` = ? ) "
//        try {
//
//            PreparedQuery<Recipe> query = makeQuery_CookbookByTag();
//            query.setArgumentHolderValue(0, tag);
//
//            Dao<Recipe, Long> daoBook = DaoService.getInstance().getDao(Recipe.class);
//            List<Recipe> books = daoBook.query(query);
//            return books;
//        } catch (SQLException e) {
//            onException(e);
//            return Lists.newArrayList();
//        }
//    }
//
//    static public List<Recipe3rd> getThirdBooksByTag(Tag tag) {
//        try {
//            PreparedQuery<Recipe3rd> query2 = makeQuery_Cookbook3rdByTag();
//            query2.setArgumentHolderValue(0, tag);
//
//            Dao<Recipe3rd, Long> daoBook2 = DaoService.getInstance().getDao(Recipe3rd.class);
//            List<Recipe3rd> books2 = daoBook2.query(query2);
//            return books2;
//        } catch (SQLException e) {
//            onException(e);
//            return Lists.newArrayList();
//        }
//    }

    static public CookbooksResponse getTodayList() {
        List<Recipe> cookbooks = DaoHelper.getWhereEq(Recipe.class,
                AbsRecipe.COLUMN_isToday, true);
        List<Recipe3rd> cookbooks3rd = DaoHelper.getWhereEq(Recipe3rd.class,
                AbsRecipe.COLUMN_isToday, true);
        CookbooksResponse result = new CookbooksResponse();
        result.cookbooks = cookbooks;
        result.cookbooks3rd = cookbooks3rd;

        return result;
    }

    static public CookbooksResponse getFavorityList() {
        List<Recipe> cookbooks = DaoHelper.getWhereEq(Recipe.class,
                AbsRecipe.COLUMN_isFavority, true);
        List<Recipe3rd> cookbooks3rd = DaoHelper.getWhereEq(Recipe3rd.class,
                AbsRecipe.COLUMN_isFavority, true);
        CookbooksResponse result = new CookbooksResponse();
        result.cookbooks = cookbooks;
        result.cookbooks3rd = cookbooks3rd;

        return result;
    }

    static public CookbooksResponse getRecommendList() {
        List<Recipe> cookbooks = DaoHelper.getWhereEq(Recipe.class,
                AbsRecipe.COLUMN_isRecommend, true);
        List<Recipe3rd> cookbooks3rd = DaoHelper.getWhereEq(Recipe3rd.class,
                AbsRecipe.COLUMN_isRecommend, true);
        CookbooksResponse result = new CookbooksResponse();
        result.cookbooks = cookbooks;
        result.cookbooks3rd = cookbooks3rd;

        return result;
    }

    static public CookbooksResponse searchByName(String name) {
        List<Recipe> cookbooks = DaoHelper.getWhereEq(Recipe.class, "name", name);
        List<Recipe3rd> cookbooks3rd = DaoHelper.getWhereEq(Recipe3rd.class, "name", name);
        CookbooksResponse result = new CookbooksResponse();
        result.cookbooks = cookbooks;
        result.cookbooks3rd = cookbooks3rd;

        return result;
    }
// 未调用
//    static public HomeAdvertsForPadResponse getPadAdverts() {
//        HomeAdvertsForPadResponse result = new HomeAdvertsForPadResponse();
//
//        result.left = DaoHelper.getWhereEq(Advert.PadAdvert.class, Advert.PadAdvert.FIELD_Localtion,
//                Advert.PadAdvert.LEFT);
//        result.middle = DaoHelper.getWhereEq(Advert.PadAdvert.class, Advert.PadAdvert.FIELD_Localtion,
//                Advert.PadAdvert.MIDDLE);
//
//        return result;
//    }


    // --------------------------------------------------------------------------------------------------
    //
    // --------------------------------------------------------------------------------------------------

    /**
     * 构造查询（多对多关系下） 未调用
     */
//    static private PreparedQuery<Recipe> makeQuery_CookbookByTag()
//            throws SQLException {
//
//        Dao<Tag_Recipe, Long> daoTagBook = DaoService.getInstance().getDao(Tag_Recipe.class);
//        // 创建一个内关联查询用户项目表
//        QueryBuilder<Tag_Recipe, Long> qbTagBook = daoTagBook.queryBuilder();
//
//        // SELECT `book_id` FROM `tagbook` WHERE `tag_id` = ?
//        // select book_id
//        qbTagBook.selectColumns(Tag_Recipe.COLUMN_BOOK_ID);
//        SelectArg arg = new SelectArg();
//        // where tag_id = ?
//        qbTagBook.where().eq(Tag_Recipe.COLUMN_TAG_ID, arg);
//
//        // 创建外部查询项目表
//        Dao<Recipe, Long> daoBook = DaoService.getInstance().getDao(Recipe.class);
//        QueryBuilder<Recipe, Long> qbCookbook = daoBook.queryBuilder();
//
//        // SELECT * FROM `ThumbCookbook` WHERE `id` IN ()
//        qbCookbook.orderBy(Recipe.COLUMN_ID, true).where()
//                .in(Recipe.COLUMN_ID, qbTagBook);
//
//        /**
//         * 这里返回时完整的sql语句为 "SELECT * FROM `ThumbCookbook` WHERE `book_id` IN (
//         * SELECT `book_id` FROM `tagbook` WHERE `tag_id` = ? ) "
//         */
//        return qbCookbook.prepare();
//    }
//
//    /**
//     * 构造查询（多对多关系下）
//     */
//    static private PreparedQuery<Recipe3rd> makeQuery_Cookbook3rdByTag()
//            throws SQLException {
//
//        Dao<Tag_Recipe3rd, Long> daoTagBook = DaoService.getInstance().getDao(Tag_Recipe3rd.class);
//        // 创建一个内关联查询用户项目表
//        QueryBuilder<Tag_Recipe3rd, Long> qbTagBook = daoTagBook.queryBuilder();
//
//        // SELECT `book_id` FROM `tagbook` WHERE `tag_id` = ?
//        qbTagBook.selectColumns(Tag_Recipe3rd.COLUMN_BOOK_ID);
//        SelectArg userSelectArg = new SelectArg();
//        qbTagBook.where().eq(Tag_Recipe3rd.COLUMN_TAG_ID, userSelectArg);
//
//        // 创建外部查询项目表
//        Dao<Recipe3rd, Long> daoBook = DaoService.getInstance().getDao(Recipe3rd.class);
//        QueryBuilder<Recipe3rd, Long> qbCookbook = daoBook.queryBuilder();
//
//        // SELECT * FROM `ThumbCookbook` WHERE `id` IN ()
//        qbCookbook.where().in("id", qbTagBook);
//
//        /**
//         * 这里返回时完整的sql语句为 "SELECT * FROM `ThumbCookbook` WHERE `book_id` IN (
//         * SELECT `book_id` FROM `tagbook` WHERE `tag_id` = ? ) "
//         */
//        return qbCookbook.prepare();
//    }
//
//    static void onException(Exception e) {
//        Log.e(TAG, e.getMessage());
//        e.printStackTrace();
//    }
}
