package com.backendLevelup.Backend.controller;

import com.backendLevelup.Backend.assemblers.CategoriaAssembler; // Import del Assembler
import com.backendLevelup.Backend.dtos.CategoriaDTO; // Import del DTO
import com.backendLevelup.Backend.model.Categoria;
import com.backendLevelup.Backend.service.CategoriaService;
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

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v2/categorias")
@Tag(name = "Categorías", description = "Gestión de categorías de productos")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final CategoriaAssembler categoriaAssembler; // Inyectamos el Assembler

    public CategoriaController(CategoriaService categoriaService, CategoriaAssembler categoriaAssembler) {
        this.categoriaService = categoriaService;
        this.categoriaAssembler = categoriaAssembler;
    }

    // Método auxiliar HATEOAS usando DTO
    private EntityModel<CategoriaDTO> buildCategoriaResource(CategoriaDTO categoriaDTO) {
        EntityModel<CategoriaDTO> resource = EntityModel.of(categoriaDTO);
        resource.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(CategoriaController.class).obtenerCategoria(categoriaDTO.getId())
        ).withSelfRel());
        resource.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(CategoriaController.class).listarCategorias()
        ).withRel("todas-las-categorias"));
        return resource;
    }

    @Operation(summary = "Crear categoría", description = "Crea una nueva categoría para agrupar productos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<CategoriaDTO>> crearCategoria(@RequestBody CategoriaDTO categoriaDTO) {
        // 1. Convertir DTO a Entidad para guardar (Asumiendo que tu assembler tiene toEntity)
        // Si tu assembler no tiene toEntity simple, podrias crear la entidad manualmente aqui.
        // Pero idealmente el assembler lo hace.
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre(categoriaDTO.getNombre());
        // Agrega otros campos si los tiene tu DTO

        Categoria creada = categoriaService.crearCategoria(nuevaCategoria);

        // 2. Convertir Entidad guardada a DTO para responder
        CategoriaDTO dtoRespuesta = categoriaAssembler.toDTO(creada);

        return new ResponseEntity<>(buildCategoriaResource(dtoRespuesta), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener categoría", description = "Busca una categoría por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CategoriaDTO>> obtenerCategoria(@PathVariable Long id) {
        Categoria categoria = categoriaService.obtenerCategoriaPorId(id);

        // Convertir a DTO
        CategoriaDTO dto = categoriaAssembler.toDTO(categoria);

        return ResponseEntity.ok(buildCategoriaResource(dto));
    }

    @Operation(summary = "Listar categorías", description = "Devuelve el listado completo de categorías")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CategoriaDTO>>> listarCategorias() {
        List<Categoria> categorias = categoriaService.listarCategorias();

        // Convertir lista de Entidades a lista de DTOs con HATEOAS
        List<EntityModel<CategoriaDTO>> resources = categorias.stream()
                .map(categoriaAssembler::toDTO)       // Entidad -> DTO
                .map(this::buildCategoriaResource)    // DTO -> EntityModel (HATEOAS)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<CategoriaDTO>> collection = CollectionModel.of(resources);
        collection.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(CategoriaController.class).listarCategorias()
        ).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @Operation(summary = "Actualizar categoría", description = "Modifica el nombre o descripción de una categoría")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CategoriaDTO>> actualizarCategoria(@PathVariable Long id, @RequestBody CategoriaDTO categoriaDTO) {
        // Mapeo manual simple para actualizar (o usa el assembler si tiene update)
        Categoria categoriaUpdates = new Categoria();
        categoriaUpdates.setNombre(categoriaDTO.getNombre());

        Categoria actualizada = categoriaService.actualizarCategoria(id, categoriaUpdates);

        // Responder con DTO
        CategoriaDTO dto = categoriaAssembler.toDTO(actualizada);
        return ResponseEntity.ok(buildCategoriaResource(dto));
    }

    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría del sistema")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}