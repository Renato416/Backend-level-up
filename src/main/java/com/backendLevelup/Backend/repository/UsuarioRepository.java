package com.backendLevelup.Backend.repository;

import com.backendLevelup.Backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar por run
    boolean existsByRun(String run);
    Optional<Usuario> findByRun(String run);

    // Buscar por correo
    boolean existsByCorreoElectronico(String correoElectronico);
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);
}
