package com.backendLevelup.Backend.service;

import com.backendLevelup.Backend.assemblers.UsuarioAssembler;
import com.backendLevelup.Backend.dtos.LoginDTO;
import com.backendLevelup.Backend.dtos.RegistroUsuarioDTO;
import com.backendLevelup.Backend.dtos.UsuarioDTO;
import com.backendLevelup.Backend.exceptions.ResourceNotFoundException;
import com.backendLevelup.Backend.model.Usuario;
import com.backendLevelup.Backend.repository.UsuarioRepository;
import com.backendLevelup.Backend.security.JwtService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioAssembler usuarioAssembler;
    private final JwtService jwtService;
    private final MyUserDetailsService userDetailsService;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              PasswordEncoder passwordEncoder,
                              UsuarioAssembler usuarioAssembler,
                              JwtService jwtService,
                              MyUserDetailsService userDetailsService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioAssembler = usuarioAssembler;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    // ---------------------------
    // REGISTRO
    // ---------------------------
    @Override
    public UsuarioDTO createUsuario(RegistroUsuarioDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setAddress(dto.getAddress());
        usuario.setPhone(dto.getPhone());
        usuario.setBirthDate(dto.getBirthDate());

        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioAssembler.toDTO(guardado);
    }

    // ---------------------------
    // LOGIN
    // ---------------------------
    @Override
    public UsuarioDTO login(LoginDTO dto) {

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(usuario.getEmail());

        String token = jwtService.generateToken(userDetails);

        UsuarioDTO response = usuarioAssembler.toDTO(usuario);
        response.setToken(token);

        return response;
    }

    // ---------------------------
    // LISTAR
    // ---------------------------
    @Override
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioAssembler::toDTO)
                .collect(Collectors.toList());
    }

    // ---------------------------
    // OBTENER POR ID
    // ---------------------------
    @Override
    public UsuarioDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado con ID: " + id)
                );

        return usuarioAssembler.toDTO(usuario);
    }

    // ---------------------------
    // ACTUALIZAR
    // ---------------------------
    @Override
    public UsuarioDTO actualizarUsuario(Long id, RegistroUsuarioDTO dto) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado con ID: " + id)
                );

        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        usuario.setAddress(dto.getAddress());
        usuario.setPhone(dto.getPhone());
        usuario.setBirthDate(dto.getBirthDate());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        return usuarioAssembler.toDTO(actualizado);
    }
}
