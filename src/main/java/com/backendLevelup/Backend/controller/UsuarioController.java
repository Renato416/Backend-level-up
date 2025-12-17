package com.backendLevelup.Backend.controller;

import com.backendLevelup.Backend.dtos.LoginDTO;
import com.backendLevelup.Backend.dtos.RegistroUsuarioDTO;
import com.backendLevelup.Backend.dtos.UsuarioDTO;
import com.backendLevelup.Backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/auth")
@Tag(name = "Autenticación y Usuarios", description = "Gestión de usuarios y acceso")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // -------------------------------------------------
    // REGISTRO (PÚBLICO)
    // -------------------------------------------------
    @PostMapping("/registro")
    public ResponseEntity<EntityModel<UsuarioDTO>> registrarUsuario(
            @RequestBody RegistroUsuarioDTO dto) {

        UsuarioDTO usuarioCreado = usuarioService.createUsuario(dto);

        EntityModel<UsuarioDTO> resource = EntityModel.of(usuarioCreado);

        Link selfLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UsuarioController.class).registrarUsuario(dto)
        ).withSelfRel();

        Link loginLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UsuarioController.class).loginUsuario(new LoginDTO())
        ).withRel("login");

        resource.add(selfLink, loginLink);

        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    // -------------------------------------------------
    // LOGIN (PÚBLICO)
    // -------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<EntityModel<UsuarioDTO>> loginUsuario(
            @RequestBody LoginDTO dto) {

        UsuarioDTO usuarioLogeado = usuarioService.login(dto);

        EntityModel<UsuarioDTO> resource = EntityModel.of(usuarioLogeado);

        resource.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UsuarioController.class).loginUsuario(dto)
        ).withSelfRel());

        return ResponseEntity.ok(resource);
    }

    // -------------------------------------------------
    // LISTAR USUARIOS (SOLO ADMIN)
    // -------------------------------------------------
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listar")
    public ResponseEntity<CollectionModel<EntityModel<UsuarioDTO>>> listarUsuarios() {

        List<EntityModel<UsuarioDTO>> usuarios = usuarioService.listarUsuarios()
                .stream()
                .map(EntityModel::of)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<UsuarioDTO>> collection =
                CollectionModel.of(usuarios);

        collection.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UsuarioController.class).listarUsuarios()
        ).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    // -------------------------------------------------
    // OBTENER USUARIO (ADMIN o DUEÑO)
    // -------------------------------------------------
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.usuario.id")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UsuarioDTO>> obtenerUsuario(
            @PathVariable Long id) {

        UsuarioDTO usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(EntityModel.of(usuario));
    }

    // -------------------------------------------------
    // ACTUALIZAR USUARIO (ADMIN o DUEÑO)
    // -------------------------------------------------
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.usuario.id")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UsuarioDTO>> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody RegistroUsuarioDTO dto) {

        UsuarioDTO actualizado = usuarioService.actualizarUsuario(id, dto);
        return ResponseEntity.ok(EntityModel.of(actualizado));
    }
}
