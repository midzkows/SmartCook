package com.example.dev.smartcook;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import com.example.dev.smartcook.model.Feed;

public interface ArduinoAPI {

    String BASE_URL = "http://192.168.0.94/";

    @Headers("Content-Type: application/json")
    @GET(".json")
    Call<Feed> getData();
}
