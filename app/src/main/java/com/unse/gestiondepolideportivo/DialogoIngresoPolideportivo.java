package com.unse.gestiondepolideportivo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.unse.gestiondepolideportivo.BaseDatos.PiletaRepo;
import com.unse.gestiondepolideportivo.Modelo.PiletaIngreso;

public class DialogoIngresoPolideportivo extends DialogFragment  implements View.OnClickListener {

    View view;
    ImageButton btnMasMay, btnMasMen, btnMenosMay, btnMenosMen;
    EditText edtDNI;
    Button btnAceptar;
    TextView txtCantidadMay, txtCantidadMen, txtTotal;
    Spinner mSpinnerCategorias;
    String[] categorias = {"Estudiante","Docente","No Docente","Afiliado","Particular"};

    int contadorMay = 0, contadorMeno = 0, categoriaSelect = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_ingreso_poli, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Esto es lo nuevoooooooo, evita los bordes cuadrados
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        loadViews();

        loadData();

        loadListener();

        return view;
    }

    private void loadData() {
        contadorMeno = contadorMay = 0;
        updateCounter(contadorMay, contadorMeno);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categorias);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCategorias.setAdapter(dataAdapter2);
    }

    private void updateCounter(int may, int men) {
        txtCantidadMay.setText(String.valueOf(may));
        txtCantidadMen.setText(String.valueOf(men));

    }

    private void loadListener() {
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dni = edtDNI.getText().toString().trim();
                if(!dni.equals("") && Utils.validarDNI(dni)){
                    String categoria = categorias[categoriaSelect];
                    String fecha = Utils.getFecha();
                    if (contadorMay >= 1 || contadorMeno >= 1){
                        PiletaRepo piletaRepo = new PiletaRepo(getContext());
                        PiletaIngreso piletaIngreso = new PiletaIngreso(Integer.parseInt(dni),-1, categoriaSelect+1,
                                contadorMay, contadorMeno,fecha, 20f, 20f, 40657677);
                        piletaRepo.insert(piletaIngreso);
                        dismiss();
                    }else{
                        Utils.showToast(getContext(), "¡Al menos debe haber un ingreso!");
                    }
                }else{
                    Utils.showToast(getContext(),"¡Ingrese un DNI!");
                }
            }
        });
        mSpinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoriaSelect = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categoriaSelect = 0;
            }
        });
        btnMasMay.setOnClickListener(this);
        btnMasMen.setOnClickListener(this);
        btnMenosMay.setOnClickListener(this);
        btnMenosMen.setOnClickListener(this);
    }

    private void loadViews() {
        btnAceptar = view.findViewById(R.id.btnAceptar);
        edtDNI = view.findViewById(R.id.edtDNI);
        txtCantidadMay = view.findViewById(R.id.txtCantidadMay);
        txtCantidadMen = view.findViewById(R.id.txtCantidadMen);
        btnMasMay = view.findViewById(R.id.btnAddMay);
        btnMenosMay = view.findViewById(R.id.btnRemoveMay);
        btnMasMen = view.findViewById(R.id.btnAddMen);
        btnMenosMen = view.findViewById(R.id.btnRemoveMen);
        txtTotal = view.findViewById(R.id.txtTotal);
        mSpinnerCategorias = view.findViewById(R.id.spineer);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddMay:
                contadorMay++;
                updateCounter(contadorMay, contadorMeno);
                break;
            case R.id.btnRemoveMay:
                if (contadorMay <= 0){
                    contadorMay = 0;
                }else{
                    contadorMay--;
                }
                updateCounter(contadorMay, contadorMeno);
                break;
            case R.id.btnAddMen:
                contadorMeno++;
                updateCounter(contadorMay, contadorMeno);
                break;
            case R.id.btnRemoveMen:
                if (contadorMeno <= 0){
                    contadorMeno = 0;
                }else{
                    contadorMeno--;
                }
                updateCounter(contadorMay, contadorMeno);
                break;
        }

    }
}
