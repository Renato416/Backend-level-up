package com.backendLevelup.Backend.repository;

import com.backendLevelup.Backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
