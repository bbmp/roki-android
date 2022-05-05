package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.legent.dao.DaoHelper;
import com.legent.pojos.AbsStorePojo;

import java.io.Serializable;
import java.util.List;

/**
 * 联网设备组列表
 *
 * @author sylar
 */
public class DeviceGroupList extends AbsStorePojo<Long> implements Serializable {

    @DatabaseField(generatedId = true)
    private long id;

    private int sortId;

    /**
     * 厂家
     */
    @DatabaseField
    @JsonProperty("vendor")
    public String vendor;

    /**
     * icon
     */
    @DatabaseField
    @JsonProperty("iconUrl")
    public String iconUrl;

    /**
     * name
     */
    @DatabaseField
    @JsonProperty("name")
    public String name;

    /**
     * englishName
     */
    @DatabaseField
    @JsonProperty("englishName")
    public String englishName;

    /**
     * 设备所属品类（大类）
     */
    @DatabaseField
    @JsonProperty("dc")
    public String dc;
    /**
     * 产品型号对象集合
     */
    @ForeignCollectionField()
    private ForeignCollection<DeviceItemList> db_deviceItemLists;

    @JsonProperty("pds")
    public List<DeviceItemList> js_deviceItemLists;


    public List<DeviceItemList> getDeviceItemLists() {
       /* if (db_deviceItemLists == null || db_deviceItemLists.size() == 0)
            return Lists.newArrayList();

        List<DeviceItemList> list = Lists.newArrayList(db_deviceItemLists);
        DeviceItemList deviceItemList = new DeviceItemList();
        deviceItemList.setName("找不到型号");
        list.add(deviceItemList);
        return list;*/

          if (js_deviceItemLists == null || js_deviceItemLists.size() == 0)
            return Lists.newArrayList();

        List<DeviceItemList> list = Lists.newArrayList(js_deviceItemLists);
//        DeviceItemList deviceItemList = new DeviceItemList();
//        deviceItemList.setName("找不到型号");
//        list.add(deviceItemList);
        return list;

    }

    private int image_device;
    private String deviceName;
    private String deviceEnglishName;

    public String getDeviceEnglishName() {
        return deviceEnglishName;
    }

    public void setDeviceEnglishName(String deviceEnglishName) {
        this.deviceEnglishName = deviceEnglishName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getImage_device() {
        return image_device;
    }

    public void setImage_device(int image_device) {
        this.image_device = image_device;
    }

    @Override
    public String getName() {
        return dc;
    }

    public String getVendor() {
        return vendor;
    }

    public String getDc() {
        return dc;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }
    @Override
    public void save2db() {
        super.save2db();
        DaoHelper.deleteWhereEq(DeviceItemList.class, DeviceItemList.DeviceItemList_ID, id);
        if (js_deviceItemLists != null) {
            for (DeviceItemList deviceItemList : js_deviceItemLists) {
                deviceItemList.deviceGroupList = this;
                deviceItemList.save2db();
            }
        }
        DaoHelper.update(this);
        DaoHelper.refresh(this);
    }

    @Override
    public Long getID() {
        return id;
    }

}