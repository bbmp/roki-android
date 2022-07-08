package com.robam.roki.ui.bean3;

import com.robam.roki.ui.view.linkrecipetag.bean.BaseGroupedItem;

public class DeviceTagGroupItem extends BaseGroupedItem<DeviceTagGroupItem.ItemInfo> {
    public DeviceTagGroupItem(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public DeviceTagGroupItem(ItemInfo info) {
        super(info);
    }
    public static class ItemInfo extends BaseGroupedItem.ItemInfo {
        private String imgUrl;
        private String name;
        private String dp;
        private String displayType;
        private String netTips;
        private String iconUrl;

        public ItemInfo(String title, String group) {
            super(title, group);
        }

        public ItemInfo(String title, String group, String imgUrl, String name) {
            super(title, group);
            this.imgUrl = imgUrl;
            this.name = name;
        }

        public String getDp() {
            return dp;
        }

        public void setDp(String dp) {
            this.dp = dp;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public String getName() {
            return name;
        }

        public String getDisplayType() {
            return displayType;
        }

        public void setDisplayType(String displayType) {
            this.displayType = displayType;
        }

        public String getNetTips() {
            return netTips;
        }

        public void setNetTips(String netTips) {
            this.netTips = netTips;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }
    }
}
