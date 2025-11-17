package com.backendLevelup.Backend.controller;

import com.backendLevelup.Backend.model.Carrito;
import com.backendLevelup.Backend.model.CarritoDetalle;
import com.backendLevelup.Backend.service.CarritoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carritos")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    // Obtener carrito de un usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Carrito> obtenerCarrito(@PathVariable Long usuarioId) {
        Carrito carrito = carritoService.obtenerCarritoUsuario(usuarioId);
        return ResponseEntity.ok(carrito);
    }

    // Agregar producto al carrito
    @PostMapping("/usuario/{usuarioId}/producto/{productoId}")
    public ResponseEntity<CarritoDetalle> agregarProducto(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId,
            @RequestParam Long cantidad) {

        CarritoDetalle detalle = carritoService.agregarProducto(usuarioId, productoId, cantidad);
        return new ResponseEntity<>(detalle, HttpStatus.CREATED);
    }

    // Actualizar cantidad de un producto
    @PutMapping("/usuario/{usuarioId}/producto/{productoId}")
    public ResponseEntity<CarritoDetalle> actualizarCantidad(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId,
            @RequestParam Long cantidad) {

        CarritoDetalle detalle = carritoService.actualizarCantidad(usuarioId, productoId, cantidad);
        return ResponseEntity.ok(detalle);
    }

    // Eliminar producto del carrito
    @DeleteMapping("/usuario/{usuarioId}/producto/{productoId}")
    public ResponseEntity<Void> eliminarProducto(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId) {

        carritoService.eliminarProducto(usuarioId, productoId);
        return ResponseEntity.noContent().build();
    }

    // Vaciar carrito
    @DeleteMapping("/usuario/{usuarioId}")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
