package com.unse.gestiondepolideportivo;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unse.gestiondepolideportivo.Modelo.PiletaIngresoPorFechas;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.view.View.GONE;

public class DialogoConfirmacionEstadia extends DialogFragment implements View.OnClickListener {

    View view;
    EditText edtDNI;
    Button btnAceptar;
    TextView txtInfo;
    ProgressBar progress;
    ImageView imgIcon;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_confirmacion, container, false);
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
        update(0, "Ingrese un DNI para verificar disponibilidad");
        progress.setVisibility(GONE);
    }


    private void loadListener() {
        btnAceptar.setOnClickListener(this);

    }

    private void loadViews() {
        btnAceptar = view.findViewById(R.id.btnAceptar);
        edtDNI = view.findViewById(R.id.edtDNI);
        txtInfo = view.findViewById(R.id.txtInfo);
        imgIcon = view.findViewById(R.id.imgEstado);
        progress = view.findViewById(R.id.progres);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAceptar:
                buscar();
                break;
        }

    }

    private void buscar() {
        String dni = edtDNI.getText().toString();
        if (Utils.validarDNI(dni)) {
            progress.setVisibility(View.VISIBLE);
            btnAceptar.setVisibility(GONE);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("pileta").document(dni).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    try{
                        PiletaIngresoPorFechas pileta = documentSnapshot.toObject(PiletaIngresoPorFechas.class);
                        String fecha = pileta.getFechas();
                        Date date = new Date(System.currentTimeMillis());
                        Date fechaUser = Utils.getFechaDate(fecha);
                        Map<TimeUnit, Long> dif = Utils.computeDiff(fechaUser, date);
                        if (dif.get(TimeUnit.DAYS) > 0){
                            update(2, "Vigencia fuera de termino, venció el "+Utils.getFechaOnlyDay(fechaUser));
                        }else if(dif.get(TimeUnit.DAYS) < 0 || dif.get(TimeUnit.HOURS) >= 0 || dif.get(TimeUnit.MINUTES) >= 0){
                            update(1, "Vigencia hasta el "+Utils.getFechaOnlyDay(fechaUser));
                        }

                    }catch (Exception e){
                        Utils.showToast(getActivity(), "Error interno, ¡intente nuevamente!");
                        update(3, "El DNI ingresado no posee pagos registrados");
                    }
                    progress.setVisibility(GONE);
                    btnAceptar.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    update(3, "El DNI ingresado no posee pagos registrados");
                    progress.setVisibility(GONE);
                    btnAceptar.setVisibility(View.VISIBLE);
                }
            });
        } else {
            Utils.showToast(getActivity(), "Ingrese un documento válido");
        }
    }

    private void update(int i, String s) {
        switch (i) {
            case 0:
                Glide.with(imgIcon.getContext()).load(R.drawable.ic_search).into(imgIcon);
                break;
            case 1:
                Glide.with(imgIcon.getContext()).load(R.drawable.ic_check).into(imgIcon);
                break;
            case 2:
                Glide.with(imgIcon.getContext()).load(R.drawable.ic_not).into(imgIcon);
                break;
            case 3:
                Glide.with(imgIcon.getContext()).load(R.drawable.ic_info).into(imgIcon);
                break;
        }
        txtInfo.setText(s);
    }

}
