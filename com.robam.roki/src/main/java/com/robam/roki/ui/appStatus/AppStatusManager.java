//package com.robam.roki.ui.appStatus;
//
///**
// * @author r210190
// * des  APP状态控制类
// */
//public class AppStatusManager {
//    /**
//     *  APP状态 初始值为不在前台状态
//     */
//    public int appStatus = AppStatus.STATUS_RECYVLE;
//
//    public static AppStatusManager appStatusManager;
//
//    private AppStatusManager(){}
//
//    //单例模式
//    public static AppStatusManager getInstance() {
//        if (appStatusManager == null) {
//            appStatusManager = new AppStatusManager();
//        }
//        return appStatusManager;
//    }
//
//    public int getAppStatus() {
//        return appStatus;
//    }
//
//    public void setAppStatus(int appStatus) {
//        this.appStatus = appStatus;
//    }
//
//}
