package com.arsltech.developer.MovisdoApp.model;

public class RespuestaGestanteModel {
    public String id, id_alternativa,id_visita_gestante,detalle;

    public RespuestaGestanteModel(String id, String id_alternativa, String id_visita_gestante, String detalle) {
        this.id = id;
        this.id_alternativa = id_alternativa;
        this.id_visita_gestante = id_visita_gestante;
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

    public String getId_visita_gestante() {
        return id_visita_gestante;
    }

    public void setId_visita_gestante(String id_visita_gestante) {
        this.id_visita_gestante = id_visita_gestante;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}
