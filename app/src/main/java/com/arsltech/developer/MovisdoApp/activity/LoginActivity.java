package com.arsltech.developer.MovisdoApp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import com.arsltech.developer.MovisdoApp.utils.GestorDeSesiones;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    /*Declaramos variables globales de clase*/
    private static final int REQUEST_CALL = 1;
    EditText ed_username,ed_password;
    String str_username,str_password;
    String url = "http://10.0.2.2/bdmovisdoAPI/login.php";
    String url2 = "http://192.168.0.103/bdmovisdoAPI/login.php";
    String url3 = "http://kuwayo.com/bdmovisdoAPI/login.php";//Url para conectarnos al server

    private final int ASSET_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);//mostramos la interfaz de login, cargando nuestro xml

        ed_username = findViewById(R.id.ed_username);
        ed_password = findViewById(R.id.ed_password);

        String[] registro= GestorDeSesiones.VerificarRegistro(getApplicationContext());

        if(registro[2]!=""){
            CompletarCampos(registro[0],registro[1]);
        }

    }

    private void requestLocationPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) &&
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE) &&
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CALL_LOG) &&
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALL_LOG)
        ) {
            new AlertDialog.Builder(this)
                    .setTitle("Permiso necesario")
                    .setMessage("Este permiso es necesario para poder acceder a su ubicaión")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.CALL_PHONE,
                                    Manifest.permission.WRITE_CALL_LOG,
                                    Manifest.permission.READ_CALL_LOG,}, ASSET_REQUEST_CODE);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.WRITE_CALL_LOG,
                    Manifest.permission.READ_CALL_LOG,}, ASSET_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if (requestCode == ASSET_REQUEST_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[3] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[4] == PackageManager.PERMISSION_GRANTED
            ) {

                Boolean result=ValidarCampos();
                if(result){
                    String[] registro=GestorDeSesiones.VerificarRegistro(getApplicationContext());
                    if(ValidacionOffline(ed_username,ed_password,registro)){


                        IniciarMenu(registro[2]);
                    }
                    else {
                        VerificarCredenciales(url3,ed_username,ed_password);
                    }

                }
            }
            else {
                Toast.makeText(this, "Permisos denegados",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean ValidarCampos(){
        Boolean result;
        if(ed_username.getText().toString().equals("")){
            Toast.makeText(this, "Ingrese un usuario", Toast.LENGTH_SHORT).show();
            result=false;
        }
        else if(ed_password.getText().toString().equals("")){
            Toast.makeText(this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
            result=false;
        }
        else{
            result=true;
        }
        return result;
    }



    private void CompletarCampos(String username, String password){
        ed_username.setText(username);
        ed_password.setText(password);
    }

    private void AutorizarRecursos(){
        if (    ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(LoginActivity.this, new String[]
                    {Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.WRITE_CALL_LOG,
                    }, ASSET_REQUEST_CODE);
        } else {
            requestLocationPermissions();
        }
    }

    private void IniciarMenu(String id_promotor){

        /*Llamada a la otra actividad*/
        Intent intent=new Intent(getApplicationContext(), MenuActivity.class);
        //pasamos PK
        intent.putExtra("id_promotor",id_promotor);

        //llamamos a la otra actividad
        startActivity(intent);

        finish();
    }



    private void VerificarCredenciales(String url, EditText ed_username, EditText ed_password){
        //Progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Por favor espere..");
        progressDialog.show();


        str_username = ed_username.getText().toString().trim();
        str_password = ed_password.getText().toString().trim();

        //Consulta a bd para el login

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String ServerResponse) {
                progressDialog.dismiss();
                try {
                    JSONObject json = new JSONObject(ServerResponse);
                    String message=json.optString("message");

                    if(message.matches("Login Successful")){

                        //Captura de PK de promotor usuario
                        String id = json.optString("id");

                        GestorDeSesiones.GuardarPreferencias(ed_username,ed_password,id,getApplicationContext());

                        /*Llamada a la otra actividad*/
                        Intent intent=new Intent(getApplicationContext(), MenuActivity.class);
                        //pasamos PK
                        intent.putExtra("id_promotor",id);

                        //llamamos a la otra actividad
                        startActivity(intent);

                        finish();

                        //Modal de confirmación del php
                        Toast.makeText(LoginActivity.this, "Modo online activado", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
        )
                //Paso de parámetros
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("username",str_username);
                params.put("password",str_password);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(request);
    }

    private boolean ValidacionOffline(EditText ed_username, EditText ed_password, String[]registro){

        if(registro[2]!=""){
            if (ed_username.getText().toString().matches(registro[0]) && ed_password.getText().toString().matches(registro[1])){
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }

    }
    public void Login(View view) {

        AutorizarRecursos();

    }


/*
    public void moveToRegistration(View view) {
        startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
        finish();
    }

 */
}
