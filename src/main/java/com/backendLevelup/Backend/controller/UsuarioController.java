package com.backendLevelup.Backend.controller;

import com.backendLevelup.Backend.dtos.LoginDTO;
import com.backendLevelup.Backend.dtos.RegistroUsuarioDTO;
import com.backendLevelup.Backend.dtos.UsuarioDTO;
import com.backendLevelup.Backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel; // <--- NUEVO
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List; // <--- NUEVO
import java.util.stream.Collectors; // <--- NUEVO


@RestController
@RequestMapping("/api/v2/auth") // Ruta base
@Tag(name = "Autenticación", description = "Gestión de usuarios y acceso")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // -------------------------------------------------------------------
    // REGISTRO
    // -------------------------------------------------------------------
    @Operation(summary = "Registrar nuevo usuario", description = "Crea una cuenta de usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de registro inválidos")
    })
    @PostMapping("/registro")
    public ResponseEntity<EntityModel<UsuarioDTO>> registrarUsuario(@RequestBody RegistroUsuarioDTO dto) {
        UsuarioDTO usuarioCreado = usuarioService.createUsuario(dto);

        // HATEOAS: Crear links de navegación
        EntityModel<UsuarioDTO> resource = EntityModel.of(usuarioCreado);

        // Link a sí mismo
        Link selfLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UsuarioController.class).registrarUsuario(dto)
        ).withSelfRel();

        // Link sugerido: ir al login
        Link loginLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UsuarioController.class).loginUsuario(new LoginDTO())
        ).withRel("login");

        resource.add(selfLink, loginLink);

        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    // -------------------------------------------------------------------
    // LOGIN
    // -------------------------------------------------------------------
    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y devuelve sus credenciales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    })
    @PostMapping("/login")
    public ResponseEntity<EntityModel<UsuarioDTO>> loginUsuario(@RequestBody LoginDTO dto) {
        UsuarioDTO usuarioLogeado = usuarioService.login(dto);

        EntityModel<UsuarioDTO> resource = EntityModel.of(usuarioLogeado);

        Link selfLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UsuarioController.class).loginUsuario(dto)
        ).withSelfRel();

        resource.add(selfLink);

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    // -------------------------------------------------------------------
    // NUEVO: LISTAR TODOS LOS USUARIOS
    // -------------------------------------------------------------------
    @Operation(summary = "Listar usuarios", description = "Obtiene la lista de todos los usuarios registrados (Solo Admin)")
    @GetMapping("/listar") // Ruta final: GET /api/v2/auth/listar
    public ResponseEntity<CollectionModel<EntityModel<UsuarioDTO>>> listarUsuarios() {
        // 1. Llamamos al servicio
        List<UsuarioDTO> usuarios = usuarioService.listarUsuarios();

        // 2. Convertimos la lista normal a una lista de recursos HATEOAS
        List<EntityModel<UsuarioDTO>> usuariosResources = usuarios.stream()
                .map(usuario -> {
                    EntityModel<UsuarioDTO> resource = EntityModel.of(usuario);
                    // Aquí podrías agregar links individuales a cada usuario (ej. detalle) si quisieras
                    return resource;
                })
                .collect(Collectors.toList());

        // 3. Creamos el modelo de colección
        CollectionModel<EntityModel<UsuarioDTO>> collectionModel = CollectionModel.of(usuariosResources);

        // 4. Agregamos un link a la propia lista ("self")
        collectionModel.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UsuarioController.class).listarUsuarios()
        ).withSelfRel());

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }
}