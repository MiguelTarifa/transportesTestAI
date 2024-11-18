package com.migueltarifa.transportesTestAI.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.time.LocalDateTime;

@Entity
@Table(name = "cargas")
public class Carga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carga")
    private int idCarga;

    @Column(name = "version")
    private String version;

    @Column(name = "nombre_fichero")
    private String nombreFichero;

    @Column(name = "fecha_carga")
    private LocalDateTime fechaCarga;

    // Constructor sin argumentos (requerido por JPA)
    public Carga() {
    }

    public Carga(String version, String nombreFichero, LocalDateTime fechaCarga) {
        this.version = version;
        this.nombreFichero = nombreFichero;
        this.fechaCarga = fechaCarga;
    }

    public int getIdCarga() {
        return idCarga;
    }

    public String getVersion() {
        return version;
    }

    public String getNombreFichero() {
        return nombreFichero;
    }

    public LocalDateTime getFechaCarga() {
        return fechaCarga;
    }

    public void setIdCarga(int idCarga) {
        this.idCarga = idCarga;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setNombreFichero(String nombreFichero) {
        this.nombreFichero = nombreFichero;
    }

    public void setFechaCarga(LocalDateTime fechaCarga) {
        this.fechaCarga = fechaCarga;
    }
}
