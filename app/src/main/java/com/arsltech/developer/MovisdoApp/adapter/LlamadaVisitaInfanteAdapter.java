package com.arsltech.developer.MovisdoApp.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.arsltech.developer.MovisdoApp.ItemAnimation;
import com.arsltech.developer.MovisdoApp.R;
import com.arsltech.developer.MovisdoApp.activity.GestanteActivity;
import com.arsltech.developer.MovisdoApp.activity.InfanteActivity;
import com.arsltech.developer.MovisdoApp.activity.LlamadaVisitaInfanteActivity;
import com.arsltech.developer.MovisdoApp.model.LlamadaVisitaInfanteModel;
import com.arsltech.developer.MovisdoApp.model.VisitaInfanteModel;

import java.util.ArrayList;

public class LlamadaVisitaInfanteAdapter extends RecyclerView.Adapter<LlamadaVisitaInfanteAdapter.LlamadaVisitaInfanteViewHolder> {

    Context context;
    Cursor cursor;
    ArrayList<LlamadaVisitaInfanteModel> llamadaVisitaInfanteModelArrayList;
    ArrayList<LlamadaVisitaInfanteModel> filteredLlamadaVisitaInfanteArrayList;
    public LlamadaVisitaInfanteAdapter(Context context, ArrayList<LlamadaVisitaInfanteModel> llamadaVisitaInfanteModelArrayList) {
        this.context = context;
        this.llamadaVisitaInfanteModelArrayList = llamadaVisitaInfanteModelArrayList;
        this.filteredLlamadaVisitaInfanteArrayList=llamadaVisitaInfanteModelArrayList;

    }

    public static final class LlamadaVisitaInfanteViewHolder extends RecyclerView.ViewHolder{
        TextView tvInfante;
        TextView tvDatosLlamada;
        TextView tvDuracion;
        TextView tvTipoLlamada;

        public LlamadaVisitaInfanteViewHolder(View itemView) {
            super(itemView);
            tvInfante=itemView.findViewById(R.id.textPersonData);
            tvDatosLlamada=itemView.findViewById(R.id.textDatosLlamada);
            tvDuracion=itemView.findViewById(R.id.textDuracion);
            tvTipoLlamada=itemView.findViewById(R.id.textTipoLlamada);
        }
    }

    @Override
    public LlamadaVisitaInfanteAdapter.LlamadaVisitaInfanteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.llamada_layout,parent,false);
        return new LlamadaVisitaInfanteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LlamadaVisitaInfanteAdapter.LlamadaVisitaInfanteViewHolder holder, int position) {
        holder.tvInfante.setText(filteredLlamadaVisitaInfanteArrayList.get(position).getInfanteDisplayName());
        holder.tvDatosLlamada.setText(filteredLlamadaVisitaInfanteArrayList.get(position).getDatos_llamada());
        holder.tvDuracion.setText(filteredLlamadaVisitaInfanteArrayList.get(position).getDuracion());
        holder.tvTipoLlamada.setText(filteredLlamadaVisitaInfanteArrayList.get(position).getTipoLlamada());

        ItemAnimation.animateLeftRight(holder.itemView, position);


    }

    @Override
    public int getItemCount() {
        return filteredLlamadaVisitaInfanteArrayList.size();
    }

    //Filtro de recycler por categoria
    /*
    public Filter getFilterCategoria(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String key=charSequence.toString();
                if(key.isEmpty()){
                    filteredLlamadaVisitaInfanteArrayList=llamadaVisitaInfanteModelArrayList;
                }
                else {
                    ArrayList<LlamadaVisitaInfanteModel> filteredResult=new ArrayList<>();
                    for (LlamadaVisitaInfanteModel row: llamadaVisitaInfanteModelArrayList){
                        if(row.getCategoria().toLowerCase().contains(key.toLowerCase())){
                            filteredResult.add(row);
                        }
                    }
                    filteredLlamadaVisitaInfanteArrayList=filteredResult;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=filteredLlamadaVisitaInfanteArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredLlamadaVisitaInfanteArrayList=(ArrayList<LlamadaVisitaInfanteModel>)filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

     */

    public ArrayList<LlamadaVisitaInfanteModel> ConvertCursorToArray(Cursor data, String infanteDisplayName) {
        cursor = data;
        String datos_llamada;
        String duracion;
        String id_visita_infante;
        String id_llamada_visita_infante_remoto;

        ArrayList<LlamadaVisitaInfanteModel> arrayResult = new ArrayList<LlamadaVisitaInfanteModel>();

        for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {

            id_llamada_visita_infante_remoto = data.getString(4);
            datos_llamada = data.getString(1);
            duracion = data.getString(2);
            id_visita_infante = data.getString(3);

            LlamadaVisitaInfanteModel llamadaVisitaInfanteModel;
            llamadaVisitaInfanteModel=new LlamadaVisitaInfanteModel(id_llamada_visita_infante_remoto,datos_llamada,duracion,id_visita_infante
                    ,infanteDisplayName,"Saliente");

            arrayResult.add(llamadaVisitaInfanteModel);
        }
        ActualizarConstructor(arrayResult);
        return arrayResult;

    }

    private void ActualizarConstructor(ArrayList<LlamadaVisitaInfanteModel> data){
        llamadaVisitaInfanteModelArrayList=data;
        filteredLlamadaVisitaInfanteArrayList=data;
        notifyDataSetChanged();
    }


}