package com.arsltech.developer.MovisdoApp.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arsltech.developer.MovisdoApp.ItemAnimation;
import com.arsltech.developer.MovisdoApp.R;
import com.arsltech.developer.MovisdoApp.activity.AlternativaGestanteActivity;

import com.arsltech.developer.MovisdoApp.model.PreguntaGestanteModel;

import com.arsltech.developer.MovisdoApp.provider.MovisdoContract;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PreguntaGestanteAdapter extends RecyclerView.Adapter<PreguntaGestanteAdapter.PreguntaGestanteViewHolder>  {

    String id_visita_gestante;
    String id_gestante;

    public String getId_visita_gestante() {
        return id_visita_gestante;
    }

    public void setId_visita_gestante(String id_visita_gestante) {
        this.id_visita_gestante = id_visita_gestante;
    }

    public String getId_gestante() {
        return id_gestante;
    }

    public void setId_gestante(String id_gestante) {
        this.id_gestante = id_gestante;
    }

    Cursor cursor;
    Context context;
    ArrayList<PreguntaGestanteModel> preguntaGestanteModelArrayList;
    ArrayList<PreguntaGestanteModel> filteredPreguntaGestanteArrayList;

    public PreguntaGestanteAdapter(Context context, ArrayList<PreguntaGestanteModel> preguntaGestanteModelArrayList) {
        this.context = context;
        this.preguntaGestanteModelArrayList = preguntaGestanteModelArrayList;
        this.filteredPreguntaGestanteArrayList = preguntaGestanteModelArrayList;
    }



    public static final class PreguntaGestanteViewHolder extends RecyclerView.ViewHolder {
        TextView tvPregunta;
        TextView tvArea;
        TextView tvSugTemporal;
        TextView tvEstado;

        public PreguntaGestanteViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvPregunta=itemView.findViewById(R.id.textPregunta);
            tvArea=itemView.findViewById(R.id.textArea);
            tvSugTemporal=itemView.findViewById(R.id.textSugTemporal);
            tvEstado=itemView.findViewById(R.id.textEstado);
        }
    }

    @Override
    public PreguntaGestanteAdapter.PreguntaGestanteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.pregunta_layout, parent, false);
        return new PreguntaGestanteViewHolder(view);
    }



    @Override
    public void onBindViewHolder(PreguntaGestanteAdapter.PreguntaGestanteViewHolder holder, int position) {
        holder.tvPregunta.setText(filteredPreguntaGestanteArrayList.get(position).getPregunta());
        holder.tvArea.setText(filteredPreguntaGestanteArrayList.get(position).getArea());
        holder.tvSugTemporal.setText(filteredPreguntaGestanteArrayList.get(position).getSug_temporal());
        if(filteredPreguntaGestanteArrayList.get(position).getEstado().equals("Sin contestar")){

            holder.tvEstado.setTextColor(Color.parseColor("#D5001F"));
            holder.tvEstado.setText(filteredPreguntaGestanteArrayList.get(position).getEstado());
        }
        if(filteredPreguntaGestanteArrayList.get(position).getEstado().equals("Contestada")){

            holder.tvEstado.setTextColor(Color.parseColor("#00D51F"));
            holder.tvEstado.setText(filteredPreguntaGestanteArrayList.get(position).getEstado());
        }


        ItemAnimation.animateLeftRight(holder.itemView, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, AlternativaGestanteActivity.class);
                intent.putExtra("id_pregunta",filteredPreguntaGestanteArrayList.get(position).getId());
                intent.putExtra("pregunta",filteredPreguntaGestanteArrayList.get(position).getPregunta());
                intent.putExtra("area",filteredPreguntaGestanteArrayList.get(position).getArea());
                intent.putExtra("sug_temporal",filteredPreguntaGestanteArrayList.get(position).getSug_temporal());
                intent.putExtra("estado",filteredPreguntaGestanteArrayList.get(position).getEstado());
                intent.putExtra("id_visita_gestante",getId_visita_gestante());
                intent.putExtra("id_gestante",getId_gestante());
                context.startActivity(intent);


            }
        });
    }

    public ArrayList<PreguntaGestanteModel> ConvertCursorToArray(Cursor data, String id_gestante, String id_visita_gestante) {
        cursor = data;
        String pregunta;
        String area;
        String categoria;
        String sug_temporal;
        String id_pregunta_remoto;

        setId_gestante(id_gestante);
        ArrayList<PreguntaGestanteModel> arrayResult = new ArrayList<PreguntaGestanteModel>();
        ArrayList<String> arrayResult2 = new ArrayList<String>();
        arrayResult2=RetrieveAnswersChoosen(id_gestante);
        for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {

            id_pregunta_remoto = data.getString(5);
            pregunta = data.getString(1);
            area = data.getString(2);
            categoria = data.getString(3);
            sug_temporal = data.getString(4);
            PreguntaGestanteModel preguntaGestanteModel;
            if(arrayResult2.size()>0){

                for (int i=0;i<arrayResult2.size();i++){

                    if(arrayResult2.contains(id_pregunta_remoto)){
                        preguntaGestanteModel=new PreguntaGestanteModel(id_pregunta_remoto,pregunta,area,categoria,
                                sug_temporal,"Contestada");
                        arrayResult.add(preguntaGestanteModel);
                        break;
                    }
                    else {
                        if(i+1==arrayResult2.size()){
                            preguntaGestanteModel=new PreguntaGestanteModel(id_pregunta_remoto,pregunta,area,categoria,
                                    sug_temporal,"Sin contestar");
                            arrayResult.add(preguntaGestanteModel);
                        }

                    }
                }

            }
            else {
                preguntaGestanteModel=new PreguntaGestanteModel(id_pregunta_remoto,pregunta,area,categoria,
                        sug_temporal,"Sin contestar");
                arrayResult.add(preguntaGestanteModel);
            }

        }
        ActualizarConstructor(arrayResult,id_visita_gestante);

        return arrayResult;

    }

    public ArrayList<String> RetrieveAnswersChoosen(String id_gestante) {
        ArrayList<String> alternativaElegidaModelArrayList=new ArrayList<String>();
        try {
            Uri uri;
            uri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TALTERNATIVA_ELEGIDA2_DETAILS);
            String[] projeccion = new String[]{MovisdoContract.TALTERNATIVA+"."+MovisdoContract.AlternativaColumns.ID_PREGUNTA};
            String selection = MovisdoContract.TVISITA_GESTANTE+"."+MovisdoContract.VisitaGestanteColumns.ID_GESTANTE+" =?";
            String[] selectionArgs = {id_gestante};
            Cursor c = context.getContentResolver().query(
                    uri,
                    projeccion,
                    selection,
                    selectionArgs,
                    null);

            while (c.moveToNext()) {
                String id_pregunta=c.getString(0);
                alternativaElegidaModelArrayList.add(id_pregunta);

            }
            c.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return alternativaElegidaModelArrayList;

    }

    private void ActualizarConstructor(ArrayList<PreguntaGestanteModel> data,String id_visita_gestante){
        preguntaGestanteModelArrayList=data;
        filteredPreguntaGestanteArrayList=data;
        setId_visita_gestante(id_visita_gestante);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
            return filteredPreguntaGestanteArrayList.size();
    }

    //Filtro de recycler por pregunta
    public Filter getFilterPregunta(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String key=charSequence.toString();
                if(key.isEmpty()){
                    filteredPreguntaGestanteArrayList=preguntaGestanteModelArrayList;
                }
                else {
                    ArrayList<PreguntaGestanteModel> filteredResult=new ArrayList<>();
                    for (PreguntaGestanteModel row: preguntaGestanteModelArrayList){
                        if(row.getPregunta().toLowerCase().contains(key.toLowerCase())){
                            filteredResult.add(row);
                        }
                    }
                    filteredPreguntaGestanteArrayList=filteredResult;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=filteredPreguntaGestanteArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredPreguntaGestanteArrayList=(ArrayList<PreguntaGestanteModel>)filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    //Filtro de recycler por Estado
    public Filter getFilterStatus(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String key=charSequence.toString();
                if(key.isEmpty()){
                    filteredPreguntaGestanteArrayList=preguntaGestanteModelArrayList;
                }
                else {
                    ArrayList<PreguntaGestanteModel> filteredResult=new ArrayList<>();
                    for (PreguntaGestanteModel row: preguntaGestanteModelArrayList){
                        if(row.getEstado().toLowerCase().contains(key.toLowerCase())){
                            filteredResult.add(row);
                        }
                    }
                    filteredPreguntaGestanteArrayList=filteredResult;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=filteredPreguntaGestanteArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredPreguntaGestanteArrayList=(ArrayList<PreguntaGestanteModel>)filterResults.values;
                notifyDataSetChanged();

            }
        };
    }


}
