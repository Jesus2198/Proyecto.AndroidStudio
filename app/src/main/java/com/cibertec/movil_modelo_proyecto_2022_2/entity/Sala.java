package com.cibertec.movil_modelo_proyecto_2022_2.entity;


import java.io.Serializable;

public class Sala implements Serializable {

    private int idSala;
    private String numero;
    private String piso;
    private String numAlumnos;
    private String recursos;
    private String fechaRegistro;
    private int estado;
    private Sede sede;


    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getNumAlumnos() {
        return numAlumnos;
    }

    public void setNumAlumnos(String numAlumnos) {
        this.numAlumnos = numAlumnos;
    }

    public String getRecursos() {
        return recursos;
    }

    public void setRecursos(String recursos) {
        this.recursos = recursos;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }
}
