package com.arsltech.developer.MovisdoApp.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arsltech.developer.MovisdoApp.R;
import com.arsltech.developer.MovisdoApp.model.AlternativaModel;
import com.arsltech.developer.MovisdoApp.model.EncuestadoModel;
import com.arsltech.developer.MovisdoApp.model.GestanteModel;
import com.arsltech.developer.MovisdoApp.model.InfanteModel;
import com.arsltech.developer.MovisdoApp.model.LlamadaVisitaGestanteModel;
import com.arsltech.developer.MovisdoApp.model.LlamadaVisitaInfanteModel;
import com.arsltech.developer.MovisdoApp.model.PreguntaModel;
import com.arsltech.developer.MovisdoApp.model.RespuestaGestanteModel;
import com.arsltech.developer.MovisdoApp.model.RespuestaInfanteModel;
import com.arsltech.developer.MovisdoApp.model.VisitaGestanteModel;
import com.arsltech.developer.MovisdoApp.model.VisitaInfanteModel;
import com.arsltech.developer.MovisdoApp.provider.MovisdoContract;
import com.arsltech.developer.MovisdoApp.utils.Constantes;
import com.arsltech.developer.MovisdoApp.utils.Utilidades;
import com.arsltech.developer.MovisdoApp.web.VolleySingleton;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Maneja la transferencia de datos entre el servidor y el cliente
 */
public class MovisdoSyncAdapter extends AbstractThreadedSyncAdapter {
    //Etiqueta para detectar errores
    private static final String TAG = MovisdoSyncAdapter.class.getSimpleName();

    //Atributo para constructor
    ContentResolver resolver;

    //Gson parsea la respuesta del API REST en formato Json
    private Gson gson = new Gson();

    /**
     * Clase donde guardamos índices de columnas y projecciones de la clase encuestado
     */
    public static class ENCUESTADO_CLASS{
        /**
         * Proyección para las consultas
         */
        private static final String[] ENCUESTADO_PROJECTION = new String[]{
                MovisdoContract.EncuestadoColumns._ID,
                MovisdoContract.EncuestadoColumns.ID_REMOTA,

                MovisdoContract.EncuestadoColumns.NOMBRE,
                MovisdoContract.EncuestadoColumns.APPATERNO,
                MovisdoContract.EncuestadoColumns.APMATERNO,
                MovisdoContract.EncuestadoColumns.DNI,
                MovisdoContract.EncuestadoColumns.FECHA_NACIMIENTO,
                MovisdoContract.EncuestadoColumns.CELULAR,
                MovisdoContract.EncuestadoColumns.DIRECCION,
                MovisdoContract.EncuestadoColumns.REF_VIVIENDA,
                MovisdoContract.EncuestadoColumns.CATEGORIA,
                MovisdoContract.EncuestadoColumns.ID_PROMOTOR,
        };

        // Indices para las columnas indicadas en la proyección
        public static final int COLUMNA_ID = 0;
        public static final int COLUMNA_ID_REMOTA = 1;

        public static final int COLUMNA_NOMBRE = 2;
        public static final int COLUMNA_APPATERNO = 3;
        public static final int COLUMNA_APMATERNO = 4;
        public static final int COLUMNA_DNI = 5;
        public static final int COLUMNA_FECHA_NACIMIENTO = 6;
        public static final int COLUMNA_CELULAR = 7;
        public static final int COLUMNA_DIRECCION = 8;
        public static final int COLUMNA_REF_VIVIENDA = 9;
        public static final int COLUMNA_CATEGORIA = 10;
        public static final int COLUMNA_ID_PROMOTOR = 11;
    }

    public static class INFANTE_CLASS {
        private static final String[] INFANTE_PROJECTION = new String[]{
                MovisdoContract.InfanteColumns._ID,
                MovisdoContract.InfanteColumns.ID_REMOTA,

                MovisdoContract.InfanteColumns.NOMBRE,
                MovisdoContract.InfanteColumns.APPATERNO,
                MovisdoContract.InfanteColumns.APMATERNO,
                MovisdoContract.InfanteColumns.DNI,
                MovisdoContract.InfanteColumns.FECHA_NACIMIENTO,
                MovisdoContract.InfanteColumns.SEXO,
                MovisdoContract.InfanteColumns.ESTAB_SALUD,
                MovisdoContract.InfanteColumns.PREMATURO,
                MovisdoContract.InfanteColumns.CATEGORIA,
                MovisdoContract.InfanteColumns.ID_ENCUESTADO,
        };

        // Indices para las columnas indicadas en la proyección
        public static final int COLUMNA_ID = 0;
        public static final int COLUMNA_ID_REMOTA = 1;

        public static final int COLUMNA_NOMBRE = 2;
        public static final int COLUMNA_APPATERNO = 3;
        public static final int COLUMNA_APMATERNO = 4;
        public static final int COLUMNA_DNI = 5;
        public static final int COLUMNA_FECHA_NACIMIENTO = 6;
        public static final int COLUMNA_SEXO = 7;
        public static final int COLUMNA_ESTAB_SALUD = 8;
        public static final int COLUMNA_PREMATURO = 9;
        public static final int COLUMNA_CATEGORIA = 10;
        public static final int COLUMNA_ID_ENCUESTADO = 11;
    }

    public static class GESTANTE_CLASS {
        private static final String[] GESTANTE_PROJECTION = new String[]{
                MovisdoContract.GestanteColumns._ID,
                MovisdoContract.GestanteColumns.ID_REMOTA,

                MovisdoContract.GestanteColumns.FECHA_PARTO,
                MovisdoContract.GestanteColumns.ESTAB_SALUD,
                MovisdoContract.GestanteColumns.SEXO_BEBE,
                MovisdoContract.GestanteColumns.ID_ENCUESTADO,
        };

        // Indices para las columnas indicadas en la proyección
        public static final int COLUMNA_ID = 0;
        public static final int COLUMNA_ID_REMOTA = 1;

        public static final int COLUMNA_FECHA_PARTO = 2;
        public static final int COLUMNA_ESTAB_SALUD = 3;
        public static final int COLUMNA_SEXO_BEBE = 4;
        public static final int COLUMNA_ID_ENCUESTADO = 5;
    }

    public static class VISITA_INFANTE_CLASS {
        private static final String[] VISITA_INFANTE_PROJECTION = new String[]{
                MovisdoContract.VisitaInfanteColumns._ID,
                MovisdoContract.VisitaInfanteColumns.ID_REMOTA,

                MovisdoContract.VisitaInfanteColumns.FECHA,
                MovisdoContract.VisitaInfanteColumns.MOD_VISITA,
                MovisdoContract.VisitaInfanteColumns.LATITUD,
                MovisdoContract.VisitaInfanteColumns.LONGITUD,
                MovisdoContract.VisitaInfanteColumns.ID_INFANTE,
        };

        // Indices para las columnas indicadas en la proyección
        public static final int COLUMNA_ID = 0;
        public static final int COLUMNA_ID_REMOTA = 1;

        public static final int COLUMNA_FECHA = 2;
        public static final int COLUMNA_MOD_VISITA = 3;
        public static final int COLUMNA_LATITUD = 4;
        public static final int COLUMNA_LONGITUD = 5;
        public static final int COLUMNA_ID_INFANTE = 6;
    }

    public static class VISITA_GESTANTE_CLASS {
        private static final String[] VISITA_GESTANTE_PROJECTION = new String[]{
                MovisdoContract.VisitaGestanteColumns._ID,
                MovisdoContract.VisitaGestanteColumns.ID_REMOTA,

                MovisdoContract.VisitaGestanteColumns.FECHA,
                MovisdoContract.VisitaGestanteColumns.MOD_VISITA,
                MovisdoContract.VisitaGestanteColumns.LATITUD,
                MovisdoContract.VisitaGestanteColumns.LONGITUD,
                MovisdoContract.VisitaGestanteColumns.ID_GESTANTE,
        };

        // Indices para las columnas indicadas en la proyección
        public static final int COLUMNA_ID = 0;
        public static final int COLUMNA_ID_REMOTA = 1;

        public static final int COLUMNA_FECHA = 2;
        public static final int COLUMNA_MOD_VISITA = 3;
        public static final int COLUMNA_LATITUD = 4;
        public static final int COLUMNA_LONGITUD = 5;
        public static final int COLUMNA_ID_GESTANTE = 6;
    }

    public static class PREGUNTA_CLASS {
        private static final String[] PREGUNTA_PROJECTION = new String[]{
                MovisdoContract.PreguntaColumns._ID,
                MovisdoContract.PreguntaColumns.ID_REMOTA,

                MovisdoContract.PreguntaColumns.PREGUNTA,
                MovisdoContract.PreguntaColumns.AREA,
                MovisdoContract.PreguntaColumns.CATEGORIA,
                MovisdoContract.PreguntaColumns.SUG_TEMPORAL,
        };

        // Indices para las columnas indicadas en la proyección
        public static final int COLUMNA_ID = 0;
        public static final int COLUMNA_ID_REMOTA = 1;

        public static final int COLUMNA_PREGUNTA = 2;
        public static final int COLUMNA_AREA = 3;
        public static final int COLUMNA_CATEGORIA = 4;
        public static final int COLUMNA_SUG_TEMPORAL = 5;
    }

    public static class ALTERNATIVA_CLASS {
        private static final String[] ALTERNATIVA_PROJECTION = new String[]{
                MovisdoContract.AlternativaColumns._ID,
                MovisdoContract.AlternativaColumns.ID_REMOTA,

                MovisdoContract.AlternativaColumns.ALTERNATIVA,
                MovisdoContract.AlternativaColumns.ID_PREGUNTA
        };

        // Indices para las columnas indicadas en la proyección
        public static final int COLUMNA_ID = 0;
        public static final int COLUMNA_ID_REMOTA = 1;

