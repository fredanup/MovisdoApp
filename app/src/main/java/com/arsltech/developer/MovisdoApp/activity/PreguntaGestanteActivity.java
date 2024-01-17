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
import com.arsltech.developer.MovisdoApp.adapter.PreguntaGestanteAdapter;
import com.arsltech.developer.MovisdoApp.adapter.PreguntaInfanteAdapter;
import com.arsltech.developer.MovisdoApp.model.PreguntaGestanteModel;
import com.arsltech.developer.MovisdoApp.model.PreguntaInfanteModel;
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

public class PreguntaGestanteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor>  {

    //Variables
    //Recyclerview contenedor de items
    RecyclerView dataContainer;

    //Adaptador de recycler manejar su comportamiento
    PreguntaGestanteAdapter preguntaGestanteAdapter;

    //Texto para filtrar resultados
    EditText searchView;

    //texto capturado al escribir
    CharSequence search;

    //labels contadoras
    TextView questionCount,answeredCount,pendientCount;

    //Textview para toolbar
    TextView toolBarTitle;

    ArrayList<PreguntaGestanteModel> preguntaGestanteModelArrayList;


    /* Variables para recibir contenido de nuestro menu
        -DrawerLayout contenedor principal de toda la página
        -Contenedor del menú de navegación
     */
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    /*Variable id_visita_gestante, id_gestante + getter and setter*/
    String id_visita_gestante,id_gestante;

    public String getId_visita_gestante() {
        return id_visita_gestante;
    }

    public void setId_visita_gestante(String id_visita_gestante) {
        this.id_visita_gestante = id_visita_gestante;
    }

    /*getter and setter de id_gestante*/
    public String getId_gestante() {
        return id_gestante;
    }

    public void setId_gestante(String id_gestante) {
        this.id_gestante = id_gestante;
    }


    /*Carga de datos en recyclerview*/
    private void setPreguntaGestanteAdapter(ArrayList<PreguntaGestanteModel> preguntaGestanteModelArrayList, RecyclerView dataContainer){

        dataContainer=findViewById(R.id.dataContainer);
        //Instancia de layoutmanager
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(PreguntaGestanteActivity.this,RecyclerView.VERTICAL,false);
        //Esablecemos el layoutmanayer al contenedor asignado
        dataContainer.setLayoutManager(layoutManager);
        //instancia del adaptador y llenado de constructor
        preguntaGestanteAdapter=new PreguntaGestanteAdapter(PreguntaGestanteActivity.this,preguntaGestanteModelArrayList);
        //llenamos el contenedor con el adaptador ya nstanciado
        dataContainer.setAdapter(preguntaGestanteAdapter);
        //Mejoramos performance de recyclerview
//        dataContainer.setHasFixedSize(true);
    }

    @Override
    public void setSupportActionBar(Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Captura de xml principal
        setContentView(R.layout.menu_pregunta_gestante_layout);

        //Captura de elementos del xml
        searchView=findViewById(R.id.search_bar);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        questionCount=findViewById(R.id.questionCount);
        answeredCount=findViewById(R.id.answeredCount);
        pendientCount=findViewById(R.id.pendientCount);

        toolBarTitle=findViewById(R.id.textToolBar);
        //Cambiamos etiqueta de nuestro toolbar
        toolBarTitle.setText("Preguntas de gestante");

        //uso del elemento toolbar como nuestro actionbar
        setSupportActionBar(toolbar);

        //recibimos pk de visita_gestante y lo guardamos
        setId_visita_gestante(getIntent().getStringExtra("id_visita_gestante"));//recibimos el id de visita_gestante

        //recibimos pk de gestante y lo guardamos
        setId_gestante(getIntent().getStringExtra("id_gestante"));//recibimos el id de gestante

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
        navigationView.setNavigationItemSelectedListener(PreguntaGestanteActivity.this);

        //Seeccionamos por defecto home
        navigationView.setCheckedItem(R.id.nav_parent);

        //inicializamos array
        preguntaGestanteModelArrayList=new ArrayList<>();

        //Llenamos el adaptador con el contenido que tenga el arraylist
        setPreguntaGestanteAdapter(preguntaGestanteModelArrayList,dataContainer);


        //Búsqueda en recyclerview
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                preguntaGestanteAdapter.getFilterPregunta().filter(charSequence);
                search=charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
                Intent intent_parent=new Intent(PreguntaGestanteActivity.this, MenuActivity.class);
                //pasamos PK
                intent_parent.putExtra("id_promotor",id_promotor[2]);
                //llamamos a la otra actividad
                startActivity(intent_parent);
                break;
            case R.id.nav_children:
                /*Llamada a la otra actividad*/
                Intent intent_child=new Intent(PreguntaGestanteActivity.this, InfantePromotorActivity.class);
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

    public void filterAnswered(View view) {
        preguntaGestanteAdapter.getFilterStatus().filter("Contestada");
    }

    public void filterAll(View view) {
        //Llenamos el adaptador con el contenido que tenga el arraylist
        setPreguntaGestanteAdapter(preguntaGestanteModelArrayList,dataContainer);
    }

    public void filterPendient(View view) {
        preguntaGestanteAdapter.getFilterStatus().filter("Sin contestar");
    }


    public void syncOnClick(View view) {

        MovisdoSyncAdapter.sincronizarAhora(this, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        preguntaGestanteAdapter.notifyDataSetChanged();

    }

    @NonNull
    @NotNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable @org.jetbrains.annotations.Nullable Bundle args) {
        String selection = MovisdoContract.PreguntaColumns.CATEGORIA+" =?";
        String categoria="Gestante";
        String[] selectionArgs=new String[]{categoria};
        String sortOrder="CAST (" + MovisdoContract.PreguntaColumns.ID_REMOTA + " AS INTEGER)";

        CursorLoader dataLoader = new CursorLoader(
                this,
                MovisdoContract.GET_CONTENT_URI(MovisdoContract.TPREGUNTA),
                null, selection, selectionArgs, sortOrder);

        return  dataLoader;
    }

    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<Cursor> loader, Cursor data) {
        preguntaGestanteModelArrayList=preguntaGestanteAdapter.ConvertCursorToArray(data,getId_gestante(),getId_visita_gestante());
        ContadorDeRegistros(preguntaGestanteModelArrayList,questionCount,answeredCount,pendientCount);
    }

    private void ContadorDeRegistros(ArrayList<PreguntaGestanteModel> preguntaGestanteModelArrayList, TextView questionCount, TextView answeredCount, TextView pendientCount) {
        String totalCount=String.valueOf(preguntaGestanteModelArrayList.size());

        //Contadores para filtros de infantes
        int answeredCountNumber=0;
        int pendientCountNumber=0;

        for (PreguntaGestanteModel preguntaGestanteModel:
                preguntaGestanteModelArrayList) {
            if(preguntaGestanteModel.getEstado().matches("Contestada")){
                answeredCountNumber++;
            }
            if(preguntaGestanteModel.getEstado().matches("Sin contestar")){
                pendientCountNumber++;
            }
        }

        questionCount.setText(totalCount);
        answeredCount.setText(String.valueOf(answeredCountNumber));
        pendientCount.setText(String.valueOf(pendientCountNumber));
    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<Cursor> loader) {
        preguntaGestanteAdapter.ConvertCursorToArray(null,getId_gestante(),getId_visita_gestante());
    }
}
