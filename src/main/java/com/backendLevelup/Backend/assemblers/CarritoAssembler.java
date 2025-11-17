package com.backendLevelup.Backend.assemblers;

import com.backendLevelup.Backend.dtos.CarritoDTO;
import com.backendLevelup.Backend.dtos.CarritoDetalleDTO;
import com.backendLevelup.Backend.model.Carrito;
import com.backendLevelup.Backend.model.CarritoDetalle;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarritoAssembler {

    private final CarritoDetalleAssembler detalleAssembler;

    public CarritoAssembler(CarritoDetalleAssembler detalleAssembler) {
        this.detalleAssembler = detalleAssembler;
    }

    // Convierte de entidad a DTO
    public CarritoDTO toDTO(Carrito carrito) {
        if (carrito == null) return null;

        CarritoDTO dto = new CarritoDTO();
        dto.setId(carrito.getId());
        dto.setUsuarioId(carrito.getUsuario() != null ? carrito.getUsuario().getId() : null);

        List<CarritoDetalleDTO> detallesDTO = carrito.getDetalles().stream()
                .map(detalleAssembler::toDTO) // referencia de método válida aquí
                .collect(Collectors.toList());

        dto.setDetalles(detallesDTO);
        return dto;
    }

    // Convierte de DTO a entidad
    public Carrito toEntity(CarritoDTO dto) {
        if (dto == null) return null;

        Carrito carrito = new Carrito();
        carrito.setId(dto.getId());
        // El usuario normalmente se asigna en el servicio usando usuarioRepository
        // carrito.setUsuario(usuarioRepository.findById(dto.getUsuarioId()).orElse(null));

        // Aquí usamos lambda para pasar el Carrito padre al assembler de detalle
        List<CarritoDetalle> detalles = dto.getDetalles().stream()
                .map(detalleDTO -> detalleAssembler.toEntity(detalleDTO, carrito))
                .collect(Collectors.toList());

        carrito.setDetalles(detalles);
        return carrito;
    }
}
