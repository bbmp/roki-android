package com.robam.common.services;

import com.legent.Callback;
import com.legent.Helper;
import com.legent.dao.DaoHelper;
import com.legent.services.AbsService;
import com.robam.common.io.cloud.Reponses.HomeAdvertsForPadResponse;
import com.robam.common.pojos.Advert.MobAdvert;
import com.robam.common.pojos.AdvertImage;

import java.util.List;

public class AdvertManager extends AbsService {

    static private AdvertManager instance = new AdvertManager();

    synchronized static public AdvertManager getInstance() {
        return instance;
    }

    StoreService ss = StoreService.getInstance();

    private AdvertManager() {
    }

    public void getMobAdverts(final Callback<List<MobAdvert>> callback) {
        ss.getHomeAdvertsForMob(new Callback<List<MobAdvert>>() {

            @Override
            public void onSuccess(List<MobAdvert> result) {
                Helper.onSuccess(callback, result);
            }

            @Override
            public void onFailure(Throwable t) {
                List<MobAdvert> list = DaoHelper.getAll(MobAdvert.class);
                Helper.onSuccess(callback, list);
            }
        });

    }

    public void getPadAdverts(final Callback<HomeAdvertsForPadResponse> callback) {
        ss.getHomeAdvertsForPad(new Callback<HomeAdvertsForPadResponse>() {

            @Override
            public void onSuccess(HomeAdvertsForPadResponse result) {
                Helper.onSuccess(callback, result);
            }

            @Override
            public void onFailure(Throwable t) {
                HomeAdvertsForPadResponse result = StoreHelper.getPadAdverts();
                Helper.onSuccess(callback, result);
            }
        });
    }

    public void getPadFavorityImages(final Callback<List<AdvertImage>> callback) {
        ss.getFavorityImagesForPad(new Callback<List<AdvertImage>>() {

            @Override
            public void onSuccess(List<AdvertImage> result) {
                Helper.onSuccess(callback, result);
            }

            @Override
            public void onFailure(Throwable t) {
                List<AdvertImage> result = DaoHelper.getWhereEq(
                        AdvertImage.class, AdvertImage.FIELD_isFavority, true);
                Helper.onSuccess(callback, result);
            }
        });
    }

    public void getPadRecommendImages(final Callback<List<AdvertImage>> callback) {
        ss.getRecommendImagesForPad(new Callback<List<AdvertImage>>() {

            @Override
            public void onSuccess(List<AdvertImage> result) {
                Helper.onSuccess(callback, result);
            }

            @Override
            public void onFailure(Throwable t) {
                List<AdvertImage> result = DaoHelper.getWhereEq(
                        AdvertImage.class, AdvertImage.FIELD_isRecommend, true);
                Helper.onSuccess(callback, result);
            }
        });
    }

    public void getPadAllBookImages(final Callback<List<AdvertImage>> callback) {
        ss.getAllBookImagesForPad(new Callback<List<AdvertImage>>() {

            @Override
            public void onSuccess(List<AdvertImage> result) {
                Helper.onSuccess(callback, result);
            }

            @Override
            public void onFailure(Throwable t) {
                List<AdvertImage> result = DaoHelper.getWhereEq(
                        AdvertImage.class, AdvertImage.FIELD_isInAll, true);
                Helper.onSuccess(callback, result);
            }
        });
    }

    // ------------------------------------------------------------------------------------------------------------------


}
