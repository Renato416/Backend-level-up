package com.backendLevelup.Backend.init;

import com.backendLevelup.Backend.model.*;
import com.backendLevelup.Backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder; // <--- IMPORTANTE: Nuevo import

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            UsuarioRepository usuarioRepo,
            CategoriaRepository categoriaRepo,
            ProductoRepository productoRepo,
            CarritoRepository carritoRepo,
            BoletaRepository boletaRepo,
            BoletaDetalleRepository boletaDetalleRepo,
            PasswordEncoder passwordEncoder // <--- IMPORTANTE: Inyectamos el codificador aquí
    ) {
        return args -> {

            // NOTA: Si ya tienes datos, esto evitará que se actualicen las contraseñas.
            // Para que funcione, debes borrar la BD o usar 'create-drop' una vez (ver instrucciones abajo).
            if (usuarioRepo.count() > 0 || productoRepo.count() > 0) {
                System.out.println("➡ Datos ya existentes. No se vuelven a cargar.");
                return;
            }

            System.out.println("🔰 Cargando datos iniciales (Con contraseñas encriptadas)...");

            // =====================================================
            // 1) USUARIOS (Ahora usamos passwordEncoder.encode)
            // =====================================================

            Usuario u1 = new Usuario(null, "Juan Pérez", "Calle 123",
                    LocalDate.of(1990,5,10),
                    passwordEncoder.encode("1234"), // <--- Encriptado
                    "1-9", "admin", "juan@duocuc.cl");

            Usuario u2 = new Usuario(null, "María González", "Av. Siempre Viva",
                    LocalDate.of(1995,2,15),
                    passwordEncoder.encode("abcd"), // <--- Encriptado
                    "2-7", "cliente", "maria@gmail.com");

            Usuario u3 = new Usuario(null, "Renato (Profesor)", "Dirección X",
                    LocalDate.of(1987,8,18),
                    passwordEncoder.encode("11111111"), // <--- Encriptado
                    "3-4", "admin", "re.rojasc@profesorduoc.cl");

            Usuario u4 = new Usuario(null, "Renato (Duoc)", "Dirección Y",
                    LocalDate.of(1987,8,18),
                    passwordEncoder.encode("11111111"), // <--- Encriptado
                    "4-4", "admin", "re.rojasc@duocuc.cl");

            Usuario u5 = new Usuario(null, "Renato (Gmail)", "Dirección Z",
                    LocalDate.of(1987,8,18),
                    passwordEncoder.encode("11111111"), // <--- Encriptado
                    "5-5", "cliente", "re.rojasc@gmail.cl");

            usuarioRepo.saveAll(List.of(u1, u2, u3, u4, u5));

            // =====================================================
            // 2) CATEGORÍAS
            // =====================================================
            Categoria accesorios = new Categoria(null, "Accesorios", null);
            Categoria muebles = new Categoria(null, "Muebles", null);
            Categoria consolas = new Categoria(null, "Consolas", null);
            Categoria monitores = new Categoria(null, "Monitores", null);
            Categoria perifericos = new Categoria(null, "Periféricos", null);

            categoriaRepo.saveAll(List.of(
                    accesorios, muebles, consolas, monitores, perifericos
            ));

            // =====================================================
            // 3) PRODUCTOS (del frontend)
            // =====================================================
            List<Producto> productos = List.of(
                    new Producto(null, "Audífonos GAMER", 64990L, 10L, accesorios, "https://res.cloudinary.com/dp5kdxr1l/image/upload/v1764122045/audifonos_p3ke0c.jpg"),
                    new Producto(null, "Silla GAMER", 72990L, 10L, muebles, "https://res.cloudinary.com/dp5kdxr1l/image/upload/v1764122304/silla_wscwan.jpg"),
                    new Producto(null, "Escritorio GAMER", 70990L, 10L, muebles, "https://res.cloudinary.com/dp5kdxr1l/image/upload/v1764122111/Escritorio_mq4zek.webp"),
                    new Producto(null, "Mando Xbox GAMER", 79990L, 10L, consolas, "https://res.cloudinary.com/dp5kdxr1l/image/upload/v1764122153/Mando_qudyog.webp"),
                    new Producto(null, "Mouse GAMER", 28990L, 10L, accesorios, "https://res.cloudinary.com/dp5kdxr1l/image/upload/v1764122185/Mause_z8shyu.webp"),
                    new Producto(null, "Mousepad GAMER", 6990L, 10L, accesorios, "https://res.cloudinary.com/dp5kdxr1l/image/upload/v1764122236/mausepad_hlyvbg.avif"),
                    new Producto(null, "Monitor GAMER", 134990L, 10L, monitores, "https://res.cloudinary.com/dp5kdxr1l/image/upload/v1764122277/monitor_nbtaox.jpg"),
                    new Producto(null, "Teclado GAMER", 15990L, 10L, perifericos, "https://res.cloudinary.com/dp5kdxr1l/image/upload/v1764122370/Teclado_g1d1km.webp")
            );

            productoRepo.saveAll(productos);

            // =====================================================
            // 4) Crear carritos vacíos para cada usuario
            // =====================================================
            carritoRepo.save(new Carrito(null, u1, List.of()));
            carritoRepo.save(new Carrito(null, u2, List.of()));
            carritoRepo.save(new Carrito(null, u3, List.of()));
            carritoRepo.save(new Carrito(null, u4, List.of()));
            carritoRepo.save(new Carrito(null, u5, List.of()));

            // =====================================================
            // 5) Crear una boleta inicial
            // =====================================================
            Boleta boleta = new Boleta(
                    null,
                    u1,
                    OffsetDateTime.now(),
                    149990L,
                    List.of()
            );

            boletaRepo.save(boleta);

            System.out.println("✔ Datos iniciales cargados correctamente.");
        };
    }
}