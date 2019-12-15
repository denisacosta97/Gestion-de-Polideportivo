package com.unse.gestiondepolideportivo;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.unse.gestiondepolideportivo.BaseDatos.BDGestor;
import com.unse.gestiondepolideportivo.BaseDatos.DBManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


public class Utils {


    //Constantes para las BD
    public static final String STRING_TYPE = "text";
    public static final String INT_TYPE = "integer";
    public static final String FLOAT_TYPE = "float";
    public static final String NULL_TYPE = "not null";
    public static final String AUTO_INCREMENT = "primary key autoincrement";
    public static final String IS_FIRST_TIME_LAUNCH = "is_first";
    public static final String PREF_NAME = "bienestar_welcome";
    public static final String DNI_TRABAJADOR = "dni";
    public static final String EMAIL_CONTACT1 = "appmicrosde@gmail.com";
    public static final String PASS_CONTACT1 = "unse@2018";


    public static void showLog(String t, String msj) {
        if (false)
            Log.e(t, msj);
    }

    public static void showToast(Context c, String msj) {
        Toast.makeText(c, msj, Toast.LENGTH_SHORT).show();
    }


    public static String getFecha() {

        Date date = new Date(System.currentTimeMillis());

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String value = cal.get(Calendar.DAY_OF_MONTH) + "/"
                + (cal.get(Calendar.MONTH) + 1) + "/" +
                cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" +
                cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);

        return value;


    }

    public static String getFechaOnlyDay(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String value = cal.get(Calendar.DAY_OF_MONTH) + "/"
                + (cal.get(Calendar.MONTH) + 1) + "/" +
                cal.get(Calendar.YEAR);

        return value;


    }

    public static Map<TimeUnit,Long> computeDiff(Date date1, Date date2) {

        long diffInMillies = date2.getTime() - date1.getTime();

        //Map:{DAYS=1, HOURS=3, MINUTES=46, SECONDS=40, MILLISECONDS=0, MICROSECONDS=0, NANOSECONDS=0}

        //create the list
        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);

        //create the result map of TimeUnit and difference
        Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
        long milliesRest = diffInMillies;

        for ( TimeUnit unit : units ) {

            //calculate difference in millisecond
            long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;

            //put the result in the map
            result.put(unit,diff);
        }

        return result;
    }

    public static Date getFechaDate(String fecha) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date fechaI = null;
        try {
            fechaI = simpleDateFormat.parse(fecha);

        } catch (ParseException e) {
            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                fechaI = simpleDateFormat.parse(fecha);

            } catch (ParseException e2) {
                e2.printStackTrace();
            }
        }
        return fechaI;

    }

    public static String getHora(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String hora = "", minutos = "";
        if (cal.get(Calendar.HOUR_OF_DAY) == 0)
            hora = "00";
        else
            hora = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        if (cal.get(Calendar.MINUTE) == 0)
            minutos = "00";
        else minutos = String.valueOf(cal.get(Calendar.MINUTE));
        String value = hora + ":" +
                minutos;

        return value;
    }

    public static String getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int value = cal.get(Calendar.YEAR);

        return String.valueOf(value);
    }

    public static String getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int value = cal.get(Calendar.DAY_OF_MONTH);

        return String.valueOf(value);
    }


    public static String getMes(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        switch (cal.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                return "Enero";
            case Calendar.FEBRUARY:
                return "Febrero";
            case Calendar.MARCH:
                return "Marzo";
            case Calendar.APRIL:
                return "Abril";
            case Calendar.MAY:
                return "Mayo";
            case Calendar.JUNE:
                return "Junio";
            case Calendar.JULY:
                return "Julio";
            case Calendar.AUGUST:
                return "Agosto";
            case Calendar.SEPTEMBER:
                return "Septiembre";
            case Calendar.OCTOBER:
                return "Octubre";
            case Calendar.NOVEMBER:
                return "Noviembre";
            case Calendar.DECEMBER:
                return "Diciembre";
        }

        return "";
    }


    public static String getDayWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return "Lunes";
            case Calendar.TUESDAY:
                return "Martes";
            case Calendar.WEDNESDAY:
                return "Miércoles";
            case Calendar.THURSDAY:
                return "Jueves";
            case Calendar.FRIDAY:
                return "Viernes";
            case Calendar.SATURDAY:
                return "Sábado";
            case Calendar.SUNDAY:
                return "Domingo";
        }
        return "";
    }

    public static boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public static boolean validarDNI(String dni) {
        boolean isNumber = true;
        try {
            Integer.parseInt(dni);
        } catch (NumberFormatException e) {
            isNumber = false;
        }
        return isNumber && dni.length() >= 7 && validarNumero(dni);
    }

    public static boolean validarNumero(String numero) {
        String regex = "[0-9]+";
        return numero.matches(regex);
    }

    //Metodo para saber si un permiso esta autorizado o no
    public static boolean isPermissionGranted(Context ctx, String permision) {
        int permission = ActivityCompat.checkSelfPermission(
                ctx,
                permision);
        return permission == PackageManager.PERMISSION_GRANTED;

    }


    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public static void initBD(Context context) {
        BDGestor gestor = new BDGestor(context);
        DBManager.initializeInstance(gestor);
    }


}
