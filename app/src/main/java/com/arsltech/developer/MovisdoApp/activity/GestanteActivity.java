package com.arsltech.developer.MovisdoApp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import com.arsltech.developer.MovisdoApp.adapter.GestanteAdapter;
import com.arsltech.developer.MovisdoApp.adapter.InfanteAdapter;
import com.arsltech.developer.MovisdoApp.model.EncuestadoModel;
import com.arsltech.developer.MovisdoApp.model.GestanteModel;
import com.arsltech.developer.MovisdoApp.provider.MovisdoContract;
import com.arsltech.developer.MovisdoApp.sync.MovisdoSyncAdapter;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GestanteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {


    //Variables
    //Recyclerview contenedor de items
    RecyclerView dataContainer;

    //Adaptador de recycler manejar su comportamiento
    GestanteAdapter gestanteAdapter;

    //Texto para filtrar resultados
    EditText searchView;

    //texto capturado al escribir
    CharSequence search;

    //labels contadoras
    TextView genderCount,maleCount,femaleCount;

    //Textview para toolbar
    TextView toolBarTitle;

    //contador de varones y de mujeres
    int maleCountNumber,femaleCountNumber;

    //Arraylist que contiene objetos gestante
    ArrayList<GestanteModel> gestanteModelArrayList;

    String url_gestant = "http://kuwayo.com/bdmovisdoAPI/gestante.php";

    //Infante Gestante
    GestanteModel gestanteModel;

    /* Variables para recibir contenido de nuestro menu
        -DrawerLayout contenedor principal de toda la página
        -Contenedor del menú de navegación
     */

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    /*Variable encuestado + getter and setter*/
    String id_encuestado,encuestadoDisplayName,id_promotor;

    public String getId_promotor() {
        return id_promotor;
    }

    public void setId_promotor(String id_promotor) {
        this.id_promotor = id_promotor;
    }

    public String getId_encuestado() {
        return id_encuestado;
    }

    public void setId_encuestado(String id_encuestado) {
        this.id_encuestado = id_encuestado;
    }

    public String getEncuestadoDisplayName() {
        return encuestadoDisplayName;
    }

    public void setEncuestadoDisplayName(String encuestadoDisplayName) {
        this.encuestadoDisplayName = encuestadoDisplayName;
    }


    /*Carga de datos en recyclerview*/
    private void setGestanteAdapter(ArrayList<GestanteModel> gestanteModelList,RecyclerView dataContainer){

        dataContainer=findViewById(R.id.dataContainer);
        //Instancia de layoutmanager
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(GestanteActivity.this,RecyclerView.VERTICAL,false);
        //Esablecemos el layoutmanayer al contenedor asignado
        dataContainer.setLayoutManager(layoutManager);
        //instancia del adaptador y llenado de constructor
        gestanteAdapter=new GestanteAdapter(GestanteActivity.this,gestanteModelList);
        //llenamos el contenedor con el adaptador ya nstanciado
        dataContainer.setAdapter(gestanteAdapter);
        //Mejoramos performance de recyclerview
        //dataContainer.setHasFixedSize(true);
    }

    @Override
    public void setSupportActionBar(Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
    }

    /*Método principal que inicializa la actividad*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Captura de xml principal
        setContentView(R.layout.menu_gestante_layout);

        //Captura de elementos del xml
        searchView=findViewById(R.id.search_bar);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        genderCount=findViewById(R.id.genderCount);
        maleCount=findViewById(R.id.maleCount);
        femaleCount=findViewById(R.id.femaleCount);

        toolBarTitle=findViewById(R.id.textToolBar);

        //Cambiamos etiqueta de nuestro toolbar
        toolBarTitle.setText("Gestaciones");

        //uso del elemento toolbar como nuestro actionbar
        setSupportActionBar(toolbar);

        //recibimos pk de encuestado
        setId_encuestado(getIntent().getStringExtra("id_encuestado"));//recibimos el id del encuestado

        //Recibimos datos de gestante
        setEncuestadoDisplayName(getIntent().getStringExtra("encuestadoDisplayName"));

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
        navigationView.setNavigationItemSelectedListener(GestanteActivity.this);

        //Seeccionamos por defecto home
        navigationView.setCheckedItem(R.id.nav_parent);


        //inicializamos array
        gestanteModelArrayList=new ArrayList<>();


        //Llenamos el adaptador con el contenido que tenga el arraylist
        setGestanteAdapter(gestanteModelArrayList,dataContainer);


        //Búsqueda en recyclerview
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                gestanteAdapter.getFilterGender().filter(charSequence);
                search=charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }


    //Llamada a base de datos
    public void retrieveGestantData(String id_encuestado){
        StringRequest request=new StringRequest(Request.Method.POST, url_gestant,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        gestanteModelArrayList.clear();
                        try {
                            JSONObject jsonObject1=new JSONObject(response);
                            String success=jsonObject1.getString("success");
                            JSONArray jsonArray=jsonObject1.getJSONArray("tgestante");
                            if(success.equals("1")){
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject2=jsonArray.getJSONObject(i);
                                    String id=jsonObject2.getString("id");
                                    String fecha_parto=jsonObject2.getString("fecha_parto");
                                    String estab_salud=jsonObject2.getString("estab_salud");
                                    String sexo_bebe=jsonObject2.getString("sexo_bebe");
                                    String id_encuestado=jsonObject2.getString("id_encuestado");
                                    String encuestadoDisplayName=getEncuestadoDisplayName();

                                    gestanteModel=new GestanteModel(id,fecha_parto,estab_salud,sexo_bebe,id_encuestado,encuestadoDisplayName);
                                    gestanteModelArrayList.add(gestanteModel);
                                    //Contamos padres y gestantes
                                    if(gestanteModel.getSexo_bebe().equals("Masculino")){
                                        maleCountNumber++;
                                    }
                                    else {
                                        femaleCountNumber++;
                                    }

                                    gestanteAdapter.notifyDataSetChanged();

                                }

                                //Llenamos etiqueta de total,femenino y masculino
                                genderCount.setText(String.valueOf(gestanteModelArrayList.size()));
                                femaleCount.setText(String.valueOf(String.valueOf(femaleCountNumber)));
                                maleCount.setText(String.valueOf(String.valueOf(maleCountNumber)));
                            }

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GestanteActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("id_encuestado",id_encuestado);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    //Cuando se hace click en back
    @Override
    public void onBackPressed() {
        //Hacer click en atrás es como hacer un toggle en el menú
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
        switch (menuItem.getItemId()){

            case R.id.nav_parent:
                /*Llamada a la otra actividad*/
                Intent intent_parent=new Intent(GestanteActivity.this, MenuActivity.class);
                //pasamos PK
                intent_parent.putExtra("id_promotor",id_promotor);
                //llamamos a la otra actividad
                startActivity(intent_parent);
                break;

            case R.id.nav_children:
                /*Llamada a la otra actividad*/
                Intent intent_child=new Intent(GestanteActivity.this, InfantePromotorActivity.class);
                //pasamos PK
                intent_child.putExtra("id_promotor",id_promotor);
                //llamamos a la otra actividad
                startActivity(intent_child);
                break;
            case R.id.nav_online:

                menu.findItem(R.id.nav_offline).setVisible(true);
                menu.findItem(R.id.nav_online).setVisible(false);
                break;
            case R.id.nav_offline:

                menu.findItem(R.id.nav_offline).setVisible(false);
                menu.findItem(R.id.nav_online).setVisible(true);
                break;
            case R.id.nav_logout:

                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                //finalizamos la activada menu
                finish();
                break;


        }
        //Cuando se haga click en algún item del menú este se minimice
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    public void filterAll(View view) {
        //Llenamos el adaptador con el contenido que tenga el arraylist
        setGestanteAdapter(gestanteModelArrayList,dataContainer);
    }

    public void filterMale(View view) {
        gestanteAdapter.getFilterGender().filter("Masculino");
    }

    public void filterFemale(View view) {
        gestanteAdapter.getFilterGender().filter("Femenino");
    }

    public void syncOnClick(View view) {

        MovisdoSyncAdapter.sincronizarAhora(this, false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable @org.jetbrains.annotations.Nullable Bundle args) {
        String selection = MovisdoContract.GestanteColumns.ID_ENCUESTADO+" =?";
        String[] selectionArgs=new String[]{getId_encuestado()};
        String sortOrder="CAST (" + MovisdoContract.GestanteColumns.ID_REMOTA + " AS INTEGER)";

        CursorLoader dataLoader = new CursorLoader(
                this,
                MovisdoContract.GET_CONTENT_URI(MovisdoContract.TGESTANTE),
                null, selection, selectionArgs, sortOrder);

        return  dataLoader;
    }

    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<Cursor> loader, Cursor data) {
        gestanteModelArrayList=gestanteAdapter.ConvertCursorToArray(data,getEncuestadoDisplayName());
        ContadorDeRegistros(data,genderCount,femaleCount,maleCount);
    }

    private void ContadorDeRegistros(Cursor data, TextView genderCount,TextView femaleCount, TextView maleCount){

        String totalCount=String.valueOf(data.getCount());

        //Contadores para filtros de infantes
        int femaleCountNumber=0;
        int maleCountNumber=0;

        for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
            // The Cursor is now set to the right position
            if (data.getString(3).matches("0")){
                femaleCountNumber=femaleCountNumber+1;
            }
            if (data.getString(3).matches("1")){
                maleCountNumber=maleCountNumber+1;
            }

        }

        genderCount.setText(totalCount);
        femaleCount.setText(String.valueOf(femaleCountNumber));
        maleCount.setText(String.valueOf(maleCountNumber));

    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<Cursor> loader) {
        gestanteAdapter.ConvertCursorToArray(null,getEncuestadoDisplayName());
    }
}
