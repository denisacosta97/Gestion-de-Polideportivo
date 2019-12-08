package com.unse.gestiondepolideportivo.Modelo;

import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PiletaIngreso {

    public static final String TAG = PiletaIngreso.class.getSimpleName();
    public static final String TABLE = "pileta";
    // Labels Table Columns names
    public static final String KEY_ID = BaseColumns._ID;
    public static final String KEY_DNI = "dni";
    public static final String KEY_CATEGORIA = "categoria";
    public static final String KEY_CANTMAY = "cantMayores";
    public static final String KEY_CANTMEN = "cantMenores";
    public static final String KEY_FECHA = "fecha";
    public static final String KEY_PRECIO1 = "precio1";
    public static final String KEY_PRECIO2 = "precio2";
    public static final String KEY_EMPLEADO = "dniEmpleado";

    private int dni;
    private int id;
    private int categoria;
    private int cantMayores;
    private int cantMenores;
    private String fecha;
    private float precio1;
    private float precio2;
    private int dniEmpleado;

    public PiletaIngreso() {
        this.dni = 0;
        this.id = 0;
        this.categoria = 0;
        this.cantMayores = 0;
        this.cantMenores = 0;
        this.fecha = null;
        this.precio1 = 0f;
        this.precio2 = 0f;
    }

    public PiletaIngreso(int dni, int id, int categoria, int cantMayores, int cantMenores, String fecha,
                         float precio1, float precio2, int dniEm) {
        this.dni = dni;
        this.id = id;
        this.categoria = categoria;
        this.cantMayores = cantMayores;
        this.cantMenores = cantMenores;
        this.fecha = fecha;
        this.precio1 = precio1;
        this.precio2 = precio2;
        this.dniEmpleado = dniEm;
    }

    public int getDniEmpleado() {
        return dniEmpleado;
    }

    public void setDniEmpleado(int dniEmpleado) {
        this.dniEmpleado = dniEmpleado;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public float getPrecio1() {
        return precio1;
    }

    public void setPrecio1(float precio1) {
        this.precio1 = precio1;
    }

    public float getPrecio2() {
        return precio2;
    }

    public void setPrecio2(float precio2) {
        this.precio2 = precio2;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(this);
        return json;
    }
}
