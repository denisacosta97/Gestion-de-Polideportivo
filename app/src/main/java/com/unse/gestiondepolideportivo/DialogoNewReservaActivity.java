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
    ImageButton btnMasMay, btnMasMen, btnMenosMay, btnMenosMen;
    EditText edtDNI, edtHsIni, edtHsFin;
    Context mContext;
    Button btnAceptar;
    TextView txtCantidadMay, txtCantidadMen, txtTotal;
    Spinner mSpinnerCategorias, mSpinnerInstalaciones;
    String[] categorias = {"Estudiante","Docente","No Docente","Afiliado","Particular"};
    String[] instalaciones = {"Quincho Gris","Quincho Marrón","SUM"};

    int contadorMay = 0, contadorMeno = 0, categoriaSelect = 0, instalacionesSelect = 0;
    String categoria, instalacion;

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

        return view;
    }

    private void loadData() {
        contadorMeno = contadorMay = 0;
        updateCounter(contadorMay, contadorMeno);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categorias);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCategorias.setAdapter(dataAdapter2);

        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, instalaciones);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerInstalaciones.setAdapter(dataAdapter3);
    }

    private void updateCounter(int may, int men) {
        txtCantidadMay.setText(String.valueOf(may));
        txtCantidadMen.setText(String.valueOf(men));
    }

    private void loadListener() {
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dni = edtDNI.getText().toString().trim();
                if(!dni.equals("") && Utils.validarDNI(dni)){
                    String cantMay = txtCantidadMay.getText().toString();
                    String cantMen = txtCantidadMen.getText().toString();
                    String categoria = categorias[categoriaSelect];
                    String instalacion = instalaciones[instalacionesSelect];
                    String horaIni = edtHsIni.toString();
                    String horaFin = edtHsFin.toString();
                    String fecha = Utils.getFecha();
                    float precio = txtTotal.getInputType(); //revisar
                    if (Integer.parseInt(cantMay) >= 1 || Integer.parseInt(cantMen) >= 1){
                        ReservaRepo reservaRepo = new ReservaRepo(getContext());
                        Reserva reserva = new Reserva(Integer.parseInt(dni),-1, categoriaSelect+1,instalacionesSelect+1,
                               Integer.parseInt(cantMay), Integer.parseInt(cantMen), horaIni, horaFin, fecha, precio);
                        reservaRepo.insert(reserva);
                    }else{
                        Utils.showToast(getContext(), "¡Al menos debe haber un ingreso!");
                    }
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
                float precioTotal = calcularPrecio(categoriaSelect+1, instalacionesSelect+1);
                String precio = Float.toString(precioTotal);
                txtTotal.setText("$" + precio);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                instalacionesSelect = 0;
            }
        });

        btnMasMay.setOnClickListener(this);
        btnMasMen.setOnClickListener(this);
        btnMenosMay.setOnClickListener(this);
        btnMenosMen.setOnClickListener(this);
        edtHsIni.setOnClickListener(this);
        edtHsFin.setOnClickListener(this);
    }

    private float calcularPrecio(int categ, int inst) {
        float precio = 0;

        switch (categ){
            case 1:
                //Estudiantes
                if(inst == 1 || inst == 2)
                    precio = 1200;
                else
                    precio = 3000;
                break;
            case 4:
                //Afiliados
                if(inst == 1 || inst == 2)
                    precio = 1100;
                else
                    precio = 2750;
                break;
            case 5:
                //Particular
                if(inst == 1 || inst == 2)
                    precio = 2200;
                else
                    precio = 5500;
                break;
            default:
                //Docentes, Nodocentes y Egresados
                if(inst == 1 || inst == 2)
                    precio = 1850;
                else
                    precio = 4000;
                break;
        }
        return precio;
    }


    private void loadViews() {
        btnAceptar = view.findViewById(R.id.btnAceptar);
        edtDNI = view.findViewById(R.id.edtDNI);
        txtCantidadMay = view.findViewById(R.id.txtCantidadMay);
        txtCantidadMen = view.findViewById(R.id.txtCantidadMen);
        btnMasMay = view.findViewById(R.id.btnAddMay);
        btnMenosMay = view.findViewById(R.id.btnRemoveMay);
        btnMasMen = view.findViewById(R.id.btnAddMen);
        btnMenosMen = view.findViewById(R.id.btnRemoveMen);
        txtTotal = view.findViewById(R.id.txtTotal);
        mSpinnerCategorias = view.findViewById(R.id.spineer);
        mSpinnerInstalaciones = view.findViewById(R.id.spineer2);
        edtHsIni = view.findViewById(R.id.edtHraIni);
        edtHsFin = view.findViewById(R.id.edtHraFin);
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
            case R.id.btnAddMay:
                contadorMay++;
                updateCounter(contadorMay, contadorMeno);
                break;
            case R.id.btnRemoveMay:
                if (contadorMay <= 0){
                    contadorMay = 0;
                }else{
                    contadorMay--;
                }
                updateCounter(contadorMay, contadorMeno);
                break;
            case R.id.btnAddMen:
                contadorMeno++;
                updateCounter(contadorMay, contadorMeno);
                break;
            case R.id.btnRemoveMen:
                if (contadorMeno <= 0){
                    contadorMeno = 0;
                }else{
                    contadorMeno--;
                }
                updateCounter(contadorMay, contadorMeno);
                break;
            case R.id.edtHraIni:
                showTimeDialog(0);
                break;
            case R.id.edtHraFin:
                showTimeDialog(1);
                break;
        }
    }
}
