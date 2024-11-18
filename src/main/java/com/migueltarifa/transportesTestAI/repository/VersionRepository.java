package com.migueltarifa.transportesTestAI.repository;

import com.migueltarifa.transportesTestAI.model.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VersionRepository extends JpaRepository<Version, Long> {
    // No es necesario definir métodos aquí para las operaciones básicas
    // Spring Data JPA ya proporciona métodos como save() y findAll()

    // Metodo que verifica si un registro con idVersion ya existe
    boolean existsByIdVersion(String idVersion);
}
