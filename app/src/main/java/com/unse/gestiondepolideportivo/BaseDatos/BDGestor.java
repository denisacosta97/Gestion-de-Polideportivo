package com.unse.gestiondepolideportivo.BaseDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.unse.gestiondepolideportivo.Modelo.PiletaIngresoParcial;


public class BDGestor extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "GestorPoli.db";
    private static final int DATABASE_VERSION = 1;


    public BDGestor(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(PiletaRepo.createTable());
        sqLiteDatabase.execSQL(ReservaRepo.createTable());
        sqLiteDatabase.execSQL(PiletaParcialRepo.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);

    }
}
