package com.arsltech.developer.MovisdoApp.model;

public class RespuestaInfanteModel {
    public String id, id_alternativa,id_visita_infante,detalle;

    public RespuestaInfanteModel(String id, String id_alternativa, String id_visita_infante, String detalle) {
        this.id = id;
        this.id_alternativa = id_alternativa;
        this.id_visita_infante = id_visita_infante;
        this.detalle = detalle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_alternativa() {
        return id_alternativa;
    }

    public void setId_alternativa(String id_alternativa) {
        this.id_alternativa = id_alternativa;
    }

    public String getId_visita_infante() {
        return id_visita_infante;
    }

    public void setId_visita_infante(String id_visita_infante) {
        this.id_visita_infante = id_visita_infante;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}
