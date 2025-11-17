package com.backendLevelup.Backend.service;

import com.backendLevelup.Backend.exceptions.ResourceNotFoundException;
import com.backendLevelup.Backend.model.Categoria;
import com.backendLevelup.Backend.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public Categoria crearCategoria(Categoria categoria) {

        // Validar que no exista una categoría con el mismo nombre
        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new RuntimeException("La categoría ya existe");
        }

        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoría no encontrada con id: " + id)
                );
    }

    @Override
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria actualizarCategoria(Long id, Categoria categoriaActualizada) {

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoría no encontrada con id: " + id)
                );

        categoria.setNombre(categoriaActualizada.getNombre());

        return categoriaRepository.save(categoria);
    }

    @Override
    public void eliminarCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoría no encontrada con id: " + id)
                );

        categoriaRepository.delete(categoria);
    }
}
