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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v2/auth") // Ruta base
@Tag(name = "Autenticación y Usuarios", description = "Gestión de usuarios y acceso")
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
    // LISTAR TODOS LOS USUARIOS
    // -------------------------------------------------------------------
    @Operation(summary = "Listar usuarios", description = "Obtiene la lista de todos los usuarios registrados (Solo Admin)")
    @GetMapping("/listar") // Ruta final: GET /api/v2/auth/listar
    public ResponseEntity<CollectionModel<EntityModel<UsuarioDTO>>> listarUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.listarUsuarios();

        List<EntityModel<UsuarioDTO>> usuariosResources = usuarios.stream()
                .map(usuario -> EntityModel.of(usuario))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<UsuarioDTO>> collectionModel = CollectionModel.of(usuariosResources);
        collectionModel.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UsuarioController.class).listarUsuarios()
        ).withSelfRel());

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    // -------------------------------------------------------------------
    // NUEVO: OBTENER USUARIO POR ID (Para el formulario de edición)
    // -------------------------------------------------------------------
    @Operation(summary = "Obtener usuario por ID", description = "Busca un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UsuarioDTO>> obtenerUsuario(@PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(EntityModel.of(usuario));
    }

    // -------------------------------------------------------------------
    // NUEVO: ACTUALIZAR USUARIO (Guardar cambios)
    // -------------------------------------------------------------------
    @Operation(summary = "Actualizar usuario", description = "Modifica los datos de un usuario existente")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UsuarioDTO>> actualizarUsuario(@PathVariable Long id, @RequestBody RegistroUsuarioDTO dto) {
        UsuarioDTO actualizado = usuarioService.actualizarUsuario(id, dto);
        return ResponseEntity.ok(EntityModel.of(actualizado));
    }
}