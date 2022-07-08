package com.robam.common.events;

public class WebUrlEvent {
    public String url;

    public WebUrlEvent(String url) {
        this.url = url;
    }
    public String secondTitle;
    public String forwardImageUrl;
    public String title;
    public WebUrlEvent(String url, String secondTitle, String forwardImageUrl,String title) {
        this.url = url;
        this.secondTitle = secondTitle;
        this.forwardImageUrl = forwardImageUrl;
        this.title = title;
    }
}
