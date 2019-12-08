package com.unse.gestiondepolideportivo.Modelo;

public class ItemDato extends ItemListado {

    public ItemDato(PiletaIngreso piletaIngreso){
        this.mPiletaIngreso = piletaIngreso;
    }

    private PiletaIngreso mPiletaIngreso;

    public PiletaIngreso getPiletaIngreso() {
        return mPiletaIngreso;
    }

    public void setPiletaIngreso(PiletaIngreso piletaIngreso) {
        mPiletaIngreso = piletaIngreso;
    }

    @Override
    public int getTipo() {
        return TIPO_DATO;
    }
}
