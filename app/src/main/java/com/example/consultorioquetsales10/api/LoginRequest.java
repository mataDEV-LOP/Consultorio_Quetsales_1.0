package com.example.consultorioquetsales10.api;

public class LoginRequest {
    private String doctorId;
    private String password;

    public LoginRequest(String doctorId, String password) {
        this.doctorId = doctorId;
        this.password = password;
    }
}