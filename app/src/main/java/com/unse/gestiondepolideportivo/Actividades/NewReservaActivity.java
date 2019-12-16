package com.unse.gestiondepolideportivo.Actividades;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unse.gestiondepolideportivo.BaseDatos.ReservaRepo;
import com.unse.gestiondepolideportivo.Herramientas.PreferenciasManager;
import com.unse.gestiondepolideportivo.Herramientas.Utils;
import com.unse.gestiondepolideportivo.Modelo.Reserva;
import com.unse.gestiondepolideportivo.Modelo.ReservasPorFechas;
import com.unse.gestiondepolideportivo.Herramientas.DatePickerFragment;
import com.unse.gestiondepolideportivo.R;

import java.util.Calendar;

public class NewReservaActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbar;
    EditText edtDNI, edtHsIni, edtHsFin, edtFechaReserva;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reserva);

        setToolbar();

        loadViews();

        loadData();

        loadListener();

        float precioTotal = calcularPrecio(categoriaSelect, instalacionesSelect);
        String precio = Float.toString(precioTotal);
        txtTotal.setText("$" + precio);

    }

    private void loadData() {
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorias);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCategorias.setAdapter(dataAdapter2);

        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, instalaciones);
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
                    String fechaReserva = edtFechaReserva.getText().toString();
                    String precio = txtTotal.getText().toString();
                    String dniEmpleado = new PreferenciasManager(getApplicationContext()).getValueString(Utils.DNI_TRABAJADOR);

                    ReservaRepo reservaRepo = new ReservaRepo(getApplicationContext());
                    Reserva reserva = new Reserva(Integer.parseInt(dni),-1,
                            categoriaSelect,instalacionesSelect, horaIni,
                            horaFin, fechaReserva, precio, dniEmpleado, fecha);
                    reservaRepo.insert(reserva);
                    Utils.showToast(getApplicationContext(), "Reserva registrada.");

                    ReservasPorFechas reservasPorFechas = new ReservasPorFechas(dni, instalacionesSelect, fechaReserva, fecha);
                    sendData(reservasPorFechas);
                    finish();
                }else{
                    Utils.showToast(getApplicationContext(),"¡Ingrese un DNI!");
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
        edtFechaReserva.setOnClickListener(this);
    }

    private void loadViews() {
        btnAceptar = findViewById(R.id.btnAceptar);
        edtDNI = findViewById(R.id.edtDNI);
        txtCantidadMay = findViewById(R.id.txtCantidadMay);
        txtCantidadMen = findViewById(R.id.txtCantidadMen);
        txtTotal = findViewById(R.id.txtTotal);
        mSpinnerCategorias = findViewById(R.id.spineer);
        mSpinnerInstalaciones = findViewById(R.id.spineer2);
        edtHsIni = findViewById(R.id.edtHraIni);
        edtHsFin = findViewById(R.id.edtHraFin);
        edtFechaReserva = findViewById(R.id.edtFechaReserva);
    }

    private void sendData(ReservasPorFechas reservasPorFechas) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reservas").document(reservasPorFechas.getDni()).set(reservasPorFechas).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Utils.showToast(getApplicationContext(), "¡Error al enviar datos!");
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Utils.showToast(getApplicationContext(), "¡Registro subido!");
            }
        });
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle("Hora:");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    private void showDateDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + "/" + (month+1) + "/" + year;
                edtFechaReserva.setText(selectedDate);
            }
        });
        newFragment.show(getFragmentManager(), "datePicker");

    }

    private void setToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        findViewById(R.id.imgFlecha).setVisibility(View.VISIBLE);
        findViewById(R.id.imgFlecha).setOnClickListener(this);
        ((TextView)findViewById(R.id.txtTitulo)).setText("Nueva reserva");
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
            case R.id.imgFlecha:
                onBackPressed();
                break;
            case R.id.edtFechaReserva:
                showDateDialog();
                break;
        }
    }

}
