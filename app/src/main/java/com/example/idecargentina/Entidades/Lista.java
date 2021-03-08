package com.example.idecargentina.Entidades;

public class Lista {
    public int codlista;
    public int imagen;
    public String nombre;
    public String nombre2;

    public Lista(int codlista, int imagen, String nombre, String nombre2) {
        this.codlista = codlista;
        this.imagen = imagen;
        this.nombre = nombre;
        this.nombre2 = nombre2;
    }

    public Lista() {
    }

    public Lista(int codlista) {
        this.codlista = codlista;
    }

    public int getCodlista() {
        return codlista;
    }

    public void setCodlista(int codlista) {
        this.codlista = codlista;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }
}
