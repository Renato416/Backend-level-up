package com.backendLevelup.Backend.controller;

import com.backendLevelup.Backend.model.Boleta;
import com.backendLevelup.Backend.model.BoletaDetalle;
import com.backendLevelup.Backend.service.BoletaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boletas")
public class BoletaController {

    private final BoletaService boletaService;

    public BoletaController(BoletaService boletaService) {
        this.boletaService = boletaService;
    }

    // Crear boleta con lista de detalles
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Boleta> crearBoleta(
            @PathVariable Long usuarioId,
            @RequestBody List<BoletaDetalle> detalles) {

        Boleta boleta = boletaService.crearBoleta(usuarioId, detalles);
        return new ResponseEntity<>(boleta, HttpStatus.CREATED);
    }

    // Obtener boleta por ID
    @GetMapping("/{boletaId}")
    public ResponseEntity<Boleta> obtenerBoleta(@PathVariable Long boletaId) {
        Boleta boleta = boletaService.obtenerBoleta(boletaId);
        return ResponseEntity.ok(boleta);
    }

    // Obtener todas las boletas de un usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Boleta>> obtenerBoletasUsuario(@PathVariable Long usuarioId) {
        List<Boleta> boletas = boletaService.obtenerBoletasUsuario(usuarioId);
        return ResponseEntity.ok(boletas);
    }

    // Agregar detalle a boleta existente
    @PostMapping("/{boletaId}/producto/{productoId}")
    public ResponseEntity<BoletaDetalle> agregarDetalle(
            @PathVariable Long boletaId,
            @PathVariable Long productoId,
            @RequestParam Long cantidad,
            @RequestParam Long precioUnitario) {

        BoletaDetalle detalle = boletaService.agregarDetalle(boletaId, productoId, cantidad, precioUnitario);
        return new ResponseEntity<>(detalle, HttpStatus.CREATED);
    }
}
