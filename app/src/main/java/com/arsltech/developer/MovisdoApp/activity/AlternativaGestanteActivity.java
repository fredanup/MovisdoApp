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
import com.arsltech.developer.MovisdoApp.adapter.AlternativaGestanteAdapter;
import com.arsltech.developer.MovisdoApp.model.AlternativaGestanteModel;
import com.arsltech.developer.MovisdoApp.model.RespuestaGestanteModel;
import com.arsltech.developer.MovisdoApp.model.RespuestaInfanteModel;
import com.arsltech.developer.MovisdoApp.provider.MovisdoContract;
import com.arsltech.developer.MovisdoApp.sync.MovisdoSyncAdapter;
import com.arsltech.developer.MovisdoApp.utils.GestorDeSesiones;
import com.arsltech.developer.MovisdoApp.utils.Utilidades;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AlternativaGestanteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    //Variables
    //Recyclerview contenedor de items
    RecyclerView dataContainer;

    //Adaptador de recycler manejar su comportamiento
    AlternativaGestanteAdapter alternativaGestanteAdapter;

    //Arraylist que contiene objetos infante
    ArrayList<AlternativaGestanteModel> alternativaGestanteModelArrayList;

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

    String id_pregunta,id_visita_gestante,pregunta,detalle,area,sug_temporal,estado, id_gestante;

    public String getId_pregunta() {
        return id_pregunta;
    }

    public void setId_pregunta(String id_pregunta) {
        this.id_pregunta = id_pregunta;
    }

    public String getId_visita_gestante() {
        return id_visita_gestante;
    }

    public void setId_visita_gestante(String id_visita_gestante) {
        this.id_visita_gestante = id_visita_gestante;
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

    public String getId_gestante() {
        return id_gestante;
    }

    public void setId_gestante(String id_gestante) {
        this.id_gestante = id_gestante;
    }

    /*Carga de datos en recyclerview*/
    private void setAlternativaGestanteAdapter(ArrayList<AlternativaGestanteModel> alternativaGestanteModelArrayList,RecyclerView dataContainer){

        dataContainer=findViewById(R.id.dataContainer);
        //Instancia de layoutmanager
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(AlternativaGestanteActivity.this,RecyclerView.VERTICAL,false);
        //Esablecemos el layoutmanayer al contenedor asignado
        dataContainer.setLayoutManager(layoutManager);
        //instancia del adaptador y llenado de constructor
        alternativaGestanteAdapter=new AlternativaGestanteAdapter(AlternativaGestanteActivity.this,alternativaGestanteModelArrayList);
        //llenamos el contenedor con el adaptador ya nstanciado
        dataContainer.setAdapter(alternativaGestanteAdapter);
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
        setId_visita_gestante(getIntent().getStringExtra("id_visita_gestante"));
        setPregunta(getIntent().getStringExtra("pregunta"));
        setArea(getIntent().getStringExtra("area"));
        setSug_temporal(getIntent().getStringExtra("sug_temporal"));
        setEstado(getIntent().getStringExtra("estado"));
        setId_gestante(getIntent().getStringExtra("id_gestante"));

        tvPregunta.setText(getPregunta());
        tvArea.setText(getArea());
        tvSugTemporal.setText(getSug_temporal());

        if(getEstado().matches("Contestada")){
            ArrayList<RespuestaGestanteModel> retrieveAnswers=RetrieveAllChoosenAnswers(getId_gestante(),getId_pregunta());
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
        navigationView.setNavigationItemSelectedListener(AlternativaGestanteActivity.this);

        //Seeccionamos por defecto home
        navigationView.setCheckedItem(R.id.nav_parent);

        getSupportLoaderManager().initLoader(0,null,this);
        MovisdoSyncAdapter.inicializarSyncAdapter(this);

        alternativaGestanteModelArrayList=new ArrayList<>();

        //Llenamos el adaptador con el contenido que tenga el arraylist
        setAlternativaGestanteAdapter(alternativaGestanteModelArrayList,dataContainer);


    }

    public void syncOnClick(View view) {

        MovisdoSyncAdapter.sincronizarAhora(this, false);
    }

    /**
     * Obtiene alternativas elegidas de la pregunta ? del infante con id ?
     * @param id_gestante
     * @param id_pregunta
     * @return
     */
    public ArrayList <RespuestaGestanteModel> RetrieveAllChoosenAnswers(String id_gestante,String id_pregunta) {
        ArrayList<RespuestaGestanteModel> retrieveAnswers=new ArrayList<RespuestaGestanteModel>();
        String id="";
        String id_alternativa="";
        String id_visita_gestante="";
        String detalle="";
        try {
            Uri uri;
            //Unión de alternativa con respuesta, obtiene todas las alternativas que fueron seleccionadas como respuestas de todas las visitas cuyo infante tiene el ID?
            uri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TALTERNATIVA_ELEGIDA2_DETAILS);
            //Mostrar solo el campo detalle
            String[] projeccion = new String[]{MovisdoContract.TRESPUESTA_GESTANTE+"."+MovisdoContract.RespuestaGestanteColumns.ID_REMOTA
                    ,MovisdoContract.TRESPUESTA_GESTANTE+"."+MovisdoContract.RespuestaGestanteColumns.ID_ALTERNATIVA
                    ,MovisdoContract.TRESPUESTA_GESTANTE+"."+MovisdoContract.RespuestaGestanteColumns.ID_VISITA_GESTANTE
                    ,MovisdoContract.TRESPUESTA_GESTANTE+"."+MovisdoContract.RespuestaGestanteColumns.DETALLE};
            //Donde el id_infante sea ? y la pregunta sea ?
            String selection = MovisdoContract.TVISITA_GESTANTE+"."+MovisdoContract.VisitaGestanteColumns.ID_GESTANTE+" =? AND "+
                    MovisdoContract.TALTERNATIVA+"."+MovisdoContract.AlternativaColumns.ID_PREGUNTA+" =?";
            String[] selectionArgs = {id_gestante,id_pregunta};
            Cursor c = getContentResolver().query(
                    uri,
                    projeccion,
                    selection,
                    selectionArgs,
                    null);

            while (c.moveToNext()) {
                id=c.getString(0);
                id_alternativa=c.getString(1);
                id_visita_gestante=c.getString(2);
                detalle=c.getString(3);
                RespuestaGestanteModel respuestaGestanteModel=new RespuestaGestanteModel(id,id_alternativa,id_visita_gestante,detalle);
                retrieveAnswers.add(respuestaGestanteModel);
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
        alternativaGestanteModelArrayList=alternativaGestanteAdapter.ConvertCursorToArray(data,getId_visita_gestante(),getId_gestante());
    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<Cursor> loader) {
        alternativaGestanteAdapter.ConvertCursorToArray(null,getId_visita_gestante(),getId_gestante());
    }

    public void btn_save_answers(View view) {
        ArrayList<String> alternativasElegidas;
        alternativasElegidas=alternativaGestanteAdapter.getAlternativaElegidaArrayList();

        ArrayList<RespuestaGestanteModel> retrieveAnswers=new ArrayList<>();
        retrieveAnswers=RetrieveAllChoosenAnswers(getId_gestante(),getId_pregunta());

        int nroRespuestas=retrieveAnswers.size();

        if(nroRespuestas>0 || alternativasElegidas.size()>0){


            setDetalle(etDetalle.getText().toString().trim());
            ContentValues values;
            if(getEstado().matches("Contestada")){

                for (RespuestaGestanteModel answer :
                        retrieveAnswers) {
                    values = new ContentValues();
                    String[]selectionArgs=new String[]{answer.getId()};
                    values.put(MovisdoContract.RespuestaGestanteColumns.PENDIENTE_ELIMINACION, "1");
                    getContentResolver().update(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_GESTANTE),values,
                            MovisdoContract.RespuestaGestanteColumns.ID_REMOTA+" =?", selectionArgs);

                }
                for (String respuesta :
                        alternativasElegidas) {

                    values = new ContentValues();
                    values.put(MovisdoContract.RespuestaGestanteColumns.ID_ALTERNATIVA, respuesta);
                    values.put(MovisdoContract.RespuestaGestanteColumns.ID_VISITA_GESTANTE, getId_visita_gestante());
                    values.put(MovisdoContract.RespuestaGestanteColumns.DETALLE, getDetalle());
                    values.put(MovisdoContract.RespuestaGestanteColumns.PENDIENTE_INSERCION, 1);


                    getContentResolver().insert(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_GESTANTE), values);
                    MovisdoSyncAdapter.sincronizarAhora(this, true);

                }
            }
            else {
                for (String respuesta :
                        alternativasElegidas) {
                    values = new ContentValues();
                    values.put(MovisdoContract.RespuestaGestanteColumns.ID_ALTERNATIVA, respuesta);
                    values.put(MovisdoContract.RespuestaGestanteColumns.ID_VISITA_GESTANTE, getId_visita_gestante());
                    values.put(MovisdoContract.RespuestaGestanteColumns.DETALLE, getDetalle());
                    values.put(MovisdoContract.RespuestaGestanteColumns.PENDIENTE_INSERCION, 1);
                    getContentResolver().insert(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_GESTANTE), values);
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
                Intent intent_parent=new Intent(AlternativaGestanteActivity.this, MenuActivity.class);
                //pasamos PK
                intent_parent.putExtra("id_promotor",id_promotor[2]);
                //llamamos a la otra actividad
                startActivity(intent_parent);
                break;
            case R.id.nav_children:
                /*Llamada a la otra actividad*/
                Intent intent_child=new Intent(AlternativaGestanteActivity.this, InfantePromotorActivity.class);
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
