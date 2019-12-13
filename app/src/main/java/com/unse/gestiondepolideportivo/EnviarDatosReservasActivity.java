package com.unse.gestiondepolideportivo;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.unse.gestiondepolideportivo.BaseDatos.ReservaRepo;
import com.unse.gestiondepolideportivo.Modelo.Reserva;

import java.util.ArrayList;

public class EnviarDatosReservasActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbar;
    Spinner mSpinner;
    ReservaRepo mReservaRepo;
    Button btnEnviar;
    TextView txtInfo;
    String[] fechas = new String[]{};
    String fecha;
    int cantidad = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_datos_reservas);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setToolbar();

        loadViews();

        loadData();

        loadListener();

    }

    private void loadData() {
        mReservaRepo = new ReservaRepo(getApplicationContext());
        fechas = getFechas();
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, fechas);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter2);

    }

    private String[] getFechas() {
        ArrayList<String> fechas = mReservaRepo.getAllFechas();
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
        ArrayList<Reserva> list = mReservaRepo.getAllByFecha(fecha);
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
        ((TextView) findViewById(R.id.txtTitulo)).setText("Envío de datos reservas");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgFlecha:
                onBackPressed();
                break;
            case R.id.btnEnviar:
                enviarMensaje(v);
                break;
        }
    }

    private void enviarMensaje(View v) {
        String subject = "ENVIO DATOS "+Utils.getFecha();
        StringBuilder mensaje = new StringBuilder();
        for (Reserva reserva : new ReservaRepo(getApplicationContext()).getAllByFecha(fecha)){
            mensaje.append(reserva.toString());
            mensaje.append(",");
        }
        sendEmails(mensaje, subject);
    }

    private void sendEmails(StringBuilder mensaje, String subject) {
        String email = Utils.EMAIL_CONTACT1;
        try {
            BackgroundMail bm = new BackgroundMail(this);
            bm.setGmailUserName(email);
            bm.setGmailPassword(Utils.PASS_CONTACT1);
            bm.setMailTo(email);
            bm.setFormSubject(subject);
            bm.setFormBody(mensaje.toString());
            bm.setType(BackgroundMail.TYPE_PLAIN);
            bm.setSendingMessageError("Error, intenta de nuevo");
            bm.setSendingMessageSuccess("¡Mensaje enviado!");
            bm.setOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                @Override
                public void onSuccess() {
                    finish();
                }
            });
            bm.setOnFailCallback(new BackgroundMail.OnFailCallback() {
                @Override
                public void onFail() {
                    Toast.makeText(EnviarDatosReservasActivity.this, "Por favor, envíalo nuevamente.", Toast.LENGTH_SHORT).show();
                }
            });
            bm.setSendingMessage("Enviando mensaje...");
            bm.send();

        }catch (Exception e){
            Toast.makeText(EnviarDatosReservasActivity.this, "ERROR, envíalo nuevamente.", Toast.LENGTH_SHORT).show();
        }

    }


}
