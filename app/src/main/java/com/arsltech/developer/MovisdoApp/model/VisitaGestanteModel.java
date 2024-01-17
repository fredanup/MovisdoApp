package com.arsltech.developer.MovisdoApp.model;

public class VisitaGestanteModel {
    public String id, fecha,mod_visita, latitud, longitud,id_gestante,encuestadoDisplayName;

    public VisitaGestanteModel(String id, String fecha, String mod_visita, String latitud, String longitud, String id_gestante,String encuestadoDisplayName) {
        this.id = id;
        this.fecha = fecha;
        this.mod_visita = mod_visita;
        this.latitud = latitud;
        this.longitud = longitud;
        this.id_gestante = id_gestante;
        this.encuestadoDisplayName=encuestadoDisplayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMod_visita() {
        return mod_visita;
    }

    public void setMod_visita(String mod_visita) {
        this.mod_visita = mod_visita;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getId_gestante() {
        return id_gestante;
    }

    public void setId_gestante(String id_gestante) {
        this.id_gestante = id_gestante;
    }

    public String getEncuestadoDisplayName() {
        return encuestadoDisplayName;
    }

    public void setEncuestadoDisplayName(String encuestadoDisplayName) {
        this.encuestadoDisplayName = encuestadoDisplayName;
    }
}
