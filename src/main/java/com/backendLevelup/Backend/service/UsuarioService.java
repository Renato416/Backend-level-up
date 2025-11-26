package com.backendLevelup.Backend.service;

import com.backendLevelup.Backend.dtos.LoginDTO;
import com.backendLevelup.Backend.dtos.RegistroUsuarioDTO;
import com.backendLevelup.Backend.dtos.UsuarioDTO;
import java.util.List; // <--- AGREGAR IMPORT

public interface UsuarioService {

    UsuarioDTO createUsuario(RegistroUsuarioDTO dto);

    UsuarioDTO login(LoginDTO dto);

    // AGREGAR ESTO:
    List<UsuarioDTO> listarUsuarios();

    UsuarioDTO obtenerPorId(Long id);
    UsuarioDTO actualizarUsuario(Long id, RegistroUsuarioDTO dto);
}