package com.backendLevelup.Backend.dtos;

import lombok.*;
import java.time.OffsetDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoletaDTO {

    private Long id;
    private Long usuarioId;
    private OffsetDateTime fecha;
    private Long total;

    private List<BoletaDetalleDTO> detalles;

    // --- AGREGADO MANUALMENTE PARA CORREGIR EL ERROR ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    // ---------------------------------------------------
}