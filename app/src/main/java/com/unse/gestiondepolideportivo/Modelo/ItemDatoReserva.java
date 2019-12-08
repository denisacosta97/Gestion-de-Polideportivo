package com.unse.gestiondepolideportivo.Modelo;


public class ItemDatoReserva extends ItemReserva {

    public ItemDatoReserva(Reserva reserva){
        this.mReserva = reserva;
    }

    private Reserva mReserva;

    public Reserva getReserva() {
        return mReserva;
    }

    public void setReserva(Reserva reserva) {
        mReserva = reserva;
    }

    @Override
    public int getTipo() {
        return TIPO_DATO;
    }
}
