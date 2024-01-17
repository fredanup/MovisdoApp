package com.arsltech.developer.MovisdoApp.model;

public class EncuestadoModel {
    public String id, nombre, apPaterno,apMaterno,dni,fecha_nacimiento,celular,direccion,ref_vivienda,categoria,id_promotor;

    public EncuestadoModel(String id, String nombre, String apPaterno, String apMaterno, String dni, String fecha_nacimiento, String celular, String direccion, String ref_vivienda, String categoria, String id_promotor) {
        this.id = id;
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.dni = dni;
        this.fecha_nacimiento = fecha_nacimiento;
        this.celular = celular;
        this.direccion = direccion;
        this.ref_vivienda = ref_vivienda;
        this.categoria = categoria;
        this.id_promotor = id_promotor;
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

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRef_vivienda() {
        return ref_vivienda;
    }

    public void setRef_vivienda(String ref_vivienda) {
        this.ref_vivienda = ref_vivienda;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getId_promotor() {
        return id_promotor;
    }

    public void setId_promotor(String id_promotor) {
        this.id_promotor = id_promotor;
    }
}
