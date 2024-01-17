package com.arsltech.developer.MovisdoApp.utils;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;


import com.arsltech.developer.MovisdoApp.provider.MovisdoContract;

import org.json.JSONException;
import org.json.JSONObject;

public class Utilidades {
    // Indices para las columnas indicadas en la proyección
    public static class EncuestadoIndexColumns {
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

    public static class InfanteIndexColumns {
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

    public static class GestanteIndexColumns {
        public static final int COLUMNA_ID = 0;
        public static final int COLUMNA_ID_REMOTA = 1;
        public static final int COLUMNA_FECHA_PARTO = 2;
        public static final int COLUMNA_ESTAB_SALUD = 3;
        public static final int COLUMNA_SEXO_BEBE = 4;
        public static final int COLUMNA_ID_ENCUESTADO = 5;
    }

    public static class VisitaInfanteIndexColumns {
        public static final int COLUMNA_ID = 0;
        public static final int COLUMNA_ID_REMOTA = 1;
        public static final int COLUMNA_FECHA = 2;
        public static final int COLUMNA_MOD_VISITA = 3;
        public static final int COLUMNA_LATITUD = 4;
        public static final int COLUMNA_LONGITUD = 5;
        public static final int COLUMNA_ID_INFANTE = 6;
    }

    public static class VisitaGestanteIndexColumns {
        public static final int COLUMNA_ID = 0;
        public static final int COLUMNA_ID_REMOTA = 1;
        public static final int COLUMNA_FECHA = 2;
        public static final int COLUMNA_MOD_VISITA = 3;
        public static final int COLUMNA_LATITUD = 4;
        public static final int COLUMNA_LONGITUD = 5;
        public static final int COLUMNA_ID_GESTANTE = 6;
    }

    public static class PreguntaIndexColumns {
        public static final int COLUMNA_ID = 0;
        public static final int COLUMNA_ID_REMOTA = 1;
        public static final int COLUMNA_PREGUNTA = 2;
        public static final int COLUMNA_AREA = 3;
        public static final int COLUMNA_CATEGORIA = 4;
        public static final int COLUMNA_SUG_TEMPORAL = 5;
    }

    public static class AlternativaIndexColumns {
        public static final int COLUMNA_ID = 0;
        public static final int COLUMNA_ID_REMOTA = 1;
        public static final int COLUMNA_ALTERNATIVA = 2;
        public static final int COLUMNA_ID_PREGUNTA = 3;
    }

    public static class RespuestaInfanteIndexColumns {
        public static final int COLUMNA_ID = 0;
        public static final int COLUMNA_ID_REMOTA = 1;
        public static final int COLUMNA_ID_ALTERNATIVA = 2;
        public static final int COLUMNA_ID_VISITA_INFANTE = 3;
        public static final int COLUMNA_DETALLE = 4;
    }

    public static class RespuestaGestanteIndexColumns {
        public static final int COLUMNA_ID = 0;
        public static final int COLUMNA_ID_REMOTA = 1;
        public static final int COLUMNA_ID_ALTERNATIVA = 2;
        public static final int COLUMNA_ID_VISITA_GESTANTE = 3;
        public static final int COLUMNA_DETALLE = 4;
    }

    public static class LlamadaVisitaInfanteIndexColumns {
        public static final int COLUMNA_ID = 0;
        public static final int COLUMNA_ID_REMOTA = 1;
        public static final int COLUMNA_DATOS_LLAMADA = 2;
        public static final int COLUMNA_DURACION = 3;
        public static final int COLUMNA_ID_VISITA_INFANTE = 4;
    }

    public static class LlamadaVisitaGestanteIndexColumns {
        public static final int COLUMNA_ID = 0;
        public static final int COLUMNA_ID_REMOTA = 1;
        public static final int COLUMNA_DATOS_LLAMADA = 2;
        public static final int COLUMNA_DURACION = 3;
        public static final int COLUMNA_ID_VISITA_GESTANTE = 4;
    }


