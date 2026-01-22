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
                    "https://http2.mlstatic.com/D_NQ_NP_908575-MLA45640430254_042021-F.jpg",
                    "Audífonos gamer con sonido envolvente 7.1",
                    4.5
            );

            Producto p2 = new Producto(
                    null,
                    "Silla Gamer",
                    72990L,
                    "https://images.contentstack.io/v3/assets/blt7c5c2f2f888a7cc3/blt97d26f07c0df0db1/66ba2279f8d31f76f2b63929/Silla_gamer.jpg",
                    "Silla ergonómica reclinable",
                    4.7
            );

            Producto p3 = new Producto(
                    null,
                    "Teclado Mecánico RGB",
                    15990L,
                    "https://http2.mlstatic.com/teclado-mecanico-gamer-iluminado-rgb-anti-ghosting-yeyian-fl-D_NQ_NP_931450-MLM31237131039_062019-F.jpg",
                    "Teclado mecánico switches azules",
                    4.3
            );

            Producto p4 = new Producto(
                    null,
                    "Mouse Gamer",
                    28990L,
                    "https://www.toptecnouy.com/imgs/productos/productos34_31059.jpg",
                    "Mouse gamer 16000 DPI",
                    4.4
            );
            Producto p5 = new Producto(
                    null,
                    "Monitor Gamer 27\"",
                    199990L,
                    "https://www.hd-tecnologia.com/imagenes/articulos/2017/04/Nuevos-Monitores-Gamers-Predator-de-Acer-con-tecnolog%C3%ADa-Quantum-Dot-Curvo-G-Sync-144-Hz-HDR-y-4k.jpg",
                    "Monitor 27 pulgadas, 144Hz, resolución QHD",
                    4.6
            );

            Producto p6 = new Producto(
                    null,
                    "Mousepad XL RGB",
                    19990L,
                    "https://i.etsystatic.com/40866174/r/il/09ee67/5156855587/il_1080xN.5156855587_c781.jpg",
                    "Mousepad extra grande con iluminación RGB",
                    4.2
            );

            productoRepository.saveAll(List.of(p1, p2, p3, p4, p5, p6));

            System.out.println("✅ Datos iniciales cargados correctamente.");
        };
    }
}
