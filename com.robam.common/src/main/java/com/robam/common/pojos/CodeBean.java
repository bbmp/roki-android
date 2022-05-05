package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CodeBean {

    @JsonProperty("code")
    public String code;

    @JsonProperty("codeValue")
    public String codeValue;

    @JsonProperty("codeDesc")
    public String codeDesc;

}
