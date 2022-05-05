package com.robam.common.events;

/**
 * Created by Administrator on 2019/7/10.
 */

public class CallResultApiEvent {

    private String commandApi;
    private String data;

    public CallResultApiEvent(String commandApi, String data) {
        this.commandApi = commandApi;
        this.data = data;
    }

    public String getCommandApi() {
        return commandApi;
    }

    public String getData() {
        return data;
    }
}
