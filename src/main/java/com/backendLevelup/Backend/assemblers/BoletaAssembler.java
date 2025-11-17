package com.backendLevelup.Backend.assemblers;

import com.backendLevelup.Backend.dtos.BoletaDTO;
import com.backendLevelup.Backend.model.Boleta;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BoletaAssembler {

    private final BoletaDetalleAssembler detalleAssembler;

    public BoletaAssembler(BoletaDetalleAssembler detalleAssembler) {
        this.detalleAssembler = detalleAssembler;
    }

    public BoletaDTO toDTO(Boleta boleta) {
        if (boleta == null) return null;

        BoletaDTO dto = new BoletaDTO();
        dto.setId(boleta.getId());
        dto.setUsuarioId(boleta.getUsuario() != null ? boleta.getUsuario().getId() : null);
        dto.setFecha(boleta.getFecha());
        dto.setTotal(boleta.getTotal());

        if (boleta.getDetalles() != null) {
            dto.setDetalles(
                    boleta.getDetalles()
                            .stream()
                            .map(detalleAssembler::toDTO)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }
}
