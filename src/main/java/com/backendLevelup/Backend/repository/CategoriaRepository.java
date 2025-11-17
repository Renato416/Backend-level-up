package com.backendLevelup.Backend.repository;

import com.backendLevelup.Backend.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Buscar categoría por nombre
    Optional<Categoria> findByNombre(String nombre);

    // Verificar existencia de categoría por nombre
    boolean existsByNombre(String nombre);
}
