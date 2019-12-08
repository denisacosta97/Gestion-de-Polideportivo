package com.unse.gestiondepolideportivo.Modelo;

import android.provider.BaseColumns;

public class Reserva {

    public static final String TAG = Reserva.class.getSimpleName();
    public static final String TABLE = "Reserva";
    // Labels Table Columns names
    public static final String KEY_ID = BaseColumns._ID;
    public static final String KEY_DNI = "dni";
    public static final String KEY_INSTALACION = "INSTALACION";
    public static final String KEY_CANTMAY = "cantMayores";
    public static final String KEY_CANTMEN = "cantMenores";
    public static final String KEY_HRAINI = "hraIni";
    public static final String KEY_HRAFIN = "hraFin";
    public static final String KEY_FECHA = "fecha";
    public static final String KEY_PRECIO = "precio";

    private int dni;
    private int id;
    private int categoria;
    private int instalacion;
    private int cantMayores;
    private int cantMenores;
    private String hraInicio;
    private String hraFin;
    private String fecha;
    private float precio;

    public Reserva(int dni, int id, int categoria, int instalacion, int cantMayores,
                   int cantMenores, String hraInicio, String hraFin, String fecha, float precio) {
        this.dni = dni;
        this.id = id;
        this.categoria = categoria;
        this.instalacion = instalacion;
        this.cantMayores = cantMayores;
        this.cantMenores = cantMenores;
        this.hraInicio = hraInicio;
        this.hraFin = hraFin;
        this.fecha = fecha;
        this.precio = precio;
    }

    public Reserva() {
        this.dni = 0;
        this.id = 0;
        this.categoria = 0;
        this.instalacion = 0;
        this.cantMayores = 0;
        this.cantMenores = 0;
        this.hraInicio = "";
        this.hraFin = "";
        this.fecha = "";
        this.precio = 0;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public int getInstalacion() {
        return instalacion;
    }

    public void setInstalacion(int instalacion) {
        this.instalacion = instalacion;
    }

    public int getCantMayores() {
        return cantMayores;
    }

    public void setCantMayores(int cantMayores) {
        this.cantMayores = cantMayores;
    }

    public int getCantMenores() {
        return cantMenores;
    }

    public void setCantMenores(int cantMenores) {
        this.cantMenores = cantMenores;
    }

    public String getHraInicio() {
        return hraInicio;
    }

    public void setHraInicio(String hraInicio) {
        this.hraInicio = hraInicio;
    }

    public String getHraFin() {
        return hraFin;
    }

    public void setHraFin(String hraFin) {
        this.hraFin = hraFin;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }
}
