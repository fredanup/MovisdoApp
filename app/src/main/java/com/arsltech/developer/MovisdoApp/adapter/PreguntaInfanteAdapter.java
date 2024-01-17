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
import com.arsltech.developer.MovisdoApp.activity.AlternativaInfanteActivity;
import com.arsltech.developer.MovisdoApp.model.PreguntaInfanteModel;
import com.arsltech.developer.MovisdoApp.provider.MovisdoContract;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PreguntaInfanteAdapter extends RecyclerView.Adapter<PreguntaInfanteAdapter.PreguntaInfanteViewHolder>  {

    String id_visita_infante;
    String id_infante;
    public String getId_visita_infante() {
        return id_visita_infante;
    }

    public void setId_visita_infante(String id_visita_infante) {
        this.id_visita_infante = id_visita_infante;
    }

    public String getId_infante() {
        return id_infante;
    }

    public void setId_infante(String id_infante) {
        this.id_infante = id_infante;
    }

    Context context;
    Cursor cursor;
    ArrayList<PreguntaInfanteModel> preguntaInfanteModelArrayList;
    ArrayList<PreguntaInfanteModel> filteredPreguntaInfanteArrayList;

    public PreguntaInfanteAdapter(Context context, ArrayList<PreguntaInfanteModel> preguntaInfanteModelArrayList) {
        this.context = context;
        this.preguntaInfanteModelArrayList = preguntaInfanteModelArrayList;
        this.filteredPreguntaInfanteArrayList = preguntaInfanteModelArrayList;
    }



    public static final class PreguntaInfanteViewHolder extends RecyclerView.ViewHolder {
        TextView tvPregunta;
        TextView tvArea;
        TextView tvSugTemporal;
        TextView tvEstado;

        public PreguntaInfanteViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvPregunta=itemView.findViewById(R.id.textPregunta);
            tvArea=itemView.findViewById(R.id.textArea);
            tvSugTemporal=itemView.findViewById(R.id.textSugTemporal);
            tvEstado=itemView.findViewById(R.id.textEstado);

        }
    }

    @Override
    public PreguntaInfanteAdapter.PreguntaInfanteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.pregunta_layout, parent, false);

        return new PreguntaInfanteViewHolder(view);
    }



    @Override
    public void onBindViewHolder(PreguntaInfanteAdapter.PreguntaInfanteViewHolder holder, int position) {

        holder.tvPregunta.setText(filteredPreguntaInfanteArrayList.get(position).getPregunta());
        holder.tvArea.setText(filteredPreguntaInfanteArrayList.get(position).getArea());
        holder.tvSugTemporal.setText(filteredPreguntaInfanteArrayList.get(position).getSug_temporal());
        if(filteredPreguntaInfanteArrayList.get(position).getEstado().equals("Sin contestar")){

            holder.tvEstado.setTextColor(Color.parseColor("#D5001F"));
            holder.tvEstado.setText(filteredPreguntaInfanteArrayList.get(position).getEstado());
        }
        if(filteredPreguntaInfanteArrayList.get(position).getEstado().equals("Contestada")){

            holder.tvEstado.setTextColor(Color.parseColor("#00D51F"));
            holder.tvEstado.setText(filteredPreguntaInfanteArrayList.get(position).getEstado());
        }

        ItemAnimation.animateLeftRight(holder.itemView, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    Intent intent=new Intent(context, AlternativaInfanteActivity.class);
                    intent.putExtra("id_pregunta",filteredPreguntaInfanteArrayList.get(position).getId());
                    intent.putExtra("pregunta",filteredPreguntaInfanteArrayList.get(position).getPregunta());
                    intent.putExtra("area",filteredPreguntaInfanteArrayList.get(position).getArea());
                    intent.putExtra("sug_temporal",filteredPreguntaInfanteArrayList.get(position).getSug_temporal());
                    intent.putExtra("estado",filteredPreguntaInfanteArrayList.get(position).getEstado());
                    intent.putExtra("id_visita_infante",getId_visita_infante());
                    intent.putExtra("id_infante",getId_infante());
                    context.startActivity(intent);


            }
        });
    }

    public ArrayList<PreguntaInfanteModel> ConvertCursorToArray(Cursor data,String id_infante,String id_visita_infante) {
        cursor = data;
        String pregunta;
        String area;
        String categoria;
        String sug_temporal;
        String id_pregunta_remoto;

        setId_infante(id_infante);
        ArrayList<PreguntaInfanteModel> arrayResult = new ArrayList<PreguntaInfanteModel>();
        ArrayList<String> arrayResult2 = new ArrayList<String>();
        arrayResult2=RetrieveAnswersChoosen(id_infante);
        for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {

            id_pregunta_remoto = data.getString(5);
            pregunta = data.getString(1);
            area = data.getString(2);
            categoria = data.getString(3);
            sug_temporal = data.getString(4);
            PreguntaInfanteModel preguntaInfanteModel;
            if(arrayResult2.size()>0){

                for (int i=0;i<arrayResult2.size();i++){

                    if(arrayResult2.contains(id_pregunta_remoto)){
                        preguntaInfanteModel=new PreguntaInfanteModel(id_pregunta_remoto,pregunta,area,categoria,
                                sug_temporal,"Contestada");
                        arrayResult.add(preguntaInfanteModel);
                        break;
                    }
                    else {
                        if(i+1==arrayResult2.size()){
                            preguntaInfanteModel=new PreguntaInfanteModel(id_pregunta_remoto,pregunta,area,categoria,
                                    sug_temporal,"Sin contestar");
                            arrayResult.add(preguntaInfanteModel);
                        }

                    }
                }


            }
            else {
                preguntaInfanteModel=new PreguntaInfanteModel(id_pregunta_remoto,pregunta,area,categoria,
                        sug_temporal,"Sin contestar");
                arrayResult.add(preguntaInfanteModel);
            }

        }
        ActualizarConstructor(arrayResult,id_visita_infante);

        return arrayResult;

    }

    public ArrayList<String> RetrieveAnswersChoosen(String id_infante) {
       ArrayList<String> alternativaElegidaModelArrayList=new ArrayList<String>();
        try {
            Uri uri;
            uri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TALTERNATIVA_ELEGIDA_DETAILS);
            String[] projeccion = new String[]{MovisdoContract.TALTERNATIVA+"."+MovisdoContract.AlternativaColumns.ID_PREGUNTA};
            String selection = MovisdoContract.TVISITA_INFANTE+"."+MovisdoContract.VisitaInfanteColumns.ID_INFANTE+" =?";
            String[] selectionArgs = {id_infante};
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



    private void ActualizarConstructor(ArrayList<PreguntaInfanteModel> data,String id_visita_infante){
        preguntaInfanteModelArrayList=data;
        filteredPreguntaInfanteArrayList=data;
        setId_visita_infante(id_visita_infante);
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
            return filteredPreguntaInfanteArrayList.size();
    }

    //Filtro de recycler por pregunta
    public Filter getFilterPregunta(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String key=charSequence.toString();
                if(key.isEmpty()){
                    filteredPreguntaInfanteArrayList=preguntaInfanteModelArrayList;
                }
                else {
                    ArrayList<PreguntaInfanteModel> filteredResult=new ArrayList<>();
                    for (PreguntaInfanteModel row: preguntaInfanteModelArrayList){
                        if(row.getPregunta().toLowerCase().contains(key.toLowerCase())){
                            filteredResult.add(row);
                        }
                    }
                    filteredPreguntaInfanteArrayList=filteredResult;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=filteredPreguntaInfanteArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredPreguntaInfanteArrayList=(ArrayList<PreguntaInfanteModel>)filterResults.values;
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
                    filteredPreguntaInfanteArrayList=preguntaInfanteModelArrayList;
                }
                else {
                    ArrayList<PreguntaInfanteModel> filteredResult=new ArrayList<>();
                    for (PreguntaInfanteModel row: preguntaInfanteModelArrayList){
                        if(row.getEstado().toLowerCase().contains(key.toLowerCase())){
                            filteredResult.add(row);
                        }
                    }
                    filteredPreguntaInfanteArrayList=filteredResult;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=filteredPreguntaInfanteArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredPreguntaInfanteArrayList=(ArrayList<PreguntaInfanteModel>)filterResults.values;
                notifyDataSetChanged();

            }
        };
    }


}
