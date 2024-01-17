package com.arsltech.developer.MovisdoApp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.arsltech.developer.MovisdoApp.ItemAnimation;
import com.arsltech.developer.MovisdoApp.R;
import com.arsltech.developer.MovisdoApp.model.AlternativaInfanteModel;
import com.arsltech.developer.MovisdoApp.model.RespuestaInfanteModel;
import com.arsltech.developer.MovisdoApp.provider.MovisdoContract;

import java.util.ArrayList;

public class AlternativaInfanteAdapter extends RecyclerView.Adapter<AlternativaInfanteAdapter.AlternativaInfanteViewHolder> {

    Context context;
    Cursor cursor;
    ArrayList<AlternativaInfanteModel> alternativaInfanteModelArrayList;
    ArrayList<AlternativaInfanteModel> filteredAlternativaInfanteArrayList;
    public ArrayList<String> alternativaElegidaArrayList;
    String id_visita_infante;
    String id_pregunta;
    String id_infante;
    public ArrayList<Integer> selectCheck = new ArrayList<>();

    public void setAlternativaElegidaArrayList(ArrayList<String> alternativaElegidaArrayList) {
        this.alternativaElegidaArrayList = alternativaElegidaArrayList;
    }

    public ArrayList<String> getAlternativaElegidaArrayList() {
        return alternativaElegidaArrayList;
    }

    public ArrayList<Integer> getSelectCheck() {
        return selectCheck;
    }

    public void setSelectCheck(ArrayList<Integer> selectCheck) {
        this.selectCheck = selectCheck;
    }


    public String getId_pregunta() {
        return id_pregunta;
    }

    public void setId_pregunta(String id_pregunta) {
        this.id_pregunta = id_pregunta;
    }

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

    public AlternativaInfanteAdapter(Context context, ArrayList<AlternativaInfanteModel> alternativaInfanteModelArrayList) {
        this.context = context;
        this.alternativaInfanteModelArrayList = alternativaInfanteModelArrayList;
        this.filteredAlternativaInfanteArrayList=alternativaInfanteModelArrayList;

    }

    public static final class AlternativaInfanteViewHolder extends RecyclerView.ViewHolder{

        CheckBox cbAlternativa;

        public AlternativaInfanteViewHolder(View itemView) {
            super(itemView);
            cbAlternativa=itemView.findViewById(R.id.checkbox_alternativa);


        }
    }

