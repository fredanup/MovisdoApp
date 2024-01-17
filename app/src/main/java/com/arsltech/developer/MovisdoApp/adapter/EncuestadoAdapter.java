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
import com.arsltech.developer.MovisdoApp.model.EncuestadoModel;

import java.util.ArrayList;

public class EncuestadoAdapter extends RecyclerView.Adapter<EncuestadoAdapter.EncuestadoViewHolder> {

    Context context;
    Cursor cursor;
    ArrayList<EncuestadoModel> encuestadoModelArrayList;
    ArrayList<EncuestadoModel> filteredEncuestadoArrayList;


    public EncuestadoAdapter(Context context, ArrayList<EncuestadoModel> encuestadoModelArrayList) {

        this.context = context;
        this.encuestadoModelArrayList = encuestadoModelArrayList;
        this.filteredEncuestadoArrayList=encuestadoModelArrayList;

    }

    public static final class EncuestadoViewHolder extends RecyclerView.ViewHolder{
        TextView tvEncuestado;
        TextView tvCelular;
        TextView tvDireccion;
        TextView tvCategoria;

        public EncuestadoViewHolder(View itemView) {
            super(itemView);
            tvEncuestado=itemView.findViewById(R.id.textEncuestado);
            tvCelular=itemView.findViewById(R.id.textCelular);
            tvDireccion=itemView.findViewById(R.id.textDireccion);
            tvCategoria=itemView.findViewById(R.id.textCategoria);

        }
    }

    @Override
    public EncuestadoAdapter.EncuestadoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.encuestado_layout,parent,false);
        return new EncuestadoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(EncuestadoAdapter.EncuestadoViewHolder holder, int position) {


        holder.tvEncuestado.setText(filteredEncuestadoArrayList.get(position).getNombre()+" "+filteredEncuestadoArrayList.get(position).getApPaterno()
        +" "+filteredEncuestadoArrayList.get(position).getApMaterno());
        holder.tvCelular.setText(filteredEncuestadoArrayList.get(position).getCelular());
        holder.tvDireccion.setText(filteredEncuestadoArrayList.get(position).getDireccion());
        holder.tvCategoria.setText(filteredEncuestadoArrayList.get(position).getCategoria());

        ItemAnimation.animateLeftRight(holder.itemView, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(filteredEncuestadoArrayList.get(position).getCategoria().contains("Apoderado")){
                    Intent intent=new Intent(context, InfanteActivity.class);
                    intent.putExtra("id_encuestado",filteredEncuestadoArrayList.get(position).getId());
                    intent.putExtra("id_promotor",filteredEncuestadoArrayList.get(position).getId_promotor());
                    context.startActivity(intent);
                }
                if(filteredEncuestadoArrayList.get(position).getCategoria().contains("Gestante")){
                    Intent intent=new Intent(context, GestanteActivity.class);
                    intent.putExtra("id_encuestado",filteredEncuestadoArrayList.get(position).getId());
                    intent.putExtra("encuestadoDisplayName",filteredEncuestadoArrayList.get(position).getNombre()+" "+
                            filteredEncuestadoArrayList.get(position).getApPaterno()+" "+filteredEncuestadoArrayList.get(position).getApMaterno());

                    context.startActivity(intent);
                }


            }
        });

    }

    public ArrayList<EncuestadoModel> ConvertCursorToArray(Cursor data) {
        cursor = data;
        String nombre;
        String apPaterno;
        String apMaterno;
        String dni;
        String fecha_nacimiento;
        String celular;
        String direccion;
        String ref_vivienda;
        String categoria;
        String id_promotor;
        String id_encuestado_remoto;

        ArrayList<EncuestadoModel> arrayResult = new ArrayList<EncuestadoModel>();

        for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {

            id_encuestado_remoto = data.getString(11);
            nombre = data.getString(1);
            apPaterno = data.getString(2);
            apMaterno = data.getString(3);
            dni = data.getString(4);
            fecha_nacimiento = data.getString(5);
            celular = data.getString(6);
            direccion = data.getString(7);
            ref_vivienda = data.getString(8);
            categoria = data.getString(9);
            id_promotor = data.getString(10);
            EncuestadoModel encuestadoModel;
            encuestadoModel=new EncuestadoModel(id_encuestado_remoto,nombre,apPaterno,apMaterno,
                    dni,fecha_nacimiento,celular,direccion,ref_vivienda,categoria,id_promotor);

            arrayResult.add(encuestadoModel);
        }
         ActualizarConstructor(arrayResult);
        return arrayResult;

    }

    private void ActualizarConstructor(ArrayList<EncuestadoModel> data){
        encuestadoModelArrayList=data;
        filteredEncuestadoArrayList=data;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public int getItemCount() {

        return filteredEncuestadoArrayList.size();

    }

    //Filtro de recycler por categoria
    public Filter getFilterCategoria(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String key=charSequence.toString();
                if(key.isEmpty()){

                    filteredEncuestadoArrayList=encuestadoModelArrayList;

                }
                else {
                    ArrayList<EncuestadoModel> filteredResult=new ArrayList<>();
                    for (EncuestadoModel row: encuestadoModelArrayList){
                        if(row.getCategoria().toLowerCase().contains(key.toLowerCase())){
                            filteredResult.add(row);
                        }
                    }
                    filteredEncuestadoArrayList=filteredResult;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=filteredEncuestadoArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredEncuestadoArrayList=(ArrayList<EncuestadoModel>)filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    //Filtro de recycler por nombre
    public Filter getFilterNombre(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String key=charSequence.toString();
                if(key.isEmpty()){
                    filteredEncuestadoArrayList=encuestadoModelArrayList;

                }
                else {
                    ArrayList<EncuestadoModel> filteredResult=new ArrayList<>();
                    for (EncuestadoModel row: encuestadoModelArrayList){
                        if(row.getNombre().toLowerCase().contains(key.toLowerCase()) ||
                                row.getApPaterno().toLowerCase().contains(key.toLowerCase()) ||
                                row.getApMaterno().toLowerCase().contains(key.toLowerCase())){
                            filteredResult.add(row);
                        }
                    }
                    filteredEncuestadoArrayList=filteredResult;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=filteredEncuestadoArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredEncuestadoArrayList=(ArrayList<EncuestadoModel>)filterResults.values;
                notifyDataSetChanged();

            }
        };
    }


}
