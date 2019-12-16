package com.unse.gestiondepolideportivo.Actividades;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unse.gestiondepolideportivo.Adaptadores.AcompañanteAdapter;
import com.unse.gestiondepolideportivo.BaseDatos.PiletaParcialRepo;
import com.unse.gestiondepolideportivo.BaseDatos.PiletaRepo;
import com.unse.gestiondepolideportivo.Herramientas.ObservadorPrecio;
import com.unse.gestiondepolideportivo.Herramientas.PreferenciasManager;
import com.unse.gestiondepolideportivo.Herramientas.Utils;
import com.unse.gestiondepolideportivo.Modelo.PiletaIngreso;
import com.unse.gestiondepolideportivo.Modelo.PiletaIngresoParcial;
import com.unse.gestiondepolideportivo.Modelo.PiletaIngresoPorFechas;
import com.unse.gestiondepolideportivo.R;
import com.unse.gestiondepolideportivo.RecyclerListener.ItemClickSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class IngresoPolideportivoActivity extends AppCompatActivity implements View.OnClickListener, ObservadorPrecio {

    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<PiletaAcompañante> mList;
    AcompañanteAdapter mAdapter;

    Button btnAceptar, btnDia, btnSemana, btnMes, btnAñadir;
    LinearLayout mLayout;
    EditText edtDNI;
    TextView txtTotal;
    Spinner mSpinnerCategorias;
    String[] categorias = {"Seleccione una opción...", "Afiliado", "Docente", "Egresado", "Estudiante", "Jubilado", "Nodocente", "Particular"};

    int categoriaSelect = 0, tipo = 0;

    float total = 0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_poli);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setToolbar();

        loadViews();

        loadData();

        loadListener();

        updatePrecio(0);

    }

    private void loadData() {
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorias);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCategorias.setAdapter(dataAdapter2);

        mList = new ArrayList<>();

        mAdapter = new AcompañanteAdapter(mList, getApplicationContext(), this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }

    private void loadListener() {
        btnAñadir.setOnClickListener(this);
        btnDia.setOnClickListener(this);
        btnMes.setOnClickListener(this);
        btnSemana.setOnClickListener(this);
        mSpinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoriaSelect = position;
                updateButtons(R.id.btnDia);
                tipo = 0;
                float precioTotal = calcularPrecio(categoriaSelect);
                updatePrecio(precioTotal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(mRecyclerView);
        itemClickSupport.setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(RecyclerView parent, View view, int position, long id) {
                mList.remove(position);
                mAdapter.notifyDataSetChanged();
                updatePrecio(calcularPrecio(categoriaSelect));
                return false;
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dni = edtDNI.getText().toString().trim();
                if (!dni.equals("") && Utils.validarDNI(dni)) {
                    String dniEmpelado = new PreferenciasManager(getApplicationContext()).getValueString(Utils.DNI_TRABAJADOR);
                    String categoria = categorias[categoriaSelect];
                    String fecha = Utils.getFecha();
                    float precio = total;
                    PiletaRepo piletaRepo = new PiletaRepo(getApplicationContext());
                    PiletaIngreso piletaIngreso = new PiletaIngreso(Integer.parseInt(dni), -1, categoriaSelect
                            , fecha, precio, Integer.parseInt(dniEmpelado));
                    int id = piletaRepo.insert(piletaIngreso);
                    PiletaParcialRepo piletaParcialRepo = new PiletaParcialRepo(getApplicationContext());
                    for (PiletaAcompañante pileta : mList) {
                        PiletaIngresoParcial parcial = new PiletaIngresoParcial(pileta.getCategoria(), pileta.getCantidad(),
                                pileta.getPrecioTotal());
                        parcial.setIdIngreso(id);
                        piletaParcialRepo.insert(parcial);
                    }
                    if (tipo != 0) {
                        if (tipo == 1){
                            Date date = Utils.getFechaDate(fecha);
                            Calendar c = Calendar.getInstance();
                            c.setTime(date);
                            c.add(Calendar.DATE, 7);  // number of days to add
                            date = c.getTime();  // dt is now the new date
                            fecha = Utils.getFechaOnlyDay(date);
                        }else if (tipo == 2){
                            Date date = Utils.getFechaDate(fecha);
                            Calendar c = Calendar.getInstance();
                            c.setTime(date);
                            c.add(Calendar.MONTH, 1);  // number of days to add
                            date = c.getTime();  // dt is now the new date
                            fecha = Utils.getFechaOnlyDay(date);
                        }
                        PiletaIngresoPorFechas pileta = new PiletaIngresoPorFechas(dni, categoriaSelect, fecha, tipo);
                        sendData(pileta);
                    }
                    Utils.showToast(getApplicationContext(), "¡Guardado!");
                    finish();
                } else {
                    Utils.showToast(getApplicationContext(), "¡Ingrese un DNI!");
                }
            }
        });

    }

    private void updatePrecio(float precioTotal) {
        float precio = 0f;
        for (PiletaAcompañante pileta : mList) {
            precio = precio + pileta.getPrecioTotal();
        }
        precio = precio + precioTotal;
        total = precio;
        txtTotal.setText(String.format("$ %s", precio));
    }

    private void sendData(PiletaIngresoPorFechas pileta) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pileta").document(pileta.getDni()).set(pileta).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Utils.showToast(getApplicationContext(), "¡Error al enviar datos!");
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Utils.showToast(getContext(), "¡Registro subido!");
                Toast.makeText(getApplicationContext(), "¡Registro subido!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadViews() {
        mRecyclerView = findViewById(R.id.recycler);
        btnAceptar = findViewById(R.id.btnAceptar);
        btnDia = findViewById(R.id.btnDia);
        btnSemana = findViewById(R.id.btnSemana);
        btnMes = findViewById(R.id.btnMes);
        edtDNI = findViewById(R.id.edtDNI);
        txtTotal = findViewById(R.id.txtTotal);
        mSpinnerCategorias = findViewById(R.id.spineer);
        btnAñadir = findViewById(R.id.btnAñadir);
        mLayout = findViewById(R.id.layoutAcompañantes);
    }

    private void setToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        findViewById(R.id.imgFlecha).setVisibility(View.VISIBLE);
        findViewById(R.id.imgFlecha).setOnClickListener(this);
        ((TextView) findViewById(R.id.txtTitulo)).setText("Ingreso Nueco");
    }

    private float calcularPrecio(int categ) {
        float precioTotal = 0;

        switch (categ) {
            case 1:
            case 0:
                //Afiliados
                precioTotal = 0;
                break;
            case 4:
            case 5:
                //Estudiantes
                if (tipo == 0) {
                    precioTotal = 50;
                } else if (tipo == 1) {
                    precioTotal = 250;
                } else {
                    precioTotal = 1000;
                }
                break;

            case 7:
                //Particular
                precioTotal = 250;
                break;
            default:
                //Docentes, Nodocentes y Egresados
                if (tipo == 0) {
                    precioTotal = 100;
                } else if (tipo == 1) {
                    precioTotal = 500;
                } else {
                    precioTotal = 2000;
                }
                break;
        }
        return precioTotal;
    }

    @Override
    public void onClick(View v) {
        float precioTotal = 0;
        switch (v.getId()) {
            case R.id.imgFlecha:
                onBackPressed();
                break;
            case R.id.btnAñadir:
                mList.add(new PiletaAcompañante(1, 1, 0f));
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.btnDia:
                tipo = 0;
                precioTotal = calcularPrecio(categoriaSelect);
                updatePrecio(precioTotal);
                updateButtons(v.getId());
                break;
            case R.id.btnSemana:
                tipo = 1;
                precioTotal = calcularPrecio(categoriaSelect);
                updatePrecio(precioTotal);
                updateButtons(v.getId());
                break;
            case R.id.btnMes:
                tipo = 2;
                precioTotal = calcularPrecio(categoriaSelect);
                updatePrecio(precioTotal);
                updateButtons(v.getId());
                break;
        }
    }

    public void updateButtons(int id) {
        switch (id) {
            case R.id.btnMes:
                btnMes.setBackgroundResource(R.drawable.button_border_select);
                btnDia.setBackgroundResource(R.drawable.button_border);
                btnSemana.setBackgroundResource(R.drawable.button_border);
                break;
            case R.id.btnDia:
                btnDia.setBackgroundResource(R.drawable.button_border_select);
                btnMes.setBackgroundResource(R.drawable.button_border);
                btnSemana.setBackgroundResource(R.drawable.button_border);
                break;
            case R.id.btnSemana:
                btnSemana.setBackgroundResource(R.drawable.button_border_select);
                btnDia.setBackgroundResource(R.drawable.button_border);
                btnMes.setBackgroundResource(R.drawable.button_border);
                break;
        }
        if (id != R.id.btnDia) {
            mLayout.setVisibility(View.GONE);
        } else
            mLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void actualizarPrecio() {
        updatePrecio(calcularPrecio(categoriaSelect));
    }
}