        public static final int COLUMNA_ALTERNATIVA = 2;
        public static final int COLUMNA_ID_PREGUNTA = 3;
    }

    public static class RESPUESTA_INFANTE_CLASS {
        private static final String[] RESPUESTA_INFANTE_PROJECTION = new String[]{
                MovisdoContract.RespuestaInfanteColumns._ID,
                MovisdoContract.RespuestaInfanteColumns.ID_REMOTA,

                MovisdoContract.RespuestaInfanteColumns.ID_ALTERNATIVA,
                MovisdoContract.RespuestaInfanteColumns.ID_VISITA_INFANTE,
                MovisdoContract.RespuestaInfanteColumns.DETALLE
        };

        // Indices para las columnas indicadas en la proyección
        public static final int COLUMNA_ID = 0;
        public static final int COLUMNA_ID_REMOTA = 1;

        public static final int COLUMNA_ID_ALTERNATIVA = 2;
        public static final int COLUMNA_ID_VISITA_INFANTE = 3;
        public static final int COLUMNA_DETALLE = 4;
    }

    public static class RESPUESTA_GESTANTE_CLASS {
        private static final String[] RESPUESTA_GESTANTE_PROJECTION = new String[]{
                MovisdoContract.RespuestaGestanteColumns._ID,
                MovisdoContract.RespuestaGestanteColumns.ID_REMOTA,

                MovisdoContract.RespuestaGestanteColumns.ID_ALTERNATIVA,
                MovisdoContract.RespuestaGestanteColumns.ID_VISITA_GESTANTE,
                MovisdoContract.RespuestaGestanteColumns.DETALLE
        };

        // Indices para las columnas indicadas en la proyección
        public static final int COLUMNA_ID = 0;
        public static final int COLUMNA_ID_REMOTA = 1;

        public static final int COLUMNA_ID_ALTERNATIVA = 2;
        public static final int COLUMNA_ID_VISITA_GESTANTE = 3;
        public static final int COLUMNA_DETALLE = 4;
    }

    public static class LLAMADA_VISITA_INFANTE_CLASS {
        private static final String[] LLAMADA_VISITA_INFANTE_PROJECTION = new String[]{
                MovisdoContract.LlamadaVisitaInfanteColumns._ID,
                MovisdoContract.LlamadaVisitaInfanteColumns.ID_REMOTA,

                MovisdoContract.LlamadaVisitaInfanteColumns.DATOS_LLAMADA,
                MovisdoContract.LlamadaVisitaInfanteColumns.DURACION,
                MovisdoContract.LlamadaVisitaInfanteColumns.ID_VISITA_INFANTE
        };

        // Indices para las columnas indicadas en la proyección
        public static final int COLUMNA_ID = 0;
        public static final int COLUMNA_ID_REMOTA = 1;

        public static final int COLUMNA_DATOS_LLAMADA = 2;
        public static final int COLUMNA_DURACION = 3;
        public static final int COLUMNA_ID_VISITA_INFANTE = 4;
    }

    public static class LLAMADA_VISITA_GESTANTE_CLASS {
        private static final String[] LLAMADA_VISITA_GESTANTE_PROJECTION = new String[]{
                MovisdoContract.LlamadaVisitaGestanteColumns._ID,
                MovisdoContract.LlamadaVisitaGestanteColumns.ID_REMOTA,

                MovisdoContract.LlamadaVisitaGestanteColumns.DATOS_LLAMADA,
                MovisdoContract.LlamadaVisitaGestanteColumns.DURACION,
                MovisdoContract.LlamadaVisitaGestanteColumns.ID_VISITA_GESTANTE
        };

        // Indices para las columnas indicadas en la proyección
        public static final int COLUMNA_ID = 0;
        public static final int COLUMNA_ID_REMOTA = 1;

        public static final int COLUMNA_DATOS_LLAMADA = 2;
        public static final int COLUMNA_DURACION = 3;
        public static final int COLUMNA_ID_VISITA_GESTANTE = 4;
    }


    //Métodos globales

