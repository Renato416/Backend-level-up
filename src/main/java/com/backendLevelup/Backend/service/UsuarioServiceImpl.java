package com.backendLevelup.Backend.service;

import com.backendLevelup.Backend.assemblers.UsuarioAssembler;
import com.backendLevelup.Backend.dtos.LoginDTO;
import com.backendLevelup.Backend.dtos.RegistroUsuarioDTO;
import com.backendLevelup.Backend.dtos.UsuarioDTO;
import com.backendLevelup.Backend.exceptions.ResourceNotFoundException;
import com.backendLevelup.Backend.exceptions.UsuarioValidationException;
import com.backendLevelup.Backend.model.Usuario;
import com.backendLevelup.Backend.repository.UsuarioRepository;
import com.backendLevelup.Backend.security.JwtService; // 🔹 IMPORTANTE
import org.springframework.security.core.userdetails.UserDetails; // 🔹 IMPORTANTE
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioAssembler usuarioAssembler;
    // inyecciones necesarias para el Token
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

    @Override
    public UsuarioDTO createUsuario(RegistroUsuarioDTO dto) {
        // Validar correo
        if (usuarioRepository.existsByCorreoElectronico(dto.getCorreoElectronico())) {
            throw new UsuarioValidationException("El correo ya está registrado");
        }

        // Validar edad
        if (Period.between(dto.getFechaNacimiento(), LocalDate.now()).getYears() < 18) {
            throw new UsuarioValidationException("Debe ser mayor de edad");
        }

        // Encriptar contraseña
        String contraseñaEncriptada = passwordEncoder.encode(dto.getContraseña());

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setDireccion(dto.getDireccion());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setContraseña(contraseñaEncriptada);
        usuario.setRun(dto.getRun());
        usuario.setRol("ROLE_USER");
        usuario.setCorreoElectronico(dto.getCorreoElectronico());

        Usuario guardado = usuarioRepository.save(usuario);

        // 🔹 Opcional: Podrías generar token aquí también si quieres auto-login al registrar
        return usuarioAssembler.toDTO(guardado);
    }

    @Override
    public UsuarioDTO login(LoginDTO dto) {
        // 1. Buscar usuario en BD
        Usuario usuario = usuarioRepository.findByCorreoElectronico(dto.getCorreoElectronico())
                .orElseThrow(() -> new UsuarioValidationException("Usuario no encontrado"));

        // 2. Verificar contraseña
        if (!passwordEncoder.matches(dto.getContraseña(), usuario.getContraseña())) {
            throw new ResourceNotFoundException("Contraseña incorrecta");
        }

        // 3. 🔹 GENERAR EL TOKEN
        // Cargamos los detalles de seguridad (roles, permisos)
        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getCorreoElectronico());

        // Creamos el string del token
        String jwtToken = jwtService.generateToken(userDetails);

        // 4. Convertir a DTO y agregar el token
        UsuarioDTO response = usuarioAssembler.toDTO(usuario);
        response.setToken(jwtToken); // <--- ¡Aquí entregamos la llave!

        return response;
    }
}