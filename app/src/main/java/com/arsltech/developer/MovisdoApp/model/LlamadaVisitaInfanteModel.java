package com.arsltech.developer.MovisdoApp.model;

public class LlamadaVisitaInfanteModel {
    public String id, datos_llamada,duracion, id_visita_infante, infanteDisplayName, tipoLlamada;


    public LlamadaVisitaInfanteModel(String id, String datos_llamada, String duracion, String id_visita_infante, String infanteDisplayName, String tipoLlamada) {
        this.id = id;
        this.datos_llamada = datos_llamada;
        this.duracion = duracion;
        this.id_visita_infante = id_visita_infante;
        this.infanteDisplayName = infanteDisplayName;
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

    public String getId_visita_infante() {
        return id_visita_infante;
    }

    public void setId_visita_infante(String id_visita_infante) {
        this.id_visita_infante = id_visita_infante;
    }

    public String getInfanteDisplayName() {
        return infanteDisplayName;
    }

    public void setInfanteDisplayName(String infanteDisplayName) {
        this.infanteDisplayName = infanteDisplayName;
    }

    public String getTipoLlamada() {
        return tipoLlamada;
    }

    public void setTipoLlamada(String tipoLlamada) {
        this.tipoLlamada = tipoLlamada;
    }
}
