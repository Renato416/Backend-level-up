package com.backendLevelup.Backend.controller;

import com.backendLevelup.Backend.assemblers.BoletaAssembler;
import com.backendLevelup.Backend.assemblers.BoletaDetalleAssembler;
import com.backendLevelup.Backend.dtos.BoletaDTO;
import com.backendLevelup.Backend.dtos.BoletaDetalleDTO;
import com.backendLevelup.Backend.model.Boleta;
import com.backendLevelup.Backend.model.BoletaDetalle;
import com.backendLevelup.Backend.service.BoletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/boletas")
@Tag(name = "Boletas", description = "Gestión de órdenes de compra y boletas")
public class BoletaController {

    private final BoletaService boletaService;
    private final BoletaAssembler boletaAssembler;
    private final BoletaDetalleAssembler boletaDetalleAssembler;

    public BoletaController(
            BoletaService boletaService,
            BoletaAssembler boletaAssembler,
            BoletaDetalleAssembler boletaDetalleAssembler) {

        this.boletaService = boletaService;
        this.boletaAssembler = boletaAssembler;
        this.boletaDetalleAssembler = boletaDetalleAssembler;
    }

    // ----------------------------
    // CREAR BOLETA (CLIENTE / ADMIN)
    // ----------------------------
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<EntityModel<BoletaDTO>> crearBoleta(
            @PathVariable Long usuarioId,
            @RequestBody List<BoletaDetalle> detalles) {

        Boleta boleta = boletaService.crearBoleta(usuarioId, detalles);
        return new ResponseEntity<>(
                EntityModel.of(boletaAssembler.toDTO(boleta)),
                HttpStatus.CREATED
        );
    }

    // ----------------------------
    // OBTENER BOLETA (CLIENTE / ADMIN)
    // ----------------------------
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    @GetMapping("/{boletaId}")
    public ResponseEntity<EntityModel<BoletaDTO>> obtenerBoleta(
            @PathVariable Long boletaId) {

        return ResponseEntity.ok(
                EntityModel.of(
                        boletaAssembler.toDTO(boletaService.obtenerBoleta(boletaId))
                )
        );
    }

    // ----------------------------
    // LISTAR BOLETAS USUARIO (CLIENTE / ADMIN)
    // ----------------------------
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<CollectionModel<EntityModel<BoletaDTO>>> obtenerBoletasUsuario(
            @PathVariable Long usuarioId) {

        List<EntityModel<BoletaDTO>> boletas = boletaService.obtenerBoletasUsuario(usuarioId)
                .stream()
                .map(boletaAssembler::toDTO)
                .map(EntityModel::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(boletas));
    }

    // ----------------------------
    // AGREGAR DETALLE (ADMIN)
    // ----------------------------
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{boletaId}/producto/{productoId}")
    public ResponseEntity<EntityModel<BoletaDetalleDTO>> agregarDetalle(
            @PathVariable Long boletaId,
            @PathVariable Long productoId,
            @RequestParam Long cantidad,
            @RequestParam Long precioUnitario) {

        BoletaDetalle detalle = boletaService.agregarDetalle(
                boletaId, productoId, cantidad, precioUnitario
        );

        return new ResponseEntity<>(
                EntityModel.of(boletaDetalleAssembler.toDTO(detalle)),
                HttpStatus.CREATED
        );
    }
}
