package com.arsltech.developer.MovisdoApp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arsltech.developer.MovisdoApp.R;
import com.arsltech.developer.MovisdoApp.adapter.EncuestadoAdapter;
import com.arsltech.developer.MovisdoApp.adapter.InfanteAdapter;
import com.arsltech.developer.MovisdoApp.model.EncuestadoModel;
import com.arsltech.developer.MovisdoApp.model.InfanteModel;
import com.arsltech.developer.MovisdoApp.provider.MovisdoContract;
import com.arsltech.developer.MovisdoApp.sync.MovisdoSyncAdapter;
import com.arsltech.developer.MovisdoApp.utils.GestorDeSesiones;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InfantePromotorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , LoaderManager.LoaderCallbacks<Cursor> {

    //Variables
    //Recyclerview contenedor de items
    RecyclerView dataContainer;

    //Adaptador de recycler manejar su comportamiento
    InfanteAdapter infanteAdapter;

    //Texto para filtrar resultados
    EditText searchView;

    //texto capturado al escribir
    CharSequence search;

    //labels contadoras
    TextView infanteCount,recienNacidoCount,menorDe6MesesCount,de6a11mesesCount,de1a3añosCount;

    //Textview para toolbar
    TextView toolBarTitle;

    //Contadores para filtros de infantes
    int totalCountNumber,recienNacidoCountNumber,menorDe6MesesCountNumber,de6a11mesesCountNumber,de1a3añosCountNumber;

    //Arraylist que contiene objetos infante
    ArrayList<InfanteModel> infanteModelArrayList;

    //url de infante
    String url_infante = "http://kuwayo.com/bdmovisdoAPI/infante_promotor.php";

    //atributos
    InfanteModel infanteModel;

    /* Variables para recibir contenido de nuestro menu
       -DrawerLayout contenedor principal de toda la página
       -Contenedor del menú de navegación
    */

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    /*Variable infante + getter and setter*/



    /*Carga de datos en recyclerview*/
    private void setInfanteAdapter(ArrayList<InfanteModel> infanteModelArrayList, RecyclerView dataContainer){

        dataContainer=findViewById(R.id.dataContainer);
        //Instancia de layoutmanager
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(InfantePromotorActivity.this,RecyclerView.VERTICAL,false);
        //Esablecemos el layoutmanayer al contenedor asignado
        dataContainer.setLayoutManager(layoutManager);
        //instancia del adaptador y llenado de constructor
        infanteAdapter=new InfanteAdapter(InfantePromotorActivity.this,infanteModelArrayList);
        //llenamos el contenedor con el adaptador ya nstanciado
        dataContainer.setAdapter(infanteAdapter);
    }

    @Override
    public void setSupportActionBar(Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
    }

    /*Método principal que inicializa la actividad*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_infante_layout);
        //Captura de xml principal
        searchView=findViewById(R.id.search_bar);

        //Captura de elementos del xml
        searchView=findViewById(R.id.search_bar);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        infanteCount=findViewById(R.id.infanteCount);
        recienNacidoCount=findViewById(R.id.recienNacidoCount);
        menorDe6MesesCount=findViewById(R.id.menorDe6MesesCount);
        de6a11mesesCount=findViewById(R.id.de6a11MesesCount);
        de1a3añosCount=findViewById(R.id.de1a3AñosCount);

        toolBarTitle=findViewById(R.id.textToolBar);
        //Cambiamos etiqueta de nuestro toolbar
        toolBarTitle.setText("Infantes");

        //uso del elemento toolbar como nuestro actionbar
        setSupportActionBar(toolbar);

        getSupportLoaderManager().initLoader(0,null,this);
        MovisdoSyncAdapter.inicializarSyncAdapter(this);

        //Ocultamos opcion de menu offline
        Menu menu=navigationView.getMenu();
        menu.findItem(R.id.nav_offline).setVisible(false);

        //Traemos al frente al menú para que capture nuestros eventos
        navigationView.bringToFront();

        //Evento al hacer click en toolbar muestra el menú
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Habilitamos el poder hacer click en el menú
        navigationView.setNavigationItemSelectedListener(InfantePromotorActivity.this);

        //Seeccionamos por defecto home
        navigationView.setCheckedItem(R.id.nav_children);


        //inicializamos array
        infanteModelArrayList=new ArrayList<>();

        //Llenamos el adaptador con el contenido que tenga el arraylist
        setInfanteAdapter(infanteModelArrayList,dataContainer);


        //Búsqueda en recyclerview
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                infanteAdapter.getFilterNombre().filter(charSequence);
                search=charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }


    //Llamada a base de datos
    public void retrieveChildrenData(String id_promotor){
        StringRequest request=new StringRequest(Request.Method.POST, url_infante,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        infanteModelArrayList.clear();
                        try {
                            JSONObject jsonObject1=new JSONObject(response);
                            String success=jsonObject1.getString("success");
                            JSONArray jsonArray=jsonObject1.getJSONArray("tinfante");
                            if(success.equals("1")){
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject2=jsonArray.getJSONObject(i);
                                    String id=jsonObject2.getString("id");
                                    String nombre=jsonObject2.getString("nombre");
                                    String apPaterno=jsonObject2.getString("apPaterno");
                                    String apMaterno=jsonObject2.getString("apMaterno");
                                    String dni=jsonObject2.getString("dni");
                                    String fecha_nacimiento=jsonObject2.getString("fecha_nacimiento");
                                    String sexo=jsonObject2.getString("sexo");
                                    String estab_salud=jsonObject2.getString("estab_salud");
                                    String prematuro=jsonObject2.getString("prematuro");
                                    String categoria=jsonObject2.getString("categoria");
                                    String id_encuestado=jsonObject2.getString("id_encuestado");

                                    infanteModel=new InfanteModel(id,nombre,apPaterno,apMaterno,dni,fecha_nacimiento,sexo,estab_salud,prematuro,categoria,id_encuestado);

                                    infanteModelArrayList.add(infanteModel);
                                    //Contamos padres y gestantes
                                    if(infanteModel.getCategoria().equals("Recién nacido")){
                                        recienNacidoCountNumber++;
                                    }
                                    if(infanteModel.getCategoria().equals("Menor de 6 meses")){
                                        menorDe6MesesCountNumber++;
                                    }
                                    if(infanteModel.getCategoria().equals("De 6 a 11 meses")){
                                        de6a11mesesCountNumber++;
                                    }
                                    if(infanteModel.getCategoria().equals("De 1 a 3 años")){
                                        de1a3añosCountNumber++;
                                    }
                                    infanteAdapter.notifyDataSetChanged();

                                }
                                //Llenamos etiqueta de total,padres y gestantes
                                infanteCount.setText(String.valueOf(infanteModelArrayList.size()));
                                recienNacidoCount.setText(String.valueOf(String.valueOf(recienNacidoCountNumber)));
                                menorDe6MesesCount.setText(String.valueOf(String.valueOf(menorDe6MesesCountNumber)));
                                de6a11mesesCount.setText(String.valueOf(String.valueOf(de6a11mesesCountNumber)));
                                de1a3añosCount.setText(String.valueOf(String.valueOf(de1a3añosCountNumber)));
                            }

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InfantePromotorActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("id_promotor",id_promotor);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    //Cuando se hace click en back
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    //Método que se genera por el evento setNavigationItemSelectedListener
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Menu menu=navigationView.getMenu();
        String[] id_promotor= GestorDeSesiones.VerificarRegistro(getApplicationContext());
        switch (menuItem.getItemId()){
            case R.id.nav_parent:

                /*Llamada a la otra actividad*/
                Intent intent_parent=new Intent(InfantePromotorActivity.this, MenuActivity.class);
                //pasamos PK
                intent_parent.putExtra("id_promotor",id_promotor[2]);
                //llamamos a la otra actividad
                startActivity(intent_parent);
                break;

            case R.id.nav_logout:

                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                //finalizamos la activada menu
                finish();
                break;

            case R.id.nav_online:

                menu.findItem(R.id.nav_offline).setVisible(true);
                menu.findItem(R.id.nav_online).setVisible(false);
                break;
            case R.id.nav_offline:

                menu.findItem(R.id.nav_offline).setVisible(false);
                menu.findItem(R.id.nav_online).setVisible(true);
                break;

        }
        //Cuando se haga click en algún item del menú este se minimice
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    public void filterAll(View view) {
        //Llenamos el adaptador con el contenido que tenga el arraylist
        setInfanteAdapter(infanteModelArrayList,dataContainer);
    }

    public void filterRecienNacido(View view) {
        infanteAdapter.getFilterCategoria().filter("Recién nacido");
    }

    public void filterMenorDe6Meses(View view) {
        infanteAdapter.getFilterCategoria().filter("Menor de 6 meses");
    }

    public void filterDe6a11Meses(View view) {
        infanteAdapter.getFilterCategoria().filter("De 6 a 11 meses");
    }

    public void filterDe1a3años(View view) {
        infanteAdapter.getFilterCategoria().filter("De 1 a 3 años");
    }

    public void syncOnClick(View view) {

        MovisdoSyncAdapter.sincronizarAhora(this, false);
    }


    @NonNull
    @NotNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable @org.jetbrains.annotations.Nullable Bundle args) {
        String[] id_promotor= GestorDeSesiones.VerificarRegistro(getApplicationContext());
        String selection = MovisdoContract.TENCUESTADO+"."+MovisdoContract.EncuestadoColumns.ID_PROMOTOR+" =?";
        String[] projection=new String[]{
                MovisdoContract.TINFANTE+"."+MovisdoContract.InfanteColumns._ID,
                MovisdoContract.TINFANTE+"."+MovisdoContract.InfanteColumns.NOMBRE,
                MovisdoContract.TINFANTE+"."+MovisdoContract.InfanteColumns.APPATERNO,
                MovisdoContract.TINFANTE+"."+MovisdoContract.InfanteColumns.APMATERNO,
                MovisdoContract.TINFANTE+"."+MovisdoContract.InfanteColumns.DNI,
                MovisdoContract.TINFANTE+"."+MovisdoContract.InfanteColumns.FECHA_NACIMIENTO,
                MovisdoContract.TINFANTE+"."+MovisdoContract.InfanteColumns.SEXO,
                MovisdoContract.TINFANTE+"."+MovisdoContract.InfanteColumns.ESTAB_SALUD,
                MovisdoContract.TINFANTE+"."+MovisdoContract.InfanteColumns.PREMATURO,
                MovisdoContract.TINFANTE+"."+MovisdoContract.InfanteColumns.CATEGORIA,
                MovisdoContract.TINFANTE+"."+MovisdoContract.InfanteColumns.ID_ENCUESTADO,
                MovisdoContract.TINFANTE+"."+MovisdoContract.InfanteColumns.ID_REMOTA,
                MovisdoContract.TINFANTE+"."+MovisdoContract.InfanteColumns.ESTADO,
                MovisdoContract.TINFANTE+"."+MovisdoContract.InfanteColumns.PENDIENTE_INSERCION,
        };
        String[] selectionArgs=new String[]{id_promotor[2]};
        String sortOrder="CAST (" + MovisdoContract.TINFANTE+"."+MovisdoContract.InfanteColumns.ID_REMOTA + " AS INTEGER)";

        CursorLoader dataLoader = new CursorLoader(
                this,
                MovisdoContract.GET_CONTENT_URI(MovisdoContract.TENCUESTADO_PHONE_DETAILS),
                projection, selection, selectionArgs, sortOrder);

        return  dataLoader;

    }

    private void ContadorDeRegistros(Cursor data, TextView infanteCount,TextView recienNacidoCount, TextView menorDe6MesesCount, TextView de6a11mesesCount, TextView de1a3añosCount){

        String totalCount=String.valueOf(data.getCount());

        //Contadores para filtros de infantes
        int recienNacidoCountNumber=0;
        int menorDe6MesesCountNumber=0;
        int de6a11mesesCountNumber=0;
        int de1a3añosCountNumber=0;

        for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
            // The Cursor is now set to the right position
            if (data.getString(9).matches("Recién nacido")){
                recienNacidoCountNumber=recienNacidoCountNumber+1;
            }
            if (data.getString(9).matches("Menor de 6 meses")){
                menorDe6MesesCountNumber=menorDe6MesesCountNumber+1;
            }
            if (data.getString(9).matches("De 6 a 11 meses")){
                de6a11mesesCountNumber=de6a11mesesCountNumber+1;
            }
            if (data.getString(9).matches("De 1 a 3 años")){
                de1a3añosCountNumber=de1a3añosCountNumber+1;
            }
        }

        infanteCount.setText(totalCount);
        recienNacidoCount.setText(String.valueOf(recienNacidoCountNumber));
        menorDe6MesesCount.setText(String.valueOf(menorDe6MesesCountNumber));
        de6a11mesesCount.setText(String.valueOf(de6a11mesesCountNumber));
        de1a3añosCount.setText(String.valueOf(de1a3añosCountNumber));

    }

    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<Cursor> loader, Cursor data) {
        infanteModelArrayList=infanteAdapter.ConvertCursorToArray(data);
        ContadorDeRegistros(data,infanteCount,recienNacidoCount,menorDe6MesesCount,de6a11mesesCount,de1a3añosCount);
    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<Cursor> loader) {
        infanteAdapter.ConvertCursorToArray(null);
    }
}
