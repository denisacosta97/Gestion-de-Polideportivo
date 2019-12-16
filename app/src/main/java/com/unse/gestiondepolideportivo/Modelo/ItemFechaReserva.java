package com.unse.gestiondepolideportivo.Modelo;

import com.unse.gestiondepolideportivo.Modelo.ItemReserva;

public class ItemFechaReserva extends ItemReserva {
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
