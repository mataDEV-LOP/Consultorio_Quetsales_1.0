package com.example.consultorioquetsales10.model;

import com.example.consultorioquetsales10.Expediente;

import java.util.List;

public class ExpedienteResponse {
    private boolean success;
    private List<Expediente> expedientes;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public List<Expediente> getExpedientes() {
        return expedientes;
    }

    public String getMessage() {
        return message;
    }
}