package com.backendLevelup.Backend.repository;

import com.backendLevelup.Backend.model.CarritoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoDetalleRepository extends JpaRepository<CarritoDetalle, Long> {
    // Por ahora no necesitamos métodos adicionales, JpaRepository ya tiene save, delete y findById
}
