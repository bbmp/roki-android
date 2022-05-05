package com.robam.roki.model.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.robam.roki.ui.view.linkrecipetag.bean.BaseGroupedItem;
import com.robam.roki.ui.view.linkrecipetag.bean.DefaultGroupedItem;

public class RecipeTagGroupItem extends BaseGroupedItem<RecipeTagGroupItem.ItemInfo> {

    public RecipeTagGroupItem(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public RecipeTagGroupItem(RecipeTagGroupItem.ItemInfo item) {
        super(item);
    }


    public static class ItemInfo extends BaseGroupedItem.ItemInfo {
        private Long id;
        private String imgUrl;
        private String name;


        public ItemInfo(String title, String group, Long id) {
            super(title, group);
            this.id = id;
        }

        public ItemInfo(String title, String group, Long id, String imgUrl) {
            this(title, group, id);
            this.imgUrl = imgUrl;
        }

        public ItemInfo(String title, String group, Long id, String imgUrl, String name) {
            this(title, group, id, imgUrl);
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


    }


}
