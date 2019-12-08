package com.unse.gestiondepolideportivo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.unse.gestiondepolideportivo.BaseDatos.PiletaRepo;
import com.unse.gestiondepolideportivo.Modelo.PiletaIngreso;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class EnviarDatosActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbar;
    Spinner mSpinner;
    PiletaRepo mPiletaRepo;
    Button btnEnviar;
    TextView txtInfo;
    String[] fechas = new String[]{};
    String fecha;
    int cantidad = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_datos);

        setToolbar();

        loadViews();

        loadData();

        loadListener();

    }

    private void loadData() {
        mPiletaRepo = new PiletaRepo(getApplicationContext());
        fechas = getFechas();
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, fechas);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter2);

    }

    private String[] getFechas() {
        ArrayList<String> fechas = mPiletaRepo.getAllFechas();
        return fechas.toArray(new String[0]);
    }

    private void loadListener() {
        btnEnviar.setOnClickListener(this);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadInfo(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void loadInfo(int position) {
        fecha = fechas[position];
        ArrayList<PiletaIngreso> list = mPiletaRepo.getAllByFecha(fecha);
        cantidad = list.size();
        if (list.size() > 1)
            txtInfo.setText(String.format("%s registros encontrados", list.size()));
        else
            txtInfo.setText(String.format("%s registro encontrado", list.size()));
    }

    private void loadViews() {
        btnEnviar = findViewById(R.id.btnEnviar);
        txtInfo = findViewById(R.id.txtInfo);
        mSpinner = findViewById(R.id.spineer);


    }

    private void setToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        findViewById(R.id.imgFlecha).setVisibility(View.VISIBLE);
        findViewById(R.id.imgFlecha).setOnClickListener(this);
        ((TextView) findViewById(R.id.txtTitulo)).setText("Envío de Datos");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgFlecha:
                onBackPressed();
                break;
            case R.id.btnEnviar:
                enviar();
                break;
        }
    }

    private void enviar() {
        if(cantidad > 0){
            sendMail();
        }else{
            Utils.showToast(getApplicationContext(),"No hay datos registrados en este día");
        }
    }

    private void sendMail() {
       // String email = new PreferenciasManager(getContext()).getValueString(UtilsCristian.EMAIL_CONTACT);
        String subject = "ENVIO DATOS "+Utils.getFecha();
        StringBuilder mensaje = new StringBuilder();
        for (PiletaIngreso piletaIngreso : new PiletaRepo(getApplicationContext()).getAllByFecha(fecha)){
            mensaje.append(piletaIngreso.toString());
            mensaje.append(",");
        }


        //Creating SendMail object
        SendMail sm = new SendMail(getApplicationContext(), "appmicrosde@gmail.com", subject, "jejeje");

        //Executing sendmail to send email
        sm.execute();
    }

}
