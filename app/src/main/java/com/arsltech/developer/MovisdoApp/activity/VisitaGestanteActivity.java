package com.arsltech.developer.MovisdoApp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arsltech.developer.MovisdoApp.R;
import com.arsltech.developer.MovisdoApp.adapter.VisitaGestanteAdapter;

import com.arsltech.developer.MovisdoApp.model.VisitaGestanteModel;

import com.arsltech.developer.MovisdoApp.provider.MovisdoContract;
import com.arsltech.developer.MovisdoApp.sync.MovisdoSyncAdapter;
import com.arsltech.developer.MovisdoApp.utils.GestorDeSesiones;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;


public class VisitaGestanteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor>  {

    //Variables
    //Recyclerview contenedor de items
    RecyclerView dataContainer;

    //Adaptador de recycler manejar su comportamiento
    VisitaGestanteAdapter visitaGestanteAdapter;

    //Texto para filtrar resultados
    EditText searchView;

    //texto capturado al escribir
    CharSequence search="";

    //labels contadoras
    TextView visitaGestanteCount,remoteCount,localCount;

    //Textview para toolbar
    TextView toolBarTitle;


    ArrayList<VisitaGestanteModel> visitaGestanteModelArrayList;

    /* Variables para recibir contenido de nuestro menu
           -DrawerLayout contenedor principal de toda la página
           -Contenedor del menú de navegación
        */

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    /*Variable encuestado y gestante + getter and setter*/
    String id_gestante, encuestadoDisplayName;

    public String getId_gestante() {
        return id_gestante;
    }

    public void setId_gestante(String id_gestante) {
        this.id_gestante = id_gestante;
    }

    public String getEncuestadoDisplayName() {
        return encuestadoDisplayName;
    }

    public void setEncuestadoDisplayName(String encuestadoDisplayName) {
        this.encuestadoDisplayName = encuestadoDisplayName;
    }

    /*Carga de datos en recyclerview*/
    private void setVisitaGestanteAdapter(ArrayList<VisitaGestanteModel> visitaGestanteModelList, RecyclerView dataContainer){

        dataContainer=findViewById(R.id.dataContainer);
        //Instancia de layoutmanager
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(VisitaGestanteActivity.this,RecyclerView.VERTICAL,false);
        //Esablecemos el layoutmanayer al contenedor asignado
        dataContainer.setLayoutManager(layoutManager);
        //instancia del adaptador y llenado de constructor
        visitaGestanteAdapter=new VisitaGestanteAdapter(VisitaGestanteActivity.this,visitaGestanteModelList);
        //llenamos el contenedor con el adaptador ya nstanciado
        dataContainer.setAdapter(visitaGestanteAdapter);
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
        setContentView(R.layout.menu_visita_gestante_layout);

        //Captura de elementos del xml
        searchView=findViewById(R.id.search_bar);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        visitaGestanteCount=findViewById(R.id.visitCount);
        remoteCount=findViewById(R.id.remoteVisitCount);
        localCount=findViewById(R.id.localVisitCount);
        toolBarTitle=findViewById(R.id.textToolBar);
        //Cambiamos etiqueta de nuestro toolbar
        toolBarTitle.setText("Visita a gestante");

        //uso del elemento toolbar como nuestro actionbar
        setSupportActionBar(toolbar);

        //recibimos pk de gestante y lo guardamos
        setId_gestante(getIntent().getStringExtra("id_gestante"));//recibimos el id del gestante
        setEncuestadoDisplayName(getIntent().getStringExtra("encuestadoDisplayName"));//recibimos data de encuestado

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
        navigationView.setNavigationItemSelectedListener(VisitaGestanteActivity.this);

        //Seeccionamos por defecto home
        navigationView.setCheckedItem(R.id.nav_parent);

        //inicializamos array
        visitaGestanteModelArrayList=new ArrayList<>();

        //Llenamos el adaptador con el contenido que tenga el arraylist
        setVisitaGestanteAdapter(visitaGestanteModelArrayList,dataContainer);


        //Búsqueda en recyclerview
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                visitaGestanteAdapter.getFilterModalidad().filter(charSequence);
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
                Intent intent_parent=new Intent(VisitaGestanteActivity.this, MenuActivity.class);
                //pasamos PK
                intent_parent.putExtra("id_promotor",id_promotor[2]);
                //llamamos a la otra actividad
                startActivity(intent_parent);
                break;

            case R.id.nav_children:
                /*Llamada a la otra actividad*/
                Intent intent_child=new Intent(VisitaGestanteActivity.this, InfantePromotorActivity.class);
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

    public void filterRemoteVisit(View view) {
        visitaGestanteAdapter.getFilterModalidad().filter("Remoto");
    }

    public void filterLocalVisit(View view) {
        visitaGestanteAdapter.getFilterModalidad().filter("Presencial");
    }

    public void filterAll(View view) {
        //Llenamos el adaptador con el contenido que tenga el arraylist
        setVisitaGestanteAdapter(visitaGestanteModelArrayList,dataContainer);
    }

    public void syncOnClick(View view) {

        MovisdoSyncAdapter.sincronizarAhora(this, false);
    }

    public void btn_add_visita_gestante(View view) {

        Intent i = new Intent(VisitaGestanteActivity.this, VisitaGestanteCreateActivity.class);
        i.putExtra("id_gestante",id_gestante);
        startActivity(i);

    }

    //recibimos hilo de ejecucion y actualizamos consulta
    @Override
    protected void onResume() {
        super.onResume();
        visitaGestanteAdapter.notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable @org.jetbrains.annotations.Nullable Bundle args) {
        String selection = MovisdoContract.VisitaGestanteColumns.ID_GESTANTE+" =? AND "+MovisdoContract.VisitaGestanteColumns.PENDIENTE_ELIMINACION+" =0";
        String[] selectionArgs=new String[]{getId_gestante()};
        String sortOrder="CAST (" + MovisdoContract.VisitaGestanteColumns.ID_REMOTA + " AS INTEGER)";

        CursorLoader dataLoader = new CursorLoader(
                this,
                MovisdoContract.GET_CONTENT_URI(MovisdoContract.TVISITA_GESTANTE),
                null, selection, selectionArgs, sortOrder);

        return  dataLoader;
    }

    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<Cursor> loader, Cursor data) {
        visitaGestanteModelArrayList=visitaGestanteAdapter.ConvertCursorToArray(data,getEncuestadoDisplayName());
        ContadorDeRegistros(data,visitaGestanteCount,remoteCount,localCount);
    }

    private void ContadorDeRegistros(Cursor data, TextView visitaGestanteCount, TextView remoteCount, TextView localCount) {
        String totalCount=String.valueOf(data.getCount());

        //Contadores para filtros de infantes
        int remoteCountNumber=0;
        int localCountNumber=0;

        for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
            // The Cursor is now set to the right position
            if (data.getString(2).matches("Remoto")){
                remoteCountNumber=remoteCountNumber+1;
            }
            if (data.getString(2).matches("Presencial")){
                localCountNumber=localCountNumber+1;
            }

        }

        visitaGestanteCount.setText(totalCount);
        remoteCount.setText(String.valueOf(remoteCountNumber));
        localCount.setText(String.valueOf(localCountNumber));
    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<Cursor> loader) {
        visitaGestanteAdapter.ConvertCursorToArray(null,getEncuestadoDisplayName());
    }
}
