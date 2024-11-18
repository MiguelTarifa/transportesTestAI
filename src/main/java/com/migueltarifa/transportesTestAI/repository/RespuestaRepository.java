package com.migueltarifa.transportesTestAI.repository;

import com.migueltarifa.transportesTestAI.model.Carga;
import com.migueltarifa.transportesTestAI.model.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {
    // No es necesario definir métodos aquí para las operaciones básicas
    // Spring Data JPA ya proporciona métodos como save() y findAll()

}
