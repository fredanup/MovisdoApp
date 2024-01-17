package com.arsltech.developer.MovisdoApp.model;

public class VisitaInfanteModel {
    public String id, fecha, mod_visita,latitud,longitud,id_infante, infanteDisplayName;

    public VisitaInfanteModel(String id, String fecha, String mod_visita, String latitud, String longitud, String id_infante, String infanteDisplayName) {
        this.id = id;
        this.fecha = fecha;
        this.mod_visita = mod_visita;
        this.latitud = latitud;
        this.longitud = longitud;
        this.id_infante = id_infante;
        this.infanteDisplayName = infanteDisplayName;
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

    public String getId_infante() {
        return id_infante;
    }

    public void setId_infante(String id_infante) {
        this.id_infante = id_infante;
    }

    public String getInfanteDisplayName() {
        return infanteDisplayName;
    }

    public void setInfanteDisplayName(String infanteDisplayName) {
        this.infanteDisplayName = infanteDisplayName;
    }
}
