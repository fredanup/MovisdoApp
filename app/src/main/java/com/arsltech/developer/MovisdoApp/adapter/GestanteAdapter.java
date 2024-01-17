package com.arsltech.developer.MovisdoApp.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.arsltech.developer.MovisdoApp.ItemAnimation;
import com.arsltech.developer.MovisdoApp.R;
import com.arsltech.developer.MovisdoApp.activity.VisitaGestanteActivity;
import com.arsltech.developer.MovisdoApp.model.GestanteModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GestanteAdapter extends RecyclerView.Adapter<GestanteAdapter.GestanteViewHolder> {

    Context context;
    Cursor cursor;
    ArrayList<GestanteModel> gestanteModelArrayList;
    ArrayList<GestanteModel> filteredGestanteArrayList;
    public GestanteAdapter(Context context, ArrayList<GestanteModel> gestanteModelArrayList) {
        this.context = context;
        this.gestanteModelArrayList = gestanteModelArrayList;
        this.filteredGestanteArrayList=gestanteModelArrayList;

    }


    public static final class GestanteViewHolder extends RecyclerView.ViewHolder{
        TextView tvGestante;
        TextView tvEstabSalud;
        TextView tvSexo;
        TextView tvFechaParto;

        public GestanteViewHolder(View itemView) {
            super(itemView);
            tvGestante=itemView.findViewById(R.id.textGestante);
            tvEstabSalud=itemView.findViewById(R.id.textEstabSalud);
            tvSexo=itemView.findViewById(R.id.textSexo);
            tvFechaParto=itemView.findViewById(R.id.textFechaParto);
        }
    }

    @Override
    public GestanteAdapter.GestanteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.gestante_layout,parent,false);
        return new GestanteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GestanteAdapter.GestanteViewHolder holder, int position) {
        holder.tvGestante.setText(filteredGestanteArrayList.get(position).getEncuestadoDisplayName());
        holder.tvEstabSalud.setText(filteredGestanteArrayList.get(position).getEstab_salud());
        holder.tvSexo.setText(filteredGestanteArrayList.get(position).getSexo_bebe());

        try {
            Date dateToFormat=new SimpleDateFormat("yyyy-MM-dd").parse(filteredGestanteArrayList.get(position).getFecha_parto());
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String dateFormated = dateFormat.format(dateToFormat);
            holder.tvFechaParto.setText(dateFormated);

        } catch (ParseException e) {
            Toast.makeText(context,"Formato de fecha inválido",Toast.LENGTH_SHORT).show();
        }

        ItemAnimation.animateLeftRight(holder.itemView, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, VisitaGestanteActivity.class);
                intent.putExtra("id_gestante",filteredGestanteArrayList.get(position).getId());
                intent.putExtra("encuestadoDisplayName",filteredGestanteArrayList.get(position).getEncuestadoDisplayName());
                context.startActivity(intent);
            }
        });

    }

    public ArrayList<GestanteModel> ConvertCursorToArray(Cursor data, String encuestadoDisplayName) {
        cursor = data;
        String fecha_parto;
        String estab_salud;
        String sexo_bebe;
        String id_encuestado;
        String id_gestante_remoto;

        ArrayList<GestanteModel> arrayResult = new ArrayList<GestanteModel>();

        for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {

            id_gestante_remoto = data.getString(5);
            fecha_parto = data.getString(1);
            estab_salud = data.getString(2);
            sexo_bebe = data.getString(3);
            id_encuestado = data.getString(4);
            GestanteModel gestanteModel;
            gestanteModel=new GestanteModel(id_gestante_remoto,fecha_parto,estab_salud,sexo_bebe,
                    id_encuestado,encuestadoDisplayName);

            arrayResult.add(gestanteModel);
        }
        ActualizarConstructor(arrayResult);
        return arrayResult;

    }

    private void ActualizarConstructor(ArrayList<GestanteModel> data){
        gestanteModelArrayList=data;
        filteredGestanteArrayList=data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filteredGestanteArrayList.size();
    }

    public Filter getFilterGender(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String key=charSequence.toString();
                if(key.isEmpty()){
                    filteredGestanteArrayList=gestanteModelArrayList;
                }
                else {
                    ArrayList<GestanteModel> filteredResult=new ArrayList<>();
                    for (GestanteModel row: gestanteModelArrayList){
                        if(row.getSexo_bebe().toLowerCase().contains(key.toLowerCase())){
                            filteredResult.add(row);
                        }
                    }
                    filteredGestanteArrayList=filteredResult;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=filteredGestanteArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredGestanteArrayList=(ArrayList<GestanteModel>)filterResults.values;
                notifyDataSetChanged();

            }
        };
    }
}
