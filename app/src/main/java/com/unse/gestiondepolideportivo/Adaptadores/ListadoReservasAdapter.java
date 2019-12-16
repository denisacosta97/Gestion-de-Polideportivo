package com.unse.gestiondepolideportivo.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unse.gestiondepolideportivo.Modelo.ItemFechaReserva;
import com.unse.gestiondepolideportivo.Modelo.ItemDatoReserva;
import com.unse.gestiondepolideportivo.Modelo.ItemReserva;
import com.unse.gestiondepolideportivo.R;
import com.unse.gestiondepolideportivo.Herramientas.Utils;

import java.util.Date;
import java.util.List;

public class ListadoReservasAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ItemReserva> lista;

    public ListadoReservasAdapter(Context context, List<ItemReserva> lista) {
        this.lista = lista;
        this.mContext = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case ItemReserva.TIPO_DATO:
                View view = inflater.inflate(R.layout.item_dato_reserva, viewGroup, false);
                viewHolder = new DateViewHolder(view);
                break;
            case ItemReserva.TIPO_FECHA:
                View view2 = inflater.inflate(R.layout.item_fecha, viewGroup, false);
                viewHolder = new FechaViewHolder(view2);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {

            case ItemReserva.TIPO_FECHA:
                ItemFechaReserva itemFecha = (ItemFechaReserva) lista.get(position);
                FechaViewHolder fechaViewHolder = (FechaViewHolder) viewHolder;
                Date date = Utils.getFechaDate(itemFecha.getFecha());
                fechaViewHolder.txtFecha.setText(String.format("%s, %s de %s", Utils.getDayWeek(date), Utils.getDay(date),
                        Utils.getMes(date)));
                break;

            case ItemReserva.TIPO_DATO:
                ItemDatoReserva dateItem = (ItemDatoReserva) lista.get(position);
                DateViewHolder dateViewHolder = (DateViewHolder) viewHolder;
                dateViewHolder.txtDNI.setText(String.valueOf(dateItem.getReserva().getDni()));
                dateViewHolder.txtCategoria.setText(getCategoria(dateItem.getReserva().getCategoria()-1));
                dateViewHolder.txtInstalaciones.setText(getInstalaciones(dateItem.getReserva().getInstalacion()-1));
                dateViewHolder.txtPrecio.setText(String.valueOf(dateItem.getReserva().getPrecio()));
                break;
        }


    }

    private String getCategoriaName(String cat) {
        return cat;
    }

    public String getCategoria(int i){
        String[] categorias = {"Afiliado", "Docente", "Egresado", "Estudiante", "Jubilado", "Nodocente", "Particular"};
        return categorias[i];
    }

    public String getInstalaciones(int i){
        String[] instalaciones = {"Quincho Gris","Quincho Marrón","SUM"};
        return instalaciones[i];
    }

    //ViewHolder for date row item
    class DateViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDNI, txtInstalaciones, txtCategoria, txtPrecio;

        DateViewHolder(View v) {
            super(v);
            txtDNI = v.findViewById(R.id.txtDNI);
            txtCategoria = v.findViewById(R.id.txtCategoria2);
            txtInstalaciones = v.findViewById(R.id.txtInstalaciones2);
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