package com.arsltech.developer.MovisdoApp.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import android.provider.CallLog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arsltech.developer.MovisdoApp.ItemAnimation;
import com.arsltech.developer.MovisdoApp.R;

import com.arsltech.developer.MovisdoApp.activity.LlamadaVisitaInfanteActivity;
import com.arsltech.developer.MovisdoApp.activity.PreguntaInfanteActivity;

import com.arsltech.developer.MovisdoApp.activity.VisitaInfanteActivity;
import com.arsltech.developer.MovisdoApp.activity.VisitaInfanteCreateActivity;
import com.arsltech.developer.MovisdoApp.model.VisitaInfanteModel;
import com.arsltech.developer.MovisdoApp.provider.MovisdoContract;
import com.arsltech.developer.MovisdoApp.sync.MovisdoSyncAdapter;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VisitaInfanteAdapter extends RecyclerView.Adapter<VisitaInfanteAdapter.VisitaInfanteViewHolder>{

    Context context;
    Cursor cursor;
    ArrayList<VisitaInfanteModel> visitaInfanteModelArrayList;
    ArrayList<VisitaInfanteModel> filteredVisitaInfanteArrayList;


    public VisitaInfanteAdapter(Context context, ArrayList<VisitaInfanteModel> visitaInfanteModelArrayList) {
        this.context = context;
        this.visitaInfanteModelArrayList = visitaInfanteModelArrayList;
        this.filteredVisitaInfanteArrayList=visitaInfanteModelArrayList;
    }

    public static final class VisitaInfanteViewHolder extends RecyclerView.ViewHolder{
        TextView tvInfante;
        TextView tvModVisita;
        TextView tvCoordenada;
        TextView tvFecha;

        ImageView imageTash;

        public VisitaInfanteViewHolder(View itemView) {
            super(itemView);

            tvInfante=itemView.findViewById(R.id.textUser);
            tvModVisita=itemView.findViewById(R.id.textModVisita);
            tvCoordenada=itemView.findViewById(R.id.textCoordenada);
            tvFecha=itemView.findViewById(R.id.textFecha);
            imageTash=itemView.findViewById(R.id.imageTrash);

        }
    }

    @Override
    public VisitaInfanteAdapter.VisitaInfanteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.visita_layout,parent,false);
        return new VisitaInfanteViewHolder(view);
    }



    @Override
    public void onBindViewHolder(VisitaInfanteAdapter.VisitaInfanteViewHolder holder, int position) {

        holder.tvInfante.setText(filteredVisitaInfanteArrayList.get(position).getInfanteDisplayName());
        holder.tvModVisita.setText(filteredVisitaInfanteArrayList.get(position).getMod_visita());
        holder.tvCoordenada.setText(filteredVisitaInfanteArrayList.get(position).getLatitud()+", "+filteredVisitaInfanteArrayList.get(position).getLongitud());


        try {
            Date dateToFormat=new SimpleDateFormat("yyyy-MM-dd").parse(filteredVisitaInfanteArrayList.get(position).getFecha());
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String dateFormated = dateFormat.format(dateToFormat);
            holder.tvFecha.setText(dateFormated);

        } catch (ParseException e) {
            Toast.makeText(context,"Formato de fecha inválido",Toast.LENGTH_SHORT).show();
        }



        ItemAnimation.animateLeftRight(holder.itemView, position);

        holder.imageTash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ViewGroup parent = (ViewGroup) v.getParent();

                Button btnConfirm;
                TextView tvCancel;

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                View dialogView= LayoutInflater.from(context).inflate(R.layout.activity_modal,parent,false);
                builder.setCancelable(false);
                builder.setView(dialogView);

                btnConfirm=dialogView.findViewById(R.id.btnConfirm);
                tvCancel=dialogView.findViewById(R.id.textCancel);

                AlertDialog alertDialog=builder.create();

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ContentValues values = new ContentValues();

                        values.put(MovisdoContract.VisitaInfanteColumns.PENDIENTE_ELIMINACION, "1");

                        String [] id_visita_infante=new String[]{filteredVisitaInfanteArrayList.get(position).id};
                        context.getContentResolver().update(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TVISITA_INFANTE),values,
                                MovisdoContract.VisitaInfanteColumns.ID_REMOTA+" =?", id_visita_infante);
                        MovisdoSyncAdapter.sincronizarAhora(context, true);

                        Toast.makeText(context,"Registro eliminado...",Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (filteredVisitaInfanteArrayList.get(position).getMod_visita().contains("Presencial")) {

                            Intent intent_pregunta = new Intent(context, PreguntaInfanteActivity.class);
                            intent_pregunta.putExtra("id_visita_infante", filteredVisitaInfanteArrayList.get(position).getId());
                            intent_pregunta.putExtra("id_infante", filteredVisitaInfanteArrayList.get(position).getId_infante());
                            context.startActivity(intent_pregunta);
                        }
                        if (filteredVisitaInfanteArrayList.get(position).getMod_visita().contains("Remoto")) {

                            final BottomSheetDialog bt = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
                            View newView = LayoutInflater.from(context).inflate(R.layout.layout_bottom_sheet, null);

                            //Variable para obtener permisos para realizar llamada
                            final int REQUEST_CALL = 1;

                            //url para crear llamada

                            //variable para los permisos de registro de llamada

                            TextView tvFecha = newView.findViewById(R.id.textFecha);
                            TextView tvLatitud = newView.findViewById(R.id.textLatitud);
                            TextView tvLongitud = newView.findViewById(R.id.textLongitud);
                            TextView tvModalidad = newView.findViewById(R.id.textModalidad);
                            TextView tvCelular = newView.findViewById(R.id.textCelular);
                            String url_llamadas = "http://kuwayo.com/bdmovisdoAPI/create_visita_llamada_infante.php";

                            LinearLayout linLayLlamada = newView.findViewById(R.id.linLayLlamada);
                            LinearLayout linLayPregunta = newView.findViewById(R.id.linLayPregunta);
                            LinearLayout linlayLlamadas = newView.findViewById(R.id.linLayLlamadas);

                            //Llenamos parametros
                            try {
                                Date dateToFormat = new SimpleDateFormat("yyyy-MM-dd").parse(filteredVisitaInfanteArrayList.get(position).getFecha());
                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                String dateFormated = dateFormat.format(dateToFormat);
                                tvFecha.setText(dateFormated);

                            } catch (ParseException e) {
                                Toast.makeText(context, "Formato de fecha inválido", Toast.LENGTH_SHORT).show();
                            }

                            tvLatitud.setText("Latitud: " + filteredVisitaInfanteArrayList.get(position).getLatitud());
                            tvLongitud.setText("Longitud: " + filteredVisitaInfanteArrayList.get(position).getLongitud());
                            tvModalidad.setText(filteredVisitaInfanteArrayList.get(position).getMod_visita());
                            tvCelular.setText(ReturnPhoneData(filteredVisitaInfanteArrayList.get(position).getId_infante()));


                            bt.setContentView(newView);
                            bt.show();

                            linLayLlamada.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {

                                    //Recibimos texto de callText
                                    String number = tvCelular.getText().toString();
                                    //la consulta a la bd nos dio un numero de celular el cual depuramos de espacios y si hay contenido seguimos
                                    if (number.trim().length() > 0) {

                                        //Solicitamos permisos
                                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            //Si aceptamos obtenemos acceso para poder hacer llamadas
                                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);

                                        } else {
                                            //Una vez aceptado el permiso se procede con la llamada
                                            String dial = "tel:+51" + number;
                                            Intent intent_call = new Intent(Intent.ACTION_CALL, Uri.parse(dial));
                                            context.startActivity(intent_call);

                                        }
                                    }

                                }
                            });
                            linLayPregunta.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent_pregunta = new Intent(context, PreguntaInfanteActivity.class);
                                    intent_pregunta.putExtra("id_visita_infante", filteredVisitaInfanteArrayList.get(position).getId());
                                    intent_pregunta.putExtra("id_infante", filteredVisitaInfanteArrayList.get(position).getId_infante());
                                    context.startActivity(intent_pregunta);


                                }
                            });
                            linlayLlamadas.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    InsertarLlamada(url_llamadas, filteredVisitaInfanteArrayList.get(position).getId(), tvCelular);
                                    Intent intent_llamadas = new Intent(context, LlamadaVisitaInfanteActivity.class);
                                    intent_llamadas.putExtra("id_visita_infante", filteredVisitaInfanteArrayList.get(position).getId());
                                    intent_llamadas.putExtra("infanteDisplayName", filteredVisitaInfanteArrayList.get(position).getInfanteDisplayName());
                                    context.startActivity(intent_llamadas);
                                }
                            });

                        }

                    }
                });

    }

    public ArrayList<VisitaInfanteModel> ConvertCursorToArray(Cursor data, String infanteDisplayName) {
        cursor = data;
        String fecha;
        String mod_visita;
        String latitud;
        String longitud;
        String id_infante;
        String id_visita_infante_remoto;

        ArrayList<VisitaInfanteModel> arrayResult = new ArrayList<VisitaInfanteModel>();

        for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {

            id_visita_infante_remoto = data.getString(6);
            fecha = data.getString(1);
            mod_visita = data.getString(2);
            latitud = data.getString(3);
            longitud = data.getString(4);
            id_infante = data.getString(5);

            VisitaInfanteModel visitaInfanteModel;
            visitaInfanteModel=new VisitaInfanteModel(id_visita_infante_remoto,fecha,mod_visita,latitud,
                    longitud,id_infante,infanteDisplayName);

            arrayResult.add(visitaInfanteModel);
        }
        ActualizarConstructor(arrayResult);

        return arrayResult;

    }

    private void ActualizarConstructor(ArrayList<VisitaInfanteModel> data){
        visitaInfanteModelArrayList=data;
        filteredVisitaInfanteArrayList=data;
        notifyDataSetChanged();
    }

    //Metodo para convertir duración en segundos(string) a Time pero en String
    private String ConvertIntToTime(String duration){
        String durationToConvert=duration;
        int time=Integer.parseInt(durationToConvert);
        int p1 = time % 60;
        int p2 = time / 60;
        int p3 = p2 % 60;
        p2 = p2 / 60;

        return p2 + ":" + p3 + ":" + p1;

    }

    //Metodo para convertir date(string) en TimeStamp(string)
    private String ConvertTimeStampToString(String date){
        String dateToConvert=date;
        Date dateConverted;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        long milliSeconds= Long.parseLong(dateToConvert);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        dateConverted=calendar.getTime();
        return new Timestamp(dateConverted.getTime()).toString();
    }


    private String ReturnPhoneData(String id_infante) {
        String phoneNumber = "";
        try {
            Uri uri;
            uri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TENCUESTADO_PHONE_DETAILS);
            String selection = MovisdoContract.TINFANTE+"."+MovisdoContract.InfanteColumns.ID_REMOTA+" =?";
            String[] selectionArgs = {id_infante};
            String[] projeccion = new String[]{MovisdoContract.TENCUESTADO+"."+MovisdoContract.EncuestadoColumns.CELULAR};
            Cursor c = context.getContentResolver().query(
                    uri,
                    projeccion,
                    selection,
                    selectionArgs,
                    null);

            while (c.moveToNext()) {

                phoneNumber=c.getString(0);

            }
            c.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return phoneNumber;
    }

    //Insertamos data en tabla llamadas
    private void InsertarLlamada(String url_llamada, String id_visita_infante,TextView tvCelular){
        String datos_llamada, duracion;

        //Recibimos texto de callText
        String number = tvCelular.getText().toString();
        //la consulta a la bd nos dio un numero de celular el cual depuramos de espacios y si hay contenido seguimos
        if (number.trim().length() > 0) {
            try {
                Uri uri;
                uri=Uri.parse("content://call_log/calls");
                String []projeccion=new String[]{CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.DURATION, CallLog.Calls.DATE};
                Cursor c=context.getContentResolver().query(
                        uri,
                        projeccion,
                        null,
                        null,
                        android.provider.CallLog.Calls.DATE + " DESC limit 1;");

                while(c.moveToNext()){

                    if(c.getString(1).equals("+51"+number)){

                        datos_llamada=ConvertTimeStampToString(c.getString(3));
                        duracion=ConvertIntToTime(c.getString(2));

                        ContentValues values = new ContentValues();
                        values.put(MovisdoContract.LlamadaVisitaInfanteColumns.DATOS_LLAMADA, datos_llamada);
                        values.put(MovisdoContract.LlamadaVisitaInfanteColumns.DURACION, duracion);
                        values.put(MovisdoContract.LlamadaVisitaInfanteColumns.ID_VISITA_INFANTE, id_visita_infante);
                        values.put(MovisdoContract.LlamadaVisitaInfanteColumns.PENDIENTE_INSERCION, 1);

                        context.getContentResolver().insert(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TLLAMADA_VISITA_INFANTE), values);
                        MovisdoSyncAdapter.sincronizarAhora(context, true);

                        //insertCallToDatabase(url_llamada,datos_llamada,duracion,id_visita_infante);


                    //textView.append("Tipo: "+c.getString(0)+" Número: "+c.getString(1)+" Duración: "+ConvertIntToTime(c.getString(2))+" Fecha: "+ConvertDateToTimeStamp(c.getString(3))+"\n");
                    }

                }
                c.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }

    }


    @Override
    public int getItemCount() {
        return filteredVisitaInfanteArrayList.size();
    }

    public Filter getFilterModalidad(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String key=charSequence.toString();
                if(key.isEmpty()){
                    filteredVisitaInfanteArrayList=visitaInfanteModelArrayList;
                }
                else {
                    ArrayList<VisitaInfanteModel> filteredResult=new ArrayList<>();
                    for (VisitaInfanteModel row: visitaInfanteModelArrayList){
                        if(row.getMod_visita().toLowerCase().contains(key.toLowerCase())){
                            filteredResult.add(row);
                        }
                    }
                    filteredVisitaInfanteArrayList=filteredResult;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=filteredVisitaInfanteArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredVisitaInfanteArrayList=(ArrayList<VisitaInfanteModel>)filterResults.values;
                notifyDataSetChanged();

            }
        };
    }
}
