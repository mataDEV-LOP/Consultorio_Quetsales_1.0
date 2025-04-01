package com.example.consultorioquetsales10.model;

public class LoginResponse {
    private boolean success;
    private Doctor doctor;
    private String token; // Nuevo campo para el token JWT
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public static class Doctor {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}