package com.unse.gestiondepolideportivo;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.unse.gestiondepolideportivo.BaseDatos.ReservaRepo;
import com.unse.gestiondepolideportivo.Modelo.ItemDato;
import com.unse.gestiondepolideportivo.Modelo.ItemDatoReserva;
import com.unse.gestiondepolideportivo.Modelo.ItemFecha;
import com.unse.gestiondepolideportivo.Modelo.ItemListado;
import com.unse.gestiondepolideportivo.Modelo.ItemReserva;
import com.unse.gestiondepolideportivo.Modelo.PiletaIngreso;
import com.unse.gestiondepolideportivo.Modelo.Reserva;
import com.unse.gestiondepolideportivo.RecyclerListener.ItemClickSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListadoReservaActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ListadoReservasAdapter adapter;

    List<ItemReserva> listado = new ArrayList<>();

    List<ItemReserva> listaOficial = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_reserva);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setToolbar();

        loadViews();

        loadData();

        loadListener();

    }

    public List<ItemReserva> getAll(ArrayList<Reserva> list) {
        List<ItemReserva> listados = new ArrayList<>();
        for (Reserva reserva : list) {
            listados.add(new ItemDatoReserva(reserva));
        }
        return listados;
    }

    private void loadData() {
        mRecyclerView.setHasFixedSize(true);

        listado = getAll(new ReservaRepo(getApplicationContext()).getAll());

        HashMap<String, List<ItemReserva>> groupedHashMap = groupDataIntoHashMap(listado);

        for (String date : groupedHashMap.keySet()) {
            ItemFechaReserva dateItem = new ItemFechaReserva();
            dateItem.setFecha(date);
            listaOficial.add(dateItem);

            for (ItemReserva item : groupedHashMap.get(date)) {
                ItemDatoReserva generalItem = (ItemDatoReserva) item;
                listaOficial.add(generalItem);
            }
        }
        adapter = new ListadoReservasAdapter(this, listaOficial);
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);


    }

    private HashMap<String, List<ItemReserva>> groupDataIntoHashMap(List<ItemReserva> list) {

        HashMap<String, List<ItemReserva>> groupedHashMap = new HashMap<>();

        for (ItemReserva dato : list) {

            ItemDatoReserva itemDatoReserva = (ItemDatoReserva) dato;

            //String key = piletaIngreso.getPiletaIngreso().getFecha();
            String key = Utils.getFechaOnlyDay(Utils.getFechaDate(itemDatoReserva.getReserva().getFecha()));

            if (groupedHashMap.containsKey(key)) {
                groupedHashMap.get(key).add(dato);
            } else {
                List<ItemReserva> nuevaLista = new ArrayList<>();
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
        ((TextView)findViewById(R.id.txtTitulo)).setText("Listado de reservas");
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
