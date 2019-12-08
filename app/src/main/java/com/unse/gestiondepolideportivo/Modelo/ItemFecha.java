package com.unse.gestiondepolideportivo.Modelo;

public class ItemFecha extends ItemListado {

    private String fecha;

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public int getTipo() {
        return TIPO_FECHA;
    }
}
