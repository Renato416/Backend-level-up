package com.backendLevelup.Backend.controller;

import com.backendLevelup.Backend.assemblers.BoletaAssembler;
import com.backendLevelup.Backend.assemblers.BoletaDetalleAssembler;
import com.backendLevelup.Backend.dtos.BoletaDTO;
import com.backendLevelup.Backend.dtos.BoletaDetalleDTO;
import com.backendLevelup.Backend.model.Boleta;
import com.backendLevelup.Backend.model.BoletaDetalle;
import com.backendLevelup.Backend.service.BoletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public BoletaController(BoletaService boletaService,
                            BoletaAssembler boletaAssembler,
                            BoletaDetalleAssembler boletaDetalleAssembler) {
        this.boletaService = boletaService;
        this.boletaAssembler = boletaAssembler;
        this.boletaDetalleAssembler = boletaDetalleAssembler;
    }

    // Método auxiliar para HATEOAS
    private EntityModel<BoletaDTO> buildBoletaResource(BoletaDTO boletaDTO) {
        EntityModel<BoletaDTO> resource = EntityModel.of(boletaDTO);
        resource.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(BoletaController.class).obtenerBoleta(boletaDTO.getId())
        ).withSelfRel());
        return resource;
    }

    @Operation(summary = "Crear boleta", description = "Genera una nueva boleta de compra para un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Boleta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de la compra")
    })
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<EntityModel<BoletaDTO>> crearBoleta(
            @PathVariable Long usuarioId,
            @RequestBody List<BoletaDetalle> detalles) {

        // Nota: Mantenemos List<BoletaDetalle> como entrada para compatibilidad con el servicio,
        // pero devolvemos BoletaDTO.
        Boleta boleta = boletaService.crearBoleta(usuarioId, detalles);

        // Convertir a DTO
        BoletaDTO dto = boletaAssembler.toDTO(boleta);

        return new ResponseEntity<>(buildBoletaResource(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener boleta", description = "Busca una boleta por su ID")
    @GetMapping("/{boletaId}")
    public ResponseEntity<EntityModel<BoletaDTO>> obtenerBoleta(@PathVariable Long boletaId) {
        Boleta boleta = boletaService.obtenerBoleta(boletaId);

        // Convertir a DTO
        BoletaDTO dto = boletaAssembler.toDTO(boleta);

        return ResponseEntity.ok(buildBoletaResource(dto));
    }

    @Operation(summary = "Listar boletas de usuario", description = "Historial de compras de un usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<CollectionModel<EntityModel<BoletaDTO>>> obtenerBoletasUsuario(@PathVariable Long usuarioId) {
        List<Boleta> boletas = boletaService.obtenerBoletasUsuario(usuarioId);

        // Convertir lista de entidades a DTOs
        List<EntityModel<BoletaDTO>> resources = boletas.stream()
                .map(boletaAssembler::toDTO)
                .map(this::buildBoletaResource)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<BoletaDTO>> collection = CollectionModel.of(resources);
        collection.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(BoletaController.class).obtenerBoletasUsuario(usuarioId)
        ).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @Operation(summary = "Agregar detalle a boleta", description = "Añade un item extra a una boleta existente (Admin)")
    @PostMapping("/{boletaId}/producto/{productoId}")
    public ResponseEntity<EntityModel<BoletaDetalleDTO>> agregarDetalle(
            @PathVariable Long boletaId,
            @PathVariable Long productoId,
            @RequestParam Long cantidad,
            @RequestParam Long precioUnitario) {

        BoletaDetalle detalle = boletaService.agregarDetalle(boletaId, productoId, cantidad, precioUnitario);

        // Convertir detalle a DTO
        BoletaDetalleDTO dto = boletaDetalleAssembler.toDTO(detalle);

        EntityModel<BoletaDetalleDTO> resource = EntityModel.of(dto);
        resource.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(BoletaController.class).obtenerBoleta(boletaId)
        ).withRel("ver-boleta"));

        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }
}