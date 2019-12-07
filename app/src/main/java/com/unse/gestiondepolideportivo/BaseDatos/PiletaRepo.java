package com.unse.gestiondepolideportivo.BaseDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unse.gestiondepolideportivo.Modelo.PiletaIngreso;
import com.unse.gestiondepolideportivo.Utils;

import java.util.ArrayList;

public class PiletaRepo {

    private PiletaIngreso mPiletaIngreso;

    public PiletaRepo(Context context) {
        Utils.initBD(context);
        mPiletaIngreso = new PiletaIngreso();

    }

    public PiletaIngreso getPiletaIngreso() {
        return mPiletaIngreso;
    }

    public void setPiletaIngreso(PiletaIngreso carrito) {
        this.mPiletaIngreso = carrito;
    }

    static String createTable() {
        return String.format("create table %s(%s %s %s,%s %s %s,%s %s %s,%s %s %s,%s %s %s,%s %s %s,%s %s %s,%s %s %s)",
                PiletaIngreso.TABLE,
                PiletaIngreso.KEY_ID, Utils.INT_TYPE, Utils.AUTO_INCREMENT,
                PiletaIngreso.KEY_DNI, Utils.INT_TYPE, Utils.NULL_TYPE,
                PiletaIngreso.KEY_CATEGORIA, Utils.INT_TYPE, Utils.NULL_TYPE,
                PiletaIngreso.KEY_CANTMAY, Utils.INT_TYPE, Utils.NULL_TYPE,
                PiletaIngreso.KEY_CANTMEN, Utils.INT_TYPE, Utils.NULL_TYPE,
                PiletaIngreso.KEY_FECHA, Utils.STRING_TYPE, Utils.NULL_TYPE,
                PiletaIngreso.KEY_PRECIO1, Utils.FLOAT_TYPE, Utils.NULL_TYPE,
                PiletaIngreso.KEY_PRECIO2, Utils.FLOAT_TYPE, Utils.NULL_TYPE);
    }

    private ContentValues loadValues(PiletaIngreso pileta, int tipo) {
        ContentValues values = new ContentValues();
        if (tipo != 1)
            values.put(PiletaIngreso.KEY_ID, pileta.getId());
        values.put(PiletaIngreso.KEY_DNI, pileta.getDni());
        values.put(PiletaIngreso.KEY_CATEGORIA, pileta.getCategoria());
        values.put(PiletaIngreso.KEY_CANTMAY, pileta.getCantMayores());
        values.put(PiletaIngreso.KEY_CANTMEN, pileta.getCantMenores());
        values.put(PiletaIngreso.KEY_FECHA, pileta.getFecha());
        values.put(PiletaIngreso.KEY_PRECIO1, pileta.getPrecio1());
        values.put(PiletaIngreso.KEY_PRECIO2, pileta.getPrecio2());

        return values;
    }

    private PiletaIngreso loadFromCursor(Cursor cursor) {
        mPiletaIngreso = new PiletaIngreso();
        mPiletaIngreso.setId(cursor.getInt(0));
        mPiletaIngreso.setDni(cursor.getInt(1));
        mPiletaIngreso.setCategoria(cursor.getInt(2));
        mPiletaIngreso.setCantMayores(cursor.getInt(3));
        mPiletaIngreso.setCantMenores(cursor.getInt(4));
        mPiletaIngreso.setFecha(cursor.getString(5));
        mPiletaIngreso.setPrecio1(cursor.getInt(6));
        mPiletaIngreso.setPrecio2(cursor.getInt(7));

        return mPiletaIngreso;
    }

    public void insert(PiletaIngreso pileta) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        db.insert(PiletaIngreso.TABLE, null, loadValues(pileta, 1));
        DBManager.getInstance().closeDatabase();
    }

    public void delete(PiletaIngreso pileta) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        db.delete(PiletaIngreso.TABLE, PiletaIngreso.KEY_ID + " = ?", new String[]{String.valueOf(pileta.getId())});
        DBManager.getInstance().closeDatabase();
    }

    public PiletaIngreso get(int id) {
        mPiletaIngreso = new PiletaIngreso();
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery("select * from " + PiletaIngreso.TABLE + " where " + PiletaIngreso.KEY_ID + " = " + id, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            mPiletaIngreso = loadFromCursor(cursor);
            cursor.close();
        }
        DBManager.getInstance().closeDatabase();
        return mPiletaIngreso;
    }

    public boolean isExist(int numero) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery("select * from " + PiletaIngreso.TABLE + " where " + PiletaIngreso.KEY_ID + " = " + numero, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        DBManager.getInstance().closeDatabase();
        return false;

    }

    public void update(PiletaIngreso pileta) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        String id = String.valueOf(pileta.getId());
        String selection = PiletaIngreso.KEY_ID + " = " + id;
        db.update(PiletaIngreso.TABLE, loadValues(pileta, 2), selection, null);
        DBManager.getInstance().closeDatabase();
    }

    public int getCount() {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        String countQuery = "SELECT  * FROM " + PiletaIngreso.TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        DBManager.getInstance().closeDatabase();
        cursor.close();
        return count;
    }

    public ArrayList<PiletaIngreso> getAll() {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        ArrayList<PiletaIngreso> list = new ArrayList<PiletaIngreso>();
        String query = String.format("select * from %s", PiletaIngreso.TABLE);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                mPiletaIngreso = loadFromCursor(cursor);
                list.add(mPiletaIngreso);
            } while (cursor.moveToNext());
        }
        DBManager.getInstance().closeDatabase();
        cursor.close();

        return list;

    }

}