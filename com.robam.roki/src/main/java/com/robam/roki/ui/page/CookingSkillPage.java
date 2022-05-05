package com.robam.roki.ui.page;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.legent.ui.ext.BasePage;
import com.robam.roki.R;

/**
 * Created by zhoudingjun on 2017/6/5.
 */

public class CookingSkillPage extends BasePage {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_cooking_skill, null, false);
        return view;
    }
}
