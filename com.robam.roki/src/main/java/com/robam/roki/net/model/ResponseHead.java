package com.robam.roki.net.model;



import com.google.gson.annotations.SerializedName;

public class ResponseHead
{
    public String respCode;
    @SerializedName(alternate={"respmessage"}, value="respMsg")
    public String respMsg;
    public String token;
}
