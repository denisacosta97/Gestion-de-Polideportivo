package com.unse.gestiondepolideportivo.BaseDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unse.gestiondepolideportivo.Modelo.PiletaIngreso;
import com.unse.gestiondepolideportivo.Herramientas.Utils;

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
        return String.format("create table %s(%s %s %s,%s %s %s,%s %s %s,%s %s %s,%s %s %s, %s %s %s)",
                PiletaIngreso.TABLE,
                PiletaIngreso.KEY_ID, Utils.INT_TYPE, Utils.AUTO_INCREMENT,
                PiletaIngreso.KEY_DNI, Utils.INT_TYPE, Utils.NULL_TYPE,
                PiletaIngreso.KEY_CATEGORIA, Utils.INT_TYPE, Utils.NULL_TYPE,
                PiletaIngreso.KEY_FECHA, Utils.STRING_TYPE, Utils.NULL_TYPE,
                PiletaIngreso.KEY_PRECIO1, Utils.FLOAT_TYPE, Utils.NULL_TYPE,
                PiletaIngreso.KEY_EMPLEADO, Utils.INT_TYPE, Utils.NULL_TYPE);
    }

    private ContentValues loadValues(PiletaIngreso pileta, int tipo) {
        ContentValues values = new ContentValues();
        if (tipo != 1)
            values.put(PiletaIngreso.KEY_ID, pileta.getId());
        values.put(PiletaIngreso.KEY_DNI, pileta.getDni());
        values.put(PiletaIngreso.KEY_CATEGORIA, pileta.getCategoria());
        values.put(PiletaIngreso.KEY_FECHA, pileta.getFecha());
        values.put(PiletaIngreso.KEY_PRECIO1, pileta.getPrecio1());
        values.put(PiletaIngreso.KEY_EMPLEADO, pileta.getDniEmpleado());

        return values;
    }

    private PiletaIngreso loadFromCursor(Cursor cursor) {
        mPiletaIngreso = new PiletaIngreso();
        mPiletaIngreso.setId(cursor.getInt(0));
        mPiletaIngreso.setDni(cursor.getInt(1));
        mPiletaIngreso.setCategoria(cursor.getInt(2));
        mPiletaIngreso.setFecha(cursor.getString(3));
        mPiletaIngreso.setPrecio1(cursor.getFloat(4));
        mPiletaIngreso.setDniEmpleado(cursor.getInt(5));

        return mPiletaIngreso;
    }

    public int insert(PiletaIngreso pileta) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        long id = db.insert(PiletaIngreso.TABLE, null, loadValues(pileta, 1));
        DBManager.getInstance().closeDatabase();
        return (int) id;
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

    public ArrayList<String> getAllFechas() {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        ArrayList<String> list = new ArrayList<String>();
        String query = String.format("select * from %s", PiletaIngreso.TABLE);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                mPiletaIngreso = loadFromCursor(cursor);
                String fecha = Utils.getFechaOnlyDay(Utils.getFechaDate(mPiletaIngreso.getFecha()));
                if (!list.contains(fecha))
                    list.add(fecha);
            } while (cursor.moveToNext());
        }
        DBManager.getInstance().closeDatabase();
        cursor.close();

        return list;
    }

    public ArrayList<PiletaIngreso> getAllByFecha(String fecha) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        ArrayList<PiletaIngreso> list = new ArrayList<PiletaIngreso>();
        String query = String.format("select * from %s", PiletaIngreso.TABLE);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                mPiletaIngreso = loadFromCursor(cursor);
                String f = Utils.getFechaOnlyDay(Utils.getFechaDate(mPiletaIngreso.getFecha()));
                if (fecha.equals(f))
                    list.add(mPiletaIngreso);
            } while (cursor.moveToNext());
        }
        DBManager.getInstance().closeDatabase();
        cursor.close();

        return list;
    }
}