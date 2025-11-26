package com.backendLevelup.Backend.security;

import com.backendLevelup.Backend.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class MyUserDetails implements UserDetails {

    private final Usuario usuario;

    public MyUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toUpperCase()));
    }

    @Override
    public String getPassword() {
        return usuario.getContraseña();
    }

    @Override
    public String getUsername() {
        return usuario.getCorreoElectronico();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public Usuario getUsuario() { return usuario; }
}
