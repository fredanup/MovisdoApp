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
import com.arsltech.developer.MovisdoApp.activity.VisitaInfanteActivity;
import com.arsltech.developer.MovisdoApp.model.InfanteModel;


import java.util.ArrayList;

public class InfanteAdapter extends RecyclerView.Adapter<InfanteAdapter.InfanteViewHolder> {

    Context context;
    Cursor cursor;
    ArrayList<InfanteModel> infanteModelArrayList;
    ArrayList<InfanteModel> filteredInfanteArrayList;
    public InfanteAdapter(Context context, ArrayList<InfanteModel> infanteModelArrayList) {

        this.context = context;
        this.infanteModelArrayList = infanteModelArrayList;
        this.filteredInfanteArrayList=infanteModelArrayList;

    }

    public static final class InfanteViewHolder extends RecyclerView.ViewHolder{
        TextView tvInfante;
        TextView tvDni;
        TextView tvSexo;
        TextView tvCategoria;

        public InfanteViewHolder(View itemView) {
            super(itemView);
            tvInfante=itemView.findViewById(R.id.textInfante);
            tvDni=itemView.findViewById(R.id.textDni);
            tvSexo=itemView.findViewById(R.id.textSexo);
            tvCategoria=itemView.findViewById(R.id.textCategoria);
        }
    }

    @Override
    public InfanteAdapter.InfanteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.infante_layout,parent,false);
        return new InfanteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InfanteAdapter.InfanteViewHolder holder, int position) {
        holder.tvInfante.setText(filteredInfanteArrayList.get(position).getNombre()+" "+filteredInfanteArrayList.get(position).getApPaterno()
        +" "+filteredInfanteArrayList.get(position).getApMaterno());
        holder.tvDni.setText(filteredInfanteArrayList.get(position).getDni());
        holder.tvSexo.setText(filteredInfanteArrayList.get(position).getSexo());
        holder.tvCategoria.setText(filteredInfanteArrayList.get(position).getCategoria());

        ItemAnimation.animateLeftRight(holder.itemView, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, VisitaInfanteActivity.class);
                intent.putExtra("id_infante",filteredInfanteArrayList.get(position).getId());
                intent.putExtra("infanteDisplayName",filteredInfanteArrayList.get(position).getNombre()+" "+
                        filteredInfanteArrayList.get(position).getApPaterno()+" "+filteredInfanteArrayList.get(position).getApMaterno());
                context.startActivity(intent);
            }
        });

    }

    public ArrayList<InfanteModel> ConvertCursorToArray(Cursor data) {
        cursor = data;
        String nombre;
        String apPaterno;
        String apMaterno;
        String dni;
        String fecha_nacimiento;
        String sexo;
        String estab_salud;
        String prematuro;
        String categoria;
        String id_encuestado;
        String id_infante_remoto;

        ArrayList<InfanteModel> arrayResult = new ArrayList<InfanteModel>();

        for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {

            id_infante_remoto = data.getString(11);
            nombre = data.getString(1);
            apPaterno = data.getString(2);
            apMaterno = data.getString(3);
            dni = data.getString(4);
            fecha_nacimiento = data.getString(5);
            sexo = data.getString(6);
            estab_salud = data.getString(7);
            prematuro = data.getString(8);
            categoria = data.getString(9);
            id_encuestado = data.getString(10);
            InfanteModel infanteModel;
            infanteModel=new InfanteModel(id_infante_remoto,nombre,apPaterno,apMaterno,
                    dni,fecha_nacimiento,sexo,estab_salud,prematuro,categoria,id_encuestado);

            arrayResult.add(infanteModel);
        }
        ActualizarConstructor(arrayResult);
        return arrayResult;

    }

    private void ActualizarConstructor(ArrayList<InfanteModel> data){
        infanteModelArrayList=data;
        filteredInfanteArrayList=data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filteredInfanteArrayList.size();
    }

    //Filtro de recycler por categoria
    public Filter getFilterCategoria(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String key=charSequence.toString();
                if(key.isEmpty()){
                    filteredInfanteArrayList=infanteModelArrayList;
                }
                else {
                    ArrayList<InfanteModel> filteredResult=new ArrayList<>();
                    for (InfanteModel row: infanteModelArrayList){
                        if(row.getCategoria().toLowerCase().contains(key.toLowerCase())){
                            filteredResult.add(row);
                        }
                    }
                    filteredInfanteArrayList=filteredResult;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=filteredInfanteArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredInfanteArrayList=(ArrayList<InfanteModel>)filterResults.values;
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
                    filteredInfanteArrayList=infanteModelArrayList;
                }
                else {
                    ArrayList<InfanteModel> filteredResult=new ArrayList<>();
                    for (InfanteModel row: infanteModelArrayList){
                        if(row.getNombre().toLowerCase().contains(key.toLowerCase()) ||
                                row.getApPaterno().toLowerCase().contains(key.toLowerCase()) ||
                                row.getApMaterno().toLowerCase().contains(key.toLowerCase())){
                            filteredResult.add(row);
                        }
                    }
                    filteredInfanteArrayList=filteredResult;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=filteredInfanteArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredInfanteArrayList=(ArrayList<InfanteModel>)filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

}
