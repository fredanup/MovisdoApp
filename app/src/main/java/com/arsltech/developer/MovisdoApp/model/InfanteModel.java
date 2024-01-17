package com.arsltech.developer.MovisdoApp.model;

public class InfanteModel {
    public String id, nombre, apPaterno,apMaterno,dni,fecha_nacimiento,sexo,estab_salud,prematuro,categoria,id_encuestado;


    public InfanteModel(String id, String nombre, String apPaterno, String apMaterno, String dni, String fecha_nacimiento, String sexo, String estab_salud, String prematuro, String categoria, String id_encuestado) {
        this.id = id;
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.dni = dni;
        this.fecha_nacimiento = fecha_nacimiento;
        this.sexo = sexo;
        this.estab_salud = estab_salud;
        this.prematuro = prematuro;
        this.categoria = categoria;
        this.id_encuestado = id_encuestado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getSexo() {
        String genero="";
        if(sexo.contains("0")){
            genero= "Femenino";
        }
        if(sexo.contains("1")){
            genero= "Masculino";
        }
        return genero;
    }

    public void setSexo(String sexo) {
        this.sexo=sexo;
    }

    public String getEstab_salud() {
        return estab_salud;
    }

    public void setEstab_salud(String estab_salud) {
        this.estab_salud = estab_salud;
    }

    public String getPrematuro() {
        return prematuro;
    }

    public void setPrematuro(String prematuro) {
        this.prematuro = prematuro;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getId_encuestado() {
        return id_encuestado;
    }

    public void setId_encuestado(String id_encuestado) {
        this.id_encuestado = id_encuestado;
    }
}
