package com.unse.gestiondepolideportivo;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unse.gestiondepolideportivo.BaseDatos.PiletaRepo;
import com.unse.gestiondepolideportivo.Modelo.PiletaIngreso;
import com.unse.gestiondepolideportivo.Modelo.PiletaIngresoPorFechas;

public class DialogoIngresoPolideportivo extends DialogFragment  implements View.OnClickListener {

    View view;
    ImageButton btnMasMay, btnMasMen, btnMenosMay, btnMenosMen;
    EditText edtDNI;
    Button btnAceptar, btnDia, btnSemana, btnMes;
    TextView txtCantidadMay, txtCantidadMen, txtTotal;
    Spinner mSpinnerCategorias;
    String[] categorias = {"Seleccione una opción...", "Afiliado", "Docente", "Egresado", "Estudiante", "Jubilado", "Nodocente", "Particular"};

    int contadorMay = 0, contadorMeno = 0, categoriaSelect = 0, tipo = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_ingreso_poli, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
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
                if (!dni.equals("") && Utils.validarDNI(dni)) {
                    String dniEmpelado = new PreferenciasManager(getContext()).getValueString(Utils.DNI_TRABAJADOR);
                    String categoria = categorias[categoriaSelect];
                    String fecha = Utils.getFecha();
                    if (contadorMay >= 1 || contadorMeno >= 1) {
                        float precio = calcularPrecio(categoriaSelect);
                        float precioFinal = contadorMay * precio + contadorMeno * precio;
                        PiletaRepo piletaRepo = new PiletaRepo(getContext());
                        PiletaIngreso piletaIngreso = new PiletaIngreso(Integer.parseInt(dni), -1, categoriaSelect,
                                contadorMay, contadorMeno, fecha, precioFinal, Integer.parseInt(dniEmpelado));
                        piletaRepo.insert(piletaIngreso);
                        if (tipo != 0) {
                            PiletaIngresoPorFechas pileta = new PiletaIngresoPorFechas(dni, categoriaSelect, fecha, tipo);
                            sendData(pileta);
                        }
                        dismiss();
                    } else {
                        Utils.showToast(getActivity(), "¡Al menos debe haber un ingreso!");
                    }
                } else {
                    Utils.showToast(getActivity(), "¡Ingrese un DNI!");
                }
            }
        });
        mSpinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoriaSelect = position;
                InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                float precioTotal = calcularPrecio(categoriaSelect);
                String precio = Float.toString(precioTotal);
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
        btnDia.setOnClickListener(this);
        btnSemana.setOnClickListener(this);
        btnMes.setOnClickListener(this);

    }

    private void sendData(PiletaIngresoPorFechas pileta) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pileta").document(pileta.getDni()).set(pileta).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Utils.showToast(getActivity(), "¡Error al enviar datos!");
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Utils.showToast(getContext(), "¡Registro subido!");
                Toast.makeText(getActivity(), "¡Registro subido!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadViews() {
        btnAceptar = view.findViewById(R.id.btnAceptar);
        btnDia = view.findViewById(R.id.btnDia);
        btnSemana = view.findViewById(R.id.btnSemana);
        btnMes = view.findViewById(R.id.btnMes);
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

    private float calcularPrecio(int categ) {
        float precioTotal = 0;

        switch (categ){
            case 1:
                //Afiliados
                precioTotal = 0;
                break;
            case 4:
            case 5:
                //Estudiantes
                if(tipo == 0){
                    precioTotal = 50;
                }
                else
                    if(tipo == 1){
                        precioTotal = 250;
                    }
                    else{
                        precioTotal = 1000;
                    }
                break;

            case 7:
                //Particular
                precioTotal = 250;
                break;
            default:
                //Docentes, Nodocentes y Egresados
                if(tipo == 0){
                    precioTotal = 100;
                }
                else
                if(tipo == 1){
                    precioTotal = 500;
                }
                else{
                    precioTotal = 2000;
                }
                break;
        }
        return precioTotal;
    }

    @Override
    public void onClick(View v) {
        float precioTotal;
        String precio;
        switch (v.getId()){
            case R.id.btnAddMay:
                contadorMay++;
                updateCounter(contadorMay, contadorMeno);
                break;
            case R.id.btnRemoveMay:
                if (contadorMay <= 0) {
                    contadorMay = 0;
                } else {
                    contadorMay--;
                }
                updateCounter(contadorMay, contadorMeno);
                break;
            case R.id.btnAddMen:
                contadorMeno++;
                updateCounter(contadorMay, contadorMeno);
                break;
            case R.id.btnRemoveMen:
                if (contadorMeno <= 0) {
                    contadorMeno = 0;
                } else {
                    contadorMeno--;
                }
                updateCounter(contadorMay, contadorMeno);
                break;
            case R.id.btnDia:
                tipo = 0;
                precioTotal = calcularPrecio(categoriaSelect);
                precio = Float.toString(precioTotal);
                txtTotal.setText("$" + precio);
                break;
            case R.id.btnSemana:
                tipo = 1;
                precioTotal = calcularPrecio(categoriaSelect);
                precio = Float.toString(precioTotal);
                txtTotal.setText("$" + precio);
                break;
            case R.id.btnMes:
                tipo = 2;
                precioTotal = calcularPrecio(categoriaSelect);
                precio = Float.toString(precioTotal);
                txtTotal.setText("$" + precio);
                break;
        }

    }
}
