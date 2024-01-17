package com.arsltech.developer.MovisdoApp.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Clase envoltura para el gestor de Bases de datos
 */
class DatabaseHelper extends SQLiteOpenHelper {


    public DatabaseHelper(Context context,
                          String name,
                          SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase database) {
        createTable(database); // Crear la tabla "gasto"
    }

    /**
     * Crear tabla en la base de datos
     *
     * @param database Instancia de la base de datos
     */
    private void createTable(SQLiteDatabase database) {
        String tencuestado = "CREATE TABLE " + MovisdoContract.TENCUESTADO + " (" +
                MovisdoContract.EncuestadoColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovisdoContract.EncuestadoColumns.NOMBRE + " TEXT, " +
                MovisdoContract.EncuestadoColumns.APPATERNO + " TEXT, " +
                MovisdoContract.EncuestadoColumns.APMATERNO + " TEXT, " +
                MovisdoContract.EncuestadoColumns.DNI + " TEXT," +
                MovisdoContract.EncuestadoColumns.FECHA_NACIMIENTO + " TEXT," +
                MovisdoContract.EncuestadoColumns.CELULAR + " TEXT," +
                MovisdoContract.EncuestadoColumns.DIRECCION + " TEXT," +
                MovisdoContract.EncuestadoColumns.REF_VIVIENDA + " TEXT," +
                MovisdoContract.EncuestadoColumns.CATEGORIA + " TEXT," +
                MovisdoContract.EncuestadoColumns.ID_PROMOTOR + " TEXT," +
                MovisdoContract.EncuestadoColumns.ID_REMOTA + " TEXT UNIQUE," +
                MovisdoContract.EncuestadoColumns.ESTADO + " INTEGER NOT NULL DEFAULT "+ MovisdoContract.ESTADO_OK+"," +
                MovisdoContract.EncuestadoColumns.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0);";

