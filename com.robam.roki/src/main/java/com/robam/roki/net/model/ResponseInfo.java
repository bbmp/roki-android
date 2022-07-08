package com.robam.roki.net.model;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import com.robam.roki.net.model.ResponseHead;


public class ResponseInfo<T>
{
    @SerializedName(alternate={"data"}, value="body")
    public T body;
    public String msg;
    public ResponseHead respHead;
    public boolean success;

    public String getCode()
    {
        ResponseHead localResponseHead = this.respHead;
        if (localResponseHead == null) {
            return String.valueOf(65132);
        }
        return localResponseHead.respCode;
    }

    public String getErrorMsg()
    {
        ResponseHead localResponseHead = this.respHead;
        if (localResponseHead != null) {
            return localResponseHead.respMsg;
        }
        if (!TextUtils.isEmpty(this.msg)) {
            return this.msg;
        }
        return "json解析异常";
    }

    public boolean isSuccess()
    {
        ResponseHead localResponseHead = this.respHead;
        if (((localResponseHead != null) && (("000".equals(localResponseHead.respCode)) || ("000000".equals(this.respHead.respCode)) || ("200".equals(this.respHead.respCode)) || ("201".equals(this.respHead.respCode)))) || (this.success))
        {
//            localResponseHead = this.respHead;
//            if ((localResponseHead != null) && (!TextUtils.isEmpty(localResponseHead.token))) {
//                UserConfig.getInstance().setToken(this.respHead.token);
//            }
            return true;
        }
        return false;
    }
}
