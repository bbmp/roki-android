package com.robam.common.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.common.collect.Lists;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.legent.dao.AbsDbHelper;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.device.SubDeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.paramCode;
import com.robam.common.pojos.Advert.MobAdvert;
import com.robam.common.pojos.Advert.PadAdvert;
import com.robam.common.pojos.AdvertImage;
import com.robam.common.pojos.CookAlbum;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.CookStepTip;
import com.robam.common.pojos.CookStepTipMaterial;
import com.robam.common.pojos.Dc;
import com.robam.common.pojos.DeviceGroupList;
import com.robam.common.pojos.DeviceItemList;
import com.robam.common.pojos.Group;
import com.robam.common.pojos.Material;
import com.robam.common.pojos.Materials;
import com.robam.common.pojos.PlatformCode;
import com.robam.common.pojos.PreStep;
import com.robam.common.pojos.PreSubStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.Recipe3rd;
import com.robam.common.pojos.RecipeLiveList;
import com.robam.common.pojos.RecipeProvider;
import com.robam.common.pojos.RecipeTheme;
import com.robam.common.pojos.RuleStream;
import com.robam.common.pojos.Rules;
import com.robam.common.pojos.SteamUserAction;
import com.robam.common.pojos.SysCfg;
import com.robam.common.pojos.Tag;
import com.robam.common.pojos.Tag_Recipe;
import com.robam.common.pojos.Tag_Recipe3rd;
import com.robam.common.pojos.UserAction;
import com.robam.common.pojos.OvenUserAction;
import java.sql.SQLException;
import java.util.List;

public class DbHelper extends AbsDbHelper {


    static List<Class<?>> tables = Lists.newLinkedList();

    static public DbHelper newHelper(Context cx, String dbName, int dbVersion) {
        tables.clear();
        // ---------------------------------------------
        tables.add(SysCfg.class);
        // ---------------------------------------------
        tables.add(MobAdvert.class);
        tables.add(PadAdvert.class);
        tables.add(AdvertImage.class);
        tables.add(CookAlbum.class);
        tables.add(RecipeProvider.class);
        //菜谱分类 周定钧
        tables.add(Group.class);
        tables.add(Tag.class);

        tables.add(UserAction.class);
        tables.add(OvenUserAction.class);
        tables.add(SteamUserAction.class);


        //联网优化 周定钧
        tables.add(DeviceGroupList.class);
        tables.add(DeviceItemList.class);
        // ---------------------------------------------
        tables.add(Recipe.class);
        tables.add(PreStep.class);
        tables.add(PreSubStep.class);
        tables.add(CookStep.class);
        tables.add(CookStepTip.class);
        tables.add(CookStepTipMaterial.class);
        tables.add(Materials.class);
        tables.add(Material.class);
        //周定钧
        tables.add(Dc.class);
        tables.add(PlatformCode.class);
        tables.add(paramCode.class);
        tables.add(RecipeTheme.class);
        //殷威

        // ---------------------------------------------
        tables.add(Recipe3rd.class);
        tables.add(Tag_Recipe.class);
        tables.add(Tag_Recipe3rd.class);
        tables.add(RecipeLiveList.class);
        tables.add(SubDeviceInfo.class);
        tables.add(DeviceInfo.class);
        tables.add(RuleStream.class);
        tables.add(Rules.class);
        // ---------------------------------------------
        return new DbHelper(cx, dbName, dbVersion);
    }

    private DbHelper(Context cx, String dbName, int dbVersion) {
        super(cx, dbName, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sd, ConnectionSource cs) {
        super.onCreate(sd, cs);

        try {

            for (Class<?> clazz : tables) {
                TableUtils.createTable(cs, clazz);
                Log.d(TAG, "table created :" + clazz.getSimpleName());
                LogUtils.i("20170825","table created::"+clazz.getSimpleName());
            }

            Log.d(TAG, String.format("DB created:%s table count:%s",
                    getDatabaseName(), tables.size()));

        } catch (Exception e) {
            Log.e(TAG, "DB created error:" + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sd, ConnectionSource cs,
                          int oldVersion, int newVersion) {

        super.onUpgrade(sd, cs, oldVersion, newVersion);
        dropAll(cs);
        onCreate(sd, cs);
    }

    private void dropAll(ConnectionSource cs) {

        try {
            for (Class<?> clazz : tables) {
                TableUtils.dropTable(cs, clazz, true);
            }

            Log.d(TAG, "DB droped");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}