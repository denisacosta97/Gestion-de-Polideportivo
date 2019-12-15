package com.unse.gestiondepolideportivo;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.unse.gestiondepolideportivo.BaseDatos.ReservaRepo;
import com.unse.gestiondepolideportivo.Modelo.Reserva;

import java.util.Calendar;

public class DialogoNewReservaActivity extends DialogFragment implements View.OnClickListener {

    View view;
    EditText edtDNI, edtHsIni, edtHsFin;
    Context mContext;
    Button btnAceptar;
    TextView txtCantidadMay, txtCantidadMen, txtTotal;
    Spinner mSpinnerCategorias, mSpinnerInstalaciones;
    String[] categorias = {"Seleccione una opción...", "Afiliado", "Docente", "Egresado", "Estudiante", "Jubilado", "Nodocente", "Particular"};
    String[] instalaciones = {"Seleccione una opción...", "Quincho Gris","Quincho Marrón","SUM"};

    int categoriaSelect = 0, instalacionesSelect = 0;

    public void setContext(Context ctx){
        this.mContext = ctx;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_new_reserva, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        //Esto es lo nuevoooooooo, evita los bordes cuadrados
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        loadViews();

        loadData();

        loadListener();

        float precioTotal = calcularPrecio(categoriaSelect, instalacionesSelect);
        String precio = Float.toString(precioTotal);
        txtTotal.setText("$" + precio);

        return view;
    }

    private void loadData() {
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categorias);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCategorias.setAdapter(dataAdapter2);

        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, instalaciones);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerInstalaciones.setAdapter(dataAdapter3);
    }

    private void loadListener() {
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dni = edtDNI.getText().toString().trim();
                if(!dni.equals("") && Utils.validarDNI(dni)){
                    String categoria = categorias[categoriaSelect];
                    String instalacion = instalaciones[instalacionesSelect];
                    String horaIni = edtHsIni.getText().toString();
                    String horaFin = edtHsFin.getText().toString();
                    String fecha = Utils.getFecha();
                    String precio = txtTotal.getText().toString();
                    String dniEmpleado = Utils.DNI_TRABAJADOR;

                    ReservaRepo reservaRepo = new ReservaRepo(getContext());
                    Reserva reserva = new Reserva(Integer.parseInt(dni),-1,
                            categoriaSelect,instalacionesSelect, horaIni,
                            horaFin, fecha, precio, dniEmpleado);
                    reservaRepo.insert(reserva);
                    Utils.showToast(getContext(), "Reserva registrada.");
                    dismiss();

                }else{
                    Utils.showToast(getContext(),"¡Ingrese un DNI!");
                }
            }
        });

        mSpinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoriaSelect = position;
                txtTotal.setText("");
                InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categoriaSelect = 0;
            }
        });

        mSpinnerInstalaciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                instalacionesSelect = position;
                txtTotal.setText("");
                float precioTotal = calcularPrecio(categoriaSelect, instalacionesSelect);
                String precio = Float.toString(precioTotal);
                txtTotal.setText("$" + precio);
                InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                instalacionesSelect = 0;
            }
        });

        edtHsIni.setOnClickListener(this);
        edtHsFin.setOnClickListener(this);
    }

    private void loadViews() {
        btnAceptar = view.findViewById(R.id.btnAceptar);
        edtDNI = view.findViewById(R.id.edtDNI);
        txtCantidadMay = view.findViewById(R.id.txtCantidadMay);
        txtCantidadMen = view.findViewById(R.id.txtCantidadMen);
        txtTotal = view.findViewById(R.id.txtTotal);
        mSpinnerCategorias = view.findViewById(R.id.spineer);
        mSpinnerInstalaciones = view.findViewById(R.id.spineer2);
        edtHsIni = view.findViewById(R.id.edtHraIni);
        edtHsFin = view.findViewById(R.id.edtHraFin);
    }

    private float calcularPrecio(int categ, int inst) {
        float precio = 0;

        switch (categ){
            case 0:
                precio = 0;
                break;
            case 1:
                //Afiliados
                if(inst == 1 || inst == 2) {
                    precio = 1100;
                }
                else {
                    if(inst != 0) {
                        precio = 2750;
                    }
                }
                break;
            case 4:
                //Estudiantes
                if(inst == 1 || inst == 2) {
                    precio = 1200;
                }
                else {
                    if(inst != 0) {
                        precio = 3000;
                    }
                }
                break;
            case 5:
                //Jubilado
                if(inst == 1 || inst == 2) {
                    precio = 925;
                }
                else {
                    if(inst != 0) {
                        precio = 2000;
                    }
                }
                break;
            case 7:
                //Particular
                if(inst == 1 || inst == 2) {
                    precio = 2200;
                }
                else {
                    if(inst != 0) {
                        precio = 5500;
                    }
                }
                break;
            default:
                //Docentes, Nodocentes y Egresados
                if(inst == 1 || inst == 2) {
                    precio = 1850;
                }
                else {
                    if(inst != 0) {
                        precio = 4000;
                    }
                }
                break;
        }
        return precio;
    }

    private void showTimeDialog(final int edt){
        final Calendar myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    if(edt == 0) {
                        myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        myCalender.set(Calendar.MINUTE, minute);
                        edtHsIni.setText(hourOfDay + ":" + minute);
                    }
                    else{
                        myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        myCalender.set(Calendar.MINUTE, minute);
                        edtHsFin.setText(hourOfDay + ":" + minute);
                    }
                }
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle("Hora:");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edtHraIni:
                showTimeDialog(0);
                break;
            case R.id.edtHraFin:
                showTimeDialog(1);
                break;
        }
    }
}
