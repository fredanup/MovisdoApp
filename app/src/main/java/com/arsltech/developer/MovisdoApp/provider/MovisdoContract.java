package com.arsltech.developer.MovisdoApp.provider;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract Class entre el provider y las aplicaciones
 */
public class MovisdoContract {
    /**
     * Autoridad del Content Provider
     */
    public final static String AUTHORITY
            = "com.arsltech.developer.MovisdoApp";
    /**
     * Representación de la tabla a consultar
     */
    public static final String TENCUESTADO = "tencuestado";
    public static final String TINFANTE = "tinfante";
    public static final String TGESTANTE = "tgestante";
    public static final String TVISITA_INFANTE = "tvisita_infante";
    public static final String TVISITA_GESTANTE = "tvisita_gestante";
    public static final String TPREGUNTA = "tpregunta";
    public static final String TALTERNATIVA = "talternativa";
    public static final String TRESPUESTA_INFANTE = "trespuesta_infante";
    public static final String TRESPUESTA_GESTANTE = "trespuesta_gestante";
    public static final String TLLAMADA_VISITA_INFANTE = "tllamada_visita_infante";
    public static final String TLLAMADA_VISITA_GESTANTE = "tllamada_visita_gestante";

    public static final String TENCUESTADO_PHONE_DETAILS = "tencuestado_phone_details";
    public static final String TGESTANTE_PHONE_DETAILS = "tgestante_phone_details";
    public static final String TALTERNATIVA_ELEGIDA_DETAILS = "talternativa_elegida_details";
    public static final String TALTERNATIVA_ELEGIDA2_DETAILS = "talternativa_elegida2_details";


    public static Uri GET_CONTENT_URI(String table) {
         Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/"+table);
         return CONTENT_URI;
    }

    public static String getSingleMime(String table) {
        String SINGLE_MIME =
                "vnd.android.cursor.item/vnd." + AUTHORITY;
        if (table != null) {
            return SINGLE_MIME + table;
        } else {
            return null;
        }
    }

    public static String getMultipleMime(String table) {
        String MULTIPLE_MIME =
                "vnd.android.cursor.dir/vnd." + AUTHORITY;
        if (table != null) {
            return MULTIPLE_MIME + table;
        } else {
            return null;
        }
    }

    /**
     * Comparador de URIs de contenido
     */

    public static final UriMatcher uriMatcher;
    /**
     * Código para URIs de multiples registros
     */
    public static final int ENCUESTADO_ALLROWS = 1;
    public static final int ENCUESTADO_SINGLE_ROW = 2;
    public static final int INFANTE_ALLROWS = 3;
    public static final int INFANTE_SINGLE_ROW = 4;
    public static final int GESTANTE_ALLROWS = 5;
    public static final int GESTANTE_SINGLE_ROW = 6;
    public static final int VISITA_INFANTE_ALLROWS = 7;
    public static final int VISITA_INFANTE_SINGLE_ROW = 8;
    public static final int VISITA_GESTANTE_ALLROWS = 9;
    public static final int VISITA_GESTANTE_SINGLE_ROW = 10;
    public static final int PREGUNTA_ALLROWS = 11;
    public static final int PREGUNTA_SINGLE_ROW = 12;
    public static final int ALTERNATIVA_ALLROWS = 13;
    public static final int ALTERNATIVA_SINGLE_ROW = 14;
    public static final int RESPUESTA_INFANTE_ALLROWS = 15;
    public static final int RESPUESTA_INFANTE_SINGLE_ROW = 16;
    public static final int RESPUESTA_GESTANTE_ALLROWS = 17;
    public static final int RESPUESTA_GESTANTE_SINGLE_ROW = 18;
    public static final int LLAMADA_VISITA_INFANTE_ALLROWS = 19;
    public static final int LLAMADA_VISITA_INFANTE_SINGLE_ROW = 20;
    public static final int LLAMADA_VISITA_GESTANTE_ALLROWS = 21;
    public static final int LLAMADA_VISITA_GESTANTE_SINGLE_ROW = 22;

    public static final int ENCUESTADO_PHONE_DETAILS_ALLROWS=23;
    public static final int GESTANTE_PHONE_DETAILS_ALLROWS=24;
    public static final int ALTERNATIVA_ELEGIDA_DETAILS_ALLROWS=25;
    public static final int ALTERNATIVA_ELEGIDA2_DETAILS_ALLROWS=26;


