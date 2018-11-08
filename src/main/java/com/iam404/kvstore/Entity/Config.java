package com.iam404.restkeyvaluevault.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Config {

    @SerializedName("key")
    @Expose
    private String key;

    public String getKey() {
        return key;
    }


}