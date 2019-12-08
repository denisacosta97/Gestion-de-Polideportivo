package com.unse.gestiondepolideportivo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbar;
    CardView mCardViewPileta, mCardViewReservas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();

        loadViews();

        loadListener();

        Utils.initBD(getApplicationContext());

    }

    private void loadListener() {
        mCardViewReservas.setOnClickListener(this);
        mCardViewPileta.setOnClickListener(this);
    }

    private void loadViews() {
        mCardViewPileta = findViewById(R.id.cardPileta);
        mCardViewReservas = findViewById(R.id.cardReservas);
    }

    private void setToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        ((TextView)findViewById(R.id.txtTitulo)).setText("Gestión de Polideportivo");
        setSupportActionBar(mToolbar);
        findViewById(R.id.imgFlecha).setVisibility(View.GONE);
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
        }

    }
}