    // Asignación de URIs
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, TENCUESTADO, ENCUESTADO_ALLROWS);
        uriMatcher.addURI(AUTHORITY, TENCUESTADO + "/#", ENCUESTADO_SINGLE_ROW);

        uriMatcher.addURI(AUTHORITY, TINFANTE, INFANTE_ALLROWS);
        uriMatcher.addURI(AUTHORITY, TINFANTE + "/#", INFANTE_SINGLE_ROW);

        uriMatcher.addURI(AUTHORITY, TGESTANTE, GESTANTE_ALLROWS);
        uriMatcher.addURI(AUTHORITY, TGESTANTE + "/#", GESTANTE_SINGLE_ROW);

        uriMatcher.addURI(AUTHORITY, TVISITA_INFANTE, VISITA_INFANTE_ALLROWS);
        uriMatcher.addURI(AUTHORITY, TVISITA_INFANTE + "/#", VISITA_INFANTE_SINGLE_ROW);

        uriMatcher.addURI(AUTHORITY, TVISITA_GESTANTE, VISITA_GESTANTE_ALLROWS);
        uriMatcher.addURI(AUTHORITY, TVISITA_GESTANTE + "/#", VISITA_GESTANTE_SINGLE_ROW);

        uriMatcher.addURI(AUTHORITY, TPREGUNTA, PREGUNTA_ALLROWS);
        uriMatcher.addURI(AUTHORITY, TPREGUNTA + "/#", PREGUNTA_SINGLE_ROW);

        uriMatcher.addURI(AUTHORITY, TALTERNATIVA, ALTERNATIVA_ALLROWS);
        uriMatcher.addURI(AUTHORITY, TALTERNATIVA + "/#", ALTERNATIVA_SINGLE_ROW);

        uriMatcher.addURI(AUTHORITY, TRESPUESTA_INFANTE, RESPUESTA_INFANTE_ALLROWS);
        uriMatcher.addURI(AUTHORITY, TRESPUESTA_INFANTE + "/#", RESPUESTA_INFANTE_SINGLE_ROW);

        uriMatcher.addURI(AUTHORITY, TRESPUESTA_GESTANTE, RESPUESTA_GESTANTE_ALLROWS);
        uriMatcher.addURI(AUTHORITY, TRESPUESTA_GESTANTE + "/#", RESPUESTA_GESTANTE_SINGLE_ROW);

        uriMatcher.addURI(AUTHORITY, TLLAMADA_VISITA_INFANTE, LLAMADA_VISITA_INFANTE_ALLROWS);
        uriMatcher.addURI(AUTHORITY, TLLAMADA_VISITA_INFANTE + "/#", LLAMADA_VISITA_INFANTE_SINGLE_ROW);

        uriMatcher.addURI(AUTHORITY, TLLAMADA_VISITA_GESTANTE, LLAMADA_VISITA_GESTANTE_ALLROWS);
        uriMatcher.addURI(AUTHORITY, TLLAMADA_VISITA_GESTANTE + "/#", LLAMADA_VISITA_GESTANTE_SINGLE_ROW);

        uriMatcher.addURI(AUTHORITY, TENCUESTADO_PHONE_DETAILS, ENCUESTADO_PHONE_DETAILS_ALLROWS);
        uriMatcher.addURI(AUTHORITY, TGESTANTE_PHONE_DETAILS, GESTANTE_PHONE_DETAILS_ALLROWS);
        uriMatcher.addURI(AUTHORITY, TALTERNATIVA_ELEGIDA_DETAILS, ALTERNATIVA_ELEGIDA_DETAILS_ALLROWS);
        uriMatcher.addURI(AUTHORITY, TALTERNATIVA_ELEGIDA2_DETAILS, ALTERNATIVA_ELEGIDA2_DETAILS_ALLROWS);


    }


    //Join attributes
    public static final String EncuestadoJoinInfante=MovisdoContract.TENCUESTADO+" INNER JOIN "+MovisdoContract.TINFANTE+" ON "+MovisdoContract.TINFANTE+"."+
            MovisdoContract.InfanteColumns.ID_ENCUESTADO+" = "+MovisdoContract.TENCUESTADO+"."+ MovisdoContract.EncuestadoColumns.ID_REMOTA;

    public static final String EncuestadoJoinGestante=MovisdoContract.TENCUESTADO+" INNER JOIN "+MovisdoContract.TGESTANTE+" ON "+MovisdoContract.TGESTANTE+"."+
            MovisdoContract.GestanteColumns.ID_ENCUESTADO+" = "+MovisdoContract.TENCUESTADO+"."+ MovisdoContract.EncuestadoColumns.ID_REMOTA;

    /**
     * Unión de alternativa con respuesta, obtiene todas las alternativas que fueron seleccionadas como respuestas de todas las visitas cuyo infante tiene el ID?
     */
    public static final String AlternativaJoinRespuestaInfante=MovisdoContract.TALTERNATIVA+" INNER JOIN "+MovisdoContract.TRESPUESTA_INFANTE+" ON "+MovisdoContract.TALTERNATIVA+"."+
            AlternativaColumns.ID_REMOTA+" = "+MovisdoContract.TRESPUESTA_INFANTE+"."+ RespuestaInfanteColumns.ID_ALTERNATIVA+" INNER JOIN "+MovisdoContract.TVISITA_INFANTE+" ON "+
            MovisdoContract.TRESPUESTA_INFANTE+"."+RespuestaInfanteColumns.ID_VISITA_INFANTE+" = "+MovisdoContract.TVISITA_INFANTE+"."+VisitaInfanteColumns.ID_REMOTA;

    public static final String AlternativaJoinRespuestaGestante=MovisdoContract.TALTERNATIVA+" INNER JOIN "+MovisdoContract.TRESPUESTA_GESTANTE+" ON "+MovisdoContract.TALTERNATIVA+"."+
            AlternativaColumns.ID_REMOTA+" = "+MovisdoContract.TRESPUESTA_GESTANTE+"."+ RespuestaGestanteColumns.ID_ALTERNATIVA+" INNER JOIN "+MovisdoContract.TVISITA_GESTANTE+" ON "+
            MovisdoContract.TRESPUESTA_GESTANTE+"."+RespuestaGestanteColumns.ID_VISITA_GESTANTE+" = "+MovisdoContract.TVISITA_GESTANTE+"."+VisitaGestanteColumns.ID_REMOTA;


    // Valores para la columna ESTADO
    public static final int ESTADO_OK = 0;
    public static final int ESTADO_SYNC = 1;


    /**
     * Estructura de la tabla
     */
    public static class EncuestadoColumns implements BaseColumns {

        private EncuestadoColumns() {
            // Sin instancias
        }

        public final static String NOMBRE  = "nombre";
        public final static String APPATERNO  = "apPaterno";
        public final static String APMATERNO  = "apMaterno";
        public final static String DNI  = "dni";
        public final static String FECHA_NACIMIENTO   = "fecha_nacimiento";
        public final static String CELULAR   = "celular";
        public final static String DIRECCION   = "direccion";
        public final static String REF_VIVIENDA   = "ref_vivienda";
        public final static String CATEGORIA   = "categoria";
        public final static String ID_PROMOTOR   = "id_promotor";


        public static final String ESTADO = "estado";
        public static final String ID_REMOTA = "idRemota";
        public final static String PENDIENTE_INSERCION = "pendiente_insercion";

    }
    public static class InfanteColumns implements BaseColumns {

        private InfanteColumns() {
            // Sin instancias
        }

        public final static String NOMBRE  = "nombre";
        public final static String APPATERNO  = "apPaterno";
        public final static String APMATERNO  = "apMaterno";
        public final static String DNI  = "dni";
        public final static String FECHA_NACIMIENTO   = "fecha_nacimiento";
        public final static String SEXO   = "sexo";
        public final static String ESTAB_SALUD   = "estab_salud";
        public final static String PREMATURO   = "prematuro";
        public final static String CATEGORIA   = "categoria";
        public final static String ID_ENCUESTADO   = "id_encuestado";


        public static final String ESTADO = "estado";
        public static final String ID_REMOTA = "idRemota";
        public final static String PENDIENTE_INSERCION = "pendiente_insercion";

    }

    public static class GestanteColumns implements BaseColumns {

        private GestanteColumns() {
            // Sin instancias
        }

        public final static String FECHA_PARTO  = "fecha_parto";
        public final static String ESTAB_SALUD  = "estab_salud";
        public final static String SEXO_BEBE  = "sexo_bebe";
        public final static String ID_ENCUESTADO  = "id_encuestado";


        public static final String ESTADO = "estado";
        public static final String ID_REMOTA = "idRemota";
        public final static String PENDIENTE_INSERCION = "pendiente_insercion";

    }

    public static class VisitaInfanteColumns implements BaseColumns {

        public VisitaInfanteColumns() {
        }

        public final static String FECHA  = "fecha";
        public final static String MOD_VISITA  = "mod_visita";
        public final static String LATITUD  = "latitud";
        public final static String LONGITUD  = "longitud";
        public final static String ID_INFANTE  = "id_infante";


        public static final String ESTADO = "estado";
        public static final String ID_REMOTA = "idRemota";
        public final static String PENDIENTE_INSERCION = "pendiente_insercion";
        public final static String PENDIENTE_ELIMINACION = "pendiente_eliminacion";

    }

    public static class VisitaGestanteColumns implements BaseColumns {

        public VisitaGestanteColumns() {
        }

        public final static String FECHA  = "fecha";
        public final static String MOD_VISITA  = "mod_visita";
        public final static String LATITUD  = "latitud";
        public final static String LONGITUD  = "longitud";
        public final static String ID_GESTANTE  = "id_gestante";


        public static final String ESTADO = "estado";
        public static final String ID_REMOTA = "idRemota";
        public final static String PENDIENTE_INSERCION = "pendiente_insercion";
        public final static String PENDIENTE_ELIMINACION = "pendiente_eliminacion";

    }

    public static class PreguntaColumns implements BaseColumns {

        public PreguntaColumns() {
        }

        public final static String PREGUNTA  = "pregunta";
        public final static String AREA  = "area";
        public final static String CATEGORIA  = "categoria";
        public final static String SUG_TEMPORAL  = "sug_temporal";


        public static final String ESTADO = "estado";
        public static final String ID_REMOTA = "idRemota";
        public final static String PENDIENTE_INSERCION = "pendiente_insercion";

    }

    public static class AlternativaColumns implements BaseColumns {

        public AlternativaColumns() {
        }

        public final static String ALTERNATIVA  = "alternativa";
        public final static String ID_PREGUNTA  = "id_pregunta";

        public static final String ESTADO = "estado";
        public static final String ID_REMOTA = "idRemota";
        public final static String PENDIENTE_INSERCION = "pendiente_insercion";

    }

    public static class RespuestaInfanteColumns implements BaseColumns {

        public RespuestaInfanteColumns() {
        }

        public final static String ID_ALTERNATIVA  = "id_alternativa";
        public final static String ID_VISITA_INFANTE  = "id_visita_infante";
        public final static String DETALLE  = "detalle";


        public static final String ESTADO = "estado";
        public static final String ID_REMOTA = "idRemota";
        public final static String PENDIENTE_ELIMINACION = "pendiente_eliminacion";
        public final static String PENDIENTE_INSERCION = "pendiente_insercion";

    }

    public static class RespuestaGestanteColumns implements BaseColumns {

        public RespuestaGestanteColumns() {
        }

        public final static String ID_ALTERNATIVA  = "id_alternativa";
        public final static String ID_VISITA_GESTANTE  = "id_visita_gestante";
        public final static String DETALLE  = "detalle";


        public static final String ESTADO = "estado";
        public static final String ID_REMOTA = "idRemota";
        public final static String PENDIENTE_ELIMINACION = "pendiente_eliminacion";
        public final static String PENDIENTE_INSERCION = "pendiente_insercion";

    }

    public static class LlamadaVisitaInfanteColumns implements BaseColumns {

        public LlamadaVisitaInfanteColumns() {
        }

        public final static String DATOS_LLAMADA  = "datos_llamada";
        public final static String DURACION  = "duracion";
        public final static String ID_VISITA_INFANTE  = "id_visita_infante";


        public static final String ESTADO = "estado";
        public static final String ID_REMOTA = "idRemota";
        public final static String PENDIENTE_INSERCION = "pendiente_insercion";

    }

    public static class LlamadaVisitaGestanteColumns implements BaseColumns {

        public LlamadaVisitaGestanteColumns() {
        }

        public final static String DATOS_LLAMADA  = "datos_llamada";
        public final static String DURACION  = "duracion";
        public final static String ID_VISITA_GESTANTE  = "id_visita_gestante";


        public static final String ESTADO = "estado";
        public static final String ID_REMOTA = "idRemota";
        public final static String PENDIENTE_INSERCION = "pendiente_insercion";

    }
}