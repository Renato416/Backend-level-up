package com.backendLevelup.Backend.init;

import com.backendLevelup.Backend.model.Producto;
import com.backendLevelup.Backend.model.Usuario;
import com.backendLevelup.Backend.repository.ProductoRepository;
import com.backendLevelup.Backend.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            // Evita duplicar datos
            if (usuarioRepository.count() > 0 || productoRepository.count() > 0) {
                System.out.println("➡ Datos ya existen. No se cargan nuevamente.");
                return;
            }

            System.out.println("🚀 Cargando datos iniciales...");

            // ============================
            // 👤 USUARIOS
            // ============================
            Usuario u1 = new Usuario(
                    null,
                    "juanperez",
                    "juan@correo.cl",
                    passwordEncoder.encode("1234"),
                    "Calle Falsa 123",
                    "+56911111111",
                    LocalDate.of(1990, 5, 10)
            );

            Usuario u2 = new Usuario(
                    null,
                    "mariagonzalez",
                    "maria@gmail.com",
                    passwordEncoder.encode("abcd"),
                    "Av. Siempre Viva 742",
                    "+56922222222",
                    LocalDate.of(1995, 2, 15)
            );

            Usuario u3 = new Usuario(
                    null,
                    "admin",
                    "admin@levelup.cl",
                    passwordEncoder.encode("admin123"),
                    "Oficina Central",
                    "+56933333333",
                    LocalDate.of(1987, 8, 18)
            );

            usuarioRepository.saveAll(List.of(u1, u2, u3));

            // ============================
            // 📦 PRODUCTOS
            // ============================
            Producto p1 = new Producto(
                    null,
                    "Audífonos Gamer",
                    64990L,
                    "https://res.cloudinary.com/demo/image/upload/audifonos.jpg",
                    "Audífonos gamer con sonido envolvente 7.1",
                    4.5
            );

            Producto p2 = new Producto(
                    null,
                    "Silla Gamer",
                    72990L,
                    "https://res.cloudinary.com/demo/image/upload/silla.jpg",
                    "Silla ergonómica reclinable",
                    4.7
            );

            Producto p3 = new Producto(
                    null,
                    "Teclado Mecánico RGB",
                    15990L,
                    "https://res.cloudinary.com/demo/image/upload/teclado.jpg",
                    "Teclado mecánico switches azules",
                    4.3
            );

            Producto p4 = new Producto(
                    null,
                    "Mouse Gamer",
                    28990L,
                    "https://res.cloudinary.com/demo/image/upload/mouse.jpg",
                    "Mouse gamer 16000 DPI",
                    4.4
            );

            productoRepository.saveAll(List.of(p1, p2, p3, p4));

            System.out.println("✅ Datos iniciales cargados correctamente.");
        };
    }
}
