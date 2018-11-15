package com.example.dev.smartcook.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("T1")
    @Expose
    private String T1;
    @SerializedName("T2")
    @Expose
    private String T2;
    @SerializedName("T3")
    @Expose
    private String T3;
    @SerializedName("T4")
    @Expose
    private String T4;

    public String getT1() {
        return T1;
    }

    public String getT2() {
        return T2;
    }

    public String getT3() {
        return T3;
    }

    public String getT4() {
        return T4;
    }

}
