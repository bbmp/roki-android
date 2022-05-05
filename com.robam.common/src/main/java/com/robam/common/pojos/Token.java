package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by zhoudingjun on 2017/5/27.
 */

public class Token extends JSONObject implements Serializable {

    @JsonProperty("accessToken")
    public String accessToken;
    @JsonProperty("cookieKey")
    public String cookieKey;
    @JsonProperty("cookieValue")
    public String cookieValue;

    public Token(){}

    public Token(String accessToken, String cookieKey, String cookieValue) {
        this.accessToken = accessToken;
        this.cookieKey = cookieKey;
        this.cookieValue = cookieValue;
    }

    @Override
    public String toString() {
        return "Token{" +
                "accessToken='" + accessToken + '\'' +
                ", cookieKey='" + cookieKey + '\'' +
                ", cookieValue='" + cookieValue + '\'' +
                '}';
    }
}
