package com.example.consultorioquetsales10.api;

import com.example.consultorioquetsales10.Expediente;
import com.example.consultorioquetsales10.model.ExpedienteResponse;
import com.example.consultorioquetsales10.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/expedientes")
    Call<ExpedienteResponse> guardarExpediente(@Header("Authorization") String token, @Body Expediente expediente);

    @GET("api/expedientes")
    Call<ExpedienteResponse> obtenerExpedientes(@Header("Authorization") String token);
}