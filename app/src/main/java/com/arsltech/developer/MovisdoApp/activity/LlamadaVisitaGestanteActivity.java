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
import com.arsltech.developer.MovisdoApp.adapter.LlamadaVisitaGestanteAdapter;

import com.arsltech.developer.MovisdoApp.adapter.LlamadaVisitaInfanteAdapter;
import com.arsltech.developer.MovisdoApp.model.LlamadaVisitaGestanteModel;

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

public class LlamadaVisitaGestanteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor>  {

    //Variables
    RecyclerView dataContainer;

    //Adaptador de recycler manejar su comportamiento
    LlamadaVisitaGestanteAdapter llamadaVisitaGestanteAdapter;

    //Texto para filtrar resultados
    EditText searchView;

    //texto capturado al escribir
    CharSequence search;

    //Textview para toolbar
    TextView toolBarTitle;

    //labels contadoras
    TextView callCount,outcomingCount,incomingCount;

    //contador de llamadas salientes y entrantes
    int outcomingCountNumber,intcomingCountNumber;

    ArrayList<LlamadaVisitaGestanteModel> llamadaVisitaGestanteModelArrayList;

    String url_llamada_visita_gestante = "http://kuwayo.com/bdmovisdoAPI/llamada_visita_gestante.php";

    LlamadaVisitaGestanteModel llamadaVisitaGestanteModel;

    /* Variables para recibir contenido de nuestro menu
        -DrawerLayout contenedor principal de toda la página
        -Contenedor del menú de navegación
     */

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    /*Variable promotor + getter and setter*/
    String id_visita_gestante, gestanteDisplayName;


    public String getId_visita_gestante() {
        return id_visita_gestante;
    }

    public void setId_visita_gestante(String id_visita_gestante) {
        this.id_visita_gestante = id_visita_gestante;
    }

    public String getGestanteDisplayName() {
        return gestanteDisplayName;
    }

    public void setGestanteDisplayName(String gestanteDisplayName) {
        this.gestanteDisplayName = gestanteDisplayName;
    }


