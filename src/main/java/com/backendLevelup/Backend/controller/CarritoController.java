package com.backendLevelup.Backend.controller;

import com.backendLevelup.Backend.assemblers.CarritoAssembler;
import com.backendLevelup.Backend.assemblers.CarritoDetalleAssembler;
import com.backendLevelup.Backend.dtos.CarritoDTO;
import com.backendLevelup.Backend.dtos.CarritoDetalleDTO;
import com.backendLevelup.Backend.model.Carrito;
import com.backendLevelup.Backend.model.CarritoDetalle;
import com.backendLevelup.Backend.service.CarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v2/carritos")
@Tag(name = "Carrito de Compras", description = "Gestión del carrito y sus items")
public class CarritoController {

    private final CarritoService carritoService;
    private final CarritoAssembler carritoAssembler;
    private final CarritoDetalleAssembler carritoDetalleAssembler;

    public CarritoController(CarritoService carritoService,
                             CarritoAssembler carritoAssembler,
                             CarritoDetalleAssembler carritoDetalleAssembler) {
        this.carritoService = carritoService;
        this.carritoAssembler = carritoAssembler;
        this.carritoDetalleAssembler = carritoDetalleAssembler;
    }

    @Operation(summary = "Obtener carrito", description = "Obtiene el carrito activo de un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito encontrado"),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<EntityModel<CarritoDTO>> obtenerCarrito(@PathVariable Long usuarioId) {
        Carrito carrito = carritoService.obtenerCarritoUsuario(usuarioId);

        // Convertir a DTO
        CarritoDTO dto = carritoAssembler.toDTO(carrito);

        EntityModel<CarritoDTO> resource = EntityModel.of(dto);
        resource.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(CarritoController.class).obtenerCarrito(usuarioId)
        ).withSelfRel());

        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Agregar producto", description = "Añade un producto al carrito del usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto agregado"),
            @ApiResponse(responseCode = "400", description = "Stock insuficiente o error")
    })
    @PostMapping("/usuario/{usuarioId}/producto/{productoId}")
    public ResponseEntity<EntityModel<CarritoDetalleDTO>> agregarProducto(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId,
            @RequestParam Long cantidad) {

        CarritoDetalle detalle = carritoService.agregarProducto(usuarioId, productoId, cantidad);

        // Convertir Detalle a DTO
        CarritoDetalleDTO dto = carritoDetalleAssembler.toDTO(detalle);

        EntityModel<CarritoDetalleDTO> resource = EntityModel.of(dto);
        resource.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(CarritoController.class).obtenerCarrito(usuarioId)
        ).withRel("ver-carrito"));

        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar cantidad", description = "Modifica la cantidad de un producto en el carrito")
    @PutMapping("/usuario/{usuarioId}/producto/{productoId}")
    public ResponseEntity<EntityModel<CarritoDetalleDTO>> actualizarCantidad(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId,
            @RequestParam Long cantidad) {

        CarritoDetalle detalle = carritoService.actualizarCantidad(usuarioId, productoId, cantidad);

        // Convertir a DTO
        CarritoDetalleDTO dto = carritoDetalleAssembler.toDTO(detalle);

        EntityModel<CarritoDetalleDTO> resource = EntityModel.of(dto);
        resource.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(CarritoController.class).obtenerCarrito(usuarioId)
        ).withRel("ver-carrito"));

        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Eliminar producto", description = "Quita un producto específico del carrito")
    @DeleteMapping("/usuario/{usuarioId}/producto/{productoId}")
    public ResponseEntity<Void> eliminarProducto(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId) {

        carritoService.eliminarProducto(usuarioId, productoId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Vaciar carrito", description = "Elimina todos los productos del carrito")
    @DeleteMapping("/usuario/{usuarioId}")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }
}