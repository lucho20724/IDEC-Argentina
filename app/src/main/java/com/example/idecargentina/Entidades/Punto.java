package com.example.idecargentina.Entidades;

public class Punto {
    int codcampo;
    double latitud, longitud;
    String titulo, descripcion;
    int codusuario;

    public Punto(int codcampo, double latitud, double longitud, String titulo, String descripcion, int codusuario) {
        this.codcampo = codcampo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.codusuario = codusuario;
    }

    public Punto() {
    }

    public int getCodcampo() {
        return codcampo;
    }

    public void setCodcampo(int codcampo) {
        this.codcampo = codcampo;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCodusuario() {
        return codusuario;
    }

    public void setCodusuario(int codusuario) {
        this.codusuario = codusuario;
    }
}
