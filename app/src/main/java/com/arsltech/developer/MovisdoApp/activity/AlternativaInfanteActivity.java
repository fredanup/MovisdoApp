package com.arsltech.developer.MovisdoApp.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import com.arsltech.developer.MovisdoApp.R;
import com.arsltech.developer.MovisdoApp.adapter.AlternativaInfanteAdapter;
import com.arsltech.developer.MovisdoApp.model.AlternativaInfanteModel;
import com.arsltech.developer.MovisdoApp.model.RespuestaInfanteModel;
import com.arsltech.developer.MovisdoApp.provider.MovisdoContract;
import com.arsltech.developer.MovisdoApp.sync.MovisdoSyncAdapter;
import com.arsltech.developer.MovisdoApp.utils.GestorDeSesiones;
import com.arsltech.developer.MovisdoApp.utils.Utilidades;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AlternativaInfanteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    //Variables
    //Recyclerview contenedor de items
    RecyclerView dataContainer;

    //Adaptador de recycler manejar su comportamiento
    AlternativaInfanteAdapter alternativaInfanteAdapter;

    //Arraylist que contiene objetos infante
    ArrayList<AlternativaInfanteModel> alternativaInfanteModelArrayList;

    //Textview para toolbar
    TextView toolBarTitle;

    TextView tvPregunta,tvArea,tvSugTemporal;

    EditText etDetalle;

    Button btnGuardar;

    /* Variables para recibir contenido de nuestro menu
       -DrawerLayout contenedor principal de toda la página
       -Contenedor del menú de navegación
    */

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    String id_visita_infante,id_pregunta,pregunta,detalle,area,sug_temporal, estado, id_infante;

    public String getId_visita_infante() {
        return id_visita_infante;
    }

    public void setId_visita_infante(String id_visita_infante) {
        this.id_visita_infante = id_visita_infante;
    }

    public String getId_pregunta() {
        return id_pregunta;
    }

    public void setId_pregunta(String id_pregunta) {
        this.id_pregunta = id_pregunta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSug_temporal() {
        return sug_temporal;
    }

    public void setSug_temporal(String sug_temporal) {
        this.sug_temporal = sug_temporal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getId_infante() {
        return id_infante;
    }

    public void setId_infante(String id_infante) {
        this.id_infante = id_infante;
    }

    /*Carga de datos en recyclerview*/
    private void setAlternativaInfanteAdapter(ArrayList<AlternativaInfanteModel> alternativaInfanteModelArrayList,RecyclerView dataContainer){

        dataContainer=findViewById(R.id.dataContainer);
        //Instancia de layoutmanager
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(AlternativaInfanteActivity.this,RecyclerView.VERTICAL,false);
        //Esablecemos el layoutmanayer al contenedor asignado
        dataContainer.setLayoutManager(layoutManager);
        //instancia del adaptador y llenado de constructor
        alternativaInfanteAdapter=new AlternativaInfanteAdapter(AlternativaInfanteActivity.this,alternativaInfanteModelArrayList);

        //llenamos el contenedor con el adaptador ya nstanciado
        dataContainer.setAdapter(alternativaInfanteAdapter);
        //Mejoramos performance de recyclerview
//        dataContainer.setHasFixedSize(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_alternativa_layout);

        //Captura de elementos del xml
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        tvPregunta=findViewById(R.id.textPregunta);
        tvArea=findViewById(R.id.textArea);
        tvSugTemporal=findViewById(R.id.textSugTemporal);
        etDetalle=findViewById(R.id.textDetalle);
        toolBarTitle=findViewById(R.id.textToolBar);
        btnGuardar=findViewById(R.id.btn_save);
        //Cambiamos etiqueta de nuestro toolbar
        toolBarTitle.setText("Responda la pregunta...");

        //uso del elemento toolbar como nuestro actionbar
        setSupportActionBar(toolbar);

        //recibimos pk de promotor y lo guardamos
        setId_pregunta(getIntent().getStringExtra("id_pregunta")); //recibimos el id del promotor
        setId_visita_infante(getIntent().getStringExtra("id_visita_infante"));
        setPregunta(getIntent().getStringExtra("pregunta"));
        setArea(getIntent().getStringExtra("area"));
        setSug_temporal(getIntent().getStringExtra("sug_temporal"));
        setEstado(getIntent().getStringExtra("estado"));
        setId_infante(getIntent().getStringExtra("id_infante"));

        tvPregunta.setText(getPregunta());
        tvArea.setText(getArea());
        tvSugTemporal.setText(getSug_temporal());
        if(getEstado().matches("Contestada")){
            ArrayList<RespuestaInfanteModel> retrieveAnswers=RetrieveAllChoosenAnswers(getId_infante(),getId_pregunta());
            etDetalle.setText(retrieveAnswers.get(0).getDetalle());
            btnGuardar.setText("Actualizar");

        }

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
        navigationView.setNavigationItemSelectedListener(AlternativaInfanteActivity.this);

        //Seeccionamos por defecto home
        navigationView.setCheckedItem(R.id.nav_parent);

        getSupportLoaderManager().initLoader(0,null,this);
        MovisdoSyncAdapter.inicializarSyncAdapter(this);

        alternativaInfanteModelArrayList=new ArrayList<>();

        //Llenamos el adaptador con el contenido que tenga el arraylist
        setAlternativaInfanteAdapter(alternativaInfanteModelArrayList,dataContainer);


    }

    public void syncOnClick(View view) {

        MovisdoSyncAdapter.sincronizarAhora(this, false);
    }

    /**
     * Obtiene alternativas elegidas de la pregunta ? del infante con id ?
     * @param id_infante
     * @param id_pregunta
     * @return
     */
    public ArrayList <RespuestaInfanteModel> RetrieveAllChoosenAnswers(String id_infante,String id_pregunta) {
        ArrayList<RespuestaInfanteModel> retrieveAnswers=new ArrayList<RespuestaInfanteModel>();
        String id="";
        String id_alternativa="";
        String id_visita_infante="";
        String detalle="";
        try {
            Uri uri;
            //Unión de alternativa con respuesta, obtiene todas las alternativas que fueron seleccionadas como respuestas de todas las visitas cuyo infante tiene el ID?
            uri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TALTERNATIVA_ELEGIDA_DETAILS);
            //Mostrar solo el campo detalle
            String[] projeccion = new String[]{MovisdoContract.TRESPUESTA_INFANTE+"."+MovisdoContract.RespuestaInfanteColumns.ID_REMOTA
                    ,MovisdoContract.TRESPUESTA_INFANTE+"."+MovisdoContract.RespuestaInfanteColumns.ID_ALTERNATIVA
                    ,MovisdoContract.TRESPUESTA_INFANTE+"."+MovisdoContract.RespuestaInfanteColumns.ID_VISITA_INFANTE
                    ,MovisdoContract.TRESPUESTA_INFANTE+"."+MovisdoContract.RespuestaInfanteColumns.DETALLE};
            //Donde el id_infante sea ? y la pregunta sea ?
            String selection = MovisdoContract.TVISITA_INFANTE+"."+MovisdoContract.VisitaInfanteColumns.ID_INFANTE+" =? AND "+
                    MovisdoContract.TALTERNATIVA+"."+MovisdoContract.AlternativaColumns.ID_PREGUNTA+" =?";
            String[] selectionArgs = {id_infante,id_pregunta};
            Cursor c = getContentResolver().query(
                    uri,
                    projeccion,
                    selection,
                    selectionArgs,
                    null);

            while (c.moveToNext()) {
                id=c.getString(0);
                id_alternativa=c.getString(1);
                id_visita_infante=c.getString(2);
                detalle=c.getString(3);
                RespuestaInfanteModel respuestaInfanteModel=new RespuestaInfanteModel(id,id_alternativa,id_visita_infante,detalle);
                retrieveAnswers.add(respuestaInfanteModel);
            }
            c.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return retrieveAnswers;

    }

    @NonNull
    @NotNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable @org.jetbrains.annotations.Nullable Bundle args) {

        //Alternativas de la pregunta con id =?
            String selection = MovisdoContract.AlternativaColumns.ID_PREGUNTA+" =?";
            String[] selectionArgs=new String[]{getId_pregunta()};
            String sortOrder="CAST (" + MovisdoContract.AlternativaColumns.ID_REMOTA + " AS INTEGER)";

            CursorLoader dataLoader = new CursorLoader(
                    this,
                    MovisdoContract.GET_CONTENT_URI(MovisdoContract.TALTERNATIVA),
                    null, selection, selectionArgs, sortOrder);

            return  dataLoader;

    }

    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<Cursor> loader, Cursor data) {
        alternativaInfanteModelArrayList=alternativaInfanteAdapter.ConvertCursorToArray(data,getId_visita_infante(),getId_infante());

    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<Cursor> loader) {
        alternativaInfanteAdapter.ConvertCursorToArray(null,getId_visita_infante(),getId_infante());

    }



    public void btn_save_answers(View view) {

        ArrayList<String> alternativasElegidas;
        alternativasElegidas=alternativaInfanteAdapter.getAlternativaElegidaArrayList();

        ArrayList<RespuestaInfanteModel> retrieveAnswers=new ArrayList<>();
        retrieveAnswers=RetrieveAllChoosenAnswers(getId_infante(),getId_pregunta());

        int nroRespuestas=retrieveAnswers.size();

        if(nroRespuestas>0 || alternativasElegidas.size()>0){


            setDetalle(etDetalle.getText().toString().trim());
            ContentValues values;
            if(getEstado().matches("Contestada")){

                for (RespuestaInfanteModel answer :
                        retrieveAnswers) {
                    values = new ContentValues();
                    String[]selectionArgs=new String[]{answer.getId()};
                    values.put(MovisdoContract.RespuestaInfanteColumns.PENDIENTE_ELIMINACION, "1");
                    getContentResolver().update(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_INFANTE),values,
                            MovisdoContract.RespuestaInfanteColumns.ID_REMOTA+" =?", selectionArgs);

                }
                for (String respuesta :
                        alternativasElegidas) {


                    values = new ContentValues();
                    values.put(MovisdoContract.RespuestaInfanteColumns.ID_ALTERNATIVA, respuesta);
                    values.put(MovisdoContract.RespuestaInfanteColumns.ID_VISITA_INFANTE, getId_visita_infante());
                    values.put(MovisdoContract.RespuestaInfanteColumns.DETALLE, getDetalle());
                    values.put(MovisdoContract.RespuestaInfanteColumns.PENDIENTE_INSERCION, 1);


                    getContentResolver().insert(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_INFANTE), values);
                    MovisdoSyncAdapter.sincronizarAhora(this, true);

                }
            }
            else {
                for (String respuesta :
                        alternativasElegidas) {
                    values = new ContentValues();
                    values.put(MovisdoContract.RespuestaInfanteColumns.ID_ALTERNATIVA, respuesta);
                    values.put(MovisdoContract.RespuestaInfanteColumns.ID_VISITA_INFANTE, getId_visita_infante());
                    values.put(MovisdoContract.RespuestaInfanteColumns.DETALLE, getDetalle());
                    values.put(MovisdoContract.RespuestaInfanteColumns.PENDIENTE_INSERCION, 1);
                    getContentResolver().insert(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_INFANTE), values);
                    MovisdoSyncAdapter.sincronizarAhora(this, true);
                }
            }

            if (Utilidades.materialDesign())
                finishAfterTransition();
            else {

                finish();
            }



        }
        else {
            Toast.makeText(getApplicationContext(),"No se seleccionó alguna alternativa",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem menuItem) {
        Menu menu=navigationView.getMenu();
        String[] id_promotor= GestorDeSesiones.VerificarRegistro(getApplicationContext());
        switch (menuItem.getItemId()){
            case R.id.nav_parent:

                /*Llamada a la otra actividad*/
                Intent intent_parent=new Intent(AlternativaInfanteActivity.this, MenuActivity.class);
                //pasamos PK
                intent_parent.putExtra("id_promotor",id_promotor[2]);
                //llamamos a la otra actividad
                startActivity(intent_parent);
                break;
            case R.id.nav_children:
                /*Llamada a la otra actividad*/
                Intent intent_child=new Intent(AlternativaInfanteActivity.this, InfantePromotorActivity.class);
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


}
