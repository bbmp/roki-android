package com.robam.roki.ui.bean3;

import java.util.List;

public class BannerBean {

    public int code;
    public String message;
    public boolean success;
    public List<DataDTO> data;
    public int pageIndex;
    public int pageSize;
    public int total;
    public int totalPage;

    public static class DataDTO {
        public int dataStatus;
        public String createUser;
        public String updateUser;
        public String createDatetime;
        public String updateDatetime;
        public int id;
        public String title;
        public String imageUrl;
        public String imageName;
        public int sortNo;
        public int linkType;
        public int linkAction;
        public String resource;
        public int groundingStatus;
        public String groundingDateTime;
        public Object cronDate;
        public String resourceName;
        public String secondTitle;//分享文案
        public String forwardImageUrl;//分享图片链接
    }
}
