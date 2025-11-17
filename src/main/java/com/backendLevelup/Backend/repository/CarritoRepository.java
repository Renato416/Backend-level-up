package com.backendLevelup.Backend.repository;

import com.backendLevelup.Backend.model.Carrito;
import com.backendLevelup.Backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    // Buscar carrito por usuario
    Optional<Carrito> findByUsuario(Usuario usuario);
}
