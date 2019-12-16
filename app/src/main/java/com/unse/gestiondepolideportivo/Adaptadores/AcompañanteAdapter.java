package com.unse.gestiondepolideportivo.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.unse.gestiondepolideportivo.Herramientas.ObservadorPrecio;
import com.unse.gestiondepolideportivo.Actividades.PiletaAcompañante;
import com.unse.gestiondepolideportivo.R;

import java.util.ArrayList;

public class AcompañanteAdapter extends RecyclerView.Adapter<AcompañanteAdapter.AcompañanteViewHolder> {

    ArrayList<PiletaAcompañante> mList;
    Context mContext;
    ObservadorPrecio mObservadorPrecio;

    public AcompañanteAdapter(ArrayList<PiletaAcompañante> list, Context context, ObservadorPrecio precio) {
        mList = list;
        mContext = context;
        mObservadorPrecio = precio;
    }

    @NonNull
    @Override
    public AcompañanteAdapter.AcompañanteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ingreso_poli, viewGroup, false);

        return new AcompañanteViewHolder(view, mObservadorPrecio);
    }

    @Override
    public void onBindViewHolder(@NonNull AcompañanteAdapter.AcompañanteViewHolder holder, int position) {

        PiletaAcompañante acompanante = mList.get(position);

        holder.txtPrecio.setText(String.format("$ %s", acompanante.getPrecioTotal()));
        holder.txtCantidad.setText(String.valueOf(acompanante.getCantidad()));
        holder.mSpinner.setSelection(acompanante.getCategoria());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class AcompañanteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        String[] categorias = {"Seleccione una opción...", "Afiliado", "Docente", "Egresado", "Estudiante", "Jubilado", "Nodocente", "Particular"};


        ImageButton btnMas, btnMenos;
        Spinner mSpinner;
        TextView txtPrecio, txtCantidad;
        ObservadorPrecio mObservadorPrecio;

        AcompañanteViewHolder(View view, ObservadorPrecio observadorPrecio) {
            super(view);
            btnMas = view.findViewById(R.id.btnAddMay);
            btnMenos = view.findViewById(R.id.btnRemoveMay);
            txtPrecio = view.findViewById(R.id.txtPrecio);
            mSpinner = view.findViewById(R.id.spineer);
            txtCantidad = view.findViewById(R.id.txtCantidad);
            this.mObservadorPrecio = observadorPrecio;

            btnMenos.setOnClickListener(this);
            btnMas.setOnClickListener(this);
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, categorias);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(dataAdapter2);

            mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mList.get(getAdapterPosition()).setCategoria(position);
                    update();
                    notifyDataSetChanged();
                    mObservadorPrecio.actualizarPrecio();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAddMay:
                    mList.get(getAdapterPosition()).setCantidad(mList.get(
                            getAdapterPosition()).getCantidad() + 1);

                    break;
                case R.id.btnRemoveMay:
                    if (mList.get(getAdapterPosition()).getCantidad() <= 0) {
                        mList.get(getAdapterPosition()).setCantidad(0);
                    } else {
                        mList.get(getAdapterPosition()).setCantidad(mList.get(
                                getAdapterPosition()).getCantidad() - 1);
                    }

                    break;
            }
            update();
            notifyDataSetChanged();
            mObservadorPrecio.actualizarPrecio();
        }

        private void update() {
            float precio = calcularPrecio(mList.get(getAdapterPosition()).getCategoria(), 0);
            mList.get(getAdapterPosition()).setPrecioTotal(precio * mList.get(getAdapterPosition())
                    .getCantidad());
        }
    }

    private float calcularPrecio(int categ, int tipo) {
        float precioTotal = 0;

        switch (categ) {
            case 1:
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
}