        String tinfante="CREATE TABLE " + MovisdoContract.TINFANTE + " (" +
                MovisdoContract.InfanteColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovisdoContract.InfanteColumns.NOMBRE + " TEXT, " +
                MovisdoContract.InfanteColumns.APPATERNO + " TEXT, " +
                MovisdoContract.InfanteColumns.APMATERNO + " TEXT, " +
                MovisdoContract.InfanteColumns.DNI + " TEXT," +
                MovisdoContract.InfanteColumns.FECHA_NACIMIENTO + " TEXT," +
                MovisdoContract.InfanteColumns.SEXO + " TEXT," +
                MovisdoContract.InfanteColumns.ESTAB_SALUD + " TEXT," +
                MovisdoContract.InfanteColumns.PREMATURO + " TEXT," +
                MovisdoContract.InfanteColumns.CATEGORIA + " TEXT," +
                MovisdoContract.InfanteColumns.ID_ENCUESTADO + " TEXT," +
                MovisdoContract.InfanteColumns.ID_REMOTA + " TEXT UNIQUE," +
                MovisdoContract.InfanteColumns.ESTADO + " INTEGER NOT NULL DEFAULT "+ MovisdoContract.ESTADO_OK+"," +
                MovisdoContract.InfanteColumns.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0);";

        String tgestante="CREATE TABLE " + MovisdoContract.TGESTANTE + " (" +
                MovisdoContract.GestanteColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovisdoContract.GestanteColumns.FECHA_PARTO + " TEXT, " +
                MovisdoContract.GestanteColumns.ESTAB_SALUD + " TEXT, " +
                MovisdoContract.GestanteColumns.SEXO_BEBE + " TEXT, " +
                MovisdoContract.GestanteColumns.ID_ENCUESTADO + " TEXT," +
                MovisdoContract.GestanteColumns.ID_REMOTA + " TEXT UNIQUE," +
                MovisdoContract.GestanteColumns.ESTADO + " INTEGER NOT NULL DEFAULT "+ MovisdoContract.ESTADO_OK+"," +
                MovisdoContract.GestanteColumns.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0);";

        String tvisita_infante="CREATE TABLE " + MovisdoContract.TVISITA_INFANTE + " (" +
                MovisdoContract.VisitaInfanteColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovisdoContract.VisitaInfanteColumns.FECHA + " TEXT, " +
                MovisdoContract.VisitaInfanteColumns.MOD_VISITA + " TEXT, " +
                MovisdoContract.VisitaInfanteColumns.LATITUD + " TEXT, " +
                MovisdoContract.VisitaInfanteColumns.LONGITUD + " TEXT," +
                MovisdoContract.VisitaInfanteColumns.ID_INFANTE + " TEXT," +
                MovisdoContract.VisitaInfanteColumns.ID_REMOTA + " TEXT UNIQUE," +
                MovisdoContract.VisitaInfanteColumns.ESTADO + " INTEGER NOT NULL DEFAULT "+ MovisdoContract.ESTADO_OK+"," +
                MovisdoContract.VisitaInfanteColumns.PENDIENTE_ELIMINACION + " INTEGER NOT NULL DEFAULT 0," +
                MovisdoContract.VisitaInfanteColumns.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0);";

        String tvisita_gestante="CREATE TABLE " + MovisdoContract.TVISITA_GESTANTE + " (" +
                MovisdoContract.VisitaGestanteColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovisdoContract.VisitaGestanteColumns.FECHA + " TEXT, " +
                MovisdoContract.VisitaGestanteColumns.MOD_VISITA + " TEXT, " +
                MovisdoContract.VisitaGestanteColumns.LATITUD + " TEXT, " +
                MovisdoContract.VisitaGestanteColumns.LONGITUD + " TEXT," +
                MovisdoContract.VisitaGestanteColumns.ID_GESTANTE + " TEXT," +
                MovisdoContract.VisitaGestanteColumns.ID_REMOTA + " TEXT UNIQUE," +
                MovisdoContract.VisitaGestanteColumns.ESTADO + " INTEGER NOT NULL DEFAULT "+ MovisdoContract.ESTADO_OK+"," +
                MovisdoContract.VisitaInfanteColumns.PENDIENTE_ELIMINACION + " INTEGER NOT NULL DEFAULT 0," +
                MovisdoContract.VisitaGestanteColumns.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0);";

        String tpregunta="CREATE TABLE " + MovisdoContract.TPREGUNTA + " (" +
                MovisdoContract.PreguntaColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovisdoContract.PreguntaColumns.PREGUNTA + " TEXT, " +
                MovisdoContract.PreguntaColumns.AREA + " TEXT, " +
                MovisdoContract.PreguntaColumns.CATEGORIA + " TEXT, " +
                MovisdoContract.PreguntaColumns.SUG_TEMPORAL + " TEXT," +
                MovisdoContract.PreguntaColumns.ID_REMOTA + " TEXT UNIQUE," +
                MovisdoContract.PreguntaColumns.ESTADO + " INTEGER NOT NULL DEFAULT "+ MovisdoContract.ESTADO_OK+"," +
                MovisdoContract.PreguntaColumns.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0);";

        String talternativa="CREATE TABLE " + MovisdoContract.TALTERNATIVA + " (" +
                MovisdoContract.AlternativaColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovisdoContract.AlternativaColumns.ALTERNATIVA + " TEXT, " +
                MovisdoContract.AlternativaColumns.ID_PREGUNTA + " TEXT, " +
                MovisdoContract.AlternativaColumns.ID_REMOTA + " TEXT UNIQUE," +
                MovisdoContract.AlternativaColumns.ESTADO + " INTEGER NOT NULL DEFAULT "+ MovisdoContract.ESTADO_OK+"," +
                MovisdoContract.AlternativaColumns.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0);";

        String trespuesta_infante="CREATE TABLE " + MovisdoContract.TRESPUESTA_INFANTE + " (" +
                MovisdoContract.RespuestaInfanteColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovisdoContract.RespuestaInfanteColumns.ID_ALTERNATIVA + " TEXT, " +
                MovisdoContract.RespuestaInfanteColumns.ID_VISITA_INFANTE + " TEXT, " +
                MovisdoContract.RespuestaInfanteColumns.DETALLE + " TEXT, " +
                MovisdoContract.RespuestaInfanteColumns.ID_REMOTA + " TEXT UNIQUE," +
                MovisdoContract.RespuestaInfanteColumns.ESTADO + " INTEGER NOT NULL DEFAULT "+ MovisdoContract.ESTADO_OK+"," +
                MovisdoContract.RespuestaInfanteColumns.PENDIENTE_ELIMINACION + " INTEGER NOT NULL DEFAULT 0," +
                MovisdoContract.RespuestaInfanteColumns.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0);";

        String trespuesta_gestante="CREATE TABLE " + MovisdoContract.TRESPUESTA_GESTANTE + " (" +
                MovisdoContract.RespuestaGestanteColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovisdoContract.RespuestaGestanteColumns.ID_ALTERNATIVA + " TEXT, " +
                MovisdoContract.RespuestaGestanteColumns.ID_VISITA_GESTANTE + " TEXT, " +
                MovisdoContract.RespuestaGestanteColumns.DETALLE + " TEXT, " +
                MovisdoContract.RespuestaGestanteColumns.ID_REMOTA + " TEXT UNIQUE," +
                MovisdoContract.RespuestaGestanteColumns.ESTADO + " INTEGER NOT NULL DEFAULT "+ MovisdoContract.ESTADO_OK+"," +
                MovisdoContract.RespuestaGestanteColumns.PENDIENTE_ELIMINACION + " INTEGER NOT NULL DEFAULT 0," +
                MovisdoContract.RespuestaGestanteColumns.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0);";

        String tllamada_visita_infante="CREATE TABLE " + MovisdoContract.TLLAMADA_VISITA_INFANTE + " (" +
                MovisdoContract.LlamadaVisitaInfanteColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovisdoContract.LlamadaVisitaInfanteColumns.DATOS_LLAMADA + " TEXT, " +
                MovisdoContract.LlamadaVisitaInfanteColumns.DURACION + " TEXT, " +
                MovisdoContract.LlamadaVisitaInfanteColumns.ID_VISITA_INFANTE + " TEXT, " +
                MovisdoContract.LlamadaVisitaInfanteColumns.ID_REMOTA + " TEXT UNIQUE," +
                MovisdoContract.LlamadaVisitaInfanteColumns.ESTADO + " INTEGER NOT NULL DEFAULT "+ MovisdoContract.ESTADO_OK+"," +
                MovisdoContract.LlamadaVisitaInfanteColumns.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0);";

        String tllamada_visita_gestante="CREATE TABLE " + MovisdoContract.TLLAMADA_VISITA_GESTANTE + " (" +
                MovisdoContract.LlamadaVisitaGestanteColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovisdoContract.LlamadaVisitaGestanteColumns.DATOS_LLAMADA + " TEXT, " +
                MovisdoContract.LlamadaVisitaGestanteColumns.DURACION + " TEXT, " +
                MovisdoContract.LlamadaVisitaGestanteColumns.ID_VISITA_GESTANTE + " TEXT, " +
                MovisdoContract.LlamadaVisitaGestanteColumns.ID_REMOTA + " TEXT UNIQUE," +
                MovisdoContract.LlamadaVisitaGestanteColumns.ESTADO + " INTEGER NOT NULL DEFAULT "+ MovisdoContract.ESTADO_OK+"," +
                MovisdoContract.LlamadaVisitaGestanteColumns.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0);";


        database.execSQL(tencuestado);
        database.execSQL(tinfante);
        database.execSQL(tgestante);
        database.execSQL(tvisita_infante);
        database.execSQL(tvisita_gestante);
        database.execSQL(tpregunta);
        database.execSQL(talternativa);
        database.execSQL(trespuesta_infante);
        database.execSQL(trespuesta_gestante);
        database.execSQL(tllamada_visita_infante);
        database.execSQL(tllamada_visita_gestante);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("drop table " + MovisdoContract.TENCUESTADO);
            db.execSQL("drop table " + MovisdoContract.TINFANTE);
            db.execSQL("drop table " + MovisdoContract.TGESTANTE);
            db.execSQL("drop table " + MovisdoContract.TVISITA_INFANTE);
            db.execSQL("drop table " + MovisdoContract.TVISITA_GESTANTE);
            db.execSQL("drop table " + MovisdoContract.TPREGUNTA);
            db.execSQL("drop table " + MovisdoContract.TALTERNATIVA);
            db.execSQL("drop table " + MovisdoContract.TRESPUESTA_INFANTE);
            db.execSQL("drop table " + MovisdoContract.TRESPUESTA_GESTANTE);
            db.execSQL("drop table " + MovisdoContract.TLLAMADA_VISITA_INFANTE);
            db.execSQL("drop table " + MovisdoContract.TLLAMADA_VISITA_GESTANTE);

        }
        catch (SQLiteException e) { }
        onCreate(db);
    }

}