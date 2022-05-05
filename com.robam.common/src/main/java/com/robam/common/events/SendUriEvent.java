package com.robam.common.events;

import android.net.Uri;

/**
 * Created by yinwei on 2017/10/10.
 */

public class SendUriEvent {
    public Uri uriSnap,uriCrop;
    public SendUriEvent(Uri uriSnap,Uri uriCrop){
        this.uriSnap = uriSnap;
        this.uriCrop = uriCrop;
    }
}
