package com.robam.roki.ui.page.device.pot;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.utils.ByteUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.TimeUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamOvenOpenDoorSteamEvent;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.ThemeRecipeMultipleItem;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.CommonItemDecoration;
import com.robam.roki.ui.adapter.LocalBookAdapter;
import com.robam.roki.ui.adapter.RecipeTopicAdapter;
import com.robam.roki.ui.adapter3.PotMoreMenuAdapter;
import com.robam.roki.ui.adapter3.Rv610LocalRecipe2Adapter;
import com.robam.roki.ui.adapter3.RvRecipeThemeAdapter;
import com.robam.roki.ui.dialog.AbsSettingDialog;
import com.robam.roki.ui.page.RecipeDetailPage;
import com.robam.roki.ui.page.device.oven.CookBookTag;
import com.robam.roki.ui.page.device.steamovenone.Local610CookbookDetailPage;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TestDatas;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PotMenuFragment extends Fragment {

    RecyclerView rv_view;
    String mGuid;
    Pot pot;

    public static Fragment instance(String title) {
        PotMenuFragment fragment = new PotMenuFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PageArgumentKey.title, title);
        fragment.setArguments(bundle);
        return fragment;
    }
    public static Fragment instance(String title,Pot pot) {
        PotMenuFragment fragment = new PotMenuFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PageArgumentKey.title, title);
        bundle.putSerializable(PageArgumentKey.Bean, pot);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_item, null);
    }

    boolean allTimeUseTag;
    RecipeTopicAdapter mRecipeTopicAdapter;
    PotMoreMenuAdapter mPotMoreMenuAdapter;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_view = view.findViewById(R.id.rv_view);
        Bundle bundle = getArguments();
        mGuid = bundle == null ? null : bundle.getString(PageArgumentKey.Guid);
        if (bundle != null) {
            pot = (Pot) bundle.getSerializable(PageArgumentKey.Bean);
        }

        rv_view.setLayoutManager(new GridLayoutManager(getContext() , 2 , RecyclerView.VERTICAL ,false));
        List<String> list = new ArrayList<>();
        list.add("红薯"); list.add("红烧肉");list.add("红烧鱼");list.add("清蒸大闸蟹");list.add("糖醋排骨");
        mPotMoreMenuAdapter = new PotMoreMenuAdapter(list);
        rv_view.setAdapter(mPotMoreMenuAdapter);
//        getWeekRecipeTops();

        mPotMoreMenuAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("Item:"+v.getTag(),Toast.LENGTH_SHORT);
            }
        });
        mPotMoreMenuAdapter.setOnAddClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("Add:"+v.getTag(),Toast.LENGTH_SHORT);
                List<Pot.Interaction> data = new ArrayList<>();
                Pot.Interaction interaction = new Pot.Interaction();
                interaction.key = 1;
                interaction.length = 7;
                //控制方式[1B]
                //0:无
                //1:升温
                //2:降温
                //3:恒温
                //灶具档位[1B]
                //1~N
                //温度[2B]
                //小端整数摄氏度
                //时间[2B]
                //搅拌电机模式[1B]
                //0,1,2
                interaction.value = new int[7];
                //1~5,表示P档菜谱序号
                interaction.value[0]=1;
                //0:左 1:右
                interaction.value[1]=1;
                //步骤1参数{Key:1,Len:7,Value}
                byte [] temp = ByteUtils.intToBytes2(300);
                interaction.value[2]=temp[0];
                interaction.value[3]=temp[1];
                byte [] time = ByteUtils.intToBytes2(600);
                interaction.value[4]=time[0];
                interaction.value[5]=time[1];
                interaction.value[6]=1;

                data.add(interaction);

                pot.setPMenu(0, data, new VoidCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });

            }
        });
    }

    private void getWeekRecipeTops() {
        String weekTime = TimeUtils.getlastWeekTime();
        RokiRestHelper.getWeekTops(weekTime, 0, 5, new Callback<List<Recipe>>() {
            @Override
            public void onSuccess(final List<Recipe> recipes) {

                for (Recipe recipe : recipes) {
                    recipe.setItemType(Recipe.IMG);
                }
                recipes.add(new Recipe(Recipe.TEXT));
                mRecipeTopicAdapter = new RecipeTopicAdapter(recipes);
                rv_view.setAdapter(mRecipeTopicAdapter);
                mRecipeTopicAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                        if (recipes.size() == position + 1) {
                            UIService.getInstance().postPage(PageKey.TopWeekPage);
                        } else {
                            RecipeDetailPage.show(recipes.get(position).id, recipes.get(position).sourceType);
                        }
                    }
                });

            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
