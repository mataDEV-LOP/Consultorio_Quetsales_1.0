package com.example.consultorioquetsales10;

public class Expediente {
    private String nombrePaciente;
    private String fechaNacimiento;
    private int edad;
    private String telefono;
    private String correoElectronico;
    private String alergias;
    private double temperatura;
    private String presionArterial;

    // Constructor vacío requerido por Firebase
    public Expediente() {}

    // Constructor con parámetros
    public Expediente(String nombrePaciente, String fechaNacimiento, int edad, String telefono,
                      String correoElectronico, String alergias, double temperatura, String presionArterial) {
        this.nombrePaciente = nombrePaciente;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.alergias = alergias;
        this.temperatura = temperatura;
        this.presionArterial = presionArterial;
    }

    // Getters y setters
    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }

    public double getTemperatura() { return temperatura; }
    public void setTemperatura(double temperatura) { this.temperatura = temperatura; }

    public String getPresionArterial() { return presionArterial; }
    public void setPresionArterial(String presionArterial) { this.presionArterial = presionArterial; }
}
