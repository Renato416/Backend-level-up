package com.backendLevelup.Backend.repository;

import com.backendLevelup.Backend.model.Boleta;
import com.backendLevelup.Backend.model.BoletaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoletaDetalleRepository extends JpaRepository<BoletaDetalle, Long> {

    List<BoletaDetalle> findByBoleta(Boleta boleta);
}
