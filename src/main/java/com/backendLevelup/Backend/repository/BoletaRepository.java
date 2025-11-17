package com.backendLevelup.Backend.repository;

import com.backendLevelup.Backend.model.Boleta;
import com.backendLevelup.Backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {

    List<Boleta> findByUsuario(Usuario usuario);
}
