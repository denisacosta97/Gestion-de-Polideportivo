package com.unse.gestiondepolideportivo.Actividades;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.unse.gestiondepolideportivo.Adaptadores.ListadoIngresosAdapter;
import com.unse.gestiondepolideportivo.BaseDatos.PiletaParcialRepo;
import com.unse.gestiondepolideportivo.BaseDatos.PiletaRepo;
import com.unse.gestiondepolideportivo.Herramientas.Utils;
import com.unse.gestiondepolideportivo.Modelo.ItemDato;
import com.unse.gestiondepolideportivo.Modelo.ItemFecha;
import com.unse.gestiondepolideportivo.Modelo.ItemListado;
import com.unse.gestiondepolideportivo.Modelo.PiletaIngreso;
import com.unse.gestiondepolideportivo.Modelo.PiletaIngresoParcial;
import com.unse.gestiondepolideportivo.R;
import com.unse.gestiondepolideportivo.RecyclerListener.ItemClickSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListadoIngresoActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ListadoIngresosAdapter adapter;

    List<ItemListado> listado = new ArrayList<>();

    List<ItemListado> listaOficial = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_pileta);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setToolbar();

        loadViews();

        loadData();

        loadListener();

    }

    public List<ItemListado> getAll(ArrayList<PiletaIngreso> list) {
        List<ItemListado> listados = new ArrayList<>();
        PiletaParcialRepo parcialRepo = new PiletaParcialRepo(getApplicationContext());
        for (PiletaIngreso piletaIngreso : list) {
            ArrayList<PiletaIngresoParcial> listPar = parcialRepo.getAllByIngreso(piletaIngreso.getId());
            int cant = 1;
            for (PiletaIngresoParcial parcial : listPar){
                cant = cant + parcial.getCantidad();
            }
            piletaIngreso.setCantidadTotal(cant);
            listados.add(new ItemDato(piletaIngreso));
        }
        return listados;
    }

    private void loadData() {
        mRecyclerView.setHasFixedSize(true);

       listado = getAll(new PiletaRepo(getApplicationContext()).getAll());

        HashMap<String, List<ItemListado>> groupedHashMap = groupDataIntoHashMap(listado);

        for (String date : groupedHashMap.keySet()) {
            ItemFecha dateItem = new ItemFecha();
            dateItem.setFecha(date);
            listaOficial.add(dateItem);

            for (ItemListado item : groupedHashMap.get(date)) {
                ItemDato generalItem = (ItemDato) item;
                listaOficial.add(generalItem);
            }
        }
        adapter = new ListadoIngresosAdapter(this, listaOficial);
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);


    }

    private HashMap<String, List<ItemListado>> groupDataIntoHashMap(List<ItemListado> list) {

        HashMap<String, List<ItemListado>> groupedHashMap = new HashMap<>();

        for (ItemListado dato : list) {

            ItemDato piletaIngreso = (ItemDato) dato;

            //String key = piletaIngreso.getPiletaIngreso().getFecha();
            String key = Utils.getFechaOnlyDay(Utils.getFechaDate(piletaIngreso.getPiletaIngreso().getFecha()));

            if (groupedHashMap.containsKey(key)) {
                groupedHashMap.get(key).add(dato);
            } else {
                List<ItemListado> nuevaLista = new ArrayList<>();
                nuevaLista.add(dato);
                groupedHashMap.put(key, nuevaLista);
            }
        }


        return groupedHashMap;
    }




    private void loadListener() {
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(mRecyclerView);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {

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
        ((TextView)findViewById(R.id.txtTitulo)).setText("Listado de Ingresos");
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