    /**
     * Constructor que inicializa la clase
     * @param context
     * @param autoInitialize
     */
    public MovisdoSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        resolver = context.getContentResolver();
    }

    /**
     * Constructor para mantener compatibilidad en versiones inferiores a 3.0
     */
    public MovisdoSyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        resolver = context.getContentResolver();
    }


    /**
     * PRIMERA LLAMADA
     * Método llamado al llamarse al método oncreate de la actividad, es el primer método del syncadapter en ser llamado
     * @param context contexto de la aplicación
     */
    public static void inicializarSyncAdapter(Context context) {
        obtenerCuentaASincronizar(context);
    }

    /**
     * Crea u obtiene una cuenta existente
     *
     * @param context Contexto para acceder al administrador de cuentas
     * @return cuenta auxiliar.
     */
    public static Account obtenerCuentaASincronizar(Context context) {
        // Obtener instancia del administrador de cuentas
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Crear cuenta por defecto
        Account newAccount = new Account(
                context.getString(R.string.app_name), Constantes.ACCOUNT_TYPE);

        // Comprobar existencia de la cuenta
        if (null == accountManager.getPassword(newAccount)) {

            // Añadir la cuenta al account manager sin password y sin datos de usuario
            if (!accountManager.addAccountExplicitly(newAccount, "", null))
                return null;

        }
        Log.i(TAG, "Cuenta de usuario obtenida.");
        return newAccount;
    }

    /**
     * SEGUNDA LLAMADA
     * Inicia manualmente la sincronización, es llamado al hacerse click en el botón sync
     *
     * @param context    Contexto para crear la petición de sincronización
     * @param onlyUpload Usa true para sincronizar el servidor o false para sincronizar el cliente
     */
    public static void sincronizarAhora(Context context, boolean onlyUpload) {
        Log.i(TAG, "Realizando petición de sincronización manual.");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        if (onlyUpload)
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, true);
        ContentResolver.requestSync(obtenerCuentaASincronizar(context),context.getString(R.string.provider_authority), bundle);
    }

    /**
     * Se ejecuta luego de segunda llamada
     * @param account
     * @param extras
     * @param authority
     * @param provider
     * @param syncResult
     */
    @Override
    public void onPerformSync(Account account,
                              Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              final SyncResult syncResult) {

        Log.i(TAG, "onPerformSync()...");

        boolean soloSubida = extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false);

        if (!soloSubida) {
            realizarSincronizacionLocal(syncResult,Constantes.GET_URL_ENCUESTADO,MovisdoContract.TENCUESTADO);
            realizarSincronizacionLocal(syncResult,Constantes.GET_URL_INFANTE,MovisdoContract.TINFANTE);
            realizarSincronizacionLocal(syncResult,Constantes.GET_URL_GESTANTE,MovisdoContract.TGESTANTE);
            realizarSincronizacionLocal(syncResult,Constantes.GET_URL_VISITA_INFANTE,MovisdoContract.TVISITA_INFANTE);
            realizarSincronizacionLocal(syncResult,Constantes.GET_URL_VISITA_GESTANTE,MovisdoContract.TVISITA_GESTANTE);
            realizarSincronizacionLocal(syncResult,Constantes.GET_URL_PREGUNTA,MovisdoContract.TPREGUNTA);
            realizarSincronizacionLocal(syncResult,Constantes.GET_URL_ALTERNATIVA,MovisdoContract.TALTERNATIVA);
            realizarSincronizacionLocal(syncResult,Constantes.GET_URL_RESPUESTA_INFANTE,MovisdoContract.TRESPUESTA_INFANTE);
            realizarSincronizacionLocal(syncResult,Constantes.GET_URL_RESPUESTA_GESTANTE,MovisdoContract.TRESPUESTA_GESTANTE);
            realizarSincronizacionLocal(syncResult,Constantes.GET_URL_LLAMADA_VISITA_INFANTE,MovisdoContract.TLLAMADA_VISITA_INFANTE);
            realizarSincronizacionLocal(syncResult,Constantes.GET_URL_LLAMADA_VISITA_GESTANTE,MovisdoContract.TLLAMADA_VISITA_GESTANTE);
        } else {
            realizarSincronizacionRemota(MovisdoContract.TENCUESTADO,Constantes.INSERT_URL_ENCUESTADO);
            realizarSincronizacionRemota(MovisdoContract.TINFANTE,Constantes.INSERT_URL_INFANTE);
            realizarSincronizacionRemota(MovisdoContract.TGESTANTE,Constantes.INSERT_URL_GESTANTE);
            realizarSincronizacionRemota(MovisdoContract.TVISITA_INFANTE,Constantes.INSERT_URL_VISITA_INFANTE);
            realizarSincronizacionRemotaDeEliminacion(MovisdoContract.TVISITA_INFANTE,Constantes.DELETE_URL_VISITA_INFANTE);
            realizarSincronizacionRemota(MovisdoContract.TVISITA_GESTANTE,Constantes.INSERT_URL_VISITA_GESTANTE);
            realizarSincronizacionRemotaDeEliminacion(MovisdoContract.TVISITA_GESTANTE,Constantes.DELETE_URL_VISITA_GESTANTE);
            realizarSincronizacionRemota(MovisdoContract.TPREGUNTA,Constantes.INSERT_URL_PREGUNTA);
            realizarSincronizacionRemota(MovisdoContract.TALTERNATIVA,Constantes.INSERT_URL_ALTERNATIVA);
            realizarSincronizacionRemota(MovisdoContract.TRESPUESTA_INFANTE,Constantes.INSERT_URL_RESPUESTA_INFANTE);
            realizarSincronizacionRemotaDeEliminacion(MovisdoContract.TRESPUESTA_INFANTE,Constantes.DELETE_URL_RESPUESTA_INFANTE);
            realizarSincronizacionRemota(MovisdoContract.TRESPUESTA_GESTANTE,Constantes.INSERT_URL_RESPUESTA_GESTANTE);
            realizarSincronizacionRemotaDeEliminacion(MovisdoContract.TRESPUESTA_GESTANTE,Constantes.DELETE_URL_RESPUESTA_GESTANTE);
            realizarSincronizacionRemota(MovisdoContract.TLLAMADA_VISITA_INFANTE,Constantes.INSERT_URL_LLAMADA_VISITA_INFANTE);
            realizarSincronizacionRemota(MovisdoContract.TLLAMADA_VISITA_GESTANTE,Constantes.INSERT_URL_LLAMADA_VISITA_GESTANTE);

        }
    }

    private void realizarSincronizacionLocal(final SyncResult syncResult, String table_get_url,String tableName) {
        Log.i(TAG, "Actualizando el cliente.");

        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        table_get_url,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaGet(response, syncResult,tableName);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, error.networkResponse.toString());
                            }
                        }
                )
        );
    }

    /**
     * Procesa la respuesta del servidor al pedir que se retornen todos los encuestados.
     *
     * @param response   Respuesta en formato Json
     * @param syncResult Registro de resultados de sincronización
     */
    private void procesarRespuestaGet(JSONObject response, SyncResult syncResult,String tableName) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocales(response, syncResult,tableName);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza los registros locales a través de una comparación con los datos
     * del servidor
     *
     * @param response   Respuesta en formato Json obtenida del servidor
     * @param syncResult Registros de la sincronización
     */
    private void actualizarLlamadaVisitaGestanteDatosLocales(JSONObject response, SyncResult syncResult){
        JSONArray llamadaVisitaGestantes = null;

        try {
            // Obtener array "infantes"
            llamadaVisitaGestantes = response.getJSONArray(Constantes.LLAMADA_VISITA_GESTANTES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        LlamadaVisitaGestanteModel[] res = gson.fromJson(llamadaVisitaGestantes != null ? llamadaVisitaGestantes.toString() : null, LlamadaVisitaGestanteModel[].class);
        List<LlamadaVisitaGestanteModel> data = Arrays.asList(res);


        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas entrantes
        HashMap<String, LlamadaVisitaGestanteModel> expenseMap = new HashMap<String, LlamadaVisitaGestanteModel>();


        for (LlamadaVisitaGestanteModel e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TLLAMADA_VISITA_GESTANTE);

        String selection = MovisdoContract.LlamadaVisitaGestanteColumns.ID_REMOTA + " IS NOT NULL";

        Cursor c = resolver.query(uri, LLAMADA_VISITA_GESTANTE_CLASS.LLAMADA_VISITA_GESTANTE_PROJECTION, selection, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        String datos_llamada;
        String duracion;
        String id_visita_gestante;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            id = c.getString(LLAMADA_VISITA_GESTANTE_CLASS.COLUMNA_ID_REMOTA);
            datos_llamada = c.getString(LLAMADA_VISITA_GESTANTE_CLASS.COLUMNA_DATOS_LLAMADA);
            duracion = c.getString(LLAMADA_VISITA_GESTANTE_CLASS.COLUMNA_DURACION);
            id_visita_gestante = c.getString(LLAMADA_VISITA_GESTANTE_CLASS.COLUMNA_ID_VISITA_GESTANTE);


            LlamadaVisitaGestanteModel match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TLLAMADA_VISITA_GESTANTE).buildUpon()
                        .appendPath(id).build();

                // Comprobar si el encuestado necesita ser actualizado
                boolean b = match.datos_llamada != null && !match.datos_llamada.equals(datos_llamada);
                boolean b1 = match.duracion != null && !match.duracion.equals(duracion);
                boolean b2 = match.id_visita_gestante != null && !match.id_visita_gestante.equals(id_visita_gestante);


                if (b || b1 || b2) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(MovisdoContract.LlamadaVisitaGestanteColumns.DATOS_LLAMADA, match.datos_llamada)
                            .withValue(MovisdoContract.LlamadaVisitaGestanteColumns.DURACION, match.duracion)
                            .withValue(MovisdoContract.LlamadaVisitaGestanteColumns.ID_VISITA_GESTANTE, match.id_visita_gestante)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TLLAMADA_VISITA_GESTANTE).buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (LlamadaVisitaGestanteModel e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de: " + e.id);
            ops.add(ContentProviderOperation.newInsert(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TLLAMADA_VISITA_GESTANTE))
                    .withValue(MovisdoContract.LlamadaVisitaGestanteColumns.ID_REMOTA, e.id)
                    .withValue(MovisdoContract.LlamadaVisitaGestanteColumns.DATOS_LLAMADA, e.datos_llamada)
                    .withValue(MovisdoContract.LlamadaVisitaGestanteColumns.DURACION, e.duracion)
                    .withValue(MovisdoContract.LlamadaVisitaGestanteColumns.ID_VISITA_GESTANTE, e.id_visita_gestante)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(MovisdoContract.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    MovisdoContract.GET_CONTENT_URI(MovisdoContract.TLLAMADA_VISITA_GESTANTE),
                    null,
                    false);
            Log.i(TAG, "Sincronización finalizada.");

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }

    }

    /**
     * Actualiza los registros locales a través de una comparación con los datos
     * del servidor
     *
     * @param response   Respuesta en formato Json obtenida del servidor
     * @param syncResult Registros de la sincronización
     */
    private void actualizarLlamadaVisitaInfanteDatosLocales(JSONObject response, SyncResult syncResult){
        JSONArray llamadaVisitaInfantes = null;

        try {
            // Obtener array "infantes"
            llamadaVisitaInfantes = response.getJSONArray(Constantes.LLAMADA_VISITA_INFANTES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        LlamadaVisitaInfanteModel[] res = gson.fromJson(llamadaVisitaInfantes != null ? llamadaVisitaInfantes.toString() : null, LlamadaVisitaInfanteModel[].class);
        List<LlamadaVisitaInfanteModel> data = Arrays.asList(res);


        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas entrantes
        HashMap<String, LlamadaVisitaInfanteModel> expenseMap = new HashMap<String, LlamadaVisitaInfanteModel>();


        for (LlamadaVisitaInfanteModel e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TLLAMADA_VISITA_INFANTE);

        String selection = MovisdoContract.LlamadaVisitaInfanteColumns.ID_REMOTA + " IS NOT NULL";

        Cursor c = resolver.query(uri, LLAMADA_VISITA_INFANTE_CLASS.LLAMADA_VISITA_INFANTE_PROJECTION, selection, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        String datos_llamada;
        String duracion;
        String id_visita_infante;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            id = c.getString(LLAMADA_VISITA_INFANTE_CLASS.COLUMNA_ID_REMOTA);
            datos_llamada = c.getString(LLAMADA_VISITA_INFANTE_CLASS.COLUMNA_DATOS_LLAMADA);
            duracion = c.getString(LLAMADA_VISITA_INFANTE_CLASS.COLUMNA_DURACION);
            id_visita_infante = c.getString(LLAMADA_VISITA_INFANTE_CLASS.COLUMNA_ID_VISITA_INFANTE);


            LlamadaVisitaInfanteModel match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TLLAMADA_VISITA_INFANTE).buildUpon()
                        .appendPath(id).build();

                // Comprobar si el encuestado necesita ser actualizado
                boolean b = match.datos_llamada != null && !match.datos_llamada.equals(datos_llamada);
                boolean b1 = match.duracion != null && !match.duracion.equals(duracion);
                boolean b2 = match.id_visita_infante != null && !match.id_visita_infante.equals(id_visita_infante);


                if (b || b1 || b2) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(MovisdoContract.LlamadaVisitaInfanteColumns.DATOS_LLAMADA, match.datos_llamada)
                            .withValue(MovisdoContract.LlamadaVisitaInfanteColumns.DURACION, match.duracion)
                            .withValue(MovisdoContract.LlamadaVisitaInfanteColumns.ID_VISITA_INFANTE, match.id_visita_infante)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TLLAMADA_VISITA_INFANTE).buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (LlamadaVisitaInfanteModel e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de: " + e.id);
            ops.add(ContentProviderOperation.newInsert(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TLLAMADA_VISITA_INFANTE))
                    .withValue(MovisdoContract.LlamadaVisitaInfanteColumns.ID_REMOTA, e.id)
                    .withValue(MovisdoContract.LlamadaVisitaInfanteColumns.DATOS_LLAMADA, e.datos_llamada)
                    .withValue(MovisdoContract.LlamadaVisitaInfanteColumns.DURACION, e.duracion)
                    .withValue(MovisdoContract.LlamadaVisitaInfanteColumns.ID_VISITA_INFANTE, e.id_visita_infante)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(MovisdoContract.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    MovisdoContract.GET_CONTENT_URI(MovisdoContract.TLLAMADA_VISITA_INFANTE),
                    null,
                    false);
            Log.i(TAG, "Sincronización finalizada.");

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }

    }


    /**
     * Actualiza los registros locales a través de una comparación con los datos
     * del servidor
     *
     * @param response   Respuesta en formato Json obtenida del servidor
     * @param syncResult Registros de la sincronización
     */
    private void actualizarRespuestaGestanteDatosLocales(JSONObject response, SyncResult syncResult){
        JSONArray respuestaGestantes = null;

        try {
            // Obtener array "infantes"
            respuestaGestantes = response.getJSONArray(Constantes.RESPUESTA_GESTANTES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        RespuestaGestanteModel[] res = gson.fromJson(respuestaGestantes != null ? respuestaGestantes.toString() : null, RespuestaGestanteModel[].class);
        List<RespuestaGestanteModel> data = Arrays.asList(res);


        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas entrantes
        HashMap<String, RespuestaGestanteModel> expenseMap = new HashMap<String, RespuestaGestanteModel>();


        for (RespuestaGestanteModel e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_GESTANTE);

        String selection = MovisdoContract.RespuestaGestanteColumns.ID_REMOTA + " IS NOT NULL";

        Cursor c = resolver.query(uri, RESPUESTA_GESTANTE_CLASS.RESPUESTA_GESTANTE_PROJECTION, selection, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        String id_alternativa;
        String id_visita_gestante;
        String detalle;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            id = c.getString(RESPUESTA_GESTANTE_CLASS.COLUMNA_ID_REMOTA);
            id_alternativa = c.getString(RESPUESTA_GESTANTE_CLASS.COLUMNA_ID_ALTERNATIVA);
            id_visita_gestante = c.getString(RESPUESTA_GESTANTE_CLASS.COLUMNA_ID_VISITA_GESTANTE);
            detalle = c.getString(RESPUESTA_GESTANTE_CLASS.COLUMNA_DETALLE);

            RespuestaGestanteModel match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_GESTANTE).buildUpon()
                        .appendPath(id).build();

                // Comprobar si el encuestado necesita ser actualizado
                boolean b = match.id_alternativa != null && !match.id_alternativa.equals(id_alternativa);
                boolean b1 = match.id_visita_gestante != null && !match.id_visita_gestante.equals(id_visita_gestante);
                boolean b2 = match.detalle != null && !match.detalle.equals(detalle);


                if (b || b1 || b2) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(MovisdoContract.RespuestaGestanteColumns.ID_ALTERNATIVA, match.id_alternativa)
                            .withValue(MovisdoContract.RespuestaGestanteColumns.ID_VISITA_GESTANTE, match.id_visita_gestante)
                            .withValue(MovisdoContract.RespuestaGestanteColumns.DETALLE, match.detalle)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_GESTANTE).buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (RespuestaGestanteModel e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de: " + e.id);
            ops.add(ContentProviderOperation.newInsert(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_GESTANTE))
                    .withValue(MovisdoContract.RespuestaGestanteColumns.ID_REMOTA, e.id)
                    .withValue(MovisdoContract.RespuestaGestanteColumns.ID_ALTERNATIVA, e.id_alternativa)
                    .withValue(MovisdoContract.RespuestaGestanteColumns.ID_VISITA_GESTANTE, e.id_visita_gestante)
                    .withValue(MovisdoContract.RespuestaGestanteColumns.DETALLE, e.detalle)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(MovisdoContract.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_GESTANTE),
                    null,
                    false);
            Log.i(TAG, "Sincronización finalizada.");

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }

    }

    /**
     * Actualiza los registros locales a través de una comparación con los datos
     * del servidor
     *
     * @param response   Respuesta en formato Json obtenida del servidor
     * @param syncResult Registros de la sincronización
     */
    private void actualizarRespuestaInfanteDatosLocales(JSONObject response, SyncResult syncResult){
        JSONArray respuestaInfantes = null;

        try {
            // Obtener array "infantes"
            respuestaInfantes = response.getJSONArray(Constantes.RESPUESTA_INFANTES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        RespuestaInfanteModel[] res = gson.fromJson(respuestaInfantes != null ? respuestaInfantes.toString() : null, RespuestaInfanteModel[].class);
        List<RespuestaInfanteModel> data = Arrays.asList(res);


        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas entrantes
        HashMap<String, RespuestaInfanteModel> expenseMap = new HashMap<String, RespuestaInfanteModel>();


        for (RespuestaInfanteModel e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_INFANTE);

        String selection = MovisdoContract.RespuestaInfanteColumns.ID_REMOTA + " IS NOT NULL";

        Cursor c = resolver.query(uri, RESPUESTA_INFANTE_CLASS.RESPUESTA_INFANTE_PROJECTION, selection, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        String id_alternativa;
        String id_visita_infante;
        String detalle;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            id = c.getString(RESPUESTA_INFANTE_CLASS.COLUMNA_ID_REMOTA);
            id_alternativa = c.getString(RESPUESTA_INFANTE_CLASS.COLUMNA_ID_ALTERNATIVA);
            id_visita_infante = c.getString(RESPUESTA_INFANTE_CLASS.COLUMNA_ID_VISITA_INFANTE);
            detalle = c.getString(RESPUESTA_INFANTE_CLASS.COLUMNA_DETALLE);

            RespuestaInfanteModel match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_INFANTE).buildUpon()
                        .appendPath(id).build();

                // Comprobar si el encuestado necesita ser actualizado
                boolean b = match.id_alternativa != null && !match.id_alternativa.equals(id_alternativa);
                boolean b1 = match.id_visita_infante != null && !match.id_visita_infante.equals(id_visita_infante);
                boolean b2 = match.detalle != null && !match.detalle.equals(detalle);

                if (b || b1 || b2) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(MovisdoContract.RespuestaInfanteColumns.ID_ALTERNATIVA, match.id_alternativa)
                            .withValue(MovisdoContract.RespuestaInfanteColumns.ID_VISITA_INFANTE, match.id_visita_infante)
                            .withValue(MovisdoContract.RespuestaInfanteColumns.DETALLE, match.detalle)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_INFANTE).buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (RespuestaInfanteModel e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de: " + e.id);
            ops.add(ContentProviderOperation.newInsert(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_INFANTE))
                    .withValue(MovisdoContract.RespuestaInfanteColumns.ID_REMOTA, e.id)
                    .withValue(MovisdoContract.RespuestaInfanteColumns.ID_ALTERNATIVA, e.id_alternativa)
                    .withValue(MovisdoContract.RespuestaInfanteColumns.ID_VISITA_INFANTE, e.id_visita_infante)
                    .withValue(MovisdoContract.RespuestaInfanteColumns.DETALLE, e.detalle)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(MovisdoContract.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_INFANTE),
                    null,
                    false);
            Log.i(TAG, "Sincronización finalizada.");

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }

    }

    /**
     * Actualiza los registros locales a través de una comparación con los datos
     * del servidor
     *
     * @param response   Respuesta en formato Json obtenida del servidor
     * @param syncResult Registros de la sincronización
     */
    private void actualizarAlternativaDatosLocales(JSONObject response, SyncResult syncResult){
        JSONArray alternativas = null;

        try {
            // Obtener array "infantes"
            alternativas = response.getJSONArray(Constantes.ALTERNATIVAS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        AlternativaModel[] res = gson.fromJson(alternativas != null ? alternativas.toString() : null, AlternativaModel[].class);
        List<AlternativaModel> data = Arrays.asList(res);


        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas entrantes
        HashMap<String, AlternativaModel> expenseMap = new HashMap<String, AlternativaModel>();


        for (AlternativaModel e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TALTERNATIVA);

        String selection = MovisdoContract.AlternativaColumns.ID_REMOTA + " IS NOT NULL";

        Cursor c = resolver.query(uri, ALTERNATIVA_CLASS.ALTERNATIVA_PROJECTION, selection, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        String alternativa;
        String id_pregunta;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            id = c.getString(ALTERNATIVA_CLASS.COLUMNA_ID_REMOTA);
            alternativa = c.getString(ALTERNATIVA_CLASS.COLUMNA_ALTERNATIVA);
            id_pregunta = c.getString(ALTERNATIVA_CLASS.COLUMNA_ID_PREGUNTA);

            AlternativaModel match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TALTERNATIVA).buildUpon()
                        .appendPath(id).build();

                // Comprobar si el encuestado necesita ser actualizado
                boolean b = match.alternativa != null && !match.alternativa.equals(alternativa);
                boolean b1 = match.id_pregunta != null && !match.id_pregunta.equals(id_pregunta);

                if (b || b1) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(MovisdoContract.AlternativaColumns.ALTERNATIVA, match.alternativa)
                            .withValue(MovisdoContract.AlternativaColumns.ID_PREGUNTA, match.id_pregunta)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TALTERNATIVA).buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (AlternativaModel e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de: " + e.id);
            ops.add(ContentProviderOperation.newInsert(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TALTERNATIVA))
                    .withValue(MovisdoContract.AlternativaColumns.ID_REMOTA, e.id)
                    .withValue(MovisdoContract.AlternativaColumns.ALTERNATIVA, e.alternativa)
                    .withValue(MovisdoContract.AlternativaColumns.ID_PREGUNTA, e.id_pregunta)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(MovisdoContract.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    MovisdoContract.GET_CONTENT_URI(MovisdoContract.TALTERNATIVA),
                    null,
                    false);
            Log.i(TAG, "Sincronización finalizada.");

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }

    }

    /**
     * Actualiza los registros locales a través de una comparación con los datos
     * del servidor
     *
     * @param response   Respuesta en formato Json obtenida del servidor
     * @param syncResult Registros de la sincronización
     */
    private void actualizarPreguntaDatosLocales(JSONObject response, SyncResult syncResult){
        JSONArray preguntas = null;

        try {
            // Obtener array "infantes"
            preguntas = response.getJSONArray(Constantes.PREGUNTAS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        PreguntaModel[] res = gson.fromJson(preguntas != null ? preguntas.toString() : null, PreguntaModel[].class);
        List<PreguntaModel> data = Arrays.asList(res);


        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas entrantes
        HashMap<String, PreguntaModel> expenseMap = new HashMap<String, PreguntaModel>();


        for (PreguntaModel e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TPREGUNTA);

        String selection = MovisdoContract.PreguntaColumns.ID_REMOTA + " IS NOT NULL";

        Cursor c = resolver.query(uri, PREGUNTA_CLASS.PREGUNTA_PROJECTION, selection, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        String pregunta;
        String area;
        String categoria;
        String sug_temporal;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            id = c.getString(PREGUNTA_CLASS.COLUMNA_ID_REMOTA);
            pregunta = c.getString(PREGUNTA_CLASS.COLUMNA_PREGUNTA);
            area = c.getString(PREGUNTA_CLASS.COLUMNA_AREA);
            categoria = c.getString(PREGUNTA_CLASS.COLUMNA_CATEGORIA);
            sug_temporal = c.getString(PREGUNTA_CLASS.COLUMNA_SUG_TEMPORAL);

            PreguntaModel match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TPREGUNTA).buildUpon()
                        .appendPath(id).build();

                // Comprobar si el encuestado necesita ser actualizado
                boolean b = match.pregunta != null && !match.pregunta.equals(pregunta);
                boolean b1 = match.area != null && !match.area.equals(area);
                boolean b2 = match.categoria != null && !match.categoria.equals(categoria);
                boolean b3 = match.sug_temporal != null && !match.sug_temporal.equals(sug_temporal);

                if (b || b1 || b2 || b3) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(MovisdoContract.PreguntaColumns.PREGUNTA, match.pregunta)
                            .withValue(MovisdoContract.PreguntaColumns.AREA, match.area)
                            .withValue(MovisdoContract.PreguntaColumns.CATEGORIA, match.categoria)
                            .withValue(MovisdoContract.PreguntaColumns.SUG_TEMPORAL, match.sug_temporal)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TPREGUNTA).buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (PreguntaModel e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de: " + e.id);
            ops.add(ContentProviderOperation.newInsert(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TPREGUNTA))
                    .withValue(MovisdoContract.PreguntaColumns.ID_REMOTA, e.id)
                    .withValue(MovisdoContract.PreguntaColumns.PREGUNTA, e.pregunta)
                    .withValue(MovisdoContract.PreguntaColumns.AREA, e.area)
                    .withValue(MovisdoContract.PreguntaColumns.CATEGORIA, e.categoria)
                    .withValue(MovisdoContract.PreguntaColumns.SUG_TEMPORAL, e.sug_temporal)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(MovisdoContract.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    MovisdoContract.GET_CONTENT_URI(MovisdoContract.TPREGUNTA),
                    null,
                    false);
            Log.i(TAG, "Sincronización finalizada.");

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }

    }

    /**
     * Actualiza los registros locales a través de una comparación con los datos
     * del servidor
     *
     * @param response   Respuesta en formato Json obtenida del servidor
     * @param syncResult Registros de la sincronización
     */
    private void actualizarVisitaGestanteDatosLocales(JSONObject response, SyncResult syncResult){
        JSONArray visita_gestantes = null;

        try {
            // Obtener array "infantes"
            visita_gestantes = response.getJSONArray(Constantes.VISITA_GESTANTES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        VisitaGestanteModel[] res = gson.fromJson(visita_gestantes != null ? visita_gestantes.toString() : null, VisitaGestanteModel[].class);
        List<VisitaGestanteModel> data = Arrays.asList(res);


        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas entrantes
        HashMap<String, VisitaGestanteModel> expenseMap = new HashMap<String, VisitaGestanteModel>();


        for (VisitaGestanteModel e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TVISITA_GESTANTE);

        String selection = MovisdoContract.VisitaGestanteColumns.ID_REMOTA + " IS NOT NULL";

        Cursor c = resolver.query(uri, VISITA_GESTANTE_CLASS.VISITA_GESTANTE_PROJECTION, selection, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        String fecha;
        String mod_visita;
        String latitud;
        String longitud;
        String id_gestante;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            id = c.getString(VISITA_GESTANTE_CLASS.COLUMNA_ID_REMOTA);
            fecha = c.getString(VISITA_GESTANTE_CLASS.COLUMNA_FECHA);
            mod_visita = c.getString(VISITA_GESTANTE_CLASS.COLUMNA_MOD_VISITA);
            latitud = c.getString(VISITA_GESTANTE_CLASS.COLUMNA_LATITUD);
            longitud = c.getString(VISITA_GESTANTE_CLASS.COLUMNA_LONGITUD);
            id_gestante = c.getString(VISITA_GESTANTE_CLASS.COLUMNA_ID_GESTANTE);

            VisitaGestanteModel match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TVISITA_GESTANTE).buildUpon()
                        .appendPath(id).build();

                // Comprobar si el encuestado necesita ser actualizado
                boolean b = match.fecha != null && !match.fecha.equals(fecha);
                boolean b1 = match.mod_visita != null && !match.mod_visita.equals(mod_visita);
                boolean b2 = match.latitud != null && !match.latitud.equals(latitud);
                boolean b3 = match.longitud != null && !match.longitud.equals(longitud);
                boolean b4 = match.id_gestante != null && !match.id_gestante.equals(id_gestante);

                if (b || b1 || b2 || b3 || b4) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(MovisdoContract.VisitaGestanteColumns.FECHA, match.fecha)
                            .withValue(MovisdoContract.VisitaGestanteColumns.MOD_VISITA, match.mod_visita)
                            .withValue(MovisdoContract.VisitaGestanteColumns.LATITUD, match.latitud)
                            .withValue(MovisdoContract.VisitaGestanteColumns.LONGITUD, match.longitud)
                            .withValue(MovisdoContract.VisitaGestanteColumns.ID_GESTANTE, match.id_gestante)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TVISITA_GESTANTE).buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (VisitaGestanteModel e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de: " + e.id);
            ops.add(ContentProviderOperation.newInsert(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TVISITA_GESTANTE))
                    .withValue(MovisdoContract.VisitaGestanteColumns.ID_REMOTA, e.id)
                    .withValue(MovisdoContract.VisitaGestanteColumns.FECHA, e.fecha)
                    .withValue(MovisdoContract.VisitaGestanteColumns.MOD_VISITA, e.mod_visita)
                    .withValue(MovisdoContract.VisitaGestanteColumns.LATITUD, e.latitud)
                    .withValue(MovisdoContract.VisitaGestanteColumns.LONGITUD, e.longitud)
                    .withValue(MovisdoContract.VisitaGestanteColumns.ID_GESTANTE, e.id_gestante)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(MovisdoContract.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    MovisdoContract.GET_CONTENT_URI(MovisdoContract.TVISITA_GESTANTE),
                    null,
                    false);
            Log.i(TAG, "Sincronización finalizada.");

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }

    }

    /**
     * Actualiza los registros locales a través de una comparación con los datos
     * del servidor
     *
     * @param response   Respuesta en formato Json obtenida del servidor
     * @param syncResult Registros de la sincronización
     */
    private void actualizarVisitaInfanteDatosLocales(JSONObject response, SyncResult syncResult){
        JSONArray visita_infantes = null;

        try {
            // Obtener array "infantes"
            visita_infantes = response.getJSONArray(Constantes.VISITA_INFANTES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        VisitaInfanteModel[] res = gson.fromJson(visita_infantes != null ? visita_infantes.toString() : null, VisitaInfanteModel[].class);
        List<VisitaInfanteModel> data = Arrays.asList(res);


        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas entrantes
        HashMap<String, VisitaInfanteModel> expenseMap = new HashMap<String, VisitaInfanteModel>();


        for (VisitaInfanteModel e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TVISITA_INFANTE);

        String selection = MovisdoContract.VisitaInfanteColumns.ID_REMOTA + " IS NOT NULL";

        Cursor c = resolver.query(uri, VISITA_INFANTE_CLASS.VISITA_INFANTE_PROJECTION, selection, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        String fecha;
        String mod_visita;
        String latitud;
        String longitud;
        String id_infante;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            id = c.getString(VISITA_INFANTE_CLASS.COLUMNA_ID_REMOTA);
            fecha = c.getString(VISITA_INFANTE_CLASS.COLUMNA_FECHA);
            mod_visita = c.getString(VISITA_INFANTE_CLASS.COLUMNA_MOD_VISITA);
            latitud = c.getString(VISITA_INFANTE_CLASS.COLUMNA_LATITUD);
            longitud = c.getString(VISITA_INFANTE_CLASS.COLUMNA_LONGITUD);
            id_infante = c.getString(VISITA_INFANTE_CLASS.COLUMNA_ID_INFANTE);

            VisitaInfanteModel match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TVISITA_INFANTE).buildUpon()
                        .appendPath(id).build();

                // Comprobar si el encuestado necesita ser actualizado
                boolean b = match.fecha != null && !match.fecha.equals(fecha);
                boolean b1 = match.mod_visita != null && !match.mod_visita.equals(mod_visita);
                boolean b2 = match.latitud != null && !match.latitud.equals(latitud);
                boolean b3 = match.longitud != null && !match.longitud.equals(longitud);
                boolean b4 = match.id_infante != null && !match.id_infante.equals(id_infante);

                if (b || b1 || b2 || b3 || b4) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(MovisdoContract.VisitaInfanteColumns.FECHA, match.fecha)
                            .withValue(MovisdoContract.VisitaInfanteColumns.MOD_VISITA, match.mod_visita)
                            .withValue(MovisdoContract.VisitaInfanteColumns.LATITUD, match.latitud)
                            .withValue(MovisdoContract.VisitaInfanteColumns.LONGITUD, match.longitud)
                            .withValue(MovisdoContract.VisitaInfanteColumns.ID_INFANTE, match.id_infante)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TVISITA_INFANTE).buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (VisitaInfanteModel e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de: " + e.id);
            ops.add(ContentProviderOperation.newInsert(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TVISITA_INFANTE))
                    .withValue(MovisdoContract.VisitaInfanteColumns.ID_REMOTA, e.id)
                    .withValue(MovisdoContract.VisitaInfanteColumns.FECHA, e.fecha)
                    .withValue(MovisdoContract.VisitaInfanteColumns.MOD_VISITA, e.mod_visita)
                    .withValue(MovisdoContract.VisitaInfanteColumns.LATITUD, e.latitud)
                    .withValue(MovisdoContract.VisitaInfanteColumns.LONGITUD, e.longitud)
                    .withValue(MovisdoContract.VisitaInfanteColumns.ID_INFANTE, e.id_infante)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(MovisdoContract.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    MovisdoContract.GET_CONTENT_URI(MovisdoContract.TVISITA_INFANTE),
                    null,
                    false);
            Log.i(TAG, "Sincronización finalizada.");

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }

    }

    /**
     * Actualiza los registros locales a través de una comparación con los datos
     * del servidor
     *
     * @param response   Respuesta en formato Json obtenida del servidor
     * @param syncResult Registros de la sincronización
     */
    private void actualizarGestanteDatosLocales(JSONObject response, SyncResult syncResult){
        JSONArray gestantes = null;

        try {
            // Obtener array "infantes"
            gestantes = response.getJSONArray(Constantes.GESTANTES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        GestanteModel[] res = gson.fromJson(gestantes != null ? gestantes.toString() : null, GestanteModel[].class);
        List<GestanteModel> data = Arrays.asList(res);


        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas entrantes
        HashMap<String, GestanteModel> expenseMap = new HashMap<String, GestanteModel>();


        for (GestanteModel e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TGESTANTE);

        String selection = MovisdoContract.GestanteColumns.ID_REMOTA + " IS NOT NULL";

        Cursor c = resolver.query(uri, GESTANTE_CLASS.GESTANTE_PROJECTION, selection, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        String fecha_parto;
        String estab_salud;
        String sexo_bebe;
        String id_encuestado;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            id = c.getString(GESTANTE_CLASS.COLUMNA_ID_REMOTA);
            fecha_parto = c.getString(GESTANTE_CLASS.COLUMNA_FECHA_PARTO);
            estab_salud = c.getString(GESTANTE_CLASS.COLUMNA_ESTAB_SALUD);
            sexo_bebe = c.getString(GESTANTE_CLASS.COLUMNA_SEXO_BEBE);
            id_encuestado = c.getString(GESTANTE_CLASS.COLUMNA_ID_ENCUESTADO);

            GestanteModel match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TGESTANTE).buildUpon()
                        .appendPath(id).build();

                // Comprobar si el encuestado necesita ser actualizado
                boolean b = match.fecha_parto != null && !match.fecha_parto.equals(fecha_parto);
                boolean b1 = match.estab_salud != null && !match.estab_salud.equals(estab_salud);
                boolean b2 = match.sexo_bebe != null && !match.sexo_bebe.equals(sexo_bebe);
                boolean b3 = match.id_encuestado != null && !match.id_encuestado.equals(id_encuestado);

                if (b || b1 || b2 || b3 ) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(MovisdoContract.GestanteColumns.FECHA_PARTO, match.fecha_parto)
                            .withValue(MovisdoContract.GestanteColumns.ESTAB_SALUD, match.estab_salud)
                            .withValue(MovisdoContract.GestanteColumns.SEXO_BEBE, match.sexo_bebe)
                            .withValue(MovisdoContract.GestanteColumns.ID_ENCUESTADO, match.id_encuestado)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TGESTANTE).buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (GestanteModel e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de: " + e.id);
            ops.add(ContentProviderOperation.newInsert(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TGESTANTE))
                    .withValue(MovisdoContract.GestanteColumns.ID_REMOTA, e.id)
                    .withValue(MovisdoContract.GestanteColumns.FECHA_PARTO, e.fecha_parto)
                    .withValue(MovisdoContract.GestanteColumns.ESTAB_SALUD, e.estab_salud)
                    .withValue(MovisdoContract.GestanteColumns.SEXO_BEBE, e.sexo_bebe)
                    .withValue(MovisdoContract.GestanteColumns.ID_ENCUESTADO, e.id_encuestado)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(MovisdoContract.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    MovisdoContract.GET_CONTENT_URI(MovisdoContract.TGESTANTE),
                    null,
                    false);
            Log.i(TAG, "Sincronización finalizada.");

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }

    }

    /**
     * Actualiza los registros locales a través de una comparación con los datos
     * del servidor
     *
     * @param response   Respuesta en formato Json obtenida del servidor
     * @param syncResult Registros de la sincronización
     */
    private void actualizarInfanteDatosLocales(JSONObject response, SyncResult syncResult){
        JSONArray infantes = null;

        try {
            // Obtener array "infantes"
            infantes = response.getJSONArray(Constantes.INFANTES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        InfanteModel[] res = gson.fromJson(infantes != null ? infantes.toString() : null, InfanteModel[].class);
        List<InfanteModel> data = Arrays.asList(res);


        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas entrantes
        HashMap<String, InfanteModel> expenseMap = new HashMap<String, InfanteModel>();


        for (InfanteModel e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TINFANTE);

        String selection = MovisdoContract.InfanteColumns.ID_REMOTA + " IS NOT NULL";

        Cursor c = resolver.query(uri, INFANTE_CLASS.INFANTE_PROJECTION, selection, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        String nombre;
        String apPaterno;
        String apMaterno;
        String dni;
        String fecha_nacimiento;
        String sexo;
        String estab_salud;
        String prematuro;
        String categoria;
        String id_encuestado;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            id = c.getString(INFANTE_CLASS.COLUMNA_ID_REMOTA);
            nombre = c.getString(INFANTE_CLASS.COLUMNA_NOMBRE);
            apPaterno = c.getString(INFANTE_CLASS.COLUMNA_APPATERNO);
            apMaterno = c.getString(INFANTE_CLASS.COLUMNA_APMATERNO);
            dni = c.getString(INFANTE_CLASS.COLUMNA_DNI);
            fecha_nacimiento = c.getString(INFANTE_CLASS.COLUMNA_FECHA_NACIMIENTO);
            sexo = c.getString(INFANTE_CLASS.COLUMNA_SEXO);
            estab_salud = c.getString(INFANTE_CLASS.COLUMNA_ESTAB_SALUD);
            prematuro = c.getString(INFANTE_CLASS.COLUMNA_PREMATURO);
            categoria = c.getString(INFANTE_CLASS.COLUMNA_CATEGORIA);
            id_encuestado = c.getString(INFANTE_CLASS.COLUMNA_ID_ENCUESTADO);

            InfanteModel match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TINFANTE).buildUpon()
                        .appendPath(id).build();

                // Comprobar si el encuestado necesita ser actualizado
                boolean b = match.nombre != null && !match.nombre.equals(nombre);
                boolean b1 = match.apPaterno != null && !match.apPaterno.equals(apPaterno);
                boolean b2 = match.apMaterno != null && !match.apMaterno.equals(apMaterno);
                boolean b3 = match.dni != null && !match.dni.equals(dni);
                boolean b4 = match.fecha_nacimiento != null && !match.fecha_nacimiento.equals(fecha_nacimiento);
                boolean b5 = match.sexo != null && !match.sexo.equals(sexo);
                boolean b6 = match.estab_salud != null && !match.estab_salud.equals(estab_salud);
                boolean b7 = match.prematuro != null && !match.prematuro.equals(prematuro);
                boolean b8 = match.categoria != null && !match.categoria.equals(categoria);
                boolean b9 = match.id_encuestado != null && !match.id_encuestado.equals(id_encuestado);

                if (b || b1 || b2 || b3 || b4 || b5 || b6 || b7 || b8 || b9) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(MovisdoContract.InfanteColumns.NOMBRE, match.nombre)
                            .withValue(MovisdoContract.InfanteColumns.APPATERNO, match.apPaterno)
                            .withValue(MovisdoContract.InfanteColumns.APMATERNO, match.apMaterno)
                            .withValue(MovisdoContract.InfanteColumns.DNI, match.dni)
                            .withValue(MovisdoContract.InfanteColumns.FECHA_NACIMIENTO, match.fecha_nacimiento)
                            .withValue(MovisdoContract.InfanteColumns.SEXO, match.sexo)
                            .withValue(MovisdoContract.InfanteColumns.ESTAB_SALUD, match.estab_salud)
                            .withValue(MovisdoContract.InfanteColumns.PREMATURO, match.prematuro)
                            .withValue(MovisdoContract.InfanteColumns.CATEGORIA, match.categoria)
                            .withValue(MovisdoContract.InfanteColumns.ID_ENCUESTADO, match.id_encuestado)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TINFANTE).buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (InfanteModel e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de: " + e.id);
            ops.add(ContentProviderOperation.newInsert(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TINFANTE))
                    .withValue(MovisdoContract.InfanteColumns.ID_REMOTA, e.id)
                    .withValue(MovisdoContract.InfanteColumns.NOMBRE, e.nombre)
                    .withValue(MovisdoContract.InfanteColumns.APPATERNO, e.apPaterno)
                    .withValue(MovisdoContract.InfanteColumns.APMATERNO, e.apMaterno)
                    .withValue(MovisdoContract.InfanteColumns.DNI, e.dni)
                    .withValue(MovisdoContract.InfanteColumns.FECHA_NACIMIENTO, e.fecha_nacimiento)
                    .withValue(MovisdoContract.InfanteColumns.SEXO, e.sexo)
                    .withValue(MovisdoContract.InfanteColumns.ESTAB_SALUD, e.estab_salud)
                    .withValue(MovisdoContract.InfanteColumns.PREMATURO, e.prematuro)
                    .withValue(MovisdoContract.InfanteColumns.CATEGORIA, e.categoria)
                    .withValue(MovisdoContract.InfanteColumns.ID_ENCUESTADO, e.id_encuestado)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(MovisdoContract.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    MovisdoContract.GET_CONTENT_URI(MovisdoContract.TINFANTE),
                    null,
                    false);
            Log.i(TAG, "Sincronización finalizada.");

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }

    }

    private void actualizarEncuestadoDatosLocales(JSONObject response, SyncResult syncResult){
        JSONArray encuestados = null;

        try {
            // Obtener array "encuestados"
            encuestados = response.getJSONArray(Constantes.ENCUESTADOS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        EncuestadoModel[] res = gson.fromJson(encuestados != null ? encuestados.toString() : null, EncuestadoModel[].class);
        List<EncuestadoModel> data = Arrays.asList(res);


        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas entrantes
        HashMap<String, EncuestadoModel> expenseMap = new HashMap<String, EncuestadoModel>();

        for (EncuestadoModel e : data) {
            expenseMap.put(e.id, e);
        }

        // Consultar registros remotos actuales
        Uri uri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TENCUESTADO);

        String selection = MovisdoContract.EncuestadoColumns.ID_REMOTA + " IS NOT NULL";

        Cursor c = resolver.query(uri, ENCUESTADO_CLASS.ENCUESTADO_PROJECTION, selection, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        String nombre;
        String apPaterno;
        String apMaterno;
        String dni;
        String fecha_nacimiento;
        String celular;
        String direccion;
        String ref_vivienda;
        String categoria;
        String id_promotor;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            id = c.getString(ENCUESTADO_CLASS.COLUMNA_ID_REMOTA);
            nombre = c.getString(ENCUESTADO_CLASS.COLUMNA_NOMBRE);
            apPaterno = c.getString(ENCUESTADO_CLASS.COLUMNA_APPATERNO);
            apMaterno = c.getString(ENCUESTADO_CLASS.COLUMNA_APMATERNO);
            dni = c.getString(ENCUESTADO_CLASS.COLUMNA_DNI);
            fecha_nacimiento = c.getString(ENCUESTADO_CLASS.COLUMNA_FECHA_NACIMIENTO);
            celular = c.getString(ENCUESTADO_CLASS.COLUMNA_CELULAR);
            direccion = c.getString(ENCUESTADO_CLASS.COLUMNA_DIRECCION);
            ref_vivienda = c.getString(ENCUESTADO_CLASS.COLUMNA_REF_VIVIENDA);
            categoria = c.getString(ENCUESTADO_CLASS.COLUMNA_CATEGORIA);
            id_promotor = c.getString(ENCUESTADO_CLASS.COLUMNA_ID_PROMOTOR);


            EncuestadoModel match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TENCUESTADO).buildUpon()
                        .appendPath(id).build();

                // Comprobar si el encuestado necesita ser actualizado
                boolean b = match.nombre != null && !match.nombre.equals(nombre);
                boolean b1 = match.apPaterno != null && !match.apPaterno.equals(apPaterno);
                boolean b2 = match.apMaterno != null && !match.apMaterno.equals(apMaterno);
                boolean b3 = match.dni != null && !match.dni.equals(dni);
                boolean b4 = match.fecha_nacimiento != null && !match.fecha_nacimiento.equals(fecha_nacimiento);
                boolean b5 = match.celular != null && !match.celular.equals(celular);
                boolean b6 = match.direccion != null && !match.direccion.equals(direccion);
                boolean b7 = match.ref_vivienda != null && !match.ref_vivienda.equals(ref_vivienda);
                boolean b8 = match.categoria != null && !match.categoria.equals(categoria);
                boolean b9 = match.id_promotor != null && !match.id_promotor.equals(id_promotor);

                if (b || b1 || b2 || b3 || b4 || b5 || b6 || b7 || b8 || b9) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(MovisdoContract.EncuestadoColumns.NOMBRE, match.nombre)
                            .withValue(MovisdoContract.EncuestadoColumns.APPATERNO, match.apPaterno)
                            .withValue(MovisdoContract.EncuestadoColumns.APMATERNO, match.apMaterno)
                            .withValue(MovisdoContract.EncuestadoColumns.DNI, match.dni)
                            .withValue(MovisdoContract.EncuestadoColumns.FECHA_NACIMIENTO, match.fecha_nacimiento)
                            .withValue(MovisdoContract.EncuestadoColumns.CELULAR, match.celular)
                            .withValue(MovisdoContract.EncuestadoColumns.DIRECCION, match.direccion)
                            .withValue(MovisdoContract.EncuestadoColumns.REF_VIVIENDA, match.ref_vivienda)
                            .withValue(MovisdoContract.EncuestadoColumns.CATEGORIA, match.categoria)
                            .withValue(MovisdoContract.EncuestadoColumns.ID_PROMOTOR, match.id_promotor)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = MovisdoContract.GET_CONTENT_URI(MovisdoContract.TENCUESTADO).buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (EncuestadoModel e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de: " + e.id);
            ops.add(ContentProviderOperation.newInsert(MovisdoContract.GET_CONTENT_URI(MovisdoContract.TENCUESTADO))
                    .withValue(MovisdoContract.EncuestadoColumns.ID_REMOTA, e.id)
                    .withValue(MovisdoContract.EncuestadoColumns.NOMBRE, e.nombre)
                    .withValue(MovisdoContract.EncuestadoColumns.APPATERNO, e.apPaterno)
                    .withValue(MovisdoContract.EncuestadoColumns.APMATERNO, e.apMaterno)
                    .withValue(MovisdoContract.EncuestadoColumns.DNI, e.dni)
                    .withValue(MovisdoContract.EncuestadoColumns.FECHA_NACIMIENTO, e.fecha_nacimiento)
                    .withValue(MovisdoContract.EncuestadoColumns.CELULAR, e.celular)
                    .withValue(MovisdoContract.EncuestadoColumns.DIRECCION, e.direccion)
                    .withValue(MovisdoContract.EncuestadoColumns.REF_VIVIENDA, e.ref_vivienda)
                    .withValue(MovisdoContract.EncuestadoColumns.CATEGORIA, e.categoria)
                    .withValue(MovisdoContract.EncuestadoColumns.ID_PROMOTOR, e.id_promotor)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(MovisdoContract.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    MovisdoContract.GET_CONTENT_URI(MovisdoContract.TENCUESTADO),
                    null,
                    false);
            Log.i(TAG, "Sincronización finalizada.");

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }

    }
    private void actualizarDatosLocales(JSONObject response, SyncResult syncResult, String tableName) {
        switch (tableName){
            case "tencuestado":
                actualizarEncuestadoDatosLocales(response,syncResult);
                break;
            case "tinfante":
                actualizarInfanteDatosLocales(response,syncResult);
                break;
            case "tgestante":
                actualizarGestanteDatosLocales(response,syncResult);
                break;
            case "tvisita_infante":
                actualizarVisitaInfanteDatosLocales(response,syncResult);
                break;
            case "tvisita_gestante":
                actualizarVisitaGestanteDatosLocales(response,syncResult);
                break;
            case "tpregunta":
                actualizarPreguntaDatosLocales(response,syncResult);
                break;
            case "talternativa":
                actualizarAlternativaDatosLocales(response,syncResult);
                break;
            case "trespuesta_infante":
                actualizarRespuestaInfanteDatosLocales(response,syncResult);
                break;
            case "trespuesta_gestante":
                actualizarRespuestaGestanteDatosLocales(response,syncResult);
                break;
            case "tllamada_visita_infante":
                actualizarLlamadaVisitaInfanteDatosLocales(response,syncResult);
                break;
            case "tllamada_visita_gestante":
                actualizarLlamadaVisitaGestanteDatosLocales(response,syncResult);
                break;
        }


    }

    private void realizarSincronizacionRemotaDeEliminacion(String tableName,String table_delete_url) {
        Log.i(TAG, "Actualizando el servidor...");

        iniciarActualizacionDeEliminacion(tableName);

        Cursor c = obtenerRegistrosSuciosDeEliminacion(tableName);

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros sucios para eliminación.");

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                /**
                 * Índide de la columna Id de la tabla N
                 */
                final int idLocal = c.getInt(0);
                JSONObject jObject = new JSONObject();
                switch (tableName){

                    case "tvisita_infante":
                        jObject=Utilidades.ConvertVisitaInfanteCursorToJSONObjectForDelete(c);
                        break;
                    case "tvisita_gestante":
                        jObject=Utilidades.ConvertVisitaGestanteCursorToJSONObjectForDelete(c);
                        break;
                    case "trespuesta_infante":
                        jObject=Utilidades.ConvertRespuestaInfanteCursorToJSONObjectForDelete(c);
                        break;
                    case "trespuesta_gestante":
                        jObject=Utilidades.ConvertRespuestaGestanteCursorToJSONObjectForDelete(c);
                        break;

                }
                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                table_delete_url,
                                jObject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaDelete(response, idLocal,tableName);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, "Error Volley: " + error.getMessage());
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" + getParamsEncoding();
                            }
                        }
                );
            }

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }
        c.close();
    }


    private void realizarSincronizacionRemota(String tableName,String table_insert_url) {
        Log.i(TAG, "Actualizando el servidor...");

        iniciarActualizacion(tableName);

        Cursor c = obtenerRegistrosSucios(tableName);

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros sucios.");

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                /**
                 * Índide de la columna Id de la tabla N
                 */
                final int idLocal = c.getInt(0);
                JSONObject jObject = new JSONObject();
                switch (tableName){
                    case "tencuestado":
                        jObject=Utilidades.ConvertEncuestadoCursorToJSONObject(c);
                        break;
                    case "tinfante":
                        jObject=Utilidades.ConvertInfanteCursorToJSONObject(c);
                        break;
                    case "tgestante":
                        jObject=Utilidades.ConvertGestanteCursorToJSONObject(c);
                        break;
                    case "tvisita_infante":
                        jObject=Utilidades.ConvertVisitaInfanteCursorToJSONObject(c);
                        break;
                    case "tvisita_gestante":
                        jObject=Utilidades.ConvertVisitaGestanteCursorToJSONObject(c);
                        break;
                    case "tpregunta":
                        jObject=Utilidades.ConvertPreguntaCursorToJSONObject(c);
                        break;
                    case "talternativa":
                        jObject=Utilidades.ConvertAlternativaCursorToJSONObject(c);
                        break;
                    case "trespuesta_infante":
                        jObject=Utilidades.ConvertRespuestaInfanteCursorToJSONObject(c);
                        break;
                    case "trespuesta_gestante":
                        jObject=Utilidades.ConvertRespuestaGestanteCursorToJSONObject(c);
                        break;
                    case "tllamada_visita_infante":
                        jObject=Utilidades.ConvertLlamadaVisitaInfanteCursorToJSONObject(c);
                        break;
                    case "tllamada_visita_gestante":
                        jObject=Utilidades.ConvertLlamadaVisitaGestanteCursorToJSONObject(c);
                        break;


                }
                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                table_insert_url,
                                jObject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsert(response, idLocal,tableName);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, "Error Volley: " + error.getMessage());
                                    }
                                }

                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" + getParamsEncoding();
                            }
                        }
                );
            }

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }
        c.close();
    }

    /**
     * Cambia a estado "de sincronización" el registro que se acaba de insertar localmente
     */
    private void iniciarActualizacion(String tableName) {

        //Recordemos que por defecto los registros ingresados localmente tienen el valor de 0
        Uri uri = MovisdoContract.GET_CONTENT_URI(tableName);
        String selection = "pendiente_insercion" + "=? AND "
                + "estado" + "=?";

        String[] selectionArgs = new String[]{"1", MovisdoContract.ESTADO_OK + ""};

        //Cambia de estado de ok a sync a todos los registros
        ContentValues v = new ContentValues();
        //Estado de sync tiene el valor de 1
        v.put("estado", MovisdoContract.ESTADO_SYNC);

        /*  Selecciona todos los registros que están pendientes de inserción,
            y actualiza sus campos estado a 0 que significa que están bien
         */
        int results = resolver.update(uri, v, selection, selectionArgs);
        Log.i(TAG, "Registros puestos en cola de inserción:" + results);
    }



    /**
     * Cambia a estado "de sincronización de eliminación" el registro que se acaba de modificar localmente para su posterior eliminación en base de datos
     */
    private void iniciarActualizacionDeEliminacion(String tableName) {

        //Recordemos que por defecto los registros ingresados localmente tienen el valor de 0
        Uri uri = MovisdoContract.GET_CONTENT_URI(tableName);
        String selection = "pendiente_eliminacion" + "=? AND "
                + "estado =?";

        String[] selectionArgs = new String[]{"1", MovisdoContract.ESTADO_OK + ""};

        //Cambia de estado de ok a sync a todos los registros
        ContentValues v = new ContentValues();
        //Estado de sync tiene el valor de 1
        v.put("estado", MovisdoContract.ESTADO_SYNC);

        /*  Selecciona todos los registros que están pendientes de eliminacion,
            y actualiza sus campos estado a 0 que significa que están bien
         */
        int results = resolver.update(uri, v, selection, selectionArgs);
        Log.i(TAG, "Registros puestos en cola de eliminación:" + results);
    }

    /**
     * Obtiene el registro que se acaba de marcar como "pendiente por eliminar" y
     * con "estado de sincronización"
     *
     * @return Cursor con el registro.
     */
    private Cursor obtenerRegistrosSuciosDeEliminacion(String tableName) {
        Uri uri = MovisdoContract.GET_CONTENT_URI(tableName);
        /*
        Seleccionar todos los registros que están pendientes de eliminación y cuyo estado está para sincronizar
         */
        String selection = "pendiente_eliminacion" + "=? AND "
                + "estado" + "=?";
        String[] selectionArgs = new String[]{"1", MovisdoContract.ESTADO_SYNC + ""};
        if (tableName.matches("tvisita_infante")){
            return resolver.query(uri, VISITA_INFANTE_CLASS.VISITA_INFANTE_PROJECTION, selection, selectionArgs, null);
        }
        else if (tableName.matches("tvisita_gestante")){
            return resolver.query(uri, VISITA_GESTANTE_CLASS.VISITA_GESTANTE_PROJECTION, selection, selectionArgs, null);
        }
        else if (tableName.matches("trespuesta_infante")){
            return resolver.query(uri, RESPUESTA_INFANTE_CLASS.RESPUESTA_INFANTE_PROJECTION, selection, selectionArgs, null);
        }
        else if (tableName.matches("trespuesta_gestante")){
            return resolver.query(uri, RESPUESTA_GESTANTE_CLASS.RESPUESTA_GESTANTE_PROJECTION, selection, selectionArgs, null);
        }
        else {
            return null;
        }
    }

    /**
     * Obtiene el registro que se acaba de marcar como "pendiente por sincronizar" y
     * con "estado de sincronización"
     *
     * @return Cursor con el registro.
     */
    private Cursor obtenerRegistrosSucios(String tableName) {
        Uri uri = MovisdoContract.GET_CONTENT_URI(tableName);
        /*
            Selecciona los registros que están pendientes de inserción y en estado de sincronización
         */
        String selection = "pendiente_insercion" + "=? AND "
                + "estado" + "=?";
        String[] selectionArgs = new String[]{"1", MovisdoContract.ESTADO_SYNC + ""};
        if (tableName.matches("tencuestado")){
            return resolver.query(uri, ENCUESTADO_CLASS.ENCUESTADO_PROJECTION, selection, selectionArgs, null);
        }
        else if (tableName.matches("tinfante")){
            return resolver.query(uri, INFANTE_CLASS.INFANTE_PROJECTION, selection, selectionArgs, null);
        }
        else if (tableName.matches("tgestante")){
            return resolver.query(uri, GESTANTE_CLASS.GESTANTE_PROJECTION, selection, selectionArgs, null);
        }
        else if (tableName.matches("tvisita_infante")){
            return resolver.query(uri, VISITA_INFANTE_CLASS.VISITA_INFANTE_PROJECTION, selection, selectionArgs, null);
        }
        else if (tableName.matches("tvisita_gestante")){
            return resolver.query(uri, VISITA_GESTANTE_CLASS.VISITA_GESTANTE_PROJECTION, selection, selectionArgs, null);
        }
        else if (tableName.matches("tpregunta")){
            return resolver.query(uri, PREGUNTA_CLASS.PREGUNTA_PROJECTION, selection, selectionArgs, null);
        }
        else if (tableName.matches("talternativa")){
            return resolver.query(uri, ALTERNATIVA_CLASS.ALTERNATIVA_PROJECTION, selection, selectionArgs, null);
        }
        else if (tableName.matches("trespuesta_infante")){
            return resolver.query(uri, RESPUESTA_INFANTE_CLASS.RESPUESTA_INFANTE_PROJECTION, selection, selectionArgs, null);
        }
        else if (tableName.matches("trespuesta_gestante")){
            return resolver.query(uri, RESPUESTA_GESTANTE_CLASS.RESPUESTA_GESTANTE_PROJECTION, selection, selectionArgs, null);
        }
        else if (tableName.matches("tllamada_visita_infante")){
            return resolver.query(uri, LLAMADA_VISITA_INFANTE_CLASS.LLAMADA_VISITA_INFANTE_PROJECTION, selection, selectionArgs, null);
        }
        else if (tableName.matches("tllamada_visita_gestante")){
            return resolver.query(uri, LLAMADA_VISITA_GESTANTE_CLASS.LLAMADA_VISITA_GESTANTE_PROJECTION, selection, selectionArgs, null);
        }
        else {
            return null;
        }

    }

    /**
     * Procesa los diferentes tipos de respuesta obtenidos del servidor
     *
     * @param response Respuesta en formato Json
     */
    public void procesarRespuestaDelete(JSONObject response, int idLocal, String tableName) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);

            switch (estado) {
                case Constantes.SUCCESS:
                    Log.i(TAG, mensaje);
                    finalizarActualizacionDeEliminacion(idLocal,tableName);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Limpia el registro que se sincronizó y le asigna la nueva id remota proveida
     * por el servidor
     *
     * @param idRemota id remota
     */
    private void finalizarActualizacionDeEliminacion(int idLocal,String tableName) {
        Uri uri = MovisdoContract.GET_CONTENT_URI(tableName);
        String selection = "_id" + "=? AND pendiente_eliminacion =?";
        String[] selectionArgs = new String[]{String.valueOf(idLocal),"1"};
        resolver.delete(uri, selection, selectionArgs);
    }


    /**
     * Procesa los diferentes tipos de respuesta obtenidos del servidor
     *
     * @param response Respuesta en formato Json
     */
    public void procesarRespuestaInsert(JSONObject response, int idLocal, String tableName) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);
            // Obtener identificador del nuevo registro creado en el servidor
            String idRemota = response.getString(Constantes.ID);

            switch (estado) {
                case Constantes.SUCCESS:
                    Log.i(TAG, mensaje);
                    finalizarActualizacion(idRemota, idLocal,tableName);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Limpia el registro que se sincronizó y le asigna la nueva id remota proveida
     * por el servidor
     *
     * @param idRemota id remota
     */
    private void finalizarActualizacion(String idRemota, int idLocal,String tableName) {
        Uri uri = MovisdoContract.GET_CONTENT_URI(tableName);
        /*
            Selecciona los registros cuyo id se pasa como parámetro y procede a actualizarlos
            para que no estén pendientes de inserción y de estado ok luego de eso actualiza
            el atributo pk server con la pk del server
         */
        String selection = "_id" + "=?";
        String[] selectionArgs = new String[]{String.valueOf(idLocal)};

        ContentValues v = new ContentValues();
        v.put("pendiente_insercion", "0");
        v.put("estado", MovisdoContract.ESTADO_OK);
        v.put("idRemota", idRemota);

        resolver.update(uri, v, selection, selectionArgs);
    }

}
