package com.unse.gestiondepolideportivo.Modelo;

import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class Reserva {

    public static final String TAG = Reserva.class.getSimpleName();
    public static final String TABLE = "Reserva";
    // Labels Table Columns names
    public static final String KEY_ID = BaseColumns._ID;
    public static final String KEY_DNI = "dni";
    public static final String KEY_INSTALACION = "INSTALACION";
    public static final String KEY_CATEGORIA = "categoria";
    public static final String KEY_HRAINI = "hraIni";
    public static final String KEY_HRAFIN = "hraFin";
    public static final String KEY_FECHARESERVA = "fecha_reserva";
    public static final String KEY_PRECIO = "precio";
    public static final String KEY_EMPLEADO = "empleado";
    public static final String KEY_FECHA = "fecha";

    private int dni;
    private int id;
    private int categoria;
    private int instalacion;
    private String hraInicio;
    private String hraFin;
    private String fechaReserva;
    private String precio;
    private String empleado;
    private String fecha;

    public Reserva(int dni, int id, int categoria, int instalacion, String hraInicio, String hraFin,
                   String fechaReserva, String precio, String empleado, String fecha) {
        this.dni = dni;
        this.id = id;
        this.categoria = categoria;
        this.instalacion = instalacion;
        this.hraInicio = hraInicio;
        this.hraFin = hraFin;
        this.fechaReserva = fechaReserva;
        this.precio = precio;
        this.empleado = empleado;
        this.fecha = fecha;
    }

    public Reserva() {
        this.dni = 0;
        this.id = 0;
        this.categoria = 0;
        this.instalacion = 0;
        this.hraInicio = "";
        this.hraFin = "";
        this.fechaReserva = "";
        this.precio = "";
        this.empleado = "";
        this.fecha = "";
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

    public String getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(String fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public HashMap<String, Object> toHashMap(){
        HashMap<String, Object> map = new HashMap<>();

        map.put("dni", getDni());
        map.put("fechaR", getFechaReserva());
        map.put("lugar", getInstalacion());
        map.put("horaI", getHraInicio());
        map.put("horaF", getHraFin());
        map.put("fecha", getFecha());

        return map;
    }

    @NonNull
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(this);
        return json;
    }
}
