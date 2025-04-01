package com.example.consultorioquetsales10;

public class Servicio {
    private int imagenResId;
    private String titulo;
    private String descripcion;

    public Servicio(int imagenResId, String titulo, String descripcion) {
        this.imagenResId = imagenResId;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public int getImagenResId() {
        return imagenResId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }
}