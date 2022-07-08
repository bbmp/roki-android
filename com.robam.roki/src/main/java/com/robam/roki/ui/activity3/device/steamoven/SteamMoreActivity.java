package com.robam.roki.ui.activity3.device.steamoven;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.activity3.device.base.DeviceMoreActivity;
import com.robam.roki.ui.page.device.steamovenone.steamovenone920.RecipeListActivity;

import java.util.ArrayList;
import java.util.List;

public class SteamMoreActivity extends DeviceMoreActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_steam_more);
//    }
ArrayList<String> data = new ArrayList<>();
    @Override
    protected List<String> initMoreData() {


        data.add("设备名称");
        data.add("烹饪记录");
        data.add("留言咨询");
        data.add("一键售后");
        data.add("产品信息");
        return data;

    }

    @Override
    protected void otherOnClick(String more) {
        if (more.equals(data.get(1))){
            Intent intent = new Intent(getContext(), RecipeListActivity.class);
            intent.putExtra(PageArgumentKey.Guid, mGuid);
            startActivity(intent);
        }

    }
}