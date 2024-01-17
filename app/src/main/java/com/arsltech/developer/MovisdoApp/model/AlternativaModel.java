package com.arsltech.developer.MovisdoApp.model;

public class AlternativaModel {
    public String id,alternativa,id_pregunta;

    public AlternativaModel(String id, String alternativa, String id_pregunta) {
        this.id = id;
        this.alternativa = alternativa;
        this.id_pregunta = id_pregunta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlternativa() {
        return alternativa;
    }

    public void setAlternativa(String alternativa) {
        this.alternativa = alternativa;
    }

    public String getId_pregunta() {
        return id_pregunta;
    }

    public void setId_pregunta(String id_pregunta) {
        this.id_pregunta = id_pregunta;
    }
}
