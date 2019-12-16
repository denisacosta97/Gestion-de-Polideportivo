package com.unse.gestiondepolideportivo.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unse.gestiondepolideportivo.Modelo.ItemDato;
import com.unse.gestiondepolideportivo.Modelo.ItemFecha;
import com.unse.gestiondepolideportivo.Modelo.ItemListado;
import com.unse.gestiondepolideportivo.R;
import com.unse.gestiondepolideportivo.Herramientas.Utils;

import java.util.Date;
import java.util.List;

public class ListadoIngresosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ItemListado> lista;

    public ListadoIngresosAdapter(Context context, List<ItemListado> lista) {
        this.lista = lista;
        this.mContext = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case ItemListado.TIPO_DATO:
                View view = inflater.inflate(R.layout.item_dato, viewGroup, false);
                viewHolder = new DateViewHolder(view);
                break;
            case ItemListado.TIPO_FECHA:
                View view2 = inflater.inflate(R.layout.item_fecha, viewGroup, false);
                viewHolder = new FechaViewHolder(view2);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {

            case ItemListado.TIPO_FECHA:
                ItemFecha itemFecha = (ItemFecha) lista.get(position);
                FechaViewHolder fechaViewHolder= (FechaViewHolder) viewHolder;
                Date date = Utils.getFechaDate(itemFecha.getFecha());
                fechaViewHolder.txtFecha.setText(String.format("%s, %s de %s", Utils.getDayWeek(date), Utils.getDay(date),
                        Utils.getMes(date)));
                break;

            case ItemListado.TIPO_DATO:
                ItemDato dateItem = (ItemDato) lista.get(position);
                DateViewHolder dateViewHolder = (DateViewHolder) viewHolder;
                dateViewHolder.txtDNI.setText(String.valueOf(dateItem.getPiletaIngreso().getDni()));
                dateViewHolder.txtCantidadMay.setText(String.valueOf(dateItem.getPiletaIngreso().getCantidadTotal()));
                dateViewHolder.txtCategoria.setText(getCategoria(dateItem.getPiletaIngreso().getCategoria()-1));
                dateViewHolder.txtPrecio.setText(String.valueOf(dateItem.getPiletaIngreso().getPrecio1()));
                break;
        }
    }

    public String getCategoria(int i){
        String[] categorias = {"Afiliado", "Docente", "Egresado", "Estudiante", "Jubilado", "Nodocente", "Particular"};
       return categorias[i];
    }


    //ViewHolder for date row item
    class DateViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDNI, txtCantidadMay, txtCantMenor, txtPrecio, txtCategoria;

         DateViewHolder(View v) {
            super(v);
            txtDNI = v.findViewById(R.id.txtDNI);
            txtCantidadMay = v.findViewById(R.id.txtMayor);
            txtCantMenor = v.findViewById(R.id.txtMenor);
            txtCategoria = v.findViewById(R.id.txtCategoria);
            txtPrecio = v.findViewById(R.id.txtPrecio);
        }
    }

    //View holder for general row item
    class FechaViewHolder extends RecyclerView.ViewHolder {
        private TextView txtFecha;

        FechaViewHolder(View v) {
            super(v);
            txtFecha = v.findViewById(R.id.txtFecha);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return lista.get(position).getTipo();
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }
}
