package com.arsltech.developer.MovisdoApp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.arsltech.developer.MovisdoApp.ItemAnimation;
import com.arsltech.developer.MovisdoApp.R;
import com.arsltech.developer.MovisdoApp.model.AlternativaGestanteModel;
import com.arsltech.developer.MovisdoApp.model.AlternativaInfanteModel;
import com.arsltech.developer.MovisdoApp.model.RespuestaGestanteModel;
import com.arsltech.developer.MovisdoApp.model.RespuestaInfanteModel;
import com.arsltech.developer.MovisdoApp.provider.MovisdoContract;

import java.util.ArrayList;

public class AlternativaGestanteAdapter extends RecyclerView.Adapter<AlternativaGestanteAdapter.AlternativaGestanteViewHolder> {

    Context context;
    Cursor cursor;
    ArrayList<AlternativaGestanteModel> alternativaGestanteModelArrayList;
    ArrayList<AlternativaGestanteModel> filteredAlternativaGestanteArrayList;
    public ArrayList<String> alternativaElegidaArrayList;
    String id_visita_gestante;
    String id_pregunta;
    String id_gestante;

    public ArrayList<String> getAlternativaElegidaArrayList() {
        return alternativaElegidaArrayList;
    }

    public void setAlternativaElegidaArrayList(ArrayList<String> alternativaElegidaArrayList) {
        this.alternativaElegidaArrayList = alternativaElegidaArrayList;
    }

    public ArrayList<Integer> selectCheck = new ArrayList<>();

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

    public AlternativaGestanteAdapter(Context context, ArrayList<AlternativaGestanteModel> alternativaGestanteModelArrayList) {
        this.context = context;
        this.alternativaGestanteModelArrayList = alternativaGestanteModelArrayList;
        this.filteredAlternativaGestanteArrayList=alternativaGestanteModelArrayList;

    }

    public static final class AlternativaGestanteViewHolder extends RecyclerView.ViewHolder{

        CheckBox cbAlternativa;

        public AlternativaGestanteViewHolder(View itemView) {
            super(itemView);


            cbAlternativa=itemView.findViewById(R.id.checkbox_alternativa);

        }
    }

