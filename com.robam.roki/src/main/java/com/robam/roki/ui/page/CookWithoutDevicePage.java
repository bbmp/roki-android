package com.robam.roki.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.legent.Callback;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.Recipe;
import com.robam.common.services.CookbookManager;
import com.robam.roki.R;
import com.robam.roki.service.MobileStoveCookTaskService;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/14.
 */
public class CookWithoutDevicePage extends BasePage {

    Recipe book;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page_cook_without_device, container, false);
        ButterKnife.inject(this, view);

        long bookId = getArguments().getLong(PageArgumentKey.BookId);
        RokiRestHelper.getCookbookById(bookId, Reponses.CookbookResponse.class, new RetrofitCallback<Reponses.CookbookResponse>() {
            @Override
            public void onSuccess(Reponses.CookbookResponse cookbookResponse) {
                if (null != cookbookResponse)
                    book = cookbookResponse.cookbook;
            }

            @Override
            public void onFaild(String err) {

            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.toWeb)
    public void onClickToWeb() {
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Url, "http://www.robam.com/");
        UIService.getInstance().postPage(PageKey.WebClient, bd);
    }

    @OnClick(R.id.toCook)
    public void onClickToCook() {
        if (book != null) {
            MobileStoveCookTaskService.getInstance().start(null, book,"0");
        }
    }
}
