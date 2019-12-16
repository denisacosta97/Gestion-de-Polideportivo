package com.unse.gestiondepolideportivo.Actividades;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.unse.gestiondepolideportivo.Herramientas.PreferenciasManager;
import com.unse.gestiondepolideportivo.Herramientas.Utils;
import com.unse.gestiondepolideportivo.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbar;
    CardView mCardViewPileta, mCardViewReservas;
    Button mBtnSalir;
    private PreferenciasManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setToolbar();

        loadViews();

        loadListener();

        Utils.initBD(getApplicationContext());

    }

    private void loadListener() {
        mCardViewReservas.setOnClickListener(this);
        mCardViewPileta.setOnClickListener(this);
        mBtnSalir.setOnClickListener(this);
    }

    private void loadViews() {
        mCardViewPileta = findViewById(R.id.cardPileta);
        mCardViewReservas = findViewById(R.id.cardReservas);
        mBtnSalir = findViewById(R.id.btnSalir);
    }

    private void setToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        ((TextView)findViewById(R.id.txtTitulo)).setText("Gestión de Polideportivo");
        setSupportActionBar(mToolbar);
        //findViewById(R.id.imgFlecha).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cardPileta:
                startActivity(new Intent(getApplicationContext(), GestionPiletaActivity.class));
                break;
            case R.id.cardReservas:
                startActivity(new Intent(getApplicationContext(), GestionReservaActivity.class));
                break;
            case R.id.btnSalir:
                prefManager = new PreferenciasManager(this);
                prefManager.setFirstTimeLaunch(true);
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }

    }
}
