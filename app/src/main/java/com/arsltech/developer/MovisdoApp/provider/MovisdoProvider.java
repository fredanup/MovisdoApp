package com.arsltech.developer.MovisdoApp.provider;

import android.content.ContentProvider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Content Provider personalizado para los gastos
 */
public class MovisdoProvider extends ContentProvider {
    /**
     * Nombre de la base de datos
     */
    private static final String DATABASE_NAME = "bdmovisdo.db";
    /**
     * Versión actual de la base de datos
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * Instancia global del Content Resolver
     */
    private ContentResolver resolver;
    /**
     * Instancia del administrador de BD
     */
    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        // Inicializando gestor BD
        databaseHelper = new DatabaseHelper(
                getContext(),
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );

        resolver = getContext().getContentResolver();

        return true;
    }

    @Override
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {

        // Obtener base de datos
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        // Comparar Uri
        int match = MovisdoContract.uriMatcher.match(uri);

        Cursor c;

        switch (match) {
            case MovisdoContract.ENCUESTADO_ALLROWS:

                // Consultando todos los registros
                c = db.query(MovisdoContract.TENCUESTADO, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TENCUESTADO));
                break;
            case MovisdoContract.ENCUESTADO_SINGLE_ROW:

                // Consultando un solo registro basado en el Id del Uri
                long id_encuestado = ContentUris.parseId(uri);
                c = db.query(MovisdoContract.TENCUESTADO, projection,
                        MovisdoContract.EncuestadoColumns._ID + " = " + id_encuestado,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TENCUESTADO));
                break;
            case MovisdoContract.INFANTE_ALLROWS:

                // Consultando todos los registros
                c = db.query(MovisdoContract.TINFANTE, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TINFANTE));
                break;
            case MovisdoContract.INFANTE_SINGLE_ROW:

                // Consultando un solo registro basado en el Id del Uri
                long id_infante = ContentUris.parseId(uri);
                c = db.query(MovisdoContract.TINFANTE, projection,
                        MovisdoContract.InfanteColumns._ID + " = " + id_infante,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TINFANTE));
                break;
            case MovisdoContract.GESTANTE_ALLROWS:

                // Consultando todos los registros
                c = db.query(MovisdoContract.TGESTANTE, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TGESTANTE));
                break;
            case MovisdoContract.GESTANTE_SINGLE_ROW:

                // Consultando un solo registro basado en el Id del Uri
                long id_gestante = ContentUris.parseId(uri);
                c = db.query(MovisdoContract.TGESTANTE, projection,
                        MovisdoContract.GestanteColumns._ID + " = " + id_gestante,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TGESTANTE));
                break;
            case MovisdoContract.VISITA_INFANTE_ALLROWS:

                // Consultando todos los registros
                c = db.query(MovisdoContract.TVISITA_INFANTE, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TVISITA_INFANTE));
                break;
            case MovisdoContract.VISITA_INFANTE_SINGLE_ROW:

                // Consultando un solo registro basado en el Id del Uri
                long id_visita_infante = ContentUris.parseId(uri);
                c = db.query(MovisdoContract.TVISITA_INFANTE, projection,
                        MovisdoContract.VisitaInfanteColumns._ID + " = " + id_visita_infante,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TVISITA_INFANTE));
                break;
            case MovisdoContract.VISITA_GESTANTE_ALLROWS:

                // Consultando todos los registros
                c = db.query(MovisdoContract.TVISITA_GESTANTE, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TVISITA_GESTANTE));
                break;
            case MovisdoContract.VISITA_GESTANTE_SINGLE_ROW:

                // Consultando un solo registro basado en el Id del Uri
                long id_visita_gestante = ContentUris.parseId(uri);
                c = db.query(MovisdoContract.TVISITA_GESTANTE, projection,
                        MovisdoContract.VisitaGestanteColumns._ID + " = " + id_visita_gestante,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TVISITA_GESTANTE));
                break;
            case MovisdoContract.PREGUNTA_ALLROWS:

                // Consultando todos los registros
                c = db.query(MovisdoContract.TPREGUNTA, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TPREGUNTA));
                break;
            case MovisdoContract.PREGUNTA_SINGLE_ROW:

                // Consultando un solo registro basado en el Id del Uri
                long id_pregunta = ContentUris.parseId(uri);
                c = db.query(MovisdoContract.TPREGUNTA, projection,
                        MovisdoContract.PreguntaColumns._ID + " = " + id_pregunta,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TPREGUNTA));
                break;
            case MovisdoContract.ALTERNATIVA_ALLROWS:

                // Consultando todos los registros
                c = db.query(MovisdoContract.TALTERNATIVA, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TALTERNATIVA));
                break;
            case MovisdoContract.ALTERNATIVA_SINGLE_ROW:

                // Consultando un solo registro basado en el Id del Uri
                long id_alternativa = ContentUris.parseId(uri);
                c = db.query(MovisdoContract.TALTERNATIVA, projection,
                        MovisdoContract.AlternativaColumns._ID + " = " + id_alternativa,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TALTERNATIVA));
                break;
            case MovisdoContract.RESPUESTA_INFANTE_ALLROWS:

                // Consultando todos los registros
                c = db.query(MovisdoContract.TRESPUESTA_INFANTE, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_INFANTE));
                break;
            case MovisdoContract.RESPUESTA_INFANTE_SINGLE_ROW:

                // Consultando un solo registro basado en el Id del Uri
                long id_respuesta_infante = ContentUris.parseId(uri);
                c = db.query(MovisdoContract.TRESPUESTA_INFANTE, projection,
                        MovisdoContract.RespuestaInfanteColumns._ID + " = " + id_respuesta_infante,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_INFANTE));
                break;
            case MovisdoContract.RESPUESTA_GESTANTE_ALLROWS:

                // Consultando todos los registros
                c = db.query(MovisdoContract.TRESPUESTA_GESTANTE, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_GESTANTE));
                break;
            case MovisdoContract.RESPUESTA_GESTANTE_SINGLE_ROW:

                // Consultando un solo registro basado en el Id del Uri
                long id_respuesta_gestante = ContentUris.parseId(uri);
                c = db.query(MovisdoContract.TRESPUESTA_GESTANTE, projection,
                        MovisdoContract.RespuestaGestanteColumns._ID + " = " + id_respuesta_gestante,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_GESTANTE));
                break;
            case MovisdoContract.LLAMADA_VISITA_INFANTE_ALLROWS:

                // Consultando todos los registros
                c = db.query(MovisdoContract.TLLAMADA_VISITA_INFANTE, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TLLAMADA_VISITA_INFANTE));
                break;
            case MovisdoContract.LLAMADA_VISITA_INFANTE_SINGLE_ROW:

                // Consultando un solo registro basado en el Id del Uri
                long id_llamada_visita_infante = ContentUris.parseId(uri);
                c = db.query(MovisdoContract.TLLAMADA_VISITA_INFANTE, projection,
                        MovisdoContract.LlamadaVisitaInfanteColumns._ID + " = " + id_llamada_visita_infante,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TLLAMADA_VISITA_INFANTE));
                break;
            case MovisdoContract.LLAMADA_VISITA_GESTANTE_ALLROWS:

                // Consultando todos los registros
                c = db.query(MovisdoContract.TLLAMADA_VISITA_GESTANTE, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TLLAMADA_VISITA_GESTANTE));
                break;
            case MovisdoContract.LLAMADA_VISITA_GESTANTE_SINGLE_ROW:

                // Consultando un solo registro basado en el Id del Uri
                long id_llamada_visita_gestante = ContentUris.parseId(uri);
                c = db.query(MovisdoContract.TLLAMADA_VISITA_GESTANTE, projection,
                        MovisdoContract.LlamadaVisitaGestanteColumns._ID + " = " + id_llamada_visita_gestante,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        MovisdoContract.GET_CONTENT_URI(MovisdoContract.TLLAMADA_VISITA_GESTANTE));
                break;
            case MovisdoContract.ENCUESTADO_PHONE_DETAILS_ALLROWS:

                qb.setTables(MovisdoContract.EncuestadoJoinInfante);
                c=qb.query(db, projection,
                        selection,
                        selectionArgs, null, null, sortOrder);

                break;
            case MovisdoContract.GESTANTE_PHONE_DETAILS_ALLROWS:

                qb.setTables(MovisdoContract.EncuestadoJoinGestante);
                c=qb.query(db, projection,
                        selection,
                        selectionArgs, null, null, sortOrder);

                break;
            case MovisdoContract.ALTERNATIVA_ELEGIDA_DETAILS_ALLROWS:

                qb.setTables(MovisdoContract.AlternativaJoinRespuestaInfante);
                c=qb.query(db, projection,
                        selection,
                        selectionArgs, null, null, sortOrder);

                break;
            case MovisdoContract.ALTERNATIVA_ELEGIDA2_DETAILS_ALLROWS:

                qb.setTables(MovisdoContract.AlternativaJoinRespuestaGestante);
                c=qb.query(db, projection,
                        selection,
                        selectionArgs, null, null, sortOrder);

                break;
            default:
                throw new IllegalArgumentException("URI no soportada: " + uri);
        }
        return c;

    }

    @Override
    public String getType(Uri uri) {
        switch (MovisdoContract.uriMatcher.match(uri)) {
            case MovisdoContract.ENCUESTADO_ALLROWS:
                return MovisdoContract.getMultipleMime(MovisdoContract.TENCUESTADO);
            case MovisdoContract.ENCUESTADO_SINGLE_ROW:
                return MovisdoContract.getSingleMime(MovisdoContract.TENCUESTADO);
            case MovisdoContract.INFANTE_ALLROWS:
                return MovisdoContract.getMultipleMime(MovisdoContract.TINFANTE);
            case MovisdoContract.INFANTE_SINGLE_ROW:
                return MovisdoContract.getSingleMime(MovisdoContract.TINFANTE);
            case MovisdoContract.GESTANTE_ALLROWS:
                return MovisdoContract.getMultipleMime(MovisdoContract.TGESTANTE);
            case MovisdoContract.GESTANTE_SINGLE_ROW:
                return MovisdoContract.getSingleMime(MovisdoContract.TGESTANTE);
            case MovisdoContract.VISITA_INFANTE_ALLROWS:
                return MovisdoContract.getMultipleMime(MovisdoContract.TVISITA_INFANTE);
            case MovisdoContract.VISITA_INFANTE_SINGLE_ROW:
                return MovisdoContract.getSingleMime(MovisdoContract.TVISITA_INFANTE);
            case MovisdoContract.VISITA_GESTANTE_ALLROWS:
                return MovisdoContract.getMultipleMime(MovisdoContract.TVISITA_GESTANTE);
            case MovisdoContract.VISITA_GESTANTE_SINGLE_ROW:
                return MovisdoContract.getSingleMime(MovisdoContract.TVISITA_GESTANTE);
            case MovisdoContract.PREGUNTA_ALLROWS:
                return MovisdoContract.getMultipleMime(MovisdoContract.TPREGUNTA);
            case MovisdoContract.PREGUNTA_SINGLE_ROW:
                return MovisdoContract.getSingleMime(MovisdoContract.TPREGUNTA);
            case MovisdoContract.ALTERNATIVA_ALLROWS:
                return MovisdoContract.getMultipleMime(MovisdoContract.TALTERNATIVA);
            case MovisdoContract.ALTERNATIVA_SINGLE_ROW:
                return MovisdoContract.getSingleMime(MovisdoContract.TALTERNATIVA);
            case MovisdoContract.RESPUESTA_INFANTE_ALLROWS:
                return MovisdoContract.getMultipleMime(MovisdoContract.TRESPUESTA_INFANTE);
            case MovisdoContract.RESPUESTA_INFANTE_SINGLE_ROW:
                return MovisdoContract.getSingleMime(MovisdoContract.TRESPUESTA_INFANTE);
            case MovisdoContract.RESPUESTA_GESTANTE_ALLROWS:
                return MovisdoContract.getMultipleMime(MovisdoContract.TRESPUESTA_GESTANTE);
            case MovisdoContract.RESPUESTA_GESTANTE_SINGLE_ROW:
                return MovisdoContract.getSingleMime(MovisdoContract.TRESPUESTA_GESTANTE);
            case MovisdoContract.LLAMADA_VISITA_INFANTE_ALLROWS:
                return MovisdoContract.getMultipleMime(MovisdoContract.TLLAMADA_VISITA_INFANTE);
            case MovisdoContract.LLAMADA_VISITA_INFANTE_SINGLE_ROW:
                return MovisdoContract.getSingleMime(MovisdoContract.TLLAMADA_VISITA_INFANTE);
            case MovisdoContract.LLAMADA_VISITA_GESTANTE_ALLROWS:
                return MovisdoContract.getMultipleMime(MovisdoContract.TLLAMADA_VISITA_GESTANTE);
            case MovisdoContract.LLAMADA_VISITA_GESTANTE_SINGLE_ROW:
                return MovisdoContract.getSingleMime(MovisdoContract.TLLAMADA_VISITA_GESTANTE);
            default:
                throw new IllegalArgumentException("Tipo de gasto desconocido: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Validar la uri OJO!!
        /*
        if (MovisdoContract.uriMatcher.match(uri) != MovisdoContract.ENCUESTADO_ALLROWS ||
                MovisdoContract.uriMatcher.match(uri) != MovisdoContract.INFANTE_ALLROWS) {
            throw new IllegalArgumentException("URI desconocida : " + uri);
        }

         */
        ContentValues contentValues;
        if (values != null) {
            contentValues = new ContentValues(values);
        } else {
            contentValues = new ContentValues();
        }
        Uri uri_inserted;
        // Inserción de nueva fila
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        switch (MovisdoContract.uriMatcher.match(uri)) {
            case MovisdoContract.ENCUESTADO_ALLROWS:
                long rowId_Encuestado = db.insert(MovisdoContract.TENCUESTADO, null, contentValues);
                if (rowId_Encuestado > 0) {
                    uri_inserted = ContentUris.withAppendedId(
                            MovisdoContract.GET_CONTENT_URI(MovisdoContract.TENCUESTADO), rowId_Encuestado);
                    resolver.notifyChange(uri_inserted, null, false);
                    return uri_inserted;
                }

                break;
            case MovisdoContract.INFANTE_ALLROWS:
                long rowId_Infante = db.insert(MovisdoContract.TINFANTE, null, contentValues);
                if (rowId_Infante > 0) {
                    uri_inserted = ContentUris.withAppendedId(
                            MovisdoContract.GET_CONTENT_URI(MovisdoContract.TINFANTE), rowId_Infante);
                    resolver.notifyChange(uri_inserted, null, false);
                    return uri_inserted;
                }

                break;
            case MovisdoContract.GESTANTE_ALLROWS:
                long rowId_Gestante = db.insert(MovisdoContract.TGESTANTE, null, contentValues);
                if (rowId_Gestante > 0) {
                    uri_inserted = ContentUris.withAppendedId(
                            MovisdoContract.GET_CONTENT_URI(MovisdoContract.TGESTANTE), rowId_Gestante);
                    resolver.notifyChange(uri_inserted, null, false);
                    return uri_inserted;
                }

                break;
            case MovisdoContract.VISITA_INFANTE_ALLROWS:
                long rowId_Visita_infante = db.insert(MovisdoContract.TVISITA_INFANTE, null, contentValues);
                if (rowId_Visita_infante > 0) {
                    uri_inserted = ContentUris.withAppendedId(
                            MovisdoContract.GET_CONTENT_URI(MovisdoContract.TVISITA_INFANTE), rowId_Visita_infante);
                    resolver.notifyChange(uri_inserted, null, false);
                    return uri_inserted;
                }

                break;
            case MovisdoContract.VISITA_GESTANTE_ALLROWS:
                long rowId_Visita_gestante = db.insert(MovisdoContract.TVISITA_GESTANTE, null, contentValues);
                if (rowId_Visita_gestante > 0) {
                    uri_inserted = ContentUris.withAppendedId(
                            MovisdoContract.GET_CONTENT_URI(MovisdoContract.TVISITA_GESTANTE), rowId_Visita_gestante);
                    resolver.notifyChange(uri_inserted, null, false);
                    return uri_inserted;
                }

                break;
            case MovisdoContract.PREGUNTA_ALLROWS:
                long rowId_Pregunta = db.insert(MovisdoContract.TPREGUNTA, null, contentValues);
                if (rowId_Pregunta > 0) {
                    uri_inserted = ContentUris.withAppendedId(
                            MovisdoContract.GET_CONTENT_URI(MovisdoContract.TPREGUNTA), rowId_Pregunta);
                    resolver.notifyChange(uri_inserted, null, false);
                    return uri_inserted;
                }

                break;
            case MovisdoContract.ALTERNATIVA_ALLROWS:
                long rowId_Alternativa = db.insert(MovisdoContract.TALTERNATIVA, null, contentValues);
                if (rowId_Alternativa > 0) {
                    uri_inserted = ContentUris.withAppendedId(
                            MovisdoContract.GET_CONTENT_URI(MovisdoContract.TALTERNATIVA), rowId_Alternativa);
                    resolver.notifyChange(uri_inserted, null, false);
                    return uri_inserted;
                }

                break;
            case MovisdoContract.RESPUESTA_INFANTE_ALLROWS:
                long rowId_Respuesta_infante = db.insert(MovisdoContract.TRESPUESTA_INFANTE, null, contentValues);
                if (rowId_Respuesta_infante > 0) {
                    uri_inserted = ContentUris.withAppendedId(
                            MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_INFANTE), rowId_Respuesta_infante);
                    resolver.notifyChange(uri_inserted, null, false);
                    return uri_inserted;
                }

                break;
            case MovisdoContract.RESPUESTA_GESTANTE_ALLROWS:
                long rowId_Respuesta_gestante = db.insert(MovisdoContract.TRESPUESTA_GESTANTE, null, contentValues);
                if (rowId_Respuesta_gestante > 0) {
                    uri_inserted = ContentUris.withAppendedId(
                            MovisdoContract.GET_CONTENT_URI(MovisdoContract.TRESPUESTA_GESTANTE), rowId_Respuesta_gestante);
                    resolver.notifyChange(uri_inserted, null, false);
                    return uri_inserted;
                }

                break;
            case MovisdoContract.LLAMADA_VISITA_INFANTE_ALLROWS:
                long rowId_Llamada_visita_infante = db.insert(MovisdoContract.TLLAMADA_VISITA_INFANTE, null, contentValues);
                if (rowId_Llamada_visita_infante > 0) {
                    uri_inserted = ContentUris.withAppendedId(
                            MovisdoContract.GET_CONTENT_URI(MovisdoContract.TLLAMADA_VISITA_INFANTE), rowId_Llamada_visita_infante);
                    resolver.notifyChange(uri_inserted, null, false);
                    return uri_inserted;
                }

                break;
            case MovisdoContract.LLAMADA_VISITA_GESTANTE_ALLROWS:
                long rowId_Llamada_visita_gestante = db.insert(MovisdoContract.TLLAMADA_VISITA_GESTANTE, null, contentValues);
                if (rowId_Llamada_visita_gestante > 0) {
                    uri_inserted = ContentUris.withAppendedId(
                            MovisdoContract.GET_CONTENT_URI(MovisdoContract.TLLAMADA_VISITA_GESTANTE), rowId_Llamada_visita_gestante);
                    resolver.notifyChange(uri_inserted, null, false);
                    return uri_inserted;
                }

                break;
            default:
                throw new IllegalArgumentException("URI desconocida : " + uri);

        }
        throw new SQLException("Falla al insertar fila en : " + uri);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int match = MovisdoContract.uriMatcher.match(uri);
        int affected;

        switch (match) {
            case MovisdoContract.ENCUESTADO_ALLROWS:
                affected = db.delete(MovisdoContract.TENCUESTADO,
                        selection,
                        selectionArgs);
                break;
            case MovisdoContract.ENCUESTADO_SINGLE_ROW:
                long id = ContentUris.parseId(uri);
                affected = db.delete(MovisdoContract.TENCUESTADO,
                        MovisdoContract.EncuestadoColumns.ID_REMOTA + "=" + id
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                // Notificar cambio asociado a la uri
                resolver.
                        notifyChange(uri, null, false);
                break;
            case MovisdoContract.INFANTE_ALLROWS:
                affected = db.delete(MovisdoContract.TINFANTE,
                        selection,
                        selectionArgs);
                break;
            case MovisdoContract.INFANTE_SINGLE_ROW:
                long id_infante = ContentUris.parseId(uri);
                affected = db.delete(MovisdoContract.TINFANTE,
                        MovisdoContract.InfanteColumns.ID_REMOTA + "=" + id_infante
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                // Notificar cambio asociado a la uri
                resolver.
                        notifyChange(uri, null, false);
                break;
            case MovisdoContract.GESTANTE_ALLROWS:
                affected = db.delete(MovisdoContract.TGESTANTE,
                        selection,
                        selectionArgs);
                break;
            case MovisdoContract.GESTANTE_SINGLE_ROW:
                long id_gestante = ContentUris.parseId(uri);
                affected = db.delete(MovisdoContract.TGESTANTE,
                        MovisdoContract.GestanteColumns.ID_REMOTA + "=" + id_gestante
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                // Notificar cambio asociado a la uri
                resolver.
                        notifyChange(uri, null, false);
                break;
            case MovisdoContract.VISITA_INFANTE_ALLROWS:
                affected = db.delete(MovisdoContract.TVISITA_INFANTE,
                        selection,
                        selectionArgs);
                break;
            case MovisdoContract.VISITA_INFANTE_SINGLE_ROW:
                long id_visita_infante = ContentUris.parseId(uri);
                affected = db.delete(MovisdoContract.TVISITA_INFANTE,
                        MovisdoContract.VisitaInfanteColumns.ID_REMOTA + "=" + id_visita_infante
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                // Notificar cambio asociado a la uri
                resolver.
                        notifyChange(uri, null, false);
                break;
            case MovisdoContract.VISITA_GESTANTE_ALLROWS:
                affected = db.delete(MovisdoContract.TVISITA_GESTANTE,
                        selection,
                        selectionArgs);
                break;
            case MovisdoContract.VISITA_GESTANTE_SINGLE_ROW:
                long id_visita_gestante = ContentUris.parseId(uri);
                affected = db.delete(MovisdoContract.TVISITA_GESTANTE,
                        MovisdoContract.VisitaGestanteColumns.ID_REMOTA + "=" + id_visita_gestante
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                // Notificar cambio asociado a la uri
                resolver.
                        notifyChange(uri, null, false);
                break;
            case MovisdoContract.PREGUNTA_ALLROWS:
                affected = db.delete(MovisdoContract.TPREGUNTA,
                        selection,
                        selectionArgs);
                break;
            case MovisdoContract.PREGUNTA_SINGLE_ROW:
                long id_pregunta = ContentUris.parseId(uri);
                affected = db.delete(MovisdoContract.TPREGUNTA,
                        MovisdoContract.PreguntaColumns.ID_REMOTA + "=" + id_pregunta
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                // Notificar cambio asociado a la uri
                resolver.
                        notifyChange(uri, null, false);
                break;

            case MovisdoContract.ALTERNATIVA_ALLROWS:
                affected = db.delete(MovisdoContract.TALTERNATIVA,
                        selection,
                        selectionArgs);
                break;
            case MovisdoContract.ALTERNATIVA_SINGLE_ROW:
                long id_alternativa = ContentUris.parseId(uri);
                affected = db.delete(MovisdoContract.TALTERNATIVA,
                        MovisdoContract.AlternativaColumns.ID_REMOTA + "=" + id_alternativa
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                // Notificar cambio asociado a la uri
                resolver.
                        notifyChange(uri, null, false);
                break;
            case MovisdoContract.RESPUESTA_INFANTE_ALLROWS:
                affected = db.delete(MovisdoContract.TRESPUESTA_INFANTE,
                        selection,
                        selectionArgs);
                break;
            case MovisdoContract.RESPUESTA_INFANTE_SINGLE_ROW:
                long id_respuesta_infante = ContentUris.parseId(uri);
                affected = db.delete(MovisdoContract.TRESPUESTA_INFANTE,
                        MovisdoContract.RespuestaInfanteColumns.ID_REMOTA + "=" + id_respuesta_infante
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                // Notificar cambio asociado a la uri
                resolver.
                        notifyChange(uri, null, false);
                break;
            case MovisdoContract.RESPUESTA_GESTANTE_ALLROWS:
                affected = db.delete(MovisdoContract.TRESPUESTA_GESTANTE,
                        selection,
                        selectionArgs);
                break;
            case MovisdoContract.RESPUESTA_GESTANTE_SINGLE_ROW:
                long id_respuesta_gestante = ContentUris.parseId(uri);
                affected = db.delete(MovisdoContract.TRESPUESTA_GESTANTE,
                        MovisdoContract.RespuestaGestanteColumns.ID_REMOTA + "=" + id_respuesta_gestante
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                // Notificar cambio asociado a la uri
                resolver.
                        notifyChange(uri, null, false);
                break;
            case MovisdoContract.LLAMADA_VISITA_INFANTE_ALLROWS:
                affected = db.delete(MovisdoContract.TLLAMADA_VISITA_INFANTE,
                        selection,
                        selectionArgs);
                break;
            case MovisdoContract.LLAMADA_VISITA_INFANTE_SINGLE_ROW:
                long id_llamada_visita_infante = ContentUris.parseId(uri);
                affected = db.delete(MovisdoContract.TLLAMADA_VISITA_INFANTE,
                        MovisdoContract.LlamadaVisitaInfanteColumns.ID_REMOTA + "=" + id_llamada_visita_infante
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                // Notificar cambio asociado a la uri
                resolver.
                        notifyChange(uri, null, false);
                break;
            case MovisdoContract.LLAMADA_VISITA_GESTANTE_ALLROWS:
                affected = db.delete(MovisdoContract.TLLAMADA_VISITA_GESTANTE,
                        selection,
                        selectionArgs);
                break;
            case MovisdoContract.LLAMADA_VISITA_GESTANTE_SINGLE_ROW:
                long id_llamada_visita_gestante = ContentUris.parseId(uri);
                affected = db.delete(MovisdoContract.TLLAMADA_VISITA_GESTANTE,
                        MovisdoContract.LlamadaVisitaGestanteColumns.ID_REMOTA + "=" + id_llamada_visita_gestante
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                // Notificar cambio asociado a la uri
                resolver.
                        notifyChange(uri, null, false);
                break;
            default:
                throw new IllegalArgumentException("Elemento encuestado desconocido: " +
                        uri);

        }
        return affected;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int affected;
        switch (MovisdoContract.uriMatcher.match(uri)) {
            case MovisdoContract.ENCUESTADO_ALLROWS:
                affected = db.update(MovisdoContract.TENCUESTADO, values,
                        selection, selectionArgs);
                break;
            case MovisdoContract.ENCUESTADO_SINGLE_ROW:
                String id = uri.getPathSegments().get(1);
                affected = db.update(MovisdoContract.TENCUESTADO, values,
                        MovisdoContract.EncuestadoColumns.ID_REMOTA + "=" + id
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            case MovisdoContract.INFANTE_ALLROWS:
                affected = db.update(MovisdoContract.TINFANTE, values,
                        selection, selectionArgs);
                break;
            case MovisdoContract.INFANTE_SINGLE_ROW:
                String id_infante = uri.getPathSegments().get(1);
                affected = db.update(MovisdoContract.TINFANTE, values,
                        MovisdoContract.InfanteColumns.ID_REMOTA + "=" + id_infante
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            case MovisdoContract.GESTANTE_ALLROWS:
                affected = db.update(MovisdoContract.TGESTANTE, values,
                        selection, selectionArgs);
                break;
            case MovisdoContract.GESTANTE_SINGLE_ROW:
                String id_gestante = uri.getPathSegments().get(1);
                affected = db.update(MovisdoContract.TGESTANTE, values,
                        MovisdoContract.GestanteColumns.ID_REMOTA + "=" + id_gestante
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            case MovisdoContract.VISITA_INFANTE_ALLROWS:
                affected = db.update(MovisdoContract.TVISITA_INFANTE, values,
                        selection, selectionArgs);
                break;
            case MovisdoContract.VISITA_INFANTE_SINGLE_ROW:
                String id_visita_infante = uri.getPathSegments().get(1);
                affected = db.update(MovisdoContract.TVISITA_INFANTE, values,
                        MovisdoContract.VisitaInfanteColumns.ID_REMOTA + "=" + id_visita_infante
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            case MovisdoContract.VISITA_GESTANTE_ALLROWS:
                affected = db.update(MovisdoContract.TVISITA_GESTANTE, values,
                        selection, selectionArgs);
                break;
            case MovisdoContract.VISITA_GESTANTE_SINGLE_ROW:
                String id_visita_gestante = uri.getPathSegments().get(1);
                affected = db.update(MovisdoContract.TVISITA_GESTANTE, values,
                        MovisdoContract.VisitaGestanteColumns.ID_REMOTA + "=" + id_visita_gestante
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            case MovisdoContract.PREGUNTA_ALLROWS:
                affected = db.update(MovisdoContract.TPREGUNTA, values,
                        selection, selectionArgs);
                break;
            case MovisdoContract.PREGUNTA_SINGLE_ROW:
                String id_pregunta = uri.getPathSegments().get(1);
                affected = db.update(MovisdoContract.TPREGUNTA, values,
                        MovisdoContract.PreguntaColumns.ID_REMOTA + "=" + id_pregunta
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            case MovisdoContract.ALTERNATIVA_ALLROWS:
                affected = db.update(MovisdoContract.TALTERNATIVA, values,
                        selection, selectionArgs);
                break;
            case MovisdoContract.ALTERNATIVA_SINGLE_ROW:
                String id_alternativa = uri.getPathSegments().get(1);
                affected = db.update(MovisdoContract.TALTERNATIVA, values,
                        MovisdoContract.AlternativaColumns.ID_REMOTA + "=" + id_alternativa
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            case MovisdoContract.RESPUESTA_INFANTE_ALLROWS:
                affected = db.update(MovisdoContract.TRESPUESTA_INFANTE, values,
                        selection, selectionArgs);
                break;
            case MovisdoContract.RESPUESTA_INFANTE_SINGLE_ROW:
                String id_respuesta_infante = uri.getPathSegments().get(1);
                affected = db.update(MovisdoContract.TRESPUESTA_INFANTE, values,
                        MovisdoContract.RespuestaInfanteColumns.ID_REMOTA + "=" + id_respuesta_infante
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            case MovisdoContract.RESPUESTA_GESTANTE_ALLROWS:
                affected = db.update(MovisdoContract.TRESPUESTA_GESTANTE, values,
                        selection, selectionArgs);
                break;
            case MovisdoContract.RESPUESTA_GESTANTE_SINGLE_ROW:
                String id_respuesta_gestante = uri.getPathSegments().get(1);
                affected = db.update(MovisdoContract.TRESPUESTA_GESTANTE, values,
                        MovisdoContract.RespuestaGestanteColumns.ID_REMOTA + "=" + id_respuesta_gestante
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            case MovisdoContract.LLAMADA_VISITA_INFANTE_ALLROWS:
                affected = db.update(MovisdoContract.TLLAMADA_VISITA_INFANTE, values,
                        selection, selectionArgs);
                break;
            case MovisdoContract.LLAMADA_VISITA_INFANTE_SINGLE_ROW:
                String id_llamada_visita_infante = uri.getPathSegments().get(1);
                affected = db.update(MovisdoContract.TLLAMADA_VISITA_INFANTE, values,
                        MovisdoContract.LlamadaVisitaInfanteColumns.ID_REMOTA + "=" + id_llamada_visita_infante
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            case MovisdoContract.LLAMADA_VISITA_GESTANTE_ALLROWS:
                affected = db.update(MovisdoContract.TLLAMADA_VISITA_GESTANTE, values,
                        selection, selectionArgs);
                break;
            case MovisdoContract.LLAMADA_VISITA_GESTANTE_SINGLE_ROW:
                String id_llamada_visita_gestante = uri.getPathSegments().get(1);
                affected = db.update(MovisdoContract.TLLAMADA_VISITA_GESTANTE, values,
                        MovisdoContract.LlamadaVisitaGestanteColumns.ID_REMOTA + "=" + id_llamada_visita_gestante
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("URI desconocida: " + uri);
        }
        resolver.notifyChange(uri, null, false);
        return affected;
    }

}