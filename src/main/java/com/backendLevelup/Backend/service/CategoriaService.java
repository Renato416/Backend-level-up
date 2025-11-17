package com.backendLevelup.Backend.service;

import com.backendLevelup.Backend.model.Categoria;
import java.util.List;

public interface CategoriaService {

    Categoria crearCategoria(Categoria categoria);

    Categoria obtenerCategoriaPorId(Long id);

    List<Categoria> listarCategorias();

    Categoria actualizarCategoria(Long id, Categoria categoria);

    void eliminarCategoria(Long id);
}
