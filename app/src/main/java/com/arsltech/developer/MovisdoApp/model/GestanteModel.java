package com.arsltech.developer.MovisdoApp.model;

public class GestanteModel {
    public String id,fecha_parto,estab_salud,sexo_bebe,id_encuestado,encuestadoDisplayName;

    public GestanteModel(String id, String fecha_parto, String estab_salud, String sexo_bebe, String id_encuestado,String encuestadoDisplayName) {
        this.id = id;
        this.fecha_parto = fecha_parto;
        this.estab_salud = estab_salud;
        this.sexo_bebe = sexo_bebe;
        this.id_encuestado = id_encuestado;
        this.encuestadoDisplayName=encuestadoDisplayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha_parto() {
        return fecha_parto;
    }

    public void setFecha_parto(String fecha_parto) {
        this.fecha_parto = fecha_parto;
    }

    public String getEstab_salud() {
        return estab_salud;
    }

    public void setEstab_salud(String estab_salud) {
        this.estab_salud = estab_salud;
    }

    public String getSexo_bebe() {
        if(sexo_bebe.matches("0")){
            return "Femenino";
        }
        else {
            return "Masculino";
        }

    }

    public void setSexo_bebe(String sexo_bebe) {
        this.sexo_bebe = sexo_bebe;
    }

    public String getId_encuestado() {
        return id_encuestado;
    }

    public void setId_encuestado(String id_encuestado) {
        this.id_encuestado = id_encuestado;
    }

    public String getEncuestadoDisplayName() {
        return encuestadoDisplayName;
    }

    public void setEncuestadoDisplayName(String encuestadoDisplayName) {
        this.encuestadoDisplayName = encuestadoDisplayName;
    }
}
