package com.arsltech.developer.MovisdoApp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.arsltech.developer.MovisdoApp.ItemAnimation;
import com.arsltech.developer.MovisdoApp.R;
import com.arsltech.developer.MovisdoApp.model.LlamadaVisitaGestanteModel;
import com.arsltech.developer.MovisdoApp.model.LlamadaVisitaInfanteModel;


import java.util.ArrayList;

public class LlamadaVisitaGestanteAdapter extends RecyclerView.Adapter<LlamadaVisitaGestanteAdapter.LlamadaVisitaGestanteViewHolder> {

    Context context;
    Cursor cursor;
    ArrayList<LlamadaVisitaGestanteModel> llamadaVisitaGestanteModelArrayList;
    ArrayList<LlamadaVisitaGestanteModel> filteredLlamadaVisitaGestanteArrayList;
    public LlamadaVisitaGestanteAdapter(Context context, ArrayList<LlamadaVisitaGestanteModel> llamadaVisitaGestanteModelArrayList) {
        this.context = context;
        this.llamadaVisitaGestanteModelArrayList = llamadaVisitaGestanteModelArrayList;
        this.filteredLlamadaVisitaGestanteArrayList=llamadaVisitaGestanteModelArrayList;

    }

    public static final class LlamadaVisitaGestanteViewHolder extends RecyclerView.ViewHolder{
        TextView tvGestante;
        TextView tvDatosLlamada;
        TextView tvDuracion;
        TextView tvTipoLlamada;

        public LlamadaVisitaGestanteViewHolder(View itemView) {
            super(itemView);
            tvGestante=itemView.findViewById(R.id.textPersonData);
            tvDatosLlamada=itemView.findViewById(R.id.textDatosLlamada);
            tvDuracion=itemView.findViewById(R.id.textDuracion);
            tvTipoLlamada=itemView.findViewById(R.id.textTipoLlamada);
        }
    }

    @Override
    public LlamadaVisitaGestanteAdapter.LlamadaVisitaGestanteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.llamada_layout,parent,false);
        return new LlamadaVisitaGestanteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LlamadaVisitaGestanteAdapter.LlamadaVisitaGestanteViewHolder holder, int position) {
        holder.tvGestante.setText(filteredLlamadaVisitaGestanteArrayList.get(position).getGestanteDisplayName());
        holder.tvDatosLlamada.setText(filteredLlamadaVisitaGestanteArrayList.get(position).getDatos_llamada());
        holder.tvDuracion.setText(filteredLlamadaVisitaGestanteArrayList.get(position).getDuracion());
        holder.tvTipoLlamada.setText(filteredLlamadaVisitaGestanteArrayList.get(position).getTipoLlamada());

        ItemAnimation.animateLeftRight(holder.itemView, position);


    }

    @Override
    public int getItemCount() {
        return filteredLlamadaVisitaGestanteArrayList.size();
    }

    public ArrayList<LlamadaVisitaGestanteModel> ConvertCursorToArray(Cursor data, String gestanteDisplayName) {
        cursor = data;
        String datos_llamada;
        String duracion;
        String id_visita_gestante;
        String id_llamada_visita_gestante_remoto;

        ArrayList<LlamadaVisitaGestanteModel> arrayResult = new ArrayList<LlamadaVisitaGestanteModel>();

        for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {

            id_llamada_visita_gestante_remoto = data.getString(4);
            datos_llamada = data.getString(1);
            duracion = data.getString(2);
            id_visita_gestante = data.getString(3);

            LlamadaVisitaGestanteModel llamadaVisitaGestanteModel;
            llamadaVisitaGestanteModel=new LlamadaVisitaGestanteModel(id_llamada_visita_gestante_remoto,datos_llamada,duracion,id_visita_gestante
                    ,gestanteDisplayName,"Saliente");

            arrayResult.add(llamadaVisitaGestanteModel);
        }
        ActualizarConstructor(arrayResult);
        return arrayResult;

    }

    private void ActualizarConstructor(ArrayList<LlamadaVisitaGestanteModel> data){
        llamadaVisitaGestanteModelArrayList=data;
        filteredLlamadaVisitaGestanteArrayList=data;
        notifyDataSetChanged();
    }

    //Filtro de recycler por categoria
    /*
    public Filter getFilterCategoria(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String key=charSequence.toString();
                if(key.isEmpty()){
                    filteredLlamadaVisitagestanteArrayList=llamadaVisitagestanteModelArrayList;
                }
                else {
                    ArrayList<LlamadaVisitagestanteModel> filteredResult=new ArrayList<>();
                    for (LlamadaVisitagestanteModel row: llamadaVisitagestanteModelArrayList){
                        if(row.getCategoria().toLowerCase().contains(key.toLowerCase())){
                            filteredResult.add(row);
                        }
                    }
                    filteredLlamadaVisitagestanteArrayList=filteredResult;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=filteredLlamadaVisitagestanteArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredLlamadaVisitagestanteArrayList=(ArrayList<LlamadaVisitagestanteModel>)filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

     */



}