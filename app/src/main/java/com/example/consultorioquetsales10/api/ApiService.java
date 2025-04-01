package com.example.consultorioquetsales10.api;

import com.example.consultorioquetsales10.model.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}