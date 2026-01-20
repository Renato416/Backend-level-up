package com.backendLevelup.Backend.assemblers;

import com.backendLevelup.Backend.dtos.UsuarioDTO;
import com.backendLevelup.Backend.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioAssembler {

    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;

        return new UsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getAddress(),
                usuario.getPhone(),
                usuario.getBirthDate(),
                null
        );
    }
}
