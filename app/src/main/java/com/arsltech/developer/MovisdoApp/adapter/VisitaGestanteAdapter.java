package com.arsltech.developer.MovisdoApp.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
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
import com.arsltech.developer.MovisdoApp.activity.LlamadaVisitaGestanteActivity;
import com.arsltech.developer.MovisdoApp.activity.PreguntaGestanteActivity;
import com.arsltech.developer.MovisdoApp.model.VisitaGestanteModel;
import com.arsltech.developer.MovisdoApp.provider.MovisdoContract;
import com.arsltech.developer.MovisdoApp.sync.MovisdoSyncAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VisitaGestanteAdapter extends RecyclerView.Adapter<VisitaGestanteAdapter.VisitaGestanteViewHolder> {

    Context context;
    Cursor cursor;
    ArrayList<VisitaGestanteModel> visitaGestanteModelArrayList;
    ArrayList<VisitaGestanteModel> filteredVisitaGestanteArrayList;


    public VisitaGestanteAdapter(Context context, ArrayList<VisitaGestanteModel> visitaGestanteModelArrayList) {
        this.context = context;
        this.visitaGestanteModelArrayList = visitaGestanteModelArrayList;
        this.filteredVisitaGestanteArrayList=visitaGestanteModelArrayList;

    }

    public static final class VisitaGestanteViewHolder extends RecyclerView.ViewHolder{
        TextView tvGestante;
        TextView tvModVisita;
        TextView tvCoordenada;
        TextView tvFecha;

        ImageView imageTash;

        public VisitaGestanteViewHolder(View itemView) {
            super(itemView);
            tvGestante=itemView.findViewById(R.id.textUser);
            tvModVisita=itemView.findViewById(R.id.textModVisita);
            tvCoordenada=itemView.findViewById(R.id.textCoordenada);
            tvFecha=itemView.findViewById(R.id.textFecha);
            imageTash=itemView.findViewById(R.id.imageTrash);
        }
    }

    @Override
    public VisitaGestanteAdapter.VisitaGestanteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.visita_layout,parent,false);

        return new VisitaGestanteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VisitaGestanteAdapter.VisitaGestanteViewHolder holder, int position) {
        holder.tvGestante.setText(filteredVisitaGestanteArrayList.get(position).getEncuestadoDisplayName());
        holder.tvModVisita.setText(filteredVisitaGestanteArrayList.get(position).getMod_visita());
        holder.tvCoordenada.setText(filteredVisitaGestanteArrayList.get(position).getLatitud()+", "+filteredVisitaGestanteArrayList.get(position).getLongitud());
        try {
            Date dateToFormat=new SimpleDateFormat("yyyy-MM-dd").parse(filteredVisitaGestanteArrayList.get(position).getFecha());
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String dateFormated = dateFormat.format(dateToFormat);
            holder.tvFecha.setText(dateFormated);

        } catch (ParseException e) {
            Toast.makeText(context,"Formato de fecha inválido",Toast.LENGTH_SHORT).show();
        }

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

                        values.put(MovisdoContract.VisitaGestanteColumns.PENDIENTE_ELIMINACION, "1");

                        String [] id_visita_gestante=new String[]{filteredVisitaGestanteArrayList.get(position).id};
                        context.getContentResolver().update(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TVISITA_GESTANTE),values,
                                MovisdoContract.VisitaGestanteColumns.ID_REMOTA+" =?", id_visita_gestante);
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

        ItemAnimation.animateLeftRight(holder.itemView, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(filteredVisitaGestanteArrayList.get(position).getMod_visita().contains("Presencial")){
                    Intent intent_pregunta=new Intent(context, PreguntaGestanteActivity.class);
                    intent_pregunta.putExtra("id_visita_gestante",filteredVisitaGestanteArrayList.get(position).getId());
                    intent_pregunta.putExtra("id_gestante",filteredVisitaGestanteArrayList.get(position).getId_gestante());
                    context.startActivity(intent_pregunta);
                }

                if(filteredVisitaGestanteArrayList.get(position).getMod_visita().contains("Remoto")){

                    final BottomSheetDialog bt=new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
                    View newView= LayoutInflater.from(context).inflate(R.layout.layout_bottom_sheet,null);

                    //Variable para obtener permisos de llamada
                    final int REQUEST_CALL = 1;
                    //String para recibir el telefono de la base de datos
                    String celular;
                    //Url para consultar telefono de infante
                    String url_gestante = "http://kuwayo.com/bdmovisdoAPI/gestante_row.php";


                    TextView tvFecha=newView.findViewById(R.id.textFecha);
                    TextView tvLatitud=newView.findViewById(R.id.textLatitud);
                    TextView tvLongitud=newView.findViewById(R.id.textLongitud);
                    TextView tvModalidad=newView.findViewById(R.id.textModalidad);
                    TextView tvCelular=newView.findViewById(R.id.textCelular);
                    String url_llamadas = "http://kuwayo.com/bdmovisdoAPI/create_visita_llamada_gestante.php";


                    LinearLayout linLayLlamada=newView.findViewById(R.id.linLayLlamada);
                    LinearLayout linLayPregunta=newView.findViewById(R.id.linLayPregunta);
                    LinearLayout linlayLlamadas=newView.findViewById(R.id.linLayLlamadas);

                    //Llenamos parametros
                    try {
                        Date dateToFormat=new SimpleDateFormat("yyyy-MM-dd").parse(filteredVisitaGestanteArrayList.get(position).getFecha());
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String dateFormated = dateFormat.format(dateToFormat);
                        tvFecha.setText(dateFormated);

                    } catch (ParseException e) {
                        Toast.makeText(context,"Formato de fecha inválido",Toast.LENGTH_SHORT).show();
                    }

                    tvLatitud.setText("Latitud: "+filteredVisitaGestanteArrayList.get(position).getLatitud());
                    tvLongitud.setText("Longitud: "+filteredVisitaGestanteArrayList.get(position).getLongitud());
                    tvModalidad.setText(filteredVisitaGestanteArrayList.get(position).getMod_visita());
                    tvCelular.setText(ReturnPhoneData(filteredVisitaGestanteArrayList.get(position).getId_gestante()));

                    //ReceiveEncuestadoPhoneData(url_gestante,filteredVisitaGestanteArrayList.get(position).getId(),tvCelular);
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
                                    context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

                                }
                            }
                            /*
                            Intent intent_llamada=new Intent(context, LlamadaVisitaGestanteActivity.class);
                            intent_llamada.putExtra("id_visita_gestante",filteredVisitaGestanteArrayList.get(position).getId());
                            context.startActivity(intent_llamada);

                             */
                        }
                    });
                    linLayPregunta.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent_pregunta=new Intent(context, PreguntaGestanteActivity.class);
                            intent_pregunta.putExtra("id_visita_gestante",filteredVisitaGestanteArrayList.get(position).getId());
                            intent_pregunta.putExtra("id_gestante",filteredVisitaGestanteArrayList.get(position).getId_gestante());
                            context.startActivity(intent_pregunta);
                        }
                    });
                    linlayLlamadas.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            InsertarLlamada(url_llamadas,filteredVisitaGestanteArrayList.get(position).getId(),tvCelular);
                            Intent intent_llamadas=new Intent(context, LlamadaVisitaGestanteActivity.class);
                            intent_llamadas.putExtra("id_visita_gestante",filteredVisitaGestanteArrayList.get(position).getId());
                            intent_llamadas.putExtra("gestanteDisplayName",filteredVisitaGestanteArrayList.get(position).getEncuestadoDisplayName());
                            context.startActivity(intent_llamadas);


                        }
                    });

                }

            }
        });


    }
    public ArrayList<VisitaGestanteModel> ConvertCursorToArray(Cursor data, String gestanteDisplayName) {
        cursor = data;
        String fecha;
        String mod_visita;
        String latitud;
        String longitud;
        String id_gestante;
        String id_visita_gestante_remoto;

        ArrayList<VisitaGestanteModel> arrayResult = new ArrayList<VisitaGestanteModel>();

        for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {

            id_visita_gestante_remoto = data.getString(6);
            fecha = data.getString(1);
            mod_visita = data.getString(2);
            latitud = data.getString(3);
            longitud = data.getString(4);
            id_gestante = data.getString(5);

            VisitaGestanteModel visitaGestanteModel;
            visitaGestanteModel=new VisitaGestanteModel(id_visita_gestante_remoto,fecha,mod_visita,latitud,
                    longitud,id_gestante,gestanteDisplayName);

            arrayResult.add(visitaGestanteModel);
        }
        ActualizarConstructor(arrayResult);
        return arrayResult;

    }

    private void ActualizarConstructor(ArrayList<VisitaGestanteModel> data){
        visitaGestanteModelArrayList=data;
        filteredVisitaGestanteArrayList=data;
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

    //llenamos el TextViewCelular con el celular del apoderado
    private void ReceiveEncuestadoPhoneData(String url_gestante,String id_visita_gestante,TextView tvCelular){
        //realizamos consulta para obtener telefono
        StringRequest request = new StringRequest(Request.Method.POST, url_gestante, new Response.Listener<String>() {
            @Override
            public void onResponse(String ServerResponse) {

                try {
                    //Dado que solo recibimos un objeto ponemos JSONObject de lo contrario sería JSONArray
                    JSONObject json = new JSONObject(ServerResponse);
                    String success=json.optString("success");

                    if(success.matches("1")){
                        tvCelular.setText(json.optString("celular"));
                    }
                    else{
                        Toast.makeText(context, success, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            //Mandamos parámetro para la consulta
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("id_visita_gestante",id_visita_gestante);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private String ReturnPhoneData(String id_gestante) {
        String phoneNumber = "";
        try {
            Uri uri;
            uri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TGESTANTE_PHONE_DETAILS);
            String selection = MovisdoContract.TGESTANTE+"."+MovisdoContract.GestanteColumns.ID_REMOTA+" =?";
            String[] selectionArgs = {id_gestante};
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
    private void InsertarLlamada(String url_llamada, String id_visita_gestante,TextView tvCelular){
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
                        values.put(MovisdoContract.LlamadaVisitaGestanteColumns.DATOS_LLAMADA, datos_llamada);
                        values.put(MovisdoContract.LlamadaVisitaGestanteColumns.DURACION, duracion);
                        values.put(MovisdoContract.LlamadaVisitaGestanteColumns.ID_VISITA_GESTANTE, id_visita_gestante);
                        values.put(MovisdoContract.LlamadaVisitaGestanteColumns.PENDIENTE_INSERCION, 1);

                        context.getContentResolver().insert(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TLLAMADA_VISITA_GESTANTE), values);
                        MovisdoSyncAdapter.sincronizarAhora(context, true);

                        //insertCallToDatabase(url_llamada,datos_llamada,duracion,id_visita_gestante);


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

    private void insertCallToDatabase(String url_llamada,String datos_llamada,String duracion, String id_visita_gestante){
        StringRequest request = new StringRequest(Request.Method.POST, url_llamada, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();

                params.put("datos_llamada",datos_llamada);
                params.put("duracion",duracion);
                params.put("id_visita_gestante",id_visita_gestante);
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }


    @Override
    public int getItemCount() {
        return filteredVisitaGestanteArrayList.size();
    }

    public Filter getFilterModalidad(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String key=charSequence.toString();
                if(key.isEmpty()){
                    filteredVisitaGestanteArrayList=visitaGestanteModelArrayList;
                }
                else {
                    ArrayList<VisitaGestanteModel> filteredResult=new ArrayList<>();
                    for (VisitaGestanteModel row: visitaGestanteModelArrayList){
                        if(row.getMod_visita().toLowerCase().contains(key.toLowerCase())){
                            filteredResult.add(row);
                        }
                    }
                    filteredVisitaGestanteArrayList=filteredResult;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=filteredVisitaGestanteArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredVisitaGestanteArrayList=(ArrayList<VisitaGestanteModel>)filterResults.values;
                notifyDataSetChanged();

            }
        };
    }
}