    @Override
    public AlternativaInfanteAdapter.AlternativaInfanteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.alternativa_multiple_layout,parent,false);
        return new AlternativaInfanteViewHolder(view);
    }



    @Override
    public void onBindViewHolder(AlternativaInfanteAdapter.AlternativaInfanteViewHolder holder, int position) {
        if(filteredAlternativaInfanteArrayList.size()!=2){
            holder.cbAlternativa.setText(filteredAlternativaInfanteArrayList.get(position).getAlternativa());
            ArrayList<RespuestaInfanteModel> receiveAnswers=RetrieveAllChoosenAnswers(getId_infante(),getId_pregunta());
            if(receiveAnswers.size()>0){

                for (RespuestaInfanteModel answer :
                        receiveAnswers) {

                    if (answer.getId_alternativa().matches(filteredAlternativaInfanteArrayList.get(position).getId())){
                        holder.cbAlternativa.setChecked(true);

                    }

                }

            }

            ItemAnimation.animateLeftRight(holder.itemView, position);

            holder.cbAlternativa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.cbAlternativa.isChecked()){

                        alternativaElegidaArrayList.add(filteredAlternativaInfanteArrayList.get(position).getId());

                    }
                    else {
                        alternativaElegidaArrayList.remove(filteredAlternativaInfanteArrayList.get(position).getId());

                    }

                }
            });

        }
        else {
            holder.cbAlternativa.setText(filteredAlternativaInfanteArrayList.get(position).getAlternativa());
            //Toast.makeText(context,String.valueOf(selectCheck.get(1)),Toast.LENGTH_LONG).show();

            if (selectCheck.get(position) == 1) {
                holder.cbAlternativa.setChecked(true);
            } else {
                holder.cbAlternativa.setChecked(false);
            }

            holder.cbAlternativa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int k=0; k<selectCheck.size(); k++) {
                        if(k==position) {
                            selectCheck.set(k,1);
                        } else {
                            selectCheck.set(k,0);
                        }
                    }
                    notifyDataSetChanged();

                }
            });
            holder.cbAlternativa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked==true){

                        alternativaElegidaArrayList.clear();
                        alternativaElegidaArrayList.add(filteredAlternativaInfanteArrayList.get(position).getId());
                    }
                }
            });

        }

    }

    /**
     * Obtiene alternativas elegidas de la pregunta ? del infante con id ?
     * @param id_infante
     * @param id_pregunta
     * @return
     */
    public ArrayList <RespuestaInfanteModel> RetrieveAllChoosenAnswers(String id_infante, String id_pregunta) {
        ArrayList<RespuestaInfanteModel> retrieveAnswers=new ArrayList<RespuestaInfanteModel>();
        String id="";
        String id_alternativa="";
        String id_visita_infante="";
        String detalle="";
        try {
            Uri uri;
            //Unión de alternativa con respuesta, obtiene todas las alternativas que fueron seleccionadas como respuestas de todas las visitas cuyo infante tiene el ID?
            uri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TALTERNATIVA_ELEGIDA_DETAILS);
            //Mostrar solo el campo detalle
            String[] projeccion = new String[]{MovisdoContract.TRESPUESTA_INFANTE+"."+MovisdoContract.RespuestaInfanteColumns.ID_REMOTA
                    ,MovisdoContract.TRESPUESTA_INFANTE+"."+MovisdoContract.RespuestaInfanteColumns.ID_ALTERNATIVA
                    ,MovisdoContract.TRESPUESTA_INFANTE+"."+MovisdoContract.RespuestaInfanteColumns.ID_VISITA_INFANTE
                    ,MovisdoContract.TRESPUESTA_INFANTE+"."+MovisdoContract.RespuestaInfanteColumns.DETALLE};
            //Donde el id_infante sea ? y la pregunta sea ?
            String selection = MovisdoContract.TVISITA_INFANTE+"."+MovisdoContract.VisitaInfanteColumns.ID_INFANTE+" =? AND "+
                    MovisdoContract.TALTERNATIVA+"."+MovisdoContract.AlternativaColumns.ID_PREGUNTA+" =? AND "+MovisdoContract.TRESPUESTA_INFANTE+"."+MovisdoContract.RespuestaInfanteColumns.PENDIENTE_ELIMINACION+"=0";
            String[] selectionArgs = {id_infante,id_pregunta};
            Cursor c = context.getContentResolver().query(
                    uri,
                    projeccion,
                    selection,
                    selectionArgs,
                    null);

            while (c.moveToNext()) {
                id=c.getString(0);
                id_alternativa=c.getString(1);
                id_visita_infante=c.getString(2);
                detalle=c.getString(3);
                RespuestaInfanteModel respuestaInfanteModel=new RespuestaInfanteModel(id,id_alternativa,id_visita_infante,detalle);
                retrieveAnswers.add(respuestaInfanteModel);
            }
            c.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return retrieveAnswers;

    }


    public ArrayList<AlternativaInfanteModel> ConvertCursorToArray(Cursor data, String id_visita_infante, String id_infante) {
        cursor = data;
        String alternativa="";
        String id_pregunta="";
        String id_remoto="";


        ArrayList<AlternativaInfanteModel> arrayResult = new ArrayList<AlternativaInfanteModel>();

        for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {


            alternativa = data.getString(1);
            id_pregunta = data.getString(2);
            id_remoto = data.getString(3);

            AlternativaInfanteModel alternativaInfanteModel;
            alternativaInfanteModel=new AlternativaInfanteModel(id_remoto,alternativa,id_pregunta);

            arrayResult.add(alternativaInfanteModel);
        }

        ActualizarConstructor(arrayResult,id_visita_infante,id_pregunta,id_infante);
        return arrayResult;

    }

    private void CargarAlternativasElegidas(String id_infante,String id_pregunta){
        ArrayList<RespuestaInfanteModel> receiveAnswers=new ArrayList<RespuestaInfanteModel>();
        receiveAnswers=RetrieveAllChoosenAnswers(id_infante,id_pregunta);


        selectCheck.clear();
        if(filteredAlternativaInfanteArrayList.size()==2){
            if(receiveAnswers.size()>0){

                for (AlternativaInfanteModel alternativa :
                        filteredAlternativaInfanteArrayList) {

                    for (RespuestaInfanteModel answer: receiveAnswers) {

                        if(answer.getId_alternativa().matches(alternativa.getId())){
                            selectCheck.add(1);
                            selectCheck.add(0);
                            break;
                        }
                        else {

                            selectCheck.add(0);
                            selectCheck.add(1);
                            break;
                        }

                    }
                    break;

                }
            }
            else {
                for(int j=0; j<filteredAlternativaInfanteArrayList.size();j++){
                    selectCheck.add(0);
                }
            }


        }

    }

    private void ActualizarConstructor(ArrayList<AlternativaInfanteModel> data,String id_visita_infante,String id_pregunta,String id_infante){
        alternativaInfanteModelArrayList=data;
        filteredAlternativaInfanteArrayList=data;
        alternativaElegidaArrayList=new ArrayList<>();
        setId_visita_infante(id_visita_infante);
        setId_pregunta(id_pregunta);
        setId_infante(id_infante);
        CargarAlternativasElegidas(getId_infante(),getId_pregunta());

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filteredAlternativaInfanteArrayList.size();
    }


}
