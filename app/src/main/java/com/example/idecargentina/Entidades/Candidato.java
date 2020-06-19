package com.example.idecargentina.Entidades;

import java.io.Serializable;

public class Candidato implements Serializable {

    int codcandidato;
    String nombre;
    String apellido;
    String mail;
    String telefono;
    int codusuario;

    public Candidato(int codcandidato, String nombre, String apellido, String mail, String telefono, int codusuario) {
        this.codcandidato = codcandidato;
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.telefono = telefono;
        this.codusuario = codusuario;
    }

    public Candidato() {
    }

    public int getCodcandidato() {
        return codcandidato;
    }

    public void setCodcandidato(int codcandidato) {
        this.codcandidato = codcandidato;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getCodusuario() {
        return codusuario;
    }

    public void setCodusuario(int codusuario) {
        this.codusuario = codusuario;
    }
}
