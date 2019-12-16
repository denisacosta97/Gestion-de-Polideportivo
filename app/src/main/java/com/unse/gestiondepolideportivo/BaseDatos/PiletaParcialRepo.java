package com.unse.gestiondepolideportivo.BaseDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unse.gestiondepolideportivo.Modelo.PiletaIngresoParcial;
import com.unse.gestiondepolideportivo.Herramientas.Utils;

import java.util.ArrayList;

public class PiletaParcialRepo {

    private PiletaIngresoParcial mPiletaIngresoParcial;

    public PiletaParcialRepo(Context context) {
        Utils.initBD(context);
        mPiletaIngresoParcial = new PiletaIngresoParcial();

    }

    public PiletaIngresoParcial getPiletaIngresoParcial() {
        return mPiletaIngresoParcial;
    }

    public void setPiletaIngresoParcial(PiletaIngresoParcial pileta) {
        this.mPiletaIngresoParcial = pileta;
    }

    static String createTable() {
        return String.format("create table %s(%s %s %s,%s %s %s,%s %s %s,%s %s %s,%s %s %s)",
                PiletaIngresoParcial.TABLE,
                PiletaIngresoParcial.KEY_ID, Utils.INT_TYPE, Utils.AUTO_INCREMENT,
                PiletaIngresoParcial.KEY_CATEGORIA, Utils.INT_TYPE, Utils.NULL_TYPE,
                PiletaIngresoParcial.KEY_CANTIDAD, Utils.INT_TYPE, Utils.NULL_TYPE,
                PiletaIngresoParcial.KEY_PRECIO, Utils.FLOAT_TYPE, Utils.NULL_TYPE,
                PiletaIngresoParcial.KEY_ID_INGRESO, Utils.INT_TYPE, Utils.NULL_TYPE);
    }

    private ContentValues loadValues(PiletaIngresoParcial pileta, int tipo) {
        ContentValues values = new ContentValues();
        if (tipo != 1)
            values.put(PiletaIngresoParcial.KEY_ID, pileta.getId());
        values.put(PiletaIngresoParcial.KEY_CATEGORIA, pileta.getCategoria());
        values.put(PiletaIngresoParcial.KEY_CANTIDAD, pileta.getCantidad());
        values.put(PiletaIngresoParcial.KEY_PRECIO, pileta.getPrecio());
        values.put(PiletaIngresoParcial.KEY_ID_INGRESO, pileta.getIdIngreso());

        return values;
    }

    private PiletaIngresoParcial loadFromCursor(Cursor cursor) {
        mPiletaIngresoParcial = new PiletaIngresoParcial();
        mPiletaIngresoParcial.setId(cursor.getInt(0));
        mPiletaIngresoParcial.setCategoria(cursor.getInt(1));
        mPiletaIngresoParcial.setCantidad(cursor.getInt(2));
        mPiletaIngresoParcial.setPrecio(cursor.getFloat(3));
        mPiletaIngresoParcial.setIdIngreso(cursor.getInt(4));


        return mPiletaIngresoParcial;
    }

    public void insert(PiletaIngresoParcial pileta) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        db.insert(PiletaIngresoParcial.TABLE, null, loadValues(pileta, 1));
        DBManager.getInstance().closeDatabase();
    }

    public void delete(PiletaIngresoParcial pileta) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        db.delete(PiletaIngresoParcial.TABLE, PiletaIngresoParcial.KEY_ID + " = ?", new String[]{String.valueOf(pileta.getId())});
        DBManager.getInstance().closeDatabase();
    }

    public PiletaIngresoParcial get(int id) {
        mPiletaIngresoParcial = new PiletaIngresoParcial();
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery("select * from " + PiletaIngresoParcial.TABLE + " where " + PiletaIngresoParcial.KEY_ID + " = " + id, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            mPiletaIngresoParcial = loadFromCursor(cursor);
            cursor.close();
        }
        DBManager.getInstance().closeDatabase();
        return mPiletaIngresoParcial;
    }

    public boolean isExist(int numero) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery("select * from " + PiletaIngresoParcial.TABLE + " where " + PiletaIngresoParcial.KEY_ID + " = " + numero, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        DBManager.getInstance().closeDatabase();
        return false;

    }

    public void update(PiletaIngresoParcial pileta) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        String id = String.valueOf(pileta.getId());
        String selection = PiletaIngresoParcial.KEY_ID + " = " + id;
        db.update(PiletaIngresoParcial.TABLE, loadValues(pileta, 2), selection, null);
        DBManager.getInstance().closeDatabase();
    }

    public int getCount() {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        String countQuery = "SELECT  * FROM " + PiletaIngresoParcial.TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        DBManager.getInstance().closeDatabase();
        cursor.close();
        return count;
    }

    public ArrayList<PiletaIngresoParcial> getAllByIngreso(int id) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        ArrayList<PiletaIngresoParcial> list = new ArrayList<PiletaIngresoParcial>();
        String query = String.format("select * from %s where %s = %s", PiletaIngresoParcial.TABLE, PiletaIngresoParcial.KEY_ID_INGRESO, id);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                mPiletaIngresoParcial = loadFromCursor(cursor);
                list.add(mPiletaIngresoParcial);
            } while (cursor.moveToNext());
        }
        DBManager.getInstance().closeDatabase();
        cursor.close();

        return list;

    }

    public ArrayList<PiletaIngresoParcial> getAll() {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        ArrayList<PiletaIngresoParcial> list = new ArrayList<PiletaIngresoParcial>();
        String query = String.format("select * from %s", PiletaIngresoParcial.TABLE);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                mPiletaIngresoParcial = loadFromCursor(cursor);
                list.add(mPiletaIngresoParcial);
            } while (cursor.moveToNext());
        }
        DBManager.getInstance().closeDatabase();
        cursor.close();

        return list;

    }


}
