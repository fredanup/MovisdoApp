package com.arsltech.developer.MovisdoApp.utils;

public class Constantes {

    /**
     * Puerto que utilizas para la conexión.
     * Dejalo en blanco si no has configurado esta característica.
     */
//    private static final String PUERTO_HOST = ":63343";
    private static final String PUERTO_HOST = "";

    /**
     * Dirección IP de genymotion o AVD
     */
    private static final String IP = "http://kuwayo.com";

    /**
     * URLs del Web Service
     */
    public static final String GET_URL_ENCUESTADO = "http://kuwayo.com/bdmovisdoAPI/obtener_encuestados.php";
    public static final String INSERT_URL_ENCUESTADO = IP + PUERTO_HOST + "/bdmovisdoAPI/insertar_encuestado.php";

    public static final String GET_URL_INFANTE = "http://kuwayo.com/bdmovisdoAPI/obtener_infantes.php";
    public static final String INSERT_URL_INFANTE = IP + PUERTO_HOST + "/bdmovisdoAPI/insertar_infante.php";

    public static final String GET_URL_GESTANTE = "http://kuwayo.com/bdmovisdoAPI/obtener_gestantes.php";
    public static final String INSERT_URL_GESTANTE = IP + PUERTO_HOST + "/bdmovisdoAPI/insertar_gestante.php";

    public static final String GET_URL_VISITA_INFANTE = "http://kuwayo.com/bdmovisdoAPI/obtener_visita_infantes.php";
    public static final String INSERT_URL_VISITA_INFANTE = IP + PUERTO_HOST + "/bdmovisdoAPI/insertar_visita_infante.php";
    public static final String DELETE_URL_VISITA_INFANTE = IP + PUERTO_HOST + "/bdmovisdoAPI/eliminar_visita_infante.php";

    public static final String GET_URL_VISITA_GESTANTE = "http://kuwayo.com/bdmovisdoAPI/obtener_visita_gestantes.php";
    public static final String INSERT_URL_VISITA_GESTANTE = IP + PUERTO_HOST + "/bdmovisdoAPI/insertar_visita_gestante.php";
    public static final String DELETE_URL_VISITA_GESTANTE = IP + PUERTO_HOST + "/bdmovisdoAPI/eliminar_visita_gestante.php";

    public static final String GET_URL_PREGUNTA = "http://kuwayo.com/bdmovisdoAPI/obtener_preguntas.php";
    public static final String INSERT_URL_PREGUNTA = IP + PUERTO_HOST + "/bdmovisdoAPI/insertar_pregunta.php";

    public static final String GET_URL_ALTERNATIVA = "http://kuwayo.com/bdmovisdoAPI/obtener_alternativas.php";
    public static final String INSERT_URL_ALTERNATIVA = IP + PUERTO_HOST + "/bdmovisdoAPI/insertar_alternativa.php";

    public static final String GET_URL_RESPUESTA_INFANTE = "http://kuwayo.com/bdmovisdoAPI/obtener_respuesta_infantes.php";
    public static final String INSERT_URL_RESPUESTA_INFANTE = IP + PUERTO_HOST + "/bdmovisdoAPI/insertar_respuesta_infante.php";
    public static final String DELETE_URL_RESPUESTA_INFANTE = IP + PUERTO_HOST + "/bdmovisdoAPI/eliminar_respuesta_infante.php";

    public static final String GET_URL_RESPUESTA_GESTANTE = "http://kuwayo.com/bdmovisdoAPI/obtener_respuesta_gestantes.php";
    public static final String INSERT_URL_RESPUESTA_GESTANTE = IP + PUERTO_HOST + "/bdmovisdoAPI/insertar_respuesta_gestante.php";
    public static final String DELETE_URL_RESPUESTA_GESTANTE = IP + PUERTO_HOST + "/bdmovisdoAPI/eliminar_respuesta_gestante.php";

    public static final String GET_URL_LLAMADA_VISITA_INFANTE = "http://kuwayo.com/bdmovisdoAPI/obtener_llamada_visita_infantes.php";
    public static final String INSERT_URL_LLAMADA_VISITA_INFANTE = IP + PUERTO_HOST + "/bdmovisdoAPI/insertar_llamada_visita_infante.php";

    public static final String GET_URL_LLAMADA_VISITA_GESTANTE = "http://kuwayo.com/bdmovisdoAPI/obtener_llamada_visita_gestantes.php";
    public static final String INSERT_URL_LLAMADA_VISITA_GESTANTE = IP + PUERTO_HOST + "/bdmovisdoAPI/insertar_llamada_visita_gestante.php";

    /**
     * Campos de las respuestas Json
     */
    public static final String ID = "id";
    public static final String ESTADO = "estado";
    public static final String MENSAJE = "mensaje";

    public static final String ENCUESTADOS = "encuestados";
    public static final String INFANTES = "infantes";
    public static final String GESTANTES = "gestantes";

    public static final String VISITA_INFANTES = "visita_infantes";
    public static final String VISITA_GESTANTES = "visita_gestantes";

    public static final String PREGUNTAS = "preguntas";
    public static final String ALTERNATIVAS = "alternativas";
    public static final String RESPUESTA_INFANTES = "respuesta_infantes";
    public static final String RESPUESTA_GESTANTES = "respuesta_gestantes";

    public static final String LLAMADA_VISITA_INFANTES = "llamada_visita_infantes";
    public static final String LLAMADA_VISITA_GESTANTES = "llamada_visita_gestantes";



    /**
     * Códigos del campo {@link ESTADO}
     */
    public static final String SUCCESS = "1";
    public static final String FAILED = "2";

    /**
     * Tipo de cuenta para la sincronización
     */
    public static final String ACCOUNT_TYPE = "com.arsltech.developer.MovisdoApp.account";


}