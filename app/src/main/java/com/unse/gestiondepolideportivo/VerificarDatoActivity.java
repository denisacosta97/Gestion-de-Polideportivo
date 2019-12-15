package com.unse.gestiondepolideportivo;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unse.gestiondepolideportivo.Modelo.PiletaIngresoPorFechas;
import com.unse.gestiondepolideportivo.Modelo.Reserva;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VerificarDatoActivity extends AppCompatActivity implements View.OnClickListener {
    
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_datos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setToolbar();

        loadViews();

        loadData();

        loadListener();

    }

    private void loadListener() {
    }

    private void loadData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        PiletaIngresoPorFechas porFechas =null;
        ArrayList<String> fechas = new ArrayList<>();
        fechas.add("34/5/&6");
        //porFechas.setFechas(fechas);
        ArrayList<Integer> tipos = new ArrayList<>();
        tipos.add(4);
        //porFechas.setTipos(tipos);

        Reserva reserva = new Reserva(40657677,1,2,3,"jeej","eej","eje","34","405676");

        db.collection("reservas").add(reserva.toHashMap()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        })
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Utils.showToast(getApplicationContext(),"Datos cargados con éxito");
            }
        });

        db.collection("pileta").document("40657677").set(porFechas).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Utils.showToast(getApplicationContext(),"Subido!");
            }
        });

    }

    private void loadViews() {
    }

    private void setToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        findViewById(R.id.imgFlecha).setVisibility(View.VISIBLE);
        findViewById(R.id.imgFlecha).setOnClickListener(this);
        ((TextView)findViewById(R.id.txtTitulo)).setText("Verificacion de Datos");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgFlecha:
                onBackPressed();
                break;
        }
    }
}
