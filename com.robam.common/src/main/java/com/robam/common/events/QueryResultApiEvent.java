package com.robam.common.events;



public class QueryResultApiEvent {

    private String nativeApi;
    private String data;

    public QueryResultApiEvent(String nativeApi, String data) {
        this.nativeApi = nativeApi;
        this.data = data;
    }

    public String getNativeApi() {
        return nativeApi;
    }

    public String getData() {
        return data;
    }
}
