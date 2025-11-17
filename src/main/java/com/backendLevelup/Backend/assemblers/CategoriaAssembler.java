package com.backendLevelup.Backend.assemblers;

import com.backendLevelup.Backend.dtos.CategoriaDTO;
import com.backendLevelup.Backend.model.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaAssembler {

    // Convierte de entidad a DTO
    public CategoriaDTO toDTO(Categoria categoria) {
        if (categoria == null) return null;

        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        return dto;
    }

    // Convierte de DTO a entidad
    public Categoria toEntity(CategoriaDTO dto) {
        if (dto == null) return null;

        Categoria categoria = new Categoria();
        categoria.setId(dto.getId());
        categoria.setNombre(dto.getNombre());
        return categoria;
    }
}