    @Override
    public AlternativaGestanteAdapter.AlternativaGestanteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.alternativa_multiple_layout,parent,false);
        return new AlternativaGestanteViewHolder(view);

    }

    @Override
    public void onBindViewHolder(AlternativaGestanteAdapter.AlternativaGestanteViewHolder holder, int position) {

        if(filteredAlternativaGestanteArrayList.size()!=2){
            holder.cbAlternativa.setText(filteredAlternativaGestanteArrayList.get(position).getAlternativa());
            ArrayList<RespuestaGestanteModel> receiveAnswers=RetrieveAllChoosenAnswers(getId_gestante(),getId_pregunta());
            if(receiveAnswers.size()>0){

                for (RespuestaGestanteModel answer :
                        receiveAnswers) {

                    if (answer.getId_alternativa().matches(filteredAlternativaGestanteArrayList.get(position).getId())){
                        holder.cbAlternativa.setChecked(true);

                    }

                }

            }

            ItemAnimation.animateLeftRight(holder.itemView, position);

            holder.cbAlternativa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.cbAlternativa.isChecked()){

                        alternativaElegidaArrayList.add(filteredAlternativaGestanteArrayList.get(position).getId());

                    }
                    else {
                        alternativaElegidaArrayList.remove(filteredAlternativaGestanteArrayList.get(position).getId());

                    }

                }
            });

        }
        else {
            holder.cbAlternativa.setText(filteredAlternativaGestanteArrayList.get(position).getAlternativa());
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
                        alternativaElegidaArrayList.add(filteredAlternativaGestanteArrayList.get(position).getId());
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
    public ArrayList <RespuestaGestanteModel> RetrieveAllChoosenAnswers(String id_gestante, String id_pregunta) {
        ArrayList<RespuestaGestanteModel> retrieveAnswers=new ArrayList<RespuestaGestanteModel>();
        String id="";
        String id_alternativa="";
        String id_visita_gestante="";
        String detalle="";
        try {
            Uri uri;
            //Unión de alternativa con respuesta, obtiene todas las alternativas que fueron seleccionadas como respuestas de todas las visitas cuyo infante tiene el ID?
            uri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TALTERNATIVA_ELEGIDA2_DETAILS);
            //Mostrar solo el campo detalle
            String[] projeccion = new String[]{MovisdoContract.TRESPUESTA_GESTANTE+"."+MovisdoContract.RespuestaGestanteColumns.ID_REMOTA
                    ,MovisdoContract.TRESPUESTA_GESTANTE+"."+MovisdoContract.RespuestaGestanteColumns.ID_ALTERNATIVA
                    ,MovisdoContract.TRESPUESTA_GESTANTE+"."+MovisdoContract.RespuestaGestanteColumns.ID_VISITA_GESTANTE
                    ,MovisdoContract.TRESPUESTA_GESTANTE+"."+MovisdoContract.RespuestaGestanteColumns.DETALLE};
            //Donde el id_infante sea ? y la pregunta sea ?
            String selection = MovisdoContract.TVISITA_GESTANTE+"."+MovisdoContract.VisitaGestanteColumns.ID_GESTANTE+" =? AND "+
                    MovisdoContract.TALTERNATIVA+"."+MovisdoContract.AlternativaColumns.ID_PREGUNTA+" =? AND "+MovisdoContract.TRESPUESTA_GESTANTE+"."+MovisdoContract.RespuestaGestanteColumns.PENDIENTE_ELIMINACION+"=0";
            String[] selectionArgs = {id_gestante,id_pregunta};
            Cursor c = context.getContentResolver().query(
                    uri,
                    projeccion,
                    selection,
                    selectionArgs,
                    null);

            while (c.moveToNext()) {
                id=c.getString(0);
                id_alternativa=c.getString(1);
                id_visita_gestante=c.getString(2);
                detalle=c.getString(3);
                RespuestaGestanteModel respuestaGestanteModel=new RespuestaGestanteModel(id,id_alternativa,id_visita_gestante,detalle);
                retrieveAnswers.add(respuestaGestanteModel);
            }
            c.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return retrieveAnswers;

    }


    public ArrayList<AlternativaGestanteModel> ConvertCursorToArray(Cursor data, String id_visita_gestante, String id_gestante) {
        cursor = data;
        String alternativa="";
        String id_pregunta="";
        String id_remoto="";


        ArrayList<AlternativaGestanteModel> arrayResult = new ArrayList<AlternativaGestanteModel>();

        for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {


            alternativa = data.getString(1);
            id_pregunta = data.getString(2);
            id_remoto = data.getString(3);

            AlternativaGestanteModel alternativaGestanteModel;
            alternativaGestanteModel=new AlternativaGestanteModel(id_remoto,alternativa,id_pregunta);

            arrayResult.add(alternativaGestanteModel);
        }

        ActualizarConstructor(arrayResult,id_visita_gestante,id_pregunta,id_gestante);
        return arrayResult;

    }

    private void CargarAlternativasElegidas(String id_gestante,String id_pregunta){
        ArrayList<RespuestaGestanteModel> receiveAnswers=new ArrayList<RespuestaGestanteModel>();
        receiveAnswers=RetrieveAllChoosenAnswers(id_gestante,id_pregunta);


        selectCheck.clear();
        if(filteredAlternativaGestanteArrayList.size()==2){
            if(receiveAnswers.size()>0){

                for (AlternativaGestanteModel alternativa :
                        filteredAlternativaGestanteArrayList) {

                    for (RespuestaGestanteModel answer: receiveAnswers) {

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
                for(int j=0; j<filteredAlternativaGestanteArrayList.size();j++){
                    selectCheck.add(0);
                }
            }


        }

    }

    private void ActualizarConstructor(ArrayList<AlternativaGestanteModel> data,String id_visita_gestante,String id_pregunta,String id_gestante){
        alternativaGestanteModelArrayList=data;
        filteredAlternativaGestanteArrayList=data;
        alternativaElegidaArrayList=new ArrayList<>();
        setId_visita_gestante(id_visita_gestante);
        setId_pregunta(id_pregunta);
        setId_gestante(id_gestante);
        CargarAlternativasElegidas(getId_gestante(),getId_pregunta());

        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return filteredAlternativaGestanteArrayList.size();
    }






}
