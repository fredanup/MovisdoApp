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

import com.arsltech.developer.MovisdoApp.adapter.VisitaInfanteAdapter;

import com.arsltech.developer.MovisdoApp.model.VisitaInfanteModel;
import com.arsltech.developer.MovisdoApp.provider.MovisdoContract;
import com.arsltech.developer.MovisdoApp.sync.MovisdoSyncAdapter;
import com.arsltech.developer.MovisdoApp.utils.GestorDeSesiones;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;


public class VisitaInfanteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor>  {

    //Variables
    //Recyclerview contenedor de items
    RecyclerView dataContainer;

    //Adaptador de recycler manejar su comportamiento
    VisitaInfanteAdapter visitaInfanteAdapter;

    //Texto para filtrar resultados
    EditText searchView;

    //texto capturado al escribir
    CharSequence search="";

    //labels contadoras
    TextView visitaInfanteCount,remoteCount,localCount;

    //Textview para toolbar
    TextView toolBarTitle;

    ArrayList<VisitaInfanteModel> visitaInfanteModelArrayList;



    /* Variables para recibir contenido de nuestro menu
        -DrawerLayout contenedor principal de toda la página
        -Contenedor del menú de navegación
     */

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    /*Variable infante + getter and setter*/
    String id_infante,infante_displayName;

    public String getId_infante() {
        return id_infante;
    }

    public void setId_infante(String id_infante) {
        this.id_infante = id_infante;
    }

    /*variable infantedata + getter and setter*/

    public String getInfante_displayName() {
        return infante_displayName;
    }

    public void setInfante_displayName(String infante_displayName) {
        this.infante_displayName = infante_displayName;
    }

    /*Carga de datos en recyclerview*/
    private void setVisitaInfanteAdapter(ArrayList<VisitaInfanteModel> visitaInfanteModelList, RecyclerView dataContainer){

        dataContainer=findViewById(R.id.dataContainer);
        //Instancia de layoutmanager
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(VisitaInfanteActivity.this,RecyclerView.VERTICAL,false);
        //Esablecemos el layoutmanayer al contenedor asignado
        dataContainer.setLayoutManager(layoutManager);
        //instancia del adaptador y llenado de constructor
        visitaInfanteAdapter=new VisitaInfanteAdapter(VisitaInfanteActivity.this,visitaInfanteModelList);
        //llenamos el contenedor con el adaptador ya nstanciado
        dataContainer.setAdapter(visitaInfanteAdapter);
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
        setContentView(R.layout.menu_visita_infante_layout);

        //Captura de elementos del xml
        searchView=findViewById(R.id.search_bar);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        visitaInfanteCount=findViewById(R.id.visitCount);
        remoteCount=findViewById(R.id.remoteVisitCount);
        localCount=findViewById(R.id.localVisitCount);
        toolBarTitle=findViewById(R.id.textToolBar);
        //Cambiamos etiqueta de nuestro toolbar
        toolBarTitle.setText("Visita a infante");

        //uso del elemento toolbar como nuestro actionbar
        setSupportActionBar(toolbar);

        //recibimos pk de promotor y lo guardamos
        setId_infante(getIntent().getStringExtra("id_infante"));//recibimos el id del infante
        setInfante_displayName(getIntent().getStringExtra("infanteDisplayName"));//recibimos data del infante

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
        navigationView.setNavigationItemSelectedListener(VisitaInfanteActivity.this);

        //Seeccionamos por defecto home
        navigationView.setCheckedItem(R.id.nav_parent);


        //inicializamos array
        visitaInfanteModelArrayList=new ArrayList<>();

        //Llenamos el adaptador con el contenido que tenga el arraylist
        setVisitaInfanteAdapter(visitaInfanteModelArrayList,dataContainer);

        //Búsqueda en recyclerview
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                visitaInfanteAdapter.getFilterModalidad().filter(charSequence);
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
                Intent intent_parent=new Intent(VisitaInfanteActivity.this, MenuActivity.class);
                //pasamos PK
                intent_parent.putExtra("id_promotor",id_promotor[2]);
                //llamamos a la otra actividad
                startActivity(intent_parent);
                break;
            case R.id.nav_children:
                /*Llamada a la otra actividad*/
                Intent intent_child=new Intent(VisitaInfanteActivity.this, InfantePromotorActivity.class);
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
        visitaInfanteAdapter.getFilterModalidad().filter("Remoto");
    }

    public void filterLocalVisit(View view) {
        visitaInfanteAdapter.getFilterModalidad().filter("Presencial");
    }

    public void filterAll(View view) {
        //Llenamos el adaptador con el contenido que tenga el arraylist
        setVisitaInfanteAdapter(visitaInfanteModelArrayList,dataContainer);
    }

    public void syncOnClick(View view) {

        MovisdoSyncAdapter.sincronizarAhora(this, false);
    }

    //Creamos nueva visita
    public void btn_add_visita_infante(View view) {

        Intent i = new Intent(VisitaInfanteActivity.this, VisitaInfanteCreateActivity.class);
        i.putExtra("id_infante",id_infante);
        startActivity(i);

    }

    //recibimos hilo de ejecucion y actualizamos consulta
    @Override
    protected void onResume() {
        super.onResume();
        visitaInfanteAdapter.notifyDataSetChanged();

    }

    @NonNull
    @NotNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable @org.jetbrains.annotations.Nullable Bundle args) {

        String selection = MovisdoContract.VisitaInfanteColumns.ID_INFANTE+" =? AND "+MovisdoContract.VisitaInfanteColumns.PENDIENTE_ELIMINACION+" =0";
        String[] selectionArgs=new String[]{getId_infante()};
        String sortOrder="CAST (" + MovisdoContract.VisitaInfanteColumns.ID_REMOTA + " AS INTEGER)";

        CursorLoader dataLoader = new CursorLoader(
                this,
                MovisdoContract.GET_CONTENT_URI(MovisdoContract.TVISITA_INFANTE),
                null, selection, selectionArgs, sortOrder);

        return  dataLoader;
    }


    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<Cursor> loader, Cursor data) {
        visitaInfanteModelArrayList=visitaInfanteAdapter.ConvertCursorToArray(data,getInfante_displayName());
        ContadorDeRegistros(data,visitaInfanteCount,remoteCount,localCount);
    }

    private void ContadorDeRegistros(Cursor data, TextView visitaInfanteCount, TextView remoteCount, TextView localCount) {
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

        visitaInfanteCount.setText(totalCount);
        remoteCount.setText(String.valueOf(remoteCountNumber));
        localCount.setText(String.valueOf(localCountNumber));
    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<Cursor> loader) {
        visitaInfanteAdapter.ConvertCursorToArray(null,getInfante_displayName());
    }
}
