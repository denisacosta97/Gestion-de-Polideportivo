package com.unse.gestiondepolideportivo.Modelo;

public class ReservasPorFechas {

    private String dni;
    private int instalacion;
    private String fechaReserva, fecha;

    public ReservasPorFechas(String dni, int instalacion, String fechaReserva, String fecha) {
        this.dni = dni;
        this.instalacion = instalacion;
        this.fechaReserva = fechaReserva;
        this.fecha = fecha;
    }

    public ReservasPorFechas() {

    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public int getInstalacion() {
        return instalacion;
    }

    public void setInstalacion(int instalacion) {
        this.instalacion = instalacion;
    }

    public String getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(String fechaReserva) {
        this.fechaReserva = fechaReserva;
    }
}
