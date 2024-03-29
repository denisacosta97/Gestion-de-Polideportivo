package com.unse.gestiondepolideportivo.BaseDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unse.gestiondepolideportivo.Modelo.Reserva;
import com.unse.gestiondepolideportivo.Herramientas.Utils;

import java.util.ArrayList;

public class ReservaRepo {

    private Reserva mReserva;

    public ReservaRepo(Context context) {
        Utils.initBD(context);
        mReserva = new Reserva();

    }

    public Reserva getReserva() {
        return mReserva;
    }

    public void setReserva(Reserva carrito) {
        this.mReserva = carrito;
    }

    static String createTable() {
        return String.format("create table %s(%s %s %s,%s %s %s,%s %s %s,%s %s %s,%s %s %s,%s %s %s,%s %s %s,%s %s %s,%s %s %s,%s %s %s)",
                Reserva.TABLE,
                Reserva.KEY_ID, Utils.INT_TYPE, Utils.AUTO_INCREMENT,
                Reserva.KEY_DNI, Utils.INT_TYPE, Utils.NULL_TYPE,
                Reserva.KEY_CATEGORIA, Utils.INT_TYPE, Utils.NULL_TYPE,
                Reserva.KEY_INSTALACION, Utils.INT_TYPE, Utils.NULL_TYPE,
                Reserva.KEY_HRAINI, Utils.STRING_TYPE, Utils.NULL_TYPE,
                Reserva.KEY_HRAFIN, Utils.STRING_TYPE, Utils.NULL_TYPE,
                Reserva.KEY_FECHARESERVA, Utils.STRING_TYPE, Utils.NULL_TYPE,
                Reserva.KEY_PRECIO, Utils.STRING_TYPE, Utils.NULL_TYPE,
                Reserva.KEY_EMPLEADO, Utils.STRING_TYPE, Utils.NULL_TYPE,
                Reserva.KEY_FECHA, Utils.STRING_TYPE, Utils.NULL_TYPE);
    }

    private ContentValues loadValues(Reserva reserva, int tipo) {
        ContentValues values = new ContentValues();
        if (tipo != 1)
            values.put(Reserva.KEY_ID, reserva.getId());
        values.put(Reserva.KEY_DNI, reserva.getDni());
        values.put(Reserva.KEY_CATEGORIA, reserva.getCategoria());
        values.put(Reserva.KEY_INSTALACION, reserva.getInstalacion());
        values.put(Reserva.KEY_HRAINI, reserva.getHraInicio());
        values.put(Reserva.KEY_HRAFIN, reserva.getHraFin());
        values.put(Reserva.KEY_FECHARESERVA,reserva.getFechaReserva());
        values.put(Reserva.KEY_PRECIO, reserva.getPrecio());
        values.put(Reserva.KEY_EMPLEADO, reserva.getEmpleado());
        values.put(Reserva.KEY_FECHA, reserva.getFecha());

        return values;
    }

    private Reserva loadFromCursor(Cursor cursor) {
        mReserva = new Reserva();
        mReserva.setId(cursor.getInt(0));
        mReserva.setDni(cursor.getInt(1));
        mReserva.setCategoria(cursor.getInt(2));
        mReserva.setInstalacion(cursor.getInt(3));
        mReserva.setHraInicio(cursor.getString(4));
        mReserva.setHraFin(cursor.getString(5));
        mReserva.setFechaReserva(cursor.getString(6));
        mReserva.setPrecio(cursor.getString(7));
        mReserva.setEmpleado(cursor.getString(8));
        mReserva.setFecha(cursor.getString(9));

        return mReserva;
    }

    public void insert(Reserva reserva) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        db.insert(Reserva.TABLE, null, loadValues(reserva, 1));
        DBManager.getInstance().closeDatabase();
    }

    public void delete(Reserva reserva) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        db.delete(Reserva.TABLE, Reserva.KEY_ID + " = ?", new String[]{String.valueOf(reserva.getId())});
        DBManager.getInstance().closeDatabase();
    }

    public Reserva get(int id) {
        mReserva = new Reserva();
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery("select * from " + Reserva.TABLE + " where " + Reserva.KEY_ID + " = " + id, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            mReserva = loadFromCursor(cursor);
            cursor.close();
        }
        DBManager.getInstance().closeDatabase();
        return mReserva;
    }

    public boolean isExist(int numero) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery("select * from " + Reserva.TABLE + " where " + Reserva.KEY_ID + " = " + numero, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        DBManager.getInstance().closeDatabase();
        return false;

    }

    public void update(Reserva reserva) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        String id = String.valueOf(reserva.getId());
        String selection = Reserva.KEY_ID + " = " + id;
        db.update(Reserva.TABLE, loadValues(reserva, 2), selection, null);
        DBManager.getInstance().closeDatabase();
    }

    public int getCount() {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        String countQuery = "SELECT  * FROM " + Reserva.TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        DBManager.getInstance().closeDatabase();
        cursor.close();
        return count;
    }

    public ArrayList<String> getAllFechas() {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        ArrayList<String> list = new ArrayList<String>();
        String query = String.format("select * from %s", Reserva.TABLE);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                mReserva = loadFromCursor(cursor);
                String fecha = Utils.getFechaOnlyDay(Utils.getFechaDate(mReserva.getFecha()));
                if (!list.contains(fecha))
                    list.add(fecha);
            } while (cursor.moveToNext());
        }
        DBManager.getInstance().closeDatabase();
        cursor.close();

        return list;
    }

    public ArrayList<Reserva> getAllByFecha(String fecha) {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        ArrayList<Reserva> list = new ArrayList<Reserva>();
        String query = String.format("select * from %s", Reserva.TABLE);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                mReserva = loadFromCursor(cursor);
                String f = Utils.getFechaOnlyDay(Utils.getFechaDate(mReserva.getFecha()));
                if (fecha.equals(f))
                    list.add(mReserva);
            } while (cursor.moveToNext());
        }
        DBManager.getInstance().closeDatabase();
        cursor.close();

        return list;
    }

    public ArrayList<Reserva> getAll() {
        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        ArrayList<Reserva> list = new ArrayList<Reserva>();
        String query = String.format("select * from %s", Reserva.TABLE);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                mReserva = loadFromCursor(cursor);
                list.add(mReserva);
            } while (cursor.moveToNext());
        }
        DBManager.getInstance().closeDatabase();
        cursor.close();

        return list;

    }

}