    /*Carga de datos en recyclerview*/
    private void setLlamadaVisitaGestanteAdapter(ArrayList<LlamadaVisitaGestanteModel> llamadaVisitaGestanteModelArrayList, RecyclerView dataContainer){

        dataContainer=findViewById(R.id.dataContainer);
        //Instancia de layoutmanager
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(LlamadaVisitaGestanteActivity.this,RecyclerView.VERTICAL,false);
        //Esablecemos el layoutmanayer al contenedor asignado
        dataContainer.setLayoutManager(layoutManager);
        //instancia del adaptador y llenado de constructor
        llamadaVisitaGestanteAdapter=new LlamadaVisitaGestanteAdapter(LlamadaVisitaGestanteActivity.this,llamadaVisitaGestanteModelArrayList);
        //llenamos el contenedor con el adaptador ya nstanciado
        dataContainer.setAdapter(llamadaVisitaGestanteAdapter);
        //Mejoramos performance de recyclerview
//        dataContainer.setHasFixedSize(true);
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
        setContentView(R.layout.menu_llamada_layout);

        //Captura de elementos del xml
        searchView=findViewById(R.id.search_bar);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        callCount=findViewById(R.id.callCount);
        outcomingCount=findViewById(R.id.outcomingCount);
        incomingCount=findViewById(R.id.incomingCount);

        toolBarTitle=findViewById(R.id.textToolBar);
        //Cambiamos etiqueta de nuestro toolbar
        toolBarTitle.setText("Llamadas");

        //uso del elemento toolbar como nuestro actionbar
        setSupportActionBar(toolbar);

        //recibimos pk de promotor y lo guardamos
        setId_visita_gestante(getIntent().getStringExtra("id_visita_gestante"));//recibimos el id de visita_gestante
        setGestanteDisplayName(getIntent().getStringExtra("gestanteDisplayName"));

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
        navigationView.setNavigationItemSelectedListener(LlamadaVisitaGestanteActivity.this);

        //Seeccionamos por defecto home
        navigationView.setCheckedItem(R.id.nav_parent);

        //inicializamos array
        llamadaVisitaGestanteModelArrayList=new ArrayList<>();

        //Llenamos el adaptador con el contenido que tenga el arraylist
        setLlamadaVisitaGestanteAdapter(llamadaVisitaGestanteModelArrayList,dataContainer);

        //Búsqueda en recyclerview
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //llamadaVisitagestanteAdapter.getFilterNombre().filter(charSequence);
                search=charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    //Llamada a base de datos
    public void retrieveLlamadasVisitaGestanteData(String id_visita_gestante){
        StringRequest request=new StringRequest(Request.Method.POST, url_llamada_visita_gestante,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        llamadaVisitaGestanteModelArrayList.clear();
                        try {

                            JSONObject jsonObject1=new JSONObject(response);
                            String success=jsonObject1.getString("success");
                            if(success.equals("1")){
                                JSONArray jsonArray=jsonObject1.getJSONArray("tllamada_visita_gestante");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject2=jsonArray.getJSONObject(i);
                                    String id=jsonObject2.getString("id");
                                    String datos_llamada=jsonObject2.getString("datos_llamada");
                                    String duracion=jsonObject2.getString("duracion");
                                    String id_visita_gestante=jsonObject2.getString("id_visita_gestante");
                                    String gestante_data=getGestanteDisplayName();


                                    llamadaVisitaGestanteModel=new LlamadaVisitaGestanteModel(id,datos_llamada,duracion,id_visita_gestante,gestante_data,null);
                                    llamadaVisitaGestanteModelArrayList.add(llamadaVisitaGestanteModel);
                                    //Contamos padres y gestantes
                                    /*
                                    if(llamadaVisitagestanteModel.getTipoLlamada().equals("Entrante")){
                                        intcomingCountNumber++;
                                    }
                                    else {
                                        outcomingCountNumber++;
                                    }

                                     */

                                    llamadaVisitaGestanteAdapter.notifyDataSetChanged();

                                }
                                //Llenamos etiqueta de total,entrantes y salientes
                                callCount.setText(String.valueOf(llamadaVisitaGestanteModelArrayList.size()));
                                incomingCount.setText(String.valueOf(String.valueOf(intcomingCountNumber)));
                                outcomingCount.setText(String.valueOf(String.valueOf(outcomingCountNumber)));
                            }

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LlamadaVisitaGestanteActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("id_visita_gestante",id_visita_gestante);
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
        String[] id_promotor= GestorDeSesiones.VerificarRegistro(getApplicationContext());
        switch (menuItem.getItemId()){
            case R.id.nav_parent:
                /*Llamada a la otra actividad*/
                Intent intent_parent=new Intent(LlamadaVisitaGestanteActivity.this, MenuActivity.class);
                //pasamos PK
                intent_parent.putExtra("id_promotor",id_promotor[2]);
                //llamamos a la otra actividad
                startActivity(intent_parent);
                break;
            case R.id.nav_children:
                /*Llamada a la otra actividad*/
                Intent intent_child=new Intent(LlamadaVisitaGestanteActivity.this, InfantePromotorActivity.class);
                //pasamos PK
                intent_child.putExtra("id_promotor",id_promotor[2]);
                //llamamos a la otra actividad
                startActivity(intent_child);
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

    public void syncOnClick(View view) {

        MovisdoSyncAdapter.sincronizarAhora(this, false);
    }

    @NonNull
    @NotNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable @org.jetbrains.annotations.Nullable Bundle args) {
        String selection = MovisdoContract.LlamadaVisitaGestanteColumns.ID_VISITA_GESTANTE+" =?";
        String[] selectionArgs=new String[]{getId_visita_gestante()};
        String sortOrder="CAST (" + MovisdoContract.LlamadaVisitaGestanteColumns.ID_REMOTA + " AS INTEGER)";

        CursorLoader dataLoader = new CursorLoader(
                this,
                MovisdoContract.GET_CONTENT_URI(MovisdoContract.TLLAMADA_VISITA_GESTANTE),
                null, selection, selectionArgs, sortOrder);

        return  dataLoader;
    }

    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<Cursor> loader, Cursor data) {
        llamadaVisitaGestanteModelArrayList=llamadaVisitaGestanteAdapter.ConvertCursorToArray(data,getGestanteDisplayName());
        ContadorDeRegistros(data,callCount,outcomingCount,incomingCount);
    }

    private void ContadorDeRegistros(Cursor data, TextView callCount, TextView outcomingCount, TextView intcomingCount) {
        String totalCount=String.valueOf(data.getCount());

        //Contadores para filtros de infantes
        int outcomingCountNumber=0;
        int intcomingCountNumber=0;

        callCount.setText(totalCount);
        outcomingCount.setText(totalCount);
        intcomingCount.setText(String.valueOf(0));
    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<Cursor> loader) {
        llamadaVisitaGestanteAdapter.ConvertCursorToArray(null,getGestanteDisplayName());
    }
}
