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
   // public static final String KEY_CANTMAY = "cantMayores";
    //public static final String KEY_CANTMEN = "cantMenores";
    public static final String KEY_FECHA = "fecha";
    public static final String KEY_PRECIO1 = "precio1";
    public static final String KEY_EMPLEADO = "dniEmpleado";

    private int dni;
    private int id;
    private int categoria;
   // private int cantMayores;
    //private int cantMenores;
    private String fecha;
    private float precio1;
    private int dniEmpleado;
    private int cantidadTotal;

    public int getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(int cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public PiletaIngreso() {
        this.dni = 0;
        this.id = 0;
        this.categoria = 0;

        this.fecha = null;
        this.precio1 = 0;
    }

    public PiletaIngreso(int dni, int id, int categoria, String fecha, float precio1, int dniEmpleado) {
        this.dni = dni;
        this.id = id;
        this.categoria = categoria;
        this.fecha = fecha;
        this.precio1 = precio1;
        this.dniEmpleado = dniEmpleado;
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

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(this);
        return json;
    }
}
