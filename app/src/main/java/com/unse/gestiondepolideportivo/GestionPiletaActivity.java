package com.unse.gestiondepolideportivo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.unse.gestiondepolideportivo.Modelo.Opciones;
import com.unse.gestiondepolideportivo.RecyclerListener.ItemClickSupport;

import java.util.ArrayList;

public class GestionPiletaActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Opciones> mOpciones;
    OpcionesAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_pileta);

        setToolbar();

        loadViews();

        loadData();

        loadListener();

    }

    private void loadData() {
        mOpciones = new ArrayList<>();
        mOpciones.add(new Opciones(1, "Nuevo Ingreso", R.drawable.ic_nuevo,R.color.colorGreen));
        mOpciones.add(new Opciones(2, "Listado de Ingresos", R.drawable.ic_listado,R.color.colorGreen));
        mOpciones.add(new Opciones(3, "Enviar Datos", R.drawable.ic_subida,R.color.colorGreen));

        mAdapter = new OpcionesAdapter(mOpciones, getApplicationContext());
        mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }

    private void loadListener() {
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(mRecyclerView);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                switch((int)id){
                    case 1:
                        DialogoIngresoPolideportivo dialogo = new DialogoIngresoPolideportivo();
                        dialogo.show(getSupportFragmentManager(), "dialogo_entrada");
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(), ListadoIngresoActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(), EnviarDatosActivity.class));
                        break;
                }

            }
        });

    }

    private void loadViews() {
        mRecyclerView = findViewById(R.id.recycler);

    }

    private void setToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        findViewById(R.id.imgFlecha).setVisibility(View.VISIBLE);
        findViewById(R.id.imgFlecha).setOnClickListener(this);
        ((TextView)findViewById(R.id.txtTitulo)).setText("Gestión de Pileta");
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
