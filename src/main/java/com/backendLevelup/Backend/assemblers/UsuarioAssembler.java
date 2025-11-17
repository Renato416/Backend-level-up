package com.backendLevelup.Backend.assemblers;

import com.backendLevelup.Backend.dtos.UsuarioDTO;
import com.backendLevelup.Backend.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioAssembler {

    // Convierte de entidad a DTO
    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombreUsuario(usuario.getNombreUsuario());
        dto.setDireccion(usuario.getDireccion());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setRun(usuario.getRun());
        dto.setRol(usuario.getRol());
        dto.setCorreoElectronico(usuario.getCorreoElectronico());

        return dto;
    }

    // Convierte de DTO a entidad
    public Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) return null;

        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setDireccion(dto.getDireccion());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setRun(dto.getRun());
        usuario.setRol(dto.getRol());
        usuario.setCorreoElectronico(dto.getCorreoElectronico());

        return usuario;
    }
}
