package com.backendLevelup.Backend.controller;

import com.backendLevelup.Backend.dtos.LoginDTO;
import com.backendLevelup.Backend.dtos.RegistroUsuarioDTO;
import com.backendLevelup.Backend.dtos.UsuarioDTO;
import com.backendLevelup.Backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v2/auth") // Actualizado a V2 y 'auth'
@Tag(name = "Autenticación", description = "Gestión de usuarios y acceso")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

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
}