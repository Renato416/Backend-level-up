package com.backendLevelup.Backend.service;

import com.backendLevelup.Backend.model.Usuario;
import com.backendLevelup.Backend.repository.UsuarioRepository;
import com.backendLevelup.Backend.security.MyUserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public MyUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreoElectronico(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));

        return new MyUserDetails(usuario);
    }
}
