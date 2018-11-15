package com.example.dev.smartcook.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feed {
    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "data=" + data +
                '}';
    }
}
