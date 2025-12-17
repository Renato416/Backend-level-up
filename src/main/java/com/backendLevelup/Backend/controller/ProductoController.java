package com.backendLevelup.Backend.controller;

import com.backendLevelup.Backend.dtos.ProductoDTO;
import com.backendLevelup.Backend.service.ProductoService;
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
@RequestMapping("/api/v2/productos")
@Tag(name = "Productos", description = "Operaciones CRUD para el catálogo de productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    private EntityModel<ProductoDTO> buildResource(ProductoDTO producto) {
        EntityModel<ProductoDTO> resource = EntityModel.of(producto);
        resource.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ProductoController.class)
                        .obtenerProducto(producto.getId())
        ).withSelfRel());
        return resource;
    }

    // ----------------------------
    // CREAR (ADMIN)
    // ----------------------------
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EntityModel<ProductoDTO>> crearProducto(
            @RequestBody ProductoDTO dto) {

        return new ResponseEntity<>(
                buildResource(productoService.crearProducto(dto)),
                HttpStatus.CREATED
        );
    }

    // ----------------------------
    // OBTENER (PÚBLICO)
    // ----------------------------
    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProductoDTO>> obtenerProducto(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                buildResource(productoService.obtenerProductoPorId(id))
        );
    }

    // ----------------------------
    // LISTAR (PÚBLICO)
    // ----------------------------
    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ProductoDTO>>> listarProductos() {

        List<EntityModel<ProductoDTO>> productos = productoService.listarProductos()
                .stream()
                .map(this::buildResource)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(productos));
    }

    // ----------------------------
    // ACTUALIZAR (ADMIN)
    // ----------------------------
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ProductoDTO>> actualizarProducto(
            @PathVariable Long id,
            @RequestBody ProductoDTO dto) {

        return ResponseEntity.ok(
                buildResource(productoService.actualizarProducto(id, dto))
        );
    }

    // ----------------------------
    // ELIMINAR (ADMIN)
    // ----------------------------
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
