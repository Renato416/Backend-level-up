package com.backendLevelup.Backend.service;

import com.backendLevelup.Backend.dtos.LoginDTO;
import com.backendLevelup.Backend.dtos.RegistroUsuarioDTO;
import com.backendLevelup.Backend.dtos.UsuarioDTO;
import org.springframework.stereotype.Service;

@Service
public interface UsuarioService {

    UsuarioDTO createUsuario(RegistroUsuarioDTO dto);

    UsuarioDTO login(LoginDTO dto);

}
