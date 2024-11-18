package com.migueltarifa.transportesTestAI.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import java.time.LocalDateTime;

@Entity
@Table(name = "versiones")
@Schema(description = "Modelo que representa una versión")
public class Version {

    @Id
    @Column(name = "id_version")
    @Schema(description = "Identificador único de la versión", example = "2024")
    private String idVersion;

    @Column(name = "fecha_creacion")
    @Schema(description = "Fecha de creación de la versión", example = "2023-11-10T10:15:30")
    private LocalDateTime fechaCreacion;

    // Constructor sin argumentos (requerido por JPA)
    public Version() {
    }

    // Constructor con argumentos (opcional)
    public Version(String idVersion) {
        this.idVersion = idVersion;
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters y setters
    public String getIdVersion() {
        return idVersion;
    }

    public void setIdVersion(String idVersion) {
        this.idVersion = idVersion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