    /**
     * Determina si la aplicación corre en versiones superiores o iguales
     * a Android LOLLIPOP
     *
     * @return booleano de confirmación
     */
    public static boolean materialDesign() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Copia los datos de un gasto almacenados en un cursor hacia un
     * JSONObject
     *
     * @param c cursor
     * @return objeto jason
     */
    public static JSONObject ConvertEncuestadoCursorToJSONObject(Cursor c) {
        JSONObject jObject = new JSONObject();
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

        nombre = c.getString(EncuestadoIndexColumns.COLUMNA_NOMBRE);
        apPaterno = c.getString(EncuestadoIndexColumns.COLUMNA_APPATERNO);
        apMaterno = c.getString(EncuestadoIndexColumns.COLUMNA_APMATERNO);
        dni = c.getString(EncuestadoIndexColumns.COLUMNA_DNI);
        fecha_nacimiento = c.getString(EncuestadoIndexColumns.COLUMNA_FECHA_NACIMIENTO);
        celular = c.getString(EncuestadoIndexColumns.COLUMNA_CELULAR);
        direccion = c.getString(EncuestadoIndexColumns.COLUMNA_DIRECCION);
        ref_vivienda = c.getString(EncuestadoIndexColumns.COLUMNA_REF_VIVIENDA);
        categoria = c.getString(EncuestadoIndexColumns.COLUMNA_CATEGORIA);
        id_promotor = c.getString(EncuestadoIndexColumns.COLUMNA_ID_PROMOTOR);


        try {
            jObject.put(MovisdoContract.EncuestadoColumns.NOMBRE, nombre);
            jObject.put(MovisdoContract.EncuestadoColumns.APPATERNO, apPaterno);
            jObject.put(MovisdoContract.EncuestadoColumns.APMATERNO, apMaterno);
            jObject.put(MovisdoContract.EncuestadoColumns.DNI, dni);
            jObject.put(MovisdoContract.EncuestadoColumns.FECHA_NACIMIENTO, fecha_nacimiento);
            jObject.put(MovisdoContract.EncuestadoColumns.CELULAR, celular);
            jObject.put(MovisdoContract.EncuestadoColumns.DIRECCION, direccion);
            jObject.put(MovisdoContract.EncuestadoColumns.REF_VIVIENDA, ref_vivienda);
            jObject.put(MovisdoContract.EncuestadoColumns.CATEGORIA, categoria);
            jObject.put(MovisdoContract.EncuestadoColumns.ID_PROMOTOR, id_promotor);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;
    }

    public static JSONObject ConvertInfanteCursorToJSONObject(Cursor c) {
        JSONObject jObject = new JSONObject();
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

        nombre = c.getString(InfanteIndexColumns.COLUMNA_NOMBRE);
        apPaterno = c.getString(InfanteIndexColumns.COLUMNA_APPATERNO);
        apMaterno = c.getString(InfanteIndexColumns.COLUMNA_APMATERNO);
        dni = c.getString(InfanteIndexColumns.COLUMNA_DNI);
        fecha_nacimiento = c.getString(InfanteIndexColumns.COLUMNA_FECHA_NACIMIENTO);
        sexo = c.getString(InfanteIndexColumns.COLUMNA_SEXO);
        estab_salud = c.getString(InfanteIndexColumns.COLUMNA_ESTAB_SALUD);
        prematuro = c.getString(InfanteIndexColumns.COLUMNA_PREMATURO);
        categoria = c.getString(InfanteIndexColumns.COLUMNA_CATEGORIA);
        id_encuestado = c.getString(InfanteIndexColumns.COLUMNA_ID_ENCUESTADO);


        try {
            jObject.put(MovisdoContract.InfanteColumns.NOMBRE, nombre);
            jObject.put(MovisdoContract.InfanteColumns.APPATERNO, apPaterno);
            jObject.put(MovisdoContract.InfanteColumns.APMATERNO, apMaterno);
            jObject.put(MovisdoContract.InfanteColumns.DNI, dni);
            jObject.put(MovisdoContract.InfanteColumns.FECHA_NACIMIENTO, fecha_nacimiento);
            jObject.put(MovisdoContract.InfanteColumns.SEXO, sexo);
            jObject.put(MovisdoContract.InfanteColumns.ESTAB_SALUD, estab_salud);
            jObject.put(MovisdoContract.InfanteColumns.PREMATURO, prematuro);
            jObject.put(MovisdoContract.InfanteColumns.CATEGORIA, categoria);
            jObject.put(MovisdoContract.InfanteColumns.ID_ENCUESTADO, id_encuestado);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;
    }

    public static JSONObject ConvertGestanteCursorToJSONObject(Cursor c) {
        JSONObject jObject = new JSONObject();
        String fecha_parto;
        String estab_salud;
        String sexo_bebe;
        String id_encuestado;

        fecha_parto = c.getString(GestanteIndexColumns.COLUMNA_FECHA_PARTO);
        estab_salud = c.getString(GestanteIndexColumns.COLUMNA_ESTAB_SALUD);
        sexo_bebe = c.getString(GestanteIndexColumns.COLUMNA_SEXO_BEBE);
        id_encuestado = c.getString(GestanteIndexColumns.COLUMNA_ID_ENCUESTADO);


        try {
            jObject.put(MovisdoContract.GestanteColumns.FECHA_PARTO, fecha_parto);
            jObject.put(MovisdoContract.GestanteColumns.ESTAB_SALUD, estab_salud);
            jObject.put(MovisdoContract.GestanteColumns.SEXO_BEBE, sexo_bebe);
            jObject.put(MovisdoContract.GestanteColumns.ID_ENCUESTADO, id_encuestado);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;
    }

    public static JSONObject ConvertVisitaInfanteCursorToJSONObject(Cursor c) {
        JSONObject jObject = new JSONObject();
        String fecha;
        String mod_visita;
        String latitud;
        String longitud;
        String id_infante;

        fecha = c.getString(VisitaInfanteIndexColumns.COLUMNA_FECHA);
        mod_visita = c.getString(VisitaInfanteIndexColumns.COLUMNA_MOD_VISITA);
        latitud = c.getString(VisitaInfanteIndexColumns.COLUMNA_LATITUD);
        longitud = c.getString(VisitaInfanteIndexColumns.COLUMNA_LONGITUD);
        id_infante = c.getString(VisitaInfanteIndexColumns.COLUMNA_ID_INFANTE);


        try {
            jObject.put(MovisdoContract.VisitaInfanteColumns.FECHA, fecha);
            jObject.put(MovisdoContract.VisitaInfanteColumns.MOD_VISITA, mod_visita);
            jObject.put(MovisdoContract.VisitaInfanteColumns.LATITUD, latitud);
            jObject.put(MovisdoContract.VisitaInfanteColumns.LONGITUD, longitud);
            jObject.put(MovisdoContract.VisitaInfanteColumns.ID_INFANTE, id_infante);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;
    }

    public static JSONObject ConvertVisitaInfanteCursorToJSONObjectForDelete(Cursor c) {
        JSONObject jObject = new JSONObject();
        String id;
        String fecha;
        String mod_visita;
        String latitud;
        String longitud;
        String id_infante;

        id = c.getString(VisitaInfanteIndexColumns.COLUMNA_ID_REMOTA);
        fecha = c.getString(VisitaInfanteIndexColumns.COLUMNA_FECHA);
        mod_visita = c.getString(VisitaInfanteIndexColumns.COLUMNA_MOD_VISITA);
        latitud = c.getString(VisitaInfanteIndexColumns.COLUMNA_LATITUD);
        longitud = c.getString(VisitaInfanteIndexColumns.COLUMNA_LONGITUD);
        id_infante = c.getString(VisitaInfanteIndexColumns.COLUMNA_ID_INFANTE);


        try {
            jObject.put(MovisdoContract.VisitaInfanteColumns.ID_REMOTA, id);
            jObject.put(MovisdoContract.VisitaInfanteColumns.FECHA, fecha);
            jObject.put(MovisdoContract.VisitaInfanteColumns.MOD_VISITA, mod_visita);
            jObject.put(MovisdoContract.VisitaInfanteColumns.LATITUD, latitud);
            jObject.put(MovisdoContract.VisitaInfanteColumns.LONGITUD, longitud);
            jObject.put(MovisdoContract.VisitaInfanteColumns.ID_INFANTE, id_infante);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;
    }

    public static JSONObject ConvertVisitaGestanteCursorToJSONObjectForDelete(Cursor c) {
        JSONObject jObject = new JSONObject();
        String id;
        String fecha;
        String mod_visita;
        String latitud;
        String longitud;
        String id_gestante;

        id = c.getString(VisitaGestanteIndexColumns.COLUMNA_ID_REMOTA);
        fecha = c.getString(VisitaGestanteIndexColumns.COLUMNA_FECHA);
        mod_visita = c.getString(VisitaGestanteIndexColumns.COLUMNA_MOD_VISITA);
        latitud = c.getString(VisitaGestanteIndexColumns.COLUMNA_LATITUD);
        longitud = c.getString(VisitaGestanteIndexColumns.COLUMNA_LONGITUD);
        id_gestante = c.getString(VisitaGestanteIndexColumns.COLUMNA_ID_GESTANTE);


        try {
            jObject.put(MovisdoContract.VisitaGestanteColumns.ID_REMOTA, id);
            jObject.put(MovisdoContract.VisitaGestanteColumns.FECHA, fecha);
            jObject.put(MovisdoContract.VisitaGestanteColumns.MOD_VISITA, mod_visita);
            jObject.put(MovisdoContract.VisitaGestanteColumns.LATITUD, latitud);
            jObject.put(MovisdoContract.VisitaGestanteColumns.LONGITUD, longitud);
            jObject.put(MovisdoContract.VisitaGestanteColumns.ID_GESTANTE, id_gestante);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;
    }

    public static JSONObject ConvertRespuestaInfanteCursorToJSONObjectForDelete(Cursor c) {
        JSONObject jObject = new JSONObject();
        String id;
        String id_alternativa;
        String id_visita_infante;
        String detalle;

        id=c.getString(RespuestaInfanteIndexColumns.COLUMNA_ID_REMOTA);
        id_alternativa = c.getString(RespuestaInfanteIndexColumns.COLUMNA_ID_ALTERNATIVA);
        id_visita_infante = c.getString(RespuestaInfanteIndexColumns.COLUMNA_ID_VISITA_INFANTE);
        detalle = c.getString(RespuestaInfanteIndexColumns.COLUMNA_DETALLE);

        try {
            jObject.put(MovisdoContract.RespuestaInfanteColumns.ID_REMOTA, id);
            jObject.put(MovisdoContract.RespuestaInfanteColumns.ID_ALTERNATIVA, id_alternativa);
            jObject.put(MovisdoContract.RespuestaInfanteColumns.ID_VISITA_INFANTE, id_visita_infante);
            jObject.put(MovisdoContract.RespuestaInfanteColumns.DETALLE, detalle);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;
    }

    public static JSONObject ConvertRespuestaGestanteCursorToJSONObjectForDelete(Cursor c) {
        JSONObject jObject = new JSONObject();
        String id;
        String id_alternativa;
        String id_visita_gestante;
        String detalle;

        id=c.getString(RespuestaGestanteIndexColumns.COLUMNA_ID_REMOTA);
        id_alternativa = c.getString(RespuestaGestanteIndexColumns.COLUMNA_ID_ALTERNATIVA);
        id_visita_gestante = c.getString(RespuestaGestanteIndexColumns.COLUMNA_ID_VISITA_GESTANTE);
        detalle = c.getString(RespuestaGestanteIndexColumns.COLUMNA_DETALLE);


        try {
            jObject.put(MovisdoContract.RespuestaGestanteColumns.ID_REMOTA, id);
            jObject.put(MovisdoContract.RespuestaGestanteColumns.ID_ALTERNATIVA, id_alternativa);
            jObject.put(MovisdoContract.RespuestaGestanteColumns.ID_VISITA_GESTANTE, id_visita_gestante);
            jObject.put(MovisdoContract.RespuestaGestanteColumns.DETALLE, detalle);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;
    }

    public static JSONObject ConvertVisitaGestanteCursorToJSONObject(Cursor c) {
        JSONObject jObject = new JSONObject();
        String fecha;
        String mod_visita;
        String latitud;
        String longitud;
        String id_gestante;

        fecha = c.getString(VisitaGestanteIndexColumns.COLUMNA_FECHA);
        mod_visita = c.getString(VisitaGestanteIndexColumns.COLUMNA_MOD_VISITA);
        latitud = c.getString(VisitaGestanteIndexColumns.COLUMNA_LATITUD);
        longitud = c.getString(VisitaGestanteIndexColumns.COLUMNA_LONGITUD);
        id_gestante = c.getString(VisitaGestanteIndexColumns.COLUMNA_ID_GESTANTE);


        try {
            jObject.put(MovisdoContract.VisitaGestanteColumns.FECHA, fecha);
            jObject.put(MovisdoContract.VisitaGestanteColumns.MOD_VISITA, mod_visita);
            jObject.put(MovisdoContract.VisitaGestanteColumns.LATITUD, latitud);
            jObject.put(MovisdoContract.VisitaGestanteColumns.LONGITUD, longitud);
            jObject.put(MovisdoContract.VisitaGestanteColumns.ID_GESTANTE, id_gestante);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;
    }

    public static JSONObject ConvertPreguntaCursorToJSONObject(Cursor c) {
        JSONObject jObject = new JSONObject();
        String pregunta;
        String area;
        String categoria;
        String sug_temporal;

        pregunta = c.getString(PreguntaIndexColumns.COLUMNA_PREGUNTA);
        area = c.getString(PreguntaIndexColumns.COLUMNA_AREA);
        categoria = c.getString(PreguntaIndexColumns.COLUMNA_CATEGORIA);
        sug_temporal = c.getString(PreguntaIndexColumns.COLUMNA_SUG_TEMPORAL);


        try {
            jObject.put(MovisdoContract.PreguntaColumns.PREGUNTA, pregunta);
            jObject.put(MovisdoContract.PreguntaColumns.AREA, area);
            jObject.put(MovisdoContract.PreguntaColumns.CATEGORIA, categoria);
            jObject.put(MovisdoContract.PreguntaColumns.SUG_TEMPORAL, sug_temporal);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;
    }

    public static JSONObject ConvertAlternativaCursorToJSONObject(Cursor c) {
        JSONObject jObject = new JSONObject();
        String alternativa;
        String id_pregunta;

        alternativa = c.getString(AlternativaIndexColumns.COLUMNA_ALTERNATIVA);
        id_pregunta = c.getString(AlternativaIndexColumns.COLUMNA_ID_PREGUNTA);


        try {
            jObject.put(MovisdoContract.AlternativaColumns.ALTERNATIVA, alternativa);
            jObject.put(MovisdoContract.AlternativaColumns.ID_PREGUNTA, id_pregunta);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;
    }

    public static JSONObject ConvertRespuestaInfanteCursorToJSONObject(Cursor c) {
        JSONObject jObject = new JSONObject();
        String id_alternativa;
        String id_visita_infante;
        String detalle;

        id_alternativa = c.getString(RespuestaInfanteIndexColumns.COLUMNA_ID_ALTERNATIVA);
        id_visita_infante = c.getString(RespuestaInfanteIndexColumns.COLUMNA_ID_VISITA_INFANTE);
        detalle = c.getString(RespuestaInfanteIndexColumns.COLUMNA_DETALLE);


        try {
            jObject.put(MovisdoContract.RespuestaInfanteColumns.ID_ALTERNATIVA, id_alternativa);
            jObject.put(MovisdoContract.RespuestaInfanteColumns.ID_VISITA_INFANTE, id_visita_infante);
            jObject.put(MovisdoContract.RespuestaInfanteColumns.DETALLE, detalle);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;
    }

    public static JSONObject ConvertRespuestaGestanteCursorToJSONObject(Cursor c) {
        JSONObject jObject = new JSONObject();
        String id_alternativa;
        String id_visita_gestante;
        String detalle;

        id_alternativa = c.getString(RespuestaGestanteIndexColumns.COLUMNA_ID_ALTERNATIVA);
        id_visita_gestante = c.getString(RespuestaGestanteIndexColumns.COLUMNA_ID_VISITA_GESTANTE);
        detalle = c.getString(RespuestaGestanteIndexColumns.COLUMNA_DETALLE);


        try {
            jObject.put(MovisdoContract.RespuestaGestanteColumns.ID_ALTERNATIVA, id_alternativa);
            jObject.put(MovisdoContract.RespuestaGestanteColumns.ID_VISITA_GESTANTE, id_visita_gestante);
            jObject.put(MovisdoContract.RespuestaGestanteColumns.DETALLE, detalle);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;
    }

    public static JSONObject ConvertLlamadaVisitaInfanteCursorToJSONObject(Cursor c) {
        JSONObject jObject = new JSONObject();
        String datos_llamada;
        String duracion;
        String id_visita_infante;

        datos_llamada = c.getString(LlamadaVisitaInfanteIndexColumns.COLUMNA_DATOS_LLAMADA);
        duracion = c.getString(LlamadaVisitaInfanteIndexColumns.COLUMNA_DURACION);
        id_visita_infante = c.getString(LlamadaVisitaInfanteIndexColumns.COLUMNA_ID_VISITA_INFANTE);


        try {
            jObject.put(MovisdoContract.LlamadaVisitaInfanteColumns.DATOS_LLAMADA, datos_llamada);
            jObject.put(MovisdoContract.LlamadaVisitaInfanteColumns.DURACION, duracion);
            jObject.put(MovisdoContract.LlamadaVisitaInfanteColumns.ID_VISITA_INFANTE, id_visita_infante);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;
    }

    public static JSONObject ConvertLlamadaVisitaGestanteCursorToJSONObject(Cursor c) {
        JSONObject jObject = new JSONObject();
        String datos_llamada;
        String duracion;
        String id_visita_gestante;

        datos_llamada = c.getString(LlamadaVisitaGestanteIndexColumns.COLUMNA_DATOS_LLAMADA);
        duracion = c.getString(LlamadaVisitaGestanteIndexColumns.COLUMNA_DURACION);
        id_visita_gestante = c.getString(LlamadaVisitaGestanteIndexColumns.COLUMNA_ID_VISITA_GESTANTE);


        try {
            jObject.put(MovisdoContract.LlamadaVisitaGestanteColumns.DATOS_LLAMADA, datos_llamada);
            jObject.put(MovisdoContract.LlamadaVisitaGestanteColumns.DURACION, duracion);
            jObject.put(MovisdoContract.LlamadaVisitaGestanteColumns.ID_VISITA_GESTANTE, id_visita_gestante);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;
    }


}