package com.arsltech.developer.MovisdoApp.model;

public class LlamadaVisitaGestanteModel {
    public String id, datos_llamada,duracion,id_visita_gestante, gestanteDisplayName, tipoLlamada;


    public LlamadaVisitaGestanteModel(String id, String datos_llamada, String duracion, String id_visita_gestante, String gestanteDisplayName, String tipoLlamada) {
        this.id = id;
        this.datos_llamada = datos_llamada;
        this.duracion = duracion;
        this.id_visita_gestante = id_visita_gestante;
        this.gestanteDisplayName = gestanteDisplayName;
        this.tipoLlamada = tipoLlamada;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDatos_llamada() {
        return datos_llamada;
    }

    public void setDatos_llamada(String datos_llamada) {
        this.datos_llamada = datos_llamada;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getId_visita_gestante() {
        return id_visita_gestante;
    }

    public void setId_visita_gestante(String id_visita_gestante) {
        this.id_visita_gestante = id_visita_gestante;
    }

    public String getGestanteDisplayName() {
        return gestanteDisplayName;
    }

    public void setGestanteDisplayName(String gestanteDisplayName) {
        this.gestanteDisplayName = gestanteDisplayName;
    }

    public String getTipoLlamada() {
        return tipoLlamada;
    }

    public void setTipoLlamada(String tipoLlamada) {
        this.tipoLlamada = tipoLlamada;
    }
}
