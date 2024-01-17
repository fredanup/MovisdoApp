package com.arsltech.developer.MovisdoApp.model;

public class PreguntaGestanteModel {
    public String id, pregunta, area, categoria,sug_temporal, estado;


    public PreguntaGestanteModel(String id, String pregunta, String area, String categoria, String sug_temporal, String estado) {
        this.id = id;
        this.pregunta = pregunta;
        this.area = area;
        this.categoria = categoria;
        this.sug_temporal = sug_temporal;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSug_temporal() {
        return sug_temporal;
    }

    public void setSug_temporal(String sug_temporal) {
        this.sug_temporal = sug_temporal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
