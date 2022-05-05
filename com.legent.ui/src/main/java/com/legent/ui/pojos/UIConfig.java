package com.legent.ui.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.legent.pojos.IJsonPojo;
import com.legent.utils.JsonUtils;

import java.util.List;

/**
 * Created by sylar on 15/6/3.
 */
public class UIConfig implements IJsonPojo {

    public static UIConfig loadConfig(String content) {

        Preconditions.checkState(!Strings.isNullOrEmpty(content),"invalid ui config");
        UIConfig res = null;

        try {
            res = JsonUtils.json2Pojo(content, UIConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Preconditions.checkState(!Strings.isNullOrEmpty(res.mainFormKey),
                "mainFormKey is null");

        return res;
    }


    @JsonProperty("main")
    private String mainFormKey;

    @JsonProperty("forms")
    private List<FormInfo> forms;

    @JsonProperty("menus")
    private List<MenuInfo> menus;

    @JsonProperty("pages")
    private List<PageInfo> pages;

    public String getMainFormKey() {
        return mainFormKey;
    }

    public PageInfo getPageInfo(String pageKey) {
        for (PageInfo pi : pages) {
            if (pi.getID().equals(pageKey))
                return pi;
        }
        return null;
    }

    public com.legent.ui.IMenuListener getMenuListener(String menuKey)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        MenuInfo mi = getMenuInfo(menuKey);
        return mi.getMenuListener();
    }

    public MenuInfo getMenuInfo(String menuKey) {
        for (MenuInfo mi : menus) {
            if (mi.getID().equals(menuKey))
                return mi;
        }
        return null;
    }

    public List<MenuInfo> getAllMenus() {
        return menus;
    }

    public List<MenuInfo> getValidMenus() {
        List<MenuInfo> list = Lists.newArrayList();
        for (MenuInfo menu : menus) {
            if (menu.valid) {
                list.add(menu);
            }

    }
        return list;
    }

    public FormInfo getFormInfo(String formKey) {
        for (FormInfo fi : forms) {
            if (fi.id.equals(formKey))
                return fi;
        }
        return null;
    }


}
