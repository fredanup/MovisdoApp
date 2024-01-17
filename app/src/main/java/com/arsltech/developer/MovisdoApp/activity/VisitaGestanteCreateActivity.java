package com.arsltech.developer.MovisdoApp.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arsltech.developer.MovisdoApp.R;
import com.arsltech.developer.MovisdoApp.provider.MovisdoContract;
import com.arsltech.developer.MovisdoApp.sync.MovisdoSyncAdapter;
import com.arsltech.developer.MovisdoApp.utils.Utilidades;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class VisitaGestanteCreateActivity extends AppCompatActivity {


    /**
     * Variables
     */
    TextView tvLatitud, tvLongitud, tvFechaActual;
    LocationManager locationManager;

    String current_date;
    String latitud, longitud;
    Switch sw_mod_visita;
    String mod_visita = "Presencial";
    String id_gestante;

    public String getId_gestante() {
        return id_gestante;
    }

    public void setId_gestante(String id_gestante) {
        this.id_gestante = id_gestante;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visita_gestante_create_view);
        setId_gestante(getIntent().getStringExtra("id_gestante"));//reciibmos pk de gestante
        //captura de elementos xml

        tvLatitud = findViewById(R.id.tv_latitud);
        tvLongitud = findViewById(R.id.tv_longitud);
        sw_mod_visita = (Switch) findViewById(R.id.sw_mod_visita);
        String fecha_actual = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        tvFechaActual = findViewById(R.id.tv_fecha_actual);
        tvFechaActual.setText(fecha_actual);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                setLatitud(String.valueOf(location.getLatitude()));
                setLongitud(String.valueOf(location.getLongitude()));
                tvLatitud.setText(getLatitud());
                tvLongitud.setText(getLongitud());

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    public void btn_save(View view) {

        current_date=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        latitud =getLatitud();
        longitud=getLongitud();
        if (latitud!=null && longitud!=null){
            ContentValues values = new ContentValues();
            values.put(MovisdoContract.VisitaGestanteColumns.FECHA, current_date);
            values.put(MovisdoContract.VisitaGestanteColumns.MOD_VISITA, mod_visita);
            values.put(MovisdoContract.VisitaGestanteColumns.LATITUD, latitud);
            values.put(MovisdoContract.VisitaGestanteColumns.LONGITUD, longitud);
            values.put(MovisdoContract.VisitaGestanteColumns.ID_GESTANTE, getId_gestante());
            values.put(MovisdoContract.VisitaInfanteColumns.PENDIENTE_ELIMINACION, 0);
            values.put(MovisdoContract.VisitaGestanteColumns.PENDIENTE_INSERCION, 1);

            getContentResolver().insert(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TVISITA_GESTANTE), values);
            MovisdoSyncAdapter.sincronizarAhora(this, true);

            if (Utilidades.materialDesign())
                finishAfterTransition();
            else {
                finish();
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"Cargando ubicación, por favor espere...",Toast.LENGTH_LONG).show();
        }

    }

    public void btn_visita(View view) {

        if(view.getId()==R.id.sw_mod_visita){
            if(sw_mod_visita.isChecked()){
                mod_visita="Remoto";
            }else {
                mod_visita="Presencial";
            }
        }
    }


}
