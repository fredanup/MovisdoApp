package com.arsltech.developer.MovisdoApp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.arsltech.developer.MovisdoApp.model.EncuestadoModel;
import com.arsltech.developer.MovisdoApp.provider.MovisdoContract;
import com.arsltech.developer.MovisdoApp.sync.MovisdoSyncAdapter;
import com.arsltech.developer.MovisdoApp.utils.GestorDeSesiones;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor>  {

    //Variables
    //Textview para toolbar
    TextView toolBarTitle;

    //Recyclerview contenedor de items
    RecyclerView dataContainer;

    //Adaptador de recycler manejar su comportamiento
    EncuestadoAdapter encuestadoAdapter;

    //Texto para filtrar resultados
    EditText searchView;

    //texto capturado al escribir
    CharSequence search;

    //Contadores que muestran el total de cada item de la tabla
    TextView totalCount,parentCount,gestantCount;

    //controla el ícono de sincronización
    ImageView syncButton;

    //contador de gestantes y de padres de tipo int
    int gestantCountNumber,parentsCountNumber;

    //Arraylist que contiene objetos de tipo encuestado objeto
    ArrayList<EncuestadoModel> encuestadoModelArrayList;

    //Url de parents
    String url_parents = "http://kuwayo.com/bdmovisdoAPI/encuestado.php";

    //Objeto Encuestado
    EncuestadoModel encuestadoModel;

    /* Variables para recibir contenido de nuestro menu
        -DrawerLayout contenedor principal de toda la página
        -Contenedor del menú de navegación
        -Toolbar
     */
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    /*Variable promotor + getter and setter*/
    String id_promotor;

    //Constructores de id_promotor
    public String getId_promotor() {
        return id_promotor;
    }

    public void setId_promotor(String id_promotor) {
        this.id_promotor = id_promotor;
    }

    /*Carga de datos en recyclerview*/
    private void setEncuestadoAdapter(ArrayList<EncuestadoModel> encuestadoModelArrayList,RecyclerView dataContainer){
        //Captura de contenedor
        dataContainer=findViewById(R.id.dataContainer);
        //Instancia de layoutmanager
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(MenuActivity.this,RecyclerView.VERTICAL,false);
        //Esablecemos el layoutmanayer al contenedor asignado
        dataContainer.setLayoutManager(layoutManager);
        //instancia del adaptador y llenado de constructor
        encuestadoAdapter=new EncuestadoAdapter(MenuActivity.this,encuestadoModelArrayList);
        //llenamos el contenedor con el adaptador ya nstanciado
        dataContainer.setAdapter(encuestadoAdapter);
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
        setContentView(R.layout.menu_parents_layout);

        //Captura de elementos del xml
        searchView=findViewById(R.id.search_bar);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        totalCount=findViewById(R.id.totalCount);
        parentCount=findViewById(R.id.parentsCount);
        gestantCount=findViewById(R.id.gestantCount);
        toolBarTitle=findViewById(R.id.textToolBar);
        syncButton=findViewById(R.id.action_sync);

        //Cambiamos etiqueta de nuestro toolbar
        toolBarTitle.setText("Apoderado y Gestante");

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
        navigationView.setNavigationItemSelectedListener(MenuActivity.this);

        //Seeccionamos por defecto home
        navigationView.setCheckedItem(R.id.nav_parent);

        //inicializamos array
        encuestadoModelArrayList=new ArrayList<>();

        //Llenamos el adaptador con el contenido que tenga el arraylist, que en este caso es vacío posteriormente se llenará
        setEncuestadoAdapter(encuestadoModelArrayList,dataContainer);

        //Búsqueda en recyclerview
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                encuestadoAdapter.getFilterNombre().filter(charSequence);
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

    public void syncOnClick(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Importando base de datos, por favor espere..");
        progressDialog.show();
        MovisdoSyncAdapter.sincronizarAhora(this, false);
        progressDialog.dismiss();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //emptyView.setText("Cargando datos...");
        // Consultar todos los registros
        String []parametrosDeSesion= GestorDeSesiones.VerificarRegistro(getApplicationContext());
        String selection = MovisdoContract.EncuestadoColumns.ID_PROMOTOR+" =?";
        String[] selectionArgs=new String[]{parametrosDeSesion[2]};
        String sortOrder="CAST (" + MovisdoContract.EncuestadoColumns.ID_REMOTA + " AS INTEGER)";

        CursorLoader dataLoader = new CursorLoader(
                this,
                MovisdoContract.GET_CONTENT_URI(MovisdoContract.TENCUESTADO),
                null, selection, selectionArgs, sortOrder);

        return  dataLoader;

    }


    private void ContadorDeRegistros(Cursor data, TextView tvTotal,TextView tvParent, TextView tvGestant){

        String totalCount=String.valueOf(data.getCount());

        int parentsCount = 0,gestantCount = 0;

        for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
            // The Cursor is now set to the right position
            if (data.getString(9).matches("Apoderado")){
                parentsCount=parentsCount+1;
            }
            if (data.getString(9).matches("Gestante")){
                gestantCount=gestantCount+1;
            }
        }

        tvTotal.setText(totalCount);
        tvParent.setText(String.valueOf(parentsCount));
        tvGestant.setText(String.valueOf(gestantCount));

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        encuestadoModelArrayList=encuestadoAdapter.ConvertCursorToArray(data);
        ContadorDeRegistros(data,totalCount,parentCount,gestantCount);

        //emptyView.setText("");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        encuestadoAdapter.ConvertCursorToArray(null);
    }


    //Método que se genera por el evento setNavigationItemSelectedListener
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Menu menu=navigationView.getMenu();
        switch (menuItem.getItemId()){
            case R.id.nav_parent:

                break;
            case R.id.nav_children:
                /*Llamada a la otra actividad*/
                Intent intent=new Intent(MenuActivity.this, InfantePromotorActivity.class);
                //pasamos PK
                intent.putExtra("id_promotor",id_promotor);
                //llamamos a la otra actividad
                startActivity(intent);
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

    //Buscamos apoderados
    public void filterParents(View view) {
        encuestadoAdapter.getFilterCategoria().filter("Apoderado");
    }

    //Buscamos en todos los registros de encuestado
    public void filterAll(View view) {
        //Llenamos el adaptador con el contenido que tenga el arraylist
        setEncuestadoAdapter(encuestadoModelArrayList,dataContainer);


    }

    //Buscamos gestantes
    public void filterGestant(View view) {

        encuestadoAdapter.getFilterCategoria().filter("Gestante");
    }


}
