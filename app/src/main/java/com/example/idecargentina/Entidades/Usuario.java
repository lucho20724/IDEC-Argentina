package com.example.idecargentina.Entidades;

import java.io.Serializable;

public class Usuario implements Serializable {
    int codusuario;
    String nombre;
    String apellido;
    String mail;
    String password;
    int nroalumno;
    String telefono;
    int codrol;
    int codcampo;

    public Usuario(int codusuario, String nombre, String apellido, String mail, String password, int nroalumno, String telefono, int codrol, int codcampo) {
        this.codusuario = codusuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.password = password;
        this.nroalumno = nroalumno;
        this.telefono = telefono;
        this.codrol = codrol;
        this.codcampo = codcampo;
    }

    public Usuario() {
    }

    public int getCodusuario() {
        return codusuario;
    }

    public void setCodusuario(int codusuario) {
        this.codusuario = codusuario;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getNroalumno() {
        return nroalumno;
    }

    public void setNroalumno(int nroalumno) {
        this.nroalumno = nroalumno;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getCodrol() {
        return codrol;
    }

    public void setCodrol(int codrol) {
        this.codrol = codrol;
    }

    public int getCodcampo() {
        return codcampo;
    }

    public void setCodcampo(int codcampo) {
        this.codcampo = codcampo;
    }
}


