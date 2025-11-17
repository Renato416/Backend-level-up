package com.backendLevelup.Backend.controller;

import com.backendLevelup.Backend.dtos.LoginDTO;
import com.backendLevelup.Backend.dtos.RegistroUsuarioDTO;
import com.backendLevelup.Backend.dtos.UsuarioDTO;
import com.backendLevelup.Backend.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Endpoint para registrar un usuario
    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO> registrarUsuario(@RequestBody RegistroUsuarioDTO dto) {
        UsuarioDTO usuarioCreado = usuarioService.createUsuario(dto);
        return new ResponseEntity<>(usuarioCreado, HttpStatus.CREATED);
    }

    // Endpoint para login de usuario
    @PostMapping("/login")
    public ResponseEntity<UsuarioDTO> loginUsuario(@RequestBody LoginDTO dto) {
        UsuarioDTO usuarioLogeado = usuarioService.login(dto);
        return new ResponseEntity<>(usuarioLogeado, HttpStatus.OK);
    }
}
